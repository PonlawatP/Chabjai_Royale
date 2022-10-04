package net.msu.bronline.guis;

import net.msu.bronline.comps.Scene;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Present extends JPanel {
    JFrame cFrame;
    Canvas cCanv;
    Scene scene;
    public Present(JFrame cFrame, Canvas canv) throws IOException {
        this.cFrame = cFrame;
        this.cCanv = canv;
        scene = new Scene(cFrame, cCanv);
        setSize(cFrame.getSize());
        setPreferredSize(cFrame.getPreferredSize());
        setBackground(Color.white);
        setVisible(true);

    }

//    รับโลโก้มาแปลงขนาด แล้วเอามาวางตรงกลาง
    BufferedImage logo = ImageIO.read(new File(System.getProperty("user.dir")+ File.separator+"res"+File.separator+"logo.png"));
    float size = .6f;
    int opc = 0;
    int l_sx = (int)(537*size), l_sy = (int)(417*size); //ขนาดภาพ
    int l_px = 0, l_py = 20; //ตำแหน่งภาพ

//    ปุ่ม
    BufferedImage btn_play = ImageIO.read(new File(System.getProperty("user.dir")+ File.separator+"res"+File.separator+"btn_play.png"));
    BufferedImage btn_host = ImageIO.read(new File(System.getProperty("user.dir")+ File.separator+"res"+File.separator+"btn_host.png"));
    BufferedImage btn_join = ImageIO.read(new File(System.getProperty("user.dir")+ File.separator+"res"+File.separator+"btn_join.png"));
    BufferedImage btn_join_den = ImageIO.read(new File(System.getProperty("user.dir")+ File.separator+"res"+File.separator+"btn_join_den.png"));
    BufferedImage btn_back = ImageIO.read(new File(System.getProperty("user.dir")+ File.separator+"res"+File.separator+"btn_back.png"));

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
                btn_py = cCanv.getHeight()-200;

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

                g.drawImage(btn_back, btn_back_px,btn_back_py, btn_back_px+btn_back_sx,btn_back_py+btn_back_sy,0,0,1020,400,this);

                g.drawImage(btn_host, b_cen_x,btn_py, b_cen_x+btn_sx,btn_py+btn_sy,0,0,480,160,this);
                g.drawImage(btn_join_den, b2_cen_x,btn_py, b2_cen_x+btn_sx,btn_py+btn_sy,0,0,480,160,this);
            }
            else if(game_status == 3){
                btn_back_px = 15;
                btn_back_py = 20;
                b_cen_x = (cCanv.getWidth()/2)-(btn_sx/2);
                b_cen_y = (cCanv.getHeight()/2)-(btn_sy/2);
                btn_py = cCanv.getHeight()-100;

                g.drawImage(btn_back, btn_back_px,btn_back_py, btn_back_px+btn_back_sx,btn_back_py+btn_back_sy,0,0,1020,400,this);
                g.drawImage(btn_play, b_cen_x,btn_py, b_cen_x+btn_sx,btn_py+btn_sy,0,0,480,160,this);
            }
            else if(game_status == 4){
                btn_back_px = 15;
                btn_back_py = 20;

                g.drawImage(btn_back, btn_back_px,btn_back_py, btn_back_px+btn_back_sx,btn_back_py+btn_back_sy,0,0,1020,400,this);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
