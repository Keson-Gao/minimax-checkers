package app.modules.ai;

import app.modules.board.Board;
import app.utils.enums.PieceColor;

public class AI
{
    private GameTree gameTree;

    public Board makeMove(Board board, PieceColor aiColor)
    {
        return gameTree.getMove(board, aiColor);
    }
}