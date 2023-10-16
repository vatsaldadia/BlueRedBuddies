import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;

public class Blade extends Thread implements VisibleObjects {
    Point location = new Point();

    public Blade() {
        location.x = 300;
        location.y = 140;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            while (location.y < 200) {
                location.y += 1;
                try {
                    Thread.sleep(8);
                } catch (InterruptedException e) {
                    throw new RuntimeException("Thread interrupted..." + e);
                }
            }
            while (location.y > 140) {
                location.y -= 1;
                try {
                    Thread.sleep(15);
                } catch (Exception e) {

                }
            }

        }
    }

    @Override
    public void display(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);
        Stroke oldStroke = g2d.getStroke();
        Stroke stroke = new BasicStroke(4f);
        g2d.setStroke(stroke);
        g2d.drawRect(location.x, location.y, 60, 4);
        int x[] = { location.x + 6, location.x + 28, location.x + 54 };
        int y[] = { location.y + 8, location.y + 20, location.y + 8 };
        g2d.drawPolygon(x, y, 3);
        g2d.setColor(Color.LIGHT_GRAY.brighter());
        g2d.fillRect(location.x, location.y, 60, 4);
        g2d.fillPolygon(x, y, 3);
        g2d.setStroke(oldStroke);
    }
}
