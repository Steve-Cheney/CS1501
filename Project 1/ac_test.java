//Stephen Cheney
//4275535
//TTH 9:30 Lecture || F 9:00 Recitation

import java.util.*;
import java.io.*;

//Extend DLB class
public class ac_test extends DLB{

  public static void main(String args[]) throws Exception, IOException{

    Scanner scanner = new Scanner(System.in);
    DLB dictionary = new DLB();
    DLB user_History = new DLB();

    //Add dictionary to DLB
    File dic_File = new File("./dictionary.txt");
    BufferedReader dic = new BufferedReader(new FileReader(dic_File));
    String string_D;
    while((string_D = dic.readLine()) != null){ //Read dictionary.txt and add line by line words into the DLB
      dictionary.add(string_D);
    }

    //Add user history if it exists
    File user_File = new File("./user_history.txt");
    if(!user_File.exists()){ //Create user_history.txt if it does not exist
      user_File.createNewFile();
    }
    BufferedReader user = new BufferedReader(new FileReader(user_File));
    String string_U;
    while((string_U = user.readLine()) != null){
      if(user_History.search(string_U)){ //Word has already been added, increase frequency
        user_History.search_End(string_U).add_Freq();
      }else{
        user_History.add(string_U);
      }
    }

    BufferedWriter bw = new BufferedWriter(new FileWriter("./user_history.txt", true));

    insert_Word(dictionary, user_History, scanner, bw);
  }

