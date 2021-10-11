import Egrep.StringMatching;

import java.io.*;

public class PerformanceTest {
    private static final String[] patterns = {"S(a|g|r)*on", "Sargon"};
    private static final String textPath = "tests/babylon.txt";
    private static final String egrepComparisonPath = "resultEgrep.dat";
    private static final String KMPxDFAComparisonPath = "resultKMPxDFA.dat";
    private static BufferedWriter br;

    private static void runTestEgrep() {
        long start, end;
        for (String pattern : patterns) {
            try {
                br.write(pattern + " ");

                // egrep
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
                br.write((end - start) + " ");
                System.out.println();

                System.out.println("--- DFA/KMP ---");
                start = System.currentTimeMillis();
                StringMatching patterRecognition = new StringMatching(pattern, textPath);
                patterRecognition.match(false);
                end = System.currentTimeMillis();
                br.write((end - start) + "");
                br.newLine();
                System.out.println();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void runTestKMPxDFA(String pattern) {
        long start, end;
        try {
            br.write(pattern + " ");

            // DFA
            start = System.currentTimeMillis();
            StringMatching patterRecognitionDFA = new StringMatching(pattern, textPath);
            patterRecognitionDFA.match(true);
            end = System.currentTimeMillis();
            br.write((end - start) + " ");
            System.out.println();

            // KMP
            start = System.currentTimeMillis();
            StringMatching patterRecognitionKMP = new StringMatching(pattern, textPath);
            patterRecognitionKMP.match(false);
            end = System.currentTimeMillis();
            br.write((end - start) + "");
            br.newLine();
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            br = new BufferedWriter(new FileWriter(new File(egrepComparisonPath)));
            br.write("pattern egrep DFA\n");
            runTestEgrep();
            br.close();

            br = new BufferedWriter(new FileWriter(new File(KMPxDFAComparisonPath)));
            br.write("pattern DFA KMP\n");
            runTestKMPxDFA(patterns[1]);
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
