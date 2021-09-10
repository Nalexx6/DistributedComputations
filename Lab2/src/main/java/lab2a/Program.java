package lab2a;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Program {

    private List<List<Integer>> forest;
    private final AtomicBoolean found;
    private final Integer forestSize;
    public final Integer threadsNumber;
    private final AtomicInteger nextSection;

    public Program(Integer forestSize){
        this.forestSize = forestSize;
        this.threadsNumber = (int) Math.sqrt(forestSize);

        forest = new ArrayList(forestSize);
        for (int i = 0; i < forestSize; i++){
            List<Integer> f = new ArrayList<>(forestSize);
            for (int j = 0; j < forestSize; j++){
                f.add(0);

            }
            forest.add(f);
        }

        SecureRandom random = new SecureRandom();
        int section = random.nextInt(forestSize);
        int cell = random.nextInt(forestSize);
        System.out.println("Winnie is in section " + section + " cell " + cell);
        forest.get(section).set(cell, 1);

        found = new AtomicBoolean(false);
        nextSection = new AtomicInteger(0);

    }

    public static void main (String[] args){
        Program program = new Program(1000);

        program.executeFind();
    }

    private void executeFind(){
        for(int i = 0; i <  this.threadsNumber; i++) {
            Thread th = new BeeThread();
            th.start();
        }
    }

    private class BeeThread extends Thread {

        public BeeThread(){

        }

        @Override
        public void run() {
            while (!found.get() && nextSection.get() < forestSize) {
                nextSection.set(nextSection.get() + 1);
                checkSection(nextSection.get() - 1);
            }
        }
    }


    private void checkSection(int sectionNumber){
        if(found.get()){
            return;
        }

        System.out.println(Thread.currentThread().getName() + " searching in section " + sectionNumber);
        List<Integer> section = forest.get(sectionNumber);
        if (section.contains(1)){
            System.out.println(Thread.currentThread().getName() + " found Winnie on section" + sectionNumber);
            found.set(true);
        }
    }
}
