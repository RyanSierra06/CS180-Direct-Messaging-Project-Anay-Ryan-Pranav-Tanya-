import java.util.Scanner;

public class Application {
    private static final Object gateKeep;

    private User userPassVerification(){
        //SETTING THE USERNAME
        String user="";
        boolean validUser = false;
        do {
            System.out.print("Enter the username without '-' (also it can't be empty): ");
            user = sc.nextLine().trim();
            if(user.contains("-"){
                System.out.println("Username contains '-'. Try again!");
                validUser = false;
            }
                    else if (user.length() == 0){
                System.out.println("Empty username! Try again!");
                validUser = false;
            }else {
                File f =  new File("files/"user+".txt");
                if(f.exists){
                    System.out.println("Username already exists! Try again!");
                    validUser = false;
                }
                else{
                    validUser = true;
                }
            }

        } while (!validUser);

        //SETTING THE PASSWORD
        String pass = "";
        boolean validPass = false;
        do {
            System.out.print("Enter the password without '-' (also it can't be empty): ");
            pass = sc.nextLine().trim();
            if(pass.contains("-"){
                System.out.println("Password contains '-'. Try again!");
                validPass = false;
            }
                    else if (pass.length() == 0){
                System.out.println("Empty password! Try again!");
                validPass = false;
            } else {
                validPass = true;
            }

        } while (!validPass);

        User newUser = null;

        synchronized (gateKeep) {
            try {
                User newUser = new User(user, pass);
            } catch (java.lang.Exception e) {
                e.printStackTrace();
            }
        }

        return newUser

    }

    public static void main(String args[]){
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to the Social Media Application (Phase 1)");

        String userMenu = "User Menu";
        String CHOICE_1 = "1. CREATE A NEW ACCOUNT";
        String CHOICE_2 = "2. LOGIN WITH EXISTING ACCOUNT";
        String CHOICE_3 = "3. EXIT"

        String choice = "";

        boolean validChoice = false;
        do {
            System.out.print(userMenu);
            System.out.println(CHOICE_1);
            System.out.println(CHOICE_2);
            choice = sc.nextLine();
            if(!"123".contains(choice) || choice.length() != 1){
                validChoice = false;
            } else { validChoice = true; }
        } while (!validChoice)

        switch(choice) {
            case "1" -> {
                User newUser = cre();
                System.out.println("Created Login!");
                break;
            }

            case "2" -> {
                System.out.print("Enter the username without '-' (also it can't be empty): ");
                user = sc.nextLine().trim();

                break;
            }

            case "3" -> {
                System.out.println("Thank you for using The Social Media Application (Phase 1)! ")
            }

        }

    }


}