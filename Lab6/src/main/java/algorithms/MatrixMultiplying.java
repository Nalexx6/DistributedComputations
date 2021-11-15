package algorithms;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;


public class MatrixMultiplying {

    private static final int SIZE = 2;


    public static void main(String[] args){
        SecureRandom random = new SecureRandom();
        int[][] a = new int[SIZE][SIZE];
        int[][] b = new int[SIZE][SIZE];

        for(int i = 0; i < a.length; i++){
            for(int j = 0; j < a.length; j++){
                a[i][j] = random.nextInt(10);
                b[i][j] = random.nextInt(10);
            }
        }

        System.out.println(Arrays.deepToString(a));
        System.out.println(Arrays.deepToString(b));

        System.out.println(Arrays.deepToString(simpleMultiplying(a, b)));
        System.out.println(Arrays.deepToString(FoxMultiplying.multiply(a, b)));

    }

    public static int[][] simpleMultiplying(int[][] a, int[][] b){
        int[][] res = new int[a.length][a.length];

        for(int i = 0; i < a.length; i++){
            for(int j = 0; j < a.length; j++){
                res[i][j] = 0;
                for(int k = 0; k < a.length; k++){
                    res[i][j] += a[i][k] * b[k][j];
                }
            }
        }

        return res;
    }
}
