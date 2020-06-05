package util;

public class Printer {

    private Printer() {
    }

    public static void print(String message){
        System.out.println(message);
    }

    public static void print(Object o){
        System.out.println(o.toString());
    }
}
