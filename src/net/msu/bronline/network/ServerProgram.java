package net.msu.bronline.network;
import net.msu.bronline.comps.Player;
import net.msu.bronline.comps.Scene;
import net.msu.bronline.guis.Game;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class ServerProgram implements Runnable{
    private ServerSocket sev;
    public ServerProgram(ServerSocket sev) {
        this.sev = sev;
    }
    Scene scene;
    Game game;
    public ServerProgram(Scene scene, Game game) {
        this.scene = scene;
        this.game = game;
    }
    public void startSev() {
        System.out.println("Server starting...");
        try {
            while (!sev.isClosed()){
                Socket soc = sev.accept();
//                System.out.println("Client has connected!");
                DataInputStream dis = new DataInputStream(soc.getInputStream());
                if (dis.available() != 0){
                    if(dis.readUTF().equalsIgnoreCase("ping")){
//                        System.out.println("pinging: client => server...");
                        DataOutputStream dos = new DataOutputStream(soc.getOutputStream());
//                        conf:name:username:amount:max:status
                        dos.writeUTF("conf:"+game.getRoomName()+":"+game.getPlayerOwn().getUsername()+":"+ Player.getPlayers().size()+":"+game.getMaxPlayer()+":"+game.getGame_status());
                        dos.flush();
//                        soc.close();
                        continue;
                    }

                    System.out.println("run ClientHandler");
                    ClientHandler clientHandler = new ClientHandler(soc, scene, game);
                    Thread t = new Thread(clientHandler);
                    t.start();
                }
            }
        } catch (SocketException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Server stop...");
    }

    public void closeSev() {
        try {
            if (sev != null) {
                sev.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            sev = new ServerSocket(50394);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        startSev();
    }
}