package labyrinth;

import CSVLib.CSV;
import java.io.File;
import java.util.LinkedList;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import labyrinth.Coordinate.Direction;
import labyrinth.Labyrinth.CellContent;

public class Statistic {
    IMazeGenerator generator;
    LabyrinthVisualizer visualizer;
    VisualizatorMode mode;
    private int stepNumber = 0;
    private int wrongSteps = 0;
    private int stepsInWall = 0;
    private long beginTime = 0;
    private long endTime = 0;

    private Direction rightDirections[][];
    
    
    private Labyrinth labyrinth;
    private boolean visited[][]; 
    
    public Statistic(Labyrinth lab, IMazeGenerator generator, LabyrinthVisualizer visualizer, VisualizatorMode mode){
        this.generator = generator;
        this.visualizer = visualizer;
        this.mode = mode;
        labyrinth = lab;
        rightDirections = new Direction[lab.getSize()][lab.getSize()];
        computeRightDirections();
    }
    
    private void computeRightDirections(){
        visited = new boolean[labyrinth.getSize()][labyrinth.getSize()];
        for(int i = 0; i < labyrinth.getSize(); i++){
            for(int j = 0; j < labyrinth.getSize(); j++){
                if(labyrinth.getCell(i, j).content == CellContent.GOAL){
                    computeRightDirections(i, j);
                    return;
                }
            }
        }
    }
    
    private void computeRightDirections(int x, int y){
        visited[x][y] = true;
        Coordinate curCoord = new Coordinate(x, y);
        Coordinate nextCoord;
        if(!labyrinth.checkWall(x, y, Direction.UP)){
            nextCoord = new Coordinate(curCoord, Direction.UP);
            computeRightDirections(nextCoord.x, nextCoord.y, Direction.DOWN);
        }
        if(!labyrinth.checkWall(x, y, Direction.RIGHT)){
            nextCoord = new Coordinate(curCoord, Direction.RIGHT);
            computeRightDirections(nextCoord.x, nextCoord.y, Direction.LEFT);
        }
        if(!labyrinth.checkWall(x, y, Direction.DOWN)){
            nextCoord = new Coordinate(curCoord, Direction.DOWN);
            computeRightDirections(nextCoord.x, nextCoord.y, Direction.UP);
        }
        if(!labyrinth.checkWall(x, y, Direction.LEFT)){
            nextCoord = new Coordinate(curCoord, Direction.LEFT);
            computeRightDirections(nextCoord.x, nextCoord.y, Direction.RIGHT);
        }
    }
    
    
    private void computeRightDirections(int x, int y, Direction rightDir){
        rightDirections[x][y] = rightDir;
        visited[x][y] = true;
        Coordinate curCoord = new Coordinate(x, y);
        Coordinate nextCoord;
        if(!labyrinth.checkWall(x, y, Direction.UP)){
            nextCoord = new Coordinate(curCoord, Direction.UP);
            if(!visited[nextCoord.x][nextCoord.y])computeRightDirections(nextCoord.x, nextCoord.y, Direction.DOWN);
        }
        if(!labyrinth.checkWall(x, y, Direction.RIGHT)){
            nextCoord = new Coordinate(curCoord, Direction.RIGHT);
            if(!visited[nextCoord.x][nextCoord.y])computeRightDirections(nextCoord.x, nextCoord.y, Direction.LEFT);
        }
        if(!labyrinth.checkWall(x, y, Direction.DOWN)){
            nextCoord = new Coordinate(curCoord, Direction.DOWN);
            if(!visited[nextCoord.x][nextCoord.y])computeRightDirections(nextCoord.x, nextCoord.y, Direction.UP);
        }
        if(!labyrinth.checkWall(x, y, Direction.LEFT)){
            nextCoord = new Coordinate(curCoord, Direction.LEFT);
            if(!visited[nextCoord.x][nextCoord.y])computeRightDirections(nextCoord.x, nextCoord.y, Direction.RIGHT);
        }
    }
    
    
    public void computeStep(Direction dir){       
        if(beginTime == 0){
            beginTime = System.currentTimeMillis();
        }
        Coordinate coord = labyrinth.getPlayerCoordinate();
        if(labyrinth.checkWall(coord, dir)){
            stepsInWall++;
        }else{
            stepNumber++;
            if(rightDirections[coord.x][coord.y] != dir)wrongSteps++;
        }
    }
    
    public void stopTimer(){
        endTime = System.currentTimeMillis();
    }
    
    @Override
    public String toString(){
        String res = "";
        res+="Visualizator: " + "\n";
        res+="Algorithm: " + "\n";
        res+="Steps: " + stepNumber + "\n";
        res+="Wrong steps: " + wrongSteps + "\n";
        res+="Steps in wall: " + stepsInWall + "\n";
        res+="Time: " + (endTime - beginTime) + "\n";
            
        return res;
    }
    
    static class StatisticSet{
        LinkedList<Statistic> set = new LinkedList<>();
        
        public void add(Statistic stat){
            set.add(stat);
        }
        
        public void writeInFile(){
            if(set.size() != 0){
                CSV csv = new CSV();
                int ct = 0;
                for(Statistic stat: set){
                    csv.setCell(stat.visualizer.toString(), ct, 0);
                    csv.setCell(stat.mode.toString(), ct, 1);
                    csv.setCell(stat.generator.toString(), ct, 2);
                    csv.setCell(stat.stepNumber, ct, 3);
                    csv.setCell(stat.wrongSteps, ct, 4);
                    csv.setCell(stat.stepsInWall, ct, 5);
                    csv.setCell(stat.endTime - stat.beginTime, ct, 6);
                    ct++;
                }
                writeInFile(csv);
            }
        }
        
        private void writeInFile(CSV csv){
            JFileChooser chooser = new JFileChooser();
            File file = new File(System.getProperty("user.dir")+"\\statistic.csv");
            chooser.setSelectedFile(file);
            
            while(true){
                int value = chooser.showSaveDialog(null);
                if(value == JFileChooser.APPROVE_OPTION) {
                    try {
                        csv.writeToFile(chooser.getSelectedFile().getAbsolutePath(), ";");
                        break;
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Writing file error. Try to choose another path!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Path not choosed! Please choose the path.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}


