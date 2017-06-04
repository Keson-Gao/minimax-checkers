package app.modules.ai;

import app.modules.board.Board;
import app.utils.enums.PieceColor;
import app.utils.helper.Point;

import java.util.ArrayList;

public class TreeNode
{
    private Board board;
    private TreeNode parent;
    private ArrayList<TreeNode> children;
    private Point[] movement;

    public TreeNode(Board board)
    {
        this.board = board;
        children = new ArrayList<>();
    }

    public void addChild(TreeNode child)
    {
        child.setParent(this);
        children.add(child);
    }

    public TreeNode[] getChildren()
    {
        return children.stream().toArray(TreeNode[]::new);
    }
    public Board getBoard() { return board; }
    public Point[] getMovement() { return movement; }

    private void setParent(TreeNode parent) { this.parent = parent; }
    private void setMovement(Point[] movement) { this.movement = movement; }
}
