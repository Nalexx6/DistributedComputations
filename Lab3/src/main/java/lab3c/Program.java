package lab3c;


import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Program {

    private final List<Smoker> smokers;
    private final Supplier supplier;
    private final AtomicInteger smokingInProgress;
    private final AtomicBoolean supplyingInProgress;

    private static SecureRandom random;

    Program(){
        supplier = new Supplier(10);
        smokers = new ArrayList<>();

        for (int i = 0; i < 3; i++){
            smokers.add(new Smoker(i));
        }

        random = new SecureRandom();

        smokingInProgress = new AtomicInteger(-1);
        supplyingInProgress = new AtomicBoolean(true);

    }

    public static void main(String[] args){
        Program program = new Program();
        program.execute();

    }

    public void execute(){

        for (Smoker s :smokers){
            s.start();
        }
        supplier.start();

    }

    private class Smoker extends Thread{
        public Integer item;

        Smoker(Integer item){
            this.item = item;
        }

        @Override
        public void run() {
            while (supplyingInProgress.get()) {

                if (item.equals(smokingInProgress.get())) {
                    System.out.println(Thread.currentThread().getName() + ": Started smoking " + item);

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println(Thread.currentThread().getName() + ": Quit smoking " + item);

                    smokingInProgress.set(-1);
                }

            }
        }
    }

    private class Supplier extends Thread{

        public Integer iterations;

        Supplier(Integer iterations){
            this.iterations = iterations;
        }

        @Override
        public void run(){
            int item;

            for(int i = 0; i < iterations; i++){

                item = random.nextInt(3);

                System.out.println(Thread.currentThread().getName() + " item " + item );

                smokingInProgress.set(item);

                while (smokingInProgress.get() != -1){
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

//                try {
//                    smokers.get(item).join();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }

            supplyingInProgress.set(false);

        }
    }
}
