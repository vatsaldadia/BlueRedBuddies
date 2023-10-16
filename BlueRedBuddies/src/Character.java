import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Character implements VisibleObjects {

    Point location = new Point();
    String type;
    int x_speed = 20;
    boolean move_left, move_right, move_up, move_down, move_up_right, move_up_left, move_down_right, move_down_left;
    boolean allow = true;
    BufferedImage image0 = null;
    BufferedImage image1 = null;
    BufferedImage[][] animation = new BufferedImage[2][4];
    int animation_index0 = 0;
    int animation_index1 = 0;
    int animation_ticker = 0;
    int animation_speed = 30;
    int direction = 0;
    int action = 0;
    boolean animation_over = false;

    CSVReader reader = new CSVReader();
    Integer[][] matrix = reader.export();
    int i, j;

    

    public Character(String t) {
        type = t;
        if (type == "red") {
            location.x = 20;
            location.y = 540;
            j = 2;
            i = 28;
            // location.x = 200;
            // location.y = 300;
            // j = 11;
            // i = 16;
            // location.x = 380;
            // location.y = 200;
            // j = 20;
            // i = 11;
            try {
                image0 = ImageIO.read(new File("src/resources/images/red.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            location.x = 20;
            location.y = 480;
            j = 2;
            i = 25;
            // location.x = 200;
            // location.y = 300;
            // j = 11;
            // i = 16;
            // location.x = 380;
            // location.y = 200;
            // j = 20;
            // i = 11;
            try {
                image0 = ImageIO.read(new File("src/resources/images/blue.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (int k = 0; k < 4; k++) {
            animation[0][k] = image0.getSubimage(k * 80, 0, 80, 80);
            animation[1][k] = image0.getSubimage(k * 80, 80, 80, 80);
        }
        for (int k = 0; k < matrix.length; k++) {
            for (int l = 0; l < matrix[k].length; l++) {
                if (matrix[k][l] == 2) {
                    if (type == "red") {
                        matrix[k][l] = 1;
                    } else {
                        matrix[k][l] = 4;
                    }
                }
                if (matrix[k][l] == 3) {
                    if (type == "blue") {
                        matrix[k][l] = 1;
                    } else {
                        matrix[k][l] = 4;
                    }
                }
            }
        }
    }

    public void checkBounds() {
        move_right = true;
        move_left = true;
        move_up = true;
        move_down = true;
        move_up_right = true;
        move_up_left = true;
        move_down_right = true;
        move_down_left = true;
        if (matrix[i][j - 2] == 1 || matrix[i - 1][j - 2] == 1) {
            move_left = false;
        }
        if (matrix[i][j + 1] == 1 || matrix[i - 1][j + 1] == 1) {
            move_right = false;
        }
        if (matrix[i - 2][j - 1] == 1 || matrix[i - 2][j] == 1) {
            move_up = false;
        }
        if (matrix[i + 1][j - 1] == 1 || matrix[i + 1][j] == 1
                || (matrix[i + 1][j] == 5 && matrix[i + 1][j - 1] == 5)) {
            move_down = false;
        }
        if (matrix[i - 2][j + 1] == 1 || matrix[i - 2][j] == 1 || matrix[i - 1][j + 1] == 1) {
            move_up_right = false;
        }
        if (matrix[i - 2][j - 2] == 1 || matrix[i - 2][j - 1] == 1 || matrix[i - 1][j - 2] == 1) {
            move_up_left = false;
        }
        if (matrix[i + 1][j + 1] == 1 || matrix[i][j + 1] == 1 || matrix[i + 1][j] == 1) {
            move_down_right = false;
        }
        if (matrix[i + 1][j - 2] == 1 || matrix[i][j - 2] == 1 || matrix[i + 1][j - 1] == 1) {
            move_down_left = false;
        }
    }

    public void moveRight() {
        if (allow) {
            checkBounds();
            if (move_right) {
                location.x += x_speed;
                j += 1;
                direction = 0;
                action = 1;
                if (matrix[i + 1][j] == 0) {
                    allow = false;
                    FallThread new_fall = new FallThread(this, 1, false);
                    new_fall.start();
                }
            }
        }
    }

    public void moveLeft() {
        if (allow) {
            checkBounds();
            if (move_left) {
                location.x -= x_speed;
                j -= 1;
                direction = 1;
                action = 1;
                if (matrix[i + 1][j - 1] == 0) {
                    allow = false;
                    FallThread new_fall = new FallThread(this, -1, false);
                    new_fall.start();
                }
            }
        }
    }

    public void jump(int l) {
        if (matrix[i + 1][j] == 1 || matrix[i + 1][j - 1] == 1
                || (matrix[i + 1][j] == 5 && matrix[i + 1][j - 1] == 5)) {
            int lateral = l;
            allow = false;
            JumpThread new_jump = new JumpThread(this, lateral);
            new_jump.start();
            
        }
    }

    public boolean checkDeath() {
        if (matrix[i + 1][j] == 4 || matrix[i + 1][j - 1] == 4)
            return true;
        return false;
    }

    public boolean onStation() {
        if (matrix[i + 1][j] == 5 && matrix[i + 1][j - 1] == 5) {
            return true;
        }
        return false;
    }

    public void update(boolean b) {
        if (b) {
            matrix[16][36] = 1;
            matrix[16][37] = 1;
            matrix[16][38] = 1;
        } else {
            matrix[16][36] = 0;
            matrix[16][37] = 0;
            matrix[16][38] = 0;
            if (i == 15 && j >= 36) {
                FallThread new_fall = new FallThread(this, 0, false);
                new_fall.start();
            }
        }
    }

    public void updateAnimation() {
        animation_index0 = direction;
        if (action == 0) {
            animation_index1 = 0;
            animation_ticker = 0;
        } else {
            if (animation_ticker % 50 == 0) {
                animation_index1 = (animation_index1 + 1) % 4;
                animation_ticker = 0;
            }
            animation_ticker++;
        }
    }

    @Override
    public void display(Graphics g) {
        updateAnimation();
        // g.drawRect(location.x, location.y, 40, 40);
        int x_, y_;
        if (animation_index0 == 0) {
            x_ = location.x - 16;
        } else {
            x_ = location.x - 26;
        }
        y_ = location.y - 15;
        g.drawImage(animation[animation_index0][animation_index1], x_, y_,
                null);
    }
}