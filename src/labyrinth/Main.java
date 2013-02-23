package labyrinth;

public class Main {
    public static void main(String[] args) {
        IMazeGenerator gen = new HuntAndKillGenerator();
        
        MainFrame mfr = new MainFrame();
        
        
        mfr.setOptions(gen, new BasicLabyrinthVisualizer());
//        mfr.setOptions(gen, new MinimalisticLabyrinthVisualizer());
//        mfr.setOptions(gen, new CurveLabyrinthVisualizer());
//        mfr.setOptions(gen, new TriangleLabyrinthVisualizer());
//        mfr.setOptions(gen, new SquareLabyrinthVisualizer());
        
        mfr.initializeLabyrinth();
        
        mfr.setVisible(true);
        
    }
}
