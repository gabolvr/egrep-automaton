package Automaton;

import Egrep.EgrepAutomaton;
import KMP.KMP;
import RegEx.*;
import Text.Text;

public class StringMatching {
    private Text text;
    private RegExTree regEx;
    private String pattern;

    public StringMatching(String pattern, String filepath){
        this.pattern = pattern;
        text = new Text(filepath);
        try{
            regEx = RegEx.parse(pattern);
        } catch (Exception e) {
            System.out.println(e);
            System.exit(2);
        }
    }

    public void match(){
        switch(method()){
            case 1: // war machine
                NDFAutomaton ndfAutomaton = new NDFAutomaton(regEx);
                System.out.println(ndfAutomaton);
                DFAutomaton dfAutomaton = new DFAutomaton(ndfAutomaton);
                System.out.println(dfAutomaton);
                EgrepAutomaton egrep = new EgrepAutomaton(dfAutomaton);
                egrep.findPatternInText(new Text("tests/babylon.txt"));
                break;
            case 2:
                KMP kmp = new KMP(pattern);
                kmp.search(text);
                break;
            default:
                return;
        }
    }

    private int method(){
        return 1;
    }
}
