public class Piece
{
    private PieceColor color;/* = PieceColor.EMPTY;*/
    private Point position;
    private boolean isKing;

    public Piece(PieceColor color, Point position)
    {
        this.color = color;
        this.position = position;
        isKing = false;
    }

    public Piece(PieceColor color, Point position, boolean isKing)
    {
        this.color = color;
        this.position = position;
        this.isKing = true;
    }
    
    public void setColor(PieceColor color){
    	this.color = color;
    }
    
    public PieceColor getColor() { return color; }
    public Point getPoint() { return position; }
    public boolean isKing() { return isKing; }
    public void setPoint(Point newPosition) { position = newPosition; }
    public void setKing() { isKing = true; }
    @Override public Piece clone() {
    	return new Piece(color, position.clone(), isKing);
    }
}
