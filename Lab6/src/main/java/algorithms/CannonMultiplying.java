package algorithms;

import java.util.Arrays;

public class CannonMultiplying {
    private static int[][] a;
    private static int[][] b;
    private static int[][] res;

    public static int[][] multiply(int[][] a, int[][] b){
        CannonMultiplying.a = a;
        CannonMultiplying.b = b;

        res = new int[a.length][a.length];

        for(int i = 0; i < a.length; i++){
            for(int j = 0; j < a.length; j++){
                res[i][j] = 0;
            }
        }

        Thread[] tasks = new Thread[a.length];
        for(int i = 0; i < a.length; i++){
            tasks[i] = new Thread(new Task(i));
        }

        for(int i = 0; i < a.length; i++){
            tasks[i].start();

        }

        for(int i = 0; i < a.length; i++){
            try {
                tasks[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return res;
    }

    private static class Task implements Runnable{

        private final int row;
        public Task(int row){
            this.row = row;
        }

        @Override
        public void run() {
            int counter = 0;
            int a_j = row;
            int b_i = row;
            while (counter < a.length){
                int cur_a_j = a_j;
                int cur_b_i = b_i;

                for(int i = 0; i < a.length; i++){
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
