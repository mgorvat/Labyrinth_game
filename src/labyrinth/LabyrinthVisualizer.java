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
        gr.fillPolygon(new int[]{0, length + 1, 3 * length / 4, length / 4}, new int[]{0, 0, length / 4, length / 4}, 4);
        
    }
    public void drawRightSide(Graphics2D gr, int length, Color clr){
        gr.setColor(clr);
        gr.fillPolygon(new int[]{length + 1, length + 1, 3 * length / 4, 3 * length / 4}, new int[]{0, length, 3 * length / 4, length / 4}, 4);
        
    }
    public void drawDownSide(Graphics2D gr, int length, Color clr){
        gr.setColor(clr);
        gr.fillPolygon(new int[]{0, length + 1, 3 * length / 4, length / 4}, new int[]{length, length, 3 * length / 4, 3 * length / 4}, 4);
    }    
    public void drawLeftSide(Graphics2D gr, int length, Color clr){
        gr.setColor(clr);
        gr.fillPolygon(new int[]{0, 0, length / 4, length / 4}, new int[]{0, length, 3 * length / 4, length / 4}, 4);
    }
    
    public void drawCenter(Graphics2D gr, int length, Color clr){
        gr.setColor(clr);
        gr.fillRect(length / 4, length / 4, length / 2 + 2, length / 2 + 2);
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
                    Math.round(cellSize / 2) + 2, Math.round(cellSize / 2) + 2);
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

class EllipseLabyrinthVisualizer extends LabyrinthVisualizer{
    class Point{
        public Point(double x, double y){this.x = x; this.y = y;}
        public double x, y;
    }
    Point[][] coordinates;
    double step;
    
