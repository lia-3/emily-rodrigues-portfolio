import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.Trees; 
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        if(args.length < 1){
            System.err.println("Uso: java Main <archivo_entrada>");
            System.exit(1);
        }

        String inputFilePath = args[0]; 

        System.out.println("Intentando leer el archivo de entrada: " + inputFilePath);

        CharStream input = CharStreams.fromFileName(inputFilePath);
        SimpleClassModelWithConstraintsLexer lexer = new SimpleClassModelWithConstraintsLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        SimpleClassModelWithConstraintsParser parser = new SimpleClassModelWithConstraintsParser(tokens);
        ParseTree tree = parser.model();


        SemanticChecker checker = new SemanticChecker();
        checker.check(tree);

        if (checker.hasErrors()) {
            System.err.println("Se encontraron errores semánticos. No se genera código.");
            System.exit(1);
        }

        JavaCodeGenerator generator = new JavaCodeGenerator();
        generator.visit(tree); 
        System.out.println("Código generado exitosamente en src/ModeloGenerado.java");
    }
}