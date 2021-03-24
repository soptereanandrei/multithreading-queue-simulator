import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Queue implements Runnable{
    private BlockingQueue<Task> tasks;
    private AtomicInteger waitingPeriod; //time to serve all present clients in queue

    @Override
    public void run() {

    }
}
