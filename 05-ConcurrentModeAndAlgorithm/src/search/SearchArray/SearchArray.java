package search.SearchArray;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SearchArray {
    static int[] arr;
    static ExecutorService es = Executors.newCachedThreadPool();
    static final int THREAD_NUM = 2;
    static AtomicInteger result = new AtomicInteger(-1);

    public static int search(int searchValue, int beginPos, int endPos) {
        int i = 0;
        for (i = beginPos; i < endPos; i++) {
            if (result.get() >= 0)
                return result.get();
            if (arr[i] == searchValue) {
                if (!result.compareAndSet(-1, i))
                    return result.get();
                return i;
            }
        }
        return -1;
    }

    public static class SearchTask implements Callable<Integer> {
        int begin, end, searchValue;

        public SearchTask(int begin, int end, int searchValue) {
            this.begin = begin;
            this.end = end;
            this.searchValue = searchValue;
        }

        @Override
        public Integer call() throws Exception {
            int re = search(searchValue, begin, end);
            return re;
        }
    }

    public static int pSeach(int searchValue) throws ExecutionException, InterruptedException {
        int subArrSize = arr.length/THREAD_NUM + 1;
        List<Future<Integer>> re = new ArrayList<Future<Integer>>();
        for (int i = 0; i < arr.length; i += subArrSize) {
            int end = i + subArrSize;
            if (end >= arr.length) end = arr.length;
            re.add(es.submit(new SearchTask(searchValue, i, end)));
        }

        for (Future<Integer> future : re) {
            if (future.get() >= 0)
                return future.get();
        }
        return -1;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        arr = new int[10000];
        for (int i = 0 ; i < 10000; i++) {
            arr[i] = i;
        }
        int result = pSeach(10);
        System.out.println(result);
    }
}
