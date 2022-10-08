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
    boolean ingame = false;
    BufferedImage simg = ImageIO.read(getClass().getClassLoader().getResourceAsStream("imgs/m_full.png"));
    BufferedImage simg1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("imgs/m_floor.png"));
    BufferedImage simg2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("imgs/m_plant.png"));
    public Scene(JFrame frame, Canvas canv) throws IOException {
        cFrame = frame;
        cCanv = canv;
    }
    public Scene(JFrame frame, Canvas canv, boolean ingame) throws IOException {
        cFrame = frame;
        cCanv = canv;
        this.ingame = ingame;
    }

    boolean mouse_inp = false;
    int m_x = 0, m_y = 0;
    public void updateMouse(int x, int y){
        if(!mouse_inp) mouse_inp = true;
        m_x = x;
        m_y = y;
    }

    double x = 0, y = 0;
    double size = 1;
    int size_x = 2000, size_y = 2000;
    int opc = 0;

    public void setSize(float size) {
        this.size = size;
    }

    public double getSize() {
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
    public BufferedImage getImg(boolean plantorfloor){
        return plantorfloor ? simg2 : simg1;
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
//        return (int)x;
        int p = (int)(x-(cCanv.getWidth()-(cCanv.getWidth()*getSize())));
        double posmx = ((double) m_x-(cCanv.getWidth()/2))/(cCanv.getWidth()/2);
        return ingame ? (int)(p + (posmx*15*getSize())) : (int)x;
    }
    public int getY(){
//        return (int)y;
        int p = (int)(y-(cCanv.getHeight()-(cCanv.getHeight()*getSize())));
        double posmy = ((double) m_y-(cCanv.getHeight()/2))/(cCanv.getHeight()/2);
        return ingame ? (int)(p + (posmy*15*getSize())) : (int)y;
    }
    public int getBoundX(){
        double p = x+(cCanv.getWidth()-((cCanv.getWidth()*getSize())-cCanv.getWidth()));
        double posmx = ((double) m_x-(cCanv.getWidth()/2))/(cCanv.getWidth()/2);
        return ingame ? (int)(p + (posmx*15*getSize())) : (int)x+cCanv.getWidth();
//        return (int)(p*getZoomCanvas());
    }
    public int getBoundY(){
        double a = (cCanv.getHeight()*(getSize()*-1+2));
        double p = y+a;
        double posmy = ((double) m_y-(cCanv.getHeight()/2))/(cCanv.getHeight()/2);
        return ingame ? (int)(p + (posmy*15*getSize())) : (int)y+cCanv.getHeight();
//        return (int)(p*getZoomCanvas());
    }

    public int getSize_x() {
        return size_x;
    }

    public int getSize_y() {
        return size_y;
    }
}