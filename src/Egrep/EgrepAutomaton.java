package Egrep;

import Automaton.DFAutomaton;

public class EgrepAutomaton {
    public DFAutomaton dfAutomaton;

    public EgrepAutomaton(DFAutomaton dfAutomaton) {
        this.dfAutomaton = dfAutomaton;
    }

    public boolean findPatternInLine(String line) {
        for (int i = 0; i < line.length(); i++) {
            if (findPatternStartingAtLinePosition(line, i)) {
                System.out.println("Pattern found at pos " + i);
                return true;
            }
        }
        System.out.println("Pattern not found");
        return false;
    }

    private boolean findPatternStartingAtLinePosition(String line, int linePos) {
        int automatonState = 0;
        while (!dfAutomaton.acceptingStates.contains(automatonState)) {
            if (dfAutomaton.transitions[automatonState][line.charAt(linePos)] == -1) {
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
