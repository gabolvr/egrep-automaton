package Automaton;

public class DFAutomaton {
    protected int[][] transitions;

    public DFAutomaton(int[][] transitions) {
        this.transitions = transitions;
    }

    public int size() {
        return transitions.length;
    }
}
