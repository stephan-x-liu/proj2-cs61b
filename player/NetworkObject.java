package player;

/**
*  An object representation of network with a length and an array of squares.
**/

public class NetworkObject{
	protected Square[] network;
	protected int length;
	/**
	*  Constructor for network object.
	*  @param n is an array of Squares that represents the network.
	*  @param l is an integer representing the length of the network.
	**/
	public NetworkObject(Square[] n, int l){
		length = l;
		network = n;
	}
}