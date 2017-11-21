package almundo.callcenter;

import java.util.Queue;
import java.util.Random;

/**
 * Created by facundo on 20/11/2017.
 */
public abstract class Empleado implements Runnable {

    private Random random = new Random();

    public int RDM_MAX = 10;
    public int RDM_MIN = 5;

    protected Call call;
    protected Queue queue;

    public Call getCall() {
        return call;
    }

    public void setCall(Call call) {
        this.call = call;
    }

    public Queue getQueue() {
        return queue;
    }

    public void setQueue(Queue queue) {
        this.queue = queue;
    }

    public void run() {
        int randomNum = random.nextInt((RDM_MAX - RDM_MIN) + 1) + RDM_MIN;

        try {
            String threadName = Thread.currentThread().getName();

            System.out.println("Processing thread: " + threadName + " [" + this.getClass().getSimpleName() + "]");

            Thread.sleep(randomNum * 1000 );

            setCall(null);
            queue.offer(this);

            System.out.println("End thread " + threadName + " in " + randomNum + " seconds");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
