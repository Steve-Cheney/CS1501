/*
   Stephen Cheney || 4275535

   AptTracker class is the main program driver
 */

import java.util.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class AptTracker {

        static MinMaxPQ all = new MinMaxPQ(512); // PQ for all apartments
        static HashMap<String, Apt> allmap = new HashMap<>(); // hashmap for all apartments
        static HashMap<String, MinMaxPQ> citymap = new HashMap<>(); // hashmap of PQs to each city added in

        static Scanner scanner = new Scanner(System.in);

        public static void main(String[] args) throws Exception, IOException {
                System.out.println("\nWelcome to the apartments.org, the sibling site to apartments.com just with less Jeff Goldblum ads");
                // Add apartments
                File apt_File = new File("./apartments.txt");
                BufferedReader apartm = new BufferedReader(new FileReader(apt_File));
                String string_A;

                // read in the file, add the apartment objects
                while((string_A = apartm.readLine()) != null) {
                        if(string_A.charAt(0) == '#') continue;
                        String[] data = string_A.split(":");
                        Apt apt = new Apt(data[0], data[1], data[2], data[3], data[4], data[5]);
                        all.add(apt);
                        allmap.put(apt.getKey(), apt);
                        // create or update the hashmap of MinMaxPQs for each city
                        if(!citymap.containsKey(apt.getCity())) {
                                MinMaxPQ cityPQ = new MinMaxPQ(512);
                                cityPQ.add(apt);
                                citymap.put(apt.getCity(), cityPQ);
                        }else{
                                citymap.get(apt.getCity()).add(apt);
                        }
                }
                // main driver loop
                boolean run = true;
                while(run) {
                        int option = 0;
                        options();
                        option = getOption();
                        if(option > 7 || option < 1) {
                                System.exit(0);
                        }
                        selectOption(option);
                }
        }

        // list all options
        public static void options() {
                System.out.println("\nEnter the number for the following options, any other number quits");
                System.out.println("\t1. Add an apartment");
                System.out.println("\t2. Update an apartment");
                System.out.println("\t3. Remove apartment");
                System.out.println("\t4. Show lowest rent apartment");
                System.out.println("\t5. Show highest square footage apartment");
                System.out.println("\t6. Show lowest price apartment of a city. ");
                System.out.println("\t7. Show highest square footage apartment of a city. \n");
        }

        // take in an option from the user
        public static int getOption(){
                System.out.print("Enter option:  ");
                return scanner.nextInt();
        }

        // switch satement to call option functions
        public static void selectOption(int op) {
                switch(op) {
                case 1:
                        addApartment();
                        break;
                case 2:
                        updateApartment();
                        break;
                case 3:
                        removeApartment();
                        break;
                case 4:
                        showLowestRent();
                        break;
                case 5:
                        showHighestSqft();
                        break;
                case 6:
                        showLowestRentCity();
                        break;
                case 7:
                        showHighestSqftCity();
                        break;
                default:
                }
        }

        // add a new apartment
        public static void addApartment(){
                System.out.println("\nPlease enter in the address for the apartment you wish to add.");
                scanner.nextLine(); // prevent double enter
                System.out.print("\tStreet: ");
                String street = scanner.nextLine();
                System.out.print("\tApartment Number: ");
                String num = scanner.nextLine();
                System.out.print("\tCity: ");
                String city = scanner.nextLine();
                System.out.print("\tZip: ");
                String zip = scanner.nextLine();
                System.out.println("Now enter the rent and square footage.");
                System.out.print("\tRent: $");
                int rent = scanner.nextInt();
                System.out.print("\tSquare Footage: ");
                int sqft = scanner.nextInt();
                // create an apartment object based on input
                Apt apt = new Apt(street, num, city, zip, rent, sqft);
                all.add(apt);
                allmap.put(apt.getKey(), apt);
                //update city hashmap
                if(!citymap.containsKey(apt.getCity())) {
                        MinMaxPQ cityPQ = new MinMaxPQ(512);
                        cityPQ.add(apt);
                        citymap.put(apt.getCity(), cityPQ);
                }else{
                        citymap.get(apt.getCity()).add(apt);
                }
        }

        // update an existing apartment
        public static void updateApartment(){
                System.out.println("\nPlease enter in the address for the apartment you wish to update the rent for.");
                scanner.nextLine(); // prevent double enter
                System.out.print("\tStreet: ");
                String street = scanner.nextLine();
                System.out.print("\tApartment Number: ");
                String num = scanner.nextLine();
                System.out.print("\tCity: ");
                String city = scanner.nextLine();
                System.out.print("\tZip: ");
                String zip = scanner.nextLine();
                //create a hashkey based off of the input
                String hashkey = (street.trim() + num.trim() + city.trim() + zip.trim()).toLowerCase();
                // get the index of this hashkey
                Integer index = all.map.get(hashkey);

                System.out.print("\nWhat is the new rent? $");
                int newRent = scanner.nextInt();
                System.out.println("\nRent for:\n\t" + street + " #" + num + " " + city + ", " + zip + "\nIs now: $" + newRent);
                // use the IndexMinPQ change method to update the rent based on the index and rent
                all.rent.change(index, newRent);
        }

        // remove an apartment from consideration
        public static void removeApartment(){
                System.out.println("\nPlease enter in the address for the apartment you wish to remove.");
                scanner.nextLine(); // prevent double enter
                System.out.print("\tStreet: ");
                String street = scanner.nextLine();
                System.out.print("\tApartment Number: ");
                String num = scanner.nextLine();
                System.out.print("\tCity: ");
                String city = scanner.nextLine();
                System.out.print("\tZip: ");
                String zip = scanner.nextLine();

                String hashkey = (street.trim() + num.trim() + city.trim() + zip.trim()).toLowerCase();

                all.remove(hashkey);
                allmap.remove(hashkey);

                System.out.println("\nApartment at \n\t" + street + " #" + num + " " + city + ", " + zip + "\nHas been removed from consideration.");
        }

        // display the apartment with the lowest rent
        public static void showLowestRent(){
                System.out.println("Lowest rent: " + allmap.get(all.minRentHash()).toString());
        }

        // display the apartment with the greatest square footage
        public static void showHighestSqft(){
                System.out.println("Highest Square Footage: " + allmap.get(all.maxSqftHash()).toString());
        }

        // show the lowest rent within a city
        public static void showLowestRentCity(){
                System.out.print("\nPlease enter the city you'd like to look in: ");
                scanner.nextLine(); // prevent double enter
                String city = scanner.nextLine();
                System.out.println("\n\t" + allmap.get(citymap.get(city).minRentHash()));
        }

        // show the greatest square footage within a city
        public static void showHighestSqftCity(){
                System.out.print("\nPlease enter the city you'd like to look in: ");
                scanner.nextLine(); // prevent double enter
                String city = scanner.nextLine();
                System.out.println("\n\t" + allmap.get(citymap.get(city).maxSqftHash()));
        }

}
