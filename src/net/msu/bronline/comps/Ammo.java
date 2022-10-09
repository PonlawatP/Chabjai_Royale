package net.msu.bronline.comps;

import net.msu.bronline.guis.Game;

public class Ammo {
    private double x,y,dx,dy;
    private double val_x,val_y;

    double tx, ty, atan, deg, rad;
    Player shooter;
    float dv = 15.9f;
    public Ammo(Player shooter, int x, int y, int dx, int dy){
        this.shooter = shooter;
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;

        tx = dx-x-(64/2);
        ty = dy-y-42;
        atan = Math.atan2(ty,tx);
        deg = Math.toDegrees(atan);
        rad = Math.toRadians(deg);

        val_x = dv * Math.cos(rad);
        val_y = dv * Math.sin(rad);
    }

    public int[] getDimStart() {
        return new int[]{(int) x, (int) y};
    }
    public int[] getDimStop() {
        return new int[]{(int) dx, (int) dy};
    }

    public boolean runProjectile(){
        x += val_x;
        y += val_y;

        return false;
    }

}
