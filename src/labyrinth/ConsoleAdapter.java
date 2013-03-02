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
    }
}