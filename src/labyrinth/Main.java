package labyrinth;

public class Main {
    public static void main(String[] args) {
//        Labyrinth lab = MazeGenerationAlgorithms.recBackTrack(12);
        Labyrinth lab = MazeGenerationAlgorithms.huntAndKill(12);
//        Labyrinth lab = MazeGenerationAlgorithms.divideEtImpera(12);
        
//        ConsoleAdapter.visualize(lab);
        
//        System.out.println(diametr.get(0).x + " " + diametr.get(0).y);
//        System.out.println(diametr.get(1).x + " " + diametr.get(1).y);
        
        lab.initializeGame();
        MainFrame mfr = new MainFrame();
        
        
//        mfr.initializeLabyrinth(lab, new SimpleLabyrinthVisualizer());
        mfr.initializeLabyrinth(lab, new LineLabyrinthVisualizer());
//        mfr.initializeLabyrinth(lab, new CurveLabyrinthVisualizer());
//        mfr.initializeLabyrinth(lab, new TriangleLabyrinthVisualizer());
        
        
    }
}
