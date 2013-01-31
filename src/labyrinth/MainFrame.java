package labyrinth;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import labyrinth.Labyrinth.IGameEndCallback;

public class MainFrame extends JFrame {
    private Labyrinth labyrinth;
    private ILabyrinthVisualizer visualizer;
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
    
    private IGameEndCallback makeCallback(final MainFrame frm){
        return new IGameEndCallback(){
            @Override
            public void gameEnded() {
                JOptionPane.showMessageDialog(frm, "You win!");
            }
            
        };
    }
    
    public MainFrame(Labyrinth lab, ILabyrinthVisualizer visualizer){
//        JOptionPane.showMessageDialog(this, "You win!");
        this.labyrinth = lab;
        lab.setCallback(makeCallback(this));
                
        this.visualizer = visualizer;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);        
        this.setSize(500, 500);
        this.setVisible(true);
        
        this.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()){
                    case KeyEvent.VK_UP:
                    case KeyEvent.VK_W:
                    case KeyEvent.VK_K:
                            labyrinth.movePlayer(Coordinate.Direction.UP);
                            break;
                        
                    case KeyEvent.VK_RIGHT:
                    case KeyEvent.VK_D:
                    case KeyEvent.VK_L:
                        labyrinth.movePlayer(Coordinate.Direction.RIGHT);
                        break;
                        
                    case KeyEvent.VK_DOWN:    
                    case KeyEvent.VK_S:
                    case KeyEvent.VK_J:
                        labyrinth.movePlayer(Coordinate.Direction.DOWN);
                        break;
                        
                    case KeyEvent.VK_LEFT:
                    case KeyEvent.VK_A:
                    case KeyEvent.VK_H:
                        labyrinth.movePlayer(Coordinate.Direction.LEFT);
                        break;
                }
                labPanel.repaint();
            }
        });
        this.add(labPanel);
    }
}


