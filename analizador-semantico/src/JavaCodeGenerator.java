import java.util.Map;
import java.util.HashMap;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;
import java.util.HashSet;
import java.util.AbstractCollection;
import java.util.AbstractSequentialList;
import java.util.LinkedList;

import java.io.FileWriter;
import java.io.IOException;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;


public class JavaCodeGenerator extends SimpleClassModelWithConstraintsBaseVisitor<Void> {

    private Map<String, StringBuilder> classCodeBuilders = new HashMap<>();
    private Stack<String> currentContextNameStack = new Stack<>();
    private StringBuilder finalOutput = new StringBuilder();

    private Map<String, Integer> anonymousInvariantCounters = new HashMap<>();

    private Map<String, List<String>> classInvariantsMethods = new HashMap<>();

    private Map<String, Map<String, String>> operationPrePostConditions = new HashMap<>();


    private Map<String, String> umlToJavaTypeMap = new HashMap<>();
    private Map<String, String> defaultValues = new HashMap<>();

    private Set<String> enumNames = new HashSet<>();
    private Set<String> associationClassNames = new HashSet<>(); // Keep track of association classes

    public JavaCodeGenerator() {
        umlToJavaTypeMap.put("Boolean", "boolean");
        umlToJavaTypeMap.put("Integer", "int");
        umlToJavaTypeMap.put("Real", "float");
        umlToJavaTypeMap.put("String", "String");

        defaultValues.put("boolean", "false");
        defaultValues.put("int", "0");
        defaultValues.put("float", "0.0f");
        defaultValues.put("String", "null");
        defaultValues.put("void", "");
    }

    private String mapToJavaType(String umlType) {
        return umlToJavaTypeMap.getOrDefault(umlType, umlType);
    }

    private String getDefaultValue(String javaType) {
        return defaultValues.getOrDefault(javaType, "null");
    }

    @Override
    public Void visitModel(SimpleClassModelWithConstraintsParser.ModelContext ctx) {
        visit(ctx.classModelSpecification());

        visit(ctx.multipleContextSpecifications());

        // Now, append the final closing brace for classes/datatypes that are not enums or association classes
        for (String name : classCodeBuilders.keySet()) {
            StringBuilder classCode = classCodeBuilders.get(name);
            if (!enumNames.contains(name) && !associationClassNames.contains(name)) {
                // Only generate constructor if it's a regular class/datatype and not an enum or assoc class (assoc classes have simple constructor)
                generateConstructor(name, classCode);
                classCode.append("}\n"); // Add the final closing brace for these
            }
            // Enums are already closed in visitEnumeration
            // Association classes are already closed in visitAssociation
        }


        finalOutput.append("import java.util.Set;\n");
        finalOutput.append("import java.util.HashSet;\n");
        finalOutput.append("import java.util.SortedSet;\n");
        finalOutput.append("import java.util.TreeSet;\n");
        finalOutput.append("import java.util.List;\n");
        finalOutput.append("import java.util.ArrayList;\n");
        finalOutput.append("import java.util.AbstractCollection;\n");
        finalOutput.append("import java.util.AbstractSequentialList;\n");
        finalOutput.append("import java.util.LinkedList;\n\n");

        finalOutput.append("public class ModeloGenerado {\n\n");


        for (String name : classCodeBuilders.keySet()) {
            String[] lines = classCodeBuilders.get(name).toString().split("\n");
            for (String line : lines) {
                finalOutput.append("    ").append(line).append("\n");
            }
            finalOutput.append("\n");
        }

        finalOutput.append("}\n");
        try (FileWriter writer = new FileWriter("src/ModeloGenerado.java")) {
            writer.write(finalOutput.toString());
        } catch (IOException e) {
            System.err.println("Error al escribir el archivo ModeloGenerado.java: " + e.getMessage());
        }

        return null;
    }

    @Override
    public Void visitClassDefinition(SimpleClassModelWithConstraintsParser.ClassDefinitionContext ctx) {
        String className = ctx.identifier(0).getText();
        currentContextNameStack.push(className);

        classCodeBuilders.putIfAbsent(className, new StringBuilder());
        StringBuilder classCode = classCodeBuilders.get(className);

        classCode.append("class ").append(className);
        if (ctx.identifier().size() > 1) {
            classCode.append(" extends ").append(ctx.identifier(1).getText());
        }
        if (ctx.idList() != null) {
            classCode.append(" implements ");
            List<SimpleClassModelWithConstraintsParser.IdentifierContext> idList = ctx.idList().identifier();
            for (int i = 0; i < idList.size(); i++) {
                classCode.append(idList.get(i).getText());
                if (i < idList.size() - 1) {
                    classCode.append(", ");
                }
            }
        }
        classCode.append(" {\n");

        visitChildren(ctx);

        currentContextNameStack.pop();
        return null;
    }

