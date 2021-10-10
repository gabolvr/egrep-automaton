package Automaton;

import java.util.*;

public class DFAutomaton {
    protected int[][] transitions;
    protected HashSet<Integer> acceptingStates;

    public DFAutomaton(int[][] transitions, HashSet<Integer> acceptingStates) {
        this.transitions = transitions;
        this.acceptingStates = acceptingStates;
    }

    public DFAutomaton(NDFAutomaton ndfAutomaton) {
        // Define important states
        HashSet<Integer> importantStates = new HashSet<>();
        importantStates.add(0);
        for (int i = 0; i < ndfAutomaton.automatonTransition.length; i++) {
            for (int j = 0; j < ndfAutomaton.automatonTransition[i].length; j++) {
                if (ndfAutomaton.automatonTransition[i][j] != -1) {
                    importantStates.add(ndfAutomaton.automatonTransition[i][j]);
                }
            }
        }
        ArrayList<Integer> states = new ArrayList<>(importantStates);
        Collections.sort(states);
        HashMap<Integer, Integer> reverseImportantState = new HashMap<>();
        for(int i = 0; i < states.size(); i++) {
            reverseImportantState.put(states.get(i), i);
        }

        // DFS to find states reachable through epsilon transition
        HashSet<Integer> visited = new HashSet<>();
        ArrayList<HashSet<Integer>> epsilonReachable = new ArrayList<>();
        for (int i = 0; i < ndfAutomaton.size(); i++) {
            epsilonReachable.add(new HashSet<>());
        }
        for (int i = 0; i < ndfAutomaton.size(); i++) {
            EpsilonDFS(ndfAutomaton.epsilonTransition, epsilonReachable, visited, i);
        }

        // Create transitions for important states
        transitions = new int[states.size()][256];
        acceptingStates = new HashSet<>();

        for (int[] transition : transitions) {
            Arrays.fill(transition, -1);
        }

        for (int i = 0; i < states.size(); i++) {
            if (epsilonReachable.get(states.get(i)).contains(ndfAutomaton.size() - 1)) {
                acceptingStates.add(i);
            }

            for (Integer reachableState : epsilonReachable.get(states.get(i))) {
                for (int j = 0; j < ndfAutomaton.automatonTransition[reachableState].length; j++) {
                    if (ndfAutomaton.automatonTransition[reachableState][j] != -1) {
                        transitions[i][j] = reverseImportantState.get(ndfAutomaton.automatonTransition[reachableState][j]);
                    }
                }
            }
        }
    }

    private void EpsilonDFS(ArrayList<ArrayList<Integer>> epsilonTransition,
                              ArrayList<HashSet<Integer>> epsilonReachable,
                              HashSet<Integer> visited,
                              int origin) {
        epsilonReachable.get(origin).add(origin);
        visited.add(origin);
        for (Integer state : epsilonTransition.get(origin)) {
            if (!visited.contains(state)) {
                EpsilonDFS(epsilonTransition, epsilonReachable, visited, state);
            }
            epsilonReachable.get(origin).addAll(epsilonReachable.get(state));
        }
    }

    public int size() {
        return transitions.length;
    }

    public String toString() {
        String automaton = "Initial state: 0\nAccepting states: " + acceptingStates + "\nTransition list:\n";
        for (int i = 0; i < transitions.length; i++) {
            for (int j = 0; j < transitions[i].length; j++) {
                if (transitions[i][j] != -1) {
                    automaton += "  " + i + " -- " + (char) j + " --> " + transitions[i][j] + "\n";
                }
            }
        }
        return automaton;
    }
}
