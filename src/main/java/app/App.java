package app;

import app.modules.gui.CheckerFrame;

import javax.swing.*;

public class App
{
    public static void main(String[] args)
    {
        CheckerFrame frame = new CheckerFrame();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
    }
}
