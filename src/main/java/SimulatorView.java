import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public class SimulatorView extends JFrame {

    private JPanel content;
    public JTextField timeLimit = new JTextField();
    public JTextField nrClients = new JTextField();
    public JTextField nrServers = new JTextField();
    public JTextField minArrival = new JTextField();
    public JTextField maxArrival = new JTextField();
    public JTextField minProcessing = new JTextField();
    public JTextField maxProcessing = new JTextField();
    public JButton startButton = new JButton("Start simulation");
    private JTextField simulationTime = new JTextField();
    private JTextField averageWaitingTime = new JTextField();
    private JTextField averageServiceTime = new JTextField();
    private JTextField peakHour = new JTextField();
    private JTextArea simulationTextArea = new JTextArea(20, 40);

    public SimulatorView()
    {
        setSize(800, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        createInputPanel();

        JLabel label1 = new JLabel("Simulation information:");
        label1.setPreferredSize(new Dimension(800, 100));
        label1.setAlignmentX(3);
        content.add(label1);

        createSimulationInfoPanel();

        JLabel label2 = new JLabel("Simulation:");
        label2.setPreferredSize(new Dimension(800, 20));
        label2.setAlignmentX(3);
        content.add(label2);

        Font oldFont = simulationTime.getFont();
        simulationTextArea.setFont(new Font(oldFont.getName(), oldFont.getStyle(), oldFont.getSize() + 10));
        JScrollPane scrollPane = new JScrollPane(simulationTextArea);
        content.add(scrollPane);

        setContentPane(content);
        setVisible(true);
    }

    public void printSimulationStage(int time, String stage)
    {
        simulationTime.setText(new String(time + ""));
        averageWaitingTime.setText("");
        averageServiceTime.setText("");
        peakHour.setText("");
        simulationTextArea.setText(stage);
    }

    public void printSimulationResults(float averageWaitingTime, float averageServiceTime, int peakHour)
    {
        DecimalFormat decimalFormat = new DecimalFormat(".00");
        this.averageWaitingTime.setText(decimalFormat.format(averageWaitingTime));
        this.averageServiceTime.setText(decimalFormat.format(averageServiceTime));
        this.peakHour.setText(new String(peakHour + ""));
    }

    private void createInputPanel()
    {
        JPanel inputPanel = new JPanel();
        inputPanel.setPreferredSize(new Dimension(800, 100));
        inputPanel.setMaximumSize(new Dimension(800, 100));
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

        Dimension dimension = new Dimension(200, 20);

        inputPanel.add(createRow(
                new String[] { "Time Limit: " },
                new JComponent[] { timeLimit, (JComponent) Box.createRigidArea(new Dimension(0, 20)), startButton },
                dimension
        ));

        inputPanel.add(createRow(
                new String[] { "Numbers of clients: ", "Number of servers: " },
                new JTextField[] { nrClients, nrServers },
                dimension
        ));

        inputPanel.add(createRow(
                new String[] { "Minimum arrival time: ", "Maximum arrival time: " },
                new JTextField[] { minArrival, maxArrival },
                dimension
        ));

        inputPanel.add(createRow(
                new String[] {"Minimum processing time: ", "Maximum processing time: " },
                new JTextField[] {minProcessing, maxProcessing },
                dimension
        ));

        content.add(inputPanel);
    }

    private JPanel createRow(String[] msgs, JComponent[] comp, Dimension dimension)
    {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));

        for (int i = 0; i < comp.length; i++)
        {
            if (i < msgs.length) {
                JLabel jLabel = createJLabel(msgs[i], dimension);
                row.add(jLabel);
            }

            comp[i].setPreferredSize(dimension);
            comp[i].setMaximumSize(dimension);
            row.add(comp[i]);

            row.add(Box.createRigidArea(new Dimension(10, dimension.height)));
        }

        return row;
    }

    private JLabel createJLabel(String msg, Dimension dimension)
    {
        JLabel jLabel = new JLabel(msg);

        jLabel.setPreferredSize(dimension);
        jLabel.setMaximumSize(dimension);

        return jLabel;
    }

    private void createSimulationInfoPanel()
    {
        JPanel simulationInfo = new JPanel();
        simulationInfo.setPreferredSize(new Dimension(800, 50));
        simulationInfo.setMaximumSize(new Dimension(800, 50));
        simulationInfo.setLayout(new BoxLayout(simulationInfo, BoxLayout.Y_AXIS));

        simulationTime.setEditable(false);
        averageWaitingTime.setEditable(false);
        averageServiceTime.setEditable(false);
        peakHour.setEditable(false);

        simulationInfo.add(createRow(
                new String[] {"Simulation time: ", "Peak hour: "},
                new JComponent[] {simulationTime, peakHour},
                new Dimension(200, 20)
        ));

        simulationInfo.add(createRow(
                new String[] { "Average waiting time: ", "Average service time: ", },
                new JComponent[] { averageWaitingTime, averageServiceTime},
                new Dimension(200, 20)
        ));

        content.add(simulationInfo);
    }
}
