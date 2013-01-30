package labyrinth;


public class ConsoleAdapter {
    public static void visualize(Labyrinth lab){
        int size = lab.getSize();
        System.out.print("+");
        for(int i = 0; i < size; i++){
            System.out.print("--+");
        }
        System.out.println();
        
        for(int i = 0; i < size; i++){
            System.out.print("|");
            for(int j = 0; j < size; j++){
                String s;
                if(lab.getCell(j, i).right == true)s = "  |";
                else s = "   ";
                System.out.print(s);
            }
            System.out.println();
            
            System.out.print("+");
            for(int j = 0; j < size; j++){
                String s;
                if(lab.getCell(j, i).down == true)s = "--+";
                else s = "  +";
                System.out.print(s);
            }
            System.out.println();
        }
        System.out.println();
        
        int buf = 9;
        if(lab.getCell(0, 0).right == true)buf += 2;
        if(lab.getCell(0, 0).down == true)buf += 4;
        if(buf < 10)System.out.print(" ");
        System.out.print(buf + " ");
        for(int i = 1; i < size; i++){
            buf = 1;
            if(lab.getCell(i, 0).right == true)buf += 2;
            if(lab.getCell(i, 0).down == true)buf += 4;
            if(lab.getCell(i-1, 0).right == true)buf += 8;
            if(buf < 10)System.out.print(" ");
            System.out.print(buf + " ");
        }
        System.out.println();
        for(int i = 1; i < size; i++){
            buf = 8;
            if(lab.getCell(0, i - 1).down == true)buf += 1;
            if(lab.getCell(0, i).right == true)buf += 2;
            if(lab.getCell(0, i).down == true)buf += 4;
            if(buf < 10)System.out.print(" ");
            System.out.print(buf + " ");
            
            for(int j = 1; j < size; j++){
                buf = 0;
                if(lab.getCell(j, i - 1).down == true)buf += 1;
                if(lab.getCell(j, i).right == true)buf += 2;
                if(lab.getCell(j, i).down == true)buf += 4;
                if(lab.getCell(j - 1, i).right == true)buf += 8;
                
                if(buf < 10)System.out.print(" ");
                System.out.print(buf + " ");
            }
            System.out.println();
        }
        
    }
}
