/* MachinePlayer.java */

package player;

/**
 *  An implementation of an automatic Network player.  Keeps track of moves
 *  made by both players.  Can select a move for itself.
 */
public class MachinePlayer extends Player {
  static final int BLACK = 0;
  static final int WHITE = 1;
  final int color;
  final int opponent;
  Grid grid;
  final int searchDepth;
  // Creates a machine player with the given color.  Color is either 0 (black)
  // or 1 (white).  (White has the first move.)
  public MachinePlayer(int color) {
    this(color,3);
  }

  // Creates a machine player with the given color and search depth.  Color is
  // either 0 (black) or 1 (white).  (White has the first move.)
  public MachinePlayer(int color, int searchDepth) {
    this.color = color;
    grid = new Grid();
    this.searchDepth = searchDepth;
    opponent = (color + 1)%2;
  }

  // Returns a new move by "this" player.  Internally records the move (updates
  // the internal game board) as a move by "this" player.
  public Move chooseMove() {
    //System.out.println(grid.simpleToString());
    
    BestMove bestMove = abMaximizer(Integer.MIN_VALUE,Integer.MAX_VALUE,searchDepth,grid,color);

    grid.makeMove(bestMove.move, color);

    // if (color==BLACK){
    //   System.out.println("BLACK MOVE! " + bestMove.move);
    // }
    // else {
    //   System.out.println("WHITE MOVE! " + bestMove.move);
    // }
    // System.out.println(grid.simpleToString());

    return bestMove.move;
  
  }

  public static Move chooseMove(Grid g, int color){

    MachinePlayer mp = new MachinePlayer(color);
    mp.grid = g;

    return mp.chooseMove();

  }



  // If the Move m is legal, records the move as a move by the opponent
  // (updates the internal game board) and returns true.  If the move is
  // illegal, returns false without modifying the internal state of "this"
  // player.  This method allows your opponents to inform you of their moves.
  public boolean opponentMove(Move m) {
    if (!grid.isValidMove(m, this.opponent)){
      return false;
    } else {
      grid.makeMove(m, this.opponent);
      // if (color==BLACK){
      //   System.out.println("WHITE MOVE!");
      // }
      // else {
      //   System.out.println("BLACK MOVE!");
      // }
      // System.out.println(grid.simpleToString());

      return true;
    }
  }

  // If the Move m is legal, records the move as a move by "this" player
  // (updates the internal game board) and returns true.  If the move is
  // illegal, returns false without modifying the internal state of "this"
  // player.  This method is used to help set up "Network problems" for your
  // player to solve.
  public boolean forceMove(Move m) {
    if (!grid.isValidMove(m, color)){
      return false;
    } else {
      grid.makeMove(m, color);
      return true;
    }
  }

  public BestMove abMaximizer(int a, int b, int searchDepth, Grid g, int color) {
    BestMove bestMove = new BestMove();
    int score;
    Move[] moves = g.validMoves(color);
    if (g.hasWinningNetwork(color)) {
      bestMove.score = Integer.MAX_VALUE;
      return bestMove;
    }
    if(searchDepth == 0){
      g.updateNetworkList();
      bestMove.score = g.evaluate(this.color);
      return bestMove;
    }

    for(int i = 0; i < moves.length && moves[i]!=null; i++){
      Grid temp = g.cloneGrid();
      temp.makeMove(moves[i],color);
      BestMove t = abMinimizer(a,b,searchDepth-1,temp,(color+1)%2);
      score = t.score;
      if(score>= b){
        bestMove.score = b;
        bestMove.move = moves[i];
        return bestMove;
      }
      if(score > a){
        bestMove.score = score;
        bestMove.move = moves[i];
        a = score;
      }
    }

    return bestMove;
  }

  public BestMove abMinimizer(int a, int b, int searchDepth, Grid g, int color) {
    BestMove bestMove = new BestMove();
    int score;
    if (g.hasWinningNetwork(color)) {
      bestMove.score = Integer.MIN_VALUE;
      return bestMove;
    }
    if(searchDepth == 0){
      bestMove.score = g.evaluate(this.color);
      return bestMove;
    }
    Move[] moves = g.validMoves(color);
    for(int i = 0; i < moves.length && moves[i]!=null; i++){
      Grid temp = g.cloneGrid();
      temp.makeMove(moves[i],color);
      BestMove t = abMaximizer(a,b,searchDepth-1,temp,(color+1)%2);
      score = t.score;
      if(score <= a){
        bestMove.score = a;
        bestMove.move = moves[i];
        return bestMove;
      }
      if(score < b){
        bestMove.score = score;
        bestMove.move = moves[i];
        b = score;
      }
    }
    return bestMove;
  }

}

class BestMove{
  Move move;
  int score;
  public BestMove(){
    move = null;
    score = 0;
  }
}