    @Override
    public Void visitDatatypeDefinition(SimpleClassModelWithConstraintsParser.DatatypeDefinitionContext ctx) {
        String datatypeName = ctx.identifier().getText();
        currentContextNameStack.push(datatypeName);

        classCodeBuilders.putIfAbsent(datatypeName, new StringBuilder());
        StringBuilder datatypeCode = classCodeBuilders.get(datatypeName);

        datatypeCode.append("class ").append(datatypeName).append(" {\n");

        List<SimpleClassModelWithConstraintsParser.DatatypeBodyElementContext> bodyElements = ctx.datatypeBodyElement();
        if (bodyElements != null) {
            for (SimpleClassModelWithConstraintsParser.DatatypeBodyElementContext elementCtx : bodyElements) {
                visit(elementCtx);
            }
        }

        currentContextNameStack.pop();
        return null;
    }

    @Override
    public Void visitAttributeDefinition(SimpleClassModelWithConstraintsParser.AttributeDefinitionContext ctx) {
        if (currentContextNameStack.isEmpty()) {
            System.err.println("Error del generador: Atributo '" + ctx.identifier().getText() + "' encontrado fuera de un contexto de clase/datatype.");
            return null;
        }

        String currentContext = currentContextNameStack.peek();
        StringBuilder codeBuilder = classCodeBuilders.get(currentContext);

        String umlType = ctx.type().getText();
        String javaType = mapToJavaType(umlType);
        String attributeName = ctx.identifier().getText();

        codeBuilder.append("    private ").append(javaType).append(" ").append(attributeName).append(";\n");

        return null;
    }

    @Override
    public Void visitDatatypeBodyElement(SimpleClassModelWithConstraintsParser.DatatypeBodyElementContext ctx) {
        if (currentContextNameStack.isEmpty()) {
            System.err.println("Error del generador: Elemento de cuerpo de datatype encontrado fuera de un contexto de datatype.");
            return null;
        }

        String currentContext = currentContextNameStack.peek();
        StringBuilder codeBuilder = classCodeBuilders.get(currentContext);

        if (ctx.identifier() != null && ctx.type() != null) {
            String umlType = ctx.type().getText();
            String javaType = mapToJavaType(umlType);
            String fieldName = ctx.identifier().getText();
            codeBuilder.append("    private ").append(javaType).append(" ").append(fieldName).append(";\n");
        } else if (ctx.operationDefinition() != null) {
            visit(ctx.operationDefinition());
        }
        return null;
    }


    @Override
    public Void visitEnumeration(SimpleClassModelWithConstraintsParser.EnumerationContext ctx) {
        String enumName = ctx.identifier().getText();
        enumNames.add(enumName);
        StringBuilder enumCode = new StringBuilder();
        enumCode.append("enum ").append(enumName).append(" {\n");

        List<SimpleClassModelWithConstraintsParser.EnumerationLiteralContext> literals = ctx.enumerationLiteral();
        for (int i = 0; i < literals.size(); i++) {
            enumCode.append("    ").append(literals.get(i).identifier().getText());
            if (i < literals.size() - 1) {
                enumCode.append(",");
            }
            enumCode.append("\n");
        }
        enumCode.append("}\n"); // This closes the enum correctly.

        classCodeBuilders.put(enumName, enumCode);
        return null;
    }

