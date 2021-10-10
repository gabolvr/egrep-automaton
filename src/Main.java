// import java.util.Scanner;

import Egrep.StringMatching;

public class Main {
    public static void main(String args[]) {
        String pattern;
        String filePath;

        if (args.length != 2) {
            System.out.println("Usage: egrep-automaton [pattern] [file ...]");
            System.exit(0);
        }

        pattern = args[0];
        filePath = args[1];

        // Scanner scanner = new Scanner(System.in);
        // System.out.print("regEx: ");
        // pattern = scanner.nextLine();
        // System.out.print("File path: ");
        // filePath = scanner.nextLine();
        // scanner.close();

        // pattern = "(egrep)";
        // filePath = "src/Tests/56667-0.txt";
        // filePath = "src/Tests/manual.txt";

        boolean kmp = false;

        StringMatching solve = new StringMatching(pattern, filePath);
        solve.match();
      }
}
