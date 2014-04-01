package player;
import java.io.*;

public class GridTester{

  static final int BLACK = 0;
  static final int WHITE = 1;
  static final int NONE = 2;
  static final int DIMENSION = 8;

  public GridTester(){
  }

  public Grid gridFromInput(){
    int dim = Grid.DIMENSION;
    int[][] input = new int[dim][dim];
    int i, j;
    String line;


    BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));
    System.out.println("Please input "+dim+" lines of "+dim+" characters. W for white, B for black, . for none.");
    System.out.println("Column: 12345678");
    for(i=0;i<dim;i++){
      System.out.print("Row "+(i+1)+":  ");
      try{
        line = cin.readLine();
      }catch(IOException e){
        line = "Shit";
        System.out.println("IO Exception");
      }
      for(j=0;j<dim;j++){
        char c = line.charAt(j);
        int field;
        if(c == 'W'){
          field = Grid.WHITE; 
        }else if(c == 'B'){
          field = Grid.BLACK;
        }else if(c == '.'){
          field = Grid.NONE;
        }else{
          field = Grid.NONE;
          System.out.println("Seems there was an issue with the input, printing it for reference:");
          System.out.println(line);
        }
        input[j][i] = field;
      }
    }
    return new Grid(input);
  }

  public Grid gridFromSerial(){
    BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));
    System.out.println("Please paste in the seralized grid:");
    String line = "";
    try{
      line = cin.readLine();
    }catch(IOException e){
      line = "Shit2";
      System.out.println("IO Exception2");
    }
    return new Grid(line);
  } 

  public static void printFancyEval(Grid grid, int color){
    String s = "|=====================================|\n";
    s += "|    || 0,| 1,| 2,| 3,| 4,| 5,| 6,| 7,|\n";
    s += "|----||-------------------------------|\n";

    Square sq;
    Grid tmp;
    for(int y = 0;y<grid.DIMENSION;y++){
      s += "|----||-------------------------------|\n";
      s += "| "+y+"_ ||";
      for(int x = 0;x<grid.DIMENSION;x++){
        if(grid.get(x, y).getPiece() == Square.NONE){
          tmp = grid.cloneGrid();
          tmp.makeMove(new Move(x, y),color);
          s+= tmp.evaluate(color)+"|";
        }
        else if(grid.get(x, y).getPiece() == Square.BLACK){
          s += " B |";
        }else if(grid.get(x,y).getPiece() == Square.WHITE){
          s += " W |";
        }
      }
      s += "\n";
    }
  s += "|=====================================|\n";
  System.out.println(s);
  }
        




  public static void main(String args[]){
    GridTester tester = new GridTester();
    Grid grid;// = tester.gridFromSerial();
    //grid.updateNetworkList();
    //System.out.println(grid);
 
    // while(true){
    //   grid = tester.gridFromInput();
    //   grid.updateNetworkList();
    //   System.out.println(grid);
    //   System.out.println("Evaluates for white: "+grid.evaluate(Square.WHITE));
    //   printFancyEval(grid, Square.WHITE);
    //   System.out.println("Evaluates for black: "+grid.evaluate(Square.BLACK));
    //   printFancyEval(grid, Square.BLACK);

    // }
    grid = new Grid();
    grid.set(0,1,WHITE);
    grid.set(7,4,WHITE);
    grid.set(2,3,WHITE);
    grid.set(2,2,WHITE);
    //grid.set(4,4,WHITE);
    grid.set(6,1,WHITE);
    grid.set(6,7,BLACK);
    grid.set(6,4,BLACK);
    grid.set(3,4,BLACK);
    grid.set(1,0,BLACK);
    grid.set(1,6,BLACK);

    MachinePlayer.chooseMove(grid,WHITE);


  }
}
