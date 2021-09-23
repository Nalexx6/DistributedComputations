package lab3b;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;


public class Program {

    private final AtomicBoolean workFinished;
    private final AtomicBoolean isBusy;
    private final Object barberMonitor;
    private final Object queueMonitor;
    private final Queue<Customer> queue;

    Program(){
        barberMonitor = new Object();
        queueMonitor = new Object();
        workFinished = new AtomicBoolean(false);
        isBusy = new AtomicBoolean(false);
        queue = new ConcurrentLinkedQueue<>();
    }

    public static void main(String[] args) {
        Program program = new Program();

        program.simulateBarbershop();
    }

    public void simulateBarbershop(){
        Thread barber = new Thread(new Barber());

        barber.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        for(int i = 0; i < 3; i++){
            Customer customer = new Customer();
            queue.add(customer);
            System.out.println(Thread.currentThread().getName() + ": Customer arrives");

            customer.start();
        }

        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Customer customer = new Customer();
        queue.add(customer);
        System.out.println(Thread.currentThread().getName() + ": Customer arrives");

        customer.start();

        workFinished.set(true);

    }

    private class Barber implements Runnable {

        @Override
        public void run(){
            synchronized (barberMonitor) {
                System.out.println(Thread.currentThread().getName() + ": Day is started");

                while (!workFinished.get() || !queue.isEmpty()) {
                    if (queue.isEmpty()) {
                        try {
                            System.out.println(Thread.currentThread().getName() + ": Barber falling asleep");
                            isBusy.set(false);
                            barberMonitor.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Customer customer = queue.remove();
                        System.out.println(Thread.currentThread().getName() + ": Getting next customer: "
                                + customer.getName());
                        customer.startTrimming();
                    }
                }

                System.out.println(Thread.currentThread().getName() + ": Day is finished");
            }
        }
    }


//        }

    private class Customer extends Thread {

        private boolean isTrimmed;

        Customer(){
            isTrimmed = false;
        }

        @Override
        public void run(){
            synchronized (queueMonitor) {
                System.out.println(Thread.currentThread().getName() + ": Going to the queue");

                while (!isTrimmed) {
                    if (!isBusy.get()) {
                        synchronized (barberMonitor) {
                            barberMonitor.notify();
                            isBusy.set(true);
                            System.out.println(Thread.currentThread().getName() + ": Awaking barber");
                        }
                    } else {

                        try {
                            System.out.println(Thread.currentThread().getName() + ": Falling asleep");
                            queueMonitor.wait();

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        public void startTrimming(){
            System.out.println(Thread.currentThread().getName() + ": Trimming started");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(Thread.currentThread().getName() + ": Trimming finished");
            isTrimmed = true;
        }
    }
}

