package net.msu.bronline.comps;

import net.msu.bronline.network.ClientHandler;
import net.msu.bronline.network.NetworkDevices;

import java.awt.*;

import static net.msu.bronline.guis.Game.getGame;

public class Ammo {
    private double x,y,dx,dy;
    Player shooter;
    public Ammo(Player shooter, int x, int y, int dx, int dy){
        this.shooter = shooter;
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
    }

    public int[] getDimStart() {
        return new int[]{(int) x, (int) y};
    }
    public int[] getDimStop() {
        return new int[]{(int) dx, (int) dy};
    }

    public boolean runProjectile(){
        x+=0.5;
        y+=0.5;
        return false;
    }

}
