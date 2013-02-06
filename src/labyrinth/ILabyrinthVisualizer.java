package labyrinth;

import java.awt.*;
import java.awt.geom.AffineTransform;



public interface ILabyrinthVisualizer {
    public void visualize(Graphics2D gr, int size, Labyrinth lab);
}
class SimpleLabyrinthVisualizer implements ILabyrinthVisualizer{
    @Override
    public void visualize(Graphics2D gr, int size, Labyrinth lab) {
        gr.drawLine(0, 0, 0, size);
        gr.drawLine(0, 0, size, 0);
        
        float cellSize = (float)(size - 1) / lab.getSize();
        float curX, curY = 1;
        
        for(int i = 0; i < lab.getSize(); i++){
            curY += cellSize;
            curX = 1;
            for(int j = 0; j < lab.getSize(); j++){
                curX += cellSize;
                Labyrinth.Cell cell = lab.getCell(j, i);
                if(cell.down == true){
                    gr.drawLine(Math.round(curX - cellSize), Math.round(curY),
                            Math.round(curX), Math.round(curY));
                }
                if(cell.right == true){
                    gr.drawLine(Math.round(curX), Math.round(curY - cellSize),
                            Math.round(curX), Math.round(curY));
                }
                
                                
                if(lab.getCell(j, i).content != Labyrinth.CellContent.NONE){
                    if(lab.getCell(j, i).content == Labyrinth.CellContent.PLAYER){
                        gr.setColor(Color.green);
                        gr.fillOval(Math.round(curX - 3 * cellSize / 4), 
                                Math.round(curY - 3 * cellSize / 4),
                                Math.round(cellSize / 2), Math.round(cellSize / 2));
                        gr.setColor(Color.black);
                    }
                    
                    if(lab.getCell(j, i).content == Labyrinth.CellContent.GOAL){
                        gr.setColor(Color.red);
                        gr.fillRect(Math.round(curX - 3 * cellSize / 4), 
                                Math.round(curY - 3 * cellSize / 4),
                                Math.round(cellSize / 2), Math.round(cellSize / 2));
                        gr.setColor(Color.black);
                    }
                    
                }
            }
        }
    }
}

class CurveLabyrinthVisualizer implements ILabyrinthVisualizer{
    @Override

