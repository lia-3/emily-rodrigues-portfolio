import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode; 

import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class SemanticChecker extends SimpleClassModelWithConstraintsBaseListener {

    private Set<String> definedTypes;
    private Set<String> classes;
    private Set<String> datatypes;
    private Set<String> enumerations;
    private boolean hasErrors = false;
    private Map<String, Map<String, String>> classOperations;


    public SemanticChecker() {
        this.definedTypes = new HashSet<>(Arrays.asList("Boolean", "Integer", "Real", "String")); 
        this.classes = new HashSet<>();
        this.datatypes = new HashSet<>();
        this.enumerations = new HashSet<>();
        this.classOperations = new HashMap<>();     }


    public void check(ParseTree tree) {
        ParseTreeWalker walker = new ParseTreeWalker();

        SymbolCollectorListener collector = new SymbolCollectorListener(
            this.definedTypes, 
            this.classes,
            this.datatypes,
            this.enumerations,
            this.classOperations 
        );
        walker.walk(collector, tree);

        walker.walk(this, tree);
    }


    private class SymbolCollectorListener extends SimpleClassModelWithConstraintsBaseListener {

        private Set<String> definedTypesRef;
        private Set<String> classesRef;
        private Set<String> datatypesRef;
        private Set<String> enumerationsRef;
        private Map<String, Map<String, String>> classOperationsRef;

        public SymbolCollectorListener(Set<String> definedTypes, Set<String> classes, Set<String> datatypes, Set<String> enumerations, Map<String, Map<String, String>> classOperations) {
            this.definedTypesRef = definedTypes;
            this.classesRef = classes;
            this.datatypesRef = datatypes;
            this.enumerationsRef = enumerations;
            this.classOperationsRef = classOperations;
        }

        @Override
        public void enterClassDefinition(SimpleClassModelWithConstraintsParser.ClassDefinitionContext ctx) {
            String className = ctx.identifier(0).getText();
            classesRef.add(className);
            definedTypesRef.add(className);
            classOperationsRef.putIfAbsent(className, new HashMap<>());
        }

        @Override
        public void enterDatatypeDefinition(SimpleClassModelWithConstraintsParser.DatatypeDefinitionContext ctx) {
            String datatypeName = ctx.identifier().getText();
            datatypesRef.add(datatypeName);
            definedTypesRef.add(datatypeName);
            classOperationsRef.putIfAbsent(datatypeName, new HashMap<>()); 
        }

        @Override
        public void enterEnumeration(SimpleClassModelWithConstraintsParser.EnumerationContext ctx) {
            String enumName = ctx.identifier().getText();
            enumerationsRef.add(enumName);
            definedTypesRef.add(enumName);
        }

        @Override
        public void enterOperationDefinition(SimpleClassModelWithConstraintsParser.OperationDefinitionContext ctx) {
            String currentContextName = null;
            ParseTree parent = ctx.getParent();
            while (parent != null) {
                if (parent instanceof SimpleClassModelWithConstraintsParser.ClassDefinitionContext) {
                    currentContextName = ((SimpleClassModelWithConstraintsParser.ClassDefinitionContext) parent).identifier(0).getText();
                    break;
                } else if (parent instanceof SimpleClassModelWithConstraintsParser.DatatypeDefinitionContext) {
                    currentContextName = ((SimpleClassModelWithConstraintsParser.DatatypeDefinitionContext) parent).identifier().getText();
                    break;
                }
                parent = parent.getParent();
            }

            if (currentContextName != null) {
                String opName = ctx.identifier().getText();
                String returnType = ctx.type().getText(); 

                StringBuilder paramSignature = new StringBuilder();
                if (ctx.parameterDeclarations() != null) {
                    List<SimpleClassModelWithConstraintsParser.ParameterDeclarationContext> params =
                        ctx.parameterDeclarations().parameterDeclaration();
                    for (int i = 0; i < params.size(); i++) {
                        paramSignature.append(params.get(i).type().getText()); 
                        if (i < params.size() - 1) {
                            paramSignature.append(",");
                        }
                    }
                }
                String operationSignature = opName + "(" + paramSignature.toString() + ")";
                classOperationsRef.get(currentContextName).put(operationSignature, returnType);
            }
        }
    }

    private boolean isValidMultiplicity(String mult) {
        return mult.matches("\\*|0\\.\\.\\*|1\\.\\.\\*|\\d+|\\d+\\.\\.\\d+|\\d+\\.\\.\\*");
    }

    @Override
    public void enterAttributeDefinition(SimpleClassModelWithConstraintsParser.AttributeDefinitionContext ctx) {
        String type = ctx.type().getText();
        if (!definedTypes.contains(type)) { 
            int line = ctx.type().getStart().getLine();
            System.err.printf("Error UML.1 en línea %d: Tipo no definido '%s' para atributo '%s'.%n", line, type, ctx.identifier().getText());
            hasErrors = true;
        }
    }

    @Override
    public void enterParameterDeclaration(SimpleClassModelWithConstraintsParser.ParameterDeclarationContext ctx) {
        String type = ctx.type().getText();
        if (!definedTypes.contains(type)) { 
            int line = ctx.type().getStart().getLine();
            System.err.printf("Error UML.2/UML.3 en línea %d: Tipo no definido '%s' para parámetro '%s'.%n", line, type, ctx.identifier().getText());
            hasErrors = true;
        }
    }

    @Override
    public void enterOperationDefinition(SimpleClassModelWithConstraintsParser.OperationDefinitionContext ctx) {
        String returnType = ctx.type().getText();
        if (!returnType.equals("void") && !definedTypes.contains(returnType)) {
            int line = ctx.type().getStart().getLine();
            System.err.printf("Error UML.2/UML.3 en línea %d: Tipo de retorno no definido '%s' para operación '%s'.%n", line, returnType, ctx.identifier().getText());
            hasErrors = true;
        }
    }

    @Override
    public void enterAssociation(SimpleClassModelWithConstraintsParser.AssociationContext ctx) {
        String targetA = ctx.associationEndA.identifier(0).getText();
        int lineA = ctx.associationEndA.identifier(0).getStart().getLine();
        if (!classes.contains(targetA)) { 
            System.err.printf("Error UML.4 en línea %d: El target '%s' no es una clase definida en el modelo.%n", lineA, targetA);
            hasErrors = true;
        }

        String targetB = ctx.associationEndB.identifier(0).getText();
        int lineB = ctx.associationEndB.identifier(0).getStart().getLine();
        if (!classes.contains(targetB)) { 
            System.err.printf("Error UML.4 en línea %d: El target '%s' no es una clase definida en el modelo.%n", lineB, targetB);
            hasErrors = true;
        }

        checkMultiplicityConstraints(ctx.associationEndA, lineA);
        checkMultiplicityConstraints(ctx.associationEndB, lineB);

        if (ctx.associationClass() != null) {
            for (SimpleClassModelWithConstraintsParser.ClassBodyElementContext elementCtx : ctx.associationClass().classBody().classBodyElement()) {
                if (elementCtx.attributeDefinition() != null) {
                    SimpleClassModelWithConstraintsParser.AttributeDefinitionContext attrCtx = elementCtx.attributeDefinition();
                    String type = attrCtx.type().getText();
                    if (!definedTypes.contains(type)) {
                        int line = attrCtx.type().getStart().getLine();
                        System.err.printf("Error UML.5 (UML.1) en línea %d: Tipo no definido '%s' en clase de asociación para atributo '%s'.%n", line, type, attrCtx.identifier().getText());
                        hasErrors = true;
                    }
                }
            }
        }
    }

    private void checkMultiplicityConstraints(SimpleClassModelWithConstraintsParser.AssociationEndContext endCtx, int line) {
        String multiplicity = (endCtx.multiplicity() != null) ? endCtx.multiplicity().getText() : "1";
        String constraints = (endCtx.constraints() != null) ? endCtx.constraints().getText() : "";

        boolean isSingleMultiplicity = multiplicity.equals("1") || multiplicity.equals("0..1"); 
        boolean hasUnique = constraints.contains("unique");
        boolean hasOrdered = constraints.contains("ordered"); 

        if (isSingleMultiplicity && (hasUnique || hasOrdered)) {
            System.err.printf("Error UML.6 en línea %d: Las restricciones 'unique' o 'ordered' no son correctas para multiplicidad '%s'.%n", line, multiplicity);
            hasErrors = true;
        }
    }


    @Override
    public void enterInvariantContext(SimpleClassModelWithConstraintsParser.InvariantContextContext ctx) {
        String className = ctx.ID().getText();
        int line = ctx.ID().getSymbol().getLine(); 

        if (!classes.contains(className)) { 
            System.err.printf("Error OCL.1 en línea %d: La clase '%s' en el contexto invariante no está definida.%n", line, className);
            hasErrors = true;
        }
    }

    @Override
    public void enterPrepostContext(SimpleClassModelWithConstraintsParser.PrepostContextContext ctx) {
        String className = ctx.ID().getText();
        int line = ctx.ID().getSymbol().getLine();

        if (!classes.contains(className)) {
            System.err.printf("Error OCL.2 en línea %d: La clase '%s' en el contexto pre/post no está definida.%n", line, className);
            hasErrors = true;
            return; 
        }

        String declaredOpName = ctx.operationDeclaration().identifier().getText();
        String declaredOclReturnType = ctx.operationDeclaration().ocltype().getText(); 

        StringBuilder declaredParamSignature = new StringBuilder();
        if (ctx.operationDeclaration().parameterDeclarations() != null) {
            List<SimpleClassModelWithConstraintsParser.ParameterDeclarationContext> params =
                ctx.operationDeclaration().parameterDeclarations().parameterDeclaration();
            for (int i = 0; i < params.size(); i++) {
                declaredParamSignature.append(params.get(i).type().getText()); 
                if (i < params.size() - 1) {
                    declaredParamSignature.append(",");
                }
            }
        }
        String fullDeclaredSignature = declaredOpName + "(" + declaredParamSignature.toString() + ")";

        Map<String, String> opsInClass = classOperations.get(className);

        if (opsInClass == null || !opsInClass.containsKey(fullDeclaredSignature)) {
            System.err.printf("Error OCL.2 en línea %d: Operación '%s' con firma '%s' no definida en la clase '%s'.%n",
                            line, declaredOpName, fullDeclaredSignature, className);
            hasErrors = true;
        } else {
            String actualUmlReturnType = opsInClass.get(fullDeclaredSignature);
            if (!actualUmlReturnType.equals(declaredOclReturnType)) {
                 System.err.printf("Error OCL.2 en línea %d: El tipo de retorno de la operación '%s' en la clase '%s' no coincide. Esperado OCL tipo '%s', encontrado UML tipo '%s'.%n",
                            line, declaredOpName, className, declaredOclReturnType, actualUmlReturnType);
                hasErrors = true;
            }
        }
    }


    public boolean hasErrors() {
        return hasErrors;
    }

    private void reportError(String message) {
        hasErrors = true;
        System.err.println(message);
    }
}