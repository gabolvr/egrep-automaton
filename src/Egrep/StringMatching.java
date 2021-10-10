package Egrep;

import Automaton.DFAutomaton;
import Automaton.NDFAutomaton;
import RegEx.*;
import Text.Text;

public class StringMatching {
    private Text text;
    private RegExTree regEx;
    private String pattern;

    public StringMatching(String pattern, String filepath) {
        this.pattern = pattern;
        text = new Text(filepath);
        try {
            regEx = RegEx.parse(pattern);
        } catch (Exception e) {
            System.out.println(e);
            System.exit(2);
        }
    }

    public void match() {
        match(false);
    }

    public void match(boolean DFA) {
        Egrep egrep;
        if (method() == 2 && !DFA) {
            egrep = new EgrepKMP(pattern);
        } else {
            NDFAutomaton ndfAutomaton = new NDFAutomaton(regEx);
            DFAutomaton dfAutomaton = new DFAutomaton(ndfAutomaton);
            egrep = new EgrepAutomaton(dfAutomaton);
        }
        egrep.findPatternInText(text);
    }

   private int method(){
       if(pattern.contains("(") || pattern.contains(")") || pattern.contains("*") || pattern.contains(".") ||
       pattern.contains("|"))
           return 1;
       return 2;
   }
}
