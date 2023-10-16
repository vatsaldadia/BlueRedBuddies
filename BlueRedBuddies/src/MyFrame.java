import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class MyFrame extends JFrame implements KeyListener {

    MyCanvas canvas = new MyCanvas();
    JLabel label = new JLabel();
    MyAudioPlayer audioPlayer = new MyAudioPlayer();

    CSVReader reader = new CSVReader();
    Integer[][] matrix = reader.export();

    Character red = new Character("red");
    Character blue = new Character("blue");

    ArrayList<Shuriken> shurikens = new ArrayList<>();

    Blade blade = new Blade();

    Set<Integer> pressedKeys = new HashSet<>();

    Timer timer;
    Timer timer1;
    Timer timer2;

    int counter = 60;

    Object[] options = { "QUIT", "RESTART" };

    public MyFrame() {

        setTitle("BlueRedBuddies");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 630);
        setResizable(false);
        setLocationRelativeTo(null);
        addKeyListener(this);

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                switch (matrix[i][j]) {
                    case 1:
                        Block b1 = new Block(j * 20, i * 20, Color.BLACK);
                        canvas.objects.add(b1);
                        break;
                    case 2:
                        Block b2 = new Block(j * 20, i * 20, Color.RED);
                        canvas.objects.add(b2);
                        break;
                    case 3:
                        Block b3 = new Block(j * 20, i * 20, Color.BLUE);
                        canvas.objects.add(b3);
                        break;
                    case 4:
                        Block b4 = new Block(j * 20, i * 20, Color.MAGENTA);
                        canvas.objects.add(b4);
                        break;
                    case 5:
                        Block b5 = new Block(j * 20, i * 20, Color.YELLOW);
                        canvas.objects.add(b5);
                        break;
                    default:
                        break;
                }
            }
        }

        canvas.objects.add(red);
        canvas.objects.add(blue);
        canvas.objects.add(blade);
        canvas.setProps(red, blue);

        timer1 = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Shuriken s = new Shuriken();
                shurikens.add(s);
                canvas.objects.add(s);
            }
        });
        timer1.setInitialDelay(0);

        label.setText("" + counter);
        timer2 = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                label.setText("" + --counter);
                if (counter == 0)
                    death();
            }
        });

        timer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (red.checkDeath())
                    death();
                if (blue.checkDeath())
                    death();
                handleCollision();
                win();
                canvas.repaint();
            }
        });
        timer.start();

        label.setForeground(Color.WHITE);
        label.setFont(new Font("Courier", Font.BOLD, 70));
        label.setBounds(40, 10, 150, 150);
        canvas.add(label);

        canvas.setLayout(null);

        add(canvas);
    }

    public void handleCollision() {
        for (VisibleObjects object : canvas.objects) {
            if (object instanceof Character) {
                Character character = (Character) object;
                int x1 = character.location.x;
                int y1 = character.location.y;
                int x2 = blade.location.x;
                int y2 = blade.location.y;
                if (x2 > x1) {
                    if (x2 - x1 <= 40) {
                        if (y1 > y2 && y1 - y2 <= 16) {
                            death();
                        }
                    }
                } else {
                    if (x1 - x2 <= 52) {
                        if (y1 > y2 && y1 - y2 <= 16) {
                            death();
                        }
                    }
                }
                for (Shuriken shuriken : shurikens) {
                    int x3 = shuriken.location.x + 6;
                    int y3 = shuriken.location.y + 6;
                    if (y3 - y1 >= 0 && y3 - y1 < 40) {
                        if (x3 - x1 >= 0 && x3 - x1 < 40) {
                            death();
                        }
                    }
                }
            }
        }
        for (int i = 0; i < shurikens.size(); i++) {
            if (shurikens.get(i).location.y >= 630) {
                canvas.objects.remove(shurikens.get(i));
                shurikens.remove(shurikens.get(i));
            }
        }
    }

    public void death() {
        audioPlayer.play("death", false);
        timer2.stop();
        timer1.stop();
        timer.stop();
        canvas.repaint();
        int x = JOptionPane.showOptionDialog(this, "Game Over", "Oh No!",
                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options,
                options[0]);
        if (x == 0 || x == -1) {
            System.exit(0);
        } else if (x == 1) {
            main(null);
        }
    }

    public void win() {
        if (red.location.x == 700 && red.location.y == 80) {
            if (blue.location.x == 740 && blue.location.y == 80) {
                audioPlayer.play("win", false);
                timer2.stop();
                timer1.stop();
                timer.stop();
                canvas.repaint();
                int x = JOptionPane.showOptionDialog(this, "You Won", "Congratulations!", JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                if (x == 0 || x == -1) {
                    System.exit(0);
                } else if (x == 1) {
                    main(null);
                }
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!timer2.isRunning()) {
            timer1.start();
            timer2.start();
            blade.start();
        }
        int key = e.getKeyCode();
        pressedKeys.add(key);
        if (key == KeyEvent.VK_UP) {
            int l = 0;
            if (pressedKeys.contains(KeyEvent.VK_RIGHT)) {
                l = 1;
            } else if (pressedKeys.contains(KeyEvent.VK_LEFT)) {
                l = -1;
            }
            red.jump(l);

        }
        if (key == KeyEvent.VK_W) {
            int l = 0;
            if (pressedKeys.contains(KeyEvent.VK_D)) {
                l = 1;
            } else if (pressedKeys.contains(KeyEvent.VK_A)) {
                l = -1;
            }
            blue.jump(l);
        }
        if (!pressedKeys.isEmpty()) {
            for (Iterator<Integer> it = pressedKeys.iterator(); it.hasNext();) {
                switch (it.next()) {
                    case KeyEvent.VK_RIGHT:
                        red.moveRight();
                        break;
                    case KeyEvent.VK_LEFT:
                        red.moveLeft();
                        break;
                    case KeyEvent.VK_D:
                        blue.moveRight();
                        break;
                    case KeyEvent.VK_A:
                        blue.moveLeft();
                        break;
                }
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
        if (!pressedKeys.contains(KeyEvent.VK_UP) && !pressedKeys.contains(KeyEvent.VK_RIGHT)
                && !pressedKeys.contains(KeyEvent.VK_LEFT)) {
            red.action = 0;
        }
        if (!pressedKeys.contains(KeyEvent.VK_W) && !pressedKeys.contains(KeyEvent.VK_D)
                && !pressedKeys.contains(KeyEvent.VK_A)) {
            blue.action = 0;
        }
    }
    
    public static void main(String[] args) {
        MyFrame frame = new MyFrame();
        frame.setVisible(true);
    }
}
