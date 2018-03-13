package contactsProgram;

import util.Input;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class methods {

    public static Input ask = new Input();
    public static String path = "src/contactsProgram/contacts";

    public static void mainMenu() {
        System.out.println("1. View Contacts");
        System.out.println("2. Add a new Contact");
        System.out.println("3. Search a contact by name");
        System.out.println("4. Delete an existing contact");
        System.out.println("5. Add series of contacts");
        System.out.println("6. Exit");
    }

    public static List<String> slurp() {
        try {
            return Files.readAllLines(Paths.get(path));
        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
            return null;
        }
    }

    public static void viewContacts() {
        List<String> contacts = slurp();
        printContacts(contacts);
    }

    public static void printContacts(List<String> contacts) {
        System.out.printf("%-15s | %s\n", "First Last", "PhoneNum");
        System.out.println("---------------------------------");
        for (String contact : contacts) {
            String name = contact.substring(0, contact.indexOf("#"));
            String number = contact.substring(contact.indexOf("#") + 1);
            System.out.printf("%-15s | %14s\n", nameFormatter(name), numberFormatter(number));
            System.out.println("---------------------------------");
        }
    }

    public static String nameFormatter(String name){
        String first = name.substring(0, name.indexOf(" "));
        String last = name.substring(name.indexOf(" ") + 1);
        return first.substring(0,1).toUpperCase() + first.substring(1) + " " +
                last.substring(0,1).toUpperCase() + last.substring(1);
    }

    public static String numberFormatter (String number) {
        if (number.length() == 10) {
            return number.replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1)-$2-$3");
        }
        else {
            return number.replaceFirst("(\\d{3})(\\d+)", " $1-$2");
        }
    }


    public static void addContact() {
        List<String> newEntry = new ArrayList<>();
        String name =ask.getString("Enter your name: (First Last)");
        while (checkName(name)){
            System.out.println("Names can only contain letters");
            name = ask.getString("Enter your name: (First Last)");
        }
        Long num = ask.getLong("Enter your phone number: ");
        while(checkNumber(num.toString())){
            System.out.println("Phone number must be either 7 or 10 digits");
            num = ask.getLong("Enter your phone number: ");
        }
        String entry =  name.toLowerCase() + " #" + num ;
        newEntry.add(entry);
            overwrite(newEntry, true);
        if (ask.yesNo("Would you like to add another contact? y/n")){
            addContact();
        }
    }

    public static void searchContacts() {
        String name = ask.getString("What name do you want to find?: ");
        List<String> results = new ArrayList<>();
        for (String contact : slurp()) {
            if (contact.contains(name)) {
                results.add(contact);
            }
        }
        printContacts(results);
    }

    public static void deleteContact(boolean edit) {
        String name = ask.getString("What name would you like to remove?: ");
        List<String> results = new ArrayList<>();
        for (String contact : slurp()) {
            if (!contact.contains(name)) {
                results.add(contact);
            }else{
                if(edit){
                    "Whats the new name or number"
                            results.add(name + number)
                }
            }
        }        overwrite(results, false);

    }

    public static void overwrite(List<String> list, boolean append) {
        try {
            if (append) {
                Files.write(Paths.get(path), list, StandardOpenOption.APPEND);
            } else {
                Files.write(Paths.get(path), list);
            }
        } catch (IOException e) {
            System.out.println("Who Cares!");
        }

    }
    public static void manyContacts(){
        String people = ask.getString("Enter your contacts separated by a comma: \nex.) First Last, xxxxxxx, First Last, xxxxxxx");
        boolean numberEdit = false;
        boolean nameEdit = false;
        List<String> peopleList = Arrays.asList(people.split(","));
        List<String> contacts = new ArrayList<>();
        for (int i = 0; i < peopleList.size(); i+=2){
            numberEdit = checkNumber(peopleList.get(i+1));
            nameEdit = checkName(peopleList.get(i));
            if(numberEdit){
                System.out.println("All phone numbers must be either 7 or 10 digits only");
                System.out.println("Here are your entries, copy and paste it with the proper edits\n");
                System.out.println(people+"\n");
                manyContacts();
                return;
            }
            if(nameEdit){
                System.out.println("Names can only contain letters");
                System.out.println("Here are your entries, copy and paste it with the proper edits\n");
                System.out.println(people+"\n");
                manyContacts();
                return;
            }
            contacts.add(peopleList.get(i).trim().toLowerCase()+ " #"+ peopleList.get(i+1).trim());
        }
            overwrite(contacts, true);
    }
    public static boolean checkNumber(String userNum){
        if(userNum.trim().length()==7 || userNum.trim().length()==10){
            return false;
        }else {
            return true;
        }
    }
    public static boolean checkName(String name){
        if(name.matches("(.*)[^A-Za-z\\s]+(.*)")){
            return true;
        }else {
            return false;
        }
    }

    public static void main(String[] args) {
        checkName("john tom");
    }
}

