import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;

public class SimulatorController {

    private SimulatorView view;
    private QueuesSimulator simulator = null;
    private Thread simulatorThread = null;
    FileWriter fileWriter = null;

    public SimulatorController(SimulatorView view)
    {
        this.view = view;
        view.startButton.addActionListener(new StartButtonListener());
    }

    public class StartButtonListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if (simulator != null)
            {
                simulator.shutdown();
                simulatorThread.stop();
                try {
                    fileWriter.close();
                }
                catch (Exception except)
                {
                    System.out.println(except.getMessage());
                }
            }

            try {
                fileWriter = new FileWriter("log.txt");
            }
            catch (Exception except)
            {
                System.out.println(except.getMessage());
            }

            simulator = new QueuesSimulator(
                    view,
                    Integer.parseInt(view.timeLimit.getText()),
                    Integer.parseInt(view.nrClients.getText()),
                    Integer.parseInt(view.nrServers.getText()),
                    Integer.parseInt(view.minArrival.getText()),
                    Integer.parseInt(view.maxArrival.getText()),
                    Integer.parseInt(view.minProcessing.getText()),
                    Integer.parseInt(view.maxProcessing.getText()),
                    fileWriter
            );
            simulatorThread = new Thread(simulator);
            simulatorThread.start();
        }
    }

    public static void main(String[] args)
    {
        SimulatorView view = new SimulatorView();
        SimulatorController controller = new SimulatorController(view);
    }

}
