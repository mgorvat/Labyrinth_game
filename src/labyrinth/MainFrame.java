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
        JMenuBar menuBar = new JMenuBar();
        JMenu intefaceMenu = new JMenu("Interfaces");
        
        JMenuItem basicInterfaceItem = new JMenuItem("Basic inteface");
        JMenuItem squareInterfaceItem = new JMenuItem("Square interface");
        JMenuItem minimalisticInterfaceItem = new JMenuItem("Minimalistic interface");
        JMenuItem ellipseInterfaceItem = new JMenuItem("Elliptical interface");
        JMenuItem curveInterfaceItem = new JMenuItem("Curve interface");
        JMenuItem triangleInterfaceItem = new JMenuItem("Triangle interface");
        
        
        basicInterfaceItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                visualizer = new BasicLabyrinthVisualizer();
                labPanel.repaint();
            }
        });
        
        minimalisticInterfaceItem.addActionListener(new ActionListener(){
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
        
        ellipseInterfaceItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                visualizer = new EllipseLabyrinthVisualizer();
                labPanel.repaint();
            }
        });
                
        
        intefaceMenu.add(basicInterfaceItem);
        intefaceMenu.add(squareInterfaceItem); 
        intefaceMenu.add(minimalisticInterfaceItem);
        intefaceMenu.add(ellipseInterfaceItem); 
        intefaceMenu.add(curveInterfaceItem);
        intefaceMenu.add(triangleInterfaceItem); 
        
       
        JMenu modeMenu = new JMenu("Visualizator modes");
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
        
        
        JMenu algorithmsMenu = new JMenu("Labyrinth generators");
        
        
        JMenuItem huntAndKillGeneratorItem = new JMenuItem("Hunt and kill");
        JMenuItem backTrackGeneratorItem = new JMenuItem("Backtracking");
        JMenuItem divideEtImperaGeneratorItem = new JMenuItem("Divide and conquer");
        
        algorithmsMenu.add(huntAndKillGeneratorItem);
        algorithmsMenu.add(backTrackGeneratorItem);
        algorithmsMenu.add(divideEtImperaGeneratorItem);
        
        huntAndKillGeneratorItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                generator = new HuntAndKillGenerator();
                initializeLabyrinth();
                labPanel.repaint();
            }
        });
        
        backTrackGeneratorItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                generator = new BackTrackGenerator();
                initializeLabyrinth();
                labPanel.repaint();
            }
        });
        
        divideEtImperaGeneratorItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                generator = new DivideEtImperaGenerator();
                initializeLabyrinth();
                labPanel.repaint();
            }
        });
        
        menuBar.add(intefaceMenu);
        menuBar.add(modeMenu);
        menuBar.add(algorithmsMenu);
        
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


