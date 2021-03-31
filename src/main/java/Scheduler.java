import java.util.ArrayList;

public class Scheduler{
    private ArrayList<Queue> queues;
    private ArrayList<Thread> threads;
    private int numberOfServers;

    private int totalWaitingTime;
    private int totalServiceTime;
    private int maxClientsAtTime;
    private int peakTime;

    public ArrayList<Queue> getQueues() { return queues; }

    public int getTotalWaitingTime() {
        return totalWaitingTime;
    }

    public int getTotalServiceTime() {
        return totalServiceTime;
    }

    public int getPeakTime() {
        return peakTime;
    }

    public Scheduler(int numberOfServers) {
        this.numberOfServers = numberOfServers;
        queues = new ArrayList<Queue>(numberOfServers);
        threads = new ArrayList<Thread>(numberOfServers);

        for (int i = 0; i < numberOfServers; i++)
        {
            Queue q = new Queue();
            queues.add(q);
            Thread t = new Thread(q);
            threads.add(t);
            t.start();
        }

        totalWaitingTime = 0;
        totalServiceTime = 0;
        maxClientsAtTime = 0;
    }

    public void dispatchClient(Client c, int currentTime)
    {
        int minimumTime = Integer.MAX_VALUE;
        int queueIndex = -1;
        int currentClients = 0;

        for (int i = 0; i < numberOfServers; i++)
        {
            int waitingTime = queues.get(i).getWaitingPeriod().get();
            if (waitingTime < minimumTime) {
                minimumTime = waitingTime;
                queueIndex = i;
            }
            currentClients += queues.get(i).getNumberOfClients();
        }

        totalWaitingTime += queues.get(queueIndex).addClient(c);
        totalServiceTime += c.getProcessingTime();
        if (currentClients > maxClientsAtTime)
        {
            peakTime = currentTime;
            maxClientsAtTime = currentClients;
        }
    }

    public void shutdown()
    {
        for (Thread t : threads)
        {
            try
            {
                t.interrupt();
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }
        }
    }
}
