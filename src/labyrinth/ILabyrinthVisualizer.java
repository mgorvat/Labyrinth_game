package labyrinth;

import java.awt.Color;
import java.awt.Graphics;

public interface LabyrinthVisualizer {
    public void visualize(Graphics gr, int size, Labyrinth lab);
}

class SimpleLabyrinthVisualizer implements LabyrinthVisualizer{
    @Override
    public void visualize(Graphics gr, int size, Labyrinth lab) {
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
