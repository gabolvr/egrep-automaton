package KMP;

import Text.Text;

public class KMP {
    private String pattern;
    private int[][] dfa;

    public KMP(String regEx){
        pattern = regEx;
        int M = regEx.length();
        int R = 256;
        dfa = new int[R][M];
        dfa[regEx.charAt(0)][0] = 1;
        for (int X=0, j=1; j<M; ++j){
            for (int i=0; i<R; ++i)
                dfa[i][j] = dfa[i][X];
            dfa[regEx.charAt(j)][j] = j+1;
            X = dfa[regEx.charAt(j)][X];
        }
    }

    public void search(Text text){
        for (int lineIndex=0; lineIndex<text.getSize(); ++lineIndex){
            String line = text.getLine(lineIndex);
            int i, j, N = line.length(), M = pattern.length();
            for (i=0, j=0; i<N && j<M; ++i){
                j = dfa[line.charAt(i)][j];
            }
            if (j == M)
                System.out.println(line);
        }
    }
}
