//Stephen Cheney
//4275535
//TTH 9:30 Lecture || F 9:00 Recitation

import java.util.*;

//Extend Node class
public class DLB extends Node{

  private Node root = new Node(); //Set the root of the DLB that will point to the rest of the DLB
  private Stack<String> autocomplete = new Stack<String>(); //Stack that will hold all values of the suggest method for the DLB

  //Empty instantiator
  public DLB(){

  }

  //Used in add() || Add value's node to the child of the passed parent node
  public Node add_Child(Node parent, char v){
    if(parent.get_Child() == null){ //If parent has no child, add new Node(v)
      Node temp = new Node(v);
      parent.set_Child(temp);
      return parent.get_Child();
    }
    else{ //Parent has a child, check siblings
      return add_Sibling(parent.get_Child(), v);
    }
  }

  //Used in add() || Add value's node to the sibling
  private Node add_Sibling(Node sibling, char v){
    Node next = sibling;

    while(next.get_Sibling() != null){ //Node has a sibling
      if(next.get_Value() == v){ //Value was found, break to return
        break;
      }
      next = next.get_Sibling(); //Set node to it's sibling
    }

    if(next.get_Value() == v){ //Node already exists, is a sibling
      return next;
    }
    else{ //Node doesn't exits, add a new node of value v as the sibling
      Node temp = new Node(v);
      next.set_Sibling(temp);
      return next.get_Sibling();
    }
  }

  //Add method to add a full word
  public void add(String input){
    input += "^"; //Add terminator character to end of input
    Node curr = root; //Start at the root

    for(int i = 0; i < input.length(); i++){ //Add Nodes character by character
			Node set = add_Child(curr, input.charAt(i));
    	curr = set; //Sets curr to where last char was added
    }
  }

  //Used in search() || Used to traverse the children
  public Node search_Child(Node parent, char v){
    return search_Sibling(parent.get_Child(), v);
  }

  //Used in search() || Used to find the terminator
  public Node search_Sibling(Node sibling, char v){
    Node next = sibling;

    while(next != null){
      if(next.get_Value() == v){ //Word found, return the terminator Node
        break;
      }
    next = next.get_Sibling();
    }
    return next;
  }

  //Given a word, return true if it is contained in the DLB, fase otherwise
  public boolean search(String input){
		Node curr = root; //Start at the root

		for(int i = 0; i < input.length(); i++){
			curr = search_Child(curr, input.charAt(i));
      if(curr == null){ //If the node is null, return false
        return false;
			}
		}

		Node end = search_Child(curr, '^');

		if(end == null){ //If the the end character is null, there's a prefix
    	return false;
		}
		else if(end.get_Sibling() == null){ //If sibling is null, word found
      return true;
		}
		else { //If terminator node has a sibling and a child then there a word and a prefix is found
			return true;
		}
	}

  //Same as search() but returns the node where terminator is, or null if it doesn't exist
  //This is where frequency of a word is stored
  public Node search_End(String input){
		Node curr = root;

		for(int i = 0; i < input.length(); i++){
			curr = search_Child(curr, input.charAt(i));
      if(curr == null){
 				return null;
			}
		}

		Node end = search_Child(curr, '^');

		if(end == null){
			return null;
		}
		else if(end.get_Sibling() == null){
      return end;
		}
		else{
      return end;
    }
	}

  //Used in suggest() || Recursive call of suggest method to return all possibilities
  private void sugg_Recursive(Node start, String input){
    if(start == null){
      return;
    }
    if(start.get_Sibling() != null){
      sugg_Recursive(start.get_Sibling(), input);
    }
    if(start.get_Child() != null){
      input += start.get_Value();
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

  //Takes in a prefix, returns a stack of all possibilities
  public Stack<String> suggest(String input){
    autocomplete = new Stack<String>(); //Flush autocomplete Stack
    Node curr = root; //Start at root

    if(curr == null)
      return autocomplete;

		for(int i = 0; i < input.length(); i++){
      if(search(input)) //If input exists, add as a suggestion
        autocomplete.push(input);

			curr = search_Child(curr, input.charAt(i));
      if(curr == null){ //If the node is null, return false
 				return autocomplete;
			}
		}

    curr = curr.get_Child();
    sugg_Recursive(curr, input);
    return autocomplete;
  }

}
