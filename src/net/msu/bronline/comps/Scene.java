package net.msu.bronline.comps;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Scene {
    JFrame cFrame;
    BufferedImage simg = ImageIO.read(new File(System.getProperty("user.dir")+ File.separator+"res"+File.separator+"Paper-to-Map.jpg"));
    public Scene(JFrame frame) throws IOException {
        cFrame = frame;
    }

    int x = 0, y = 0;
    float size = 1;
    int size_x = 2000, size_y = 2000;
    int opc = 0;

    public int getOpacity() {
        return opc;
    }

    public void setOpacity(int opc) {
        this.opc = opc;
    }

    public BufferedImage getImg(){
        return simg;
    }

    
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public int getBoundX(){
        return x+cFrame.getWidth();
    }
    public int getBoundY(){
        return y+cFrame.getHeight();
    }

    public int getSize_x() {
        return size_x;
    }

    public int getSize_y() {
        return size_y;
    }
}