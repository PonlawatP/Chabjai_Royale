package net.msu.bronline.guis;

import net.msu.bronline.comps.Scene;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Present extends JPanel implements Runnable {
    JFrame cFrame;
    Thread preThread = new Thread(this);

    Scene scene;
    public Present(JFrame cFrame) throws IOException {
        this.cFrame = cFrame;
        scene = new Scene(cFrame);
        setSize(cFrame.getSize());
        setBackground(Color.white);

        setVisible(true);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setSize(cFrame.getSize());
            }
        });

        preThread.start();
    }

//    รับโลโก้มาแปลงขนาด แล้วเอามาวางตรงกลาง
    BufferedImage logo = ImageIO.read(new File(System.getProperty("user.dir")+ File.separator+"res"+File.separator+"logo.png"));
    float size = .6f;
    int opc = 0;
    int l_sx = (int)(537*size), l_sy = (int)(417*size); //ขนาดภาพ
    int l_px = 0, l_py = 20; //ตำแหน่งภาพ


//    method แปลงความใสภาพ
    BufferedImage getOpacityImg(BufferedImage img, int width, int height, int opacity) throws IOException {
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = bi.createGraphics();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (opacity/100f)));
        g.drawImage(img, 0,0,width ,height, null);
        g.dispose();

        return bi;
    }

    @Override
    protected void paintComponent(Graphics ge) {
        int cen_x = (getWidth()/2)-(l_sx/2), cen_y = (getHeight()/2)-(l_sy/2);
        Graphics2D g = (Graphics2D) ge;
        g.setColor(Color.WHITE);
        g.fillRect(0,0, getWidth(), getHeight());

        try {
            g.drawImage(getOpacityImg(scene.getImg(), scene.getSize_x(), scene.getSize_y(), scene.getOpacity()), scene.getX(), scene.getY(),scene.getBoundX(),scene.getBoundY(),0,0,scene.getBoundX(), scene.getBoundY(),this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

//        วาด Logo
        try {
            g.drawImage(getOpacityImg(logo, l_sx, l_sy, opc), l_px+cen_x,l_py+cen_y, this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    int frameTime = 0;
    @Override
    public void run() {
//        System.out.println("frame: "+frameTime);
//        animation section

        if(frameTime >= 60){
            if(scene.getOpacity() < 60){
                scene.setOpacity(scene.getOpacity()+3);
                if(scene.getOpacity() > 60)
                    scene.setOpacity(60);
            }
        } else if(frameTime >= 50){
            opc -= opc > 0 ? 20 : 0;
        }
        else if(frameTime >= 10){
            if(opc < 50){
                l_py -= 2;
                opc += 20;
            } else {
                if(opc < 100){
                    l_py -= 1;
                    opc += 3;

                    if(opc > 100)
                        opc = 100;
                }
            }
        }

        frameTime++;
        repaint();
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        run();
    }
}
