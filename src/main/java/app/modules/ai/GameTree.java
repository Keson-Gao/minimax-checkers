package app.modules.ai;

import app.modules.board.Board;
import app.modules.board.Piece;
import app.utils.enums.PieceColor;
import app.utils.helper.Point;

import java.util.ArrayList;

public class GameTree
{
    private TreeNode root;

    public void generateTree(Board board, int depth, PieceColor startingColor)
    {
        root = new TreeNode(board);

        while (depth >= 0) {
            Piece[] pieces;
            pieces = (startingColor == PieceColor.BLACK) ? board.getBlackPieces() : board.getWhitePieces();
        }
    }

    private Board[] makeMoveForEating(Piece[] pieces, Board currentBoard)
    {
        ArrayList<Board> boards = new ArrayList<>();
        for (Piece piece : pieces) {
            Point piecePoint = piece.getPoint();
            if (piece.getColor() == PieceColor.BLACK) {
                if (currentBoard.hasPieceAt(new Point(piecePoint.x + 1, piecePoint.y + 1)) &&
                    currentBoard.hasPieceAt(new Point(piecePoint.x + 2, piecePoint.y + 2))) {
                    
                }
            }
        }
    }
}
