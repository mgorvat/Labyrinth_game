package labyrinth;

import java.awt.*;
import java.awt.geom.AffineTransform;
import labyrinth.Coordinate.Direction;
import labyrinth.Labyrinth.Cell;

enum VisualizatorMode {FULL, NEAREST, SIGHT}


public abstract class LabyrinthVisualizer {
    public void visualize(Graphics2D gr, int size, Labyrinth lab, VisualizatorMode mode){
        processComputations(gr, size, lab);
        switch (mode){
            case FULL:
                fullVisualization(gr, size, lab);
                break;
            case NEAREST:
                nearestVisualization(gr, size, lab);
                break;
            case SIGHT:
                sightVisualisation(gr, size, lab);
                break;
           
                
        }
    }
    protected void fullVisualization(Graphics2D gr, int size, Labyrinth lab){
        for(int i = 0; i < lab.getSize(); i++){
            for(int j = 0; j < lab.getSize(); j++){
                drawCell(gr, size, lab, i, j);
            }
        }
    }
    
    protected void nearestVisualization(Graphics2D gr, int size, Labyrinth lab){
        int[] x = new int[]{-1, 0, 1, -1, 0, 1, -1, 0, 1};
        int[] y = new int[]{-1, -1, -1, 0, 0, 0, 1, 1, 1};
        Coordinate coord = lab.getPlayerCoordinate();
        int curX, curY;
        curX = coord.x;
        curY = coord.y;
        for(int i = 0; i < x.length; i++){
            if(curX + x[i] >= 0 && curX + x[i] < lab.getSize() && curY + y[i] >= 0 && curY + y[i] < lab.getSize()){
                drawCell(gr, size, lab, curX + x[i], curY + y[i]);
            }
        }
    }
    
    protected void sightVisualisation(Graphics2D gr, int size, Labyrinth lab){
        Coordinate coord = lab.getPlayerCoordinate();
        int x = coord.x;
        int y = coord.y;
        drawCell(gr, size, lab, x, y);
        
        int curX, curY;
        curX = x; curY = y;
        while(!lab.checkWall(curX, curY, Direction.UP)){
            curY--;
            drawCell(gr, size, lab, curX, curY);
        }
        curX = x; curY = y;
        while(!lab.checkWall(curX, curY, Direction.RIGHT)){
            curX++;
            drawCell(gr, size, lab, curX, curY);
        }
        curX = x; curY = y;
        while(!lab.checkWall(curX, curY, Direction.DOWN)){
            curY++;
            drawCell(gr, size, lab, curX, curY);
        }
        curX = x; curY = y;
        while(!lab.checkWall(curX, curY, Direction.LEFT)){
            curX--;
            drawCell(gr, size, lab, curX, curY);
        }        
    }
    
    protected abstract void drawCell(Graphics2D gr, int size, Labyrinth lab, int x, int y);
    protected abstract void processComputations(Graphics2D gr, int size, Labyrinth lab);
}

class BasicLabyrinthVisualizer extends LabyrinthVisualizer{
    int cellSize;
    AffineTransform defaultTransform;
    @Override
    protected void drawCell(Graphics2D gr, int size, Labyrinth lab, int x, int y) {
        AffineTransform at = AffineTransform.getTranslateInstance(cellSize * x, cellSize * y);
        gr.transform(at);
        if(lab.checkWall(x, y, Coordinate.Direction.UP))gr.drawLine(0, 0, cellSize, 0);
        if(lab.checkWall(x, y, Coordinate.Direction.RIGHT))gr.drawLine(cellSize, 0, cellSize, cellSize);
        if(lab.checkWall(x, y, Coordinate.Direction.DOWN))gr.drawLine(cellSize, cellSize, 0, cellSize);
        if(lab.checkWall(x, y, Coordinate.Direction.LEFT))gr.drawLine(0, 0, 0, cellSize);
        
        Cell cell = lab.getCell(x, y);
        switch(cell.content){
            case PLAYER:
                gr.setColor(Color.green);
                gr.fillOval(cellSize / 4, cellSize / 4, cellSize / 2, cellSize / 2);
                gr.setColor(Color.black);
                break;
            
            case GOAL:
                gr.setColor(Color.red);
                gr.fillRect(cellSize / 4, cellSize / 4, cellSize / 2, cellSize / 2);
                gr.setColor(Color.black);
                break;
        }
        gr.setTransform(defaultTransform);
    }
    
