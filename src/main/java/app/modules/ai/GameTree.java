package app.modules.ai;

import app.modules.board.Board;
import app.modules.board.Piece;
import app.utils.enums.PieceColor;
import app.utils.helper.Direction;
import app.utils.helper.Point;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class GameTree
{
    public Board getMove(Board board, PieceColor maxColor, int depth)
    {
        GameNode root = generateTree(board, maxColor, depth);
        GameNode chosenBoard = alphaBeta(
            root, new GameNode(Integer.MIN_VALUE), new GameNode(Integer.MAX_VALUE), maxColor, true
        );

        return chosenBoard.getBoard();
    }

    private GameNode generateTree(Board board, PieceColor maxColor, int depth)
    {
        GameNode root = new GameNode(board, 0);
        Queue<GameNode> gameNodes = new LinkedList<>();
        gameNodes.add(root);

        while (!gameNodes.isEmpty()) {
            GameNode currNode = gameNodes.remove();
            // Create children and add them to queue depending on a condition
        }
    }

    private ArrayList<GameNode> generateChildNodes(Board board, PieceColor currcolor, Queue<GameNode> nodeQueue)
    {
        Piece[] pieces = (currcolor == PieceColor.WHITE) ? board.getWhitePieces() : board.getBlackPieces();
        for (Piece piece : pieces) {
            Board duplicateBoard = board.clone();

        }
    }
}
