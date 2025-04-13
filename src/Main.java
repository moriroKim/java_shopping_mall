import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DataBase db = new DataBase();
        App app = new App(db);

        app.run();
    }
}