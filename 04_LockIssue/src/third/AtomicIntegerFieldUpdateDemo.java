package third;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public class AtomicIntegerFieldUpdateDemo {
    public static class Candidate {
        int id;
        volatile int score;
    }

    public final static AtomicIntegerFieldUpdater<Candidate> scoreUpdate = AtomicIntegerFieldUpdater.newUpdater(Candidate.class, "score");

    public static AtomicInteger allScore = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        final Candidate candidate = new Candidate();
        Thread[] threads = new Thread[10000];
        for (int i = 0 ; i < 10000; i++) {
            threads[i] = new Thread(() -> {
                if (Math.random() > 0.4) {
                    scoreUpdate.incrementAndGet(candidate);
                    allScore.incrementAndGet();
                }
            });
            threads[i].start();
        }

        for (int k = 0; k < 10000; k++) threads[k].join();
        System.out.println("score = " + candidate.score);
        System.out.println("allScore = " + allScore);
    }
}
