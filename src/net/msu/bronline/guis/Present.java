package net.msu.bronline.guis;

import net.msu.bronline.comps.Scene;

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

import static net.msu.bronline.funcs.Utils.getInsidePosition;

public class Present extends JPanel {
    JFrame cFrame;
    Canvas cCanv;
    Scene scene;
    String username;
    public Present(JFrame cFrame, Canvas canv, String username) throws IOException {
        this.cFrame = cFrame;
        this.cCanv = canv;
        this.username = username;
        scene = new Scene(cFrame, cCanv);
        setSize(cFrame.getSize());
        setPreferredSize(cFrame.getPreferredSize());
        setBackground(Color.white);
        setVisible(true);
    }

//    รับโลโก้มาแปลงขนาด แล้วเอามาวางตรงกลาง
//    ImageIcon ii = new ImageIcon(getClass().getClassLoader().getResource("imgs/logo.png"));
    BufferedImage logo = ImageIO.read(new File(getClass().getClassLoader().getResource("imgs/logo.png").getPath()));
    float size = .6f;
    int opc = 0;
    int l_sx = (int)(537*size), l_sy = (int)(417*size); //ขนาดภาพ
    int l_px = 0, l_py = 20; //ตำแหน่งภาพ

//    ปุ่ม
    BufferedImage btn_play = ImageIO.read(new File(getClass().getClassLoader().getResource("imgs/btn_play.png").getPath()));
    BufferedImage btn_host = ImageIO.read(new File(getClass().getClassLoader().getResource("imgs/btn_host.png").getPath()));
    BufferedImage btn_join = ImageIO.read(new File(getClass().getClassLoader().getResource("imgs/btn_join.png").getPath()));
    BufferedImage btn_join_den = ImageIO.read(new File(getClass().getClassLoader().getResource("imgs/btn_join_den.png").getPath()));
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
                g.drawImage(btn_join_den, b2_cen_x,btn_py, b2_cen_x+btn_sx,btn_py+btn_sy,0,0,480,160,this);
            }
            else if(game_status == 3){
                btn_back_px = 15;
                btn_back_py = 20;
                b_cen_x = (cCanv.getWidth()/2)-(btn_sx/2);
                b_cen_y = (cCanv.getHeight()/2)-(btn_sy/2);
                btn_py = cCanv.getHeight()-100;

                g.setFont(new Font("Kanit Bold", Font.PLAIN, 45));
                g.setColor(Color.WHITE);
                String ss = "\"" + username +"'s Room" + "\" Hosting";
                g.drawString(ss, cCanv.getWidth()/2-((45*ss.length()/2)/2), getInsidePosition(0, cCanv.getHeight(), 10));

                g.drawImage(btn_back, btn_back_px,btn_back_py, btn_back_px+btn_back_sx,btn_back_py+btn_back_sy,0,0,1020,400,this);
                g.drawImage(btn_play, b_cen_x,btn_py, b_cen_x+btn_sx,btn_py+btn_sy,0,0,480,160,this);
            }
            else if(game_status == 4){
                btn_back_px = 15;
                btn_back_py = 20;

                g.setFont(new Font("Kanit Bold", Font.PLAIN, 45));
                g.setColor(Color.WHITE);
                String ss = username +"'s Room";
                g.drawString(ss, cCanv.getWidth()/2-((45*ss.length()/2)/2), getInsidePosition(0, cCanv.getHeight(), 10));

                g.setFont(new Font("Kanit Light", Font.PLAIN, 20));
                String ss2 = "Starting";
                g.drawString(ss2, cCanv.getWidth()/2-((20*ss2.length()/2)/2), getInsidePosition(0, cCanv.getHeight(), 15));

                g.setFont(new Font("Kanit Bold", Font.PLAIN, 45));
                g.setColor(Color.WHITE);
                String ss3 = "Starting in 5";
                g.drawString(ss3, cCanv.getWidth()/2-((45*ss3.length()/2)/2), getInsidePosition(0, cCanv.getHeight(), 50));

                g.drawImage(btn_back, btn_back_px,btn_back_py, btn_back_px+btn_back_sx,btn_back_py+btn_back_sy,0,0,1020,400,this);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void drawJoinPlayerBoxes(Graphics ge){
        int x = getInsidePosition(0, cCanv.getWidth(), 7), y = getInsidePosition(0, cCanv.getHeight(), 15);
        int sx = getInsidePosition(0, cCanv.getWidth(), 93)-x, sy = 100;
        String room = username + "'s Room";
        String name = "("+username+")";
        String amount = "1/16";
        String status = "Waiting Players...";
        boolean isClick = false;
        boolean isHover = false;

        Graphics2D g = (Graphics2D) ge;
        BufferedImage bi = new BufferedImage(sx, sy, BufferedImage.TYPE_INT_ARGB_PRE);
        Graphics2D g2 = bi.createGraphics();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, ((!isClick && isHover ? 5 : 10)/100f)));
        if(!isClick && !isHover) g2.setColor(Color.BLACK);
        g2.fillRect(0,0, sx, sy);
        g2.dispose();

        g.drawImage(bi,x,y, this);
        g.setFont(new Font("Kanit Bold", Font.PLAIN, 30));

        g.setColor(Color.black);
        g.drawString(room, getInsidePosition(x, x+sx, 2), 30+getInsidePosition(y, y+sy, 12));
        g.drawString(amount, getInsidePosition(x, x+sx, 98)-(30*(amount.length()/2)), 30+getInsidePosition(y, y+sy, 12));

        g.setColor(Color.WHITE);
        g.drawString(room, getInsidePosition(x, x+sx, 2), 30+getInsidePosition(y, y+sy, 10));
        g.drawString(amount, getInsidePosition(x, x+sx, 98)-(30*(amount.length()/2)), 30+getInsidePosition(y, y+sy, 10));
        g.setFont(new Font("Kanit Light", Font.PLAIN, 20));
        g.setColor(Color.LIGHT_GRAY);
        g.drawString(name, getInsidePosition(x, x+sx, 2) + (int)(((30 * room.length())/2) * 1.15), 30+getInsidePosition(y, y+sy, 10));
        g.setColor(Color.CYAN);
        g.drawString(status, getInsidePosition(x, x+sx, 2), (getInsidePosition(y, y+sy, 90))-10);
    }

    int frameTime = 0;
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
    double s_vx = 0.5 + new Random().nextDouble(0.7), s_vy = 0.5 + new Random().nextDouble(0.7);

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

    // ------------- ฟังก์ชันเช็คว่าเมาส์อยู่ในปุ่มมั้ย
    public int isMouseOnStart(int x, int y){
        if(game_status == 1){
            if((x >= b_cen_x && x <= b_cen_x+btn_sx) && (y >= btn_py && y <= btn_py+btn_sy)) return 0;
        } else if(game_status == 2) {
            if((x >= b_cen_x && x <= b_cen_x+btn_sx) && (y >= btn_py && y <= btn_py+btn_sy)) return 0;
            if((x >= b2_cen_x && x <= b2_cen_x+btn_sx) && (y >= btn_py && y <= btn_py+btn_sy)) return 1;
            if((x >= btn_back_px && x <= btn_back_px+btn_back_sx) && (y >= btn_back_py && y <= btn_back_py+btn_back_sy)) return 2;
        } else if(game_status == 3) {
            if((x >= btn_back_px && x <= btn_back_px+btn_back_sx) && (y >= btn_back_py && y <= btn_back_py+btn_back_sy)) return 0;
            if((x >= b_cen_x && x <= b_cen_x+btn_sx) && (y >= btn_py && y <= btn_py+btn_sy)) return 1;
        }else if(game_status == 4) {
            if((x >= btn_back_px && x <= btn_back_px+btn_back_sx) && (y >= btn_back_py && y <= btn_back_py+btn_back_sy)) return 0;
        }

        return -1;
    }
}
