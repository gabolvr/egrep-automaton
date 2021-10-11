import Egrep.StringMatching;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class PerformanceTest {
    private static final String[] patterns = {"S(a|g|r)*on", "Sargon"};
    private static final String textPath = "tests/babylon.txt";

    private static void runTests(String pattern) {
        long start, end;
        try {
            System.out.println("--- egrep ---");
            ProcessBuilder processBuilder = new ProcessBuilder();
            start = System.currentTimeMillis();
            processBuilder.command("egrep", pattern, textPath);
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();
            while (line != null) {
                System.out.println(line);
                line = reader.readLine();
            }
            process.waitFor();
            end = System.currentTimeMillis();
            System.out.println("egrep: " + (end - start));
            System.out.println();

            System.out.println("--- DFA/KMP ---");
            start = System.currentTimeMillis();
            StringMatching patterRecognition = new StringMatching(pattern, textPath);
            patterRecognition.match(false);
            end = System.currentTimeMillis();
            System.out.println("Automaton : " + (end - start));
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        for (String pattern : patterns) {
            runTests(pattern);
        }
    }
}
