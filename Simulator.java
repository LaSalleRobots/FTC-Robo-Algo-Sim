import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Simulator extends JComponent implements KeyListener, ChangeListener {
    private static final long serialVersionUID = 1L;

    private static int width = 500;
    private static int height = 500 + 23;

    private static int bodySize = 30;
    private int roboAngle = 0;

    private static JSlider angleSlider = null;

    static private String lastPressed = "";

    public static void main(String[] args) {
        JFrame window = new JFrame("FTC Algo Simulator");
        Simulator simulator = new Simulator();

        simulator.addKeyListener(simulator);

        window.setSize(width, height);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        window.add(simulator);
        angleSlider = simulator.getSlider();
        window.add(angleSlider, "Last");
        window.setVisible(true);


        while (true) {
            try {
                Thread.sleep(1000 / 60);
                // Thread.sleep(500);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            simulator.repaint();
        }
    }

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        int x = (height / 2) - bodySize;
        int y = (width / 2) - bodySize;

        g2d.rotate(Math.toRadians(roboAngle), x + bodySize / 2, y + bodySize / 2);
        // body
        g2d.setColor(Color.GREEN);
        g2d.drawRect(x, y, bodySize, bodySize);
        g2d.fillRect(x, y, bodySize, bodySize);
        // heading
        g2d.setColor(Color.RED);
        g2d.drawLine(x + (bodySize / 2), y + (bodySize / 2), x + (bodySize / 2), y - (bodySize / 2));
        g2d.dispose();

        g.drawString("Angle : " + roboAngle, 0, 60);
        g.drawString("LastKey : " + lastPressed, 0, 80);
    }

    private JSlider getSlider() {
        JSlider slider = new JSlider(-180, 180, 0);
        slider.addChangeListener(this);
        return slider;
    }

    public void addNotify() {
        super.addNotify();
        requestFocus();
    }

    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
    }

    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub
        int code = e.getKeyCode();
        lastPressed = KeyEvent.getKeyText(code);

        if (code == KeyEvent.VK_DOWN) {
            roboAngle -= 10;
        } else if (code == KeyEvent.VK_UP) {
            roboAngle += 10;
        }
        angleSlider.setValue(roboAngle);
    }

    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
    }

    public void stateChanged(ChangeEvent e) {
        // TODO Auto-generated method stub
        int value = ((JSlider) e.getSource()).getValue();
        roboAngle = value;
    }
}