  //Method to loop adding characters and inserting words
  public static void insert_Word(DLB dict, DLB user, Scanner scanner, BufferedWriter bw) throws IOException{
    String str_Input = ""; //Combined chars for prefix
    String curr_Str = ""; //Last character input by user
    Boolean firstletter = true; //Flag for prints
    Boolean nextword = false; //Flag for prints
    Double avgtime = 0.0; //Avg time var
    int count = 0; //Count of times user enters a character
    LinkedList<String> user_List = new LinkedList<String>(); //List for printing suggestions

    while(!curr_Str.equals("!")){ //Continue to enter words
      while(!curr_Str.equals("$")){ //Continue to add characters to current word
        if(firstletter){
          System.out.print("\nEnter your first character: ");
        }
        else if(nextword){
          System.out.print("\nEnter the first character of the next word: ");
          nextword = false;
        }
        else{
          System.out.print("\nEnter the next character: ");
        }

        curr_Str = scanner.nextLine();

        //User wishes to end program
        if(curr_Str.equals("!")){
          break;
        }

        //Word is finished through '$', check if word is in User database
        if(curr_Str.equals("$")){
          System.out.println("  WORD COMPLETED: " + str_Input);

          if(!user.search(str_Input)){ //Word is not in user history
            user.add(str_Input);
            user.search_End(str_Input).add_Freq(); //Increase frequency for word
          }
          else{ //Word is in user history, increase frequency
            user.search_End(str_Input).add_Freq();
          }

          nextword = true;
          //Write to user_history.txt
          bw.write(str_Input);
          bw.newLine();

          str_Input = "";
          curr_Str = "";
          break;
        }

        str_Input += curr_Str; //Add current char to string being checked
        firstletter = false;

        //Check if user wants to autocomplete
        Boolean sug_Flag = false;
        if(curr_Str.equals("1")){
          sug_Flag = true;
        }
        if(curr_Str.equals("2")){
          sug_Flag = true;
        }
        if(curr_Str.equals("3")){
          sug_Flag = true;
        }
        if(curr_Str.equals("4")){
          sug_Flag = true;
        }
        if(curr_Str.equals("5")){
          sug_Flag = true;
        }

        Long time_before = System.nanoTime();

        //If user wants to autocomplete, then don't clear previous suggestions
        if(!sug_Flag){
          user_List = new LinkedList<String>();
        }
        else{
          if(curr_Str.equals("1")){
            str_Input =  user_List.get(0);
            System.out.println("  WORD COMPLETED: " + str_Input);
            if(!user.search(str_Input)){
              user.add(str_Input);
              user.search_End(str_Input).add_Freq(); //increase frequency for word
            }else{
              user.search_End(str_Input).add_Freq();
            }
            nextword = true;

            //Write to user_history.txt
            bw.write(str_Input);
            bw.newLine();

            str_Input = "";
            break;
          }
          else if(curr_Str.equals("2")){
            str_Input =  user_List.get(1);
            System.out.println("  WORD COMPLETED: " + str_Input);
            if(!user.search(str_Input)){
              user.add(str_Input);
              user.search_End(str_Input).add_Freq(); //increase frequency for word
            }
            else{
              user.search_End(str_Input).add_Freq();
            }
            nextword = true;
            //Write to user_history.txt
            bw.write(str_Input);
            bw.newLine();

            str_Input = "";
            break;
          }
          else if(curr_Str.equals("3")){
            str_Input =  user_List.get(2);
            System.out.println("  WORD COMPLETED: " + str_Input);
            if(!user.search(str_Input)){
              user.add(str_Input);
              user.search_End(str_Input).add_Freq(); //increase frequency for word
            }
            else{
              user.search_End(str_Input).add_Freq();
            }
            nextword = true;
            //Write to user_history.txt
            bw.write(str_Input);
            bw.newLine();

            str_Input = "";
            break;
          }
          else if(curr_Str.equals("4")){
            str_Input =  user_List.get(3);
            System.out.println("  WORD COMPLETED: " + str_Input);
            if(!user.search(str_Input)){
              user.add(str_Input);
              user.search_End(str_Input).add_Freq(); //increase frequency for word
            }
            else{
              user.search_End(str_Input).add_Freq();
            }
            nextword = true;
            //Write to user_history.txt
            bw.write(str_Input);
            bw.newLine();

            str_Input = "";
            break;
          }
          else if(curr_Str.equals("5")){
            str_Input =  user_List.get(4);
            System.out.println("  WORD COMPLETED: " + str_Input);
            if(!user.search(str_Input)){
              user.add(str_Input);
              user.search_End(str_Input).add_Freq(); //increase frequency for word
            }
            else{
              user.search_End(str_Input).add_Freq();
            }
            nextword = true;
            //Write to user_history.txt
            bw.write(str_Input);
            bw.newLine();

            str_Input = "";
            break;
          }
        } //End autocomplete if

        Stack<String> user_Suggest = sort_Stack(user.suggest(str_Input), user); //Stack of user_history based suggestions sorted by frequency
        Stack<String> dict_Suggest = dict.suggest(str_Input); //Stack of Dictionary based suggestions
        Boolean predic = true; //Flag if there are no predictions

        for(int i = 0; i < 5; i++){ //List suggestions 1 - 5
          //No suggestions at all
          if(user.suggest(str_Input).empty() && dict.suggest(str_Input).empty()){
            predic = false;
            break;
          }
          //Pop user values into user_List if a suggestion exists
          else if(!user_Suggest.empty()){
            user_List.add(user_Suggest.pop());
          }
          //No more user suggestions, pop dict suggestions and check if the dict suggestion doesn't already exist from user suggestion
          else if(!dict_Suggest.empty()){
            while(!dict_Suggest.empty() && (user_List.contains(dict_Suggest.peek()))){
              dict_Suggest.pop();
            }
            if(!dict_Suggest.empty()){
              user_List.add(dict_Suggest.pop());
            }
          }
        } //End Sugestion population

        //Timing calculations
        Long time_after = System.nanoTime();
        Double curtime = (time_after-time_before)/Math.pow(10,9);
        avgtime += curtime;
        count++;
        System.out.printf("\n(%f s)\n", curtime);

        if(predic){ //If there are valid predictions, display based on how many there are
          System.out.println("Predictions:");
          if(user_List.size() == 1){
            System.out.println("(1) " + user_List.get(0));
          }
          else if(user_List.size() == 2){
            System.out.println("(1) " + user_List.get(0) + "  (2) " + user_List.get(1));
          }
          else if(user_List.size() == 3){
            System.out.println("(1) " + user_List.get(0) + "  (2) " + user_List.get(1) +
                      "  (3) " + user_List.get(2));
          }
          else if(user_List.size() == 4){
            System.out.println("(1) " + user_List.get(0) + "  (2) " + user_List.get(1) +
                      "  (3) " + user_List.get(2) + "  (4) " + user_List.get(3));
          }
          else if(user_List.size() == 5){
            System.out.println("(1) " + user_List.get(0) + "  (2) " + user_List.get(1) +
                      "  (3) " + user_List.get(2) + "  (4) " + user_List.get(3)
                      + "  (5) " + user_List.get(4));
          }
        }
        else{
          System.out.println("No predictions found.");
        }
      } //End current word while loop
    } //End all words while loop

    bw.close(); //Finished writing to file

    //Program exiting, print average run time
    System.out.printf("\n(Average time : %f s)\n", (avgtime/count));
    System.out.println("Bye!");
  }

  //Sort stack method to sort based on frequency
  public static Stack<String> sort_Stack(Stack<String> toSort, DLB dlb){
    Stack<String> temp = new Stack<String>();

    while(!toSort.isEmpty()){
      String cur = toSort.pop();
      int temppeek = -1;
      if(!temp.isEmpty()){
        if(dlb.search_End(temp.peek()) == null){
          temppeek = 0;
        }else{
          temppeek = dlb.search_End(temp.peek()).get_Freq();
        }
      }
      while(!temp.isEmpty() && temppeek > dlb.search_End(cur).get_Freq()) {
          toSort.push(temp.pop());
      }
      temp.push(cur);
    }
    return temp;
  }
  
}
