package net.msu.bronline.funcs;

import javax.sound.sampled.*;
import java.io.*;
public class SoundClip {
    public SoundClip(InputStream stream){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    AudioInputStream ais;
                    AudioFormat format;
                    DataLine.Info info;
                    Clip clip;
//                    File initFile = new File(System.getProperty("user.dir")+File.separator+"src"+File.separator+"imgs"+File.separator+"snds"+File.separator+"st.wav");

                    ais = AudioSystem.getAudioInputStream(stream);
                    format = ais.getFormat();
                    info = new DataLine.Info(Clip.class, format);
                    clip = (Clip) AudioSystem.getLine(info);
                    clip.open(ais);
                    clip.start();
                } catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }).start();
    }
}