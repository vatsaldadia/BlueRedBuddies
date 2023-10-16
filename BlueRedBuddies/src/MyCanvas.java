import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class MyCanvas extends JPanel {
    List<VisibleObjects> objects = new ArrayList<VisibleObjects>();
    Image background = null;
    CSVReader reader = new CSVReader();
    Integer[][] matrix = reader.export();
    Character player1;
    Character player2;
    int station = 0;

    public MyCanvas() {
        try {
            background = ImageIO.read(new File("src/resources/images/bg.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        MyAudioPlayer audioPlayer = new MyAudioPlayer();
        audioPlayer.play("bg", true);
    }

    public void setProps(Character c1, Character c2) {
        player1 = c1;
        player2 = c2;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), null);
        g.setColor(Color.RED);
        g.fillOval(710, 90, 20, 20);
        g.setColor(Color.BLUE);
        g.fillOval(750, 90, 20, 20);
        boolean b = player1.onStation() || player2.onStation();
        player1.update(b);
        player2.update(b);
        if (b) {
            if (station == 0) {
                MyAudioPlayer audioPlayer1 = new MyAudioPlayer();
                audioPlayer1.play("station", false);
                station = 1;
            }
            g.setColor(Color.BLACK);
            g.drawRect(720, 320, 60, 10);
            g.setColor(Color.YELLOW);
            g.fillRect(720, 320, 60, 10);
        } else {
            if (station == 1) {
                MyAudioPlayer audioPlayer2 = new MyAudioPlayer();
                audioPlayer2.play("station", false);
                station = 0;
            }
        }
        for (VisibleObjects o : objects) {
            o.display(g);
        }
    }
}
