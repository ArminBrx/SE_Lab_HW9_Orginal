package parser;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Stack;

import codeFiles.CodeGenerator;
import errorManagement.ErrorHandler;
import scanner.lexicalAnalyzer;
import scanner.token.Token;


public class Parser {
  private ArrayList<Rule> rules;
  private Stack<Integer> parsStack;
  private ParseTable parseTable;
  private lexicalAnalyzer lexicalAnalyzer;
  private CodeGenerator cg;
  private Token lookAhead;
  private boolean finish;;

  public Parser() {
    parsStack = new Stack<Integer>();
    parsStack.push(0);
    try {
      parseTable = new ParseTable(Files.readAllLines(Paths.get("src/main/resources/parseTable")).get(0));
    } catch (Exception e) {
      e.printStackTrace();
    }
    rules = new ArrayList<Rule>();
    try {
      for (String stringRule : Files.readAllLines(Paths.get("src/main/resources/Rules"))) {
        rules.add(new Rule(stringRule));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    cg = new CodeGenerator();
  }

  public boolean shiftAction(Action t1, act t2) {
    if(t1.action == t2) {
      parsStack.push(t1.number);
      lookAhead = lexicalAnalyzer.getNextToken();
      return true;
    }
    return false;
  }

  public boolean reduceAction(Action t1, act t2) {
    if(t1.action == t2) {
      Rule rule = rules.get(t1.number);
      for (int i = 0; i < rule.RHS.size(); i++) {
        parsStack.pop();
      }

//                      Log.print("LHS : "+rule.LHS);
      parsStack.push(parseTable.getGotoTable(parsStack.peek(), rule.LHS));
//                      Log.print("");
      try {
        cg.semanticFunction(rule.semanticAction, lookAhead);
      } catch (Exception e) {
      }
      return true;
    }
    return false;
  }

  public boolean acceptAction(Action t1, act t2) {
    if(t1.action == t2) {
      finish = true;
      return true;
    }
    return false;
  }

  public void startParse(java.util.Scanner sc) {
    lexicalAnalyzer = new lexicalAnalyzer(sc);
    lookAhead = lexicalAnalyzer.getNextToken();
    finish = false;
    Action currentAction;
    while (!finish) {
      try {
//                Log.print("state : "+ parsStack.peek());
        currentAction = parseTable.getActionTable(parsStack.peek(), lookAhead);
        //Log.print("");

//        if(shiftAction(currentAction,act.shift)) break;
//        if(reduceAction(currentAction,act.reduce)) break;
        if(acceptAction(currentAction,act.accept)) break;

        switch (currentAction.action) {
          case shift:
            parsStack.push(currentAction.number);
            lookAhead = lexicalAnalyzer.getNextToken();

            break;
          case reduce:
            Rule rule = rules.get(currentAction.number);
            for (int i = 0; i < rule.RHS.size(); i++) {
              parsStack.pop();
            }

//                      Log.print("LHS : "+rule.LHS);
            parsStack.push(parseTable.getGotoTable(parsStack.peek(), rule.LHS));
//                      Log.print("");
            try {
              cg.semanticFunction(rule.semanticAction, lookAhead);
            } catch (Exception e) {
              System.out.println("Error " + e);
            }
            break;
//        case accept:
//          finish = true;
//          break;
        }


      } catch (Exception ignored) {

        ignored.printStackTrace();
//                boolean find = false;
//                for (NonTerminal t : NonTerminal.values()) {
//                    if (parseTable.getGotoTable(parsStack.peek(), t) != -1) {
//                        find = true;
//                        parsStack.push(parseTable.getGotoTable(parsStack.peek(), t));
//                        StringBuilder tokenFollow = new StringBuilder();
//                        tokenFollow.append(String.format("|(?<%s>%s)", t.name(), t.pattern));
//                        Matcher matcher = Pattern.compile(tokenFollow.substring(1)).matcher(lookAhead.toString());
//                        while (!matcher.find()) {
//                            lookAhead = lexicalAnalyzer.getNextToken();
//                        }
//                    }
//                }
//                if (!find)
//                    parsStack.pop();
      }


    }
    if (!ErrorHandler.hasError)
      cg.printMemory();


  }


}
