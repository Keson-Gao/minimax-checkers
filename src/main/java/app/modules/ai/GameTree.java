package app.modules.ai;

import app.modules.board.Board;
import app.modules.board.Piece;
import app.utils.enums.PieceColor;
import app.utils.helper.Direction;
import app.utils.helper.Point;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class GameTree
{
    public Board getMove(Board board, PieceColor maxColor, int maxDepth)
    {
        GameNode root = generateTree(board, maxColor, maxDepth);
        GameNode chosenBoard = alphaBeta(
            root, 0, new GameNode(Integer.MIN_VALUE, -1), new GameNode(Integer.MAX_VALUE, -1), maxColor, true
        );

        return chosenBoard.getBoard();
    }

    private GameNode alphaBeta(GameNode node, int depth, GameNode alpha, GameNode beta, PieceColor currColor, boolean isMax)
    {
        if (!node.hasChildren()) {
            return node;
        }

        if (isMax) {
            GameNode bestNode = new GameNode(Integer.MIN_VALUE, -1);
            for (GameNode child : node.getChildren()) {
                GameNode newNode = alphaBeta(node, depth + 1, alpha, beta, getOppositeColor(currColor), false);
                bestNode = (bestNode.getScore(currColor) > newNode.getScore(currColor)) ? bestNode : newNode;
                alpha = (alpha.getScore(currColor) > bestNode.getScore(currColor)) ? alpha : bestNode;
                if (beta.getScore(currColor) <= alpha.getScore(currColor)) {
                    break;
                }
            }

            return bestNode;
        } else {
            GameNode bestNode = new GameNode(Integer.MAX_VALUE, -1);
            for (GameNode child : node.getChildren()) {
                GameNode newNode = alphaBeta(node, depth + 1, alpha, beta, getOppositeColor(currColor), true);
                bestNode = (bestNode.getScore(currColor) < newNode.getScore(currColor)) ? bestNode : newNode;
                beta = (beta.getScore(currColor) < bestNode.getScore(currColor)) ? beta : bestNode;
                if (beta.getScore(currColor) <= alpha.getScore(currColor)) {
                    break;
                }
            }

            return bestNode;
        }
    }

    private GameNode generateTree(Board board, PieceColor maxColor, int maxDepth)
    {
        GameNode root = new GameNode(board, 0);
        Queue<GameNode> gameNodes = new LinkedList<>();
        gameNodes.add(root);
        PieceColor currColor = maxColor;

        while (!gameNodes.isEmpty()) {
            GameNode currNode = gameNodes.remove();
            ArrayList<GameNode> childNodes = generateChildNodes(currNode.getBoard(), currColor, currNode.getDepth() + 1);
            for (GameNode child : childNodes) {
                currNode.addChild(child);

                if (currNode.getDepth() + 1 < maxDepth) { // Add check for winning conditions soon.
                    gameNodes.add(child);
                }
            }

            currColor = getOppositeColor(currColor);
        }

        return root;
    }

    private ArrayList<GameNode> generateChildNodes(Board board, PieceColor currColor, int childDepth)
    {
        ArrayList<GameNode> childNodes = new ArrayList<>();
        Piece[] pieces = (currColor == PieceColor.WHITE) ? board.getWhitePieces() : board.getBlackPieces();
        for (Piece piece : pieces) {
            Stack<Point> piecePoints = new Stack<>();
            piecePoints.add(piece.getPoint());
            while (!piecePoints.empty()) {
                Point currPoint = piecePoints.pop();
                if (currColor == PieceColor.WHITE) {
                    movePieceUp(board, currPoint, childNodes, childDepth);

                    if (board.getPieceAt(currPoint).isKing()) {
                        movePieceDown(board, currPoint, childNodes, childDepth);
                    }
                }

                if (currColor == PieceColor.BLACK) {
                    movePieceDown(board, currPoint, childNodes, childDepth);

                    if (board.getPieceAt(currPoint).isKing()) {
                        movePieceUp(board, currPoint, childNodes, childDepth);
                    }
                }
            }
        }

        return childNodes;
    }

    private PieceColor getOppositeColor(PieceColor color)
    {
        return (color == PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;
    }

    private void movePieceUp(Board board, Point currPoint, ArrayList<GameNode> childNodes, int childDepth)
    {
        if (pointIsTraversable(board, getTopLeftPoint(currPoint))) {
            childNodes.add(getMoveNode(board, currPoint, getTopLeftPoint(currPoint), childDepth));
        }

        if (pointIsTraversable(board, getTopRightPoint(currPoint))) {
            childNodes.add(getMoveNode(board, currPoint, getTopRightPoint(currPoint), childDepth));
        }
    }

    private void movePieceDown(Board board, Point currPoint, ArrayList<GameNode> childNodes, int childDepth)
    {
        if (pointIsTraversable(board, getBottomLeftPoint(currPoint))) {
            childNodes.add(getMoveNode(board, currPoint, getBottomLeftPoint(currPoint), childDepth));
        }

        if (pointIsTraversable(board, getBottomRightPoint(currPoint))) {
            childNodes.add(getMoveNode(board, currPoint, getBottomRightPoint(currPoint), childDepth));
        }
    }

    private GameNode getMoveNode(Board board, Piece currentPiece, Point newPoint, int depth)
    {
        Board duplicateBoard = board.clone();
        duplicateBoard.movePieceTo(currentPiece.getPoint(), newPoint);

        return new GameNode(duplicateBoard, depth);
    }

    private boolean pointIsTraversable(Board board, Point target)
    {
        return !board.hasPieceAt(target) && isPointInBoard(target);
    }

    private boolean isPointInBoard(Point target)
    {
        return (target.x >= 0 && target.x < 8) && (target.y >= 0 && target.y < 8);
    }

    private Point getTopLeftPoint(Point source)
    {
        return new Point(source.x - 1, source.y - 1);
    }

    private Point getTopRightPoint(Point source)
    {
        return new Point(source.x + 1, source.y - 1);
    }

    private Point getBottomLeftPoint(Point source)
    {
        return new Point(source.x - 1, source.y + 1);
    }

    private Point getBottomRightPoint(Point source)
    {
        return new Point(source.x + 1, source.y + 1);
    }
}
