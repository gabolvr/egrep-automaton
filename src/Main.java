// import java.util.Scanner;

import Egrep.StringMatching;

public class Main {
    public static void main(String args[]) {
        String pattern;
        String filePath;
        boolean DFA = false;

        if ((args.length != 3 && args.length != 2) || (args.length == 3 && !args[0].equals("-DFA"))) {
            System.out.println("Usage: egrep-automaton [-DFA] [pattern] [file ...]");
            System.out.println("     -DFA --> DFA only mode");
            System.exit(0);
        }

        if (args.length == 3 && args[0].equals("-DFA")){
            pattern = args[1];
            filePath = args[2];
            DFA = true;
        } else{
            pattern = args[0];
            filePath = args[1];
        }
        // Scanner scanner = new Scanner(System.in);
        // System.out.print("regEx: ");
        // pattern = scanner.nextLine();
        // System.out.print("File path: ");
        // filePath = scanner.nextLine();
        // scanner.close();

        // pattern = "(egrep)";
        // filePath = "src/Tests/56667-0.txt";
        // filePath = "src/Tests/manual.txt";


        StringMatching solve = new StringMatching(pattern, filePath);
        solve.match(DFA);
      }
}
