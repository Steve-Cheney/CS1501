/*
   Stephen Cheney || 4275535

   Contains a min and max PQ for the apartments
 */

import java.util.HashMap;
import java.util.Map;

public class MinMaxPQ {

      public IndexMinPQ<Integer> rent; // minPQ for the rents
      public IndexMinPQ<Integer> sqft; // maxPQ for the square footage
      public HashMap<String, Integer> map; // hashmap for the values. Each rent and sqft apartment object will be indexed as the same thing
      public HashMap<Integer, String> reversemap; // map the index to the keys so we can, given an index, return the key
      private int count = 0;

      // instantiator for MinMaxPQ
      // capacity is the max number of items that can be in the queue
      public MinMaxPQ(int capacity) {
              rent = new IndexMinPQ<>(capacity, true); // set to true for minPQ
              sqft = new IndexMinPQ<>(capacity, false); // set to false for maxPQ
              map = new HashMap<>(); // make the hashmap
              reversemap = new HashMap<>(); // make the reversed hashmap
      }

      // add method. Takes in an apartment object and will insert it into the PQs and hashmaps
      public void add(Apt apartment){
              rent.insert(count, apartment.getRent());
              sqft.insert(count, apartment.getSqFeet());
              map.put(apartment.getKey(), count);
              reversemap.put(count, apartment.getKey());
              count++;
      }

      // remove method. Takes in the key of an object and removes based on the index the key is mapped to
      public void remove(String key){
              int hash = map.get(key);
              rent.delete(hash);
              sqft.delete(hash);
              count--;
      }

      // return the index of the min rent
      public int minRent(){
              return rent.minIndex();
      }
      // return the value of the min rent as a String
      public String minRentToString(){
              return "" + rent.minKey();
      }
      // return the key associated with the min rent
      public String minRentHash(){
              return "" + reversemap.get(minRent());
      }

      // return the index of the max sqft
      public int maxSqft(){
              return sqft.minIndex();
      }
      // return the value of the max sqft as a String
      public String maxSqftToString(){
              return "" + sqft.minKey();
      }
      // return the key associated with the max sqft
      public String maxSqftHash(){
              return "" + reversemap.get(maxSqft());
      }

}
