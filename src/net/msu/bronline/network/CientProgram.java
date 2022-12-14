package net.msu.bronline.network;

import net.msu.bronline.funcs.SoundClip;

import java.io.*;
import java.net.Socket;

import static net.msu.bronline.funcs.Utils.runServerFinder;
import static net.msu.bronline.guis.Game.getGame;
import static net.msu.bronline.guis.Present.getPresent;

public class CientProgram extends Thread implements Runnable{
    Socket soc;

    DataInputStream dis;
    DataOutputStream dos;
    private String Username;

    Thread thrd_write;
    Thread thrd_read;
    Cli_write cw;
    String ip;
    public CientProgram(String Username, String ip) {
        try {
            this.Username = Username;
            this.ip = ip;

            soc = new Socket(ip,50394);

            dos = new DataOutputStream(soc.getOutputStream());
            dis = new DataInputStream(soc.getInputStream());

            cw = new Cli_write(dos, this, soc);
            thrd_write = new Thread(cw);
            thrd_write.start();
            thrd_read = new Thread(new Cli_read(dis, this, cw, soc));
            thrd_read.start();
        } catch (Exception e) {
            closeEverything();
            exitGame();
            e.printStackTrace();
        }
    }

    public Cli_write getCwrite() {
        return cw;
    }

    public void exitGame() {
            quit = true;
            getPresent().setGame_status(2);
            getGame().resetGame();
            if(!getPresent().s_main.isStarted()) {
                getPresent().s_main = new SoundClip(getClass().getClassLoader().getResourceAsStream("sounds/intro.wav"), -10.0f, true);
                getPresent().s_main.play();
            }
            runServerFinder();
    }

    public String getUsername() {
        return Username;
    }

    public void closeEverything() {
//        System.out.println("close");
        try {
            if (dis != null) {
                dis.close();
            }
            if (dos != null) {
                dos.close();
            }
            if (soc != null) {
                soc.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        quit = true;
    }
    boolean quit = false;
    @Override
    public void run() {
    }
}
