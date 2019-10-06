package sicxe;

import java.io.FileWriter;
import java.io.IOException;

public class PassTwo {
  private PassOne pass1;
  private MidFile midFile;
  private SymTab sym;
  private FileWriter fwLISFILE;
  private FileWriter fwOBJFILE;
  private int defaultLoc;
  private int cdataLoc;
  private int cblksLoc;
  private int startAddr;
  private LitTab litTab;

  public PassTwo(PassOne pass1, FileWriter fwLISFILE, FileWriter fwOBJFILE) {
    if (pass1 != null) {
      this.pass1 = pass1;
      this.midFile = pass1.getMidFile();
      this.sym = pass1.getSymTab();
      this.defaultLoc = pass1.getDefaultLoc();
      this.cdataLoc = pass1.getCdataLoc();
      this.cblksLoc = pass1.getCblksLoc();
      this.fwLISFILE = fwLISFILE;
      this.fwOBJFILE = fwOBJFILE;
      this.startAddr = pass1.getStartAddr();
      this.litTab = pass1.getLitTab();
    } else {
      this.pass1 = null;
    }
  }

  public void findObcode() throws IOException {
    String hCard = "";
    String tCard = "";
    String mCard = "";
    String eCard = "";
    String line = "";
    String tempLine;
    String obcode = "";
    String label;
    String opcode;
    String operand;
    String progName = "";
    String startLine = "";
    Boolean ifStart = false;
    int[] tempOpcode;
    int[] tempOperand;
    int temp;
    int pc;
    int base = 0;
    int disp;
    int loc = 0;
    int xbpe = 0;
    int blk = 0;

    if (pass1 == null)
      return;

    fwLISFILE.write("SIC/XE Assembler \n\n");
    fwLISFILE.write("LOC/BLK    OBCODE              Source Line\n");
    fwLISFILE.write("-------   --------   ----------------------------------\n");

    while (midFile != null && midFile.getValue() == -1) {
      fwLISFILE.write("                     " + midFile.getLine() + "\n");

      if (midFile.getErrorMessage().length() > 0) {
        fwOBJFILE = null;
        fwLISFILE.write(midFile.getErrorMessage());
      }
      midFile = midFile.getNext();
    }

    if (midFile != null) {
      tempLine = midFile.getLine();
      while (tempLine.length() < 36)
        tempLine += " ";

      label = tempLine.substring(0, 8).trim();
      opcode = tempLine.substring(10, 16).trim().toUpperCase();
      operand = tempLine.substring(18, 36).trim();

      if (opcode.equalsIgnoreCase("START")) {
        line = String.format("%4s", Integer.toHexString(midFile.getValue()).toUpperCase()).replaceAll(" ", "0") + " "
            + 0 + "     " + "       " + "   " + midFile.getLine() + "\n";
        fwLISFILE.write(line);
        if (midFile.getErrorMessage().length() == 0) {
          hCard += "H" + label + " "
              + String.format("%6s", Integer.toHexString(startAddr).toUpperCase()).replaceAll(" ", "0");
          hCard += String.format("%6s", Integer.toHexString(cdataLoc + defaultLoc + cblksLoc - startAddr).toUpperCase())
              .replaceAll(" ", "0") + "\n";
          fwOBJFILE.write(hCard);
        } else {
          fwLISFILE.write(midFile.getErrorMessage());
          fwOBJFILE = null;
        }
        progName = label;
        ifStart = true;
        midFile = midFile.getNext();        
      } else {
        startLine = tempLine;        
        fwOBJFILE = null;
        ifStart = false;
      }

    }

    while (midFile != null) {
      if (ifStart) {
        tempLine = midFile.getLine();
      } else {
        tempLine = startLine;
        ifStart = true;
      }

      if (midFile.getValue() == -1) {
        fwLISFILE.write("    " + "      " + "        " + "   " + tempLine + "\n");
        if (midFile.getErrorMessage().length() > 0) {
          fwOBJFILE = null;
          fwLISFILE.write(midFile.getErrorMessage());
        }
      } else {

        while (tempLine.length() < 36)
          tempLine += " ";
        label = tempLine.substring(0, 8).trim();
        opcode = tempLine.substring(10, 16).trim().toUpperCase();
        operand = tempLine.substring(18, 36).trim();
        //
        if (tempLine.charAt(0) == '*' && tempLine.charAt(9) == '=') {
          if (opcode.length() > 3) {
            if (opcode.charAt(0) == 'X' && opcode.charAt(1) == '\'' && opcode.charAt(opcode.length() - 1) == '\'') {
              if ((opcode.length() - 3) % 2 == 0) {
                opcode = opcode.substring(2, opcode.length() - 1);
                try {
                  Integer.parseInt(opcode);
                  obcode = opcode;
                } catch (Exception e) {
                  obcode = "";
                  midFile.addErrorMessage(ErrorMsg.literalHex);
                }
              } else {
                obcode = "";
                midFile.addErrorMessage(ErrorMsg.literalOdd);
              }
            } else if (opcode.charAt(0) == 'C' && opcode.charAt(1) == '\''
                && opcode.charAt(opcode.length() - 1) == '\'') {
              obcode = "";
              opcode = tempLine.substring(12, 15).trim();
              for (int i = 0; i < opcode.length(); i++) {
                obcode += String.format("%2s", Integer.toHexString(opcode.charAt(i)).replaceAll(" ", "0"));
              }
              obcode = obcode.toUpperCase();
            }
          } else {
            obcode = "";
          }
          //
          loc = midFile.getValue();

          if (obcode.length() <= 8) {
            line = String.format("%4s", Integer.toHexString(loc)).toUpperCase().replaceAll(" ", "0") + " "
                + midFile.getBlk() + "    " + String.format("%8s", obcode) + "   " + midFile.getLine() + "\n";
          } else {
            int i = 8;
            int j = 16;
            line = String.format("%4s", Integer.toHexString(loc)).toUpperCase().replaceAll(" ", "0") + " "
                + midFile.getBlk() + "    " + String.format("%8s", obcode.substring(0, 8)) + "   " + midFile.getLine()
                + "\n";
            while (i < obcode.length()) {
              if (j > obcode.length())
                j = obcode.length();
              line += "       " + String.format("%8s", obcode.substring(i, j) + "\n");
              i = j;
              j += 8;
            }
          }
          fwLISFILE.write(line);
          if (midFile.getErrorMessage().length() > 0) {
            fwOBJFILE = null;
            fwLISFILE.write(midFile.getErrorMessage());
          }
          // tCard
          if (fwOBJFILE != null) {
            if (midFile.getBlk() == 1) {
              loc = midFile.getValue() + defaultLoc + startAddr;
            } else if (midFile.getBlk() == 2) {
              loc = midFile.getValue() + defaultLoc + cdataLoc + startAddr;
            } else {
              loc = midFile.getValue() + startAddr;
            }
            if (tCard.length() == 0) {
              tCard = "T" + String.format("%6s", Integer.toHexString(loc)).replaceAll(" ", "0").toUpperCase() + "XX"
                  + obcode;
            } else if (tCard.length() - 9 + obcode.length() <= 30 * 2) {
              tCard += obcode;
            } else {
              tCard = tCard
                  .replaceAll("XX",
                      String.format("%2s", Integer.toHexString((tCard.length() - 9) / 2)).replaceAll(" ", "0"))
                  .toUpperCase();
              fwOBJFILE.write(tCard + "\n");
              tCard = "T" + String.format("%6s", Integer.toHexString(loc)).replaceAll(" ", "0").toUpperCase() + "XX"
                  + obcode;
            }
          }
          //
        } else {
          switch (opcode) {
          case "START":
            line = "                     " + midFile.getLine() + "\n";
            fwLISFILE.write(line);
            if (midFile.getErrorMessage().length() > 0) {
              fwOBJFILE = null;
              fwLISFILE.write(midFile.getErrorMessage());
            }
            break;
          case "LTORG":
            line = "                     " + midFile.getLine() + "\n";
            fwLISFILE.write(line);
            if (midFile.getErrorMessage().length() > 0) {
              fwOBJFILE = null;
              fwLISFILE.write(midFile.getErrorMessage());
            }
            break;
          case "USE":
            loc = midFile.getValue();
            line = String.format("%4s",
                String.format("%4s", Integer.toHexString(loc)).toUpperCase().replaceAll(" ", "0") + " "
                    + midFile.getBlk() + "    ");
            line += "           " + midFile.getLine() + "\n";
            // tCard
            if (fwOBJFILE != null) {
              if (blk != midFile.getBlk()) {
                if (tCard.length() > 0) {
                  tCard = tCard
                      .replaceAll("XX",
                          String.format("%2s", Integer.toHexString((tCard.length() - 9) / 2)).replaceAll(" ", "0"))
                      .toUpperCase();
                  fwOBJFILE.write(tCard + "\n");
                  tCard = "";
                }
              }
            }
            blk = midFile.getBlk();
            fwLISFILE.write(line);
            if (midFile.getErrorMessage().length() > 0) {
              fwOBJFILE = null;
              fwLISFILE.write(midFile.getErrorMessage());
            }
            break;
          case "RESW":
          case "RESB":
            loc = midFile.getValue();
            line = String.format("%4s",
                String.format("%4s", Integer.toHexString(loc)).toUpperCase().replaceAll(" ", "0") + " "
                    + midFile.getBlk() + "    ");
            line += "           " + midFile.getLine() + "\n";
            fwLISFILE.write(line);

            if (midFile.getErrorMessage().length() > 0) {
              fwOBJFILE = null;
              fwLISFILE.write(midFile.getErrorMessage());
            }
            // tCard
            if (fwOBJFILE != null) {
              if (tCard.length() > 0) {
                tCard = tCard
                    .replaceAll("XX",
                        String.format("%2s", Integer.toHexString((tCard.length() - 9) / 2)).replaceAll(" ", "0"))
                    .toUpperCase();
                fwOBJFILE.write(tCard + "\n");
                tCard = "";
              }
            }
            break;
          case "END":
            if (operand.length() > 0) {
              tempOperand = Expressions.calculation(operand, pass1);
              if (tempOperand[1] >= 0) {
                eCard += "E"
                    + String.format("%6s", Integer.toHexString(tempOperand[0])).toUpperCase().replaceAll(" ", "0")
                    + "\n";
              } else {
                if (tempOperand[1] == -1) {
                  midFile.addErrorMessage(ErrorMsg.endSymDef);
                } else if (tempOperand[1] == -2) {
                  midFile.addErrorMessage(ErrorMsg.endAorR);
                }
                eCard = "";
              }
            } else {
              eCard += "E\n";
            }
            line = "       " + "   " + "        " + "   " + midFile.getLine() + "\n";
            fwLISFILE.write(line);
            if (midFile.getErrorMessage().length() > 0) {
              fwOBJFILE = null;
              fwLISFILE.write(midFile.getErrorMessage());
            }
            break;
          case "BYTE":
            loc = midFile.getValue();
            if (operand.length() > 3) {
              if (operand.charAt(0) == 'X' && operand.charAt(1) == '\''
                  && operand.charAt(operand.length() - 1) == '\'') {
                if ((operand.length() - 3) % 2 == 0) {
                  obcode = "";
                  operand = operand.toUpperCase();
                  for (int i = 2; i < operand.length() - 1; i++) {
                    if (operand.charAt(i) >= '0' && operand.charAt(i) <= '9') {
                      obcode += operand.charAt(i);
                    } else if (operand.charAt(i) >= 'A' && operand.charAt(i) <= 'F') {
                      obcode += operand.charAt(i);
                    } else { // error
                      midFile.addErrorMessage(ErrorMsg.byteHex);
                      obcode = "";
                      break;
                    }
                  }
                } else {
                  obcode = "";
                }
              } else if (operand.charAt(0) == 'C' && operand.charAt(1) == '\''
                  && operand.charAt(operand.length() - 1) == '\'') {
                obcode = "";
                for (int i = 2; i < operand.length() - 1; i++) {
                  obcode += String.format("%2s", Integer.toHexString(operand.charAt(i)).replaceAll(" ", "0"));
                }
                obcode = obcode.toUpperCase();
              } else {
                midFile.addErrorMessage(ErrorMsg.byteFormat);
                obcode = "";
              }
            } else {
              midFile.addErrorMessage(ErrorMsg.byteFormat);
              obcode = "";
            }
            if (obcode.length() <= 8) {
              line = String.format("%4s", Integer.toHexString(loc)).toUpperCase().replaceAll(" ", "0") + " "
                  + midFile.getBlk() + "    " + String.format("%8s", obcode) + "   " + midFile.getLine() + "\n";
            } else {
              int i = 8;
              int j = 16;
              line = String.format("%4s", Integer.toHexString(loc)).toUpperCase().replaceAll(" ", "0") + " "
                  + midFile.getBlk() + "    " + String.format("%8s", obcode.substring(0, 8)) + "   " + midFile.getLine()
                  + "\n";
              while (i < obcode.length()) {
                if (j > obcode.length())
                  j = obcode.length();
                line += "       " + String.format("%8s", obcode.substring(i, j) + "\n");
                i = j;
                j += 8;
              }
            }
            fwLISFILE.write(line);
            if (midFile.getErrorMessage().length() > 0) {
              fwOBJFILE = null;
              fwLISFILE.write(midFile.getErrorMessage());
            }
            // tCard
            if (fwOBJFILE != null) {
              if (midFile.getBlk() == 1) {
                loc = midFile.getValue() + defaultLoc + startAddr;
              } else if (midFile.getBlk() == 2) {
                loc = midFile.getValue() + defaultLoc + cdataLoc + startAddr;
              } else {
                loc = midFile.getValue() + startAddr;
              }
              if (tCard.length() == 0) {
                tCard = "T" + String.format("%6s", Integer.toHexString(loc)).replaceAll(" ", "0").toUpperCase() + "XX"
                    + obcode;
              } else if (tCard.length() - 9 + obcode.length() <= 30 * 2) {
                tCard += obcode;
              } else {
                tCard = tCard
                    .replaceAll("XX",
                        String.format("%2s", Integer.toHexString((tCard.length() - 9) / 2)).replaceAll(" ", "0"))
                    .toUpperCase();
                fwOBJFILE.write(tCard + "\n");
                tCard = "T" + String.format("%6s", Integer.toHexString(loc)).replaceAll(" ", "0").toUpperCase() + "XX"
                    + obcode;
              }
            }
            break;
          case "WORD":
            loc = midFile.getValue();
            if (operand.length() > 0) {
              tempOperand = Expressions.calculation(operand, pass1);
              if (tempOperand[1] >= 0 && tempOperand[0] <= 16777215) {
                obcode = String.format("%6s", Integer.toHexString(tempOperand[0])).toUpperCase().replaceAll(" ", "0");
                line = String.format("%4s", Integer.toHexString(loc).toUpperCase()).replaceAll(" ", "0") + " "
                    + midFile.getBlk();
                line += "      " + obcode + "   " + midFile.getLine() + "\n";
              } else {
                line = "       " + "   " + "        " + "   " + midFile.getLine() + "\n";
                if (tempOperand[1] == -1) {
                  midFile.addErrorMessage(ErrorMsg.wordSymDef);
                } else if (tempOperand[1] == -2) {
                  midFile.addErrorMessage(ErrorMsg.wordAorR);
                } else {
                  midFile.addErrorMessage(ErrorMsg.wordLarge);
                }
              }
            } else {
              line = String.format("%4s", Integer.toHexString(loc).toUpperCase()).replaceAll(" ", "0") + " "
                  + midFile.getBlk();
              line += "    " + "        " + "   " + midFile.getLine() + "\n";
              midFile.addErrorMessage(ErrorMsg.wordMissOp);
            }
            fwLISFILE.write(line);
            if (midFile.getErrorMessage().length() > 0) {
              fwOBJFILE = null;
              fwLISFILE.write(midFile.getErrorMessage());
            }
            // tCard
            if (fwOBJFILE != null) {
              if (midFile.getBlk() == 1) {
                loc = midFile.getValue() + defaultLoc + startAddr;
              } else if (midFile.getBlk() == 2) {
                loc = midFile.getValue() + defaultLoc + cdataLoc + startAddr;
              } else {
                loc = midFile.getValue() + startAddr;
              }
              if (tCard.length() == 0) {
                tCard = "T" + String.format("%6s", Integer.toHexString(loc)).replaceAll(" ", "0").toUpperCase() + "XX"
                    + obcode;
              } else if (tCard.length() - 9 + obcode.length() <= 30 * 2) {
                tCard += obcode;
              } else {
                tCard = tCard
                    .replaceAll("XX",
                        String.format("%2s", Integer.toHexString((tCard.length() - 9) / 2)).replaceAll(" ", "0"))
                    .toUpperCase();
                fwOBJFILE.write(tCard + "\n");
                tCard = "T" + String.format("%6s", Integer.toHexString(loc)).replaceAll(" ", "0").toUpperCase() + "XX"
                    + obcode;
              }
            }

            break;
          case "BASE":
            if (operand.length() > 0) {
              if (operand.length() == 1 && operand.charAt(0) == '*') {
                if (midFile.getBlk() == 0) {
                  base = midFile.getValue() + startAddr;
                } else if (midFile.getBlk() == 1) {
                  base = midFile.getValue() + defaultLoc + startAddr;
                } else {
                  base = midFile.getValue() + defaultLoc + cdataLoc + startAddr;
                }
              } else {
                tempOperand = Expressions.calculation(operand, pass1);
                if (tempOperand[1] >= 0) {
                  base = tempOperand[0];
                  line = "       " + "   " + "        " + "   " + midFile.getLine() + "\n";
                } else { // base error
                  line = "       " + "   " + "        " + "   " + midFile.getLine() + "\n";
                  if (tempOperand[1] == -1)
                    midFile.addErrorMessage(ErrorMsg.baseSymDef);
                  else
                    midFile.addErrorMessage(ErrorMsg.baseAorR);
                }
              }
            } else {
              line = "       " + "   " + "        " + "   " + midFile.getLine() + "\n";
              midFile.addErrorMessage(ErrorMsg.baseMissOp);
            }

            fwLISFILE.write(line);
            if (midFile.getErrorMessage().length() > 0) {
              fwOBJFILE = null;
              fwLISFILE.write(midFile.getErrorMessage());
            }
            break;
          case "EQU":
            loc = midFile.getValue();
            if (midFile.getBlk() != -1) {
              line = String.format("%4s", Integer.toHexString(loc).toUpperCase()).replaceAll(" ", "0") + " "
                  + midFile.getBlk();
            } else {
              line = String.format("%4s", Integer.toHexString(loc).toUpperCase()).replaceAll(" ", "0") + "  ";
            }

            line += "    " + "        " + "   " + midFile.getLine() + "\n";
            fwLISFILE.write(line);
            if (midFile.getErrorMessage().length() > 0) {
              fwOBJFILE = null;
              fwLISFILE.write(midFile.getErrorMessage());
            }
            break;
          default:
            tempOpcode = OpTab.getOpCode(opcode);

            if (tempOpcode[0] != -1) {
              if (midFile.getBlk() == 1) {
                loc = midFile.getValue() + defaultLoc + startAddr;
              } else if (midFile.getBlk() == 2) {
                loc = midFile.getValue() + defaultLoc + cdataLoc + startAddr;
              } else {
                loc = midFile.getValue() + startAddr;
              }
              switch (tempOpcode[1]) {
              case 1:
                if (operand.length() > 0) { // operand error
                  midFile.addErrorMessage(ErrorMsg.format1ExOp);
                }
                obcode = String.format("%2s", Integer.toHexString(tempOpcode[0])).toUpperCase().replaceAll(" ", "0");
                line = String.format("%4s", Integer.toHexString(midFile.getValue()).toUpperCase()).replaceAll(" ", "0")
                    + " " + midFile.getBlk();
                line += "          " + obcode + "   " + midFile.getLine() + "\n";
                fwLISFILE.write(line);
                if (midFile.getErrorMessage().length() > 0) {
                  fwOBJFILE = null;
                  fwLISFILE.write(midFile.getErrorMessage());
                }
                break;
              case 2:
                obcode = String.format("%2s", Integer.toHexString(tempOpcode[0])).toUpperCase().replaceAll(" ", "0");
                if (operand.length() > 0) {
                  if (operand.indexOf(',') > 0 && operand.indexOf(',') != operand.length() - 1) {
                    switch (operand.substring(0, operand.indexOf(','))) {
                    case "A":
                      obcode += "0";
                      break;
                    case "X":
                      obcode += "1";
                      break;
                    case "L":
                      obcode += "2";
                      break;
                    case "B":
                      obcode += "3";
                      break;
                    case "S":
                      obcode += "4";
                      break;
                    case "T":
                      obcode += "5";
                      break;
                    case "F":
                      obcode += "6";
                      break;
                    case "PC":
                      obcode += "8";
                      break;
                    case "SW":
                      obcode += "9";
                      break;
                    default:
                      midFile.addErrorMessage(ErrorMsg.format2IllOp);
                      break;
                    }
                    if (obcode.length() == 3) {
                      switch (operand.substring(operand.indexOf(',') + 1, operand.length())) {
                      case "A":
                        obcode += "0";
                        break;
                      case "X":
                        obcode += "1";
                        break;
                      case "L":
                        obcode += "2";
                        break;
                      case "B":
                        obcode += "3";
                        break;
                      case "S":
                        obcode += "4";
                        break;
                      case "T":
                        obcode += "5";
                        break;
                      case "F":
                        obcode += "6";
                        break;
                      case "PC":
                        obcode += "8";
                        break;
                      case "SW":
                        obcode += "9";
                        break;
                      default:
                        midFile.addErrorMessage(ErrorMsg.format2IllOp);
                        break;
                      }
                    }
                  } else {
                    switch (operand) {
                    case "A":
                      obcode += "00";
                      break;
                    case "X":
                      obcode += "10";
                      break;
                    case "L":
                      obcode += "20";
                      break;
                    case "B":
                      obcode += "30";
                      break;
                    case "S":
                      obcode += "40";
                      break;
                    case "T":
                      obcode += "50";
                      break;
                    case "F":
                      obcode += "60";
                      break;
                    case "PC":
                      obcode += "80";
                      break;
                    case "SW":
                      obcode += "90";
                      break;
                    default:
                      midFile.addErrorMessage(ErrorMsg.format2IllOp);
                      break;
                    }
                  }
                }
                line = String.format("%4s", Integer.toHexString(midFile.getValue()).toUpperCase()).replaceAll(" ", "0")
                    + " " + midFile.getBlk();
                line += "    " + String.format("%8s", obcode) + "   " + midFile.getLine() + "\n";
                fwLISFILE.write(line);
                if (midFile.getErrorMessage().length() > 0) {
                  fwOBJFILE = null;
                  fwLISFILE.write(midFile.getErrorMessage());
                }
                break;
              case 3:
                if (tempOpcode[0] == 76) { // RSUB
                  if (operand.length() == 0) {
                    if (tempLine.charAt(9) == ' ')
                      obcode = "4F0000";
                    else if (tempLine.charAt(9) == '+')
                      obcode = "4F000000";
                  } else { // RSUB operand error
                    midFile.addErrorMessage(ErrorMsg.rsubOperand);
                  }
                } else {
                  temp = tempOpcode[0];
                  xbpe = 0;
                  if (operand.toUpperCase().indexOf(",X") == -1) {
                    tempOperand = Expressions.calculation(operand, pass1);
                  } else {
                    xbpe += 8;
                    tempOperand = Expressions.calculation(operand.substring(0, operand.toUpperCase().indexOf(",X")),
                        pass1);
                  }
                  if (tempLine.charAt(17) == ' ' || tempLine.charAt(17) == '=') {
                    temp += 3;
                  } else if (tempLine.charAt(17) == '@') {
                    temp += 2;
                  } else if (tempLine.charAt(17) == '#') {
                    temp += 1;
                  } else { // mode Error

                  }
                  if (tempLine.charAt(9) == ' ') {
                    pc = loc + 3;
                    if (tempLine.charAt(17) == '=') {
                      if (litTab != null && litTab.searchLitTab(operand) != null) {
                        if (litTab.searchLitTab(operand).getBlk() == 0) {
                          disp = litTab.searchLitTab(operand).getValue() + startAddr;
                        } else if (litTab.searchLitTab(operand).getBlk() == 1) {
                          disp = litTab.searchLitTab(operand).getValue() + defaultLoc + startAddr;
                        } else {
                          disp = litTab.searchLitTab(operand).getValue() + defaultLoc + cdataLoc + startAddr;
                        }

                        disp -= pc;
                        if (disp <= 2047 && disp >= -2048) {

                          xbpe += 2;
                          obcode = String.format("%2s", Integer.toHexString(temp).toUpperCase()).replaceAll(" ", "0");
                          obcode += Integer.toHexString(xbpe).toUpperCase();
                          if (disp < 0) {
                            obcode += String.format("%3s", Integer.toHexString(disp).substring(5, 8).toUpperCase())
                                .replaceAll(" ", "0");
                          } else {
                            obcode += String.format("%3s", Integer.toHexString(disp).toUpperCase()).replaceAll(" ",
                                "0");
                          }

                        } else {
                          disp = tempOperand[0] - base;
                          if (disp <= 4095 && disp >= 0) {
                            xbpe += 4;
                            obcode = String.format("%2s", Integer.toHexString(temp).toUpperCase()).replaceAll(" ", "0");
                            obcode += Integer.toHexString(xbpe).toUpperCase();
                            if (disp < 0) {
                              obcode += String.format("%3s", Integer.toHexString(disp).substring(5, 8).toUpperCase())
                                  .replaceAll(" ", "0");
                            } else {
                              obcode += String.format("%3s", Integer.toHexString(disp).toUpperCase()).replaceAll(" ",
                                  "0");
                            }
                          } else { // base && PC error
                            obcode = String.format("%2s", Integer.toHexString(temp).toUpperCase()).replaceAll(" ", "0");

                            midFile.addErrorMessage(ErrorMsg.format3IllOp);
                          }
                        }
                      }
                    } else {
                      if (tempOperand[1] >= 0) {
                        if (tempOperand[1] == 1) { // PC or base
                          disp = tempOperand[0] - pc;
                          if (disp <= 2047 && disp >= -2048) {
                            xbpe += 2;
                            obcode = String.format("%2s", Integer.toHexString(temp).toUpperCase()).replaceAll(" ", "0");
                            obcode += Integer.toHexString(xbpe).toUpperCase();
                            if (disp < 0) {
                              obcode += String.format("%3s", Integer.toHexString(disp).substring(5, 8).toUpperCase())
                                  .replaceAll(" ", "0");
                            } else {
                              obcode += String.format("%3s", Integer.toHexString(disp).toUpperCase()).replaceAll(" ",
                                  "0");
                            }

                          } else {
                            disp = tempOperand[0] - base;
                            if (disp <= 4095 && disp >= 0) {
                              xbpe += 4;
                              obcode = String.format("%2s", Integer.toHexString(temp).toUpperCase()).replaceAll(" ",
                                  "0");
                              obcode += Integer.toHexString(xbpe).toUpperCase();
                              if (disp < 0) {
                                obcode += String.format("%3s", Integer.toHexString(disp).substring(5, 8).toUpperCase())
                                    .replaceAll(" ", "0");
                              } else {
                                obcode += String.format("%3s", Integer.toHexString(disp).toUpperCase()).replaceAll(" ",
                                    "0");
                              }
                            } else { // base && PC error
                              obcode = String.format("%2s", Integer.toHexString(temp).toUpperCase()).replaceAll(" ",
                                  "0");
                              midFile.addErrorMessage(ErrorMsg.format3IllOp);
                            }
                          }
                        } else if (tempOperand[1] == 0) {
                          obcode = String.format("%2s", Integer.toHexString(temp).toUpperCase()).replaceAll(" ", "0");
                          obcode += Integer.toHexString(xbpe).toUpperCase();
                          if (tempOperand[0] >= 0) {
                            obcode += String.format("%3s", Integer.toHexString(tempOperand[0]).toUpperCase())
                                .replaceAll(" ", "0");
                          } else {
                            obcode += String
                                .format("%3s", Integer.toHexString(tempOperand[0]).substring(5, 8).toUpperCase())
                                .replaceAll(" ", "0");
                          }
                        }
                      } else { // operand error
                        if (tempOperand[1] == -1) {
                          midFile.addErrorMessage(ErrorMsg.symUnDef);
                        } else {
                          midFile.addErrorMessage(ErrorMsg.format3IllOp);
                        }
                      }
                    }
                  } else if (tempLine.charAt(9) == '+') {
                    xbpe += 1;
                    obcode = String.format("%2s", Integer.toHexString(temp).toUpperCase()).replaceAll(" ", "0");
                    if (tempOperand[1] == 1) { // mCard
                      obcode += Integer.toHexString(xbpe).toUpperCase();
                      if (tempOperand[0] < 0) {
                        obcode += String
                            .format("%5s", Integer.toHexString(tempOperand[0]).substring(3, 8).toUpperCase())
                            .replaceAll(" ", "0");
                      } else {
                        obcode += String.format("%5s", Integer.toHexString(tempOperand[0]).toUpperCase())
                            .replaceAll(" ", "0");
                      }
                      mCard += "M"
                          + String.format("%6s", Integer.toHexString(loc + 1).toUpperCase()).replaceAll(" ", "0");
                      mCard += "05+" + progName + "\n";
                    } else if (tempOperand[1] == 0) {
                      obcode += Integer.toHexString(xbpe).toUpperCase();
                      if (tempOperand[0] < 0) {
                        obcode += String
                            .format("%5s", Integer.toHexString(tempOperand[0]).substring(3, 8).toUpperCase())
                            .replaceAll(" ", "0");
                      } else {
                        obcode += String.format("%5s", Integer.toHexString(tempOperand[0]).toUpperCase())
                            .replaceAll(" ", "0");
                      }
                    } else { // operand error
                      if (tempOperand[1] == -1) {
                        midFile.addErrorMessage(ErrorMsg.symUnDef);
                      } else {
                        midFile.addErrorMessage(ErrorMsg.format4IllOp);
                      }
                    }
                  } else { // mode error **
                    midFile.addErrorMessage(ErrorMsg.insFormat);
                  }
                }
                line = String.format("%4s", Integer.toHexString(midFile.getValue())).toUpperCase().replaceAll(" ", "0") + " "
                    + midFile.getBlk() + "    " + String.format("%8s", obcode) + "   " + midFile.getLine() + "\n";
                fwLISFILE.write(line);
                if (midFile.getErrorMessage().length() > 0) {
                  fwOBJFILE = null;
                  fwLISFILE.write(midFile.getErrorMessage());
                }
                break;
              }
            } else {
              line = "                     " + midFile.getLine() + "\n";
              fwLISFILE.write(line);
              if (midFile.getErrorMessage().length() > 0) {
                fwOBJFILE = null;
                fwLISFILE.write(midFile.getErrorMessage());
              }
            }
            if (midFile.getErrorMessage().length() > 0)
              fwOBJFILE = null;

            if (fwOBJFILE != null) {
              if (tCard.length() == 0) {
                tCard = "T" + String.format("%6s", Integer.toHexString(loc)).replaceAll(" ", "0").toUpperCase() + "XX"
                    + obcode;
              } else if (tCard.length() - 9 + obcode.length() <= 30 * 2) {
                tCard += obcode;
              } else {
                tCard = tCard
                    .replaceAll("XX",
                        String.format("%2s", Integer.toHexString((tCard.length() - 9) / 2)).replaceAll(" ", "0"))
                    .toUpperCase();

                fwOBJFILE.write(tCard + "\n");
                tCard = "T" + String.format("%6s", Integer.toHexString(loc)).replaceAll(" ", "0").toUpperCase() + "XX"
                    + obcode;
              }
            } //
            break;
          }
        }
      }

      if (midFile.getErrorMessage().length() > 0)
        fwOBJFILE = null;

      midFile = midFile.getNext();
    }
    
    if (fwOBJFILE != null) {
      if (tCard.length() > 0) {
        tCard = tCard
            .replaceAll("XX", String.format("%2s", Integer.toHexString((tCard.length() - 9) / 2)).replaceAll(" ", "0"))
            .toUpperCase();
        fwOBJFILE.write(tCard + "\n");
      }

      fwOBJFILE.write(mCard);
      fwOBJFILE.write(eCard);
    }

    fwLISFILE.write("                     .\n");
    fwLISFILE.write("                     .\n");
    fwLISFILE.write("                     .\n");
    fwLISFILE.write(" **********************************************\n");
    fwLISFILE.write(" * Block name  Block number  Address  Length  *\n");
    fwLISFILE.write(" *--------------------------------------------*\n");
    fwLISFILE.write(" *  default         0         "
        + String.format("%4s", Integer.toHexString(startAddr).toUpperCase()).replaceAll(" ", "0") + "     "
        + String.format("%4s", Integer.toHexString(defaultLoc).toUpperCase()).replaceAll(" ", "0") + "   *\n");
    fwLISFILE.write(" *  CDATA           1         "
        + String.format("%4s", Integer.toHexString(startAddr + defaultLoc).toUpperCase()).replaceAll(" ", "0") + "     "
        + String.format("%4s", Integer.toHexString(cdataLoc).toUpperCase()).replaceAll(" ", "0") + "   *\n");
    fwLISFILE.write(" *  CBLKS           2         "
        + String.format("%4s", Integer.toHexString(startAddr + defaultLoc + cdataLoc).toUpperCase()).replaceAll(" ", "0") + "     "
        + String.format("%4s", Integer.toHexString(cblksLoc).toUpperCase()).replaceAll(" ", "0") + "   *\n");
    fwLISFILE.write(" *--------------------------------------------*\n");
    fwLISFILE.write(" **********************************************\n");
  }
}
