package Automaton;

import java.util.ArrayList;

public class NDFAutomaton {
    public int[][] automatonTransition;
    public ArrayList<ArrayList<Integer>> epsilonTransition;

    public NDFAutomaton(int[][] automatonTransition, ArrayList<ArrayList<Integer>> epsilonTransition) {
        this.automatonTransition = automatonTransition;
        this.epsilonTransition = epsilonTransition;
    }

    public int size() {
        return automatonTransition.length;
    }

    public String toString() {
        String automaton = "Initial state: 0\nFinal state: " + (automatonTransition.length - 1) + "\nTransition list:\n";
        for (int i = 0; i < epsilonTransition.size(); i++) {
            if (!epsilonTransition.get(i).isEmpty()) {
                automaton += "  " + i + " -- epsilon --> " + epsilonTransition.get(i) + "\n";
            }
        }
        for (int i = 0; i < automatonTransition.length; i++) {
            for (int j = 0; j < automatonTransition[i].length; j++) {
                if (automatonTransition[i][j] != -1) {
                    automaton += "  " + i + " -- " + (char) j + " --> " + automatonTransition[i][j] + "\n";
                }
            }
        }
        return automaton;
    }
}
