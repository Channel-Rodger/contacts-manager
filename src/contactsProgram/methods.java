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
        System.out.println("3. Edit a contact by name");
        System.out.println("4. Search for a contact");
        System.out.println("5. Delete an existing contact");
        System.out.println("6. Add series of contacts");
        System.out.println("7. Exit");
    }

    public static void editMenu() {
        System.out.println("1. Edit Name");
        System.out.println("2. Edit Phone Number");
        System.out.println("3. Edit Name and Phone Number");
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

    public static String addValidName() {
        String name =ask.getString("Enter your name: (First Last)");
        while (checkName(name)){
            name = ask.getString("Enter your name: (First Last)");
        }
        for(String contact: slurp()){
            String contactName = contact.substring(0,contact.indexOf(" #"));
            if(name.toLowerCase().equals(contactName.toLowerCase())){
                System.out.println("That name already exists; choose a different one");
                return addValidName();
            }
        }
        return name;
    }

    public static Long addValidNumber() {
        Long num = ask.getLong("Enter your phone number: ");
        while(checkNumber(num.toString())){
            num = ask.getLong("Enter your phone number: ");
        }
        return num;
    }


    public static void addContact() {
        List<String> newEntry = new ArrayList<>();
        String name =addValidName();
        Long num = addValidNumber();
        String entry =  name.toLowerCase() + " #" + num ;
        newEntry.add(entry);
            overwrite(newEntry, true);
        if (ask.yesNo("Would you like to add another contact? y/n")){
            addContact();
        }
    }

    public static List<String> searchContacts(String prompt) {
        String name = ask.getString(prompt);
        List<String> results = new ArrayList<>();
        for (String contact : slurp()) {
            if (contact.contains(name)) {
                results.add(contact);
            }
        }
        return results;
    }

    public static String howManyEntries(String prompt){
        String name = ask.getString(prompt);
        List<String> deleteChecker = editSearch(name);
        if (deleteChecker.size() > 1) {
            printContacts(deleteChecker);
            System.out.println("Too many names matched, ");
            return howManyEntries(prompt);
        }
        return name;
    }

    public static void deleteContact() {
        String name = howManyEntries("Which name do you want to delete?: ");
        List<String> results = new ArrayList<>();
        for (String contact : slurp()) {
            if (!contact.contains(name)) {
                results.add(contact);
            }
        }
        overwrite(results, false);
        System.out.println("Delete successful!");
    }

    public static List<String> editSearch(String name) {
        List<String> results = new ArrayList<>();
        for (String contact : slurp()) {
            if (contact.contains(name)) {
                results.add(contact);
            }
        }
        return results;
    }

    public static void editContact() {
        String name = howManyEntries("What name would you like to edit");
        List<String> addressBook = new ArrayList<>();
        for (String contact : slurp()) {
            if (!contact.contains(name)) {
                addressBook.add(contact);
            } else {
                    String oldName = contact.substring(0, contact.indexOf("#"));
                    String number = contact.substring(contact.indexOf("#") + 1);
                    editMenu();
                    int userChoice = ask.getInt(1,3);
                    if (userChoice == 1) {
                       oldName = addValidName();
                       addressBook.add(oldName + " #" + number);
                    }
                    if (userChoice == 2) {
                        Long num = addValidNumber();
                        addressBook.add(oldName + " #" + num);
                    }
                    if (userChoice == 3) {
                        oldName = addValidName();
                        Long num = addValidNumber();
                        addressBook.add(oldName + " #" + num);
                    }
             }
        }
        overwrite(addressBook, false);
        System.out.println("Edit successful!");
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
            if(numberEdit || nameEdit){
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
            System.out.println("All phone numbers must be either 7 or 10 digits only");
            return true;
        }
    }
    public static boolean checkName(String name){
        if(name.matches("(.*)[^A-Za-z\\s]+(.*)")){
            System.out.println("Names can only contain letters");
            return true;
        }
        List<String> duplicateNames = new ArrayList<>();
        for(String contact: slurp()){
            String contactName = contact.substring(0,contact.indexOf(" #"));
            if(name.toLowerCase().equals(contactName.toLowerCase())){
                duplicateNames.add(name);
            }
        }
        if (duplicateNames.size() > 0 ){
            System.out.println("Found duplicate name(s): " + duplicateNames);
            System.out.println("Choose a different name");
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        checkName("john tom");
    }
}

