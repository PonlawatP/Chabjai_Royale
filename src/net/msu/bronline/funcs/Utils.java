package net.msu.bronline.funcs;

import net.msu.bronline.network.NetworkDevices;

import java.awt.*;

import static java.lang.Thread.sleep;
import static net.msu.bronline.guis.Present.getPresent;

public class Utils {
    public static int getInsidePosition(int p, int dp, double percent){
        return (int)(p + ((dp-p)*(percent/100)));
    }
    public static Color getColor(int id){
        switch (id){
            case 1:
                return new Color(0x1C69FF);
            case 2:
                return Color.MAGENTA;
            case 3:
                return Color.RED;
            case 4:
                return Color.YELLOW;
            case 5:
                return Color.cyan;
            case 6:
                return Color.GREEN;
            case 7:
                return Color.BLUE;
            case 8:
                return new Color(0xC6FF2A);
            default:
                return Color.WHITE;
        }
    }


    public static void runServerFinder(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (getPresent().getGame_status() == 2){
                    NetworkDevices.getNetworkDevices();
                    try {
                        sleep(500);
                        NetworkDevices.updateHost();
                        sleep(500);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        }).start();
    }

    public static boolean dev = false;
}
