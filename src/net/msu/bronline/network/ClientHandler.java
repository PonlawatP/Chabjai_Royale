package net.msu.bronline.network;
import net.msu.bronline.comps.Armor;
import net.msu.bronline.comps.Player;
import net.msu.bronline.comps.Scene;
import net.msu.bronline.guis.Game;

import  java.io.BufferedReader;
import java.io.*;
import java.net.Socket;
import java.util.*;

import static net.msu.bronline.guis.Game.getGame;

public class ClientHandler implements Runnable{
    public static ArrayList<ClientHandler> clientHandler = new ArrayList<>();
    private Socket soc;
    private String clientUser;
    Game game;
    Player cPlayer;
    Sv_write cw;

    Thread thrd_read;
    Thread thrd_write;

    public String getClientUser() {
        return clientUser;
    }

    public Socket getSocket() {
        return soc;
    }

    DataInputStream dis;
    DataOutputStream dos;
    public ClientHandler(Socket soc, Scene scene, Game game) {
        try {
            this.game = game;
            this.soc = soc;

            dis = new DataInputStream(soc.getInputStream());
            dos = new DataOutputStream(soc.getOutputStream());

            String mdata = dis.readUTF();
            String[] data = mdata.split(":");

            if(!mdata.contains(":") || data[0] == "") {
                closeSocOnly();
                return;
            }

            this.clientUser = data[0];
            clientHandler.add(this);
            System.out.println("SERVER : "+clientUser+" enter the game");

            Player p = new Player(scene, clientUser, Integer.parseInt(data[2]));
            cPlayer = p;
            cPlayer.c_r_t.add(Integer.parseInt(data[2]));
            cPlayer.randomCharID();
            Player.getPlayers().add(p);

            cw = new Sv_write(dos, this);
            thrd_write = new Thread(cw);
            thrd_write.start();
            thrd_read = new Thread(new Sv_read(dis, this, cw));
            thrd_read.start();

            broadcastMessage(clientUser+":join:"+p.getCharacterID()); //TODO: ทำแบบ broadcast
            loadPlayersIngame();
            loadArmorsIngame();
        } catch (Exception e) {
//            closeEverything(soc, bufReader, bufWriter);
        }
    }

    public void loadPlayersIngame() {
        Iterator<Player> pls = new ArrayList<>(Player.getPlayers()).iterator();

        while (pls.hasNext()){
            Player p = pls.next();
            if(p.getUsername() == getClientUser()) continue;
            cw.sendMessage(p.getUsername() + ":" + p.getPacket("load"));
        }
    }
    public void loadArmorsIngame() {
        Iterator<Armor> ams = new ArrayList<>(Armor.getArmors()).iterator();

        while (ams.hasNext()){
            Armor a = ams.next();
            cw.sendMessage("host:arm_load:"+a.getRawX()+":"+a.getRawY()+":"+a.getType());
        }
    }

    public static void broadcastMessage(String mess){
        Iterator<ClientHandler> chs = new ArrayList<>(clientHandler).iterator();
        while (chs.hasNext()){
            ClientHandler ch = chs.next();
            if(ch.cw == null) continue;

            ch.cw.sendMessage(mess);
        }
    }

    public Player getPlayer(){
        return cPlayer;
    }

    public Game getGame() {
        return game;
    }

    @Override
    public void run() {

    }

    public void closeEverything() {
        removeClientHandler();
        try {
            if(dis != null) dis.close();
            if(dos != null) dos.close();
            if(soc != null) soc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void closeSocOnly() {
        try {
            if(dis != null) dis.close();
            if(dos != null) dos.close();
            if(soc != null) soc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean quit = false;
    private void removeClientHandler() {
        System.out.println("SERVER : "+clientUser+" quit");
        broadcastMessage(clientUser+":quit"); //TODO: ทำแบบ broadcast
        quit = true;
        Player.removePlayer(clientUser);
        clientHandler.remove(this);
    }

}


