package utils;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;

public class CheckInput
{
    private static Scanner sc = new Scanner(System.in);

    public static int readInt(String prompt) {

        int value = 0;
        boolean valid = false;
        while (!valid) {
            System.out.print(prompt);
            try {
                value = sc.nextInt();
                valid = true;
            } catch (InputMismatchException e) {
                System.out.println("Erreur : La valeur doit être un nombre entier.");
                sc.next();
            }
        }
        return value;
    }


    public static double readDouble(String prompt) {
        double value = 0.0;
        boolean valid = false;
        while (!valid) {
            System.out.print(prompt);
            try {
                value = sc.nextDouble();
                valid = true;
            } catch (InputMismatchException e) {
                System.out.println("Erreur : La valeur doit être un nombre décimal.");
                sc.next();
            }
        }
        return value;
    }
    public static boolean readBool(String prompt){
        boolean val=false;
        try{
            while (true) {
                System.out.print(prompt);
                val = sc.nextBoolean();
                if(val==true || val==false){
                    return val;
                }
            }
        }
        catch (InputMismatchException e){
            System.out.println("Erreur : La valeur doit être boolean.");
            sc.next();
        }
        return val;
    }


    public static LocalDate readDate(String prompt) {
        LocalDate date = null;
        boolean valid = false;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        while (!valid) {
            System.out.print(prompt);
            String input = sc.next();
            try {
                date = LocalDate.parse(input, formatter);
                valid = true;
            } catch (DateTimeException e) {
                System.out.println("Erreur : La date doit être au format yyyy-mm-dd.");
            }
        }

        return date;
    }
}
