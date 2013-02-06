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