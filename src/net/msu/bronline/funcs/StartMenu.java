package net.msu.bronline.funcs;

import net.msu.bronline.guis.Present;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class StartMenu extends JFrame {
    Present ps;
    public StartMenu() throws IOException {
        setSize(new Dimension(1280,720));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        ps = new Present(this);

        setContentPane(ps);
        setVisible(true);
    }

}
