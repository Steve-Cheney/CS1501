import java.util.*;

public class DLB extends Node{
  private Node root = new Node();
  private Stack<String> autocomplete = new Stack<String>();
  public DLB(){
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
		Node cur = root;
		for (int i = 0; i < input.length(); i++) {
			cur = search_Child(cur, input.charAt(i));
      //System.out.println("cur node has value of " + cur.get_Value());
      if (cur == null) {
        //If the node is null, return false
        System.out.println("cur is null");
 				return false;
			}
		}
		Node end = search_Child(cur, '^');
    //System.out.println(end == null);
		if (end == null) {
      // If the the end character is null, there's a prefix
      System.out.println("prefix");
			return false;
		}

      // If sibling is null, word found
		else if (end.get_Sibling() == null) {
      System.out.println("sibling null");
      return true;
		}

      // If there is a sibling to the terminator node then there is both a word and a prefix found...
		else {
      System.out.println("Terminator has sibling");
			return true;
		}
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
      //System.out.println("Adding '" + input.charAt(i) + "'");
			Node set = add_Child(cur, input.charAt(i));

			cur = set;
    }
  }
  public Stack<String> suggest(String prefix /*can pass in the number of suggestions from other tree*/){
    Node cur = root;
		for (int i = 0; i < prefix.length(); i++) {
			cur = search_Child(cur, prefix.charAt(i));
      //System.out.println("cur node has value of " + cur.get_Value());
      if (cur == null) {
        //If the node is null, return false
        System.out.println("cur is null");
 				return null;
			}
		}

    cur = cur.get_Child();
    find_suggestions(cur, prefix, 0, false);
    return autocomplete;
  }
  private void find_suggestions(Node start, String prefix, int count, boolean flag){
    if (!flag){
      if(start.get_Sibling() != null){
        find_suggestions(start.get_Sibling(), prefix, count, flag);
      }
      if(start.get_Child() != null){

        prefix += start.get_Value();
        //System.out.println(prefix);

        if(start.get_Child().get_Value() == '^'){
          //autocomplete.add(prefix);
          count++;
          if(count==4){
            flag = true;
          }
        }
        find_suggestions(start.get_Child(), prefix, count, flag);
      }

      else{
        //System.out.println("End of Recursion: " + prefix);
        autocomplete.push(prefix);
        count++;
        if(count==4){
          flag = true;
        }
      }
    }
  }
}
