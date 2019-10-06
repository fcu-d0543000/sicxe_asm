package sicxe;

public class Expressions {

  public static linkList preFix(String expression, PassOne pass1) {
    linkList stack = null;
    linkList preFix = null;
    linkList temp = null;
    linkList opStack = null;
    linkList newS = null;
    String member;
    SymTab symTab = pass1.getSymTab();
    SymTab symTemp;
    int value;
    int type = 0;
    int i;
    int tail = expression.length();
    int pre = expression.length();

    /*
     * -1 undefined
     * -2 not absolute nor relative
     * */
    
    try {
      value = Integer.parseInt(expression);
      preFix = new linkList(expression, value, type);
    } catch (Exception e) {
      for (i = expression.length() - 1; i >= 0; i--) {
        if (expression.charAt(i) == '+' || expression.charAt(i) == '-') {
          if (expression.charAt(i + 1) != '(') {
            pre = i + 1;
            member = expression.substring(pre, tail);
            try {
              value = Integer.parseInt(member);
              type = 0;
            } catch (Exception e2) {
              if (symTab.searchSymTab(member) != null) {
                symTemp = symTab.searchSymTab(member);
                value = symTemp.getValue();
                if (symTemp.getBlock() == 1) {
                  value += pass1.getDefaultLoc();
                } else if (symTemp.getBlock() == 2) {
                  value += pass1.getDefaultLoc() + pass1.getCdataLoc();
                } else {//
                  value += pass1.getStartAddr();
                }
                type = symTab.searchSymTab(member).getType();
              } else {
                return null;
              }
            }
            if (preFix == null) {
              preFix = new linkList(member, value, type);//
            } else {
              newS = new linkList(member, value, type);
              temp = preFix;
              while (temp.getNext() != null)
                temp = temp.getNext();
              temp.setNext(newS);//
            }

          }
          temp = preFix;
          while (opStack != null && (opStack.getMember().equals("*") || opStack.getMember().equals("/"))) {
            newS = new linkList(opStack.getMember(), 0);
            opStack = opStack.getNext();
            while (temp.getNext() != null)
              temp = temp.getNext();
            temp.setNext(newS);////
          }
          newS = new linkList(new Character(expression.charAt(i)).toString(), 0);
          newS.setNext(opStack);
          opStack = newS;
          tail = i;
        } else if (expression.charAt(i) == '*' || expression.charAt(i) == '/') {
          if (expression.charAt(i + 1) != '(') {
            pre = i + 1;
            member = expression.substring(pre, tail);
            try {
              value = Integer.parseInt(member);
              type = 0;
            } catch (Exception e2) {
              if (symTab.searchSymTab(member) != null) {
                symTemp = symTab.searchSymTab(member);
                value = symTemp.getValue();
                if (symTemp.getBlock() == 1) {
                  value += pass1.getDefaultLoc();
                } else if (symTemp.getBlock() == 2) {
                  value += pass1.getDefaultLoc() + pass1.getCdataLoc();
                } else {  //
                  value += pass1.getStartAddr();
                }
                type = symTab.searchSymTab(member).getType();
              } else {
                return null;
              }
            }
            if (preFix == null) {
              preFix = new linkList(member, value, type);//
              // stack = new linkList(member, value, type);
            } else {
              newS = new linkList(member, value, type);
              temp = preFix;
              while (temp.getNext() != null)
                temp = temp.getNext();
              temp.setNext(newS);//
              // newS.setNext(stack);
              // stack = newS;
            }
          }

          newS = new linkList(new Character(expression.charAt(i)).toString(), 0);
          newS.setNext(opStack);
          opStack = newS;
          tail = i;
        } else if (expression.charAt(i) == ')') {
          tail = i;

          newS = new linkList(new Character(expression.charAt(i)).toString(), 0);
          newS.setNext(opStack);
          opStack = newS;
        } else if (expression.charAt(i) == '(') {
          if (expression.charAt(i + 1) == '(') {
            tail = i;
          } else {
            pre = i + 1;
            member = expression.substring(pre, tail);
            try {
              value = Integer.parseInt(member);
              type = 0;
            } catch (Exception e2) {
              if (symTab.searchSymTab(member) != null) {
                symTemp = symTab.searchSymTab(member);
                value = symTemp.getValue();
                if (symTemp.getBlock() == 1) {
                  value += pass1.getDefaultLoc();
                } else if (symTemp.getBlock() == 2) {
                  value += pass1.getDefaultLoc() + pass1.getCdataLoc();
                } else {  //
                  value += pass1.getStartAddr();
                }
                type = symTab.searchSymTab(member).getType();
              } else {
                return null;
              }
            }
            if (preFix == null) {
              preFix = new linkList(member, value, type);//
              // stack = new linkList(member, value, type);
            } else {
              newS = new linkList(member, value, type);
              temp = preFix;
              while (temp.getNext() != null)
                temp = temp.getNext();
              temp.setNext(newS);//
              // newS.setNext(stack);
              // stack = newS;
            }
          }
          temp = preFix;
          while (opStack != null && opStack.getMember().charAt(0) != ')') {
            newS = new linkList(opStack.getMember(), 0);
            opStack = opStack.getNext();
            while (temp.getNext() != null)
              temp = temp.getNext();
            temp.setNext(newS);//
            // newS.setNext(stack);
            // stack = newS;
          }
          opStack = opStack.getNext();
        } else {

        }
      }
      if (expression.length() > 0 && expression.charAt(0) != '(') {
        pre = 0;
        member = expression.substring(pre, tail);
        try {
          value = Integer.parseInt(member);
          type = 0;
        } catch (Exception e2) {
          if (symTab.searchSymTab(member) != null) {
            symTemp = symTab.searchSymTab(member);
            value = symTemp.getValue();
            if (symTemp.getBlock() == 1) {
              value += pass1.getDefaultLoc();
            } else if (symTemp.getBlock() == 2) {
              value += pass1.getDefaultLoc() + pass1.getCdataLoc();
            } else {  //
              value += pass1.getStartAddr();
            }
            type = symTab.searchSymTab(member).getType();
          } else {

            return null;
          }
        }
        if (preFix == null) {
          preFix = new linkList(member, value, type);
          // stack = new linkList(member, value, type);
        } else {
          newS = new linkList(member, value, type);
          temp = preFix;
          while (temp.getNext() != null)
            temp = temp.getNext();
          temp.setNext(newS);//
          // newS.setNext(stack);
          // stack = newS;
        }
      }
      temp = preFix;
      while (opStack != null) {
        newS = new linkList(opStack.getMember(), 0);
        opStack = opStack.getNext();
        while (temp.getNext() != null)
          temp = temp.getNext();
        temp.setNext(newS);//
        // newS.setNext(stack);
        // stack = newS;
      }
    }
    return preFix;
  }

