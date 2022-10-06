package net.msu.bronline.network;
import net.msu.bronline.comps.Player;
import net.msu.bronline.comps.Scene;
import net.msu.bronline.guis.Game;

import  java.io.BufferedReader;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class ClientHandler implements Runnable{
    public static ArrayList<ClientHandler> clientHandler = new ArrayList<>();
    private Socket soc;
//    private BufferedReader bufReader;
//    private BufferedWriter bufWriter;
    private String clientUser;
    Game game;


    private DataInputStream in       =  null;
    private DataOutputStream out     = null;
    public ClientHandler(Socket soc, Scene scene, Game game) {
        try {
            this.soc = soc;
//            this.bufWriter = new BufferedWriter(new OutputStreamWriter(soc.getOutputStream()));
//            this.bufReader = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            in = new DataInputStream(new BufferedInputStream(soc.getInputStream()));
            out = new DataOutputStream(soc.getOutputStream());
            String[] data = in.readUTF().split(":");
            this.clientUser = data[0];
            this.game = game;
            clientHandler.add(this);
            broadcastMessage("SERVER : "+clientUser+" enter the game");

            Player p = new Player(scene, clientUser, Integer.parseInt(data[2]));
            Player.getPlayers().add(p);
        } catch (Exception e) {
            closeEverything(soc, in, out);
        }
    }

    @Override
    public void run() {
        String mess;
        while (soc.isConnected()) {
//            try {
//                mess = bufReader.readLine();
////                broadcastMessage(mess);
//
////                System.out.println(mess);
//                String[] data = mess.split(":");
//                if(data[1].equalsIgnoreCase("player")){
//                    Iterator<Player> pls = Player.getPlayers().iterator();
//                    while (pls.hasNext()){
//                        Player p = pls.next();
//                        p.updateFromPacket(data);
//                    }
////                    broadcastMessage(mess);
//                }
////                broadcastMessage(game.getPlayerOwn().getUsername() + ":" + game.getPlayerOwn().getPacket());
//            } catch (Exception e) {
//                closeEverything(soc, bufReader,bufWriter);
//                break;
//            }

            try
            {
                in = new DataInputStream(new BufferedInputStream(soc.getInputStream()));
                out = new DataOutputStream(soc.getOutputStream());

                String line = "";

                while (true)
                {
//                    ObjectOutputStream oos = new ObjectOutputStream(soc.getOutputStream());
//                    out = new DataOutputStream(soc.getOutputStream());
                    try
                    {
//                        out.writeUTF("test");
                        line = in.readUTF();
//                      System.out.println(line);
                        String[] data = line.split(":");
                        if(data[1].equalsIgnoreCase("player")) {
                            Iterator<Player> pls = Player.getPlayers().iterator();
                            while (pls.hasNext()) {
                                Player p = pls.next();
                                p.updateFromPacket(data);

//                                oos.writeUTF(p.getUsername() +":"+p.getPacket());
                            }
                        }
                    }
                    catch(IOException i)
                    {
                        System.out.println("inline: " + i);
                        break;
                    }
                    out.flush();
//                    oos.close();
                }
                System.out.println("Closing connection");

                in.close();
            }
            catch(IOException i)
            {
                closeEverything(soc, in,out);
            }
        }

    }

    public void broadcastMessage(String mess) {
//        System.out.println(mess);
//        for (ClientHandler clientHandler : clientHandler) {
//            try {
//                if (!clientHandler.clientUser.equals(clientUser)) {
//                    clientHandler.bufWriter.write(mess);
//                    clientHandler.bufWriter.newLine();
//                    clientHandler.bufWriter.flush();
//                }
//            } catch (Exception e) {
//                closeEverything(soc, bufReader,bufWriter);
//            }
//        }

    }


    private void closeEverything(Socket soc2, DataInputStream bufReader2, DataOutputStream bufWriter2) {
        removeClientHandler();
        try {
            if (bufReader2 != null) {
                bufReader2.close();
            }
            if (bufWriter2 != null) {
                bufWriter2.close();
            }
            if (soc2 != null) {
                soc.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void removeClientHandler() {
        clientHandler.remove(this);
        broadcastMessage("SERVER : "+ clientUser + "Left!");
    }

}


