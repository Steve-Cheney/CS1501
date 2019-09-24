import java.util.*;
import java.io.*;
import java.util.Arrays;
import java.util.List;

public class ac_test extends DLB{

    public static void main(String args[]) throws Exception{

        Scanner scanner = new Scanner(System.in);
        DLB dictionary = new DLB();
        DLB user_History = new DLB();
        File dic_File = new File("./dictionary.txt");

        BufferedReader br = new BufferedReader(new FileReader(dic_File));

        //Add dictionary to DLB
        String st;
        while((st = br.readLine()) != null){
          //System.out.println("Adding " + st);
          dictionary.add(st);
        }

        insert_Word(dictionary, user_History, scanner);

    }

    public static void insert_Word(DLB dict, DLB user, Scanner scanner){
      //individual words
      String str_Input = "";
      String curr_Str = "";
      Boolean firstletter = true;
      Boolean nextword = false;
      Double avgtime = 0.0;
      int count = 0;
      LinkedList<String> user_List = new LinkedList<String>();
      //System.out.println("Found word: " + dictionary.search(str));
      while(!curr_Str.equals("!")){
        while(!curr_Str.equals("$")){
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

          //Word is finished through '$', check if word is in User database
          if(curr_Str.equals("$")){
            System.out.println("  WORD COMPLETED: " + str_Input);
            if(!user.search(str_Input)){
              //System.out.println("Adding " + str_Input + " to DLB");
              user.add(str_Input);
              user.search_End(str_Input).add_Freq(); //increase frequency for word
            }
            nextword = true;
            str_Input = "";
            break;
          }
          if(curr_Str.equals("!")){
            break;
          }
          str_Input += curr_Str;
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

          }else{
            if(curr_Str.equals("1")){
              str_Input =  user_List.get(0);
              System.out.println("  WORD COMPLETED: " + str_Input);
              if(!user.search(str_Input)){
                //System.out.println("Adding " + str_Input + " to DLB");
                user.add(str_Input);
                user.search_End(str_Input).add_Freq(); //increase frequency for word
              }
              nextword = true;
              str_Input = "";
              break;
            }
            if(curr_Str.equals("2")){
              str_Input =  user_List.get(1);
              System.out.println("  WORD COMPLETED: " + str_Input);
              if(!user.search(str_Input)){
                //System.out.println("Adding " + str_Input + " to DLB");
                user.add(str_Input);
                user.search_End(str_Input).add_Freq(); //increase frequency for word
              }
              nextword = true;
              str_Input = "";
              break;
            }
            if(curr_Str.equals("3")){
              str_Input =  user_List.get(2);
              System.out.println("  WORD COMPLETED: " + str_Input);
              if(!user.search(str_Input)){
                //System.out.println("Adding " + str_Input + " to DLB");
                user.add(str_Input);
                user.search_End(str_Input).add_Freq(); //increase frequency for word
              }
              nextword = true;
              str_Input = "";
              break;
            }
            if(curr_Str.equals("4")){
              str_Input =  user_List.get(3);
              System.out.println("  WORD COMPLETED: " + str_Input);
              if(!user.search(str_Input)){
                //System.out.println("Adding " + str_Input + " to DLB");
                user.add(str_Input);
                user.search_End(str_Input).add_Freq(); //increase frequency for word
              }
              nextword = true;
              str_Input = "";
              break;
            }
            if(curr_Str.equals("5")){
              str_Input =  user_List.get(4);
              System.out.println("  WORD COMPLETED: " + str_Input);
              if(!user.search(str_Input)){
                //System.out.println("Adding " + str_Input + " to DLB");
                user.add(str_Input);
                user.search_End(str_Input).add_Freq(); //increase frequency for word
              }
              nextword = true;
              str_Input = "";
              break;
            }
          }
          System.out.println(str_Input);
          Stack<String> user_Suggest = user.suggest(str_Input);
            //System.out.println("User stack size: " + user_Suggest.size());
          Stack<String> dict_Suggest = dict.suggest(str_Input);
          Boolean predic = true;

          for(int i = 0; i < 5; i++){
            //No suggestions at all
            if(user.suggest(str_Input).empty() && dict.suggest(str_Input).empty()){
              //System.out.println("User empty: " + user_Suggest.empty());
              //System.out.println("Dict empty: " + dict_Suggest.empty());
              //System.out.println(dict_Suggest.toString());
              predic = false;
              break;
            }
            //Pop user values into user_Arr to sort by freq
            else if(!user_Suggest.empty()){
              //System.out.println("\n Added from user_Suggest: " + user_Suggest.peek() + " at index " + i);
              user_List.add(user_Suggest.pop());
              //System.out.println(user_Suggest.peek());
              //System.out.println(user_Suggest.empty());

            }
            //No more user suggestions, pop dict suggestions
            else if(!dict_Suggest.empty()){
              while(!dict_Suggest.empty() && (user_List.contains(dict_Suggest.peek()))){
              //System.out.println("List contains " + dict_Suggest.peek());
              dict_Suggest.pop();
              }
              if(!dict_Suggest.empty()){
                user_List.add(dict_Suggest.pop());
              }
            }
          }
          Long time_after = System.nanoTime();
          Double curtime = (time_after-time_before)/Math.pow(10,9);
          avgtime += curtime;
          count++;
          System.out.printf("\n(%f s)\n", curtime);
          if(predic){
            //System.out.println("user_list print: " + user_List.toString());
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
          }else{
            System.out.println("No predictions found.");
          }
        }
      }
      System.out.println("\nAverage time: " + (avgtime/count) + " s");
      System.out.println("Bye!");
    }
}
