package labyrinth;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import labyrinth.Labyrinth.IGameEndCallback;

public class MainFrame extends JFrame {
    private IMazeGenerator generator;
    private Labyrinth labyrinth;
    private LabyrinthVisualizer visualizer;
    VisualizatorMode mode = VisualizatorMode.FULL;
    int labyrinthSize = 12;
        
    
    private JPanel labPanel = new LabyrinthPanel();
    
    class LabyrinthPanel extends JPanel{
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            if(labyrinth != null){
                Graphics2D gr = (Graphics2D)g;
                int size = Math.min(this.getHeight(), this.getWidth());
                visualizer.visualize(gr, size, labyrinth, mode);
            }
        }
    }
    
    private IGameEndCallback makeCallback(final MainFrame frm){
        return new IGameEndCallback(){
            @Override
            public void gameEnded() {
                JOptionPane.showMessageDialog(frm, "You win!");
                initializeLabyrinth();
            }
        };
    }
    
    public MainFrame(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);        
        this.setSize(500, 500);
        JMenuBar menuBar = new JMenuBar();
        JMenu intefaceMenu = new JMenu("Interfaces");
        
        JMenuItem basicInterfaceItem = new JMenuItem("Basic inteface");
        JMenuItem lineInterfaceItem = new JMenuItem("Minimalistic interface");
        JMenuItem curveInterfaceItem = new JMenuItem("Curve interface");
        JMenuItem triangleInterfaceItem = new JMenuItem("Triangle interface");
        JMenuItem squareInterfaceItem = new JMenuItem("Square interface");
        
        basicInterfaceItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                visualizer = new BasicLabyrinthVisualizer();
                labPanel.repaint();
            }
        });
        
        lineInterfaceItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                visualizer = new MinimalisticLabyrinthVisualizer();
                labPanel.repaint();
            }
        });
        
        curveInterfaceItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                visualizer = new CurveLabyrinthVisualizer();
                labPanel.repaint();
            }
        });
        
        triangleInterfaceItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                visualizer = new TriangleLabyrinthVisualizer();
                labPanel.repaint();
            }
        });
        
        squareInterfaceItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                visualizer = new SquareLabyrinthVisualizer();
                labPanel.repaint();
            }
        });
                
        
        intefaceMenu.add(basicInterfaceItem);
        intefaceMenu.add(curveInterfaceItem);
        intefaceMenu.add(lineInterfaceItem);
        intefaceMenu.add(triangleInterfaceItem); 
        intefaceMenu.add(squareInterfaceItem); 
       
        JMenu modeMenu = new JMenu("Modes");
        JMenuItem fullModeItem = new JMenuItem("Full");
        JMenuItem nearestModeItem = new JMenuItem("Nearest");
        JMenuItem sightModeItem = new JMenuItem("Sight line");
        fullModeItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                mode = VisualizatorMode.FULL;
                labPanel.repaint();
            }
        });
        
        nearestModeItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                mode = VisualizatorMode.NEAREST;
                labPanel.repaint();
            }
        });
        
        sightModeItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                mode = VisualizatorMode.SIGHT;
                labPanel.repaint();
            }
        });
        modeMenu.add(fullModeItem);
        modeMenu.add(nearestModeItem);
        modeMenu.add(sightModeItem);
        
        menuBar.add(intefaceMenu);
        menuBar.add(modeMenu);
        
        labPanel.addKeyListener(new KeyAdapter(){
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
        
        this.setJMenuBar(menuBar);
        this.add(labPanel);
        labPanel.setFocusable(true);
    }
    
    public void setOptions(IMazeGenerator generator, LabyrinthVisualizer visualizer){
        this.generator = generator;
        this.visualizer = visualizer;
    }
    
    public void initializeLabyrinth(){
        labyrinth = generator.generate(this.labyrinthSize);
        
        labyrinth.setCallback(makeCallback(this));
        labyrinth.initializeGame();
    }
}


