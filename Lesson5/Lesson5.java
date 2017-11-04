import java.util.ArrayList;
import java.util.List;

/**
 * Java 2. Lesson 5.
 *
 *  @author Dmitry Grinshteyn
 *  @version dated 2017-09-04
 */


public class Lesson5 {
    public static void main(String[] args) {
        new Array1();
        new Array2();
    }
}

class Array1{
    final int SIZE = 10000000;
    float[] arr = new float[SIZE];

    Array1() {
        for (int i = 0; i < SIZE; i++) {
            arr[i] = 1;
        }
        long a = System.currentTimeMillis();
        for (int i = 0; i < SIZE; i++) {
            arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }
        System.out.println("Время обработки массива последовательно: " + (System.currentTimeMillis() - a) + " мс");
    }

}

class Array2{
    final int SIZE = 10000000;
    final int H = SIZE / 2;
    float[] arr = new float[SIZE];
    float[] a1 = new float[H];
    float[] a2 = new float[H];

    Array2() {
        for (int i = 0; i < SIZE; i++) {
            arr[i] = 1;
        }
        long a = System.currentTimeMillis();
        System.arraycopy(arr, 0, a1, 0, H);
        System.arraycopy(arr, H, a2, 0, H);
        Thread mt1 = new MyThread(a1, 0);
        Thread mt2 = new MyThread(a2, H);
        mt1.start();
        mt2.start();
        try {
            mt1.join();
            mt2.join();
        }catch (InterruptedException e) {
            e . printStackTrace ();
        }
        System.arraycopy(a1, 0, arr, 0, H);
        System.arraycopy(a2, 0, arr, H, H);
        System.out.println("Время обработки массива при разбивке на два подмассива: " + (System.currentTimeMillis() - a) + " мс");
    }

    class MyThread extends Thread {
        float[] array;
        int delta;

        public void run() {
            for (int i = 0; i < H; i++) {
                array[i] = (float)(array[i] * Math.sin(0.2f + (i + delta) / 5) * Math.cos(0.2f + (i + delta) / 5) * Math.cos(0.4f + (i + delta) / 2));
            }

        }
        MyThread(float[] array, int delta){
            this.array = array;
            this.delta = delta;        }
    }

}

