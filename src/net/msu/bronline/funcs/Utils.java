package net.msu.bronline.funcs;

public class Utils {
    public static int getInsidePosition(int p, int dp, double percent){
        return (int)(p + ((dp-p)*(percent/100)));
    }
}
