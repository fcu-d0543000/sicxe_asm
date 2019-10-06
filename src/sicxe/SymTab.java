package sicxe;

public class SymTab {

  private String name;
  private int value;
  private int type;
  private int block;
  private boolean flag;
  private SymTab next;
  private SymTab tail;
  
  public static final int ABSOLUTE = 0;
  public static final int RELATIVE = 1;
  
  public SymTab getNext() {
    return next;
  }

  public String getName() {
    return name;
  }

  public int getBlock() {
    return block;
  }

  public SymTab(String name, int value, int type, int block) {
    this.name = name;
    this.value = value;
    this.type = type;
    this.block = block;
    flag = true;
    next = null;
    tail = this;
  }
  
  public int getValue() {
    return value;
  }

  public void insertNew(SymTab sym) {
    while(tail.next != null) 
      tail = tail.next;
    
    tail.next = sym;
    tail = tail.next;
  }
  
  public int getType() {
    return type;
  }

  public SymTab searchSymTab (String label) {
    SymTab temp = this;
    label = label.trim();
    while (temp != null) {
      if (temp.name.equals(label))
        return temp;
      temp = temp.next;
    }
    return null;
  }
}
