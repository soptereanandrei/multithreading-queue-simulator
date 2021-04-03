import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class QueuesSimulator implements Runnable {

    private SimulatorView view;
    private int timeLimit = 30;
    private int N = 4;
    private int Q = 2;
    private int minArrivalTime = 2;
    private int maxArrivalTime = 6;
    private int minProcessingTime = 2;
    private int maxProcessingTime = 4;

    private Scheduler scheduler;
    private ArrayList<Client> clients = new ArrayList<Client>();

    private FileWriter fileWriter;

    public QueuesSimulator()
    {
        scheduler = new Scheduler(Q);
        generateNRandomClients();
    }

    public QueuesSimulator(SimulatorView view, int timeLimit, int N, int Q, int minArrivalTime, int maxArrivalTime, int minProcessingTime, int maxProcessingTime, FileWriter fileWriter)
    {
        this.view = view;
        this.timeLimit = timeLimit;
        this.N = N;
        this.Q = Q;
        this.minArrivalTime = minArrivalTime;
        this.maxArrivalTime = maxArrivalTime;
        this.minProcessingTime = minProcessingTime;
        this.maxProcessingTime = maxProcessingTime;
        this.fileWriter = fileWriter;

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
            while(currentTime < timeLimit && (clients.size() > 0 || scheduler.serversStillWorking()))
            {
                while (clients.size() > 0 && clients.get(0).getArrivalTime() <= currentTime)
                {
                    scheduler.dispatchClient(clients.get(0), currentTime);
                    clients.remove(0);
                }

                view.printSimulationStage(currentTime, getViewSnapshot());
                writeLog(getSnapshot(currentTime));

                try {
                    Thread.sleep(1000);//sleep 1 second
                }
                catch (Exception e)
                {
                    System.out.println(e.getMessage());
                }
                currentTime++;
            }
            scheduler.shutdown();

            float averageWaitingTime = (float)scheduler.getTotalWaitingTime() / N;
            float averageServiceTime = (float)scheduler.getTotalServiceTime() / N;
            int peakHour = scheduler.getPeakTime();

            view.printSimulationResults(averageWaitingTime, averageServiceTime, peakHour);
            writeLog(createResults(averageWaitingTime, averageServiceTime, peakHour));
    }

    private String getViewSnapshot()
    {
        String result = new String();

        result += "Waiting clients : ";
        for (int i = 0; i < clients.size(); i++)
            result += "* ";
        result += "\n\n";

        int i = 1;
        for (Queue q : scheduler.getQueues())
        {
            result += "Queue " + i + ": ";
            i++;

            for (int j = 0; j < q.getNumberOfClients(); j++)
                result += "* ";

            result += "\n\n";
        }

        return result;
    }

    private void writeLog(String log)
    {
        try {
            fileWriter.write(log);
            fileWriter.flush();
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    private String getSnapshot(int currentTime)
    {
        String result = new String();
        result += "Time: " + currentTime + "\n";
        result += "Waiting clients: ";
        for (Client c : clients)
        {
            result += c + "; ";
        }
        result += "\n";
        int i = 1;
        for (Queue q : scheduler.getQueues())
        {
            result += "Queue " + i + ": ";
            boolean closed = true;
            for (Client c : q.getClients())
            {
                result += c + "; ";
                closed = false;
            }
            if (closed)
                result += "closed";
            result += "\n";
            i++;
        }
        result += "\n";

        return result;
    }

    private String createResults(float averageWaitingTime, float averageServiceTime, int peakHour)
    {
        String result = new String();
        DecimalFormat decimalFormat = new DecimalFormat(".00");
        result += "Average waiting time: " + decimalFormat.format(averageServiceTime) + "\n";
        result += "Average serving time: " + decimalFormat.format(averageServiceTime) + "\n";
        result += "Peak hour: " + peakHour;

        return result;
    }

    public void shutdown()
    {
        scheduler.shutdown();
    }
}
