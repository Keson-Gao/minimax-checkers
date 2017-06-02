package app.modules.ai;

import app.modules.board.Board;

import java.util.ArrayList;

public class TreeNode
{
    private Board board;
    private TreeNode parent;
    private ArrayList<TreeNode> children;

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
    public TreeNode getParent() { return parent; }

    private void setParent(TreeNode parent) { this.parent = parent; }
}
