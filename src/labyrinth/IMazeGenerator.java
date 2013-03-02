package labyrinth;

import java.util.*;

public interface IMazeGenerator{
    public Labyrinth generate(int size);
}


class BackTrackGenerator implements IMazeGenerator{
    @Override
    public Labyrinth generate(int size){
        Labyrinth lab = new Labyrinth(size);
        boolean[][] visited = new boolean[size][size];
        Stack<Coordinate> stack = new Stack<>();
        Random rnd = new Random();
        
        stack.push(new Coordinate(rnd.nextInt(size), rnd.nextInt(size)));
        
        while(stack.size() > 0){
            Coordinate coord = stack.peek();
            visited[coord.x][coord.y] = true;
            
            ArrayList<Coordinate.Direction> dirs = MazeGeneratorsUtils.getUnvisitedWays(visited, coord);
            if(dirs.size() > 0){
                int ind = rnd.nextInt(dirs.size());
                Coordinate.Direction dir = dirs.get(ind);
                lab.breakWall(coord, dir);
                stack.push(new Coordinate(coord, dir));
            }else{
                stack.pop();
            } 
        } 
        return lab;
    }
    @Override
    public String toString(){
        return "Backtrack";
    }
}

class HuntAndKillGenerator implements IMazeGenerator{
    @Override
    public Labyrinth generate(int size) {
        int[] deltX = new int[]{0, 1, 0, -1};
        int[] deltY = new int[]{-1, 0, 1, 0};
        
        Labyrinth lab = new Labyrinth(size);
        boolean[][] visited = new boolean[size][size];
        Random rnd = new Random();
        
        
        Coordinate coord = new Coordinate(rnd.nextInt(size), rnd.nextInt(size));
        while(coord != null){
            visited[coord.x][coord.y] = true;
            ArrayList<Coordinate.Direction> dirs = MazeGeneratorsUtils.getUnvisitedWays(visited, coord);
            if(dirs.size() > 0){
                Coordinate.Direction dir = dirs.get(rnd.nextInt(dirs.size()));
                lab.breakWall(coord, dir);
                coord = new Coordinate(coord, dir);
            }else{
                int newX = -1, newY = -1;
                outer: for(int i = 0; i < size; i++){
                    for(int j = 0; j < size; j++){
                        if(visited[i][j] == false){
                            for(int k = 0; k < 4; k++){
                                if(MazeGeneratorsUtils.isVisited(visited, i + deltX[k], j + deltY[k])){
                                    newX = i;
                                    newY = j;
                                    switch (k){
                                        case 0:
                                            lab.breakWall(i, j, Coordinate.Direction.UP);
                                            break;
                                        case 1:
                                            lab.breakWall(i, j, Coordinate.Direction.RIGHT);
                                            break;
                                        case 2:
                                            lab.breakWall(i, j, Coordinate.Direction.DOWN);
                                            break;
                                        case 3:
                                            lab.breakWall(i, j, Coordinate.Direction.LEFT);
                                            break;
                                    }
                                    
                                    break outer;
                                }
                            }
                        }
                    }
                }
                if(newX == -1){
                    coord = null;
                }else{
                    coord = new Coordinate(newX, newY);
                }
            }
        }
        return lab;
    }
    
    @Override
    public String toString(){
        return "Hunt and kill";
    }
}




class DivideEtImperaGenerator implements IMazeGenerator{
    @Override
    public Labyrinth generate(int size) {
        Labyrinth lab = new Labyrinth(size);
        divideEtImpera(lab, 0, 0, size - 1, size - 1, new Random());
        return lab;
    }
    
    private static void divideEtImpera(Labyrinth lab, int x1, int y1, int x2, int y2, Random rnd){
        if(x2 - x1 == 0){
            for(int i = y1; i < y2; i++){
                lab.breakWall(x1, i, Coordinate.Direction.DOWN);
            }
            return;
        }
        if(y2 - y1 == 0){
            for(int i = x1; i < x2; i++){
                lab.breakWall(i, y1, Coordinate.Direction.RIGHT);
            }
            return;
        }
        int xLine, yLine;
        xLine = rnd.nextInt(x2-x1) + x1;
        yLine = rnd.nextInt(y2-y1) + y1;
        
        int wallInd = rnd.nextInt(4);
        if(wallInd != 0){
            int portY = rnd.nextInt(yLine - y1 + 1) + y1;
            lab.breakWall(xLine, portY, Coordinate.Direction.RIGHT);
        }
        if(wallInd != 1){
            int portX = rnd.nextInt(x2 - xLine) + xLine + 1;
            lab.breakWall(portX, yLine, Coordinate.Direction.DOWN);
        }
        if(wallInd != 2){
            int portY = rnd.nextInt(y2 - yLine) + yLine + 1;
            lab.breakWall(xLine, portY, Coordinate.Direction.RIGHT);
        }
        if(wallInd != 3){
            int portX = rnd.nextInt(xLine - x1 + 1) + x1;
            lab.breakWall(portX, yLine, Coordinate.Direction.DOWN);
        }
              
        divideEtImpera(lab, x1, y1, xLine, yLine, rnd);
        divideEtImpera(lab, xLine + 1, y1, x2, yLine, rnd);
        divideEtImpera(lab, x1, yLine + 1, xLine, y2, rnd);
        divideEtImpera(lab, xLine + 1, yLine + 1, x2, y2, rnd);
    }
    @Override
    public String toString(){
        return "Divide and conquer";
    }
}



class MazeGeneratorsUtils {  
    public static ArrayList<Coordinate.Direction> getUnvisitedWays(boolean[][] vis, Coordinate coord){
        int x = coord.x;
        int y = coord.y;
        ArrayList<Coordinate.Direction> res = new ArrayList<>();
        
        if(x > 0 && vis[x-1][y] == false)res.add(Coordinate.Direction.LEFT);
        if(y > 0 && vis[x][y-1] == false)res.add(Coordinate.Direction.UP);
        if(x < vis.length - 1 && vis[x+1][y] == false)res.add(Coordinate.Direction.RIGHT);
        if(y < vis.length - 1 && vis[x][y+1] == false)res.add(Coordinate.Direction.DOWN); 
        return res;
    }
    
    public static boolean isVisited(boolean[][] vis, int x, int y){
        if(x < 0 || y < 0 || x >= vis.length|| y >= vis.length)return false;
        return vis[x][y];
    }
}