package net.msu.bronline.comps;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Scene {
    JFrame cFrame;
    Canvas cCanv;
    BufferedImage simg = ImageIO.read(new File(System.getProperty("user.dir")+ File.separator+"res"+File.separator+"m_full.png"));
    public Scene(JFrame frame, Canvas canv) throws IOException {
        cFrame = frame;
        cCanv = canv;
    }

    double x = 0, y = 0;
    float size = 1;
    int size_x = 2000, size_y = 2000;
    int opc = 0;

    public void setSize(float size) {
        this.size = size;
    }

    public float getSize() {
        return size;
    }

    public int getOpacity() {
        return opc;
    }

    public void setOpacity(int opc) {
        this.opc = opc;
    }

    public BufferedImage getImg(){
        return simg;
    }

    public void moveForward(double x){
        this.x+=x;
    }
    public void moveUp(double y){
        this.y+=y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZoom(){
        return 1280f/cFrame.getWidth();
    }
    public double getZoomCanvas(){
        return (1280f-16f)/(cCanv.getWidth());
    }

    public int getX(){
        return (int)x;
//        return (int)(x*getZoom());
    }
    public int getY(){
        return (int)y;
//        return (int)(y*getZoom());
    }
    public int getBoundX(){
        int p = getX()+cCanv.getWidth();
        return (int)p;
//        return (int)(p*getZoomCanvas());
    }
    public int getBoundY(){
        int p = getY()+cCanv.getHeight();
        return (int)p;
//        return (int)(p*getZoomCanvas());
    }

    public int getSize_x() {
        return size_x;
    }

    public int getSize_y() {
        return size_y;
    }
}