/*
   Stephen Cheney || 4275535

   NetworkAnalysis class is the main program driver
 */
import java.util.*;
import java.io.*;

class NetworkAnalysis{

  static Scanner scanner = new Scanner(System.in);
  static int opticalCount = 0;
  static int copperCount = 0;
  public static Network network;

  public static void main(String[] args) throws Exception, IOException{

    File net_File = new File(args[0]);
    BufferedReader net_buf = new BufferedReader(new FileReader(net_File));
    String string_A;

    network = new Network();
    // read in the file, add the Net_link objects
    while((string_A = net_buf.readLine()) != null) {
      if(string_A.charAt(0) == '#' || string_A.charAt(0) == '/') continue;
      String[] data = string_A.split(" ");
      if(data.length == 1){
        network = new Network(Integer.parseInt(data[0]));
      }
      else{
        Net_Link link = new Net_Link(Integer.parseInt(data[0]),
              Integer.parseInt(data[1]), data[2], Integer.parseInt(data[3]),
              Integer.parseInt(data[4]));
        network.add(link);
      }
    }

    boolean run = true;
    while(run) {
      int option = 0;
      options();
      option = getOption();
      if(option > 4 || option < 1) {
        System.out.println("Exiting...");
        System.exit(0);
      }
      selectOption(option);
    }
  }

  public static void options() {
    System.out.println("\nEnter the number for the following options, any other number quits");
    System.out.println("\t1. Find the Lowest Latency path");
    System.out.println("\t2. Determine if network is Copper Connected");
    System.out.println("\t3. Find lowest average latency tree");
    System.out.println("\t4. Determine failure points");
  }
  public static int getOption(){
    System.out.print("Enter option:  ");
    return scanner.nextInt();
  }

  public static void selectOption(int op) {
    switch(op) {
    case 1:
      System.out.print("\nEnter starting vertex:  ");
      int start = scanner.nextInt();
      System.out.print("Enter ending vertex:  ");
      int end = scanner.nextInt();
      network.shortestPath(start, end);
      System.out.println();
      network.printShortest();
      break;
    case 2:
      System.out.println();
      if(network.checkCopperConnection())
        System.out.println("Network is copper connected");
      else
        System.out.println("Network is NOT copper connected");
      break;
    case 3:
      System.out.println();
      network.MST();
      network.printMST();
      break;
    case 4:
      System.out.println();
      network.connectionTest();
      break;
    default:
    }
  }

}
