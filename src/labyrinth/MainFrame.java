package labyrinth;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainFrame extends JFrame {
    private Labyrinth labyrinth;
    private LabyrinthVisualizer visualizer;
    private JPanel labPanel = new LabyrinthPanel();
    
    class LabyrinthPanel extends JPanel{
        @Override
        public void paintComponent(Graphics gr){
            int size = Math.min(this.getHeight(), this.getWidth());
            gr.setColor(this.getBackground());
            gr.fillRect(0, 0, size, size);
            gr.setColor(Color.BLACK);
            visualizer.visualize(gr, size, labyrinth);
        }
    }
    
    public MainFrame(Labyrinth lab, LabyrinthVisualizer visualizer){
        this.labyrinth = lab;
        this.visualizer = visualizer;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);        
        this.setSize(500, 500);
        this.setVisible(true);
        
        this.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()){
                    case KeyEvent.VK_UP:
                        labyrinth.movePlayer(Coordinate.Direction.UP);
                        break;
                        
                    case KeyEvent.VK_RIGHT:
                        labyrinth.movePlayer(Coordinate.Direction.RIGHT);
                        break;
                        
                    case KeyEvent.VK_DOWN:    
                        labyrinth.movePlayer(Coordinate.Direction.DOWN);
                        break;
                        
                    case KeyEvent.VK_LEFT:    
                        labyrinth.movePlayer(Coordinate.Direction.LEFT);
                        break;
                }
                labPanel.repaint();
            }
        });
        
        this.add(labPanel);
    }
}


