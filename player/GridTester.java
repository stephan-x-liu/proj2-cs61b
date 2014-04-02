package player;
import java.io.*;

/**
*  Class containing purely test code for various parts of the game.
*  DOES NOT AFFECT FUNCTIONALITY OF THE GAME.
**/


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
  
  public static void chanceToWin1(){
    Grid g = new Grid();
    g.set(1,1,BLACK);
    g.set(2,1,BLACK);
    g.set(4,1,BLACK);
    g.set(5,1,BLACK);
    g.set(1,6,BLACK);
    g.set(0,2,WHITE);
    g.set(1,2,WHITE);
    g.set(4,2,WHITE);
    g.set(1,5,WHITE);
    g.set(4,5,WHITE);

    System.out.println(g.simpleToString());
    MachinePlayer m = new MachinePlayer(WHITE,g,3);
    System.out.println(m.chooseMove());

  }

  public static void chanceToWin2(){
    Grid g = new Grid();
    g.set(1,1,BLACK);
    g.set(2,1,BLACK);
    g.set(4,1,BLACK);
    g.set(5,1,BLACK);
    g.set(1,6,BLACK);
    g.set(2,6,BLACK);
    g.set(5,5,BLACK);
    g.set(0,2,WHITE);
    g.set(1,3,WHITE);
    g.set(3,2,WHITE);
    g.set(5,3,WHITE);
    g.set(6,1,WHITE);
    g.set(3,5,WHITE);
    g.set(6,5,WHITE);
    System.out.println(g.simpleToString());
    MachinePlayer m = new MachinePlayer(WHITE,g,3);
    System.out.println(m.chooseMove());
  }

  public static void blockingTest(){
    Grid g = new Grid();
    g.set(6,7,BLACK);
    g.set(6,4,BLACK);
    g.set(3,4,BLACK);
    g.set(1,0,BLACK);
    g.set(1,6,BLACK);
    g.set(0,1,WHITE);
    g.set(7,4,WHITE);
    g.set(2,3,WHITE);
    g.set(2,2,WHITE);
    g.set(6,1,WHITE);
    System.out.println(g.simpleToString());
    MachinePlayer m = new MachinePlayer(WHITE,g,3);
    System.out.println(m.chooseMove());
  }

  public static void lookAheadTest1(){
    Grid g = new Grid();
    g.set(3,3,BLACK);
    g.set(3,4,BLACK);
    g.set(5,5,BLACK);
    g.set(5,6,BLACK);
    g.set(6,3,BLACK);
    g.set(0,3,WHITE);
    g.set(2,3,WHITE);
    g.set(2,4,WHITE);
    g.set(4,6,WHITE);
    g.set(0,5,WHITE);
    System.out.println(g.simpleToString());
    MachinePlayer m = new MachinePlayer(WHITE,g,3);
    System.out.println(m.chooseMove());
  }

  public static void lookAheadTest2(){
    Grid g = new Grid();
    g.set(1,2,BLACK);
    g.set(1,0,BLACK);
    g.set(6,2,BLACK);
    g.set(6,7,BLACK);
    g.set(0,2,WHITE);
    g.set(4,6,WHITE);
    g.set(1,6,WHITE);
    g.set(4,3,WHITE);
    System.out.println(g.simpleToString());
    MachinePlayer m = new MachinePlayer(WHITE,g,3);
    System.out.println(m.chooseMove());
  }




  public static void main(String args[]){
    chanceToWin2();
    chanceToWin1();
    blockingTest();
    lookAheadTest1();
    lookAheadTest2();


  }
}
