package net.msu.bronline.guis;

import net.msu.bronline.comps.Player;
import net.msu.bronline.comps.Scene;
import net.msu.bronline.funcs.Utils;
import net.msu.bronline.network.NetworkDevices;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;
import java.util.List;

import static net.msu.bronline.funcs.Utils.getColor;
import static net.msu.bronline.funcs.Utils.getInsidePosition;
import static net.msu.bronline.guis.Game.getGame;

public class Present extends JPanel {
    static Present ps;
    JFrame cFrame;
    Canvas cCanv;
    Scene scene;
    String username;
    boolean[] movements;
    public Present(JFrame cFrame, Canvas canv, String username, boolean[] movements) throws IOException {
        ps = this;
        this.cFrame = cFrame;
        this.cCanv = canv;
        this.username = username;
        this.movements = movements;
        scene = new Scene(cFrame, cCanv);
        setSize(cFrame.getSize());
        setPreferredSize(cFrame.getPreferredSize());
        setBackground(Color.white);
        setVisible(true);
    }

//    รับโลโก้มาแปลงขนาด แล้วเอามาวางตรงกลาง
//    ImageIcon ii = new ImageIcon(getClass().getClassLoader().getResource("imgs/logo.png"));
    
    BufferedImage logo = ImageIO.read(getClass().getClassLoader().getResourceAsStream("imgs/logo.png"));
    float size = .6f;
    int opc = 0;
    int l_sx = (int)(537*size), l_sy = (int)(417*size); //ขนาดภาพ
    int l_px = 0, l_py = 20; //ตำแหน่งภาพ

//    ปุ่ม
    BufferedImage btn_play = ImageIO.read(getClass().getClassLoader().getResourceAsStream("imgs/btn_play.png"));
    BufferedImage btn_host = ImageIO.read(getClass().getClassLoader().getResourceAsStream("imgs/btn_host.png"));
    BufferedImage btn_join = ImageIO.read(getClass().getClassLoader().getResourceAsStream("imgs/btn_join.png"));
    BufferedImage btn_join_den = ImageIO.read(getClass().getClassLoader().getResourceAsStream("imgs/btn_join_den.png"));
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

    public Scene getScene() {
        return scene;
    }