    @Override
    protected void processComputations(Graphics2D gr, int size, Labyrinth lab) {
        cellSize = size / lab.getSize();
        defaultTransform = gr.getTransform();
    }
}

class CurveLabyrinthVisualizer extends LabyrinthVisualizer{
    int cellSize;
    AffineTransform defaultTransform;
    @Override
    protected void drawCell(Graphics2D gr, int size, Labyrinth lab, int x, int y) {
        AffineTransform at = AffineTransform.getTranslateInstance(cellSize * x, cellSize * y);
        gr.transform(at);
        AffineTransform buf = gr.getTransform();
        if(lab.checkWall(x, y, Coordinate.Direction.UP))drawSinCurve(cellSize, gr);       
        if(lab.checkWall(x, y, Coordinate.Direction.RIGHT)){
            gr.transform(AffineTransform.getTranslateInstance(cellSize, 0));
            gr.rotate(Math.PI / 2);
            drawSinCurve(cellSize, gr);
            gr.setTransform(buf);
        }
        gr.setTransform(buf);
        if(lab.checkWall(x, y, Coordinate.Direction.DOWN)){
            gr.transform(AffineTransform.getTranslateInstance(0, cellSize));
            drawSinCurve(cellSize, gr);
            gr.setTransform(buf);
        }
        if(lab.checkWall(x, y, Coordinate.Direction.LEFT)){
            gr.rotate(Math.PI / 2);
            drawSinCurve(cellSize, gr);
            gr.setTransform(buf);
        }
        
        Cell cell = lab.getCell(x, y);
        switch(cell.content){
            case PLAYER:
                gr.setColor(Color.green);
                gr.fillOval(cellSize / 4, cellSize / 4, cellSize / 2, cellSize / 2);
                gr.setColor(Color.black);
                break;
            
            case GOAL:
                gr.setColor(Color.red);
                gr.fillRect(cellSize / 4, cellSize / 4, cellSize / 2, cellSize / 2);
                gr.setColor(Color.black);
                break;
        }
        gr.setTransform(defaultTransform);
    }
    
    @Override
    protected void processComputations(Graphics2D gr, int size, Labyrinth lab) {
        cellSize = (int)(size / (lab.getSize() + 0.5));
        gr.transform(AffineTransform.getTranslateInstance(cellSize / 4, cellSize / 4));
        defaultTransform = gr.getTransform();
    }
    
    private void drawSinCurve(int length, Graphics2D gr){
        int h = length / 4;
        
        float prev = 0;
        for(int i = 1; i <= length; i++){
            float arg = (float)(2 * i * Math.PI / length);
            float cur = (float)(h * Math.sin(arg));
            gr.drawLine(i, Math.round(prev), i, Math.round(cur));
            prev = cur;
        }   
    }
}

