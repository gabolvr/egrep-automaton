package Egrep;

import Text.Text;

public class EgrepAutomaton {
    public DFAutomaton dfAutomaton;

    public EgrepAutomaton(DFAutomaton dfAutomaton) {
        this.dfAutomaton = dfAutomaton;
    }

    public void findPatternInText(Text text) {
        for (String line : text.getText()) {
            if (findPatternInLine(line)) {
                System.out.println(line);
            }
        }
    }

    public boolean findPatternInLine(String line) {
        for (int i = 0; i < line.length(); i++) {
            if (findPatternStartingAtLinePosition(line, i)) {
                return true;
            }
        }
        return false;
    }

    private boolean findPatternStartingAtLinePosition(String line, int linePos) {
        int automatonState = 0;
        while (!dfAutomaton.acceptingStates.contains(automatonState)) {
            if (line.charAt(linePos) >= 256 || dfAutomaton.transitions[automatonState][line.charAt(linePos)] == -1) {
                return false;
            }

            automatonState = dfAutomaton.transitions[automatonState][line.charAt(linePos)];
            linePos++;

            if (linePos >= line.length()) {
                return false;
            }
        }
        return true;
    }
}
