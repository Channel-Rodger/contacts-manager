package contactsProgram;


import util.Input;

public class main {

    public static void main(String[] args) {
        Input ask = new Input();

        int choice = 0;
        while (choice != 7) {

            if(choice == 1) {
                methods.viewContacts();
            }
            if(choice == 2) {
                methods.addContact();
            }
            if (choice == 3) {
                methods.editContact();
            }
            if(choice == 4) {
                methods.printContacts(methods.searchContacts("What name do you want to find?"));

            }
            if(choice == 5) {
                methods.deleteContact();
            }
            if(choice == 6){
                methods.manyContacts();
            }
            methods.mainMenu();
            choice = (int) ask.getLong("Enter an option (1,2,3,4 or 5)");
        }
    }
}