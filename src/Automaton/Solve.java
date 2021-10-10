package Automaton;

import Egrep.EgrepAutomaton;
import RegEx.*;

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
            egrep.findPatternInLine("  state--Sargon and Merodach-baladan--Sennacherib's attempt");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
