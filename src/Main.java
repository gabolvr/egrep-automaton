import java.util.Scanner;

import Automaton.*;

public class Main {
    public static void main(String args[]) {
        String pattern;
        String filePath;

        if (args.length != 2) {
            System.out.println("Usage: egrep-automaton [pattern] [file ...]");
            System.exit(0);
        }

        // Scanner scanner = new Scanner(System.in);
        // System.out.print("regEx: ");
        // pattern = scanner.nextLine();
        // System.out.print("File path: ");
        // filePath = scanner.nextLine();
        // scanner.close();

        pattern = args[0];
        filePath = args[1];

        Solve automaton = new Solve(pattern);
//        System.out.println(automaton);
        
        System.out.println("Bye");
      }
}
