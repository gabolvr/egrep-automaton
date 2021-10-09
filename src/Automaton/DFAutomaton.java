package Automaton;

import java.util.ArrayList;
import java.util.HashSet;

public class DFAutomaton {
    protected int[][] transitions;

    public DFAutomaton(int[][] transitions) {
        this.transitions = transitions;
    }

    public DFAutomaton(NDFAutomaton ndfAutomaton) {
        HashSet<Integer> importantStates = new HashSet<>();

        // Define important states
        importantStates.add(0);
        for (int i = 0; i < ndfAutomaton.automatonTransition.length; i++) {
            for (int j = 0; j < ndfAutomaton.automatonTransition[i].length; j++) {
                if (ndfAutomaton.automatonTransition[i][j] != -1) {
                    importantStates.add(ndfAutomaton.automatonTransition[i][j]);
                }
            }
        }

        ArrayList<HashSet<Integer>> epsilonReachable = new ArrayList<>();
        // DFS to find states reachable through epsilon transition
        
    }

    public int size() {
        return transitions.length;
    }
}