    int computeAngle(Point pt, int width, int height){
        if (Math.abs(pt.y) < 0.0001){
            if (pt.x >= 0) return 0;
            else return 180;
        }
        
        double x = pt.x * (double)height / width;
        double y = pt.y;

        double length = Math.sqrt(x * x + y * y);
        double angle = Math.acos(x / length);
        
        if(pt.y < 0) angle = 2 * Math.PI - angle;
        int res = (int)Math.round(Math.toDegrees(angle));
                
        return res;
    }  
    
    
    @Override
    protected void drawCell(Graphics2D gr, int size, Labyrinth lab, int x, int y) {
        if(lab.checkWall(x, y, Direction.UP)){
            int xCoord = -size / 2;
            int yCoord;
            int height,from, angle;
            if(lab.getSize() % 2 == 0 && y < lab.getSize() / 2 || lab.getSize() % 2 == 1 && y <= (lab.getSize()+1) / 2 - 1){
                yCoord = size / 2 - (int)((1.0 - step * y) * (size / 2));
                height = (int)((1.0 - step * y) * size);

                from = computeAngle(coordinates[x + 1][y], 2 * size, height);
                angle = computeAngle(coordinates[x][y], 2 * size, height) - from;

                gr.drawArc(xCoord, yCoord, 2 * size, height, from, angle);
            }else{
                if(lab.getSize() % 2 == 0 && y == lab.getSize() / 2){
                    int x1 = (int)(size / 2 + coordinates[x][y].x * size / 2);
                    int x2 = (int)(size / 2 + coordinates[x + 1][y].x * size / 2);
                    gr.drawLine(x1, size / 2, x2, size / 2);
                }else{
                    int yBuf = lab.getSize() - y;
                    
                    yCoord = size / 2 - (int)((1.0 - step * yBuf) * (size / 2));
                    height = (int)((1.0 - step * yBuf) * size);
                    
                    from = computeAngle(coordinates[x][y], 2 * size, height);
                    angle = computeAngle(coordinates[x + 1][y], 2 * size, height) - from;
                    if(angle<0)angle+=360;
                    
                    gr.drawArc(xCoord, yCoord, 2 * size, height, from, angle);
                }
            }
        }
        
        if(lab.checkWall(x, y, Direction.LEFT)){
            int yCoord = -size / 2;
            int xCoord;
            int width, from, angle;
            if(lab.getSize() % 2 == 0 && x < lab.getSize() / 2 || lab.getSize() % 2 == 1 && x <= (lab.getSize()+1) / 2 - 1){
                xCoord = size / 2 - (int)((1.0 - step * x) * (size / 2));
                width = (int)((1.0 - step * x) * size);

                from = computeAngle(coordinates[x][y+1], width, 2 * size);
                angle = computeAngle(coordinates[x][y], width, 2 * size) - from;

                gr.drawArc(xCoord, yCoord, width, 2 * size, from, angle);
            }else{
                if(lab.getSize() % 2 == 0 && x == lab.getSize() / 2){
                    int y1 = (int)(size / 2 - coordinates[x][y].y * size / 2);
                    int y2 = (int)(size / 2 - coordinates[x][y+1].y * size / 2);
                    gr.drawLine(size / 2, y1,size / 2, y2);
                }else{
                    int xBuf = lab.getSize() - x;
                    
                    xCoord = size / 2 - (int)((1.0 - step * xBuf) * (size / 2));
                    width = (int)((1.0 - step * xBuf) * size);
                    
                    from = computeAngle(coordinates[x][y+1], width, 2 * size);
                    angle = computeAngle(coordinates[x][y], width, 2 * size) - from;
                    if(angle < 0)angle += 360;
                    
                    gr.drawArc(xCoord, yCoord, width, 2 * size, from, angle);
                }
            }
        }
        
        
        if(lab.checkWall(x, y, Direction.DOWN)){
            int xCoord = -size / 2;
            int yCoord;
            int height,from, angle;
            if(lab.getSize() % 2 == 0 && y < lab.getSize() / 2 - 1 || lab.getSize() % 2 == 1 && y < (lab.getSize()+1) / 2 - 1){
                y++;
                yCoord = size / 2 - (int)((1.0 - step * y) * (size / 2));
                height = (int)((1.0 - step * y) * size);

                from = computeAngle(coordinates[x + 1][y], 2 * size, height);
                angle = computeAngle(coordinates[x][y], 2 * size, height) - from;

                gr.drawArc(xCoord, yCoord, 2 * size, height, from, angle);
            }else{
                if(lab.getSize() % 2 == 0 && y == lab.getSize() / 2 - 1){
                    y++;
                    int x1 = (int)(size / 2 + coordinates[x][y].x * size / 2);
                    int x2 = (int)(size / 2 + coordinates[x + 1][y].x * size / 2);
                    gr.drawLine(x1, size / 2, x2, size / 2);
                }else{
                    y++;
                    int yBuf = lab.getSize() - y;
                    
                    yCoord = size / 2 - (int)((1.0 - step * yBuf) * (size / 2));
                    height = (int)((1.0 - step * yBuf) * size);
                    
                    from = computeAngle(coordinates[x][y], 2 * size, height);
                    angle = computeAngle(coordinates[x + 1][y], 2 * size, height) - from;
                    if(angle<0)angle+=360;
        
                    gr.drawArc(xCoord, yCoord, 2 * size, height, from, angle);
                }
            }
            y--;
        }
        
        if(lab.checkWall(x, y, Direction.RIGHT)){
            int yCoord = -size / 2;
            int xCoord;
            int width, from, angle;
            if(lab.getSize() % 2 == 0 && x < lab.getSize() / 2 - 1 || lab.getSize() % 2 == 1 && x < (lab.getSize()+1) / 2 - 1){
                x++;
                xCoord = size / 2 - (int)((1.0 - step * x) * (size / 2));
                width = (int)((1.0 - step * x) * size);

                from = computeAngle(coordinates[x][y+1], width, 2 * size);
                angle = computeAngle(coordinates[x][y], width, 2 * size) - from;

                gr.drawArc(xCoord, yCoord, width, 2 * size, from, angle);
            }else{
                if(lab.getSize() % 2 == 0 && x == lab.getSize() / 2 - 1){
                    x++;
                    int y1 = (int)(size / 2 - coordinates[x][y+1].y * size / 2);
                    int y2 = (int)(size / 2 - coordinates[x][y].y * size / 2);
                    gr.drawLine(size / 2, y1, size / 2, y2);
                }else{
                    x++;
                    int xBuf = lab.getSize() - x;
                    
                    xCoord = size / 2 - (int)((1.0 - step * xBuf) * (size / 2));
                    width = (int)((1.0 - step * xBuf) * size);
                    
                    from = computeAngle(coordinates[x][y+1], width, 2 * size);
                    angle = computeAngle(coordinates[x][y], width, 2 * size) - from;
                    if(angle<0)angle+=360;
                    
                    gr.drawArc(xCoord, yCoord, width, 2 * size, from, angle);
                }
            }
            x--;
        }
        
        Cell cell = lab.getCell(x, y);
        double xCent = (coordinates[x][y].x + coordinates[x][y+1].x + coordinates[x+1][y].x + coordinates[x+1][y+1].x) / 4;
        double yCent = (coordinates[x][y].y + coordinates[x][y+1].y + coordinates[x+1][y].y + coordinates[x+1][y+1].y) / 4;
        double r = Math.abs(xCent - coordinates[x][y].x);
        if(Math.abs(xCent - coordinates[x+1][y].x) < r)r = Math.abs(xCent - coordinates[x+1][y].x);
        if(Math.abs(xCent - coordinates[x][y+1].x) < r)r = Math.abs(xCent - coordinates[x][y+1].x);
        if(Math.abs(xCent - coordinates[x+1][y+1].x) < r)r = Math.abs(xCent - coordinates[x+1][y+1].x);
        
        if(Math.abs(yCent - coordinates[x][y].y) < r)r = Math.abs(yCent - coordinates[x][y].y);
        if(Math.abs(yCent - coordinates[x+1][y].y) < r)r = Math.abs(yCent - coordinates[x+1][y].y);
        if(Math.abs(yCent - coordinates[x][y+1].y) < r)r = Math.abs(yCent - coordinates[x][y+1].y);
        if(Math.abs(yCent - coordinates[x+1][y+1].y) < r)r = Math.abs(yCent - coordinates[x+1][y+1].y);
        
        int xPos = (int)(size / 2 + xCent * size / 2);
        int yPos = (int)(size / 2 - yCent * size / 2);
        int R = (int)(r * size / 2);
        
        switch(cell.content){
            case PLAYER:
                gr.setColor(Color.ORANGE);              
                gr.fillOval(xPos - 3*R/4, yPos - 3*R/4, 7*R/8, 7*R/8);
                break;
            
            case GOAL:
                gr.setColor(Color.GREEN);
                gr.fillRect(xPos - 3*R/4, yPos - 3*R/4, 7*R/8, 7*R/8);
                break;
        }
        gr.setColor(Color.BLACK);
    }

