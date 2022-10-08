package net.msu.bronline.comps;

import net.msu.bronline.network.ClientHandler;
import net.msu.bronline.network.NetworkDevices;

import java.awt.*;

import static net.msu.bronline.guis.Game.getGame;
import static net.msu.bronline.guis.Present.getPresent;

public class Ammo {
    private double x,y,dx,dy;
    private int v = 1, h = 1;
    private double valo_v = 1, valo_h = 1;
    Player shooter;
    float dir = 5.35f;
    public Ammo(Player shooter, int x, int y, int dx, int dy){
        this.shooter = shooter;
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;

        if(dx < x) h = -1;
        if(dy < y) v = -1;
//        valo_h = Math.sqrt((Math.abs((dx-x == 0 ? 1 : dx-x))^2));
//        valo_v = Math.sqrt((Math.abs((dy-y == 0 ? 1 : dy-y))^2));
//        valo_h = Math.sqrt((Math.abs((dx-x+(64/2) == 0 ? 1 : dx-x+(64/2)))^2))*h;
//        valo_v = Math.sqrt((Math.abs((dy-y+42 == 0 ? 1 : dy-y+42))^2))*v;
        valo_h = Math.sqrt((Math.pow(Math.abs(dx-x), 2)));
        valo_v = Math.sqrt((Math.pow(Math.abs(dy-y), 2)));

        System.out.println("rx: "+valo_h + " ry: " +valo_v);
    }

    public int[] getDimStart() {
        return new int[]{(int) x, (int) y};
    }
    public int[] getDimStop() {
        return new int[]{(int) dx, (int) dy};
    }

    public boolean runProjectile(){
//        System.out.println(mpalx + " : " + mpaly);

        x+=((valo_h/dir)*h);
        y+=((valo_v/dir)*v);
        return false;
    }

}
