package net.msu.bronline.comps;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Player {
    static ArrayList<Player> pls = new ArrayList<>();

    public static ArrayList<Player> getPlayers(){
        return pls;
    }

    int m_x = 0, m_y = 0;
    int x = 150, y = 150;
    int hp = 100;
    int armor = 50;
    int armor_type = 1;

    BufferedImage cimg;
    Scene scene;
    String username = "Player";
    int c_r = new Random().nextInt(1, 8);

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
        cimg = ImageIO.read(new File(getClass().getClassLoader().getResource("imgs/cha/chars_"+c_r+".png").getPath()));
    }

    public Player(Scene scene, String username, int chr) throws IOException {
        this.scene = scene;
        this.username = username;
        c_r = chr;
        cimg = ImageIO.read(new File(getClass().getClassLoader().getResource("imgs/cha/chars_"+c_r+".png").getPath()));
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

    public int getArmor() {
        return armor;
    }

    public int getArmor_type() {
        return armor_type;
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

    public String getPacket(){
//        username:player:skin:x:y:mouse_x:mouse_y:hp:armor:armor_type:fireTrigger
        return "player:"+c_r+ ":" + getX() + ":" + getY() + ":" + getMouseX() + ":" + getMouseY() + ":" + getHp() + ":" + getArmor() + ":" + getArmor_type() + ":" + isFireTrigger();
    }
    public String getPacket(String type){
//        username:player:skin:x:y:mouse_x:mouse_y:hp:armor:armor_type:fireTrigger
        return type+":"+c_r+ ":" + getX() + ":" + getY() + ":" + getMouseX() + ":" + getMouseY() + ":" + getHp() + ":" + getArmor() + ":" + getArmor_type() + ":" + isFireTrigger();
    }
    public void updateFromPacket(String[] data){
//        System.out.println("'"+username + "' : " + "'"+data[0]+"'");
        if(!(data[0].equalsIgnoreCase(username) && data[1].equalsIgnoreCase("player"))) return;
        x = Integer.parseInt(data[3]);
        y = Integer.parseInt(data[4]);
        m_x = Integer.parseInt(data[5]);
        m_y = Integer.parseInt(data[6]);
        hp = Integer.parseInt(data[7]);
        armor = Integer.parseInt(data[8]);
        armor_type = Integer.parseInt(data[9]);
        fireTrigger = Boolean.getBoolean(data[10]);
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
