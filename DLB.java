public class DLB extends Node{
  private Node root = new Node();

  public DLB(){
  }

  public Node search_Child(Node parent, char v){
    return add_Sibling(parent.get_Child(), v);
  }

  public Node search_Sibling(Node sibling, char v){
    Node next = sibling.get_Sibling();

    while(next != null){
      if(next.get_Value() == v){
        break;
      }
      next = next.get_Sibling();
    }
    return next;
  }

  public Node add_Child(Node parent, char v){
    if(parent.get_Child() == null){
      Node temp = new Node(v);
      parent.set_Child(temp);
      //System.out.println("Setting parent's child to " + parent.get_Child().get_Value());
      return parent.get_Child();
    }
    else{
      //System.out.println("Parent has a child node, check siblings.");
      return add_Sibling(parent.get_Child(), v);
    }
  }

  private Node add_Sibling(Node sibling, char v){

    Node next = sibling;
    while(next.get_Sibling() != null){
      //System.out.println("Has a sibling");
      if(next.get_Value() == v){
        //System.out.println("Value found, break to return");
        break;
      }
      next = next.get_Sibling();
    }
    if(next.get_Value() == v){
      //System.out.println("Node already exists, is a sibling");
      return next;
    }
    else{
      Node temp = new Node(v);
      next.set_Sibling(temp);
      //System.out.println("Setting sibling to '" + next.get_Sibling().get_Value() + "'");
      return next.get_Sibling();
    }
  }

  public void add(String input){

    input += "^";
    Node cur = root;
    for (int i = 0; i < input.length(); i++){
      System.out.println("Adding '" + input.charAt(i) + "'");
			Node set = add_Child(cur, input.charAt(i));
      System.out.println("Node's value is: '" + set.get_Value() + "'");

			cur = set;
    }
  }

}
