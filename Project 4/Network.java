/*
   Stephen Cheney || 4275535

   Network class is the overall graph object, holding both the individual
   edges (Net_Link), and the adjaceny list
 */

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.*;
import java.util.Queue;

class Network{

  private static int vertices = 1; // default network of 1 connection
  private static int count = 0; // number of distinct edges
  private static ArrayList<Net_Link> Network_Arr;
  private static LinkedList<Integer> adjListArray[];
  private static LinkedList<Integer> adjListCopper[];

  // mst items
  private static PriorityQueue<Net_Link> pq;
  private static Queue<Net_Link> mst;
  private static boolean[] marked;

  // dijkstra's items
  private static Queue<Net_Link> shortPath;
  private static int[] dist;
  private static int band;
  Network(){ // default constructor
      Network_Arr = new ArrayList<Net_Link>();
      Network_Arr.add(new Net_Link());
  }

  Network(int v){ // network constructor with a number v of vertices
    this.vertices = v;
    count = 0;
    System.out.println("Created Network. Expected Network nodes: " + v);

    Network_Arr = new ArrayList<Net_Link>(); // create a new ArrayList to hold the Net_Link objects
    adjListArray = new LinkedList[v]; // create a new adjaceny list
    adjListCopper = new LinkedList[v]; // create a new adjaceny list to check copper connection

    for(int i = 0; i < v; i++){
      adjListArray[i] = new LinkedList<>(); // for each vertex, insantiate a new linked list
      adjListCopper[i] = new LinkedList<>();
    }
  }

  public static int V(){ // return size of network
    return vertices;
  }

  public static void add(Net_Link link){ // add method taking in Net_Link objects
    if(Network_Arr.size() < (vertices*vertices-1)){ // if the count of max vertices hasn't been reached
      Network_Arr.add(link); // add the link to the network
      adjListArray[link.getV0()].add(link.getV1()); // add the edge from source to destination
      adjListArray[link.getV1()].add(link.getV0()); // add the edge from destination to source since it's undirected
      //System.out.println("Successfully added: " + link.toString());
      count++;
      if(link.getCableType().equals("copper")){
        adjListCopper[link.getV0()].add(link.getV1());
        adjListCopper[link.getV1()].add(link.getV0());
      }
    }
    else{ // all items have been added already, or Network is not insantiated
        System.out.println("Error: Unsuccessfully added link");
    }
  }

  public static Net_Link get_Link(int v0, int v1){ // get method to return the Net_Link object associated with the vertices
    if(v0 >= Network_Arr.size() || v1 >= Network_Arr.size()){
      System.out.println("Error: Unsuccessfully grabbed link");
      return null;
    }
    int i = 0;
    while(i < count){
      if((Network_Arr.get(i).getV0() == v0) && (Network_Arr.get(i).getV1() == v1)){
        return Network_Arr.get(i);
      }
      else if((Network_Arr.get(i).getV0() == v1) && (Network_Arr.get(i).getV1() == v0)){
        return Network_Arr.get(i);
      }
      i++;
    }
    return null;
  }


  // check if the graph is copper connected
  public void DFS(int v, boolean[] visited, LinkedList<Integer>[] list) { // depth first search
        // current node has now been visited
        visited[v] = true;
         //System.out.print(v+" ");
        // DFS to all adjacent vertices
        for (int x : list[v]) {
            if(!visited[x]) DFS(x, visited, list);
        }

  }
  public boolean checkCopperConnection() {
        // Mark all the vertices as not visited
        boolean[] visited = new boolean[vertices];
        int c = 0; // if c is greater than 1, then two seperate DFS had to be performed, so, not connected
        for(int i = 0; i < vertices; ++i) {
            if(!visited[i]) {
                // if vertex is not visited, DFS
                c++;
                if(c > 1) return false;
                DFS(i, visited, adjListCopper);
                //System.out.println();
            }
        }
        return true;
    }

  public boolean checkEditedConnection(int v0, int v1) { // v0 and v1 are the two vertices to remove
        // Mark all the vertices as not visited
        boolean[] visited = new boolean[vertices];
        visited[v0] = true;
        visited[v1] = true;
        int c = 0; // if c is greater than 1, then two seperate DFS had to be performed, so, not connected
        for(int i = 0; i < vertices; ++i) {
            if(!visited[i]) {
                // if vertex is not visited, DFS
                c++;
                if(c > 1) return false;
                DFS(i, visited, adjListArray);
                //System.out.println();
            }
        }
        return true;
    }
    public void connectionTest(){ // check what 2 vertices being removed will cause failed connectivity
      boolean test = false;
      for (int v0 = 0; v0 < vertices; v0++){
        for (int v1 = v0; v1 < vertices; v1++){
          if(checkEditedConnection(v0,v1) == false){
            System.out.println("Failed: (" + v0 + "," + v1 + ")");
            test = true;
          }
        }
      }
      if(!test)
        System.out.println("No failure points");
    }

