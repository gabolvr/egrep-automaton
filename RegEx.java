import java.util.Scanner;

import java.util.ArrayList;
import java.util.Arrays;
import java.lang.Exception;

public class RegEx {
  //MACROS
  static final int CONCAT = 0xC04CA7;
  static final int ETOILE = 0xE7011E;
  static final int ALTERN = 0xA17E54;
  static final int PROTECTION = 0xBADDAD;

  static final int PARENTHESEOUVRANT = 0x16641664;
  static final int PARENTHESEFERMANT = 0x51515151;
  static final int DOT = 0xD07;
  
  //REGEX
  private static String regEx;
  
  //CONSTRUCTOR
  public RegEx(){}

  //MAIN
  public static void main(String arg[]) {
    System.out.println("Welcome to Bogota, Mr. Thomas Anderson.");
    if (arg.length!=0) {
      regEx = arg[0];
    } else {
      Scanner scanner = new Scanner(System.in);
      System.out.print("  >> Please enter a regEx: ");
      regEx = scanner.next();
    }
    System.out.println("  >> Parsing regEx \""+regEx+"\".");
    System.out.println("  >> ...");
    
    if (regEx.length()<1) {
      System.err.println("  >> ERROR: empty regEx.");
    } else {
      System.out.print("  >> ASCII codes: ["+(int)regEx.charAt(0));
      for (int i=1;i<regEx.length();i++) System.out.print(","+(int)regEx.charAt(i));
      System.out.println("].");
      try {
        RegExTree ret = parse();
        System.out.println("  >> Tree result: "+ret.toString()+".");
      } catch (Exception e) {
        System.err.println("  >> ERROR: syntax error for regEx \""+regEx+"\".");
      }
    }

    System.out.println("  >> ...");
    System.out.println("  >> Parsing completed.");
    System.out.println("Goodbye Mr. Anderson.");
  }