class TriangleLabyrinthVisualizer extends LabyrinthVisualizer{
    int cellSize;
    AffineTransform defaultTransform;
    public void drawUpSide(Graphics2D gr, int length, Color clr){
        gr.setColor(clr);
        gr.fillPolygon(new int[]{0, length, length / 2}, new int[]{0, 0, length / 2}, 3);
    }
    public void drawRightSide(Graphics2D gr, int length, Color clr){
        gr.setColor(clr);
        gr.fillPolygon(new int[]{length, length, length / 2}, new int[]{0, length, length / 2}, 3);
    }
    public void drawDownSide(Graphics2D gr, int length, Color clr){
        gr.setColor(clr);
        gr.fillPolygon(new int[]{0, length / 2, length}, new int[]{length, length/2, length}, 3);
    }    
    public void drawLeftSide(Graphics2D gr, int length, Color clr){
        gr.setColor(clr);
        gr.fillPolygon(new int[]{0, length / 2, 0}, new int[]{0, length / 2, length}, 3);
    }
    @Override
    protected void drawCell(Graphics2D gr, int size, Labyrinth lab, int x, int y) {
        AffineTransform at = AffineTransform.getTranslateInstance(cellSize * x, cellSize * y);
        gr.transform(at);
                if(lab.checkWall(x, y, Coordinate.Direction.UP) == true)drawUpSide(gr, (int)(cellSize + 1), Color.DARK_GRAY);
                else drawUpSide(gr, (int)(cellSize + 1), Color.LIGHT_GRAY);
                
                if(lab.checkWall(x, y, Coordinate.Direction.RIGHT) == true)drawRightSide(gr, (int)(cellSize + 1), Color.DARK_GRAY);
                else drawRightSide(gr, (int)(cellSize + 1), Color.LIGHT_GRAY);
                
                if(lab.checkWall(x, y, Coordinate.Direction.DOWN) == true)drawDownSide(gr, (int)(cellSize + 1), Color.DARK_GRAY);
                else drawDownSide(gr, (int)(cellSize + 1), Color.LIGHT_GRAY);
                
                if(lab.checkWall(x, y, Coordinate.Direction.LEFT) == true)drawLeftSide(gr, (int)(cellSize + 1), Color.DARK_GRAY);
                else drawLeftSide(gr, (int)(cellSize + 1), Color.LIGHT_GRAY);
        
        Cell cell = lab.getCell(x, y);
        switch(cell.content){
            case PLAYER:
                gr.setColor(Color.ORANGE);
                gr.fillPolygon(new int[]{Math.round(cellSize / 2), Math.round(3 * cellSize / 4),
                    Math.round(cellSize / 2), Math.round(cellSize / 4)}, 
                    new int[]{Math.round(cellSize / 4),Math.round(cellSize / 2),
                    Math.round(3 * cellSize / 4),Math.round(cellSize / 2)}, 4);
                break;
            
            case GOAL:
                gr.setColor(Color.GREEN);
                gr.fillRect(Math.round(cellSize / 4), 
                    Math.round(cellSize / 4),
                    Math.round(cellSize / 2), Math.round(cellSize / 2));       
                break;
        }
        gr.setTransform(defaultTransform);
    }

    @Override
    protected void processComputations(Graphics2D gr, int size, Labyrinth lab) {
        cellSize = size / lab.getSize();
        defaultTransform = gr.getTransform();
    }

}

class MinimalisticLabyrinthVisualizer extends LabyrinthVisualizer{
    int cellSize;
    AffineTransform defaultTransform;
    @Override
    protected void drawCell(Graphics2D gr, int size, Labyrinth lab, int x, int y) {
        AffineTransform at = AffineTransform.getTranslateInstance(cellSize * x, cellSize * y);
        gr.transform(at);
                if(!lab.checkWall(x, y, Coordinate.Direction.UP))gr.drawLine(cellSize / 2, cellSize / 2, cellSize / 2, -cellSize / 2);
                if(!lab.checkWall(x, y, Coordinate.Direction.RIGHT))gr.drawLine(cellSize / 2, cellSize / 2, 3 * cellSize / 2, cellSize / 2);
                if(!lab.checkWall(x, y, Coordinate.Direction.DOWN))gr.drawLine(cellSize / 2, cellSize / 2, cellSize / 2, 3 * cellSize / 2);
                if(!lab.checkWall(x, y, Coordinate.Direction.LEFT))gr.drawLine(cellSize / 2, cellSize / 2, -cellSize / 2, cellSize / 2);
        Cell cell = lab.getCell(x, y);
        switch(cell.content){
            case PLAYER:
                gr.setColor(Color.green);
                gr.fillOval(cellSize / 4, cellSize / 4, cellSize / 2, cellSize / 2);
                gr.setColor(Color.black);
                break;
            
            case GOAL:
                gr.setColor(Color.red);
                gr.fillRect(cellSize / 4, cellSize / 4, cellSize / 2, cellSize / 2);
                gr.setColor(Color.black);
                break;
        }
        gr.setTransform(defaultTransform);
    }

