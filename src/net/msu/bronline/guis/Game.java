package net.msu.bronline.guis;

import net.msu.bronline.comps.Player;
import net.msu.bronline.comps.Scene;
import net.msu.bronline.network.CientProgram;
import net.msu.bronline.network.ServerProgram;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class Game extends JPanel {
    JFrame cFrame;
    Canvas cCanv;
    Scene scene;
    Player p_own;

    boolean hosting = false;
    Thread t_host;

    boolean[] movements;
    Present ps;
    ServerProgram sp;
    CientProgram cp;



    int frameTime = 0;
    boolean start = false;
    String roomName;
    int p_amount = 16;
    int game_status = 0;

    public Game(JFrame cFrame, Canvas canv, boolean[] movements, Present ps, String username, boolean host) throws IOException {
        this.cFrame = cFrame;
        this.cCanv = canv;
        this.movements = movements;
        this.ps = ps;
        this.hosting = host;
        scene = new Scene(cFrame, cCanv, true);
        setSize(cFrame.getSize());
        setPreferredSize(cFrame.getPreferredSize());
        setBackground(Color.white);
        setVisible(true);

        p_own = new Player(scene);
        p_own.setUsername(username);
        Player.getPlayers().add(p_own);

        roomName = getPlayerOwn().getUsername()+"'s Room";
    }

    public int getMaxPlayer(){
        return p_amount;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getP_amount() {
        return p_amount;
    }

    public void setP_amount(int p_amount) {
        this.p_amount = p_amount;
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    String ip = "0.0.0.0";

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIP() {
        return ip;
    }

    public void startMode(){
        if(hosting){
            sp = new ServerProgram(scene, this);
            t_host = new Thread(sp);
        } else {
            cp = new CientProgram(getPlayerOwn().getUsername(), ip, ps, this);
            t_host = new Thread(cp);
        }
        t_host.start();
    }
    public void stopMode(){
        if(hosting) {
            sp.closeSev();
        } else {
            cp.closeEverything();
        }
    }

    public boolean isHosting() {
        return hosting;
    }

    public void setHosting(boolean hosting) {
        this.hosting = hosting;
    }

    public Player getPlayerOwn(){
        return p_own;
    }

    BufferedImage btn_play = ImageIO.read(new File(getClass().getClassLoader().getResource("imgs/btn_play.png").getPath()));
    BufferedImage btn_back = ImageIO.read(new File(getClass().getClassLoader().getResource("imgs/btn_back.png").getPath()));

    float btn_size = .5f;
    float btn_back_size = .1f;
    int btn_sx = (int)(480*btn_size), btn_sy = (int)(160*btn_size); //ขนาดภาพ
    int btn_back_sx = (int)(1020*btn_back_size), btn_back_sy = (int)(400*btn_back_size); //ขนาดภาพ
    int btn_px = 0, btn_py = 0;
    int btn2_px = 0, btn2_py = 0;
    int btn_back_px = 0, btn_back_py = 0;


//    method แปลงความใสภาพ
    BufferedImage getOpacityImg(BufferedImage img, int width, int height, int opacity) throws IOException {
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB_PRE);

        Graphics2D g = bi.createGraphics();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (opacity/100f)));
        g.drawImage(img, 0,0,width ,height, null);
        g.dispose();

        return bi;
    }

    int b_cen_x = 0, b_cen_y = 0;
    int i = 0, i1 = 0;
    public void draw(Graphics ge){
        Graphics2D g = (Graphics2D) ge;
        g.drawImage(
                scene.getImg(false),
                0,0
                , cCanv.getWidth(), cCanv.getHeight(),

                scene.getX(), scene.getY()
                ,scene.getBoundX(), scene.getBoundY()

                ,this
        );
        Iterator<Player> ps = Player.getPlayers().stream().iterator();
        i++;
        if(i >= 2) {
            i = 0;
            i1++;
            if(i1 > 8) i1 = 0;
        }
        int a1 = 11;
        g.setFont(new Font("Kanit Light", Font.PLAIN, 14));
        while (ps.hasNext()){
            Player p = ps.next();
//            System.out.println(p.getPacket());
            g.setColor(Color.BLACK);
            g.drawString(p.getUsername(), p.getPosX()+(64/2)-(((14*p.getUsername().length())/2)/2)+1, p.getPosY()+6);
            g.setColor(Color.WHITE);
            g.drawString(p.getUsername(), p.getPosX()+(64/2)-(((14*p.getUsername().length())/2)/2), p.getPosY()+5);
            g.drawImage(p.getPlayerImage(), p.getPosX(), p.getPosY(), p.getPosBoundX(), p.getPosBoundY(),(64*i1)+1,(64*a1)+1,(64*(i1+1))-1, (64*(a1+1))-1,this);
        }
        //        g.drawImage(scene.getImg(true), 0,0, cCanv.getWidth(), cCanv.getHeight(),scene.getX(), scene.getY(),scene.getBoundX(), scene.getBoundY(),this);

//        g.setColor(Color.RED);
//        g.drawRoundRect(0,0, 10, 10, 3, 3);

//        if(game_status == 0){
//            b_cen_x = (cCanv.getWidth()/2)-(btn_sx/2);
//            btn_py = cCanv.getHeight()-100;
//
//            g.drawImage(btn_play, b_cen_x,btn_py, b_cen_x+btn_sx,btn_py+btn_sy,0,0,480,160,this);
//        }

        drawUI(g);
    }

    public void drawUI(Graphics ge){
        Graphics2D g = (Graphics2D) ge;
        //ui-code here
    }

    public int getGame_status() {
        return game_status;
    }

    public void setGame_status(int game_status) {
        this.game_status = game_status;
    }

    public Scene getScene() {
        return scene;
    }
    int m_x = 0, m_y = 0;
    double v_speed = 1;
    public void run(int m_x, int m_y) {
        if(getGame_status() == 2){
//            if(movements[0]) scene.moveUp(-1*v_speed);
//            if(movements[1]) scene.moveForward(-1*v_speed);
//            if(movements[2]) scene.moveUp(1*v_speed);
//            if(movements[3]) scene.moveForward(1*v_speed);
            if(movements[0]) p_own.moveUp(-1*v_speed);
            if(movements[1]) p_own.moveForward(-1*v_speed);
            if(movements[2]) p_own.moveUp(1*v_speed);
            if(movements[3]) p_own.moveForward(1*v_speed);
            if(movements[4]) v_speed = 7; else v_speed = 5;
            if(movements[8]) v_speed = v_speed/2;

            this.m_x = m_x;
            this.m_y = m_y;

            scene.updateMouse(m_x, m_y);
        }

//        if(scene.getX() < 0 || scene.getBoundX() > scene.getSize_x()){
//            if(scene.getX() < 0) scene.setX(0); else scene.setX(((scene.getSize_x()-cCanv.getWidth()))-1);
//        }
//        if(scene.getY() < 0 || scene.getBoundY() > scene.getSize_y()){
//            if(scene.getY() < 0) scene.setY(0); else scene.setY(scene.getSize_y()-cCanv.getHeight()-1);
//        }
    }

    // ------------- ฟังก์ชันเช็คว่าเมาส์อยู่ในปุ่มมั้ย
    public int isMouseOnStart(int x, int y){
        if(game_status == 0){
            if((x >= b_cen_x && x <= b_cen_x+btn_sx) && (y >= btn_py && y <= btn_py+btn_sy)) return 0;
        } else if(game_status == 2){

        }

        return -1;
    }
}
