package labyrinth;

import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        IMazeGenerator gen = new HuntAndKillGenerator();
        MainFrame mfr = new MainFrame();
        mfr.setOptions(gen, new BasicLabyrinthVisualizer());
        mfr.initializeLabyrinth();
        mfr.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mfr.setSize(500, 500);
        mfr.setVisible(true);
    }
}