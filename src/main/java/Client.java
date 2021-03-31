public class Client {
    private int ID;
    private int timeArrival;
    private int timeService;

    public Client(int ID, int timeArrival, int timeService)
    {
        this.ID = ID;
        this.timeArrival = timeArrival;
        this.timeService = timeService;
    }

    public int getArrivalTime() {
        return timeArrival;
    }

    public int getProcessingTime() {
        return timeService;
    }

    public String toString()
    {
        return new String("(" + ID + "," + timeArrival + "," + timeService + ")");
    }

    public void decrementTimeService()
    {
        timeService--;
    }
}