    @Override
    protected void processComputations(Graphics2D gr, int size, Labyrinth lab) {
        cellSize = size / lab.getSize();
        defaultTransform = gr.getTransform();
    }
}

class SquareLabyrinthVisualizer extends LabyrinthVisualizer{
    int cellSize;
    AffineTransform defaultTransform;
    public void drawUpSide(Graphics2D gr, int length, Color clr){
        gr.setColor(clr);
        gr.fillPolygon(new int[]{0, length, 3 * length / 4, length / 4}, new int[]{0, 0, length / 4, length / 4}, 4);
        
    }
    public void drawRightSide(Graphics2D gr, int length, Color clr){
        gr.setColor(clr);
        gr.fillPolygon(new int[]{length, length, 3 * length / 4, 3 * length / 4}, new int[]{0, length, 3 * length / 4, length / 4}, 4);
        
    }
    public void drawDownSide(Graphics2D gr, int length, Color clr){
        gr.setColor(clr);
        gr.fillPolygon(new int[]{0, length, 3 * length / 4, length / 4}, new int[]{length, length, 3 * length / 4, 3 * length / 4}, 4);
    }    
    public void drawLeftSide(Graphics2D gr, int length, Color clr){
        gr.setColor(clr);
        gr.fillPolygon(new int[]{0, 0, length / 4, length / 4}, new int[]{0, length, 3 * length / 4, length / 4}, 4);
    }
    
    public void drawCenter(Graphics2D gr, int length, Color clr){
        gr.setColor(clr);
        gr.fillRect(length / 4, length / 4, length / 2, length / 2);
    }
    @Override
    protected void drawCell(Graphics2D gr, int size, Labyrinth lab, int x, int y) {
        AffineTransform at = AffineTransform.getTranslateInstance(cellSize * x, cellSize * y);
        gr.transform(at);
        if(lab.checkWall(x, y, Coordinate.Direction.UP) == true)drawUpSide(gr, (int)(cellSize + 1), Color.DARK_GRAY);
        else drawUpSide(gr, (int)(cellSize + 1), Color.LIGHT_GRAY);
                
        if(lab.checkWall(x, y, Coordinate.Direction.RIGHT) == true)drawRightSide(gr, (int)(cellSize + 1), Color.DARK_GRAY);
        else drawRightSide(gr, (int)(cellSize + 1), Color.LIGHT_GRAY);
                
        if(lab.checkWall(x, y, Coordinate.Direction.DOWN) == true)drawDownSide(gr, (int)(cellSize + 1), Color.DARK_GRAY);
        else drawDownSide(gr, (int)(cellSize + 1), Color.LIGHT_GRAY);
                
        if(lab.checkWall(x, y, Coordinate.Direction.LEFT) == true)drawLeftSide(gr, (int)(cellSize + 1), Color.DARK_GRAY);
        else drawLeftSide(gr, (int)(cellSize + 1), Color.LIGHT_GRAY);
        
        drawCenter(gr, cellSize, Color.LIGHT_GRAY);
                
        Cell cell = lab.getCell(x, y);
        switch(cell.content){
            case PLAYER:
                gr.setColor(Color.ORANGE);
                gr.fillPolygon(new int[]{Math.round(cellSize / 2), Math.round(3 * cellSize / 4),
                    Math.round(cellSize / 2), Math.round(cellSize / 4)}, 
                    new int[]{Math.round(cellSize / 4),Math.round(cellSize / 2),
                    Math.round(3 * cellSize / 4),Math.round(cellSize / 2)}, 4);
                break;
            
            case GOAL:
                gr.setColor(Color.GREEN);
                gr.fillRect(Math.round(cellSize / 4), 
                    Math.round(cellSize / 4),
                    Math.round(cellSize / 2), Math.round(cellSize / 2));       
                break;
        }
        gr.setTransform(defaultTransform);
    }

    @Override
    protected void processComputations(Graphics2D gr, int size, Labyrinth lab) {
        cellSize = size / lab.getSize();
        defaultTransform = gr.getTransform();
    }
}