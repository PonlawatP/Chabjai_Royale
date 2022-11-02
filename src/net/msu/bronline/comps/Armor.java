package net.msu.bronline.comps;

import net.msu.bronline.guis.Game;
import net.msu.bronline.network.ClientHandler;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import static net.msu.bronline.funcs.Utils.colld_data;
import static net.msu.bronline.funcs.Utils.geMathLinEq;

public class Armor {
    static ArrayList<Armor> armorArrayList = new ArrayList<>();
    private double x,y;
    BufferedImage cimg;
    int type;
    public boolean coll() {
        int yx = 34, yy = 34;
        for (int[][] ii : colld_data) {
            boolean coll = true;
            for (int a = 0; a < ii.length; a++) {
                int[] i = ii[a];
                int[] mm = {(int) x, (int) y};
                if (a > 0) {
                    int x1 = ii[a - 1][0];
                    int y1 = ii[a - 1][1];
                    int[] xy1 = {x1 - yx, y1 - yy};
                    int x2 = i[0];
                    int y2 = i[1];
                    int[] xy2 = {x2 - yx, y2 - yy};
                    if (geMathLinEq(xy1, xy2, mm) < 1) {
                        coll = false;
                        break;
                    }

                    if (a == ii.length - 1) {
                        int x3 = ii[a][0];
                        int y3 = ii[a][1];
                        int[] xy3 = {x3 - yx, y3 - yy};
                        int x4 = ii[0][0];
                        int y4 = ii[0][1];
                        int[] xy4 = {x4 - yx, y4 - yy};
                        if (geMathLinEq(xy3, xy4, mm) < 1) {
                            coll = false;
                            break;
                        }
                    }
                }
            }
            if (coll) {
                return true;
            }
        }
        return false;
    }
    public Armor() {
        x = 1988 * Math.random();
        y = 1988 * Math.random();
        if (coll()) return;

        type = (int) (1 + (2 * Math.random()));
        try {
            cimg = ImageIO.read(getClass().getClassLoader().getResourceAsStream("imgs/cha/arm_" + type + ".png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ClientHandler.broadcastMessage("host:amrspawn:" + x + ":" + y + ":" + type);
        armorArrayList.add(this);
    }
    public Armor(int x, int y, int type){
        this.x = x;
        this.y = y;
        if (coll()) return;
        this.type = type;
        try {
            cimg = ImageIO.read(getClass().getClassLoader().getResourceAsStream("imgs/cha/arm_" + type + ".png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ClientHandler.broadcastMessage("host:amrspawn:" + x + ":" + y + ":" + type);
        armorArrayList.add(this);
    }

    public BufferedImage getImage(){
        return cimg;
    }
    public int getX() {
        return (int) x;
    }

    public int getY() {
        return (int) y;
    }

    public boolean checkInteract(int x, int y){
        return (
                ((x >= this.x) && (x <= this.x+34) && (y >= this.y) && (y <= this.y+34)) ||
                        ((x+64 >= this.x) && (x+64 <= this.x+34) && (y >= this.y) && (y <= this.y+34)) ||
                        ((x >= this.x) && (x <= this.x+34) && (y+64 >= this.y) && (y+64 <= this.y+34)) ||
                        ((x+64 >= this.x) && (x+64 <= this.x+34) && (y+64 >= this.y) && (y+64 <= this.y+34)) ||
                        ((x+32 >= this.x) && (x+32 <= this.x+34) && (y+32 >= this.y) && (y+32 <= this.y+34))
        );
    }

    public int getType() {
        return type;
    }

    public int getPosX(){
        return (int)(((Game.getGame().getScene().getX()*-1))+x);
    }
    public int getPosY(){
        return (int)((Game.getGame().getScene().getY()*-1)+y);
    }
    public int getPosBoundX(){
        return (int)((Game.getGame().getScene().getX()*-1)+x+34);
    }
    public int getPosBoundY(){
        return (int)((Game.getGame().getScene().getY()*-1)+y+34);
    }

    public static ArrayList<Armor> getArmors(){
        return armorArrayList;
    }
    public void remove(){
        armorArrayList.remove(this);
    }
}