  //FROM REGEX TO SYNTAX TREE
  private static RegExTree parse() throws Exception {
    //BEGIN DEBUG: set conditionnal to true for debug example
    if (false) throw new Exception();
    RegExTree example = exampleAhoUllman();
    if (false) return example;
    //END DEBUG

    ArrayList<RegExTree> result = new ArrayList<RegExTree>();
    for (int i=0;i<regEx.length();i++) result.add(new RegExTree(charToRoot(regEx.charAt(i)),new ArrayList<RegExTree>()));
    
    return parse(result);
  }
  private static int charToRoot(char c) {
    if (c=='.') return DOT;
    if (c=='*') return ETOILE;
    if (c=='|') return ALTERN;
    if (c=='(') return PARENTHESEOUVRANT;
    if (c==')') return PARENTHESEFERMANT;
    return (int)c;
  }
  private static RegExTree parse(ArrayList<RegExTree> result) throws Exception {
    while (containParenthese(result)) result=processParenthese(result);
    while (containEtoile(result)) result=processEtoile(result);
    while (containConcat(result)) result=processConcat(result);
    while (containAltern(result)) result=processAltern(result);

    if (result.size()>1) throw new Exception();

    return removeProtection(result.get(0));
  }
  private static boolean containParenthese(ArrayList<RegExTree> trees) {
    for (RegExTree t: trees) if (t.root==PARENTHESEFERMANT || t.root==PARENTHESEOUVRANT) return true;
    return false;
  }
  private static ArrayList<RegExTree> processParenthese(ArrayList<RegExTree> trees) throws Exception {
    ArrayList<RegExTree> result = new ArrayList<RegExTree>();
    boolean found = false;
    for (RegExTree t: trees) {
      if (!found && t.root==PARENTHESEFERMANT) {
        boolean done = false;
        ArrayList<RegExTree> content = new ArrayList<RegExTree>();
        while (!done && !result.isEmpty())
          if (result.get(result.size()-1).root==PARENTHESEOUVRANT) { done = true; result.remove(result.size()-1); }
          else content.add(0,result.remove(result.size()-1));
        if (!done) throw new Exception();
        found = true;
        ArrayList<RegExTree> subTrees = new ArrayList<RegExTree>();
        subTrees.add(parse(content));
        result.add(new RegExTree(PROTECTION, subTrees));
      } else {
        result.add(t);
      }
    }
    if (!found) throw new Exception();
    return result;
  }
  private static boolean containEtoile(ArrayList<RegExTree> trees) {
    for (RegExTree t: trees) if (t.root==ETOILE && t.subTrees.isEmpty()) return true;
    return false;
  }
  private static ArrayList<RegExTree> processEtoile(ArrayList<RegExTree> trees) throws Exception {
    ArrayList<RegExTree> result = new ArrayList<RegExTree>();
    boolean found = false;
    for (RegExTree t: trees) {
      if (!found && t.root==ETOILE && t.subTrees.isEmpty()) {
        if (result.isEmpty()) throw new Exception();
        found = true;
        RegExTree last = result.remove(result.size()-1);
        ArrayList<RegExTree> subTrees = new ArrayList<RegExTree>();
        subTrees.add(last);
        result.add(new RegExTree(ETOILE, subTrees));
      } else {
        result.add(t);
      }
    }
    return result;
  }
  private static boolean containConcat(ArrayList<RegExTree> trees) {
    boolean firstFound = false;
    for (RegExTree t: trees) {
      if (!firstFound && t.root!=ALTERN) { firstFound = true; continue; }
      if (firstFound) if (t.root!=ALTERN) return true; else firstFound = false;
    }
    return false;
  }
  private static ArrayList<RegExTree> processConcat(ArrayList<RegExTree> trees) throws Exception {
    ArrayList<RegExTree> result = new ArrayList<RegExTree>();
    boolean found = false;
    boolean firstFound = false;
    for (RegExTree t: trees) {
      if (!found && !firstFound && t.root!=ALTERN) {
        firstFound = true;
        result.add(t);
        continue;
      }
      if (!found && firstFound && t.root==ALTERN) {
        firstFound = false;
        result.add(t);
        continue;
      }
      if (!found && firstFound && t.root!=ALTERN) {
        found = true;
        RegExTree last = result.remove(result.size()-1);
        ArrayList<RegExTree> subTrees = new ArrayList<RegExTree>();
        subTrees.add(last);
        subTrees.add(t);
        result.add(new RegExTree(CONCAT, subTrees));
      } else {
        result.add(t);
      }
    }
    return result;
  }
  private static boolean containAltern(ArrayList<RegExTree> trees) {
    for (RegExTree t: trees) if (t.root==ALTERN && t.subTrees.isEmpty()) return true;
    return false;
  }
  private static ArrayList<RegExTree> processAltern(ArrayList<RegExTree> trees) throws Exception {
    ArrayList<RegExTree> result = new ArrayList<RegExTree>();
    boolean found = false;
    RegExTree gauche = null;
    boolean done = false;
    for (RegExTree t: trees) {
      if (!found && t.root==ALTERN && t.subTrees.isEmpty()) {
        if (result.isEmpty()) throw new Exception();
        found = true;
        gauche = result.remove(result.size()-1);
        continue;
      }
      if (found && !done) {
        if (gauche==null) throw new Exception();
        done=true;
        ArrayList<RegExTree> subTrees = new ArrayList<RegExTree>();
        subTrees.add(gauche);
        subTrees.add(t);
        result.add(new RegExTree(ALTERN, subTrees));
      } else {
        result.add(t);
      }
    }
    return result;
  }
  private static RegExTree removeProtection(RegExTree tree) throws Exception {
    if (tree.root==PROTECTION && tree.subTrees.size()!=1) throw new Exception();
    if (tree.subTrees.isEmpty()) return tree;
    if (tree.root==PROTECTION) return removeProtection(tree.subTrees.get(0));

    ArrayList<RegExTree> subTrees = new ArrayList<RegExTree>();
    for (RegExTree t: tree.subTrees) subTrees.add(removeProtection(t));
    return new RegExTree(tree.root, subTrees);
  }
  
  //EXAMPLE
  // --> RegEx from Aho-Ullman book Chap.10 Example 10.25
  private static RegExTree exampleAhoUllman() {
    RegExTree a = new RegExTree((int)'a', new ArrayList<RegExTree>());
    RegExTree b = new RegExTree((int)'b', new ArrayList<RegExTree>());
    RegExTree c = new RegExTree((int)'c', new ArrayList<RegExTree>());
    ArrayList<RegExTree> subTrees = new ArrayList<RegExTree>();
    subTrees.add(c);
    RegExTree cEtoile = new RegExTree(ETOILE, subTrees);
    subTrees = new ArrayList<RegExTree>();
    subTrees.add(b);
    subTrees.add(cEtoile);
    RegExTree dotBCEtoile = new RegExTree(CONCAT, subTrees);
    subTrees = new ArrayList<RegExTree>();
    subTrees.add(a);
    subTrees.add(dotBCEtoile);
    return new RegExTree(ALTERN, subTrees);
  }

