package lab5b;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Changer implements Runnable {
    private final Random random = new Random();
    private String currentString;
    private final CyclicBarrier barrier;
    private final Checker checker;
    private boolean running = true;
    private int abCount;
    private final int indexOfThread;

    public Changer(String str, CyclicBarrier barrier, Checker checker, int index){
        this.currentString = str;
        this.barrier = barrier;
        this.checker = checker;
        this.abCount = countAbMentioning(str);
        this.indexOfThread = index;
    }

    @Override
    public void run(){
        while(running) {
            int randIndex = random.nextInt(currentString.length());
            switch (currentString.charAt(randIndex)) {
                case 'A': {
                    currentString = currentString.substring(0, randIndex) + 'C' + currentString.substring(randIndex + 1);
                    abCount--;
                    break;
                }
                case 'B': {
                    currentString = currentString.substring(0, randIndex) + 'D' + currentString.substring(randIndex + 1);
                    abCount--;
                    break;
                }
                case 'C': {
                    currentString = currentString.substring(0, randIndex) + 'A' + currentString.substring(randIndex + 1);
                    abCount++;
                    break;
                }
                case 'D': {
                    currentString = currentString.substring(0, randIndex) + 'B' + currentString.substring(randIndex + 1);
                    abCount++;
                    break;
                }
            }
            checker.getInfo(abCount);
            System.out.println("Thread #" + this.indexOfThread + " " + currentString + " " + abCount);
            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
            if(this.indexOfThread == 1) {
                System.out.println();
            }
            running = checker.isRunning();
        }
    }

    private int countAbMentioning(String str) {
        int count = 0;
        for(int i = 0; i < str.length(); i++){
            if(str.charAt(i) == 'A' || str.charAt(i) == 'B'){
                count++;
            }
        }
        return count;
    }
}
