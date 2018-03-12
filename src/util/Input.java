package util;

import java.util.Scanner;

public class Input {
    private Scanner scanner;

    public String getString(){
        System.out.println("Enter a value: ");
        scanner.nextLine();
        return scanner.nextLine();
    }
    public String getString(String prompt){
        System.out.println(prompt);
        return scanner.nextLine();
    }

    public boolean yesNo(){
        String userGuess = getString();
        if(userGuess.equalsIgnoreCase("y") || userGuess.equalsIgnoreCase("yes") ){
            return true;
        }else{
            return false;
        }
    }
    public boolean yesNo(String prompt){
        String userGuess = getString(prompt);
        return(userGuess.equalsIgnoreCase("y") || userGuess.equalsIgnoreCase("yes"));
    }

    public int getInt(int min, int max){
        System.out.println("Enter an integer from " + min + " to " + max);
        int userInt = scanner.nextInt();
        if(userInt < min || userInt > max){
           return getInt(min,max);
        }else {
            return userInt;
        }

    }
    public long getLong(){
        try {
            return Long.valueOf(scanner.nextLine());
        }catch (Exception e){
            System.out.println("Invalid Input, try again: ");
            return getLong();
        }
    }
    public long getLong(String prompt){
        System.out.println(prompt);
        return getLong();
    }


    public double getDouble(double min, double max){
        System.out.println("Enter a Double from " + min + " to " + max);
        double userDbl = scanner.nextDouble();
        if(userDbl < min || userDbl > max){
            return getDouble(min,max);
        }else {
            return userDbl;
        }

    }
    public double getDouble(){
        try {
            return Double.valueOf(scanner.nextLine());
        }catch (Exception e){
            System.out.println("NaN; enter a valid number!");
            return getDouble();
        }

    }
    public double getDouble(String prompt){
        System.out.println(prompt);
        return getDouble();
    }


    public Input() {
        this.scanner = new Scanner(System.in);
    }


//    public static void main(String[] args) {
//        Input ask = new Input(new Scanner(System.in));
//        ask.getInt("Enter a num");
//        ask.getDouble("enter a dbl");
//    }

}
