package net.msu.bronline.funcs;

import javax.sound.sampled.*;
import java.io.*;
public class SoundClip {
    boolean started = false;
    InputStream streamInput;
    float vol;
    boolean loop;
    AudioInputStream stream;
    AudioFormat format;
    DataLine.Info info;
    Clip clip;

    FloatControl gainControl;
    public SoundClip(InputStream streamInput, float vol, boolean loop){
        this.streamInput = streamInput;
        this.vol = vol;
        this.loop = loop;
        try {
            stream = AudioSystem.getAudioInputStream(new BufferedInputStream(this.streamInput));
            format = stream.getFormat();
            info = new DataLine.Info(Clip.class, format);
            clip = (Clip) AudioSystem.getLine(info);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public void play(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    clip.open(stream);

                    gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    gainControl.setValue(vol);
                    if(loop) clip.loop(99);
//                    clip.setFramePosition(5030080);
                    clip.start();
                    started = true;

                    while (clip.isOpen()){
                        if(!started || (!loop && clip.getFramePosition() == clip.getFrameLength())) {
//                            clip.setFramePosition(0);
                            clip.close();
                            break;
                        }
//                        System.out.println(clip.getFramePosition() + " : " + clip.getFrameLength());

                        Thread.sleep(2000);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void stop(){
        started = false;
        if(clip.isOpen()) clip.close();
    }

    public boolean isStarted() {
        return started;
    }
}