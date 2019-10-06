package sicxe;

public class ErrorMsg {

  //RSUB
  public static String rsubOperand = " *** Operand should not follow RSUB statement.\n";
  //Label
  public static String labelDefinition = " *** duplicate label definition.\n";  
  //Literal
  public static String illegalLiteral = " *** Illegal literal\n";
  public static String literalFormat = " *** Missing right quote in a literal.\n";
  public static String literalHex = " *** Illegal hex string in a literal.\n";
  public static String literalOdd = " *** Odd length hex string in a literal.\n";
  //EQU
  public static String equMissOp = " *** Missing operand in EQU statement.\n";
  public static String equMissLa = " *** Missing label in EQU statement.\n";
  public static String equSymDef = " *** Symbol must be defined before used in EQU statement.\n";
  public static String equAorR = " *** Operand must be absolute or relative attribute in EQU statement.\n";
  //RESB
  public static String resbMissop = " *** Missing operand in RESB statement.\n";
  public static String resbAbsolut = " *** Operand must be absolute attribute in RESB statement.\n";
  public static String resbSymDef = " *** Symbol must be defined before used in RESB statement.\n";
  //USE
  public static String useIllegal = " *** Illegal USE operand\n";
  //RESW
  public static String reswMissOp = " *** Missing operand in RESW statement.\n";
  public static String reswAbsolut = " *** Operand must be absolute attribute in RESW statement.\n";
  public static String reswSymDef = " *** Symbol must be defined before used in RESWB statement.\n";
  //BYTE
  public static String byteFormat = " *** Missing right quote in BYTE statement.\n";
  public static String byteIllOp = " *** Illegal operand in BYTE statement.\n";
  public static String byteHex = " *** Illegal hex string in BYTE statement.\n";
  public static String byteOdd = " *** Odd length hex string in BYTE statement.\n";
  public static String byteMissOp = " *** Missing operand in BYTE statement.\n";
  //WORD
  public static String wordSymDef = " *** Undefined symbol in WORD statement.\n";
  public static String wordMissOp = " *** Missing operand in WORD statement.\n";
  public static String wordAorR = " *** Operand must be absolute or relative attribute in WORD statement.\n";
  public static String wordLarge = " *** operand should < 16777216.\n";
  //BASE
  public static String baseMissOp = " *** Missing operand in BASE statement.\n";
  public static String baseSymDef = " *** Undefined symbol in BASE statement.\n";
  public static String baseAorR = " *** Operand must be absolute or relative attribute in BASE statement.\n";
  //LTORG
  public static String ltorgOp = " *** Operand should not follow LTORG statement.\n";
  //END
  public static String endSymDef = " *** Undefined label after END statement.\n";
  public static String endAorR = " *** Operand must be absolute or relative attribute in END statement.\n";
  //OPCODE
  public static String opcodeUnDef = " *** Unrecognized operation code.\n";
  public static String opcodeMiss = " *** Illegal format in operation field.\n";
  //START
  public static String startMissLa = " *** Missing label in START statement.\n";
  public static String startMissOp = " *** Missing operand in START statement.\n";
  public static String startHex = " *** Illegal hex string in START statement.\n";
  public static String startDup = " *** Duplicate START statement.\n";
  public static String startWrongWay = " *** Misplaced START statement.\n";
  public static String startMiss1st = " *** Missing START in first statement.\n";
  //FORMAT
  public static String format2MissOp = " *** Missing Operand in Format 2 instruction.\n";
  public static String format3MissOp = " *** Missing Operand in Format 3 instruction.\n";
  public static String format4MissOp = " *** Missing Operand in Format 4 instruction.\n";
  public static String format2IllOp = " *** Illegal operand in Format 2 instruction.\n";
  public static String format3IllOp = " *** Illegal operand in Format 3 instruction.\n";
  public static String format4IllOp = " *** Illegal operand in Format 4 instruction.\n";
  public static String format1ExOp = " *** Extra operand in Format 1 instruction.\n";
  //symbol
  public static String symUnDef = " *** Undefined symbol in operand field.\n";
  //Instruction
  public static String insFormat = " *** Instruction format error.\n";
    
  
}
