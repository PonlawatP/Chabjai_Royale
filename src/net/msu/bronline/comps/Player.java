package net.msu.bronline.comps;

import net.msu.bronline.network.ClientHandler;
import net.msu.bronline.network.NetworkDevices;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import static net.msu.bronline.funcs.Utils.*;
import static net.msu.bronline.guis.Game.getGame;

public class Player {
    static ArrayList<Player> pls = new ArrayList<>();

    public static ArrayList<Player> getPlayers(){
        return pls;
    }

    int m_x = 0, m_y = 0;
    int x = 150, y = 150, ox = 150, oy = 150;
    int hp = 100;
    int armor = 50;
    int armor_type = 1;
    int score = 0;

    BufferedImage cimg;
    Scene scene;
    String username = "Player";
    boolean dead = false;
    int c_r = (int) (1 + 8*Math.random());
    public List<Integer> c_r_t = new ArrayList<>();
    public int randomCharID(){
        if(c_r_t.size() >= 8) return c_r;
        for (int i : c_r_t){
            if(i == c_r) {
                c_r = (int) (1 + 8*Math.random());
//                ClientHandler.broadcastMessage(getUsername()+":skin:"+c_r);
                try {
                    cimg = ImageIO.read(getClass().getClassLoader().getResourceAsStream("imgs/cha/chars_"+c_r+".png"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return randomCharID();
            }
        }

        return c_r;
    }

    public void setCharactorID(int i){
        c_r = i;
        try {
            cimg = ImageIO.read(getClass().getClassLoader().getResourceAsStream("imgs/cha/chars_"+c_r+".png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void updateSkin(int i){
        c_r = i;
        try {
            cimg = ImageIO.read(getClass().getClassLoader().getResourceAsStream("imgs/cha/chars_"+c_r+".png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getCharacterID() {
        return c_r;
    }

    public boolean isFireTrigger() {
        return fireTrigger;
    }

    public void setFireTrigger(boolean fireTrigger) {
        this.fireTrigger = fireTrigger;
    }

    boolean fireTrigger = false;

    public Player(Scene scene) throws IOException {
        this.scene = scene;
        cimg = ImageIO.read(getClass().getClassLoader().getResourceAsStream("imgs/cha/chars_"+c_r+".png"));

        x = 64+(int) (Math.random()*(2000-128));
        y = 64+(int) (Math.random()*(2000-128));
    }

    public Player(Scene scene, String username, int chr) throws IOException {
        this.scene = scene;
        this.username = username;
        c_r = chr;
        cimg = ImageIO.read(getClass().getClassLoader().getResourceAsStream("imgs/cha/chars_"+c_r+".png"));
    }

    int ammo = 37;
    boolean ammo_reloading = false;
    int ammo_re_thr = 0;
    int ammo_re_lim = 100;
    int ammo_cld = 0;
    int re_ammo_cld = 0;
    int ammo_cld_lim = 5;
    int dead_del = 0;

    public int getAmmoRemain(){
        return ammo;
    }

    public boolean isAmmo_reloading() {
        return ammo_reloading;
    }

    public boolean reloadAmmo(){
        ammo_reloading = true;
        if(ammo_re_thr < ammo_re_lim){
            ammo_re_thr++;
            return false;
        }
        ammo_re_thr = 0;
        ammo = 37;
        ammo_reloading = false;

        return true;
    }

    public BufferedImage getPlayerImage(){
        return cimg;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }
    public double getHpPercent() {
        return hp/100f;
    }

    public int getArmor() {
        return armor;
    }

    public int getArmor_type() {
        return armor_type;
    }
    public double getArmorPercent(){
        return (getArmor()/(getArmor_type() == 1 ? 50f : 100f));
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public void setArmor_type(int armor_type) {
        this.armor_type = armor_type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private int colls(int[][] ii, int xx, int yy){
        int s = 0;
        for(int a = 0; a < ii.length; a++){
            int[] i = ii[a];
            int[] mm = {xx, yy};
            if(a>0){
                if(geMathLinEq(ii[a-1], i, mm) < 1){
                    s++;
                }
                if(a == ii.length-1) {
                    if(geMathLinEq(ii[a], ii[0], mm) < 1) {
                        s++;
                    }
                }
            }
        }
        return s;
    }

    public void moveForward(double x){
        if(this.x+14+x >= 0 && this.x+64-14+x <= 2000){
            boolean coll = false;
            int back = 0;
            for (int[][] ii : colld_data){
                int x1 = colls(ii,(int) (this.x+14+x), this.y+20);
                int x2 = colls(ii,(int) (this.x+64-14+x), this.y+20);
                int x3 = colls(ii,(int) (this.x+14+x), this.y+64-2);
                int x4 = colls(ii,(int) (this.x+64-14+x), this.y+64-2);
                if(x1 == 0 || x2 == 0 || x3 == 0 || x4 == 0) {
                    coll = true;
                }
            }

            if(!coll) {
                this.x+=x;
            } else {
//                this.y-=x;
            }
        }
    }
    public void moveUp(double y){
        if(this.y+20+y >= 0 && this.y+64-2+y <= 2000){
            boolean coll = false;
            for (int[][] ii : colld_data){
                int x1 = colls(ii,(this.x+14), (int) (this.y+20+y));
                int x2 = colls(ii,(this.x+64-14), (int) (this.y+20+y));
                int x3 = colls(ii,(this.x+14), (int) (this.y+64-2+y));
                int x4 = colls(ii,(this.x+64-14), (int) (this.y+64-2+y));
                if(x1 == 0 || x2 == 0 || x3 == 0 || x4 == 0) coll = true;
            }
            if(!coll) {
                this.y+=y;
            } else {
//                this.x-=y;
            }
        }
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public int getPosX(){
        return (int)(((scene.getX()*-1))+x);
    }
    public int getPosY(){
        return (int)((scene.getY()*-1)+y);
    }
    public int getPosBoundX(){
        return (int)((scene.getX()*-1)+x+64);
    }
    public int getPosBoundY(){
        return (int)((scene.getY()*-1)+y+64);
    }

    public int getMouseX() {
        return m_x;
    }
    public int getMouseY() {
        return m_y;
    }
    public void updateMouse(int x, int y){
        m_x = x;
        m_y = y;
    }
    double ang = -999;
    public double getAngle(){
        if(ang != -999) return ang;
        double tx = m_x-(getPosX()+(64/2));
        double ty = m_y-(getPosY()+42);
        double atan = Math.atan2(ty,tx);
        double deg = Math.toDegrees(atan);
        return deg;
    }

    public void setAmmo(int ammo) {
        this.ammo = ammo;
    }

    public void hurt(int dmg, String name){
        if(dead) return;
        if(armor > 0){
            armor-=dmg;

            if(armor < 0){
                hp += (armor*1.3);
                armor = 0;
            }
        } else {
            hp -= (dmg*1.3);

            if(hp <= 0) {
                playerDead(name);
            }
        }
    }
    public void playerDead(String name){
        hp = 0;
        dead = true;
        i1 = 0;
        Iterator<Player> ps = new ArrayList<>(Player.getPlayers()).iterator();
        while (ps.hasNext()){
            Player p = ps.next();
            if(p.getUsername().equalsIgnoreCase(name)){
                p.setScore(p.getScore()+1);

//                System.out.println(p.getUsername() + " : " + getGame().getPlayerOwn().getUsername() + " : " + !p.getUsername().equalsIgnoreCase(getGame().getPlayerOwn().getUsername()));
                if(!p.getUsername().equalsIgnoreCase(getGame().getPlayerOwn().getUsername())) scene.setPlayerTarget(p);
                if(p.getScore() >= 2){
                    getGame().getScene().winnerScene(p);
                    if(getGame().isHosting()){
                        ClientHandler.broadcastMessage(p.getUsername() + ":act:ended");
                        ClientHandler.broadcastMessage(p.getUsername() + ":winner");
                    }
                }
                return;
            }
        }
    }
    int i = 0, i1 = 0, a1 = 11, a1_lim = 8;
    public void updateAnimation(){
        i++;
        boolean back = false;

        if(dead){
            a1 = 20;
            a1_lim = 5;
            if(i >= 2) {
                i = 0;
                i1++;
                if (i1 > a1_lim) {
                    i1 = a1_lim;
                }
                dead_del++;
                if(dead_del > 90){
                    dead_del = 0;
                    respawn();
                }
            }
            return;
        } else {
//            if(ox < x || (ox < x && oy != y)){
//                a1 = 11;
//                a1_lim = 8;
//            } else if(ox > x || (ox > x && oy != y)){
//                a1 = 9;
//                a1_lim = 8;
//            } else if(oy < y){
//                a1 = 10;
//                a1_lim = 8;
//            } else if(oy > y){
//                a1 = 8;
//                a1_lim = 8;
//            }
            double ang = getAngle();
            if (ang >= 45 && ang < 135 ) { //f
                if(oy > y) back = true;

                a1 = 10;
            } else if((ang < 180 && ang >= 135 || ang >= -180 && ang < -135)) { //l
                if(ox < x || (ox < x && oy != y)) back = true;

                a1 = 9;
            } else if(ang >= -135 && ang < -45 ) { //b
                if(oy < y) back = true;

                a1 = 8;
            } else { //r
                if(ox > x || (ox > x && oy != y)) back = true;

                a1 = 11;
            }
        }

        if(i >= 2) {
            i = 0;

            if(ox == x && oy == y){
                i1 = 0;
            } else {
                if(back){
                    i1--;
                    if(i1 < 0) {
                        i1 = a1_lim;
                    }
                } else {
                    i1++;
                    if(i1 > a1_lim) {
                        i1 = 0;
                    }
                }
            }
        }
        ox = x;
        oy = y;
    }
    public void respawn(){
        if(!getGame().isHosting()) return;
        if(getGame().getGame_status() == 3) return;
        int x = 64+(int) (Math.random()*(2000-128));
        int y = 64+(int) (Math.random()*(2000-128));
        respawn(x, y);
    }
    public void respawn(int x, int y){
        if(getGame().getGame_status() == 3) return;
        hp = 100;
        armor = 50;
        armor_type = 1;
        ammo = 37;
        scene.setPlayerTarget(null);

        dead = false;
        this.x = x; this.y = y;
        ClientHandler.broadcastMessage(getUsername() + ":respawn:" + x + ":" + y);
    }
    public int getSpriteX(){
        return (64*i1)+1;
    }
    public int getSpriteY(){
        return (64*a1)+1;
    }
    public int getSpriteDX(){
        return (64*(i1+1))-1;
    }
    public int getSpriteDY(){
        return (64*(a1+1))-1;
    }

    ArrayList<int[]> marker = new ArrayList<int[]>();

    public ArrayList<int[]> getMarker() {
        return marker;
    }
    public void addMarker(){
        int[] m = {scene.getX()+m_x, scene.getY()+m_y};
        marker.add(m);
        System.out.print("{");
        int a = 0;
        for(int[] is : marker){
            System.out.print((a>0?",{":"{")+is[0]+","+is[1]+"}");
            a++;
        }
        System.out.print("},\n---------\n");
    }

    public int getScore() {
        return score;
    }

    public String getPacket(){
//        username:player:skin:x:y:mouse_x:mouse_y:hp:armor:armor_type:fireTrigger
        return "player:"+c_r+ ":" + getX() + ":" + getY() + ":" + getAngle() + ":" + getHp() + ":" + getArmor() + ":" + getArmor_type() + ":" + isDead() + ":" + getScore();
    }
    public String getPacket(String type){
//        username:player:skin:x:y:mouse_x:mouse_y:hp:armor:armor_type:fireTrigger
        return type+":"+c_r+ ":" + getX() + ":" + getY() + ":" + getAngle() + ":" + getHp() + ":" + getArmor() + ":" + getArmor_type() + ":" + isDead() + ":" + getScore();
    }
    public void updateFromPacket(String[] data){
//        System.out.println("'"+username + "' : " + "'"+data[0]+"'");
        if(!(data[0].equalsIgnoreCase(username) && data[1].equalsIgnoreCase("player"))) return;
        x = Integer.parseInt(data[3]);
        y = Integer.parseInt(data[4]);
        ang = Double.parseDouble(data[5]);
        hp = Integer.parseInt(data[6]);
        armor = Integer.parseInt(data[7]);
        armor_type = Integer.parseInt(data[8]);
        dead = data[9].equalsIgnoreCase("true");
        score = Integer.parseInt(data[10]);
    }

    public static void removePlayer(String name){
        Iterator<Player> ps = getPlayers().iterator();
        while (ps.hasNext()){
            Player p = ps.next();
            if(p.getUsername().equals(name)){
                ps.remove();
                break;
            }
        }
    }

    private ArrayList<Ammo> amms = new ArrayList<>();

    public ArrayList<Ammo> getAmmo() {
        return amms;
    }

    public boolean shoot(int x, int y, double angle, boolean senddata) {
        Ammo am = new Ammo(this, x, y, angle);
        getAmmo().add(am);

        if(!senddata) return true;

        if (getGame().isHosting()) {
            ClientHandler.broadcastMessage(getUsername() + ":shoot:" + x + ":" + y + ":" + getAngle());
        } else {
            getGame().getClientProgram().getCwrite().sendMessage(getUsername() + ":shoot:" + x + ":" + y + ":" + getAngle());
        }
        return true;
    }
    public boolean shoot(){
        if(ammo > 0){
            if(ammo_cld < ammo_cld_lim){
                ammo_cld++;
            } else {
                ammo_cld = 0;
                ammo--;
                return shoot(x, y, getAngle(), true);
            }
        }
        return false;
    }

    public static Player getPlayer(String name){
        Iterator<Player> ps = getPlayers().iterator();
        while (ps.hasNext()){
            Player p = ps.next();
            if(p.getUsername().equals(name)){
                return p;
            }
        }
        return null;
    }
}
