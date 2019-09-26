//Stephen Cheney
//4275535
//TTH 9:30 Lecture || F 9:00 Recitation

public class Node{

  //Global Node vars
  private Node sibling_Node;
  private Node child_Node;
  private char value;
  private int freq;

  //Emtpy instantiator
  public Node(){

  }

  //Create an empty Node that contains a value
  public Node(char value){
    this(value, null, null);
  }

  //Instantiator
  public Node(char v, Node sibling, Node child){
    this.value = v;
    this.sibling_Node = sibling;
    this.child_Node = child;
    this.freq = 0;
  }

  //Set the value of the Node to char v
  public void set_Value(char v){
    this.value = v;
  }

  //Set the child reference of the Node to Node child
  public void set_Child(Node child){
    this.child_Node = child;
  }

  //Set the sibling reference of the Node to Node sibling
  public void set_Sibling(Node sibling){
    this.sibling_Node = sibling;
  }

  //Return the child reference of the Node
  public Node get_Child(){
    return child_Node;
  }

  //Return the sibling reference of the Node
  public Node get_Sibling(){
    return sibling_Node;
  }

  //Return the char value of the Node
  public char get_Value(){
    return value;
  }

  //Return the frequency of the word (freq attached to terminator char)
  public int get_Freq(){
    return freq;
  }

  //Incrememnt frequency of a word by 1
  public int add_Freq(){
    return freq++;
  }

}
