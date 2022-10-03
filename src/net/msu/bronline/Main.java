package net.msu.bronline;

import net.msu.bronline.funcs.StartMenu;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try{
            StartMenu sm = new StartMenu();
        }catch (IOException ex){
            System.out.println("File Missing?");
        }
    }
}
