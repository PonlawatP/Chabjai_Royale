package net.msu.bronline.network;

import net.msu.bronline.comps.Player;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Iterator;

import static net.msu.bronline.guis.Game.getGame;

public class Sv_read extends Thread implements Runnable{
    DataInputStream dis;
    ClientHandler ch;
    Sv_write cw;
    public Sv_read(DataInputStream dis, ClientHandler ch, Sv_write cw){
        this.dis = dis;
        this.ch = ch;
        this.cw = cw;
    }

    @Override
    public void run() {
        while (!ch.quit){
            try {
                for(String mess : dis.readUTF().split("::ln::")) {
                    String[] data = mess.split(":");
                    if (data[1].equalsIgnoreCase("player")) {
                        ch.getPlayer().updateFromPacket(data);
                    } else {
                        ClientHandler.broadcastMessage(mess);
                    }
                    if (data[1].equalsIgnoreCase("shoot")){
                        Iterator<Player> ps = Player.getPlayers().iterator();
                        while (ps.hasNext()) {
                            Player p = ps.next();
                            if(data[0].equalsIgnoreCase(p.getUsername())){
                                int x = Integer.parseInt(data[2]), y = Integer.parseInt(data[3]), dx = Integer.parseInt(data[4]), dy = Integer.parseInt(data[5]);
                                p.shoot(x,y,dx,dy, false);
                                break;
                            }
                        }
                    }


                }
                sleep(2);
            } catch (EOFException | SocketException ex){
//                ex.printStackTrace();
                ch.closeEverything();
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
