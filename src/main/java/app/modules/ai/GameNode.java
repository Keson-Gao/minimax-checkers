package app.modules.ai;

import app.modules.board.Board;
import app.utils.enums.PieceColor;
import app.utils.helper.Point;

import java.util.ArrayList;

public class GameNode
{
    private Board board;
    private GameNode parent;
    private ArrayList<GameNode> children;
    private Point[] movement;
    private int whiteScore;
    private int blackScore;

    public GameNode(Board board)
    {
        this.board = board;
        whiteScore = board.getBoardValue(PieceColor.WHITE);
        blackScore = board.getBoardValue(PieceColor.BLACK);
        children = new ArrayList<>();
    }

    public GameNode(int score)
    {
        whiteScore = score;
        blackScore = score;
    }

    public void addChild(GameNode child)
    {
        if (child != null) {
            child.setParent(this);
            children.add(child);
        }
    }

    public void setMovement(Point[] movement)
    {
        this.movement = movement;
        movePiece();
        whiteScore = board.getBoardValue(PieceColor.WHITE);
        blackScore = board.getBoardValue(PieceColor.BLACK);
    }

    public int getScore(PieceColor color)
    {
        if (color == PieceColor.WHITE) {
            return whiteScore;
        }

        return blackScore;
    }

    public GameNode[] getChildren() { return children.toArray(new GameNode[0]); }
    public Board getBoard() { return board; }
    public Point[] getMovement() { return movement; }
    public boolean hasChildren() { return children.size() != 0; }

    private void setParent(GameNode parent) { this.parent = parent; }

    private void movePiece()
    {
        Point origPoint = movement[0];
        PieceColor pieceColor = board.getPieceAt(origPoint).getColor();
        boolean isPieceKing = board.getPieceAt(origPoint).isKing();
        boolean setToKing = false;
        for (int i = 1; i < movement.length; i++) { // Start from the second point.
            if (pieceColor == PieceColor.BLACK && !isPieceKing && movement[i].y == 7) setToKing = true;
            else if (pieceColor == PieceColor.WHITE && !isPieceKing && movement[i].y == 0) setToKing = true;

            if (Math.abs(origPoint.x - movement[i].x) == 2) board.removePieceAt(getMidpoint(origPoint, movement[i]));
            // Programmer's note regarding the statement above: No need to check for the y-axis
            // because we can already check if we did a jump by just using one coordinate. Remember
            // straight lines with slopes of either 1 or -1. Their points would have the same
            // value for the x and y coordinates.

            if (i == movement.length - 1) board.movePieceTo(movement[0], movement[movement.length - 1]);
            else origPoint = movement[i];
        }

        if (setToKing) board.setKingPieceAt(movement[movement.length - 1]);
    }

    private Point getMidpoint(Point source, Point target)
    {
        return (source.x > target.x && source.y > target.y) ? new Point(source.x - 1, source.y - 1) :
               (source.x < target.x && source.y > target.y) ? new Point(source.x + 1, source.y - 1) :
               (source.x > target.x && source.y < target.y) ? new Point(source.x - 1, source.y + 1) :
             /*(source.x < target.x && source.y < target.y)*/ new Point(source.x + 1, source.y + 1);
    }
}
