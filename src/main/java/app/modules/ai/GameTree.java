package app.modules.ai;

import app.modules.board.Board;
import app.modules.board.Piece;
import app.utils.enums.PieceColor;
import app.utils.helper.Direction;
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

    private void generatePieceMoveTree(Piece piece, int depth, PieceColor color, Board board)
    {
        if (depth == 1 || board.hasPieceAt(new Point(piece.getPoint().x, piece.getPoint().y))) {

        }
    }

    private boolean canStillMoveAt(Piece piece, int depth, Board board)
    {
        if (depth == 0) {
            if (piece.getColor() == PieceColor.WHITE) { // Moves up first.

            }
        }
    }

    private boolean canMove(Piece piece, int depth, Board board, Point[] points)
    {
        for (Point point : points) {
            if (isPointInBoard(point)) {
                if (!board.hasPieceAt(point) && depth == 0) {
                    return true;
                } else if (board.getPieceAt(point).getColor() != piece.getColor()) {
                    Direction direction = getNextPosDirection(piece.getPoint(), point);
                    return canJump(board, piece.getPoint(), point);
                }
            }
        }

        return false;
    }

    private boolean canJump(Board board, Point source, Point target)
    {
        Direction direction = getNextPosDirection(source, target);
        return (direction == Direction.TOP_LEFT && isCellOccupiable(board, new Point(target.x - 1, target.y - 1))) ||
               (direction == Direction.TOP_RIGHT && isCellOccupiable(board, new Point(target.x + 1, target.y - 1))) ||
               (direction == Direction.BOTTOM_LEFT && isCellOccupiable(board, new Point(target.x - 1, target.y + 1))) ||
               (direction == Direction.BOTTOM_RIGHT && isCellOccupiable(board, new Point(target.x + 1, target.y + 1)));
    }

    private boolean isCellOccupiable(Board board, Point cellPoint)
    {
        return !board.hasPieceAt(cellPoint) && isPointInBoard(cellPoint);
    }

    private boolean canMoveTop(Piece piece, Board board)
    {
        Point piecePos = piece.getPoint();
        Point topLeftPoint = new Point(piecePos.x - 1, piecePos.y - 1);
        Point topRightPoint = new Point(piecePos.x + 1, piecePos.y - 1);

        if (isPointInBoard(topLeftPoint)) {

        }
    }

    private Direction getNextPosDirection(Point source, Point target)
    {
        if (source.x > target.x && source.y > target.y) {
            return Direction.TOP_LEFT;
        } else if (source.x < target.x && source.y > target.y) {
            return Direction.TOP_RIGHT;
        } else if (source.x > target.x && source.y < target.y) {
            return Direction.BOTTOM_LEFT;
        } else if (source.x < target.x && source.y < target.y) {
            return Direction.BOTTOM_RIGHT;
        }

        return Direction.NONE;
    }

    private boolean isPointInBoard(Point pos)
    {
        return (pos.x > 0 && pos.x < 8) && (pos.y > 0 && pos.y < 8);
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
}
