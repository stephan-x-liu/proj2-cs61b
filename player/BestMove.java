package player;

/**
*  Best move object for ab pruning and minimax.
**/

public class BestMove{
  protected Move move;
  protected int score;
  public BestMove(){
    move = null;
    score = 0;
  }
}