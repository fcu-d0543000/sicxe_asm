package sicxe;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PassOne {
  private MidFile midFile;
  private BufferedReader br;
  private int AorR;
  private int startAddr;
  private int nowBlk;
  private SymTab symTab;
  private int defaultLoc = 0;
  private int cdataLoc = 0;
  private int cblksLoc = 0;
  private LitTab litTab;
  private String startLine;
  private Boolean ifStart = true;
  private Boolean findStart = false;
  public static int blkDefault = 0;
  public static int blkCdata = 1;
  public static int blkCblks = 2;

  public PassOne(FileReader fr) {
    midFile = null;
    this.br = new BufferedReader(fr);
    startAddr = 0;
    nowBlk = blkDefault;
  }

  public void findStart() throws IOException {
    String line;
    String tempLine;
    String opcode;
    String operand;
    String label;

    while (br.ready()) {
      line = br.readLine();
      tempLine = line.trim();
      if (tempLine.length() == 0 || tempLine.charAt(0) == '.') {
        if (midFile == null) {
          midFile = new MidFile(-1, 0, line);
        } else {
          midFile.insertNew(new MidFile(-1, 0, line));
        }
      } else {
        tempLine = line;
        while (tempLine.length() < 36) {
          tempLine += " ";
        }
        label = tempLine.substring(0, 8).trim();
        opcode = tempLine.substring(10, 16).trim();
        operand = tempLine.substring(18, 36).trim();

        if (opcode.trim().equalsIgnoreCase("START")) {
          findStart = true;
          if (operand.length() > 0) {
            try {
              if (midFile == null) {
                midFile = new MidFile(0, 0, line);
              } else {
                midFile.insertNew(new MidFile(0, 0, line));
              }
              startAddr = Integer.parseInt(operand, 16);
            } catch (Exception e) {
              startAddr = 0;
              midFile.setErrorMessage(ErrorMsg.startHex);
            }
          } else {
            if (midFile == null) {
              midFile = new MidFile(0, 0, line);
            } else {
              midFile.insertNew(new MidFile(0, 0, line));
            }
            startAddr = 0;
            midFile.setErrorMessage(ErrorMsg.startMissOp);
          }
          if (label.length() == 0) {
            midFile.setErrorMessage(ErrorMsg.startMissLa);
          }
        } else {
          startAddr = 0;
          ifStart = false;
          startLine = tempLine;
        }
        break;// end
      }
    }
  }

  public void findLoc() throws IOException {
    Boolean ifEnd = true;
    String line;
    String tempLine;
    String label;
    String opcode;
    String operand;
    int[] tempOpcode;
    int[] tempOperand;
    int length;
    int loc;

    if (startAddr == 0) {
      AorR = 1;
    } else {
      AorR = 0;
    }
    // defaultLoc = startAddr;
    loc = defaultLoc;

    while (br.ready() && ifEnd) {

      if (ifStart) {
        line = br.readLine();
      } else {
        line = startLine;
        // ifStart = true;
      }

      tempLine = line;
      if (tempLine.trim().length() == 0 || tempLine.charAt(0) == '.') {
        midFile.insertNew(new MidFile(-1, 0, line));
      } else {
        while (tempLine.length() < 36) {
          tempLine += " ";
        }
        label = tempLine.substring(0, 8).trim();
        opcode = tempLine.substring(10, 16).trim().toUpperCase();
        operand = tempLine.substring(18, 36).trim();

        if (opcode.length() > 0) {
          switch (opcode) {
          case "START":

            midFile.insertNew(new MidFile(0, 0, line));

            if (findStart) {
              midFile.setErrorMessage(ErrorMsg.startDup);
            } else {
              midFile.setErrorMessage(ErrorMsg.startWrongWay);
              findStart = true;
            }
            break;
          case "EQU":
            if (operand.length() > 0) {
              if (operand.equals("*")) {
                if (midFile != null) {
                  midFile.insertNew(new MidFile(loc, nowBlk, line));
                  if (!ifStart) {
                    ifStart = true;
                    midFile.setErrorMessage(ErrorMsg.startMiss1st);
                  }
                } else {
                  midFile.insertNew(new MidFile(loc, nowBlk, line));
                  midFile.setErrorMessage(ErrorMsg.startMiss1st);
                }
                if (label.length() > 0) {
                  if (symTab == null) {
                    symTab = new SymTab(label, loc, AorR, nowBlk);
                  } else if (symTab.searchSymTab(label) == null) {
                    symTab.insertNew(new SymTab(label, loc, AorR, nowBlk));
                  } else {
                    midFile.setErrorMessage(ErrorMsg.labelDefinition);
                  }
                }
              } else {
                tempOperand = Expressions.calculation(operand, this);
                if (tempOperand[1] == SymTab.RELATIVE || tempOperand[1] == SymTab.ABSOLUTE) {
                  if (tempOperand[1] == SymTab.RELATIVE) {
                    if (midFile != null) {
                      midFile.insertNew(new MidFile(tempOperand[0], nowBlk, line));
                      if (!ifStart) {
                        ifStart = true;
                        midFile.setErrorMessage(ErrorMsg.startMiss1st);
                      }
                    } else {
                      midFile = new MidFile(tempOperand[0], nowBlk, line);
                      midFile.setErrorMessage(ErrorMsg.startMiss1st);
                    }
                  } else if (tempOperand[1] == SymTab.ABSOLUTE) {
                    midFile.insertNew(new MidFile(tempOperand[0], -1, line));
                    if (midFile != null) {
                      midFile.insertNew(new MidFile(tempOperand[0], -1, line));
                      if (!ifStart) {
                        ifStart = true;
                        midFile.setErrorMessage(ErrorMsg.startMiss1st);
                      }
                    } else {
                      midFile = new MidFile(tempOperand[0], -1, line);
                      midFile.setErrorMessage(ErrorMsg.startMiss1st);
                    }
                  }
                  if (label.length() > 0) {
                    if (symTab == null) {
                      if (tempOperand[1] == SymTab.RELATIVE) {
                        symTab = new SymTab(label, tempOperand[0], tempOperand[1], nowBlk);
                      } else if (tempOperand[1] == SymTab.ABSOLUTE) {
                        symTab = new SymTab(label, tempOperand[0], tempOperand[1], 0);
                      }
                    } else if (symTab.searchSymTab(label) == null) {
                      if (tempOperand[1] == SymTab.RELATIVE) {
                        symTab.insertNew(new SymTab(label, tempOperand[0], tempOperand[1], nowBlk));
                      } else if (tempOperand[1] == SymTab.ABSOLUTE) {
                        symTab.insertNew(new SymTab(label, tempOperand[0], tempOperand[1], 0));
                      }
                    } else {
                      midFile.setErrorMessage(ErrorMsg.labelDefinition);
                    }
                  } else {
                    midFile.setErrorMessage(ErrorMsg.equMissLa);
                  }
                } else {
                  if (midFile != null) {
                    midFile.insertNew(new MidFile(-1, 0, line));
                    if (!ifStart) {
                      ifStart = true;
                      midFile.setErrorMessage(ErrorMsg.startMiss1st);
                    }
                  } else {
                    midFile = new MidFile(-1, 0, line);
                    midFile.setErrorMessage(ErrorMsg.startMiss1st);
                  }
                  if (tempOperand[1] == -1) {
                    midFile.setErrorMessage(ErrorMsg.equSymDef);
                  } else if (tempOperand[1] == -2) {
                    midFile.setErrorMessage(ErrorMsg.equAorR);
                  }

                }
              }
            } else { // Missing operand
              if (midFile != null) {
                midFile.insertNew(new MidFile(-1, 0, line));
                if (!ifStart) {
                  ifStart = true;
                  midFile.setErrorMessage(ErrorMsg.startMiss1st);
                }
              } else {
                midFile = new MidFile(-1, 0, line);
                midFile.setErrorMessage(ErrorMsg.startMiss1st);
              }
              midFile.setErrorMessage(ErrorMsg.equMissOp);
            }
            break;
          case "USE":
            switch (nowBlk) {
            case 0:
              defaultLoc = loc;
              break;
            case 1:
              cdataLoc = loc;
              break;
            case 2:
              cblksLoc = loc;
              break;
            }
            if (operand.length() > 0) {
              if (operand.equalsIgnoreCase("DEFAULT")) {
                nowBlk = blkDefault;
                loc = defaultLoc;
                if (midFile != null) {
                  midFile.insertNew(new MidFile(loc, nowBlk, line));
                  if (!ifStart) {
                    ifStart = true;
                    midFile.setErrorMessage(ErrorMsg.startMiss1st);
                  }
                } else {
                  midFile = new MidFile(loc, nowBlk, line);
                  midFile.setErrorMessage(ErrorMsg.startMiss1st);
                }
              } else if (operand.equalsIgnoreCase("CDATA")) {
                nowBlk = blkCdata;
                loc = cdataLoc;
                if (midFile != null) {
                  midFile.insertNew(new MidFile(loc, nowBlk, line));
                  if (!ifStart) {
                    ifStart = true;
                    midFile.setErrorMessage(ErrorMsg.startMiss1st);
                  }
                } else {
                  midFile = new MidFile(loc, nowBlk, line);
                  midFile.setErrorMessage(ErrorMsg.startMiss1st);
                }
              } else if (operand.equalsIgnoreCase("CBLKS")) {
                nowBlk = blkCblks;
                loc = cblksLoc;
                if (midFile != null) {
                  midFile.insertNew(new MidFile(loc, nowBlk, line));
                  if (!ifStart) {
                    ifStart = true;
                    midFile.setErrorMessage(ErrorMsg.startMiss1st);
                  }
                } else {
                  midFile = new MidFile(loc, nowBlk, line);
                  midFile.setErrorMessage(ErrorMsg.startMiss1st);
                }
              } else {
                //
                if (midFile != null) {
                  midFile.insertNew(new MidFile(-1, nowBlk, line));
                  if (!ifStart) {
                    ifStart = true;
                    midFile.setErrorMessage(ErrorMsg.startMiss1st);
                  }
                } else {
                  midFile = new MidFile(-1, nowBlk, line);
                  midFile.setErrorMessage(ErrorMsg.startMiss1st);
                }
                midFile.setErrorMessage(ErrorMsg.useIllegal);
              }
            } else {
              nowBlk = blkDefault;
              loc = defaultLoc;
              midFile.insertNew(new MidFile(loc, nowBlk, line));
            }
            break;
          case "RESB":
            if (midFile != null) {
              midFile.insertNew(new MidFile(loc, nowBlk, line));
              if (!ifStart) {
                ifStart = true;
                midFile.setErrorMessage(ErrorMsg.startMiss1st);
              }
            } else {
              midFile = new MidFile(loc, nowBlk, line);
              midFile.setErrorMessage(ErrorMsg.startMiss1st);
            }

            if (label.length() > 0) {
              if (symTab == null) {
                symTab = new SymTab(label, loc, AorR, nowBlk);
              } else if (symTab.searchSymTab(label) == null) {
                symTab.insertNew(new SymTab(label, loc, AorR, nowBlk));
              } else {
                midFile.setErrorMessage(ErrorMsg.labelDefinition);
              }
            }
            if (operand.length() > 0) {
              tempOperand = Expressions.calculation(operand, this);
              if (tempOperand[1] == 0) {
                loc += tempOperand[0];
              } else {
                loc += 1;
                if (tempOperand[1] == 1) { // error
                  midFile.setErrorMessage(ErrorMsg.resbAbsolut);
                } else if (tempOperand[1] == -1) {// error
                  midFile.setErrorMessage(ErrorMsg.resbSymDef);
                } else if (tempOperand[1] == -2) {// error
                  midFile.setErrorMessage(ErrorMsg.resbAbsolut);
                }
              }
            } else {
              loc += 1;
              midFile.setErrorMessage(ErrorMsg.resbMissop);
            }
            break;
          case "RESW":
            if (midFile != null) {
              midFile.insertNew(new MidFile(loc, nowBlk, line));
              if (!ifStart) {
                ifStart = true;
                midFile.setErrorMessage(ErrorMsg.startMiss1st);
              }
            } else {
              midFile = new MidFile(loc, nowBlk, line);
              midFile.setErrorMessage(ErrorMsg.startMiss1st);
            }

            if (label.length() > 0) {
              if (symTab == null) {
                symTab = new SymTab(label, loc, AorR, nowBlk);
              } else if (symTab.searchSymTab(label) == null) {
                symTab.insertNew(new SymTab(label, loc, AorR, nowBlk));
              } else {
                midFile.setErrorMessage(ErrorMsg.labelDefinition);
              }
            }
            if (operand.length() > 0) {
              tempOperand = Expressions.calculation(operand, this);
              if (tempOperand[1] == 0) {
                loc += tempOperand[0] * 3;
              } else {
                loc += 3;
                if (tempOperand[1] == -1) {
                  midFile.setErrorMessage(ErrorMsg.reswSymDef);
                } else {
                  midFile.setErrorMessage(ErrorMsg.reswAbsolut);
                }
              }
            } else {
              loc += 3;
              midFile.setErrorMessage(ErrorMsg.reswMissOp);
            }
            break;
          case "BYTE":
            if (midFile != null) {
              midFile.insertNew(new MidFile(loc, nowBlk, line));
              if (!ifStart) {
                ifStart = true;
                midFile.setErrorMessage(ErrorMsg.startMiss1st);
              }
            } else {
              midFile = new MidFile(loc, nowBlk, line);
              midFile.setErrorMessage(ErrorMsg.startMiss1st);
            }

            if (operand.length() > 0) {
              if (operand.length() > 3) {
                if (operand.toUpperCase().charAt(0) == 'X' && operand.charAt(1) == '\''
                    && operand.charAt(operand.length() - 1) == '\'') {
                  if ((operand.length() - 3) % 2 == 0) {
                    length = (operand.length() - 3) / 2;
                  } else {
                    length = 1;
                    midFile.setErrorMessage(ErrorMsg.byteOdd);
                  }
                } else if (operand.charAt(0) == 'C' && operand.charAt(1) == '\''
                    && operand.charAt(operand.length() - 1) == '\'') {
                  length = operand.length() - 3;
                } else {
                  length = 1;
                  midFile.setErrorMessage(ErrorMsg.byteIllOp);
                  midFile.setErrorMessage(ErrorMsg.byteFormat);
                }
              } else {
                length = 1;
                midFile.setErrorMessage(ErrorMsg.byteIllOp);
                midFile.setErrorMessage(ErrorMsg.byteFormat);
              }
            } else {
              length = 1;
              midFile.setErrorMessage(ErrorMsg.byteMissOp);
            }

            if (label.length() > 0) {
              if (symTab == null) {
                symTab = new SymTab(label, loc, AorR, nowBlk);
              } else if (symTab.searchSymTab(label) == null) {
                symTab.insertNew(new SymTab(label, loc, AorR, nowBlk));
              } else {
                midFile.setErrorMessage(ErrorMsg.labelDefinition);
              }
            }

            loc += length;
            break;
          case "WORD":
            if (midFile != null) {
              midFile.insertNew(new MidFile(loc, nowBlk, line));
              if (!ifStart) {
                ifStart = true;
                midFile.setErrorMessage(ErrorMsg.startMiss1st);
              }
            } else {
              midFile = new MidFile(loc, nowBlk, line);
              midFile.setErrorMessage(ErrorMsg.startMiss1st);
            }

            if (label.length() > 0) {
              if (symTab == null) {
                symTab = new SymTab(label, loc, AorR, nowBlk);
              } else if (symTab.searchSymTab(label) == null) {
                symTab.insertNew(new SymTab(label, loc, AorR, nowBlk));
              } else {
                midFile.setErrorMessage(ErrorMsg.labelDefinition);
              }
            }
            loc += 3;
            break;
          case "BASE":
            if (midFile != null) {
              midFile.insertNew(new MidFile(loc, nowBlk, line));
              if (!ifStart) {
                ifStart = true;
                midFile.setErrorMessage(ErrorMsg.startMiss1st);
              }
            } else {
              midFile = new MidFile(loc, nowBlk, line);
              midFile.setErrorMessage(ErrorMsg.startMiss1st);
            }
            break;
          case "LTORG":
            if (midFile != null) {
              midFile.insertNew(new MidFile(0, 0, line));
              if (!ifStart) {
                ifStart = true;
                midFile.setErrorMessage(ErrorMsg.startMiss1st);
              }
            } else {
              midFile = new MidFile(0, 0, line);
              midFile.setErrorMessage(ErrorMsg.startMiss1st);
            }

            if (litTab != null) {
              while (litTab.getLitPool() != null) {
                LitTab tempL = litTab.pop();
                LitTab tempTab = litTab.searchLitTab(tempL.getName()); 
                tempTab.setBlk(nowBlk);
                tempTab.setValue(loc);
                midFile.insertNew(new MidFile(loc, nowBlk, "*        =" + tempL.getName()));
                loc += tempL.getLength();
              }
            }
            if (operand.length() != 0) {
              midFile.setErrorMessage(ErrorMsg.ltorgOp);
            }
            break;
          case "END":
            if (litTab != null) {
              while (litTab.getLitPool() != null) {
                LitTab tempL = litTab.pop();
                tempL.setBlk(nowBlk);
                tempL.setValue(loc);
                midFile.insertNew(new MidFile(loc, nowBlk, "*" + "        =" + tempL.getName()));
                loc += tempL.getLength();
              }
            }
            if (midFile != null) {
              midFile.insertNew(new MidFile(loc, nowBlk, line));
              if (!ifStart) {
                ifStart = true;
                midFile.setErrorMessage(ErrorMsg.startMiss1st);
              }
            } else {
              midFile = new MidFile(loc, nowBlk, line);
              midFile.setErrorMessage(ErrorMsg.startMiss1st);
            }

            if (label.length() > 0) {
              if (symTab == null) {
                symTab = new SymTab(label, loc, AorR, nowBlk);
              } else if (symTab.searchSymTab(label) == null) {
                symTab.insertNew(new SymTab(label, loc, AorR, nowBlk));
              } else {
                midFile.setErrorMessage(ErrorMsg.labelDefinition);
              }
            }
            ifEnd = false;
            if (nowBlk == 0) {
              defaultLoc = loc;
            } else if (nowBlk == 1) {
              cdataLoc = loc;
            } else {
              cblksLoc = loc;
            }
            break;
          default:
            if (midFile != null) {
              midFile.insertNew(new MidFile(loc, nowBlk, line));
              if (!ifStart) {
                ifStart = true;
                midFile.setErrorMessage(ErrorMsg.startMiss1st);
              }
            } else {
              midFile = new MidFile(loc, nowBlk, line);
              midFile.setErrorMessage(ErrorMsg.startMiss1st);
            }

            if (label.length() > 0) {
              if (symTab == null) {
                symTab = new SymTab(label, loc, AorR, nowBlk);
              } else if (symTab.searchSymTab(label) == null) {
                symTab.insertNew(new SymTab(label, loc, AorR, nowBlk));
              } else {
                midFile.setErrorMessage(ErrorMsg.labelDefinition);
              }
            }
            tempOpcode = OpTab.getOpCode(opcode);
            if (tempOpcode[0] != -1) {
              switch (tempOpcode[1]) {
              case 1:
                loc += 1;
                break;
              case 2:
                loc += 2;
                break;
              case 3:
                if (tempLine.charAt(17) == '=') {
                  if (operand.length() > 0) {
                    if (operand.length() > 3) {
                      if (operand.toUpperCase().charAt(0) == 'X' && operand.charAt(1) == '\''
                          && operand.charAt(operand.length() - 1) == '\'') {
                        if ((operand.length() - 3) % 2 == 0) {
                          try {
                            Integer.parseInt(operand.substring(2, operand.length() - 1));
                            length = (operand.length() - 3) / 2;
                          } catch (Exception e) {
                            length = 1;
                            midFile.setErrorMessage(ErrorMsg.literalHex);
                          }
                          LitTab newL = new LitTab(operand.toUpperCase(), length);
                          if (litTab == null) {
                            litTab = newL;
                            litTab.push(newL);
                          } else if (litTab.searchLitTab(operand.toUpperCase()) == null) {
                            litTab.insertNew(newL);
                            litTab.push(newL);
                          }
                        } else {
                          midFile.setErrorMessage(ErrorMsg.literalOdd);
                        }
                      } else if (operand.toUpperCase().charAt(0) == 'C' && operand.charAt(1) == '\''
                          && operand.charAt(operand.length() - 1) == '\'') {
                        length = operand.length() - 3;
                        //
                        LitTab newL = new LitTab(operand.toUpperCase(), length);
                        if (litTab == null) {
                          litTab = newL;
                          litTab.push(newL);
                        } else if (litTab.searchLitTab(operand.toUpperCase()) == null) {
                          litTab.insertNew(newL);
                          litTab.push(newL);
                        }
                      } else {
                        midFile.setErrorMessage(ErrorMsg.illegalLiteral);
                        midFile.setErrorMessage(ErrorMsg.literalFormat);
                      }
                    } else {
                      midFile.setErrorMessage(ErrorMsg.illegalLiteral);
                      midFile.setErrorMessage(ErrorMsg.literalFormat);
                    }
                  } else {
                    midFile.setErrorMessage(ErrorMsg.illegalLiteral);
                    midFile.setErrorMessage(ErrorMsg.literalFormat);
                  }
                }
                if (tempLine.charAt(9) == '+')
                  loc += 4;
                else
                  loc += 3;
                break;
              }
            } else {
              loc += 3;
              midFile.setErrorMessage(ErrorMsg.opcodeUnDef);
            }
            break;
          }
        } else { // µL opcode
          midFile.insertNew(new MidFile(-1, 0, line));
          midFile.setErrorMessage(ErrorMsg.opcodeMiss);
        }
      }
    }
  }

  public SymTab getSymTab() {
    return symTab;
  }

  public int getStartAddr() {
    return startAddr;
  }

  public MidFile getMidFile() {
    return midFile;
  }

  public int getDefaultLoc() {
    return defaultLoc;
  }

  public int getCdataLoc() {
    return cdataLoc;
  }

  public int getCblksLoc() {
    return cblksLoc;
  }

  public LitTab getLitTab() {
    return litTab;
  }

}
