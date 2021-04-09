import javax.swing.*;


public class NewSimulator extends JFrame {
    private static final long serialVersionUID = 1L;


    public static void main(String[] args) {
        // set look and feel to the system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
         
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new NewSimulator().setVisible(true);
            }
        });
    }

    public NewSimulator() {
        super("FTC Algo Simulator");


        TelemetryComponent telemetry = new TelemetryComponent();

        add(telemetry);

        pack();
        setLocationRelativeTo(null);
    }
}
