package net.msu.bronline.comps;

import net.msu.bronline.guis.Game;
import net.msu.bronline.network.ClientHandler;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

import static net.msu.bronline.funcs.Utils.colld_data;
import static net.msu.bronline.funcs.Utils.geMathLinEq;
import static net.msu.bronline.guis.Game.getGame;

public class Ammo {
    private double x,y,ang;
    private double val_x,val_y;

    double tx, ty, atan, deg, rad;
    private Color cc;
    Player shooter;
    float dv = 18f;
    public Ammo(Player shooter, int x, int y, double angle){
        this.shooter = shooter;
        this.x = x;
        this.y = y;
        this.ang = angle;
        rad = Math.toRadians(angle);

        val_x = dv * Math.cos(rad);
        val_y = dv * Math.sin(rad);
        int a = (int) (Math.random()*5);

        switch (a){
            case 1:
                cc = new Color(0xEF5522);
                break;
            case 2:
                cc = new Color(0xFDC536);
                break;
            case 3:
                cc = new Color(0xEF7122);
                break;
            case 4:
                cc = new Color(0xFFA237);
                break;
            case 5:
                cc = new Color(0xFD7240);
                break;
            default:
                cc = Color.ORANGE;
                break;
        }
    }

    public Color getColor(){
        return cc.brighter();
    }

    public int[] getDimStart() {
        return new int[]{(int) x, (int) y};
    }
    public double getAngle(){
        return ang;
    }
    int dl = 0;

    public boolean runProjectile() {
        double x = this.x + val_x;
        double y = this.y + val_y;

        if (x < -100 || x > 2100 || y < -100 || y > 2100) {
            remove();
            return false;
        }

        Iterator<Player> ps = new ArrayList<>(Player.getPlayers()).iterator();
        while (ps.hasNext()) {
            Player p = ps.next();
            if (p.getUsername().equals(shooter.getUsername())) continue;
            if (x >= p.getX() - 14 && x <= p.getX() + 14 && y >= p.getY() - 27 && y <= p.getY() + 20) {
                double dmg_head = (14 - Math.abs(p.getX() - x)) / 14;
                double dmg_body = (27 - Math.abs(p.getY() - y)) / 27;
                int dmg = (int) (2 + (Math.random() * 2) + ((2 * dmg_body) + (2 * dmg_head)));
//                System.out.println(dmg_head + " : " + dmg_body + " | " + dmg);
                remove();
                if (getGame().isHosting()) { // ทำงานถ้าเป็น host
                    ClientHandler.broadcastMessage(shooter.getUsername() + ":atk:" + p.getUsername() + ":" + dmg);
//                    if(p.getUsername().equals(getGame().getPlayerOwn().getUsername()))
                    p.hurt(dmg, shooter.getUsername());
                    break;
                }
                return true;
            }
        }

        if (dl >= 2) {
            int yx = 25, yy = 70;
            for (int[][] ii : colld_data) {
                boolean coll = true;
                for (int a = 0; a < ii.length; a++) {
                    int[] i = ii[a];
                    int[] mm = {(int) x, (int) y - 27};
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
                    remove();
                    return true;
                }
            }
            dl = 0;
        } else {
            dl++;
        }

        this.x = x;
        this.y = y;

        return false;
    }

    public void remove(){
        shooter.getAmmo().remove(this);
    }
}
