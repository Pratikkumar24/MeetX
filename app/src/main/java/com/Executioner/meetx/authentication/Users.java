package com.Executioner.meetx.authentication;

public class Users {
    private String username;
    private String password;
    private String email;
    private String phoneNo;
    public Users(){
        this.username=Constants.DEFAULT_USERNAME;
        this.email=Constants.DEFAULT_EMAIL;
        this.password=Constants.DEFAULT_PASSWORD;
        this.phoneNo = Constants.DEFAULT_PHONE_NUMBER;
    }
    public String getPhoneNo(){ return this.phoneNo;}
    public String getUsername(){
        return this.username;
    }
    public String getPassword(){
        return this.password;
    }
    public String getEmail(){
        return this.email;
    }
   public void setUsername(String username){
        this.username=username;
    }
    public void setPassword(String password){
        this.password=password;
    }
    public void setEmail(String email){
        this.email=email;
    }
    public void setPhoneNo(String phoneNo) { this.phoneNo = phoneNo;}
    public String getMetadata(){
      return this.username+"$"+ this.password+"$"+ this.email+"$"+this.phoneNo;
    }
}