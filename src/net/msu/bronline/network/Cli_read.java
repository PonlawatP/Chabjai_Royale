package net.msu.bronline.network;

import net.msu.bronline.comps.Player;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Iterator;

import static net.msu.bronline.guis.Game.getGame;

public class Cli_read extends Thread implements Runnable{
    DataInputStream dis;
    CientProgram cp;
    Cli_write cw;
    Socket soc;
    public Cli_read(DataInputStream dis, CientProgram cp, Cli_write cw, Socket soc){
        this.dis = dis;
        this.cp = cp;
        this.cw = cw;
        this.soc = soc;
    }

    @Override
    public void run() {
        while (!cp.quit && !soc.isClosed()){
            try {
                int cnt = 0;
                for(String mess : dis.readUTF().split("::ln::")) {
                    String[] data = mess.split(":");
                    if(!data[1].equalsIgnoreCase("player")) System.out.println("[r] " + mess);

                    if (data[1].equalsIgnoreCase("player")) {

                        Iterator<Player> ps = Player.getPlayers().iterator();
                        while (ps.hasNext()) {
                            Player p = ps.next();
                            p.updateFromPacket(data);
                        }
                    } else if (data[1].equalsIgnoreCase("load")){
                        Player p = new Player(getGame().getScene(), data[0], Integer.parseInt(data[2]));
                        p.updateFromPacket(data);
                        Player.getPlayers().add(cnt, p);
                        cnt++;
                    } else if (data[1].equalsIgnoreCase("join")){
                            if(data[0].equals(cp.getUsername())) continue;
                            Player p = new Player(getGame().getScene(), data[0], Integer.parseInt(data[2]));
                            p.updateFromPacket(data);
                            Player.getPlayers().add(Player.getPlayers().size(), p);
                    } else if (data[1].equalsIgnoreCase("quit")) {
                        Player.removePlayer(data[0]);
                    } else if (data[1].equalsIgnoreCase("shutdown")) {
                        if(!data[0].equals("host")) continue;
                        cp.closeEverything();
                        cp.exitGame();
                    }
                }

                sleep(2);
            } catch (EOFException ex){
                ex.printStackTrace();
            }
            catch (SocketException es){
//                es.printStackTrace();
                cp.closeEverything();
                cp.exitGame();
            }
            catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
