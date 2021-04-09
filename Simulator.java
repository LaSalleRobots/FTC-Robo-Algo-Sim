import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Simulator extends JComponent implements KeyListener, ChangeListener {
    private static final long serialVersionUID = 1L;

    private static int width = 500;
    private static int height = 500 + 23;

    private static int bodySize = 30;
    private static int roboAngle = 0;

    private static JSlider angleSlider = null;
    private static JEditorPane textPane = null;

    static private String lastPressed = "";

    static double flP = 0;
    static double frP = 0;
    static double blP = 0;
    static double brP = 0;

    static Gamepad gamepad = new Gamepad();

    public static void main(String[] args) {
        JFrame window = new JFrame("FTC Algo Simulator");
        Simulator simulator = new Simulator();

        simulator.addKeyListener(simulator);

        window.setSize(width, height);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        window.add(simulator);
        angleSlider = simulator.getSlider();
        textPane = new JEditorPane();

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        split.add(angleSlider);
        split.add(textPane);

        window.add(split, "Last");
        window.setVisible(true);

        while (true) {
            try {
                Thread.sleep(1000 / 60);
                // Thread.sleep(500);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            algorithm(gamepad, Math.toRadians(roboAngle));
            simulator.repaint();
        }
    }

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();

       

        // Display robot body + heading

        int x = (height / 2) - bodySize;
        int y = (width / 2) - bodySize;

        g2d.rotate(Math.toRadians(roboAngle), x + bodySize / 2, y + bodySize / 2);
        drawWheels(g2d);
        // body
        g2d.setColor(Color.GREEN);
        g2d.drawRect(x, y, bodySize, bodySize);
        g2d.fillRect(x, y, bodySize, bodySize);
        // heading
        g2d.setColor(Color.RED);
        g2d.drawLine(x + (bodySize / 2), y + (bodySize / 2), x + (bodySize / 2), y - (bodySize / 2));
        g2d.dispose();

        // Display Gamepad

        int gamepadViewSize = 30;
        g.setColor(Color.BLACK);
        g.drawOval(width - gamepadViewSize, 0, gamepadViewSize, gamepadViewSize);
        g.drawOval(width - gamepadViewSize - gamepadViewSize, 0, gamepadViewSize, gamepadViewSize);

        int gamepadLeftPointX = (int) (width - gamepadViewSize * 1.5
                + (int) (gamepad.left_stick_x * (gamepadViewSize / 2)));
        int gamepadLeftPointY = gamepadViewSize / 2 + (int) (gamepad.left_stick_y * (gamepadViewSize / 2) * -1);
        g.setColor(Color.RED);
        g.fillOval(gamepadLeftPointX - 5, gamepadLeftPointY - 5, 10, 10);

        g.setColor(Color.GREEN);
        int gamepadRightPointX = (width - (gamepadViewSize / 2))
                + (int) (gamepad.right_stick_x * (gamepadViewSize / 2));
        int gamepadRightPointY = gamepadViewSize / 2;
        g.fillOval(gamepadRightPointX - 5, gamepadRightPointY - 5, 10, 10);

        g.setColor(Color.BLACK);

        // Display Telemetry

        g.drawString("Angle : " + roboAngle, 5, 60);
        g.drawString("LastKey : " + lastPressed, 5, 70);

        g.drawString("lGX : " + gamepad.left_stick_x, 5, 90);
        g.drawString("lGY : " + gamepad.left_stick_y, 5, 100);
        g.drawString("rGX : " + gamepad.right_stick_x, 5, 110);

        g.drawString("flP:" + flP, 5, height - 100);
        g.drawString("frP:" + frP, 5, height - 90);
        g.drawString("blP:" + blP, 5, height - 80);
        g.drawString("brP:" + brP, 5, height - 70);
    }

    private void drawWheels(Graphics2D g2d) {
        Stroke line = new BasicStroke(1);
        Stroke arrow = new BasicStroke(1);
        Point2D s, e;

        // Front Left Wheel
        s = new Point2D.Double(width/2-bodySize-5, height/2-bodySize+5);
        e = new Point2D.Double(width/2-bodySize-5, height/2-bodySize-15);
        if (flP > 0) {
            //backwards
            Arrow.draw(g2d, e, s, line, arrow, 7);
        } else {
            //forwards
            Arrow.draw(g2d, s, e, line, arrow, 7);
        }
        

        // Front Right Wheel
        s = new Point2D.Double(width/2+bodySize-5, height/2-bodySize+5);
        e = new Point2D.Double(width/2+bodySize-5, height/2-bodySize-15);
        if (frP > 0) {
            Arrow.draw(g2d, e, s, line, arrow, 7);
        } else {
            Arrow.draw(g2d, s, e, line, arrow, 7);
        }
       

        // Back Left Wheel
        s = new Point2D.Double(width/2-bodySize-5, height/2-bodySize+25);
        e = new Point2D.Double(width/2-bodySize-5, height/2-bodySize/2-10);
        if (blP > 0) {
            Arrow.draw(g2d, e, s, line, arrow, 7);
        } else {
            Arrow.draw(g2d, s, e, line, arrow, 7);
        }

        // Back Right Wheel
        s = new Point2D.Double(width/2+bodySize-5, height/2-bodySize+25);
        e = new Point2D.Double(width/2+bodySize-5, height/2-bodySize/2-10);
        if (brP > 0) {
            Arrow.draw(g2d, e, s, line, arrow, 7);
        } else {
            Arrow.draw(g2d, s, e, line, arrow, 7);
        }
        
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
        int code = e.getKeyCode();
        lastPressed = KeyEvent.getKeyText(code);

        if (code == KeyEvent.VK_W) {
            gamepad.left_stick_y = 1.0;
        }
        if (code == KeyEvent.VK_S) {
            gamepad.left_stick_y = -1.0;
        }
        if (code == KeyEvent.VK_A) {
            gamepad.left_stick_x = -1.0;
        }
        if (code == KeyEvent.VK_D) {
            gamepad.left_stick_x = 1.0;
        }
        if (code == KeyEvent.VK_LEFT) {
            gamepad.right_stick_x = -1.0;
        }
        if (code == KeyEvent.VK_RIGHT) {
            gamepad.right_stick_x = 1.0;
        }

        if (code == KeyEvent.VK_UP) {
            roboAngle -= 10;
        }
        if (code == KeyEvent.VK_DOWN) {
            roboAngle += 10;
        }

        angleSlider.setValue(roboAngle);
    }

    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        switch (code) {
        case KeyEvent.VK_W:
            gamepad.left_stick_y = 0.0;
        case KeyEvent.VK_S:
            gamepad.left_stick_y = 0.0;
        case KeyEvent.VK_A:
            gamepad.left_stick_x = 0.0;
        case KeyEvent.VK_D:
            gamepad.left_stick_x = 0.0;
        case KeyEvent.VK_LEFT:
            gamepad.right_stick_x = 0.0;
        case KeyEvent.VK_RIGHT:
            gamepad.right_stick_x = 0.0;
        }

    }

    public void stateChanged(ChangeEvent e) {
        int value = ((JSlider) e.getSource()).getValue();
        roboAngle = value;
    }

    private static class Gamepad {
        double left_stick_x;
        double left_stick_y;
        double right_stick_x;
    }

    private static double getGamepadMoveAngle(Gamepad gamepad) {
        return Math.atan2(gamepad.left_stick_y, gamepad.left_stick_x);
    }

    private static double getGamepadMoveMagnitude(Gamepad gamepad) {
        return -Math.hypot(gamepad.left_stick_x, gamepad.left_stick_y);
    }

    private static double getGamepadTurnMagnitude(Gamepad gamepad) {
        return gamepad.right_stick_x;
    }

    public static void algorithm(Gamepad gamepad1, double robotHeading) {

        flP = getGamepadMoveMagnitude(gamepad1) * Math.sin((getGamepadMoveAngle(gamepad1)-robotHeading) + (Math.PI / 4))
                + getGamepadTurnMagnitude(gamepad1);
        blP = getGamepadMoveMagnitude(gamepad1) * Math.sin((getGamepadMoveAngle(gamepad1)-robotHeading) - (Math.PI / 4))
                + getGamepadTurnMagnitude(gamepad1);
        frP = getGamepadMoveMagnitude(gamepad1) * Math.sin((getGamepadMoveAngle(gamepad1)-robotHeading) + (Math.PI / 4))
                - getGamepadTurnMagnitude(gamepad1);
        brP = getGamepadMoveMagnitude(gamepad1) * Math.sin((getGamepadMoveAngle(gamepad1)-robotHeading) - (Math.PI / 4))
                - getGamepadTurnMagnitude(gamepad1);
    }
}