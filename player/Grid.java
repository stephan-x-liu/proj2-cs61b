package player;

public class Grid{
	private Field[][] board;

	public Grid(){
		board = new Field[8][8];
		for(int i = 0; i < 8; i ++){
			for(int j = 0; j < 8; j++){
				board[i][j] = new Field(i,j,null);
			}
		}
	}

	public Field get(int x, int y){
		try{
			return board[x][y];
		}
		catch(ArrayIndexOutOfBoundsException e){
			return null;
		}
		
	}

	public Field neighbor(int x, int y){
		return board[x][y].neighbor();
	}

}