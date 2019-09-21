import java.util.*;
import java.io.*;
public class ac_test extends DLB{

    public static void main(String args[]) throws Exception{

        Scanner scanner = new Scanner(System.in);
        DLB dictionary = new DLB();
        File dic_File = new File("./dictionary.txt");

        BufferedReader br = new BufferedReader(new FileReader(dic_File));

        String st;
        while((st = br.readLine()) != null){
          //System.out.println("Adding " + st);
          dictionary.add(st);
        }
        //System.out.println("Search for: ");
        //String str = scanner.nextLine();
        //System.out.println("Found word: " + dictionary.search(str));
        Long time_before = System.nanoTime();
        Stack<String> result = dictionary.suggest("t");
        for(int i = 0; i < 5; i++){
          System.out.println(result.pop());
        }
        Long time_after = System.nanoTime();
        System.out.println("(" + (time_after-time_before)/Math.pow(10,9) + " s)");
    }
}
