package ca.concordia.user;

import ca.concordia.FlightTracker;

public class User {
   private String name;
   private String password;

   public User(String name, String password) {
       this.name = name;
       this.password = password;
   }

   public void setName(String name) {
       this.name = name;
   }

   public String getName() {
       return this.name;
   }

   public void setPassword(String password) {
       this.password = password;
   }

   public String getPassword() {
       return this.password;
   }

   public String toSql() {
       String command = "";

       if (this instanceof SysAdmin) {
           command = "Insert or replace into User (login, pass, userDiscriminator) values ('" + this.name + "','"
                   + this.password + "', " + 2 + ");";
       } else if (this instanceof AirportAdmin) {
           command = "Insert or replace into User (login, pass, userDiscriminator, employedByAirport) values ("
                   + this.name + ",'"
                   + this.password + "', " + 3 + ", '" + ((AirportAdmin) this).getAirport().getLetterCode() + "'');";
       } else {
           command = "Insert or replace into User (login, pass, userDiscriminator) values ('" + this.name + "','"
                   + this.password + "', " + 1 + ");";
       }

       return command;
   }

   private void sendtoDB() {
       String command = this.toSql();
       FlightTracker.Tracker.accessDB().passStatement(command);
   }

   public boolean save(){
        try{
            sendtoDB();
            return true;
        }catch(Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            return false;
        }
   }

}
