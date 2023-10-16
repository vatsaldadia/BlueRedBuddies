import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.util.Random;

public class Shuriken extends Thread implements VisibleObjects {

    Point location = new Point();
    MyAudioPlayer audioPlayer = new MyAudioPlayer();
    Random random = new Random(System.currentTimeMillis());
    double theta = 0;

    public Shuriken() {
        location.y = 0;
        location.x = random.nextInt(720) + 40;
        theta = 0;
        audioPlayer.play("shuriken", false);
        start();
    }

    @Override
    public void run() {
        while (location.y < 650) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException("Thread interrupted..." + e);
            }
            location.y += 3;
        }
        interrupt();
    }

    @Override
    public void display(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);
        Stroke oldStroke = g2d.getStroke();
        Stroke stroke = new BasicStroke(4f);
        g2d.setStroke(stroke);
        g2d.rotate(theta, location.x + 3, location.y + 3);
        int x1[] = { location.x, location.x + 3, location.x + 6 };
        int y1[] = { location.y, location.y - 6, location.y };
        int x2[] = { location.x + 6, location.x + 12, location.x + 6 };
        int y2[] = { location.y, location.y + 3, location.y + 6 };
        int x3[] = { location.x + 6, location.x + 3, location.x };
        int y3[] = { location.y + 6, location.y + 12, location.y + 6 };
        int x4[] = { location.x, location.x - 6, location.x };
        int y4[] = { location.y + 6, location.y + 3, location.y };
        g2d.drawPolygon(x1, y1, 3);
        g2d.drawPolygon(x2, y2, 3);
        g2d.drawPolygon(x3, y3, 3);
        g2d.drawPolygon(x4, y4, 3);
        g2d.drawOval(location.x, location.y, 6, 6);
        g2d.setColor(Color.LIGHT_GRAY.brighter());
        g2d.fillPolygon(x1, y1, 3);
        g2d.fillPolygon(x2, y2, 3);
        g2d.fillPolygon(x3, y3, 3);
        g2d.fillPolygon(x4, y4, 3);
        g2d.rotate(-theta, location.x + 3, location.y + 3);
        theta = (theta + Math.PI / 36) % 360;
        g2d.setStroke(oldStroke);
    }
}
