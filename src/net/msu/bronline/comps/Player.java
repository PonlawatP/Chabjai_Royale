package net.msu.bronline.comps;

import net.msu.bronline.network.ClientHandler;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

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

    public void moveForward(double x){
        this.x+=x;
    }
    public void moveUp(double y){
        this.y+=y;
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
            if(p.getUsername().equals(name)){
                p.setScore(p.getScore()+1);
            }
        }
    }
    int i = 0, i1 = 0, a1 = 11, a1_lim = 8;
    public void updateAnimation(){
        i++;

        if(dead){
            a1 = 20;
            a1_lim = 5;
            if(i >= 2) {
                i = 0;
                i1++;
                if (i1 > a1_lim) {
                    i1 = a1_lim;
                }
            }
            return;
        } else {
            if(ox < x || (ox < x && oy != y)){
                a1 = 11;
                a1_lim = 8;
            } else if(ox > x || (ox > x && oy != y)){
                a1 = 9;
                a1_lim = 8;
            } else if(oy < y){
                a1 = 10;
                a1_lim = 8;
            } else if(oy > y){
                a1 = 8;
                a1_lim = 8;
            }
        }

        if(i >= 2) {
            i = 0;

//            System.out.println(getAngle());
//            if(getAngle() <= -135 || getAngle() >= 135){
//                a1 = 8;
//                a1_lim = 8;
//            } else if(getAngle() <= -45){
//                a1 = 11;
//                a1_lim = 8;
//            } else if(getAngle() >= 45){
//                a1 = 10;
//                a1_lim = 8;
//            }

            if(ox == x && oy == y){
                i1 = 0;
            } else {
                i1++;
                if(i1 > a1_lim) {
                    i1 = 0;
                }
            }
        }
        ox = x;
        oy = y;
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
