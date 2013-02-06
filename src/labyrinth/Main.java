package labyrinth;

public class Main {
    public static void main(String[] args) {
//        Labyrinth lab = MazeGenerationAlgorithms.recBackTrack(15);
        Labyrinth lab = MazeGenerationAlgorithms.huntAndKill(15);
//        Labyrinth lab = MazeGenerationAlgorithms.divideEtImpera(15);
//        ConsoleAdapter.visualize(lab);
        
//        ConsoleAdapter.visualize(lab);
        
//        System.out.println(diametr.get(0).x + " " + diametr.get(0).y);
//        System.out.println(diametr.get(1).x + " " + diametr.get(1).y);
        
        lab.initializeGame();
        
        MainFrame mfr = new MainFrame();
//        mfr.initializeLabyrinth(lab, new SimpleLabyrinthVisualizer());
        mfr.initializeLabyrinth(lab, new CurveLabyrinthVisualizer());
    }
}
