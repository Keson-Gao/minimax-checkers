package app.modules.ai;

import app.modules.board.Board;
import app.modules.board.Piece;
import app.utils.enums.PieceColor;
import app.utils.helper.Pair;
import app.utils.helper.Point;

import java.util.ArrayList;

public class GameTree
{
    private TreeNode root;

    public Board getMove(Board board, PieceColor maxColor)
    {
        generateTree(board, maxColor);
        return alphaBeta(
                root,
                2,
                new Pair(null, -32000),
                new Pair(null, 32000),
                true,
                maxColor
        ).board;
    }

    private void generateTree(Board board, PieceColor startingColor)
    {
        root = new TreeNode(board);

        Piece[] pieces = (startingColor == PieceColor.BLACK) ? board.getBlackPieces() : board.getWhitePieces();
        Board[] boards = makeMoveForEating(pieces, board);
        if (boards == null) {
            boards = makeMove(pieces, board);
        }

        if (boards != null) {
            for (Board board3 : boards) {
                assignKings(board3);
                root.addChild(new TreeNode(board3));
            }
        } else {
            return;
        }

        for (TreeNode node : root.getChildren()) {
            Board board2 = node.getBoard();
            Piece[] pieces1 = (startingColor == PieceColor.BLACK) ? board2.getWhitePieces() : board2.getBlackPieces();
            Board[] boards1 = makeMoveForEating(pieces1, board2);
            if (boards1 == null) {
                boards1 = makeMove(pieces1, board2);
            }

            if (boards1 != null) {
                for (Board board1 : boards1) {
                    assignKings(board1);
                    node.addChild(new TreeNode(board1));
                }
            }
        }
    }

    private Pair alphaBeta(TreeNode node, int depth, Pair alpha, Pair beta, boolean isMaxPlayer, PieceColor maxColor)
    {
        if (depth == 0) {
            return new Pair(node.getBoard(), node.getBoard().getBoardValue(maxColor));
        }

        if (isMaxPlayer) {
            Pair v = new Pair(null, -32000);
            for (TreeNode child : node.getChildren()) {
                Pair ab = alphaBeta(child, depth - 1, alpha, beta, false, maxColor);
                if (ab.score > v.score) {
                    v = ab;
                }

                if (ab.score > alpha.score) {
                    alpha = ab;
                }

                if (beta.score <= alpha.score) {
                    break;
                }
            }

            return v;
        } else {
            Pair v = new Pair(null, 32000);
            for (TreeNode child : node.getChildren()) {
                Pair ab = alphaBeta(child, depth - 1, alpha, beta, true, maxColor);
                if (ab.score < v.score) {
                    v = ab;
                }

                if (ab.score > v.score) {
                    beta = ab;
                }

                if (beta.score < alpha.score) {
                    break;
                }
            }

            return v;
        }
    }

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
                if (!currentBoard.hasPieceAt(adjacentPoint1)) {
                    Board newBoard = movePiece(piecePoint, adjacentPoint1, currentBoard);
                    boards.add(newBoard);
                }

                Point adjacentPoint2 = new Point(piecePoint.x - 1, piecePoint.y + 1);
                if (!currentBoard.hasPieceAt(adjacentPoint2)) {
                    Board newBoard = movePiece(piecePoint, adjacentPoint2, currentBoard);
                    boards.add(newBoard);
                }

                if (piece.isKing()) {
                    Point adjacentPoint3 = new Point(piecePoint.x + 1, piecePoint.y - 1);
                    if (!currentBoard.hasPieceAt(adjacentPoint3)) {
                        Board newBoard = movePiece(piecePoint, adjacentPoint3, currentBoard);
                        boards.add(newBoard);
                    }

                    Point adjacentPoint4 = new Point(piecePoint.x - 1, piecePoint.y - 1);
                    if (!currentBoard.hasPieceAt(adjacentPoint4)) {
                        Board newBoard = movePiece(piecePoint, adjacentPoint4, currentBoard);
                        boards.add(newBoard);
                    }
                }
            } else if (piece.getColor() == PieceColor.WHITE) {
                Point adjacentPoint1 = new Point(piecePoint.x + 1, piecePoint.y - 1);
                if (!currentBoard.hasPieceAt(adjacentPoint1)) {
                    Board newBoard = movePiece(piecePoint, adjacentPoint1, currentBoard);
                    boards.add(newBoard);
                }

                Point adjacentPoint2 = new Point(piecePoint.x - 1, piecePoint.y - 1);
                if (!currentBoard.hasPieceAt(adjacentPoint2)) {
                    Board newBoard = movePiece(piecePoint, adjacentPoint2, currentBoard);
                    boards.add(newBoard);
                }

                if (piece.isKing()) {
                    Point adjacentPoint3 = new Point(piecePoint.x + 1, piecePoint.y + 1);
                    if (!currentBoard.hasPieceAt(adjacentPoint3)) {
                        Board newBoard = movePiece(piecePoint, adjacentPoint3, currentBoard);
                        boards.add(newBoard);
                    }

                    Point adjacentPoint4 = new Point(piecePoint.x - 1, piecePoint.y + 1);
                    if (!currentBoard.hasPieceAt(adjacentPoint4)) {
                        Board newBoard = movePiece(piecePoint, adjacentPoint4, currentBoard);
                        boards.add(newBoard);
                    }
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

                if (piece.isKing()) {
                    Point adjacentPoint3 = new Point(piecePoint.x + 1, piecePoint.y - 1);
                    Point newPoint3 = new Point(piecePoint.x + 2, piecePoint.y - 2);
                    if (currentBoard.hasPieceAt(adjacentPoint3) && !currentBoard.hasPieceAt(newPoint3)) {
                        Board newBoard = movePieceToEat(piecePoint, adjacentPoint3, newPoint3, currentBoard);
                        boards.add(newBoard);
                    }

                    Point adjacentPoint4 = new Point(piecePoint.x - 1, piecePoint.y - 1);
                    Point newPoint4 = new Point(piecePoint.x - 2, piecePoint.y - 2);
                    if (currentBoard.hasPieceAt(adjacentPoint4) && !currentBoard.hasPieceAt(newPoint4)) {
                        Board newBoard = movePieceToEat(piecePoint, adjacentPoint4, newPoint4, currentBoard);
                        boards.add(newBoard);
                    }
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

                if (piece.isKing()) {
                    Point adjacentPoint3 = new Point(piecePoint.x + 1, piecePoint.y + 1);
                    Point newPoint3 = new Point(piecePoint.x + 2, piecePoint.y + 2);
                    if (currentBoard.hasPieceAt(adjacentPoint3) && !currentBoard.hasPieceAt(newPoint3)) {
                        Board newBoard = movePieceToEat(piecePoint, adjacentPoint3, newPoint3, currentBoard);
                        boards.add(newBoard);
                    }

                    Point adjacentPoint4 = new Point(piecePoint.x - 1, piecePoint.y - 1);
                    Point newPoint4 = new Point(piecePoint.x - 2, piecePoint.y + 2);
                    if (currentBoard.hasPieceAt(adjacentPoint4) && !currentBoard.hasPieceAt(newPoint4)) {
                        Board newBoard = movePieceToEat(piecePoint, adjacentPoint4, newPoint4, currentBoard);
                        boards.add(newBoard);
                    }
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
