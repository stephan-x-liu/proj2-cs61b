package player;

public class Grid{
	private Square[][] board;
	//put in length 10 array of black pieces
	//put in length 10 array of white pieces
	public Grid(){
		board = new Square[8][8];
		for(int i = 0; i < 8; i ++){
			for(int j = 0; j < 8; j++){
				board[i][j] = new Square(i,j,null);
			}
		}
	}

	public Square get(int x, int y){
		try{
			return board[x][y];
		}
		catch(ArrayIndexOutOfBoundsException e){
			return null;
		}
		
	}

	private Square neighbor(int x, int y){
		return board[x][y].neighbor();
	}

	public Square[] validMoves(int piece){

	}

}