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
//        double res = ((float)((my*x2)-(mx*y2))/((my*x)+(mx*y)));
        return res;
    }

    public static int[][][] colld_data = {
//            {{18,53},{23,140}},
//            {{18,53},{23,140},{431,106},{424,21}},
//            {{482,45},{483,90},{508,90},{508,47}},
            {{556,112},{575,198},{662,181},{643,93}},
//            {{663,111},{675,191},{861,163},{848,80}},
//            {{1174,37},{1193,119},{1359,82},{1339,0}},
//            {{1370,15},{1390,98},{1556,60},{1542,0}},
//            {{39,320},{32,382},{185,396},{192,334}},
//            {{157,402},{156,426},{198,428},{200,406}},
//            {{643,342},{629,425},{810,463},{827,379}},
//            {{865,367},{850,447},{950,462},{963,382}},
//            {{1015,394},{998,485},{1230,527},{1247,431}},
//            {{1280,400},{1259,532},{1422,557},{1442,425}},
//            {{1457,282},{1475,299},{1457,339},{1497,359},{1507,335},{1490,325},{1493,308},{1505,294},{1474,264}},
//            {{1562,510},{1551,552},{1575,559},{1586,516}},
//            {{1880,148},{1844,173},{1861,196},{1855,200},{1873,224},{1879,222},{1897,245},{1930,218}},
//            {{1640,265},{1728,403},{1791,362},{1704,225}},
//            {{1717,193},{1803,329},{1865,291},{1782,152}},
//            {{1623,404},{1580,655},{1749,686},{1763,605},{1674,588},{1689,497},{1779,515},{1793,435}},
//            {{1861,674},{1894,768},{1969,739},{1935,648}},
    };

    public static boolean dev = false;
}