    @Override
    protected void processComputations(Graphics2D gr, int size, Labyrinth lab) {
        gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int n = (lab.getSize() + 2) / 2;
        int intersectionCount;
        if(lab.getSize() % 2 == 0)intersectionCount = 2 * n - 1;
        else intersectionCount = 2 * n;
        coordinates = new Point[intersectionCount][intersectionCount];
        
        if(lab.getSize() % 2 == 0)step = 1.0 / ((lab.getSize() + 2) / 2 - 1);   
        else step = 1.0 / ((lab.getSize() + 1) / 2);
        
        for(int i = 0; i < n; i++){
            for(int j = i; j < n; j++){
                double r = 1.0 - i * step;
                double k = r / 2.0;
                double width = 1.0 - j * step;
                double x, y;
                
                if(k!=0){
                    width*=k;
                    x = width * Math.sqrt((r*r - 4.0) / (width*width - 4.0));
                    y = 2.0 * Math.sqrt((width*width - r*r) / (width*width - 4.0));
                    x /= k;
                }else{
                    x = width;
                    y = 0;
                }
                
                coordinates[j][i] = new Point(-x, y);
                coordinates[j][coordinates.length - i - 1] = new Point(-x, -y);
                coordinates[coordinates.length - j - 1][i] = new Point(x, y);
                coordinates[coordinates.length - j - 1][coordinates.length - i - 1] = new Point(x, -y);
                
                coordinates[i][j] = new Point(-y, x);
                coordinates[i][coordinates.length - j - 1] = new Point(-y, -x);
                coordinates[coordinates.length - i - 1][j] = new Point(y, x);
                coordinates[coordinates.length - i - 1][coordinates.length - j - 1] = new Point(y, -x);
            }
        }
    }
}