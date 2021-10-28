package lab5b;

import java.util.concurrent.CyclicBarrier;

public class Program {
    private static final int NUMBER_OF_THREADS = 4;

    public static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(NUMBER_OF_THREADS);
        Checker checker = new Checker(NUMBER_OF_THREADS);

        Thread firstChanger = new Thread(new Changer("ABCDCDAABCD", barrier, checker, 1));
        Thread secondChanger = new Thread(new Changer("AAACAACBBAC", barrier, checker, 2));
        Thread thirdChanger = new Thread(new Changer("ACDCADCACDC", barrier, checker, 3));
        Thread fourthChanger = new Thread(new Changer("CDABBABCDAB", barrier, checker, 4));

        firstChanger.start();
        secondChanger.start();
        thirdChanger.start();
        fourthChanger.start();
    }
}
