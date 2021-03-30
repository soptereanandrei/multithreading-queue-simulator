import java.util.*;

public class QueuesSimulator implements Runnable {

    public int timeLimit;
    public int N;
    public int Q;
    public int minArrivalTime;
    public int maxArrivalTime;
    public int minProcessingTime;
    public int maxProcessingTime;

    private Scheduler scheduler;
    private ArrayList<Client> clients = new ArrayList<Client>();

    public QueuesSimulator()
    {
        scheduler = new Scheduler(Q);
        generateNRandomClients();
    }

    private void generateNRandomClients()
    {
        //generate N random tasks:
        // - random processing time
        //minProcessingTime < processingTime < maxProcessingTime
        // - random arrivalTime
        //minArrivalTime < arrivalTime < maxArrivalTime
        //sort list with respect to arrivalTime

        Random rand = new Random();
        for (int i = 1; i <= N; i++)
        {
            int arrivalTime = minArrivalTime + rand.nextInt(maxArrivalTime - minArrivalTime + 1);
            int processingTime = minProcessingTime + rand.nextInt(maxProcessingTime - minProcessingTime + 1);

            Client c = new Client(i, arrivalTime, processingTime);
            clients.add(c);
        }

        Collections.sort(clients, new Comparator<Client>() {
            @Override
            public int compare(Client c1, Client c2) {
                return c1.getArrivalTime() - c2.getArrivalTime();
            }
        });
    }

    @Override
    public void run() {
            int currentTime = 0;
            while(currentTime < timeLimit)
            {
                while (clients.size() > 0 && clients.get(0).getArrivalTime() <= currentTime)
                {
                    scheduler.dispatchClient(clients.get(0), currentTime);
                    clients.remove(0);
                }
                currentTime++;
                try {
                    Thread.sleep(1000);//sleep 1 second
                }
                catch (Exception e)
                {
                    System.out.println(e.getMessage());
                }
            }
    }

    public static void main(String[] args)
    {
        QueuesSimulator sim = new QueuesSimulator();
        Thread mainThread = new Thread(sim);
        mainThread.start();
    }
}
