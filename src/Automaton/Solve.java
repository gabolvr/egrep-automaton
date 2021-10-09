package Automaton;

import RegEx.*;

public class Solve {
    public Solve(String pattern){
        try{
            RegExTree regEx;
            regEx = RegEx.parse(pattern);
            NDFAutomaton ndfAutomaton = new NDFAutomaton(regEx);
            System.out.println(ndfAutomaton);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}