    public void visualize(Graphics2D gr, int size, Labyrinth lab) {
        float cellSize =  (float) (size / (lab.getSize() + 0.5));
        int begCoord = Math.round(cellSize / 4);
        
        AffineTransform at;
        
        
        for(int i = 0; i < lab.getSize(); i++){
            at = AffineTransform.getTranslateInstance(begCoord + Math.round(i * cellSize), begCoord);
            gr.setTransform(at);
            drawSinCurve(Math.round(cellSize), gr);
        }
        
        for(int i = 0; i < lab.getSize(); i++){
            at = AffineTransform.getTranslateInstance(begCoord, begCoord + Math.round(i * cellSize));
            gr.setTransform(at);
            gr.rotate(Math.PI / 2);

            drawSinCurve(Math.round(cellSize), gr);
        }
        
        float curX, curY;
        for(int i = 0; i < lab.getSize(); i++){
            curY = begCoord + i * cellSize;
            for(int j = 0; j < lab.getSize(); j++){
                curX = begCoord + j * cellSize;
                
                Labyrinth.Cell cell = lab.getCell(j, i);
                
                if(cell.right == true){
                    at = AffineTransform.getTranslateInstance(Math.round(curX + cellSize), Math.round(curY));
                    gr.setTransform(at);
                    gr.rotate(Math.PI / 2);
                    
                    drawSinCurve(Math.round(cellSize), gr);
                }
                
                if(cell.down == true){
                    at = AffineTransform.getTranslateInstance(Math.round(curX), Math.round(curY + cellSize));
                    gr.setTransform(at);
                                        
                    drawSinCurve(Math.round(cellSize), gr);
                }
                
                if(lab.getCell(j, i).content != Labyrinth.CellContent.NONE){
                    gr.setTransform(AffineTransform.getTranslateInstance(0,0));
                    if(lab.getCell(j, i).content == Labyrinth.CellContent.PLAYER){
                        gr.setColor(Color.green);
                        gr.fillOval(Math.round(curX + cellSize / 4), 
                                Math.round(curY + cellSize / 4),
                                Math.round(cellSize / 2), Math.round(cellSize / 2));
                        gr.setColor(Color.black);
                    }
                    
                    if(lab.getCell(j, i).content == Labyrinth.CellContent.GOAL){
                        gr.setColor(Color.red);
                        gr.fillRect(Math.round(curX + cellSize / 4), 
                                Math.round(curY + cellSize / 4),
                                Math.round(cellSize / 2), Math.round(cellSize / 2));
                        gr.setColor(Color.black);
                    }   
                }
            }   
        }
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

class TriangleLabyrinthVisualizer implements ILabyrinthVisualizer{
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
    public void visualize(Graphics2D gr, int size, Labyrinth lab) {
        float cellSize = (float)size / lab.getSize();
        
        AffineTransform at;
        
        for(int i = 0; i < lab.getSize(); i++){
            for(int j = 0; j < lab.getSize(); j++){
                at = AffineTransform.getTranslateInstance(Math.round(i * cellSize), Math.round(j * cellSize));
                gr.setTransform(at);
                                
                if(lab.checkWall(i, j, Coordinate.Direction.UP) == true)drawUpSide(gr, (int)(cellSize + 1), Color.RED);
                else drawUpSide(gr, (int)(cellSize + 1), Color.GREEN);
                
                if(lab.checkWall(i, j, Coordinate.Direction.RIGHT) == true)drawRightSide(gr, (int)(cellSize + 1), Color.RED);
                else drawRightSide(gr, (int)(cellSize + 1), Color.GREEN);
                
                if(lab.checkWall(i, j, Coordinate.Direction.DOWN) == true)drawDownSide(gr, (int)(cellSize + 1), Color.RED);
                else drawDownSide(gr, (int)(cellSize + 1), Color.GREEN);
                
                if(lab.checkWall(i, j, Coordinate.Direction.LEFT) == true)drawLeftSide(gr, (int)(cellSize + 1), Color.RED);
                else drawLeftSide(gr, (int)(cellSize + 1), Color.GREEN);
                
                
                //TODO: think about adding this.
//                gr.setColor(Color.BLACK);
//                gr.drawLine(0, 0, (int)(cellSize + 1), 0);
//                gr.drawLine((int)(cellSize + 1), 0, (int)(cellSize + 1), (int)(cellSize + 1));
//                gr.drawLine(0, (int)(cellSize + 1), (int)(cellSize + 1), (int)(cellSize + 1));
//                gr.drawLine(0, 0, 0, (int)(cellSize + 1));
                
                if(lab.getCell(i, j).content != Labyrinth.CellContent.NONE){
                    if(lab.getCell(i, j).content == Labyrinth.CellContent.PLAYER){
                        gr.setColor(Color.BLUE);
//                        gr.fillOval(Math.round(cellSize / 4), 
//                                Math.round(cellSize / 4),
//                                Math.round(cellSize / 2), Math.round(cellSize / 2));
                        gr.fillPolygon(
                                new int[]{Math.round(cellSize / 2), Math.round(3 * cellSize / 4),
                                Math.round(cellSize / 2), Math.round(cellSize / 4)}, 
                                new int[]{Math.round(cellSize / 4),Math.round(cellSize / 2),
                                Math.round(3 * cellSize / 4),Math.round(cellSize / 2)}, 4);
                        
                    }
                    
                    if(lab.getCell(i, j).content == Labyrinth.CellContent.GOAL){
                        gr.setColor(Color.BLACK);
                        gr.fillRect(Math.round(cellSize / 4), 
                                Math.round(cellSize / 4),
                                Math.round(cellSize / 2), Math.round(cellSize / 2));       
                    }   
                }       
            }
        }
    }
}

class LineLabyrinthVisualizer implements ILabyrinthVisualizer{
    @Override
    public void visualize(Graphics2D gr, int size, Labyrinth lab) {
        AffineTransform at;
        float cellSize = (float)size / lab.getSize();
        
        for(int i = 0; i < lab.getSize(); i++){
            for(int j = 0; j < lab.getSize(); j++){
                
                Labyrinth.Cell cell = lab.getCell(i, j);
                if(cell.right == false){
                    gr.drawLine((int)((i + 0.5) * cellSize ), (int)((j  + 0.5) * cellSize),
                            (int)((i + 1.5) * cellSize ), (int)((j + 0.5) * cellSize));
                }
                
                if(cell.down == false){
                    gr.drawLine((int)((i + 0.5) * cellSize ), (int)((j  + 0.5) * cellSize),
                            (int)((i + 0.5) * cellSize ), (int)((j + 1.5) * cellSize));
                }
                
                if(lab.getCell(j, i).content != Labyrinth.CellContent.NONE){
                    gr.setTransform(AffineTransform.getTranslateInstance(0,0));
                    if(lab.getCell(j, i).content == Labyrinth.CellContent.PLAYER){
                        gr.setColor(Color.green);
                        gr.fillOval(Math.round(cellSize * j + cellSize / 4), 
                                Math.round(cellSize * i + cellSize / 4),
                                Math.round(cellSize / 2), Math.round(cellSize / 2));
                        gr.setColor(Color.black);
                    }
                    
                    if(lab.getCell(j, i).content == Labyrinth.CellContent.GOAL){
                        gr.setColor(Color.red);
                        gr.fillRect(Math.round(cellSize * j + cellSize / 4), 
                                Math.round(cellSize * i + cellSize / 4),
                                Math.round(cellSize / 2), Math.round(cellSize / 2));
                        gr.setColor(Color.black);
                    }   
                }
                
            }   
        }
    }
}