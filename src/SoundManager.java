import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.util.ArrayList;

class SoundManager {
    private static SoundManager instance;
    private static ArrayList<AudioClip> audioClips; //array contains audios
    private Thread backgroundMusicThread;
    private AudioInputStream audioInputStream;
    private Clip backgroundMusic;

    public SoundManager(){
        audioClips=new ArrayList<>();
    }

    public static SoundManager getInstance(){
        if(instance==null){
            instance = new SoundManager();
        }
        return instance;
    }

    public void addMedia(String filename){
        audioClips.add(new AudioClip(filename));
    }

    public void playSound(int index , double volume){
        audioClips.get(index).setVolume(volume);
        audioClips.get(index).play();
    }


    //play sound using Clip && AudioInputStream
    public void playSoundEffect(String filename) {
        try {
            // Open an audio input stream
            audioInputStream = AudioSystem.getAudioInputStream(new File("./src/Media/sfx/" + filename + ".wav"));
            // Get a clip resource
            Clip clip = AudioSystem.getClip();
            // Open audio clip and load samples from the audio input stream
            clip.open(audioInputStream);

            // Play the sound in a separate thread
            new Thread(() -> {
                clip.start();
                try {
                    // Sleep for a while to allow the sound to play
                    Thread.sleep((long) (clip.getMicrosecondLength()*1e-3)); // Adjust the duration as needed
                } catch (InterruptedException e) {
                    System.out.println("Error with playing sound in new Thread: " + e.getMessage());
                } finally {
                    // Stop the sound
                    clip.stop();
                }
            }).start();
        } catch (Exception e) {
            System.out.println("Error with playing sound: " + e.getMessage());
        }
    }



}
