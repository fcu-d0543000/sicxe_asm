package sicxe;

public class MidFile {
  private int value;
  private String obCode;
  private String line;
  private String errorMessage;
  private int blk;
  private MidFile next;
  private MidFile tail;

  public MidFile(int value, int blk, String line) {
    this.value = value;
    this.line = line;
    this.obCode = "        ";
    this.next = null;
    this.tail = this;
    this.errorMessage = "";
    this.blk = blk;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public MidFile getNext() {
    return next;
  }

  public void setValue(int value) {
    tail.value = value;
  }

  public int getValue() {
    if (this != null)
      return value;
    return -1;
  }

  public String getObCode() {
    return obCode;
  }

  public void setObCode(String obCode) {
    this.obCode = obCode;
  }

  public String getLine() {
    return line;
  }

  public void setErrorMessage(String msg) {
    tail.errorMessage += msg;
  }

  public void insertNew(MidFile midFile) {
    while (tail.next != null)
      tail = tail.next;

    tail.next = midFile;
    tail = tail.next;
  }

  public int getBlk() {
    return blk;
  }
  
  public void addErrorMessage(String msg) {
    this.errorMessage += msg;
  }
}
