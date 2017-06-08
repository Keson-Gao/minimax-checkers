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
    private GameNode root;

    public Board getMove(Board board, PieceColor maxColor, int depth)
    {
        root = new GameNode(board);
        root = generateTree(board, root, maxColor, depth);
        return alphaBeta(
                root,
                2,
                new Pair(null, -32000),
                new Pair(null, 32000),
                true,
                maxColor
        ).board;
    }

    private GameNode generateTree(Board board, GameNode node, PieceColor currColor, int depth)
    {
        if (depth < 0) {
            return node;
        }

        Piece[] pieces = (currColor == PieceColor.BLACK) ? board.getBlackPieces() : board.getWhitePieces();
        for (Piece piece : pieces) {
            MoveNode pieceNode = generatePieceMoveTree(board, piece, 0, piece.getPoint());
            ArrayList<ArrayList<Point>> movements = generateMovePoints(
                pieceNode, new ArrayList<>(), new ArrayList<>()
            );

            for (ArrayList<Point> movement : movements) {
                Board newBoard = board.clone();
                GameNode newNode = new GameNode(newBoard);
                newNode.setMovement(movement.toArray(new Point[0]));
                node.addChild(newNode);
            }
        }

        for (GameNode node : node.getChildren()) {
            PieceColor nextColor = (currColor == PieceColor.BLACK) ? PieceColor.WHITE : PieceColor.BLACK;
            generateTree(node.getBoard(), node, nextColor, depth - 1);
        }

        return node;
    }

    private ArrayList<ArrayList<Point>> generateMovePoints(MoveNode node, ArrayList<ArrayList<Point>> points, ArrayList<Point> path)
    {
        path.add(node.getPosition());

        if (node.getTopLeftChild() != null && node.getParent() != node.getTopLeftChild()) {
            generateMovePoints(node.getTopLeftChild(), points, deepCopyArrayList(path));
        }

        if (node.getTopRightChild() != null && node.getParent() != node.getTopRightChild()) {
            generateMovePoints(node.getTopRightChild(), points, deepCopyArrayList(path));
        }

        if (node.getBottomLeftChild() != null && node.getParent() != node.getBottomLeftChild()) {
            generateMovePoints(node.getBottomLeftChild(), points, deepCopyArrayList(path));
        }

        if (node.getBottomRightChild() != null && node.getParent() != node.getBottomRightChild()) {
            generateMovePoints(node.getBottomRightChild(), points, deepCopyArrayList(path));
        }

        if (!node.hasChildren()) points.add(path);

        return points;
    }

    private MoveNode generatePieceMoveTree(Board board, Piece piece, int depth, Point nodePos)
    {
        if (!canStillMoveAt(board, piece, depth)) {
            return new MoveNode(nodePos);
        }

        MoveNode currentNode = new MoveNode(nodePos);
        Point topLeft = new Point(piece.getPoint().x - 1, piece.getPoint().y - 1);
        Point topRight = new Point(piece.getPoint().x + 1, piece.getPoint().y - 1);
        Point bottomLeft = new Point(piece.getPoint().x - 1, piece.getPoint().y + 1);
        Point bottomRight = new Point(piece.getPoint().x + 1, piece.getPoint().y + 1);

        if (piece.getColor() == PieceColor.WHITE || piece.isKing()) {
            if (canMove(board, piece, depth, new Point[] { topLeft }) && !piece.isKing()) {
                Point newPoint = (board.hasPieceAt(topLeft)) ? new Point(topLeft.x - 1, topLeft.y - 1)
                                                             : topLeft;
                currentNode.setTopLeftChild(generatePieceMoveTree(board, piece, depth, newPoint));
            }

            if (canMove(board, piece, depth, new Point[] { topRight }) && !piece.isKing()) {
                Point newPoint = (board.hasPieceAt(topRight)) ? new Point(topRight.x + 1, topRight.y - 1)
                                                              : topRight;
                currentNode.setTopRightChild(generatePieceMoveTree(board, piece, depth, newPoint));
            }
        }

        if (piece.getColor() == PieceColor.BLACK || piece.isKing()) {
            if (canMove(board, piece, depth, new Point[] { bottomLeft })) {
                Point newPoint = (board.hasPieceAt(bottomLeft)) ? new Point(bottomLeft.x - 1, bottomRight.y + 1)
                                                                : bottomLeft;
                currentNode.setBottomLeftChild(generatePieceMoveTree(board, piece, depth, newPoint));
            }

            if (canMove(board, piece, depth, new Point[] { bottomRight })) {
                Point newPoint = (board.hasPieceAt(bottomRight)) ? new Point(bottomRight.x + 1, bottomRight.y + 1)
                                                                 : bottomRight;
                currentNode.setBottomRightChild(generatePieceMoveTree(board, piece, depth, newPoint));
            }
        }

        return currentNode;
    }

    private boolean canStillMoveAt(Board board, Piece piece, int depth)
    {
        return (piece.getColor() == PieceColor.WHITE && !piece.isKing()) ? canMoveTop(board, piece, depth) :
               (piece.getColor() == PieceColor.BLACK && !piece.isKing()) ? canMoveBottom(board, piece, depth) :
               canMoveTop(board, piece, depth) && canMoveBottom(board, piece, depth);
    }

    private boolean canMoveTop(Board board, Piece piece, int depth)
    {
        Point topLeft = new Point(piece.getPoint().x - 1, piece.getPoint().y - 1);
        Point topRight = new Point(piece.getPoint().x + 1, piece.getPoint().y - 1);
        return canMove(board, piece, depth, new Point[] { topLeft, topRight });
    }

    private boolean canMoveBottom(Board board, Piece piece, int depth)
    {
        Point bottomLeft = new Point(piece.getPoint().x - 1, piece.getPoint().y + 1);
        Point bottomRight = new Point(piece.getPoint().x + 1, piece.getPoint().y + 1);
        return canMove(board, piece, depth, new Point[] { bottomLeft, bottomRight });
    }

    private boolean canMove(Board board, Piece piece, int depth, Point[] points)
    {
        for (Point point : points) {
            if (isPointInBoard(point)) {
                if (!board.hasPieceAt(point) && depth == 0) {
                    return true;
                } else if (board.getPieceAt(point).getColor() != piece.getColor()) {
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

    private ArrayList<Point> deepCopyArrayList(ArrayList<Point> source)
    {
        ArrayList<Point> target = new ArrayList<>();
        for (Point point : source) {
            target.add(point.clone());
        }

        return target;
    }

    private Pair alphaBeta(GameNode node, int depth, Pair alpha, Pair beta, boolean isMaxPlayer, PieceColor maxColor)
    {
        if (depth == 0) {
            return new Pair(node.getBoard(), node.getBoard().getBoardValue(maxColor));
        }

        if (isMaxPlayer) {
            Pair v = new Pair(null, -32000);
            for (GameNode child : node.getChildren()) {
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
            for (GameNode child : node.getChildren()) {
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
