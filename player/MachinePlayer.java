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
  final Grid grid;
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
  }

  // Returns a new move by "this" player.  Internally records the move (updates
  // the internal game board) as a move by "this" player.
  public Move chooseMove() {
    Move[] moves = grid.validMoves(color);
    int[] bestMove = abMaximizer(Integer.MIN_VALUE,Integer.MAX_VALUE,searchDepth,this.grid,this.color);
    Move move = moves[bestMove[1]];
    grid.makeMove(move, color);
    return move;
  
  } 

  // If the Move m is legal, records the move as a move by the opponent
  // (updates the internal game board) and returns true.  If the move is
  // illegal, returns false without modifying the internal state of "this"
  // player.  This method allows your opponents to inform you of their moves.
  public boolean opponentMove(Move m) {
    int opponent;
    if (color==BLACK){
      opponent = WHITE;
    }
    if (color==WHITE){
      opponent = BLACK;
    }
    if (!grid.isValidMove(m, opponent)){
      return false;
    } else {
      grid.makeMove(m, opponent);
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

  public int[] abMaximizer(int a, int b, int searchDepth, Grid g, int color) {
    int bestMoveIndex=-1;
    int score;
    //if (g.hasWinningNetwork()) {
    //  ret = {Integer.MAX_VALUE,-1};
    //  return ret;
    //}
    if(searchDepth == 0){
      return g.evaluate();
    }
    Move[] moves = g.validMoves(color);
    for(int i = 0; i < moves.length && i < 15; i++){
      Grid temp = new Grid(g.board());
      temp.makeMove(moves[i],color);
      score = abMinimizer(a,b,searchDepth-1,temp,(color+1)%2);
      if(score>= b){
        int[] ret = {b,-1};
        return ret;
      }
      if(score > a){
        bestMoveIndex = i;
        a = score;
      }
    }
    int[] ret = {a,bestMoveIndex};
    return ret;
  }

  public int abMinimizer(int a, int b, int searchDepth, Grid g, int color) {
    int score;
    //if (g.hasWinningNetwork()) {
     // return Integer.MIN_VALUE;
    //}
    if(searchDepth == 0){
      return g.evaluate();
    }
    Move[] moves = g.validMoves(color);
    for(int i = 0; i < moves.length && i < 15; i++){
      Grid temp = new Grid(g.board());
      temp.makeMove(moves[i],color);
      int[] maxed = abMaximizer(a,b,searchDepth-1,temp,(color+1)%2);
      score = maxed[0];
      if(score<= a){
        return a;
      }
      if(score < b){
        b = score;
      }
    }
    return b;
  }
}

