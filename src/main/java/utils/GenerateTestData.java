package utils;

public class GenerateTestData {
    //Generate Random Test Data
    public static int randomNumber = (int) (Math.random() * 10000); // Generate a random number between 0 and 999
    public static String TestFullName = generateRandomFullName();
    public static String TestUserName = generateRandomUsername();
    public static String TestPassword=generateRandomPassword();

    //Generate Test Full Name:
    public static String generateRandomFullName() {

        return "Automated" + " " + "User" + "_" + randomNumber;
    }

    //Generate Test User:
    public static String generateRandomUsername() {

        return "Automated_User" + "_" + randomNumber;
    }

    //Generate Password:
    public static String generateRandomPassword() {

        return "Automated_User" + "_" + randomNumber;
    }




}
