import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Queue implements Runnable{
    private LinkedBlockingQueue<Client> clients;

    private AtomicInteger waitingPeriod; //time to serve all present clients in queue

    public AtomicInteger getWaitingPeriod() {
        return waitingPeriod;
    }

    public Queue()
    {
        clients = new LinkedBlockingQueue<Client>();
        waitingPeriod = new AtomicInteger(0);
    }

    @Override
    public void run() {
        while (true) {
            if (clients.size() > 0) {
                Client servingClient = clients.poll();
                int servingTime = servingClient.getProcessingTime();
                try {
                    Thread.sleep(servingTime * 1000);//sleep servingTime seconds
                }
                catch (Exception e)
                {
                    System.out.println(e.getMessage());
                }
                waitingPeriod.addAndGet(-servingTime);
            }
        }
    }

    //return waiting time of client
    public int addClient(Client c)
    {
        clients.add(c);
        return waitingPeriod.addAndGet(c.getProcessingTime());
    }

    public int getNumberOfClients()
    {
        return clients.size();
    }
}
