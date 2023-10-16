public class JumpThread extends Thread {

    Character player;
    int speed;
    int lateral;
    MyAudioPlayer audioPlayer = new MyAudioPlayer();

    public JumpThread(Character c, int l) {
        player = c;
        speed = 20;
        lateral = l;
        audioPlayer.play("jump", false);
    }

    @Override
    public void run() {
        int initial = player.location.y;
        while (player.location.y != initial - 60) {
            player.checkBounds();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException("Thread interrupted..." + e);
            }
            if (lateral == 0) {
                if (!player.move_up) {
                    break;
                }
            } else if (lateral == 1) {
                if (!player.move_up_right) {
                    lateral = 0;
                    break;
                }
            } else {
                if (!player.move_up_left) {
                    lateral = 0;
                    break;
                }
            }
            player.location.y -= speed;
            player.i -= 1;
            player.location.x += lateral * 20;
            player.j += lateral;
        }
        FallThread new_fall = new FallThread(player, lateral, true);
        new_fall.start();
        synchronized (player) {
            try {
                player.wait();
            } catch (InterruptedException e) {

            }
        }
        player.allow = true;
    }
}
