package task1;

import java.security.SecureRandom;
import java.util.concurrent.Semaphore;

public class task2Java {

    private static final Integer BOOKS_AMOUNT = 5;
    private static final Integer NUMBER_OF_SERVICES = 10;
    private static final Semaphore semaphore = new Semaphore(BOOKS_AMOUNT);
    private static final SecureRandom random = new SecureRandom();

    public static void main(String[] args){
        for(int i = 0; i < NUMBER_OF_SERVICES; i++){
            Thread t = new Thread(new Reader());
            t.start();
        }
    }

    private static class Reader implements Runnable {

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + ": standing in queue");
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int onHands = random.nextInt(2);
            if(onHands == 1) {
                System.out.println(Thread.currentThread().getName()
                        + ": book is given to home");
            } else {
                System.out.println(Thread.currentThread().getName()
                        + ": book is given to reading space");
            }


            try {
                int timeout = 1000;
                Thread.sleep((long) timeout * (onHands + 1));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(Thread.currentThread().getName() + ": book is returned");
            semaphore.release();

        }
    }
}
