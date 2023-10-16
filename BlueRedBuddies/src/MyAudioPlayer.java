import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class MyAudioPlayer {
    
    Clip audioClip;

    public MyAudioPlayer() {
        try {
            audioClip = AudioSystem.getClip();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play(String fileName, boolean loop) {
        try {
            AudioInputStream s = AudioSystem.getAudioInputStream(new File("src/resources/sounds/" + fileName + ".wav"));
            audioClip.open(s);
            FloatControl gainControl = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);
            if (fileName == "bg") {
                gainControl.setValue(-25.0f);
            }
            if (fileName == "shuriken" || fileName == "station") {
                gainControl.setValue(-10.0f);
            }
            if (loop)
                audioClip.loop(Clip.LOOP_CONTINUOUSLY);
            else
                audioClip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop1() {
        audioClip.stop();
        audioClip.flush();
    }
}
