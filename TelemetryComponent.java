import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class TelemetryComponent extends JPanel {

    /**
     *
     */
    private static final long serialVersionUID = 127842539361397798L;
    private static List<String> telemetry;

    private JLabel title;
    private JTextArea textField;
    private JSplitPane split;

    public int history = 2;

    public void addTelemetry(String id, Object content) {
        telemetry.add( id + ": " + content.toString() + "\n");
        if (telemetry.size() > 2) {
            telemetry.remove(0);
        }
    }

    public TelemetryComponent() {
        telemetry = new ArrayList<>();

        title = new JLabel("Telemetry");

        textField = new JTextArea();
        textField.setEditable(false);
        
        split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        split.setBounds(0, 0, 600, 100);
        split.setResizeWeight(0.3);
        split.setEnabled(false);
        split.setDividerSize(0);

        split.add(title);
        split.add(textField);
        add(split);
    }

    public void update() {
        String body = "";
        for (String el: telemetry) {
            body += el;
        }
        textField.setText(body);
    }

    @Override
    public Dimension preferredSize() {
        return new Dimension(600,100);
    }

}
