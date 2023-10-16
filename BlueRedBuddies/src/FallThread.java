public class FallThread extends Thread {

    Character player;
    int speed;
    int lateral;
    boolean jump;

    public FallThread(Character c, int l, boolean j) {
        player = c;
        speed = 20;
        lateral = l;
        jump = j;
    }

    @Override
    public void run() {
        while (true) {
            player.checkBounds();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException("Thread interrupted..." + e);
            }
            if (lateral == -1) {
                if (!player.move_down_left) {
                    lateral = 0;
                }
            } else if (lateral == 1) {
                if (!player.move_down_right) {
                    lateral = 0;
                }
            }
            if (!player.move_down) {
                break;
            }
            player.location.y += speed;
            player.i += 1;
            player.location.x += lateral * 20;
            player.j += lateral;
        }
        if (jump) {
            synchronized (player) {
                player.notify();
            }
        } else {
            player.allow = true;
        }
    }
}
