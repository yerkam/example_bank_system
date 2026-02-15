package src;

public class Main {
    public static void main(String[] args) {
        FileHandler fileHandler = new FileHandler();
        fileHandler.createCheckingAccount("John", "Doe", 123456789, 1234, 1000.0, true);
    }
}
