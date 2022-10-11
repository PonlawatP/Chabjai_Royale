package net.msu.bronline.guis;

import net.msu.bronline.comps.Ammo;
import net.msu.bronline.comps.Player;
import net.msu.bronline.comps.Scene;
import net.msu.bronline.network.CientProgram;
import net.msu.bronline.network.ClientHandler;
import net.msu.bronline.network.ServerProgram;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import static net.msu.bronline.funcs.Utils.*;
import static net.msu.bronline.guis.Present.getPresent;

public class Game extends JPanel {
    public static Game g;

    JFrame cFrame;
    Canvas cCanv;
    Scene scene;
    Player p_own;

    boolean hosting = false;
    Thread t_host;

    boolean[] movements;
    ServerProgram sp;
    CientProgram cp;



    int frameTime = 0;
    boolean start = false;
    String roomName;
    int p_amount = 16;
    int game_status = 0;
    String hostUser = "";

    public Game(JFrame cFrame, Canvas canv, boolean[] movements, String username, boolean host) throws IOException {
        g = this;
        this.cFrame = cFrame;
        this.cCanv = canv;
        this.movements = movements;
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
    public void resetGame() {
        try{
            String user = p_own.getUsername();
            Player.getPlayers().clear();
            new Game(cFrame, cCanv, movements, user, hosting);
        } catch (IOException e){
            System.out.println(e.getMessage());
//            e.printStackTrace();
        }
    }
    public void updateRoom(String[] data){
        //username:conf:name:amount:max:status
        int g_all = Integer.parseInt(data[4]);
        int g_prstatus = Integer.parseInt(data[5]);
        int g_gstatus = Integer.parseInt(data[6]);
        setHostUser(data[0]);
        setRoomName(data[2]);
        setP_amount(g_all);
        setGame_status(g_gstatus);
        getPresent().setGame_status((g_prstatus == 3) ? 4 : g_prstatus);
    }

    public void setHostUser(String hostUser) {
        this.hostUser = hostUser;
    }

    public String getHostUser() {
        return hostUser;
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

    public CientProgram getClientProgram() {
        return cp;
    }

    public void startMode(){
        if(hosting){
            sp = new ServerProgram(scene, this);
            t_host = new Thread(sp);
        } else {
            cp = new CientProgram(getPlayerOwn().getUsername(), ip);
            t_host = new Thread(cp);
        }
        t_host.start();
    }
    public void stopMode(){
        if(hosting) {
            if(sp != null) sp.closeSev();
        } else {
            if(cp != null) cp.closeEverything();
        }
        getPresent().setGame_status(2);
        resetGame();
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

    BufferedImage btn_play = ImageIO.read(getClass().getClassLoader().getResourceAsStream("imgs/btn_play.png"));
    BufferedImage btn_back = ImageIO.read(getClass().getClassLoader().getResourceAsStream("imgs/btn_back.png"));

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
    public void draw(Graphics ge)  {
        Graphics2D g = (Graphics2D) ge;
        g.drawImage(
                scene.getImg(false),
                0,0
                , cCanv.getWidth(), cCanv.getHeight(),

                scene.getX(), scene.getY()
                ,scene.getBoundX(), scene.getBoundY()

                ,this
        );
        drawPlayer(g);

        try {
            drawUI(g);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void drawPlayer(Graphics2D g){
        Iterator<Player> ps = new ArrayList<>(Player.getPlayers()).iterator();

        g.setFont(new Font("Kanit Light", Font.PLAIN, 14));

        while (ps.hasNext()){
            Player p = ps.next();
//            System.out.println(p.getPacket());

            g.setColor(Color.BLACK);
            g.fillRoundRect(p.getPosX(),p.getPosY()+4, 64, 3, 2, 2);
            g.setColor(new Color(0xC5C5CE));
            g.fillRoundRect(p.getPosX(),p.getPosY()+4, (int) (64*p.getArmorPercent()), 3, 2, 2);
            g.setColor(Color.BLACK);
            g.fillRoundRect(p.getPosX(),p.getPosY()+7, 64, 3, 2, 2);
            g.setColor(new Color(0xA11414));
            g.fillRoundRect(p.getPosX(),p.getPosY()+7, (int) (64*p.getHpPercent()), 3, 2, 2);

            g.setColor(Color.BLACK);
            g.drawString(p.getUsername(), p.getPosX()+(64/2)-(((14*p.getUsername().length())/2)/2)+1, p.getPosY()+1);
            g.setColor(Color.WHITE);
            g.drawString(p.getUsername(), p.getPosX()+(64/2)-(((14*p.getUsername().length())/2)/2), p.getPosY());

//            draw ammo
            drawAmmo(g, p);
//            System.out.println(p.getUsername() + " : " + p.getAmmo().size());
            g.drawImage(p.getPlayerImage(), p.getPosX(), p.getPosY(), p.getPosBoundX(), p.getPosBoundY(), p.getSpriteX(),p.getSpriteY(),p.getSpriteDX(),p.getSpriteDY(),this);
            p.updateAnimation();
        }
    }

    public void drawAmmo(Graphics2D g, Player p){
        double posmx = ((double) m_x-(cCanv.getWidth()/2))/(cCanv.getWidth()/2);
        double posmy = ((double) m_y-(cCanv.getHeight()/2))/(cCanv.getHeight()/2);
        int mpalx = (int)((posmx*15*scene.getSize()));
        int mpaly = (int)((posmy*15*scene.getSize()));

        int calc_cenx = p.getPosX()+(64/2)-(p.getPosX()+(64/2)-p.getMouseX());
        int calc_ceny = p.getPosY()+42-(p.getPosY()+42-p.getMouseY());
        calc_cenx += mpalx;
        calc_ceny += mpaly;

        Iterator<Ammo> ams = new ArrayList<>(p.getAmmo()).iterator();
        while (ams.hasNext()){
            Ammo a = ams.next();
            g.setColor(a.getColor());
            double posax = ((double) m_x-(cCanv.getWidth()/2))/(cCanv.getWidth()/2);
            double posay = ((double) m_y-(cCanv.getHeight()/2))/(cCanv.getHeight()/2);

            int ax = a.getDimStart()[0]+(64/2)-5, ay = a.getDimStart()[1]+42-5;
            ax += (scene.getX()*-1);
            ay += (scene.getY()*-1);
//            ax -= mpalx;
//            ay -= mpaly;
            double rx = 65 * Math.cos(Math.toRadians(a.getAngle())), ry = 65 * Math.sin(Math.toRadians(a.getAngle()));

            if(dev){
                g.drawLine(ax+5, ay+5, (int) (ax+5+rx), (int) (ay+5+ry));
                g.drawString("x: "+a.getDimStart()[0] + " y: " + a.getDimStart()[1], ax+15, ay);
                g.drawString("px: "+posax + " py: " + posay, ax+15, ay+20);
            }
            g.fillOval(ax, ay, 10, 10);
        }

        if(dev){
            g.setColor(Color.BLACK);
            double rx = 65 * Math.cos(Math.toRadians(p.getAngle())), ry = 65 * Math.sin(Math.toRadians(p.getAngle()));
            g.drawLine(p.getPosX()+(64/2), p.getPosY()+42, (int) (p.getPosX()+(64/2)+rx), (int) (p.getPosY()+42+ry));
            g.drawOval(p.getPosX()-(64/2), p.getPosY()-(64-42), 64*2, (64*2));
            g.drawString("scene_x:" + (scene.getRawX()+m_x) + " scene_y: " + (scene.getRawY()+m_y), p.getPosX(), p.getPosY()-35);

            g.drawString("x:" + (p.getPosX()+(64/2)) + " y: " + (p.getPosY()+42) + " [" + (p.getPosX()+(64/2)+p.getMouseX()) + ":" + (p.getPosY()+42+p.getMouseY()) + "]", p.getPosX(), p.getPosY()+75);
            g.drawString("x:" + p.getMouseX() + " y:" + p.getMouseY() + " | " + rx + " : " + ry, p.getPosX(), p.getPosY()+95);
            g.drawString("x:" + calc_cenx + " y:" + calc_ceny + " | x:" + ((p.getPosX()+(64/2))-calc_cenx) + " y:" + ((p.getPosY()+42)-calc_ceny), p.getPosX(), p.getPosY()+115);

            g.drawString("deg: " + getPlayerOwn().getAngle(), p.getPosX(), p.getPosY()+135);
        }
    }

    public void drawUI(Graphics ge) throws IOException{
        Graphics2D g = (Graphics2D) ge;
        //ui-code here

        BufferedImage Fimg = ImageIO.read(getClass().getClassLoader().getResourceAsStream("imgs/F.png"));
        g.drawImage(Fimg,33,cFrame.getHeight()-150,this);

        g.setColor(Color.BLACK);
        g.fillRect(180,cFrame.getHeight()-90, 240,13);
        g.setColor(Color.white);
        g.fillRect(180,cFrame.getHeight()-90, (int) (240*p_own.getHpPercent()),13);

        g.setColor(Color.BLACK);
        g.fillRect(180,cFrame.getHeight()-100,220,8);
        g.setColor(new Color(0xC5C5CE));
        g.fillRect(180,cFrame.getHeight()-100, (int) (220*p_own.getArmorPercent()),8);

        Player p = getGame().getPlayerOwn();
        g.setFont(new Font("Kanit Light", Font.PLAIN, 20));
        g.setColor(Color.WHITE);
        g.drawString(p.getUsername(),180,cFrame.getHeight()-115);

//        g.setColor(new Color(0,0,0,70));
//        g.fillRect(913,cFrame.getHeight()-180,320,110);
        BufferedImage FGimg = ImageIO.read(getClass().getClassLoader().getResourceAsStream("imgs/F.png"));
        g.drawImage(FGimg,813,cFrame.getHeight()-150,this);

        BufferedImage Gimg = ImageIO.read(getClass().getClassLoader().getResourceAsStream("imgs/G.png"));
        g.drawImage(Gimg,883,cFrame.getHeight()-140,200,70,this);

        g.setColor(Color.white);
        g.setFont(new Font("Kanit Light", Font.PLAIN, 50));
        g.drawString(getPlayerOwn().getAmmoRemain()+"",1115,cFrame.getHeight()-100);
        g.setFont(new Font("Kanit Light", Font.PLAIN, 20));
        g.drawString("120",1163,cFrame.getHeight()-80);
//        g.setColor(Color.white);
//        g.drawString("120",1113,cFrame.getHeight()-80);

        if(dev){
            g.setFont(new Font("Kanit Light", Font.PLAIN, 12));
            int x = 0;
            for (int[] i : getPlayerOwn().getMarker()){
                g.setColor(Color.BLACK);
//                g.drawString("x: " + scene.getX() + " " + (i[0]+10), (scene.getX()*-1)+i[0]+10, (scene.getY()*-1)+i[1]+15);
//                g.drawString("y: " + scene.getY() + " " + (i[1]+35), (scene.getX()*-1)+i[0]+10, (scene.getY()*-1)+i[1]+35);
                g.drawString("x: " + i[0], (scene.getX()*-1)+i[0]+10, (scene.getY()*-1)+i[1]+15);
                g.drawString("y: " + i[1], (scene.getX()*-1)+i[0]+10, (scene.getY()*-1)+i[1]+35);

                if(x>0){
                    int[] mm = {getPlayerOwn().getX()+20, getPlayerOwn().getY()};
                    g.drawString("LinEq: " + geMathLinEq(getPlayerOwn().getMarker().get(x-1), i, mm), (scene.getX()*-1)+i[0]+10, (scene.getY()*-1)+i[1]+55);
                    g.setColor(Color.RED);
                    g.drawLine((scene.getX()*-1)+getPlayerOwn().getMarker().get(x-1)[0],(scene.getY()*-1)+getPlayerOwn().getMarker().get(x-1)[1], (scene.getX()*-1)+i[0], (scene.getY()*-1)+i[1]);
                }
                x++;
            }
        }

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
    public void runFunction(){
    }
    public void run(int m_x, int m_y) {
        if(getGame_status() == 2){
            if(!p_own.isDead()){
                if(movements[0]) p_own.moveUp(-1*v_speed);
                if(movements[1]) p_own.moveForward(-1*v_speed);
                if(movements[2]) p_own.moveUp(1*v_speed);
                if(movements[3]) p_own.moveForward(1*v_speed);
                if(movements[4]) v_speed = 7; else v_speed = 5;

                if(!p_own.isAmmo_reloading()){
                    if(movements[6]) {
                        p_own.shoot();
                    } else {
                        if(p_own.isFireTrigger()){
                            p_own.shoot();
                            p_own.setFireTrigger(false);
                        }
                    }
                }

                if(movements[8]) v_speed = v_speed/2;
                if(movements[9] || p_own.isAmmo_reloading() || p_own.getAmmoRemain() == 0) p_own.reloadAmmo();
            }

            this.m_x = m_x;
            this.m_y = m_y;

            Iterator<Player> ps = new ArrayList<>(Player.getPlayers()).iterator();
            while (ps.hasNext()) {
                Player p = ps.next();
                Iterator<Ammo> ams = new ArrayList<>(p.getAmmo()).iterator();
                while (ams.hasNext()) {
                    Ammo a = ams.next();
                    a.runProjectile();
                }
            }

            scene.updateMouse(m_x, m_y);
            scene.updatePosition(getPlayerOwn().getX(), getPlayerOwn().getY());
        }

//        if(scene.getX() < 0 || scene.getBoundX() > scene.getSize_x()){
//            if(scene.getX() < 0) scene.setX(0); else scene.setX(((scene.getSize_x()-cCanv.getWidth()))-1);
//        }
//        if(scene.getY() < 0 || scene.getBoundY() > scene.getSize_y()){
//            if(scene.getY() < 0) scene.setY(0); else scene.setY(scene.getSize_y()-cCanv.getHeight()-1);
//        }
    }

    String status_desc = "Waiting Players";

    public String getStatus_desc() {
        return status_desc;
    }

    public void setStatus_desc(String status_desc) {
        this.status_desc = status_desc;
    }

    // ------------- ฟังก์ชันเช็คว่าเมาส์อยู่ในปุ่มมั้ย
    public int isMouseOnStart(int x, int y){
        if(game_status == 0){
            if((x >= b_cen_x && x <= b_cen_x+btn_sx) && (y >= btn_py && y <= btn_py+btn_sy)) return 0;
        }
//        else if(game_status == 2){
//
//        }

        return -1;
    }

    public void startGame() {
        ClientHandler.broadcastMessage("host:act:pre_start");
        setGame_status(1);
        if (!movements[4]) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int i = 5; i >= 0; i--) {
                            ClientHandler.broadcastMessage("host:desc:Starting in " + i);
                            setStatus_desc("Starting in " + i);
                            Thread.sleep(1000);
                        }
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }

                    ClientHandler.broadcastMessage("host:act:start");
                    setGame_status(2);
                    getPresent().setGame_status(5);
                }
            }).start();
            return;
        }

        ClientHandler.broadcastMessage("host:act:start");
        setGame_status(2);
        getPresent().setGame_status(5);
    }



    public static Game getGame(){
        return g;
    }
}
