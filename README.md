一.	執行程式<br>
    SRCFILE 放 Assembly program, 產生 LISFILE, OBJFILE<br>
    在資料夾中 (如無法執行，請安裝JDK10版本且設定環境變數)<br>
	雙點d0543000 sicxeasm.jar<br>
	在cmd 中輸入java -jar d0543000sicxeasm.jar<br>

二.	開發語言<br>
JAVA<br>

三.	格式輸入<br>
0 ~ 7: Label <br>
8: Blank<br>
9: + or blank (extend or relative)<br>
10-15: operation code<br>
16: Blank<br>
17: @ or # or = (addressing mode or literal)<br>
18-35: operand<br>
36..: comment<br>

輸入限制:<br>
    空格使用空白鍵，禁止 TAB。<br>
    在各自區域內，左邊有空白也可以判讀。<br>
    Operation code可大小寫。<br>
    Label 大小寫不同視為不同。<br>
    EQU 輸入 * 可計算為當前Loc。<br>
    可使用Expressions，但中間不能使用 * (當前位置)。<br>
    中文敘述有機率亂碼。<br>

四.	Addressing modes<br>
    Extended format<br>
    Indirect addressing<br>
    Immediate addressing<br>
    Index addressing<br>
    Relative addressing<br>

五.	Assembler directives<br>
    START<br>
    END<br>
    BYTE<br>
    WORD<br>
    RESB<br>
    RESW<br>
    EQU<br>
    Literals<br>
    Expressions<br>
    Program blocks<br>

六.	Data structures<br>
SymTab & MidFile & LitTab 使用 Linked list<br>
Literal pool 使用 Stack<br>

七.	特別的Function
    public static linkList preFix(String expression, PassOne pass1)<br>
	將中序計算式轉換為前序，如果算式中有undefined symbol 會return null<br>
    public static int[] calculation(String expression, PassOne pass1)<br>
	將前序式的結果計算出來，並回傳一個二維陣列，分別是 值、Type，如果前序式為 null 代表有undefined symbol，回傳 [-1, -1]； 如果Type 不為 1 (Relative) 或 0 (Absolute)，則回傳 [-1, -2]，得以分辨錯誤類別。<br>

八.	計算Location錯誤處理<br>
    Unrecognized operation code，loc + 3<br>
    BYTE operand 錯誤，loc + 1<br>
    WORD operand 錯誤，loc + 3<br>
    RESB operand 錯誤，loc + 1<br>
    RESB operand 錯誤，loc + 3<br>
    Literal operand 錯誤不新增到 Literal table 中<br>

九.	錯誤訊息列表<br>
    " *** Operand should not follow RSUB statement.”<br>
    " *** duplicate label definition.”<br>
    " *** Illegal literal”<br>
    " *** Missing right quote in a literal.”<br>
    " *** Illegal hex string in a literal. “<br>
    " *** Odd length hex string in a literal.”<br>
    " *** Missing operand in EQU statement. “<br>
    " *** Missing label in EQU statement.”<br>
    " *** Symbol must be defined before used in EQU statement.”<br>
    " *** Operand must be absolute or relative attribute in EQU statement.”<br>
    " *** Missing operand in RESB statement.”<br>
    " *** Operand must be absolute attribute in RESB statement.”<br>
    " *** Symbol must be defined before used in RESB statement.”<br>
    " *** Illegal USE operand”<br>
    " *** Missing operand in RESW statement. “<br>
    " *** Operand must be absolute attribute in RESW statement.”<br>
    " *** Symbol must be defined before used in RESWB statement.”<br>
    " *** Missing right quote in BYTE statement.”<br>
    " *** Illegal operand in BYTE statement."<br>
    " *** Illegal hex string in BYTE statement."<br>
    " *** Odd length hex string in BYTE statement."<br>
    " *** Missing operand in BYTE statement.”<br>
    " *** Missing right quote in BYTE statement."<br>
    " *** Illegal operand in BYTE statement."<br>
    " *** Illegal hex string in BYTE statement"<br>
    " *** Odd length hex string in BYTE statement."<br>
    " *** Missing operand in BYTE statement."<br>
    " *** Undefined symbol in WORD statement."<br>
    " *** Missing operand in WORD statement."<br>
    " *** Operand must be absolute or relative attribute in WORD statement."<br>
    " *** operand should < 16777216."<br>
    " *** Missing operand in BASE statement."<br>
    " *** Undefined symbol in BASE statement."<br>

九.	錯誤訊息列表(續)<br>
    " *** Operand must be absolute or relative attribute in BASE statement."<br>
    " *** Operand should not follow LTORG statement."<br>
    " *** Undefined label after END statement."<br>
    " *** Operand must be absolute or relative attribute in END statement."<br>
    " *** Unrecognized operation code."<br>
    " *** Illegal format in operation field.”<br>
    " *** Missing label in START statement."<br>
    " *** Missing operand in START statement."<br>
    " *** Illegal hex string in START statement."<br>
    " *** Duplicate START statement.";<br>
    " *** Misplaced START statement."<br>
    " *** Missing START in first statement."
    " *** Missing Operand in Format 2 instruction."<br>
    " *** Missing Operand in Format 3 instruction."<br>
    " *** Missing Operand in Format 4 instruction."<br>
    " *** Illegal operand in Format 2 instruction."<br>
    " *** Illegal operand in Format 3 instruction."<br>
    " *** Illegal operand in Format 4 instruction."<br>
    " *** Extra operand in Format 1 instruction."<br>
    " *** Undefined symbol in operand field."<br>
    " *** Instruction format error."<br>