    int cen_x = 0, cen_y = 0;
    int b_cen_x = 0, b_cen_y = 0;
    int b2_cen_x = 0, b2_cen_y = 0;
    public void draw(Graphics ge){
        Graphics2D g = (Graphics2D) ge;
        cen_x = (cCanv.getWidth()/2)-(l_sx/2);
        cen_y = (cCanv.getHeight()/2)-(l_sy/2);

//        วาด Logo
        try {
            if(frameTime >= 120) g.drawImage(getOpacityImg(scene.getImg(), scene.getSize_x(), scene.getSize_y(), scene.getOpacity()), 0,0, cCanv.getWidth(), cCanv.getHeight(),scene.getX(), scene.getY(),scene.getBoundX(), scene.getBoundY(),this);

            if(frameTime < 120) g.drawImage(getOpacityImg(logo, l_sx, l_sy, opc), l_px+cen_x,l_py+cen_y, this);

//            play btn
            if(game_status == 1){
                btn_py = getInsidePosition(0, cCanv.getHeight(), 80);

                g.setFont(new Font("Kanit Bold", Font.PLAIN, 75));
                String ss = "JABCHAI ROYALE";
                g.setColor(new Color(0x543200));
                g.drawString(ss, cCanv.getWidth()/2-(((75*ss.length())/2)/2)-(((75*ss.length())/2)/2/4), getInsidePosition(0, cCanv.getHeight(), 51));
                g.setColor(Color.ORANGE);
                g.drawString(ss, cCanv.getWidth()/2-(((75*ss.length())/2)/2)-(((75*ss.length())/2)/2/4), getInsidePosition(0, cCanv.getHeight(), 50));


                b_cen_x = (cCanv.getWidth()/2)-(btn_sx/2);
                b_cen_y = (cCanv.getHeight()/2)-(btn_sy/2);
                g.drawImage(btn_play, b_cen_x,btn_py, b_cen_x+btn_sx,btn_py+btn_sy,0,0,480,160,this);
            }
            else if(game_status == 2){
                b_cen_x = ((cCanv.getWidth()/2)-(btn_sx/2))/2;
                b_cen_y = ((cCanv.getHeight()/2)-(btn_sy/2))/2;
                b2_cen_x = ((cCanv.getWidth()/2)-(btn_sx/2))+b_cen_x;
                b2_cen_y = ((cCanv.getHeight()/2)-(btn_sy/2))+b_cen_y;
                btn_py = cCanv.getHeight()-100;
                btn_back_px = 15;
                btn_back_py = 20;


                g.setFont(new Font("Kanit Bold", Font.PLAIN, 45));
                g.setColor(Color.WHITE);
                String ss = "Select Servers";
                g.drawString(ss, cCanv.getWidth()/2-((45*ss.length()/2)/2), getInsidePosition(0, cCanv.getHeight(), 10));

                g.drawImage(btn_back, btn_back_px,btn_back_py, btn_back_px+btn_back_sx,btn_back_py+btn_back_sy,0,0,1020,400,this);

                drawJoinPlayerBoxes(g);

                g.drawImage(btn_host, b_cen_x,btn_py, b_cen_x+btn_sx,btn_py+btn_sy,0,0,480,160,this);
                g.drawImage((i_click != -1 && i_click < NetworkDevices.getHostsIP().size() ? (!is_btn_load) ? btn_join : btn_join_den : btn_join_den), b2_cen_x,btn_py, b2_cen_x+btn_sx,btn_py+btn_sy,0,0,480,160,this);
            }
            else if(game_status == 3){
                btn_back_px = 15;
                btn_back_py = 20;
                b_cen_x = (cCanv.getWidth()/2)-(btn_sx/2);
                b_cen_y = (cCanv.getHeight()/2)-(btn_sy/2);
                btn_py = cCanv.getHeight()-100;

                g.setFont(new Font("Kanit Bold", Font.PLAIN, 45));
                g.setColor(Color.WHITE);
                String ss = "\"" + getGame().getRoomName() + "\" Hosting";
                g.drawString(ss, cCanv.getWidth()/2-((45*ss.length()/2)/2), getInsidePosition(0, cCanv.getHeight(), 10));

                g.setFont(new Font("Kanit Light", Font.PLAIN, 20));
                g.drawString(getGame().getStatus_desc(), cCanv.getWidth()/2-((20*getGame().getStatus_desc().length()/2)/2), getInsidePosition(0, cCanv.getHeight(), 15));
                g.setFont(new Font("Kanit Light", Font.PLAIN, 16));
                g.setColor(Color.GRAY);
                g.drawString("Your IP is: " + getGame().getIp(), 20, cCanv.getHeight()-20);

                drawHostPlayerUI(g);

                if(getGame().getGame_status() == 0){
                    g.drawImage(btn_back, btn_back_px,btn_back_py, btn_back_px+btn_back_sx,btn_back_py+btn_back_sy,0,0,1020,400,this);
                    if(Player.getPlayers().size() >= Utils.allowed_start) g.drawImage(btn_play, b_cen_x,btn_py, b_cen_x+btn_sx,btn_py+btn_sy,0,0,480,160,this);
                }
            }
            else if(game_status == 4){
                btn_back_px = 15;
                btn_back_py = 20;

                g.setFont(new Font("Kanit Bold", Font.PLAIN, 45));
                g.setColor(Color.WHITE);
                String ss = getGame().getRoomName() != null ? getGame().getRoomName() : "";
                g.drawString(ss, cCanv.getWidth()/2-((45*ss.length()/2)/2), getInsidePosition(0, cCanv.getHeight(), 10));

                g.setFont(new Font("Kanit Light", Font.PLAIN, 20));
                g.drawString(getGame().getStatus_desc(), cCanv.getWidth()/2-((20*getGame().getStatus_desc().length()/2)/2), getInsidePosition(0, cCanv.getHeight(), 15));

                if(getGame().getGame_status() == 0) {
                    drawHostPlayerUI(g);
                    g.drawImage(btn_back, btn_back_px, btn_back_py, btn_back_px + btn_back_sx, btn_back_py + btn_back_sy, 0, 0, 1020, 400, this);
                } else {
                    g.setFont(new Font("Kanit Bold", Font.PLAIN, 45));
                    g.setColor(Color.WHITE);
                    g.drawString(getGame().getStatus_desc(), cCanv.getWidth()/2-((45*getGame().getStatus_desc().length()/2)/2), getInsidePosition(0, cCanv.getHeight(), 50));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void drawHostPlayerUI(Graphics2D g){
        g.setFont(new Font("Kanit Light", Font.PLAIN, 20));

        Iterator<Player> ps = new ArrayList<>(Player.getPlayers()).iterator();

        int i = 0;
        while(ps.hasNext()){
            Player p = ps.next();

            g.setColor(getColor(p.getCharacterID()));
            String pname = p.getUsername();
            int px = getInsidePosition(0, cCanv.getWidth(), 5), py = getInsidePosition(0, cCanv.getHeight(), 25)+(70*i)+(5*i)-(90/2);
            g.drawImage(p.getPlayerImage(), px, py,px+70, py+70, p.getSpriteX(),p.getSpriteY(),p.getSpriteDX(),p.getSpriteDY(), this);
            g.drawString(pname, getInsidePosition(0, cCanv.getWidth(), 5)+70+25, getInsidePosition(0, cCanv.getHeight(), 25)+(70*i)+(5*i));
            i++;
        }
    }

    double scroll = 0;

    public double getScroll() {
        return scroll;
    }

    public void setScroll(double scroll) {
        this.scroll = scroll;
    }

    int i_hover = -1;
    int i_click = -1;

    public int getI_click() {
        return i_click;
    }

    public void setI_click(int i_click) {
        this.i_click = i_click;
    }

    public void drawJoinPlayerBoxes(Graphics ge){
        Graphics2D g = (Graphics2D) ge;

        BufferedImage bg = new BufferedImage(cCanv.getWidth(), getInsidePosition(0, cCanv.getHeight(), 65), BufferedImage.TYPE_INT_ARGB);
        Graphics2D gg = bg.createGraphics();

        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        gg.setRenderingHints(rh);
        gg.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
//        gg.setColor(Color.BLACK);
//        gg.fillRect(0,0, cCanv.getWidth(), getInsidePosition(0, cCanv.getHeight(), 65));

//        System.out.println(((100*6)+(5*6)) + " : " + (getInsidePosition(0, cCanv.getHeight(), 65)+(25*scroll)));

        int host_amount = NetworkDevices.getHostsIP().size();

//        if(25*scroll < 0 || getInsidePosition(0, cCanv.getHeight(), 65)+(25*scroll) < getInsidePosition(0, cCanv.getHeight(), 65))
        if(25*scroll < 0)
            scroll += 0.5;
//        System.out.println(getInsidePosition(0, cCanv.getHeight(), 65)+(25*scroll) + " : " + ((100*(host_amount))+(5*host_amount)));
        if(getInsidePosition(0, cCanv.getHeight(), 65)+(25*scroll) >= (100*(host_amount))+(5*host_amount))
            scroll -= 0.5;

        for(int i = 0; i < host_amount; i++){
            int x = getInsidePosition(0, cCanv.getWidth(), 7);
            int y = 0;

            int sx = getInsidePosition(0, cCanv.getWidth(), 93)-x, sy = 100;

            y = y + (sy*(i))+(5*i) - (int)(25*scroll);

            String ip = NetworkDevices.getHostIP(i);
            String[] data = NetworkDevices.getHostDetails(i);
            String room = data[2];
            String name = "("+data[0]+")";
            String amount = data[3]+"/"+data[4];
            String status;
            switch (data[6]){
                case "1":
                    status = "Starting";
                    break;
                case "2":
                    status = "Started";
                    break;
                case "3":
                    status = "Ended";
                    break;
                default:
                    status = "Waiting Players...";
                    break;
            }
            boolean isClick = i_click == i;
            boolean isHover = i_hover == i;

            BufferedImage bi = new BufferedImage(sx, sy, BufferedImage.TYPE_INT_ARGB_PRE);
            Graphics2D g2 = bi.createGraphics();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, ((!isClick && isHover ? 5 : 10)/100f)));
            if(!isClick && !isHover) g2.setColor(Color.BLACK);
            g2.fillRect(0,0, sx, sy);
            g2.dispose();

            gg.drawImage(bi,x,y, this);
            gg.setFont(new Font("Kanit Bold", Font.PLAIN, 30));

            gg.setColor(Color.black);
            gg.drawString(room, getInsidePosition(x, x+sx, 2), 30+getInsidePosition(y, y+sy, 12));
            gg.drawString(amount, getInsidePosition(x, x+sx, 98)-(30*(amount.length()/2)), 30+getInsidePosition(y, y+sy, 12));

            gg.setColor(Color.WHITE);
            gg.drawString(room, getInsidePosition(x, x+sx, 2), 30+getInsidePosition(y, y+sy, 10));
            gg.drawString(amount, getInsidePosition(x, x+sx, 98)-(30*(amount.length()/2)), 30+getInsidePosition(y, y+sy, 10));
            gg.setFont(new Font("Kanit Light", Font.PLAIN, 20));
            gg.setColor(Color.LIGHT_GRAY);
            gg.drawString(name, getInsidePosition(x, x+sx, 2) + (int)(((30 * room.length())/2) * 1.15), 30+getInsidePosition(y, y+sy, 10));
            gg.setColor(Color.CYAN);
            gg.drawString(status, getInsidePosition(x, x+sx, 2), (getInsidePosition(y, y+sy, 90))-10);
        }

        gg.dispose();
        g.drawImage(bg,0,getInsidePosition(0, cCanv.getHeight(), 15), this);
    }

    int frameTime = 0;

    public int getFrameTime() {
        return frameTime;
    }

    public void setFrameTime(int frameTime) {
        this.frameTime = frameTime;
    }

    /*
                0 = present
                1 = main
                2 = start-join
             */
    int game_status = 0;

    public int getGame_status() {
        return game_status;
    }

    public void setGame_status(int game_status) {
        this.game_status = game_status;
    }

//    ตัวแปรทิศทาง ความเร็วของฉาก
    int s_x = Math.random()>=0.5?-1:1, s_y = Math.random()>=0.5?-1:1;
    double s_vx = 0.5 + (new Random().nextDouble()*0.7), s_vy = 0.5 + (new Random().nextDouble()*0.7);

    public void run() {
        if(frameTime >= 120){
            if(game_status == 0 && scene.getOpacity() < 99){
                scene.setOpacity(scene.getOpacity()+2);
                if(scene.getOpacity() > 100)
                    scene.setOpacity(100);
                frameTime++;
            } else {
                if(game_status == 0){
                    game_status = 1;
                } else if(game_status == 1)  {
                    if(scene.getOpacity() != 100) scene.setOpacity(100);
                } else if(game_status == 2)  {
                    if(scene.getOpacity() != 40) scene.setOpacity(40);
                }
            }

            scene.moveForward(s_x*s_vx);
            scene.moveUp(s_y*s_vy);
            if(scene.getX() < 0 || scene.getBoundX() > scene.getSize_x()){
                s_x *= -1;
                if(scene.getX() < 0) scene.setX(0); else scene.setX(((scene.getSize_x()-cCanv.getWidth()))-1);
            }
            if(scene.getY() < 0 || scene.getBoundY() > scene.getSize_y()){
                s_y *= -1;
                if(scene.getY() < 0) scene.setY(0); else scene.setY(scene.getSize_y()-cCanv.getHeight()-1);
            }
        } else if(frameTime >= 80){
            opc -= opc > 0 ? 5 : 0;
            frameTime++;
        }
        else if(frameTime >= 10){
            if(opc < 50){
                l_py -= 2;
                opc += 20;
            } else {
                if(opc < 100){
                    l_py -= 1;
                    opc += 3;

                    if(opc > 100){
                        opc = 100;
                    }
                }
            }
            frameTime++;
        } else {
            frameTime++;
        }
    }

    public boolean is_btn_load = false;
    // ------------- ฟังก์ชันเช็คว่าเมาส์อยู่ในปุ่มมั้ย
    public int isMouseOnStart(int x, int y){
        if(game_status == 1){
            if((x >= b_cen_x && x <= b_cen_x+btn_sx) && (y >= btn_py && y <= btn_py+btn_sy)) return 0;
        } else if(game_status == 2) {
            if((x >= b_cen_x && x <= b_cen_x+btn_sx) && (y >= btn_py && y <= btn_py+btn_sy)) return 0;
            if((x >= b2_cen_x && x <= b2_cen_x+btn_sx) && (y >= btn_py && y <= btn_py+btn_sy)) {
                if(is_btn_load) return -1;
                if((i_click != -1 && i_click < NetworkDevices.getHostsIP().size()) || movements[4])
                    return 1;
            }
//            if((x >= b2_cen_x && x <= b2_cen_x+btn_sx) && (y >= btn_py && y <= btn_py+btn_sy) && (i_click != -1 && i_click < NetworkDevices.getHostsIP().size())) return 1;
            if((x >= btn_back_px && x <= btn_back_px+btn_back_sx) && (y >= btn_back_py && y <= btn_back_py+btn_back_sy)) return 2;
            int hn = checkHostNumber(x, y);
            i_hover = hn;
            if(hn != -1) return 3;

        } else if(game_status == 3) {
            if(getGame().getGame_status() != 1){
                if((x >= btn_back_px && x <= btn_back_px+btn_back_sx) && (y >= btn_back_py && y <= btn_back_py+btn_back_sy)) return 0;
                if((x >= b_cen_x && x <= b_cen_x+btn_sx) && (y >= btn_py && y <= btn_py+btn_sy) && Player.getPlayers().size() >= Utils.allowed_start) return 1;
            }
        } else if(game_status == 4) {
            if((x >= btn_back_px && x <= btn_back_px+btn_back_sx) && (y >= btn_back_py && y <= btn_back_py+btn_back_sy)) return 0;
        }

        return -1;
    }

    public int checkHostNumber(int x, int y){
        int host_amount = NetworkDevices.getHostsIP().size();
        for(int i = 0; i < host_amount; i++) {
            int cx = getInsidePosition(0, cCanv.getWidth(), 7);

            int sx = getInsidePosition(0, cCanv.getWidth(), 93) - cx, sy = 100;

            int cy = (sy * (i)) + (5 * i) - (int) (25 * scroll);

            if ((x >= 0+cx && x <= sx+cx) && (y >= getInsidePosition(0, cCanv.getHeight(), 15)+cy && y <= getInsidePosition(0, cCanv.getHeight(), 15)+cy+((sy*(i+1))+(5*i))))
                return i;
        }
        return -1;
    }

    public Canvas getcCanv() {
        return cCanv;
    }

    public static Present getPresent(){
        return ps;
    }
}
