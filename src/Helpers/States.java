package Helpers;

public enum States {

    AUTHORIZATION("AUTHORIZATION"),
    TRANSACTION("TRANSACTION");

    private String name = "";

    States(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

}
