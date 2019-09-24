import java.util.*;

public class DLB extends Node{
  private Node root = new Node();
  private Stack<String> autocomplete = new Stack<String>();
  public DLB(){
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
    Node curr = root;
    for (int i = 0; i < input.length(); i++){
      //System.out.println("Adding '" + input.charAt(i) + "'");
			Node set = add_Child(curr, input.charAt(i));

			curr = set;
    }
  }

  public Node search_Child(Node parent, char v){
    return search_Sibling(parent.get_Child(), v);
  }

  public Node search_Sibling(Node sibling, char v){
    Node next = sibling;

    while(next != null){
      if(next.get_Value() == v){
        break;
      }

      next = next.get_Sibling();
    }
    //System.out.println("Found " + next.get_Value());
    return next;
  }

  public boolean search(String input){
		Node curr = root;
		for (int i = 0; i < input.length(); i++) {
			curr = search_Child(curr, input.charAt(i));
      //System.out.println("curr node has value of " + curr.get_Value());
      if (curr == null) {
        //If the node is null, return false
        //System.out.println("curr is null");
 				return false;
			}
		}
		Node end = search_Child(curr, '^');
    //System.out.println(end == null);
		if (end == null) {
      // If the the end character is null, there's a prefix
      //System.out.println("prefix");
			return false;
		}

      // If sibling is null, word found
		else if (end.get_Sibling() == null) {
      //System.out.println("sibling null");
      return true;
		}

      // If there is a sibling to the terminator node then there is both a word and a prefix found...
		else {
      //System.out.println("Terminator has sibling");
			return true;
		}
	}

  public Node search_End(String input){
    //Returns the end node '^' which houses the frequency of the word
		Node curr = root;
		for (int i = 0; i < input.length(); i++) {
			curr = search_Child(curr, input.charAt(i));
      //System.out.println("curr node has value of " + curr.get_Value());
      if (curr == null) {
        //If the node is null, return false
        //System.out.println("curr is null");
 				return null;
			}
		}
		Node end = search_Child(curr, '^');
    //System.out.println(end == null);
		if (end == null) {
      // If the the end character is null, there's a prefix
      //System.out.println("prefix");
			return null;
		}

      // If sibling is null, word found
		else if (end.get_Sibling() == null) {
      //System.out.println("sibling null");
      return end;
		}

      // If there is a sibling to the terminator node then there is both a word and a prefix found...
		else {
      //System.out.println("Terminator has sibling");
			return end;
		}
	}

  private void sugg_Recursive(Node start, String input){
    if(start == null){
      return;
    }
      if(start.get_Sibling() != null){
        sugg_Recursive(start.get_Sibling(), input);
      }
      if(start.get_Child() != null){

        input += start.get_Value();
        //System.out.println(input);

        if(start.get_Child().get_Value() == '^'){
          autocomplete.push(input);

        }
        sugg_Recursive(start.get_Child(), input);
      }
      else{
        //End of Recursion
        return;
      }
  }

  public Stack<String> suggest(String input){
    autocomplete = new Stack<String>();
    Node curr = root;
    if(curr == null)
      return autocomplete;
    if(curr.get_Child() == null)
      return autocomplete;

		for (int i = 0; i < input.length(); i++) {
			curr = search_Child(curr, input.charAt(i));
      //System.out.println("curr node has value of " + curr.get_Value());
      if (curr == null) {
        //If the node is null, return false
        //System.out.println("curr is null");
 				return autocomplete;
			}
		}

    curr = curr.get_Child();
    sugg_Recursive(curr, input);
    return autocomplete;
  }
}
