package sicxe;

public class LitTab {
  String name;
  int value;
  int blk;
  int length;
  LitTab next;
  LitTab tail;
  LitTab litPool;
  
  public LitTab(String name, int length) {
    this.name = name;
    this.length = length;
    next = null;
    tail = this;
    litPool = null;
  }
  
  public void insertNew(LitTab lit) {
    while(tail.next != null) 
      tail = tail.next;
    
    tail.next = lit;
    tail = tail.next;
  }
  
  public LitTab searchLitTab (String name) {
    LitTab temp = this;
    name = name.trim();
    while (temp != null) {
      if (temp.name.equals(name))
        return temp;
      temp = temp.next;
    }
    return null;
  }
  
  public void push(LitTab lit) {
    LitTab temp2 = new LitTab(lit.getName(), lit.getLength());
    if (this.litPool == null)
      this.litPool = temp2;
    else {
      LitTab temp = this.litPool;
      while (temp.next != null)
        temp = temp.next;
      temp.next = temp2;
    }
  }
  
  public LitTab pop() {
    LitTab temp = litPool;
    if (temp != null) {
      litPool = litPool.next;
      return temp;
    }
    return null;
  }

  public String getName() {
    return name;
  }

  public int getValue() {
    return value;
  }

  public int getBlk() {
    return blk;
  }

  public int getLength() {
    return length;
  }

  public LitTab getNext() {
    return next;
  }

  public LitTab getTail() {
    return tail;
  }

  public LitTab getLitPool() {
    return litPool;
  }

  public void setValue(int value) {
    this.value = value;
  }

  public void setBlk(int blk) {
    this.blk = blk;
  }
  
  
}
