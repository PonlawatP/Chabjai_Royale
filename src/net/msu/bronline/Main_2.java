package net.msu.bronline;

import net.msu.bronline.funcs.StartMenu;

import java.io.IOException;

public class Main_2 {

    public static void main(String[] args) {
        try{
            StartMenu sm = new StartMenu("Player 2", false);
        }catch (IOException ex){
            System.out.println("File Missing?");
        }
    }
}