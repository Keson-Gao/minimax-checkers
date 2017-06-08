package app.modules.ai;

import app.modules.board.Board;
import app.utils.helper.Point;

import java.util.ArrayList;

public class GameNode
{
    private Board board;
    private GameNode parent;
    private ArrayList<GameNode> children;
    private Point[] movement;

    public GameNode(Board board)
    {
        this.board = board;
        children = new ArrayList<>();
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
    }

    public GameNode[] getChildren() { return children.toArray(new GameNode[0]); }
    public Board getBoard() { return board; }
    public Point[] getMovement() { return movement; }

    private void setParent(GameNode parent) { this.parent = parent; }

    private void movePiece()
    {
        Point origPoint = movement[0];
        for (int i = 1; i < movement.length; i++) { // Start from the second point.
            if (Math.abs(origPoint.x - movement[i].x) == 2) {
                // No need to check for the y-axis because we can already check if we did a jump
                // by just using one coordinate. Remember straight lines with slopes of either
                // 1 or -1. Their points would have the same value for the x and y coordinates.
                board.removePieceAt(getMidpoint(origPoint, movement[i]));
            }

            if (i == movement.length - 1) {
                // Oh look, we're at the last point. Time to move the piece to the last point.
                board.movePieceTo(movement[0], movement[movement.length - 1]);
            } else {
                origPoint = movement[i];
            }
        }
    }

    private Point getMidpoint(Point source, Point target)
    {
        return (source.x > target.x && source.y > target.y) ? new Point(source.x - 1, source.y - 1) :
               (source.x < target.x && source.y > target.y) ? new Point(source.x + 1, source.y - 1) :
               (source.x > target.x && source.y < target.y) ? new Point(source.x - 1, source.y + 1) :
             /*(source.x < target.x && source.y < target.y)*/ new Point(source.x + 1, source.y + 1);
    }
}
