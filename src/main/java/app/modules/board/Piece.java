package app.modules.board;

import app.utils.enums.PieceColor;
import app.utils.helper.Point;

public class Piece
{
    private PieceColor color;
    private Point position;
    private boolean isKing;

    public Piece(PieceColor color, Point position)
    {
        this.color = color;
        this.position = position;
        isKing = false;
    }

    public PieceColor getColor() { return color; }
    public Point getPoint() { return position; }
    public boolean isKing() { return isKing; }
    public void setPoint(Point newPosition) { position = newPosition; }
    public void setKing() { isKing = true; }
}
