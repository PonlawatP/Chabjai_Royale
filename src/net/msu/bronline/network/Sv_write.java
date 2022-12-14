package net.msu.bronline.network;

import net.msu.bronline.comps.Player;
import net.msu.bronline.funcs.Utils;

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
    Stack<String> st = new Stack<>();
    public Sv_write(DataOutputStream dos, ClientHandler ch){
        this.dos = dos;
        this.ch = ch;
    }

    public void sendMessage(String mess) {
        try {
            if (!st.empty())
                st.add(0, st.pop()+"::ln::"+mess);
            else
                st.add(0, mess);
        } catch (EmptyStackException ee){
            st.add(0, mess);
        }
    }
    public void sendMessageToServer() {
        if (st.empty()) return;

        while (!st.empty()) {
            try {
                String mess = st.pop();
                for (String Mdata : mess.split("::ln::")) {
                    String[] data = Mdata.split(":");
                    if (!data[1].equalsIgnoreCase("player") && Utils.dev) System.out.println("<- " + Mdata);
//                System.out.println("[s] " + Mdata);
                }

                try {
                    dos.writeUTF(mess);
                    dos.flush();
                } catch (EOFException ex) {
                    ex.printStackTrace();
                } catch (SocketException es) {
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } catch (EmptyStackException ex) {

            }
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
                        if(p.isMove()) sendMessage(p.getUsername() + ":" + p.getPacket());
                    }
                }

                sendMessageToServer();
                sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
