public class Node{
  private Node sibling_Node;
  private Node child_Node;
  private char value;
  private int freq;

  public Node(){

  }

  public Node(char value){
    this(value, null, null);
  }

  public Node(char v, Node sibling, Node child){
    this.value = v;
    this.sibling_Node = sibling;
    this.child_Node = child;
    this.freq = 1;
  }
  public void set_Value(char v){
    this.value = v;
  }

  public void set_Child(Node child){
    this.child_Node = child;
  }

  public void set_Sibling(Node sibling){
    this.sibling_Node = sibling;
  }

  public Node get_Child(){
    return child_Node;
  }
  public Node get_Sibling(){
    return sibling_Node;
  }

  public char get_Value(){
    return value;
  }

  public int get_Freq(){
    return freq;
  }

  public int add_Freq(){
    return freq++;
  }

}
