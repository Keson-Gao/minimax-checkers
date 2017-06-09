package app.modules.ai;

import app.modules.board.Board;
import app.utils.enums.PieceColor;

public class AI
{
    public Board getMove(Board board, PieceColor maxColor, int depth)
    {
        return new GameTree().getMove(board, maxColor, depth);
    }
}