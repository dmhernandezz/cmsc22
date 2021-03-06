import java.util.Scanner;

/**
 * Created by Danielle98 on 12/6/2016.
 */
public class Rule30Main {

    public static final int COUNT = 10;
    private static int a;
    private static int[][] dan = new int[31][31*2+1];

    public static void main(String[] args) {
        /** Main is based from Sample Codes on github*/

        System.out.println("Generate Number: ");
        Scanner scan = new Scanner(System.in);
        a = scan.nextInt();

        for(int i = 0; i < a; i++) {
            for (int j = 0; j < (a * 2 + 1); j++) {
                if(i == 0 && j == a*2/2){
                    dan[i][j] = 1;
                } else {
                    dan[i][j] = 0;
                }
            }
        }

        MyThread[] threads = new MyThread[COUNT];
        int colsPerThread = a*2/COUNT;
        int start = 0;
        int end = start + colsPerThread;

        for (int i = 0; i < COUNT; i++) {
            threads[i] = new MyThread(start,end,dan, a);
            start = end;
            end = start + colsPerThread;
        }

       int d = 0;
       for(int z = 0; z < a; z++) { //RESURRECTS THREAD TO REPEAT PER ROW
           for (d = d%COUNT; d < COUNT; d++) {
               threads[d].start();
           }
       }

        for (int i = 0; i < COUNT; i++) {
            while (threads[i].isAlive()) {
                try {
                    threads[i].join();
                } catch (InterruptedException e) {
                    System.err.println("thread interrupted: " + e.getMessage());
                }
            }
        }

    }
}

class MyThread extends Thread {
    int start;
    int end;
    int gen;
    int[][] dan;
    int row = 1;

    public MyThread(int start, int end, int[][] dan, int gen) {
        this.start = start;
        this.end = end;
        this.dan = dan;
        this.gen = gen;
    }

    public void run() {
        for (int i = row; i < gen; i++) {
            for (int j = start+1; j < end; j++) {
                if (dan[i - 1][j - 1] == 1) {
                    if (dan[i - 1][j] == 1 || dan[i - 1][j + 1] == 1) {
                        dan[i][j] = 0;
                    } else {
                        dan[i][j] = 1;
                    }
                }

                if (dan[i - 1][j - 1] == 0) {
                    if (dan[i - 1][j] == 1 || dan[i - 1][j + 1] == 1) {
                        dan[i][j] = 1;
                    } else {
                        dan[i][j] = 0;
                    }
                }
            }
        break;
        }
    }

}

/** In Theory
16 Generations

 t1   t2   t3   t4   t5   t6   t7   t8   t9   t10
 000  000  000  000  000  100  000  000  000  0000 -> ROW1 Default

 000  000  000  000  001  110  000  000  000  0000 -> 3 cols generated per thread ... repeats per row (Col priority)
                                                    Synchronized so that the not-yet-generated-rows can properly base its pattern sa row
                                                    prior to it(then the threads will "resurrect" to do the same thing but with the  new
                                                    default row which was generated by the threads)
 000  000  000  000  011  001  000  000  000  0000
 000  000  000  000  110  111  100  000  000  0000
 000  000  000  001  100  100  010  000  000  0000
 000  000  000  011  011  110  111  000  000  0000
 000  000  000  110  010  000  100  100  000  0000
 000  000  001  101  111  001  111  110  000  0000
 000  000  011  001  000  111  000  001  000  0000
 000  000  110  111  101  100  100  011  100  0000
 000  001  100  100  001  011  110  110  010  0000
 000  011  011  110  011  010  000  101  111  0000
 000  110  010  001  110  011  001  101  000  1000
 001  101  111  011  001  110  111  001  101  1100
 011  001  000  010  111  000  100  111  001  0010
 110  111  100  110  100  101  111  100  111  1111




 **/