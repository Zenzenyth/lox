package lox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class lox {
    static boolean hadError = false;

    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.out.println("Usage: jlox [script]");
            System.exit(64); //Following the UNIX sysexit.h 64: means the command was invoked incorrectly
        } else if (args.length == 1) {
            //TODO: ADD extension validation.
            runFile(args[0]);
        }
        else {
            runPrompt();
        }
    }

    private static void runFile(String path) throws IOException{
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));

        //Indicate an error in exit code.
        if (hadError) System.exit(65);
    }

    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        for (;;) {
            System.out.print("> ");
            String line = reader.readLine();
            if (line == null) break;
            run(line);
            hadError = false;
        }
    }

    private static void run(String source) {
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();
        System.out.println("-------------------------------------" );
        System.out.println("TOKEN TYPE      LEXEME   LITERAL VALUE" );
        System.out.println("-------------------------------------" );

        for (Token token : tokens) {
            String formattedLine = String.format("%-15s %-10s %s", token.type, token.lexeme, token.literal);
            System.out.println(formattedLine);
        }
    }

    static void error(int line, String message) {
        report(line, "", message);
    }

    static void report(int line, String where, String message) {
        System.err.println(
                "[line" + line + "] Error" + where + ": " + message);
        hadError = true;
    }
}
