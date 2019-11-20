/*
   Stephen Cheney || 4275535

   Apt class is the individual apartment object
 */

public class Apt {

        // Initialize each apartment's values
        private String address = "";
        private String aptNum = "";
        private String city = "";
        private String zip = "";
        private int rent = -1;
        private int sqFeet = -1;
        private String hashKey = null;

        public Apt(String address, String aptNum, String city, String zip, int rent, int sqFeet) {
                this.address = address;
                this.aptNum = aptNum;
                this.city = city;
                this.zip = zip;
                this.rent = rent;
                this.sqFeet = sqFeet;
        }

        public Apt(String address, String aptNum, String city, String zip, String rent, String sqFeet) {
                this.address = address;
                this.aptNum = aptNum;
                this.city = city;
                this.zip = zip;
                this.rent = Integer.parseInt(rent);
                this.sqFeet = Integer.parseInt(sqFeet);
        }

        // Setters and Getters
        public void setAddress(String address){
                this.address = address;
        }
        public void setAptNum(String aptNum){
                this.aptNum = aptNum;
        }
        public void setCity(String city){
                this.city = city;
        }
        public void setZip(String zip){
                this.zip = zip;
        }
        public void setRent(int rent){
                this.rent = rent;
        }
        public void setSqFeet(int sqFeet){
                this.sqFeet = sqFeet;
        }

        public String getAddress(){
                return this.address;
        }
        public String getAptNum(){
                return this.aptNum;
        }
        public String getCity(){
                return this.city;
        }
        public String getZip(){
                return this.zip;
        }
        public int getRent(){
                return this.rent;
        }
        public int getSqFeet(){
                return this.sqFeet;
        }

        // to string
        public String toString() {
                return ("" + address + " #" + aptNum + " " + city + ", " + zip +" || $" + rent + ", " + sqFeet + "sqft");
        }

        // returns the concatenated address as a key for hashing, sets the obj's key
        public String getKey() {
                if(hashKey == null) hashKey = createKey(this.address, this.aptNum, this.city, this.zip);
                return hashKey;
        }
        // creates the concatenated address key
        public String createKey(String a, String an, String c, String z) {
                return (a.trim() + an.trim() + c.trim() + z.trim()).toLowerCase();
        }

}