  public static int[] calculation(String expression, PassOne pass1) {
    int[] result = new int[2];
    Boolean error = true;
    result[0] = -1;
    result[1] = -1;
    linkList preFix = Expressions.preFix(expression, pass1);
    linkList stack = null;
    linkList temp = null;
    int type = 0;

    if (preFix == null)
      return result;

    temp = preFix;
    while (preFix != null && error) {

      switch (preFix.getMember()) {
      case "+":
        stack.getNext().setValue(stack.getValue() + stack.getNext().getValue());
        ;
        stack.getNext().setType(stack.getType() + stack.getNext().getType());
        stack = stack.getNext();
        break;
      case "-":
        stack.getNext().setValue(stack.getValue() - stack.getNext().getValue());
        ;
        stack.getNext().setType(stack.getType() - stack.getNext().getType());
        stack = stack.getNext();
        break;
      case "*":
        if (stack.getNext().getType() >= SymTab.RELATIVE && stack.getType() >= SymTab.RELATIVE) {
          error = false;
          break;
        } else if (stack.getType() >= SymTab.RELATIVE) {
          stack.getNext().setType(stack.getType() * stack.getNext().getValue());
        } else if (stack.getNext().getType() >= SymTab.RELATIVE) {
          stack.getNext().setType(stack.getNext().getType() * stack.getValue());
        } else {
          stack.getNext().setType(0);
        }

        stack.getNext().setValue(stack.getValue() * stack.getNext().getValue());
        stack = stack.getNext();
        break;
      case "/":
        if (stack.getNext().getType() >= SymTab.RELATIVE && stack.getType() >= SymTab.RELATIVE
            || stack.getNext().getValue() == 0) {
          error = false;
          break;
        } else if (stack.getType() >= SymTab.RELATIVE) {
          if (stack.getType() % stack.getNext().getValue() == 0) {
            stack.getNext().setType(stack.getType() / stack.getNext().getValue());
          } else {
            error = false;
            break;
          }
        } else if (stack.getNext().getType() >= SymTab.RELATIVE) {
          error = false;
          break;
        } else {
          stack.getNext().setType(0);
        }
        stack.getNext().setValue(stack.getValue() / stack.getNext().getValue());
        ;
        stack = stack.getNext();
        break;
      default:
        if (stack == null) {
          stack = new linkList(preFix.getValue(), preFix.getType());
        } else {
          temp = new linkList(preFix.getValue(), preFix.getType());
          temp.setNext(stack);
          stack = temp;
        }
        break;
      }
      preFix = preFix.getNext();
    }

    if (!error || stack.getType() > 1 || stack.getType() < 0) {
      result[0] = -1;
      result[1] = -2;
      return result;
    }

    result[0] = stack.getValue();
    result[1] = stack.getType();
    return result;
  }
}
