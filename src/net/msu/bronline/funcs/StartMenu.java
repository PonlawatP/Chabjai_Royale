package net.msu.bronline.funcs;

import net.msu.bronline.guis.Game;
import net.msu.bronline.guis.Present;
import net.msu.bronline.network.NetworkDevices;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Thread.sleep;
import static net.msu.bronline.funcs.Utils.dev;
import static net.msu.bronline.funcs.Utils.runServerFinder;
import static net.msu.bronline.guis.Game.getGame;
import static net.msu.bronline.guis.Present.getPresent;

public class StartMenu {
    public StartMenu(String username, boolean host) throws IOException {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame();

                Canv canvas = null;
                try {
                    canvas = new Canv(frame, username, host);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                frame.add(canvas);
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

                frame.setTitle("BR : FinalLab  |  "+username);

                frame.setPreferredSize(new Dimension(1280, 720));
                frame.setMinimumSize(new Dimension((int)(1280*0.8), (int)(720*0.8)));
                frame.pack();
                frame.setFocusable(true);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

                canvas.start();
            }
        });
    }

}

class Canv extends Canvas {
    JFrame cFrame;
    Present ps;
    int m_x = 0, m_y = 0;
    //w,a,s,d,shift,tab,lClick,rClick,c
    boolean[] movements = {false,false,false,false,false,false,false,false,false};

    int cCur = Cursor.DEFAULT_CURSOR;

    public void updateMouse(int x, int y){
        m_x = x;
        m_y = y;
    }

