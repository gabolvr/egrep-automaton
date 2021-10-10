package Automaton;

import RegEx.*;
import java.util.ArrayList;
import java.util.Arrays;

public class NDFAutomaton {
    public int[][] automatonTransition;
    public ArrayList<ArrayList<Integer>> epsilonTransition;

    public NDFAutomaton(int[][] automatonTransition, ArrayList<ArrayList<Integer>> epsilonTransition) {
        this.automatonTransition = automatonTransition;
        this.epsilonTransition = epsilonTransition;
    }

    public NDFAutomaton(RegExTree regExTree) {
        if (regExTree.subTrees.isEmpty()) {
            int[][] automatonTransition = new int[2][256];
            ArrayList<ArrayList<Integer>> epsilonTransition = new ArrayList<ArrayList<Integer>>();
    
            for (int i = 0; i < automatonTransition.length; i++) {
                Arrays.fill(automatonTransition[i], -1);
            }
    
            for (int i = 0; i < 2; i++) {
                epsilonTransition.add(new ArrayList<Integer>());
            }
    
            if (regExTree.root != RegEx.DOT) {
                automatonTransition[0][regExTree.root] = 1;
            }
            else {
                Arrays.fill(automatonTransition[0], 1);
            }
    
            this.automatonTransition = automatonTransition;
            this.epsilonTransition = epsilonTransition;
        }
    
        if (regExTree.root == RegEx.CONCAT) {
            NDFAutomaton left = new NDFAutomaton(regExTree.subTrees.get(0));
            NDFAutomaton right = new NDFAutomaton(regExTree.subTrees.get(1));
            int[][] automatonTransition = new int[left.size() + right.size()][256];
            ArrayList<ArrayList<Integer>> epsilonTransition = new ArrayList<ArrayList<Integer>>(left.epsilonTransition);
    
            for (int i = 0; i < left.size(); i++) {
                automatonTransition[i] = Arrays.copyOf(left.automatonTransition[i], left.automatonTransition[i].length);
            }
            for (int i = 0; i < right.size(); i++) {
                for (int j = 0; j < right.automatonTransition[i].length; j++) {
                    automatonTransition[left.size() + i][j] = (right.automatonTransition[i][j] != -1)
                            ? right.automatonTransition[i][j] + left.size()
                            : -1;
                }
            }
    
            epsilonTransition.get(left.size() - 1).add(left.size());
            for (ArrayList<Integer> epsilonFromNode : right.epsilonTransition) {
                ArrayList<Integer> newEpsilonFromNode = new ArrayList<Integer>();
                for (Integer destination : epsilonFromNode) {
                    newEpsilonFromNode.add(destination + left.size());
                }
                epsilonTransition.add(newEpsilonFromNode);
            }
    
            this.automatonTransition = automatonTransition;
            this.epsilonTransition = epsilonTransition;
        }
    
        if (regExTree.root == RegEx.ALTERN) {
            NDFAutomaton left = new NDFAutomaton(regExTree.subTrees.get(0));
            NDFAutomaton right = new NDFAutomaton(regExTree.subTrees.get(1));
            int[][] automatonTransition = new int[left.size() + right.size() + 2][256];
            ArrayList<ArrayList<Integer>> epsilonTransition = new ArrayList<ArrayList<Integer>>();
    
            // Add automaton transition
            Arrays.fill(automatonTransition[0], -1);
            Arrays.fill(automatonTransition[left.size() + right.size() + 1], -1);
            for (int i = 0; i < left.size(); i++) {
                for (int j = 0; j < left.automatonTransition[i].length; j++) {
                    automatonTransition[1 + i][j] = (left.automatonTransition[i][j] != -1)
                            ? left.automatonTransition[i][j] + 1
                            : -1;
                }
            }
    
            for (int i = 0; i < right.size(); i++) {
                for (int j = 0; j < right.automatonTransition[i].length; j++) {
                    automatonTransition[1 + left.size() + i][j] = (right.automatonTransition[i][j] != -1)
                            ? right.automatonTransition[i][j] + left.size() + 1
                            : -1;
                }
            }
    
            // Add epsilon transition
            epsilonTransition.add(new ArrayList<Integer>(Arrays.asList(1, left.size() + 1)));
    
            for (ArrayList<Integer> epsilonFromNode : left.epsilonTransition) {
                ArrayList<Integer> newEpsilonFromNode = new ArrayList<Integer>();
                for (Integer destination : epsilonFromNode) {
                    newEpsilonFromNode.add(destination + 1);
                }
                epsilonTransition.add(newEpsilonFromNode);
            }
            epsilonTransition.get(left.size()).add(left.size() + right.size() + 1);
    
            for (ArrayList<Integer> epsilonFromNode : right.epsilonTransition) {
                ArrayList<Integer> newEpsilonFromNode = new ArrayList<Integer>();
                for (Integer destination : epsilonFromNode) {
                    newEpsilonFromNode.add(destination + left.size() + 1);
                }
                epsilonTransition.add(newEpsilonFromNode);
            }
            epsilonTransition.get(left.size() + right.size()).add(left.size() + right.size() + 1);

            epsilonTransition.add(new ArrayList<>());
    
            this.automatonTransition = automatonTransition;
            this.epsilonTransition = epsilonTransition;
        }
    
        if (regExTree.root == RegEx.ETOILE) {
            NDFAutomaton child = new NDFAutomaton(regExTree.subTrees.get(0));
            int[][] automatonTransition = new int[child.size() + 2][256];
            ArrayList<ArrayList<Integer>> epsilonTransition = new ArrayList<ArrayList<Integer>>();
    
            // Add automaton transition
            Arrays.fill(automatonTransition[0], -1);
            Arrays.fill(automatonTransition[child.size() + 1], -1);
            for (int i = 0; i < child.size(); i++) {
                for (int j = 0; j < child.automatonTransition[i].length; j++) {
                    automatonTransition[1 + i][j] = (child.automatonTransition[i][j] != -1)
                            ? child.automatonTransition[i][j] + 1
                            : -1;
                }
            }
    
            // Add epsilon transition
            epsilonTransition.add(new ArrayList<Integer>(Arrays.asList(1, child.size() + 1)));
    
            for (ArrayList<Integer> epsilonFromNode : child.epsilonTransition) {
                ArrayList<Integer> newEpsilonFromNode = new ArrayList<Integer>();
                for (Integer destination : epsilonFromNode) {
                    newEpsilonFromNode.add(destination + 1);
                }
                epsilonTransition.add(newEpsilonFromNode);
            }
            epsilonTransition.get(child.size()).add(1);
            epsilonTransition.get(child.size()).add(child.size() + 1);
            epsilonTransition.add(new ArrayList<Integer>());
    
            this.automatonTransition = automatonTransition;
            this.epsilonTransition = epsilonTransition;
        }
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
