package net.msu.bronline.network;

import net.msu.bronline.comps.Player;

import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.Stack;

import static net.msu.bronline.guis.Game.getGame;

public class Sv_write extends Thread implements Runnable{
    DataOutputStream dos;
    ClientHandler ch;
//    Stack<String> st = new Stack<>();
    String st = "";
    public Sv_write(DataOutputStream dos, ClientHandler ch){
        this.dos = dos;
        this.ch = ch;
    }

    public void sendMessage(String mess) {
        if(!st.equalsIgnoreCase("")){
            st = st + "::ln::" + mess;
        } else {
            st = mess;
        }
    }
    public void sendMessageToServer(){
        if (st.equalsIgnoreCase("")) return;

        try {
            while (!st.equalsIgnoreCase("")) {
                for (String Mdata : st.split("::ln::")) {
                    String[] data = Mdata.split(":");
                    if (!data[1].equalsIgnoreCase("player")) System.out.println("[s] " + Mdata);
//                System.out.println("[s] " + Mdata);
                }

                try {
                    dos.writeUTF(st);
                    dos.flush();
                    st = "";
                } catch (EOFException ex) {
                    ex.printStackTrace();
                } catch (SocketException es) {
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (EmptyStackException ex){

        }
    }
    @Override
    public void run() {
        while (!ch.quit){
            try {
                if(getGame().getGame_status() != 0){
                    Iterator<Player> pls = new ArrayList<>(Player.getPlayers()).iterator();

                    while (pls.hasNext()){
                        Player p = pls.next();
                        if(p.getUsername() == ch.getClientUser()) continue;
                        sendMessage(p.getUsername() + ":" + p.getPacket());
                    }
                }

                sendMessageToServer();
                sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
