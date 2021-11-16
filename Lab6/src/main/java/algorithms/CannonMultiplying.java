package algorithms;

import java.util.Arrays;

public class CannonMultiplying {
    private static int[][] a;
    private static int[][] b;
    private static int process_amount;
    private static int[][] res;

    public static int[][] multiply(int[][] a, int[][] b, int process_amount){
        CannonMultiplying.a = a;
        CannonMultiplying.b = b;
        CannonMultiplying.process_amount = process_amount;

        res = new int[a.length][a.length];

        for(int i = 0; i < a.length; i++){
            for(int j = 0; j < a.length; j++){
                res[i][j] = 0;
            }
        }

        Thread[] tasks = new Thread[process_amount];
        for(int i = 0; i < tasks.length; i++){
            tasks[i] = new Thread(new Task(i));
        }

        for (Thread task : tasks) {
            task.start();

        }

        for (Thread task : tasks) {
            try {
                task.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return res;
    }

    private static class Task implements Runnable{

        private final int part_index;
        public Task(int part_index){
            this.part_index = part_index;
        }

        @Override
        public void run() {
            int pivot = (int) Math.ceil(a.length / (double) process_amount);
            for (int row = part_index * pivot; row < (part_index + 1) * pivot && row < a.length; row++) {
                int counter = 0;
                int a_j = row;
                int b_i = row;
                while (counter < a.length) {
                    int cur_a_j = a_j;
                    int cur_b_i = b_i;

                    for (int i = 0; i < a.length; i++) {
//                    System.out.println(Thread.currentThread().getName() + " " + a[row][cur_a_j] * b[cur_b_i][i]);
                        res[row][i] += a[row][cur_a_j] * b[cur_b_i][i];
                        cur_a_j = (cur_a_j + 1) % a.length;
                        cur_b_i = (cur_b_i + 1) % a.length;
//                    System.out.println(Thread.currentThread().getName() + Arrays.deepToString(res));

                    }

                    a_j = (a_j + 1) % a.length;
                    b_i = (b_i + 1) % a.length;
                    counter++;

                }
            }
        }
    }
}
