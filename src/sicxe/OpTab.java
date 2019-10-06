package sicxe;

public class OpTab {
  public static int[] getOpCode(String opCode) {
    int[] temp = {-1, -1};
    opCode = opCode.toUpperCase().trim();
    switch (opCode) {
    case "ADD":
      temp[0] = 24;
      temp[1] = 3;
      break;
    case "AND":
      temp[0] = 64;
      temp[1] = 3;
      break;
    case "COMP":
      temp[0] = 40;
      temp[1] = 3;
      break;
    case "DIV":
      temp[0] = 36;
      temp[1] = 3;
      break;
    case "J":
      temp[0] = 60;
      temp[1] = 3;
      break;
    case "JEQ":
      temp[0] = 48;
      temp[1] = 3;
      break;
    case "JGT":
      temp[0] = 52;
      temp[1] = 3;
      break;
    case "JLT":
      temp[0] = 56;
      temp[1] = 3;
      break;
    case "JSUB":
      temp[0] = 72;
      temp[1] = 3;
      break;
    case "LDA":
      temp[0] = 0;
      temp[1] = 3;
      break;
    case "LDCH":
      temp[0] = 80;
      temp[1] = 3;
      break;
    case "LDL":
      temp[0] = 8;
      temp[1] = 3;
      break;
    case "LDX":
      temp[0] = 4;
      temp[1] = 3;
      break;
    case "MUL":
      temp[0] = 32;
      temp[1] = 3;
      break;
    case "OR":
      temp[0] = 68;
      temp[1] = 3;
      break;
    case "RD":
      temp[0] = 216;
      temp[1] = 3;
      break;
    case "RSUB":
      temp[0] = 76;
      temp[1] = 3;
      break;
    case "STA":
      temp[0] = 12;
      temp[1] = 3;
      break;
    case "STCH":
      temp[0] = 84;
      temp[1] = 3;
      break;
    case "STL":
      temp[0] = 20;
      temp[1] = 3;
      break;
    case "STSW":
      temp[0] = 232;
      temp[1] = 3;
      break;
    case "STX":
      temp[0] = 16;
      temp[1] = 3;
      break;
    case "SUB":
      temp[0] = 28;
      temp[1] = 3;
      break;
    case "TD":
      temp[0] = 224;
      temp[1] = 3;
      break;
    case "TIX":
      temp[0] = 44;
      temp[1] = 3;
      break;
    case "WD":
      temp[0] = 220;
      temp[1] = 3;
      break;
    case "ADDF":
      temp[0] = 88;
      temp[1] = 3;
      break;
    case "ADDR":
      temp[0] = 144;
      temp[1] = 2;
      break;
    case "CLEAR":
      temp[0] = 180;
      temp[1] = 2;
      break;
    case "COMPF":
      temp[0] = 136;
      temp[1] = 3;
      break;
    case "COMPR":
      temp[0] = 160;
      temp[1] = 2;
      break;
    case "DIVF":
      temp[0] = 100;
      temp[1] = 3;
      break;
    case "DIVR":
      temp[0] = 156;
      temp[1] = 2;
      break;
    case "FIX":
      temp[0] = 196;
      temp[1] = 1;
      break;
    case "FLOAT":
      temp[0] = 192;
      temp[1] = 1;
      break;
    case "HIO":
      temp[0] = 244;
      temp[1] = 1;
      break;
    case "LDB":
      temp[0] = 104;
      temp[1] = 3;
      break;
    case "LDF":
      temp[0] = 112;
      temp[1] = 3;
      break;
    case "LDS":
      temp[0] = 108;
      temp[1] = 3;
      break;
    case "LDT":
      temp[0] = 116;
      temp[1] = 3;
      break;
    case "LPS":
      temp[0] = 208;
      temp[1] = 3;
      break;
    case "MULF":
      temp[0] = 96;
      temp[1] = 3;
      break;
    case "MULR":
      temp[0] = 152;
      temp[1] = 2;
      break;
    case "NORM":
      temp[0] = 200;
      temp[1] = 1;
      break;
    case "RMO":
      temp[0] = 172;
      temp[1] = 2;
      break;
    case "SHIFTL":
      temp[0] = 164;
      temp[1] = 2;
      break;
    case "SHIFTR":
      temp[0] = 168;
      temp[1] = 2;
      break;
    case "SIO":
      temp[0] = 240;
      temp[1] = 1;
      break;
    case "SSK":
      temp[0] = 236;
      temp[1] = 3;
      break;
    case "STB":
      temp[0] = 120;
      temp[1] = 3;
      break;
    case "STF":
      temp[0] = 128;
      temp[1] = 3;
      break;
    case "STI":
      temp[0] = 212;
      temp[1] = 3;
      break;
    case "STS":
      temp[0] = 124;
      temp[1] = 3;
      break;
    case "STT":
      temp[0] = 132;
      temp[1] = 3;
      break;
    case "SUBF":
      temp[0] = 92;
      temp[1] = 3;
      break;
    case "SUBR":
      temp[0] = 148;
      temp[1] = 2;
      break;
    case "SVC":
      temp[0] = 176;
      temp[1] = 2;
      break;
    case "TIXR":
      temp[0] = 184;
      temp[1] = 2;
      break;
    case "TIO":
      temp[0] = 248;
      temp[1] = 1;
      break;
    }
    return temp;
  }
}
