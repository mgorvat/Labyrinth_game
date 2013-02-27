package labyrinth;

public class Main {
    public static void main(String[] args) {
        IMazeGenerator gen = new HuntAndKillGenerator();
//        IMazeGenerator gen = new DivideEtImperaGenerator();
//        IMazeGenerator gen = new BackTrackGenerator();
        
        MainFrame mfr = new MainFrame();
        
        
        mfr.setOptions(gen, new BasicLabyrinthVisualizer());
    
    
        mfr.initializeLabyrinth();
        
        mfr.setVisible(true);
    }
}
