package algorithms;

public class TapeMultiplying {

    private static int[][] a;
    private static int[][] b;
    private static int[][] res;

    public static int[][] multiply(int[][] a, int[][] b){
        TapeMultiplying.a = a;
        TapeMultiplying.b = b;

        res = new int[a.length][a.length];

        for(int i = 0; i < a.length; i++){
            for(int j = 0; j < a.length; j++){
                res[i][j] = 0;
            }
        }

        Thread[] tapes = new Thread[a.length];
        for(int i = 0; i < a.length; i++){
            tapes[i] = new Thread(new Tape(i));
        }

        for(int i = 0; i < a.length; i++){
            tapes[i].start();

        }

        for(int i = 0; i < a.length; i++){
            try {
                tapes[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return res;
    }

    private static class Tape implements Runnable{

        private final int row;
        public Tape(int row){
            this.row = row;
        }

        @Override
        public void run() {
            int counter = 0;
            int index = row;
            while (counter < a.length){
                int cell = 0;
                for(int i = 0; i < a.length; i++){
                    cell += a[row][i] * b[i][index];
                }

                res[row][index] = cell;
                counter++;
                index = (index + 1) % a.length;
            }
        }
    }


}