    @Override
    public Void visitOperationDefinition(SimpleClassModelWithConstraintsParser.OperationDefinitionContext ctx) {
        if (currentContextNameStack.isEmpty()) {
            System.err.println("Error del generador: Operación '" + ctx.identifier().getText() + "' encontrada fuera de un contexto de clase/datatype.");
            return null;
        }

        String currentContext = currentContextNameStack.peek();
        StringBuilder classCode = classCodeBuilders.get(currentContext);

        String opName = ctx.identifier().getText();
        String returnTypeUML = ctx.type().getText();
        String returnTypeJava = mapToJavaType(returnTypeUML);

        StringBuilder parameters = new StringBuilder();
        List<String> paramTypesForSignature = new ArrayList<>();
        if (ctx.parameterDeclarations() != null) {
            List<SimpleClassModelWithConstraintsParser.ParameterDeclarationContext> params =
                ctx.parameterDeclarations().parameterDeclaration();

            for (int i = 0; i < params.size(); i++) {
                String paramTypeUML = params.get(i).type().getText();
                String paramJavaType = mapToJavaType(paramTypeUML);
                String paramName = params.get(i).identifier().getText();
                parameters.append(paramJavaType).append(" ").append(paramName);
                paramTypesForSignature.add(paramTypeUML);
                if (i < params.size() - 1) {
                    parameters.append(", ");
                }
            }
        }

        String operationSignatureKey = opName + "(" + String.join(",", paramTypesForSignature) + ")";
        String fullOperationIdentifier = currentContext + "::" + operationSignatureKey;

        operationPrePostConditions.putIfAbsent(fullOperationIdentifier, new HashMap<>());

        classCode.append("\n    public ").append(returnTypeJava).append(" ").append(opName)
                 .append("(").append(parameters.toString()).append(") {\n");

        if (operationPrePostConditions.containsKey(fullOperationIdentifier)) {
            Map<String, String> conditions = operationPrePostConditions.get(fullOperationIdentifier);
            if (conditions.containsKey("pre")) {
                classCode.append("        // pre: ").append(conditions.get("pre")).append("\n");
            }
            classCode.append("        // TODO\n");
            if (conditions.containsKey("post")) {
                classCode.append("        // post: ").append(conditions.get("post")).append("\n");
            }
        } else {
            classCode.append("        // TODO\n");
        }

        if (!returnTypeJava.equals("void")) {
            classCode.append("        return ").append(getDefaultValue(returnTypeJava)).append(";\n");
        }
        classCode.append("    }\n");
        return null;
    }

    @Override
    public Void visitAssociation(SimpleClassModelWithConstraintsParser.AssociationContext ctx) {
        String classA = ctx.associationEndA.identifier().get(0).getText();
        String roleA = ctx.associationEndA.identifier().get(1).getText();

        String classB = ctx.associationEndB.identifier().get(0).getText();
        String roleB = ctx.associationEndB.identifier().get(1).getText();

        String multiplicityA = (ctx.associationEndA.multiplicity() != null) ? ctx.associationEndA.multiplicity().getText() : "1";
        String constraintsA = (ctx.associationEndA.constraints() != null) ? ctx.associationEndA.constraints().getText() : "";

        String multiplicityB = (ctx.associationEndB.multiplicity() != null) ? ctx.associationEndB.multiplicity().getText() : "1";
        String constraintsB = (ctx.associationEndB.constraints() != null) ? ctx.associationEndB.constraints().getText() : "";

        if (classCodeBuilders.containsKey(classA)) {
            StringBuilder classCodeA = classCodeBuilders.get(classA);
            String javaTypeForB = getJavaCollectionType(multiplicityB, constraintsB, classB);
            String javaImplementationForB = getJavaCollectionImplementation(multiplicityB, constraintsB);

            if (!javaImplementationForB.isEmpty()) {
                classCodeA.append("    private ").append(javaTypeForB).append(" ").append(roleB).append(" = ").append(javaImplementationForB).append(";\n");
            } else {
                classCodeA.append("    private ").append(javaTypeForB).append(" ").append(roleB).append(";\n");
            }
        } else {
            System.err.println("Error del generador: Clase '" + classA + "' en asociación no definida (no debería ocurrir si el SemanticChecker funciona).");
        }

        if (classCodeBuilders.containsKey(classB)) {
            StringBuilder classCodeB = classCodeBuilders.get(classB);
            String javaTypeForA = getJavaCollectionType(multiplicityA, constraintsA, classA);
            String javaImplementationForA = getJavaCollectionImplementation(multiplicityA, constraintsA);

            if (!javaImplementationForA.isEmpty()) {
                classCodeB.append("    private ").append(javaTypeForA).append(" ").append(roleA).append(" = ").append(javaImplementationForA).append(";\n");
            } else {
                classCodeB.append("    private ").append(javaTypeForA).append(" ").append(roleA).append(";\n");
            }
        } else {
            System.err.println("Error del generador: Clase '" + classB + "' en asociación no definida (no debería ocurrir si el SemanticChecker funciona).");
        }

        if (ctx.associationClass() != null) {
            String associationClassName = (ctx.associationName() != null) ? ctx.associationName().identifier().getText() : classA + "_" + classB + "_Association";
            associationClassNames.add(associationClassName); // Mark as association class
            currentContextNameStack.push(associationClassName);
            classCodeBuilders.putIfAbsent(associationClassName, new StringBuilder());
            StringBuilder assocClassCode = classCodeBuilders.get(associationClassName);


            assocClassCode.append("class ").append(associationClassName).append(" {\n");

            // Visit the body of the association class to add its attributes/operations
            visit(ctx.associationClass().classBody());

            assocClassCode.append("    private ").append(classA).append(" ").append(roleA).append("Instance;\n");
            assocClassCode.append("    private ").append(classB).append(" ").append(roleB).append("Instance;\n");

            generateConstructor(associationClassName, assocClassCode); // Generate constructor for association class
            assocClassCode.append("}\n"); // Close the association class here

            currentContextNameStack.pop();
        }

        return null;
    }

