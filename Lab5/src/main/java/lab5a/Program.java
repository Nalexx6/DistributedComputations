package lab5a;

import java.security.SecureRandom;
import java.util.Arrays;

public class Program {
    private static final SecureRandom random = new SecureRandom();
    private static final int SIZE = 150;
    private static final int NUMBER_OF_PARTS = 3;
    private static final Thread[] threads = new Thread[NUMBER_OF_PARTS];
    private static final int [] recruits = new int[SIZE];
    private static final Barrier barrier = new Barrier(NUMBER_OF_PARTS);

    public static void main(String[] args) {
        RecruitsPart.fillFinishedArray(NUMBER_OF_PARTS);
        fillRecruitsArray();
        createAndStartThreads();
        System.out.println("Result: " + Arrays.toString(recruits));
    }

    private static void fillRecruitsArray() {
        for(int i = 0; i < SIZE; i++) {
            if(random.nextBoolean()) {
                recruits[i] = 1;
            } else {
                recruits[i] = 0;
            }
        }
    }

    private static void createAndStartThreads() {
        for(int i = 0; i < threads.length; i++){
            if(i == 0) {
                threads[i] = new Thread(new RecruitsPart(recruits, barrier, i,
                        0, (i + 1) * (SIZE / NUMBER_OF_PARTS)  + 1));
            } else if(i == threads.length - 1){
                threads[i] = new Thread(new RecruitsPart(recruits, barrier, i,
                        (i) * (SIZE / NUMBER_OF_PARTS) - 1, (i + 1) * (SIZE / NUMBER_OF_PARTS)));
            } else {
                threads[i] = new Thread(new RecruitsPart(recruits, barrier, i,
                        (i) * (SIZE / NUMBER_OF_PARTS) - 1, (i + 1) * (SIZE / NUMBER_OF_PARTS) + 1));
            }
        }
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
