package app.modules.ai;

import app.modules.board.Board;
import app.modules.board.Piece;
import app.utils.enums.PieceColor;
import app.utils.helper.Point;

import java.util.ArrayList;

public class GameTree
{
    private TreeNode root;

    public void generateTree(Board board, PieceColor startingColor)
    {
        root = new TreeNode(board);

        Piece[] pieces = (startingColor == PieceColor.BLACK) ? board.getBlackPieces() : board.getWhitePieces();
        Board[] boards = makeMoveForEating(pieces, board);
        if (boards == null) {
            boards = makeMove(pieces, board);
        }

        if (boards != null) {
            for (Board board : boards) {
                assignKings(board);
                root.addChild(new TreeNode(board));
            }
        } else {
            return;
        }

        for (TreeNode node : root.getChildren()) {
            Board board = node.getBoard();
            Piece[] pieces = (startingColor == PieceColor.BLACK) ? board.getWhitePieces() : board.getBlackPieces();
            Board[] boards = makeMoveForEating(pieces, board);
            if (boards == null) {
                boards = makeMove(pieces, board);
            }

            if (boards != null) {
                for (Board board : boards) {
                    assignKings(board);
                    node.addChild(new TreeNode(board));
                }
            }
        }
    }

    public Board alphaBeta(); // Implement alpha-beta pruning.

    private void assignKings(Board board)
    {
        for (int i = 0; i < 8; i++) {
            Point piecePoint = new Point(i, 0);
            if (board.hasPieceAt(piecePoint)) {
                board.setKingPieceAt(piecePoint);
            }

            piecePoint.x = 7 - piecePoint.x;
            if (board.hasPieceAt(piecePoint)) {
                board.setKingPieceAt(piecePoint);
            }
        }
    }

    // TODO: Modify makeMove() and makeMoveForEating() to move kings.

    private Board[] makeMove(Piece[] pieces, Board currentBoard)
    {
        ArrayList<Board> boards = new ArrayList<>();
        for (Piece piece : pieces) {
            Point piecePoint = piece.getPoint();
            if (piece.getColor() == PieceColor.BLACK) {
                Point adjacentPoint1 = new Point(piecePoint.x + 1, piecePoint.y + 1);
                if (!currentBoard.hasPieceAt(adjacentPoint1))) {
                    Board newBoard = movePiece(piecePoint, adjacentPoint1, currentBoard);
                    boards.add(newBoard);
                }

                Point adjacentPoint2 = new Point(piecePoint.x - 1, piecePoint.y + 1);
                if (!currentBoard.hasPieceAt(adjacentPoint2))) {
                    Board newBoard = movePiece(piecePoint, adjacentPoint2, currentBoard);
                    boards.add(newBoard);
                }
            } else if (piece.getColor() == PieceColor.WHITE) {
                Point adjacentPoint1 = new Point(piecePoint.x + 1, piecePoint.y - 1);
                if (!currentBoard.hasPieceAt(adjacentPoint1))) {
                    Board newBoard = movePiece(piecePoint, adjacentPoint1, currentBoard);
                    boards.add(newBoard);
                }

                Point adjacentPoint2 = new Point(piecePoint.x - 1, piecePoint.y - 1);
                if (!currentBoard.hasPieceAt(adjacentPoint2))) {
                    Board newBoard = movePiece(piecePoint, adjacentPoint2, currentBoard);
                    boards.add(newBoard);
                }
            }
        }

        if (boards.isEmpty()) {
            return null;
        }

        return boards.toArray(new Board[0]);
    }

    private Board movePiece(Point currentPiecePoint, Point adjacentPoint, Board currentBoard)
    {
        Board board = currentBoard.copy();
        board.movePieceTo(currentPiecePoint, adjacentPoint);

        return board;
    }

    private Board[] makeMoveForEating(Piece[] pieces, Board currentBoard)
    {
        ArrayList<Board> boards = new ArrayList<>();
        for (Piece piece : pieces) {
            Point piecePoint = piece.getPoint();
            if (piece.getColor() == PieceColor.BLACK) {
                Point adjacentPoint1 = new Point(piecePoint.x + 1, piecePoint.y + 1);
                Point newPoint1 = new Point(piecePoint.x + 2, piecePoint.y + 2);
                if (currentBoard.hasPieceAt(adjacentPoint1) && !currentBoard.hasPieceAt(newPoint1)) {
                    Board newBoard = movePieceToEat(piecePoint, adjacentPoint1, newPoint1, currentBoard);
                    boards.add(newBoard);
                }

                Point adjacentPoint2 = new Point(piecePoint.x - 1, piecePoint.y + 1);
                Point newPoint2 = new Point(piecePoint.x - 2, piecePoint.y + 2);
                if (currentBoard.hasPieceAt(adjacentPoint2) && !currentBoard.hasPieceAt(newPoint2)) {
                    Board newBoard = movePieceToEat(piecePoint, adjacentPoint2, newPoint2, currentBoard);
                    boards.add(newBoard);
                }
            } else if (piece.getColor() == PieceColor.WHITE) {
                Point adjacentPoint1 = new Point(piecePoint.x + 1, piecePoint.y - 1);
                Point newPoint1 = new Point(piecePoint.x + 2, piecePoint.y - 2);
                if (currentBoard.hasPieceAt(adjacentPoint1) && !currentBoard.hasPieceAt(newPoint1)) {
                    Board newBoard = movePieceToEat(piecePoint, adjacentPoint1, newPoint1, currentBoard);
                    boards.add(newBoard);
                }

                Point adjacentPoint2 = new Point(piecePoint.x - 1, piecePoint.y - 1);
                Point newPoint2 = new Point(piecePoint.x - 2, piecePoint.y - 2);
                if (currentBoard.hasPieceAt(adjacentPoint2) && !currentBoard.hasPieceAt(newPoint2)) {
                    Board newBoard = movePieceToEat(piecePoint, adjacentPoint2, newPoint2, currentBoard);
                    boards.add(newBoard);
                }
            }
        }

        if (boards.isEmpty()) {
            return null;
        }

        return boards.toArray(new Board[0]);
    }

    private Board movePieceToEat(Point currentPiecePoint, Point adjacentPoint, Point newPoint, Board currentBoard)
    {
        Board board = currentBoard.copy();
        board.movePieceTo(currentPiecePoint, newPoint);
        board.removePieceAt(adjacentPoint);

        return board;
    }
}
