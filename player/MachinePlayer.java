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

  // Machine player constructor purely for testing purposes.
  public MachinePlayer(int color, Grid grid, int searchDepth){
    this.color = color;
    this.grid = grid;
    this.searchDepth = searchDepth;
    opponent = (color + 1)%2;
  }

  // Returns a new move by "this" player.  Internally records the move (updates
  // the internal game board) as a move by "this" player.
  // Uses abMaximizer to find a bestMove given a search depth and grid state.
  public Move chooseMove() {

    BestMove bestMove = abMaximizer(Integer.MIN_VALUE,Integer.MAX_VALUE,searchDepth,grid,color);

    grid.makeMove(bestMove.move, color);

    return bestMove.move;
  
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

  /**
  *  Maximizer portion of ab pruning that recursively calls the minimizer function.
  *  @param a is alpha in ab pruning
  *  @param b is beta in ab pruning
  *  @param searchDepth is the recursive depth of the search
  *  @param g is the grid that is being searched on
  *  @param color is the integer representation of the color
  *  @return returns a bestMove object with a score and move.
  **/
  public BestMove abMaximizer(int a, int b, int searchDepth, Grid g, int color) {
    Move[] moves = g.validMoves(color);
    BestMove bestMove = new BestMove();
    bestMove.move = moves[0];
    int score;

    
    if (g.hasWinningNetwork((color+1)%2)) {
      bestMove.move = moves[0];
      bestMove.score = -9999999-100*searchDepth;
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
  
  /**
  *  Minimizer portion of ab pruning that recursively calls the maximizer function.
  *  @param a is alpha in ab pruning
  *  @param b is beta in ab pruning
  *  @param searchDepth is the recursive depth of the search
  *  @param g is the grid that is being searched on
  *  @param color is the integer representation of the color
  *  @return returns a bestMove object with a score and move.
  **/
  public BestMove abMinimizer(int a, int b, int searchDepth, Grid g, int color) {
    BestMove bestMove = new BestMove();
    int score;
    Move[] moves = g.validMoves(color);
    if (g.hasWinningNetwork((color+1)%2)) {
      bestMove.move = moves[0];
      bestMove.score = 9999999+100*searchDepth;
      return bestMove;
    }
    if(searchDepth == 0){
      bestMove.score = g.evaluate(this.color);
      return bestMove;
    }
    
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



