package labyrinth;

import java.util.*;

class Coordinate{  
    enum Direction { UP, RIGHT, DOWN, LEFT }
    
    public int x, y;
    public Coordinate(int x, int y){
        this.x = x;
        this.y = y;
    }
    public Coordinate(Coordinate from, Direction dir){
        switch (dir){
            case UP: 
                this.x = from.x;
                this.y = from.y - 1;
                break;
            
            case RIGHT: 
                this.x = from.x + 1;
                this.y = from.y;
                break;
            
            case DOWN:
                this.x = from.x;
                this.y = from.y + 1;
                break;
            
            case LEFT:
                this.x = from.x - 1;
                this.y = from.y;
                break;
        }
    }
}

public class Labyrinth {   
    enum CellContent {NONE, PLAYER, GOAL}
    public class Cell{
        public boolean down = true,
                       right = true;
        public CellContent content = CellContent.NONE;
        
        public Cell(){}
        public Cell(boolean down, boolean right){
            this.down = down;
            this.right = right;
        }       
    }
    
    public interface IGameEndCallback{
        public void gameEnded();
    }
    
    private int size;
    private Cell[][] labyrinth;
    private Coordinate playerCoordinate;
    private IGameEndCallback callBack;
    boolean gameInProgress = false;
    
    public Coordinate getPlayerCoordinate(){
        return playerCoordinate;
    }
    
    
    public Labyrinth(int size){
        this.size = size;
        labyrinth = new Cell[size][];
        for(int i = 0; i < size; i++){
            labyrinth[i] = new Cell[size];
            for(int j = 0; j < size; j++){
                labyrinth[i][j] = new Cell();
            }
        }
    }
    
    public void setCallback(IGameEndCallback callback){
        this.callBack = callback;
    }
    
    public void initializeGame(){
        gameInProgress = true;
        ArrayList<Coordinate> pts = findDiametr();
        getCell(pts.get(0).x, pts.get(0).y).content = CellContent.PLAYER;
        playerCoordinate = pts.get(0);
        
        getCell(pts.get(1).x, pts.get(1).y).content = CellContent.GOAL;
    }
    
    public void movePlayer(Coordinate.Direction dir){
        if(gameInProgress){
            if(!checkWall(playerCoordinate, dir)){
                getCell(playerCoordinate).content = CellContent.NONE;

                playerCoordinate = new Coordinate(playerCoordinate, dir);
                if(getCell(playerCoordinate).content == CellContent.GOAL){
                    callBack.gameEnded();
                    gameInProgress = false;            
                }
                getCell(playerCoordinate).content = CellContent.PLAYER;
            }
        }
    }
    
    public int getSize(){return size;}
        
    public Cell getCell(int x, int y){
        return labyrinth[x][y];
    }
    
    public Cell getCell(Coordinate coord){
        return labyrinth[coord.x][coord.y];
    }
    
    public void breakWall(int x, int y, Coordinate.Direction dir){
       switch (dir){
            case UP: 
                labyrinth[x][y-1].down = false;
                break;
            
            case RIGHT: 
                labyrinth[x][y].right = false;
                break;
            
            case DOWN:
                labyrinth[x][y].down = false;
                break;
            
            case LEFT:
                labyrinth[x-1][y].right = false;
                break;
        } 
    }
    
    public void breakWall(Coordinate coord, Coordinate.Direction dir){
       breakWall(coord.x, coord.y, dir);
    }
    
    public boolean checkWall(int x, int y, Coordinate.Direction dir){
        switch (dir){
            case UP: 
                if(y > 0 && labyrinth[x][y-1].down == false) return false;
                break;
            
            case RIGHT: 
                if(labyrinth[x][y].right == false) return false;
                break;
            
            case DOWN:
                if(labyrinth[x][y].down == false) return false;
                break;
            
            case LEFT:
                if(x > 0 && labyrinth[x-1][y].right == false) return false;
                break;
        }
        return true;
    }
    
    public boolean checkWall(Coordinate coord, Coordinate.Direction dir){
        return checkWall(coord.x, coord.y, dir);
    }
    
    public Coordinate farestCell(Coordinate begin){
        int [][] length = new int[this.size][this.size];
        int inf = this.size * this.size + 1;
        for(int i = 0; i < this.size; i++){
            for(int j = 0; j < this.size; j++){
                length[i][j] = inf;
            }
        }
        length[begin.x][begin.y] = 0;
        
        Queue<Coordinate> cells = new LinkedList<>();
        int maxPath = 0;
        Coordinate farestPoint = begin;
        
        cells.add(begin);
        while(!cells.isEmpty()){
            Coordinate curCoord = cells.poll();
            int x = curCoord.x;
            int y = curCoord.y;
            int curLength = length[x][y];
            if(curLength > maxPath){
                farestPoint =  curCoord;
                maxPath = curLength;
            }
            if(!checkWall(curCoord, Coordinate.Direction.UP)){
                if(length[x][y - 1] > curLength + 1){
                    cells.add(new Coordinate(curCoord, Coordinate.Direction.UP));
                    length[x][y - 1] = curLength + 1;
                }
            }
            
            if(!checkWall(curCoord, Coordinate.Direction.RIGHT)){
                if(length[x + 1][y] > curLength + 1){
                    cells.add(new Coordinate(curCoord, Coordinate.Direction.RIGHT));
                    length[x + 1][y] = curLength + 1;
                }
            }
            
            if(!checkWall(curCoord, Coordinate.Direction.DOWN)){
                if(length[x][y + 1] > curLength + 1){
                    cells.add(new Coordinate(curCoord, Coordinate.Direction.DOWN));
                    length[x][y + 1] = curLength + 1;
                }
            }
            
            if(!checkWall(curCoord, Coordinate.Direction.LEFT)){
                if(length[x - 1][y] > curLength + 1){
                    cells.add(new Coordinate(curCoord, Coordinate.Direction.LEFT));
                    length[x - 1][y] = curLength + 1;
                }
            }
        }
        
        return farestPoint;
    }
    
    public ArrayList<Coordinate> findDiametr(){
        ArrayList<Coordinate> res = new ArrayList<>();
        Coordinate diamCell1 = farestCell(new Coordinate(0, 0));
        Coordinate diamCell2 = farestCell(diamCell1);
        res.add(diamCell1);
        res.add(diamCell2);
        return res;
    }
}