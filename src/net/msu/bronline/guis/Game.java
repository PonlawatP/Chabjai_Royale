package net.msu.bronline.guis;

import net.msu.bronline.comps.Scene;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Game extends JPanel {
    JFrame cFrame;
    Canvas cCanv;
    Scene scene;

    boolean[] movements;
    public Game(JFrame cFrame, Canvas canv, boolean[] movements) throws IOException {
        this.cFrame = cFrame;
        this.cCanv = canv;
        this.movements = movements;
        scene = new Scene(cFrame, cCanv, true);
        setSize(cFrame.getSize());
        setPreferredSize(cFrame.getPreferredSize());
        setBackground(Color.white);
        setVisible(true);

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
//        g.drawImage(scene.getImg(true), 0,0, cCanv.getWidth(), cCanv.getHeight(),scene.getX(), scene.getY(),scene.getBoundX(), scene.getBoundY(),this);

        g.setColor(Color.RED);
//        g.drawRoundRect(0,0, 10, 10, 3, 3);

        if(game_status == 0){
            b_cen_x = (cCanv.getWidth()/2)-(btn_sx/2);
            btn_py = cCanv.getHeight()-100;

            g.drawImage(btn_play, b_cen_x,btn_py, b_cen_x+btn_sx,btn_py+btn_sy,0,0,480,160,this);
        }
    }

    int frameTime = 0;
    int game_status = 1;

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
        if(getGame_status() == 1){
            if(movements[0]) scene.moveUp(-1*v_speed);
            if(movements[1]) scene.moveForward(-1*v_speed);
            if(movements[2]) scene.moveUp(1*v_speed);
            if(movements[3]) scene.moveForward(1*v_speed);
            if(movements[4]) v_speed = 3; else v_speed = 2;

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
        } else if(game_status == 1){

        }

        return -1;
    }
}