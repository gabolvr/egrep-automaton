package Automaton;

import Egrep.EgrepAutomaton;
import RegEx.*;
import Text.Text;

public class Solve {
    public Solve(String pattern){
        try{
            RegExTree regEx;
            regEx = RegEx.parse(pattern);
            NDFAutomaton ndfAutomaton = new NDFAutomaton(regEx);
            System.out.println(ndfAutomaton);
            DFAutomaton dfAutomaton = new DFAutomaton(ndfAutomaton);
            System.out.println(dfAutomaton);
            EgrepAutomaton egrep = new EgrepAutomaton(dfAutomaton);
            egrep.findPatternInText(new Text("tests/babylon.txt"));
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
