package sort;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Sort {
    //bubble sort
    public static void bubbleSort(int[] arr) {
        for (int i = arr.length - 1; i > 0; i--) {
            for (int j = 0; j < i; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    //奇偶交换排序
    public static void oddEvenSort(int[] arr) {
        int exchFlag = 1, start = 0;
        while (exchFlag == 1 || start == 1) {
            exchFlag = 0;
            for (int i = start; i < arr.length; i += 2) {
                if (arr[i] > arr[i +1]) {
                    int temp = arr[i];
                    arr[i] = arr[i +1];
                    arr[i + 1] = temp;
                    exchFlag = 1;
                }
            }
            if (start == 0)
                start = 1;
            else
                start = 0;
        }
    }

    static int eachFlag = 1;
    static ExecutorService es = Executors.newCachedThreadPool();
    static final int THREAD_NUM = 2;
    static AtomicInteger result = new AtomicInteger(-1);

    static synchronized int getEachFlag() {
        return eachFlag;
    }

    static synchronized void setEachFlag(int eachFlag) {
        Sort.eachFlag = eachFlag;
    }

    static int[] arr;

    public static class OddEvenSortTask implements Runnable {
        int i;
        CountDownLatch latch;

        public OddEvenSortTask(int i, CountDownLatch latch) {
            this.i = i;
            this.latch = latch;
        }

        @Override
        public void run() {
            if (arr[i] > arr[i+1]) {
                int temp = arr[i];
                arr[i] = arr[i+1];
                arr[i+1] = temp;
                setEachFlag(1);
            }
            latch.countDown();
        }
    }

    public static void pOddEvenSort(int[] arr) throws InterruptedException {
        int start = 0;
        while (getEachFlag() == 1 || start == 1) {
            setEachFlag(0);
            CountDownLatch latch = new CountDownLatch(arr.length/2 - (arr.length%2 == 0?start:0));
            for (int i = start; i < arr.length - 1; i += 2) {
                es.submit(new OddEvenSortTask(i, latch));
            }
            latch.await();
            if (start == 0)
                start = 1;
            else
                start = 0;
        }
    }

    //traditional insertSort
    public static void insertSort(int[] Sort) {
        int length = arr.length;
        int j, i, key;
        for (i = 0; i < length; i++) {
            key = arr[i];
            j = i - 1;
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j+1] = key;
        }
    }

    //traditional shellSort
    public static void shellSort(int[] arr) {
        int h = 1;
        while (h <= arr.length / 3) {
            h = h * 3 + 1;
        }
        while ( h > 0) {
            for (int i = h; i < arr.length; i++) {
                if (arr[i] < arr[i - h]) {
                    int temp = arr[i];
                    int j = i - h;
                    while (j >= 0 && arr[j] > temp) {
                        arr[j+h] = arr[j];
                        j -= h;
                    }
                    arr[j + h] = temp;
                }
            }
            h = (h - 1) / 3;
        }
    }

    public static class ShellSortTask implements Runnable {

        int i = 0;
        int h = 0;
        CountDownLatch latch;

        public ShellSortTask(int i, int h, CountDownLatch latch) {
            this.i = i;
            this.h = h;
            this.latch = latch;
        }

        @Override
        public void run() {
            if (arr[i] < arr[i - h]) {
                int tmp = arr[i];
                int j = i - h;
                while (j >= 0 && arr[j] > tmp) {
                    arr[j+h] = arr[j];
                    j -= h;
                }
                arr[j+h] = tmp;
            }
            latch.countDown();
        }
    }

    public static void pShellSort(int[] arr) throws InterruptedException {
        int h = 1;
        CountDownLatch latch = null;
        while (h < arr.length/3) {
            h = h * 3 + 1;
        }
        while (h > 0) {
            System.out.println("h = " + h);
            if (h >= 4)
                latch = new CountDownLatch(arr.length - h);
            for (int i = h; i < arr.length; i++) {
                if (h >= 4) {
                    es.execute(new ShellSortTask(i,h,latch));
                } else {
                    if (arr[i] < arr[i-h]) {
                        int temp = arr[i];
                        int j = i - h;
                        while (j >= 0 && arr[j] > temp) {
                            arr[j+h] = arr[j];
                            j -= h;
                        }
                        arr[j+h] = temp;
                    }
                    System.out.println(Arrays.toString(arr));
                }
            }
            latch.await();
            h = (h-1)/3;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int[] arr = new int[100];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = new Random().nextInt(1000);
        }
        System.out.println(Arrays.toString(arr));
        pShellSort(arr);
    }
}
