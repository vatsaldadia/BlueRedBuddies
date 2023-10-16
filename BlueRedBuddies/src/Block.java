import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Block implements VisibleObjects {

    Point location = new Point();
    Color colour;

    public Block(int x, int y, Color col) {
        location.x = x;
        location.y = y;
        colour = col;
    }

    @Override
    public void display(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawRect(location.x, location.y, 20, 20);
        g.setColor(colour);
        g.fillRect(location.x, location.y, 20, 20);
    }
}