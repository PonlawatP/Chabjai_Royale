package net.msu.bronline.network;
import net.msu.bronline.comps.Player;
import net.msu.bronline.comps.Scene;
import net.msu.bronline.guis.Game;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import static net.msu.bronline.guis.Game.getGame;
import static net.msu.bronline.guis.Present.getPresent;

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
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (sev != null && !sev.isClosed()){
                        try {
                            NetworkDevices.sendMulticastMessage(NetworkDevices.MULTICAST_IP, NetworkDevices.MULTICAST_INTERFACE, NetworkDevices.MULTICAST_PORT,
                                    getGame().getPlayerOwn().getUsername()+":conf:"+getGame().getRoomName()+":"+ Player.getPlayers().size()+":"+getGame().getMaxPlayer()+":"+getPresent().getGame_status()+":"+getGame().getGame_status()+":"+getGame().getIP()
                            );

                            Thread.sleep(1000);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }).start();

            while (sev != null && !sev.isClosed()){
                Socket soc = sev.accept();
//                System.out.println("Client has connected!");
                DataInputStream dis = new DataInputStream(soc.getInputStream());
                if (dis.available() != 0){
                    String data = dis.readUTF();
                    if(data.equalsIgnoreCase("ping")){
//                        System.out.println("pinging: client => server...");
                        DataOutputStream dos = new DataOutputStream(soc.getOutputStream());
//                        username:conf:name:amount:max:pr_status:game_status
                        dos.writeUTF(getGame().getPlayerOwn().getUsername()+":conf:"
                                +getGame().getRoomName()+":"+ Player.getPlayers().size()+
                                ":"+getGame().getMaxPlayer()+
                                ":"+getPresent().getGame_status()+
                                ":"+getGame().getGame_status());
                        dos.flush();
//                        soc.close();
                        continue;
                    }
                }

                ClientHandler clientHandler = new ClientHandler(soc, scene, game);
                Thread t = new Thread(clientHandler);
                t.start();
            }
        } catch (SocketException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Server stop...");
    }

    public void closeSev() {
        ClientHandler.broadcastMessage("host:shutdown");
        getGame().setGame_status(0);
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
        } catch (BindException e) {
            getGame().stopMode();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        startSev();
    }
}