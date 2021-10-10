package Egrep;

import Text.Text;

public class EgrepKMP implements Egrep {
    private String pattern;
    private int[][] dfa;

    public EgrepKMP(String regEx) {
        pattern = regEx;
        int M = regEx.length();
        int R = 256; // ASCII
        dfa = new int[R][M];
        dfa[regEx.charAt(0)][0] = 1;
        for (int X = 0, j = 1; j < M; ++j) {
            for (int i = 0; i < R; ++i)
                dfa[i][j] = dfa[i][X];
            dfa[regEx.charAt(j)][j] = j + 1;
            X = dfa[regEx.charAt(j)][X];
        }
    }

    @Override
    public void findPatternInText(Text text) {
        for (String line : text.getText()) {
            int i, j, N = line.length(), M = pattern.length();
            for (i = 0, j = 0; i < N && j < M; ++i) {
                j = dfa[line.charAt(i)][j];
            }
            if (j == M)
                System.out.println(line);
        }
    }
}