    public void updateCursor(int c){
        cCur = c;
        setCursor(Cursor.getPredefinedCursor(c));
    }
    public Canv(JFrame frame, String username, boolean host) throws IOException {
        cFrame = frame;
        ps = new Present(cFrame, this, username);
        new Game(cFrame, this, movements, username, host);

        ps.setGame_status(1);
        ps.setFrameTime(120);
        ps.getScene().setOpacity(40);

        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if(ps.getGame_status() >= 5)
                    getGame().getScene().setSize((float) (getGame().getScene().getSize()+((e.getWheelRotation()*-1)*.05)));
                else{
                    if(ps.getGame_status() == 2){
                        ps.setScroll(ps.getScroll()+e.getWheelRotation());
                    }
                }
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(ps.getGame_status() < 5) {
                    int cl = ps.isMouseOnStart(e.getX(), e.getY());
                    if (cl != -1) {
                        if (ps.getGame_status() == 1) {
                            if (cl == 0) { //into game interface
                                ps.setGame_status(2);
                                ps.setScroll(0);
                                runServerFinder();
                            }
                        } else if (ps.getGame_status() == 2) {
                            if (cl == 0) { //host
                                ps.setGame_status(3);
                                getGame().setHosting(true);
                                getGame().startMode();
                            }
                            if (cl == 1) { //join
                                ps.setGame_status(4);
                                getGame().setHosting(false);
                                String ip = NetworkDevices.getHostIP(ps.getI_click());
                                System.out.println("serverip: " + ip);
                                getGame().setIp(ip);
                                getGame().updateRoom(NetworkDevices.getHostDetails(ps.getI_click()));

                                getGame().startMode();
                            }
                            if (cl == 2) {
                                ps.setGame_status(1);
                            }

                            if(cl == 3){ // server
                                int hn = ps.checkHostNumber(e.getX(), e.getY());
                                ps.setI_click(hn);
                            }
                        } else if (ps.getGame_status() == 3) { //host page
                            if (cl == 0) { //back
                                ps.setGame_status(2);
                                getGame().stopMode();
                                runServerFinder();
                            }
                            if (cl == 1) {
//                                ps.setGame_status(5);
//                                getGame().setGame_status(2);
                                getGame().startGame();
                            }
                        } else if (ps.getGame_status() == 4) {
                            if (cl == 0) {
                                ps.setGame_status(2);
                                getGame().stopMode();
                                runServerFinder();
                            }
                        }
                    } else {
                        if(ps.getGame_status() == 2){
                            ps.setI_click(-1);
                        }
                    }
                } else {
                    int cl = getGame().isMouseOnStart(e.getX(), e.getY());
                    if (cl != -1) {
//                        if (getGame().getGame_status() == 0) {
//                            if (cl == 0) getGame().setGame_status(1);
//                        }
                    } else {
                        if (getGame().getGame_status() == 2) {
                            if(e.getButton() == MouseEvent.BUTTON1) {
                                movements[6] = true;
                                getGame().getPlayerOwn().setFireTrigger(true);
                            }
                            if(e.getButton() == MouseEvent.BUTTON3) {
                                movements[7] = true;
                            }
                        }
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(ps.getGame_status() < 5) return;
                if(e.getButton() == MouseEvent.BUTTON1) {
                    movements[6] = false;
                    getGame().getPlayerOwn().setFireTrigger(false);
                }
                if(e.getButton() == MouseEvent.BUTTON3) movements[7] = false;
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                updateMouse(e.getX(), e.getY());
                if(getGame().getPlayerOwn()!=null) getGame().getPlayerOwn().updateMouse(e.getX(), e.getY());
                if(ps.getGame_status() < 5){
                    if(ps.isMouseOnStart(e.getX(), e.getY()) != -1)
                        updateCursor(Cursor.HAND_CURSOR);
                    else
                        updateCursor(Cursor.DEFAULT_CURSOR);
                } else {
                    if(getGame().isMouseOnStart(e.getX(), e.getY()) != -1)
                        updateCursor(Cursor.HAND_CURSOR);
                    else
                        updateCursor(Cursor.DEFAULT_CURSOR);
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                updateMouse(e.getX(), e.getY());
                if(getGame().getPlayerOwn()!=null)getGame().getPlayerOwn().updateMouse(e.getX(), e.getY());
                if(ps.getGame_status() < 5){
                    if(ps.isMouseOnStart(e.getX(), e.getY()) != -1)
                        updateCursor(Cursor.HAND_CURSOR);
                    else
                        updateCursor(Cursor.DEFAULT_CURSOR);
                } else {
                    if(getGame().isMouseOnStart(e.getX(), e.getY()) != -1)
                        updateCursor(Cursor.HAND_CURSOR);
                    else
                        updateCursor(Cursor.DEFAULT_CURSOR);
                }
            }
        });

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_F12) dev = !dev;
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE && getPresent().getGame_status() == 5) getGame().stopMode();

                if(ps.getGame_status() < 5) return;
                if(e.getKeyCode() == KeyEvent.VK_W) movements[0] = true;
                if(e.getKeyCode() == KeyEvent.VK_A) movements[1] = true;
                if(e.getKeyCode() == KeyEvent.VK_S) movements[2] = true;
                if(e.getKeyCode() == KeyEvent.VK_D) movements[3] = true;
                if(e.getKeyCode() == KeyEvent.VK_SHIFT) movements[4] = true;
                if(e.getKeyCode() == KeyEvent.VK_TAB) movements[5] = true;
                if(e.getKeyCode() == KeyEvent.VK_C) movements[8] = true;
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(ps.getGame_status() < 5) return;
                if(e.getKeyCode() == KeyEvent.VK_W) movements[0] = false;
                if(e.getKeyCode() == KeyEvent.VK_A) movements[1] = false;
                if(e.getKeyCode() == KeyEvent.VK_S) movements[2] = false;
                if(e.getKeyCode() == KeyEvent.VK_D) movements[3] = false;
                if(e.getKeyCode() == KeyEvent.VK_SHIFT) movements[4] = false;
                if(e.getKeyCode() == KeyEvent.VK_TAB) movements[5] = false;
                if(e.getKeyCode() == KeyEvent.VK_C) movements[8] = false;
            }
        });
    }
    private Thread thread;
    private AtomicBoolean keepRendering = new AtomicBoolean(true);

    public long getCurrentTime() {
        return System.nanoTime() / 1000000;
    }

    public void stop() {
        if (thread != null) {
            keepRendering.set(false);
            try {
                thread.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * The Game Loop
     */
    long fps = 0;
    public void start() {
        if (thread != null) {
            stop();
        }

        keepRendering.set(true);
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                long now = getCurrentTime();
                long gameTime = getCurrentTime();

                long frames = 0;
                fps = 0;

                long lastFpsCount = getCurrentTime();

                long updateRate = 1000 / 60; // Update 60 times per second

                createBufferStrategy(3);
//
                do {
                    BufferStrategy bs = getBufferStrategy();
                    while (bs == null) {
//                        System.out.println("get buffer");
                        bs = getBufferStrategy();
                    }
                    while (keepRendering.get()) {
                        now = getCurrentTime();
//                        System.out.println("now: " + now + " update: " + updateRate + " = " + (now + updateRate) + " gameT: " + gameTime);
                        while (now + updateRate > gameTime) {
                            // update your logic here
                                update();
                            gameTime += updateRate;
                            // render
                            do {
                                do {
                                    Graphics g = bs.getDrawGraphics();
                                    render(g);
                                    g.dispose();

                                    frames++;
//                                    System.out.println(frames);
                                    if (now - lastFpsCount > 1000) {
                                        fps = frames;
                                        frames = 0;
                                        lastFpsCount = getCurrentTime();
//                                        cFrame.setTitle("FPS: " + fps);
                                    }
                                }
                                while (bs.contentsRestored());
                                bs.show();
                            }
                            while (bs.contentsLost());
                        }
                    }
                } while (keepRendering.get());
            }
        });
        thread.start();
    }

    public void update() {
        if(ps.getGame_status() < 5) ps.run();
        else getGame().run(m_x, m_y);
    }

    /**
     * Render the game
     */
    public void render(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHints(rh);

        boolean darkscene = ps.getGame_status() >= 2 && ps.getGame_status() <= 4;
        if(darkscene){
            g.setColor(Color.BLACK);
        } else {
            g.setColor(Color.WHITE);
        }
        g.fillRect(0,0, cFrame.getWidth(), cFrame.getHeight());

        if(ps.getGame_status() < 5) ps.draw(g);
        else getGame().draw(g);

        if(dev){
            g.setFont(new Font("Kanit Light", Font.PLAIN, 12));
            g.setColor((!darkscene) ? Color.BLACK : Color.CYAN);
            g.drawString("FPSection: " + fps, 10, 20);
            g.drawString("w: " + cFrame.getWidth() + " h: " + cFrame.getHeight(), 10, 40);
            g.drawString("scene: " + ps.getGame_status(), 10, 60);
            g.drawString("scene_w: " + getWidth() + " scene_h: " + getHeight(), 10, 80);
            g.drawString("Mouse: [" + m_x + " : " + m_y + "] " + Cursor.getPredefinedCursor(cCur).getName(), 10, 100);
            g.drawString("zoom: " + (getGame().getScene() != null ? getGame().getScene().getSize() : "no)"), 10, 120);
            g.drawString("player: [" + (getGame().getPlayerOwn() != null ? getGame().getPlayerOwn().getX() + " : " + getGame().getPlayerOwn().getY() : "null") + "]", 10, 160);
        }
    }
}