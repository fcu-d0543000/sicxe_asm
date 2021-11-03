## 一.	執行程式<br>
    SRCFILE 放 Assembly program, 產生 LISFILE, OBJFILE>
    在資料夾中 (如無法執行，請安裝JDK10版本且設定環境變數)>
	雙點d0543000 sicxeasm.jar
	在cmd 中輸入java -jar d0543000sicxeasm.jar

## 二.	開發語言<br>
	JAVA

## 三.	格式輸入
	0 ~ 7: Label 
	8: Blank
	9: + or blank (extend or relative)
	10-15: operation code
	16: Blank
	17: @ or # or = (addressing mode or literal)
	18-35: operand
	36..: comment

## 輸入限制:
    空格使用空白鍵，禁止 TAB。
    在各自區域內，左邊有空白也可以判讀。
    Operation code可大小寫。
    Label 大小寫不同視為不同。
    EQU 輸入 * 可計算為當前Loc。
    可使用Expressions，但中間不能使用 * (當前位置)。
    中文敘述有機率亂碼。

## 四.	Addressing modes
    Extended format
    Indirect addressing
    Immediate addressing
    Index addressing
    Relative addressing

## 五.	Assembler directives
    START
    END
    BYTE
    WORD
    RESB
    RESW
    EQU
    Literals
    Expressions
    Program blocks

## 六.	Data structures
	SymTab & MidFile & LitTab 使用 Linked list
	Literal pool 使用 Stack

## 七.	特別的Function
    public static linkList preFix(String expression, PassOne pass1) - 將中序計算式轉換為前序，如果算式中有undefined symbol 會return null
    public static int[] calculation(String expression, PassOne pass1) - 將前序式的結果計算出來，並回傳一個二維陣列，分別是 值、Type，如果前序式為 null 代表有undefined symbol，回傳 [-1, -1]； 如果Type 不為 1 (Relative) 或 0 (Absolute)，則回傳 [-1, -2]，得以分辨錯誤類別。

## 八.	計算Location錯誤處理
    Unrecognized operation code，loc + 3
    BYTE operand 錯誤，loc + 1
    WORD operand 錯誤，loc + 3
    RESB operand 錯誤，loc + 1
    RESB operand 錯誤，loc + 3
    Literal operand 錯誤不新增到 Literal table 中

## 九.	錯誤訊息列表
    " *** Operand should not follow RSUB statement.”
    " *** duplicate label definition.”
    " *** Illegal literal”
    " *** Missing right quote in a literal.”
    " *** Illegal hex string in a literal. “
    " *** Odd length hex string in a literal.”
    " *** Missing operand in EQU statement. “
    " *** Missing label in EQU statement.”
    " *** Symbol must be defined before used in EQU statement.”
    " *** Operand must be absolute or relative attribute in EQU statement.”
    " *** Missing operand in RESB statement.”
    " *** Operand must be absolute attribute in RESB statement.”
    " *** Symbol must be defined before used in RESB statement.”
    " *** Illegal USE operand”
    " *** Missing operand in RESW statement. “
    " *** Operand must be absolute attribute in RESW statement.”
    " *** Symbol must be defined before used in RESWB statement.”
    " *** Missing right quote in BYTE statement.”
    " *** Illegal operand in BYTE statement."
    " *** Illegal hex string in BYTE statement."
    " *** Odd length hex string in BYTE statement."
    " *** Missing operand in BYTE statement.”
    " *** Missing right quote in BYTE statement."
    " *** Illegal operand in BYTE statement."
    " *** Illegal hex string in BYTE statement"
    " *** Odd length hex string in BYTE statement."
    " *** Missing operand in BYTE statement."
    " *** Undefined symbol in WORD statement."
    " *** Missing operand in WORD statement."
    " *** Operand must be absolute or relative attribute in WORD statement."
    " *** operand should < 16777216."
    " *** Missing operand in BASE statement."
    " *** Undefined symbol in BASE statement."
    " *** Operand must be absolute or relative attribute in BASE statement."
    " *** Operand should not follow LTORG statement."
    " *** Undefined label after END statement."
    " *** Operand must be absolute or relative attribute in END statement."
    " *** Unrecognized operation code."
    " *** Illegal format in operation field.”
    " *** Missing label in START statement."
    " *** Missing operand in START statement."
    " *** Illegal hex string in START statement."
    " *** Duplicate START statement.";
    " *** Misplaced START statement."
    " *** Missing START in first statement."
    " *** Missing Operand in Format 2 instruction."
    " *** Missing Operand in Format 3 instruction."
    " *** Missing Operand in Format 4 instruction."
    " *** Illegal operand in Format 2 instruction."
    " *** Illegal operand in Format 3 instruction."
    " *** Illegal operand in Format 4 instruction."
    " *** Extra operand in Format 1 instruction."
    " *** Undefined symbol in operand field."
    " *** Instruction format error."

