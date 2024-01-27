import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;

class SoundManager {
    private static SoundManager instance;
    private static ArrayList<AudioClip> audioClips; //array contains audios


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

}
