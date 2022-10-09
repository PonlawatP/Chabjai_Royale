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
//                        sleep(500);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        }).start();
    }

    public static double getMathM(double[] xy1, double[] xy2){
        return (xy2[1] - xy1[1])/(xy2[0] - xy1[0]);
    }

    public static double getMathM(int[] xy1, int[] xy2){
        return ((double) xy2[1] - xy1[1])/((double)xy2[0] - xy1[0]);
    }

    public static double geMathLinEq(int[] xy1, int[] xy2, int[] xy3){
        /*
            y1-y2 = m(x1-x2)
            my = (xy2[1] - xy1[1])
            mx = (xx2[0] - xx1[0])

            y1-y2 = my * (x1-x2)
                    mx

            (mx*y1)-(mx*y2) = (my*x1)+(my*x2)

            ((my*-1)*x1)+(mx*y1)-((my*x2)-(mx*y2))
         */
        int x1 = xy1[0], x2 = xy2[0];
        int y1 = xy1[1], y2 = xy2[1];
        int my = (xy2[1] - xy1[1]);
        int mx = (xy2[0] - xy1[0]);
        int x = xy3[0], y = xy3[1];

        double res = (my*x)-((my*x2)-(mx*y2))-(mx*y);
        return res;
    }

    public static boolean dev = false;
}
