import java.util.Arrays;
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
                Client servingClient = clients.peek();
                while (servingClient.getProcessingTime() > 0) {
                    try {
                        Thread.sleep(1000);//sleep servingTime seconds
                        waitingPeriod.addAndGet(-1);
                        servingClient.decrementTimeService();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
                clients.poll();
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

    public Client[] getClients()
    {
        Client[] clientsCopy = new Client[getNumberOfClients()];
        Object[] obj = clients.toArray();
        for (int i = 0; i < obj.length; i++)
        {
            clientsCopy[i] = (Client)obj[i];
        }

        return clientsCopy;
    }
}
