package app.modules.ai;

import app.modules.board.Board;
import app.modules.board.Piece;
import app.utils.enums.PieceColor;
import app.utils.helper.Direction;
import app.utils.helper.Point;

import java.util.ArrayList;

public class GameTree
{
    public Board getMove(Board board, PieceColor maxColor, int depth)
    {
        GameNode root = new GameNode(board);
        root = generateTree(board, root, maxColor, depth);
        GameNode chosenBoard = alphaBeta(
            root, new GameNode(Integer.MIN_VALUE), new GameNode(Integer.MAX_VALUE), maxColor, true
        );

        return chosenBoard.getBoard();
    }
}
