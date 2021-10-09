import Automaton.*;

public class Main {
    public static void main(String args[]) {
        String pattern;
        String filePath;

        if (args.length != 2) {
            System.out.print("Usage: egrep-automaton [pattern] [file ...]");
            System.exit(0);
        }

        pattern = args[0];
        filePath = args[1];

        Solve automaton = new Solve(pattern);
        System.out.println(automaton);
        
        System.out.println("Bye");
      }
}
