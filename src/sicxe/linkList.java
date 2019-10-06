package sicxe;

public class linkList {
  private String member;
  private int value;
  private linkList next;
  private int type;
  
  public linkList(String name, int type) {
    this.member = name;
    this.type = type;
  }
  
  public linkList(String name, int value, int type) {
    this.member = name;
    this.value = value;
    this.type = type;
  }

  public linkList(int value, int type) {
    this.value = value;
    this.type = type;
  }
  public String getMember() {
    return member;
  }

  public void setType(int type) {
    this.type = type;
  }

  public void setValue(int value) {
    this.value = value;
  }

  public linkList getNext() {
    return next;
  }

  public int getType() {
    return type;
  }

  public int getValue() {
    return value;
  }

  public void setNext(linkList next) {
    this.next = next;
  }
  
  
}
