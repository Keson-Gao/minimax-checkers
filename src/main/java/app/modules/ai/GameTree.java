package app.modules.ai;

import app.modules.board.Board;
import app.utils.enums.PieceColor;

public class GameTree
{
    private TreeNode root;

    public void generateTree(Board board, int depth, PieceColor startingColor)
    {
        root = new TreeNode(board);

        while (depth >= 0) {

        }
    }
}
