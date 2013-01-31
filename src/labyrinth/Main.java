package labyrinth;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
//        Labyrinth lab = MazeGenerationAlgorithms.recBackTrack(10);
        Labyrinth lab = MazeGenerationAlgorithms.huntAndKill(10);
//        Labyrinth lab = MazeGenerationAlgorithms.divideEtImpera(75);
//        ConsoleAdapter.visualize(lab);
        
        
//        ConsoleAdapter.visualize(lab);
        ArrayList<Coordinate> diametr = lab.findDiametr();
//        
//        System.out.println(diametr.get(0).x + " " + diametr.get(0).y);
//        System.out.println(diametr.get(1).x + " " + diametr.get(1).y);
        
        lab.initializeGame();
        
        MainFrame mfr = new MainFrame(lab, new SimpleLabyrinthVisualizer());
    }
}
