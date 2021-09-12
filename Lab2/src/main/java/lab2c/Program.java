package lab2c;

import javafx.util.Pair;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Program {

    private List<Pair<Integer, Integer>> fighters;
    private List<Thread> fights;

    public Program(Integer amountPower){
        int quantity = (int) Math.pow(2, amountPower);

        fighters = Collections.synchronizedList(new ArrayList<>());

        SecureRandom random = new SecureRandom();
        for(int i = 0; i < quantity; i++){
            fighters.add(new Pair<>(random.nextInt(1000), i % 2 + 1));
        }

        fights = new ArrayList<>();

    }

    public static void main (String[] args){
        lab2c.Program program = new lab2c.Program(5);

        if(program.executeTournament() == 1){
            System.out.println("First monastery won");
        } else {
            System.out.println("Second monastery won");
        }
    }

    public Integer executeTournament(){

        while (fighters.size() > 1) {

            fights.clear();

            for (int i = 0; i < fighters.size(); i += 2) {
                Thread thread = new FightExecutor(i);
                fights.add(thread);
            }

            for(Thread f : fights){
                f.start();
            }

            for(Thread f : fights){
                try {
                    f.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            fighters.removeIf(f -> f.getKey() == 0);

            System.out.println("----------------------------------------\n" +
                    "Round finished\n -------------------------------------");
        }

        return fighters.get(0).getValue();

    }

    private class FightExecutor extends Thread{

        int pairIndex;

        FightExecutor(int pairIndex){
            this.pairIndex = pairIndex;
        }

        public void run(){
            fight(pairIndex);
        }
    }

    public void fight(int pairIndex){

        Pair<Integer, Integer> firstFighter = fighters.get(pairIndex);
        Pair<Integer, Integer> secondFighter = fighters.get(pairIndex + 1);

        synchronized (System.out){
            System.out.println(Thread.currentThread().getName() + " First fighter from " + firstFighter.getValue()
                    + " monastery" + " with force equals " + firstFighter.getKey());

            System.out.println(Thread.currentThread().getName() + " Second fighter from " + secondFighter.getValue()
                    + " monastery" + " with force equals " + secondFighter.getKey());
        }

        if(firstFighter.getKey() > secondFighter.getKey()){
            fighters.set(pairIndex, new Pair<>(firstFighter.getKey() / 2, firstFighter.getValue()));
            fighters.set(pairIndex + 1, new Pair<>(0, secondFighter.getValue()));

        } else {
            fighters.set(pairIndex, new Pair<>(0, firstFighter.getValue()));
            fighters.set(pairIndex + 1, new Pair<>(secondFighter.getKey() / 2, secondFighter.getValue()));
        }

    }

}