    private String getJavaCollectionType(String multiplicity, String constraints, String elementType) {
        boolean isSingle = multiplicity.equals("1") || multiplicity.equals("0..1");
        boolean isUnique = constraints.contains("unique");
        boolean isOrdered = constraints.contains("ordered");

        if (isSingle) {
            return elementType;
        }

        if (isUnique && isOrdered) {
            return "SortedSet<" + elementType + ">";
        } else if (isUnique) {
            return "Set<" + elementType + ">";
        } else if (isOrdered) {
            return "List<" + elementType + ">";
        } else {
            return "List<" + elementType + ">";
        }
    }

    private String getJavaCollectionImplementation(String multiplicity, String constraints) {
        boolean isSingle = multiplicity.equals("1") || multiplicity.equals("0..1");
        boolean isUnique = constraints.contains("unique");
        boolean isOrdered = constraints.contains("ordered");

        if (isSingle) {
            return "";
        }

        if (isUnique && isOrdered) {
            return "new TreeSet<>()";
        } else if (isUnique) {
            return "new HashSet<>()";
        } else if (isOrdered) {
            return "new LinkedList<>()";
        } else {
            return "new ArrayList<>()";
        }
    }

    @Override
    public Void visitMultipleContextSpecifications(SimpleClassModelWithConstraintsParser.MultipleContextSpecificationsContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Void visitInvariantContext(SimpleClassModelWithConstraintsParser.InvariantContextContext ctx) {
        String className = ctx.ID().getText();
        if (!classCodeBuilders.containsKey(className)) {
            System.err.println("Error del generador: Contexto invariante para clase no definida o visitada aún: " + className);
            return null;
        }
        StringBuilder classCode = classCodeBuilders.get(className);

        anonymousInvariantCounters.putIfAbsent(className, 0);

        for (SimpleClassModelWithConstraintsParser.InvariantContext invCtx : ctx.invariant()) {
            String methodName;
            String oclExpression = invCtx.expression().getText();

            if (invCtx.ID() != null) {
                methodName = "check" + capitalize(invCtx.ID().getText());
            } else {
                int counter = anonymousInvariantCounters.get(className);
                methodName = "checkInvariant" + (counter + 1);
                anonymousInvariantCounters.put(className, counter + 1);
            }

            classInvariantsMethods.putIfAbsent(className, new ArrayList<>());
            classInvariantsMethods.get(className).add(methodName);

            classCode.append("\n    private boolean ").append(methodName).append("() {\n");
            classCode.append("        return false;\n");
            classCode.append("    }\n");
        }
        return null;
    }

    @Override
    public Void visitPrepostContext(SimpleClassModelWithConstraintsParser.PrepostContextContext ctx) {
        String className = ctx.ID().getText();
        String opName = ctx.operationDeclaration().identifier().getText();

        StringBuilder paramTypesSignature = new StringBuilder();
        if (ctx.operationDeclaration().parameterDeclarations() != null) {
            List<SimpleClassModelWithConstraintsParser.ParameterDeclarationContext> params =
                ctx.operationDeclaration().parameterDeclarations().parameterDeclaration();

            for (int i = 0; i < params.size(); i++) {
                paramTypesSignature.append(params.get(i).type().getText());
                if (i < params.size() - 1) {
                    paramTypesSignature.append(",");
                }
            }
        }
        String operationSignatureKey = opName + "(" + paramTypesSignature.toString() + ")";
        String fullOperationIdentifier = className + "::" + operationSignatureKey;

        operationPrePostConditions.putIfAbsent(fullOperationIdentifier, new HashMap<>());

        for (SimpleClassModelWithConstraintsParser.PreconditionContext preCtx : ctx.prepostSpecification().precondition()) {
            operationPrePostConditions.get(fullOperationIdentifier).put("pre", preCtx.expression().getText());
        }
        for (SimpleClassModelWithConstraintsParser.PostconditionContext postCtx : ctx.prepostSpecification().postcondition()) {
            operationPrePostConditions.get(fullOperationIdentifier).put("post", postCtx.expression().getText());
        }
        return null;
    }

    private void generateConstructor(String className, StringBuilder classCode) {
        if (!enumNames.contains(className)) {
            if (!classCode.toString().contains("public " + className + "()")) {
                classCode.append("\n    public ").append(className).append("() {\n");

                List<String> invariants = classInvariantsMethods.get(className);
                if (invariants != null) {
                    for (String invMethod : invariants) {
                        classCode.append("        assert(").append(invMethod).append("());\n");
                    }
                }
                classCode.append("    }\n");
            }
        }
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}