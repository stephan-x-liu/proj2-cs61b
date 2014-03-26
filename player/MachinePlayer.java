/* MachinePlayer.java */

package player;

/**
 *  An implementation of an automatic Network player.  Keeps track of moves
 *  made by both players.  Can select a move for itself.
 */
public class MachinePlayer extends Player {

  // Creates a machine player with the given color.  Color is either 0 (black)
  // or 1 (white).  (White has the first move.)
  public MachinePlayer(int color) {
  }

  // Creates a machine player with the given color and search depth.  Color is
  // either 0 (black) or 1 (white).  (White has the first move.)
  public MachinePlayer(int color, int searchDepth) {
  }

  // Returns a new move by "this" player.  Internally records the move (updates
  // the internal game board) as a move by "this" player.
  public Move chooseMove() {
    Square[] moves = grid.validMoves(color);
    int[] bestMove = abMaximizer(Integer.MIN_VALUE,Integer.MAX_VALUE,searchDepth,this.grid,this.color);
    Square move = moves[bestMove[1]];
  
  } 

  // If the Move m is legal, records the move as a move by the opponent
  // (updates the internal game board) and returns true.  If the move is
  // illegal, returns false without modifying the internal state of "this"
  // player.  This method allows your opponents to inform you of their moves.
  public boolean opponentMove(Move m) {
    return false;
  }

  // If the Move m is legal, records the move as a move by "this" player
  // (updates the internal game board) and returns true.  If the move is
  // illegal, returns false without modifying the internal state of "this"
  // player.  This method is used to help set up "Network problems" for your
  // player to solve.
  public boolean forceMove(Move m) {
    return false;
  }

  public int[] abMaximizer(int a, int b, int searchDepth, Grid g, int color) {
    int bestMoveIndex=-1;
    int score;
    if (g.hasWinningNetwork()) {
      return Integer.MAX_VALUE;
    }
    if(searchDepth == 0){
      return g.evaluate();
    }
    Square[] moves = g.validMoves(color);
    for(i = 0; i < moves.length && i < 15; i++){
      Grid temp = new Grid(g.board);
      temp.makeMove(moves[i]);
      score = abMinimizer(a,b,searchDepth-1,temp,(color+1)%2);
      if(score>= b){
        return b;
      }
      if(score > alpha){
        bestMoveIndex = i;
        a = score;
      }
    }
    return {a,i};
  }
  public int abMinimizer(int a, int b, int searchDepth, Grid g, int color) {
    int score;
    if (g.hasWinningNetwork()) {
      return Integer.MIN_VALUE;
    }
    if(searchDepth == 0){
      return g.evaluate();
    }
    Square[] moves = g.validMoves(color);
    for(i = 0; i < moves.length && i < 15; i++){
      Grid temp = new Grid(g.board);
      temp.makeMove(moves[i]);
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

