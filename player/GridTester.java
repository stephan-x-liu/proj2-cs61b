package player;
import java.io.*;

public class GridTester{
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
        input[i][j] = field;
      }
    }
    return new Grid(input);
  }

  public static void main(String args[]){
    GridTester tester = new GridTester();
    Grid grid = tester.gridFromInput();
    grid.updateNetworkList();
    System.out.println(grid);
  }
}