  public static void MST(){ // comprise an MST starting at vertex 0
    pq = new PriorityQueue<Net_Link>();
    marked = new boolean[vertices];
    mst = new LinkedList<Net_Link>();
    visitMST(0);
    while(pq.size() > 0){
      Net_Link link = pq.poll(); // Get lowest latency
      int v = link.getV0(), w = link.other(v); // edge from pq
      if(marked[v] && marked[w]) continue; // if both vertices along the edge have been visited, add it to the MST, otherwise check next vertex
      mst.add(link);
      if(!marked[v]) visitMST(v);
      if(!marked[w]) visitMST(w);
    }
  }
  private static void visitMST(int v){ // mark the input vertex as visited and add all nonvisited adjacent edges to the PQ
    marked[v] = true;
    for(int i = 0; i < adjListArray[v].size(); i++){
      if(!marked[adjListArray[v].get(i)])
      //System.out.println(adjListArray[v].get(i));
      //System.out.println(get_Link(adjListArray[v].get(i),v).toString());
      pq.add(get_Link(v,adjListArray[v].get(i)));
    }
  }

  public static void shortestPath(int start, int end){
    dist = new int[vertices];
    marked = new boolean[vertices];
    pq = new PriorityQueue<Net_Link>();
    shortPath = new LinkedList<Net_Link>();
    band = Integer.MAX_VALUE;
    int cur = start; // current vertex
    for(int b = 0; b < vertices; b++){ // set all values to be max distance
      dist[b] = Integer.MAX_VALUE;
    }
    dist[start] = 0;
    visitShort(start);
    while(!marked[end]){
      Net_Link link = pq.poll(); // Get lowest latency
      //System.out.println(link.toString());
      int v = link.getV0(), w = link.other(v); // edge from pq
      if(marked[v] && marked[w]) continue; // if both vertices along the edge have been visited, add it to the queue, otherwise check next vertex
      shortPath.add(link);
      if(link.getBandwidth() < band) band = link.getBandwidth();
      if(!marked[v]) visitShort(v);
      if(!marked[w]) visitShort(w);
    }
  }
  private static void visitShort(int v){ // mark the input vertex as visited and add all nonvisited adjacent edges to the PQ
    marked[v] = true;

    for(int i = 0; i < adjListArray[v].size(); i++){
      if(!marked[adjListArray[v].get(i)]){
        if(get_Link(v,adjListArray[v].get(i)).getLatency() < dist[adjListArray[v].get(i)])
          pq.add(get_Link(v,adjListArray[v].get(i)));
          //System.out.println("Visited");
      }
    }
  }

  public static void printPQ(){
    System.out.println("In the PQ:");
    while(pq.size() > 0){
      System.out.println(pq.poll().toString());
    }
  }

  public static void printMST(){
    System.out.println("MST:");
    Object[] arr = mst.toArray();
    for(int i = 0; i < mst.size(); i++){
      System.out.println(arr[i].toString());
    }
  }

  public static void printShortest(){
    System.out.println("Shortest path:");
    Object[] arr = shortPath.toArray();
    for(int i = 0; i < shortPath.size(); i++){
      System.out.println(arr[i].toString());
    }
    System.out.println("\nBandwidth Available: " + band + "gbs");
  }

  public static void printGraph(){ // print the graph representation with the adjaceny list
    for(int v = 0; v < vertices; v++){
      System.out.print("\n V: " + v);
      for(Integer pCrawl: adjListArray[v]){
        System.out.print(" -> "+pCrawl);
      }
      System.out.println("\n");
    }
  }

    public void netToString(){ // print out each link in the network
      System.out.println("\nNetwork:\n");
      for(int i = 0; i < count; i++){
        System.out.println(Network_Arr.get(i).toString());
      }
    }

  public static void main(String[] args){
    Network net = new Network(5);
    net.add(new Net_Link(1, 4, "copper", 1000, 100));
    net.add(new Net_Link(1, 3, "copper", 2000, 150));
    net.add(new Net_Link(1, 2, "copper", 3000, 300));
    net.add(new Net_Link(3, 2, "copper", 4000, 400));
    net.add(new Net_Link(0, 3, "copper", 5000, 500));
    net.printGraph();

    //System.out.println(net.get_Link(3,2).toString());
    MST();
    //printPQ();
    printMST();
    //System.out.println(net.get(0).getV0());
    //System.out.println(net.get(0).getV1());
  }
}
