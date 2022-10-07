package net.msu.bronline.network;
import net.msu.bronline.guis.Game;
import net.msu.bronline.guis.Present;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class CientProgram extends Thread implements Runnable{
    Socket soc;

    DataInputStream dis;
    DataOutputStream dos;

    Present ps;
    Game game;
    private String Username;

    Thread thrd_write;
    Thread thrd_read;
    Cli_write cw;
    String ip;
    public CientProgram(String Username, String ip, Present ps, Game game) {
        try {
            this.Username = Username;
            this.ps = ps;
            this.game = game;
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

    public void exitGame(){
        quit = true;
        game.setGame_status(0);
        ps.setGame_status(2);
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
