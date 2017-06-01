package app.modules.ai;

import app.modules.board.Board;

import java.util.ArrayList;
import java.util.Arrays;

public class TreeNode
{
    private Board board;
    private ArrayList<TreeNode> children;

    public TreeNode(Board board)
    {
        this.board = board;
        children = new ArrayList<>();
    }

    public void addChild(TreeNode child)
    {
        children.add(child);
    }

    public TreeNode[] getChildren()
    {
        return children.stream().toArray(TreeNode[]::new);
    }
}