  public static NDFAutomaton RegExTreeToNDFAutomaton(RegExTree regExTree) {
    if (regExTree.subTrees.isEmpty()) {
      int[][] automataTransition = new int[2][256];
      ArrayList<ArrayList<Integer>> epsilonTransition = new ArrayList<ArrayList<Integer>>();

      for (int i = 0; i < automataTransition.length; i++) {
        Arrays.fill(automataTransition[i], -1);
      }

      for (int i = 0; i < 2; i++) {
        epsilonTransition.add(new ArrayList<Integer>());
      }
      
      if (regExTree.root != DOT) {
        automataTransition[0][regExTree.root] = 1;
      }
      else {
        Arrays.fill(automataTransition[0], 1);
      }

      return new NDFAutomaton(automataTransition, epsilonTransition);
    }

    if (regExTree.root == CONCAT) {
      NDFAutomaton left = RegExTreeToNDFAutomaton(regExTree.subTrees.get(0));
      NDFAutomaton right = RegExTreeToNDFAutomaton(regExTree.subTrees.get(1));
      int[][] automataTransition = new int[left.size() + right.size()][256];
      ArrayList<ArrayList<Integer>> epsilonTransition = new ArrayList<ArrayList<Integer>>(left.epsilonTransition);

      for (int i = 0; i < left.size(); i++) {
        automataTransition[i] = Arrays.copyOf(left.automataTransition[i], left.automataTransition[i].length);
      }
      for (int i = 0; i < right.size(); i++) {
        for (int j = 0; j < right.automataTransition[i].length; j++) {
          automataTransition[left.size() + i][j] = (right.automataTransition[i][j] != -1)
            ? right.automataTransition[i][j] + left.size()
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

      return new NDFAutomaton(automataTransition, epsilonTransition);
    }

    if (regExTree.root == ALTERN) {
      NDFAutomaton left = RegExTreeToNDFAutomaton(regExTree.subTrees.get(0));
      NDFAutomaton right = RegExTreeToNDFAutomaton(regExTree.subTrees.get(1));
      int[][] automataTransition = new int[left.size() + right.size() + 2][256];
      ArrayList<ArrayList<Integer>> epsilonTransition = new ArrayList<ArrayList<Integer>>();

      // Add automata transition
      for (int i = 0; i < left.size(); i++) {
        for (int j = 0; j < left.automataTransition[i].length; j++) {
          automataTransition[1 + i][j] = (left.automataTransition[i][j] != -1) 
            ? left.automataTransition[i][j] + 1 
            : -1;
        }
      }

      for (int i = 0; i < right.size(); i++) {
        for (int j = 0; j < right.automataTransition[i].length; j++) {
          automataTransition[1 + left.size() + i][j] = (right.automataTransition[i][j] != -1) 
            ? right.automataTransition[i][j] + left.size() + 1
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

      return new NDFAutomaton(automataTransition, epsilonTransition);
    }

    if (regExTree.root == ETOILE) {
      NDFAutomaton child = RegExTreeToNDFAutomaton(regExTree.subTrees.get(0));
      int[][] automataTransition = new int[child.size() + 2][256];
      ArrayList<ArrayList<Integer>> epsilonTransition = new ArrayList<ArrayList<Integer>>();

      // Add automata transition
      for (int i = 0; i < child.size(); i++) {
        for (int j = 0; j < child.automataTransition[i].length; j++) {
          automataTransition[1 + i][j] = (child.automataTransition[i][j] != -1) 
            ? child.automataTransition[i][j] + 1 
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

      return new NDFAutomaton(automataTransition, epsilonTransition);
    }

    return null;
  }
}

//UTILITARY CLASS
class RegExTree {
  protected int root;
  protected ArrayList<RegExTree> subTrees;
  public RegExTree(int root, ArrayList<RegExTree> subTrees) {
    this.root = root;
    this.subTrees = subTrees;
  }
  //FROM TREE TO PARENTHESIS
  public String toString() {
    if (subTrees.isEmpty()) return rootToString();
    String result = rootToString()+"("+subTrees.get(0).toString();
    for (int i=1;i<subTrees.size();i++) result+=","+subTrees.get(i).toString();
    return result+")";
  }
  private String rootToString() {
    if (root==RegEx.CONCAT) return ".";
    if (root==RegEx.ETOILE) return "*";
    if (root==RegEx.ALTERN) return "|";
    if (root==RegEx.DOT) return ".";
    return Character.toString((char)root);
  }
}

class NDFAutomaton {
  protected int[][] automataTransition;
  protected ArrayList<ArrayList<Integer>> epsilonTransition;
  
  public NDFAutomaton(int[][] automataTransition, ArrayList<ArrayList<Integer>> epsilonTransition) {
    this.automataTransition = automataTransition;
    this.epsilonTransition = epsilonTransition;
  }

  public int size() {
    return automataTransition.length;
  }
}
