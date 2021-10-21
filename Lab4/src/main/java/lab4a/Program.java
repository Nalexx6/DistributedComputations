package lab4a;

import java.security.SecureRandom;

public class Program {
    private static final String fileName = "src/main/java/lab4a/database.txt";

    public static void main(String... args) {
        Locker lock = new Locker();
        Reader reader = new Reader(fileName, lock);
        Writer writer = new Writer(fileName, lock);

        String number = "1000";
        String name = "Nikita";

        try {
            SecureRandom random = new SecureRandom();
            System.out.println("Status of adding operation: " +
                    writer.changeFile(Instructions.ADD, "Name" + random.nextInt(100),
                            "1" + (random.nextInt(100) + 1000)));
            System.out.println("Status of adding operation: " +
                    writer.changeFile(Instructions.ADD, name, number));
            System.out.println("Name with number 26352673: " +
                    reader.completeSearch(Instructions.FIND_NAME_BY_NUMBER, "26352673"));
            System.out.println("Number of Nikita: " +
                    reader.completeSearch(Instructions.FIND_NUMBER_BY_NAME, name));
            System.out.println("Status of removing operation: " +
                    writer.changeFile(Instructions.REMOVE, name,
                            number));
            System.out.println("Name of 1000: " +
                    reader.completeSearch(Instructions.FIND_NAME_BY_NUMBER, number));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
