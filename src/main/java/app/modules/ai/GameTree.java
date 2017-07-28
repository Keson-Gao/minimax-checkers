package app.modules.ai;

import app.modules.board.Board;
import app.modules.board.Piece;
import app.utils.enums.PieceColor;
import app.utils.helper.Point;

import java.util.*;

public class GameTree
{
    // Debugging purposes
    private int nodeCount = 0;
    private int currParentID = 0;

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
                GameNode newNode = alphaBeta(child, depth + 1, alpha, beta, getOppositeColor(currColor), false);
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
                GameNode newNode = alphaBeta(child, depth + 1, alpha, beta, getOppositeColor(currColor), true);
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
        int currentDepth = 0;

        GameNode root = new GameNode(board, currentDepth);
        Queue<GameNode> gameNodes = new LinkedList<>();
        gameNodes.add(root);
        PieceColor currColor = maxColor;

        while (!gameNodes.isEmpty()) {
            GameNode currNode = gameNodes.remove();

            if (currNode.getDepth() != currentDepth) {
                currentDepth = currNode.getDepth();
                currColor = getOppositeColor(currColor);
            }

            currParentID = currNode.getID();
            System.out.println("Parent board | Node #" + currParentID + " | ID: " + currNode);
            drawBoard(currNode.getBoard());
            ArrayList<GameNode> childNodes = generateChildNodes(currNode.getBoard(), currColor, currNode.getDepth() + 1);
            for (GameNode child : childNodes) {
                System.out.println("Child ID: " + child);
                currNode.addChild(child);

                if (currNode.getDepth() + 1 < maxDepth) { // Add check for winning conditions soon.
                    gameNodes.add(child);
                }
            }
        }

        return root;
    }

    private ArrayList<GameNode> generateChildNodes(Board board, PieceColor currColor, int childDepth)
    {
        ArrayList<GameNode> childNodes = new ArrayList<>();
        Piece[] pieces = (currColor == PieceColor.WHITE) ? board.getWhitePieces() : board.getBlackPieces();
        for (Piece piece : pieces) {
            Stack<Point> path = new Stack<>();
            path.push(piece.getPoint());
            ArrayList<Point[]> generatedPiecePaths = new ArrayList<>();

            generatePossiblePiecePaths(board, path, generatedPiecePaths, new HashSet<>());

            for (Point[] pointPath : generatedPiecePaths) {
                if (pointPath.length > 1) {
                    GameNode node = new GameNode(board.clone(), childDepth);
                    node.setMovement(pointPath);

                    childNodes.add(node);
                }
            }
        }

        return childNodes;
    }

    private void generatePossiblePiecePaths(Board board, Stack<Point> path, ArrayList<Point[]> paths, HashSet<Point> removedPieces)
    {
        boolean isLeaf = true;
        Point currPoint = path.peek();
        Piece piece = board.getPieceAt(currPoint);
        if (path.size() == 1) { // Haven't moved yet
            if (piece.getColor() == PieceColor.WHITE || piece.isKing()) {
                Point topLeftPoint = getTopLeftPoint(currPoint, 1);
                if (isPointTraversable(board, topLeftPoint, removedPieces) && !isPointNearStackTop(path, currPoint)) {
                    isLeaf = false;
                    path.push(topLeftPoint);
                    generatePossiblePiecePaths(board, path, paths, removedPieces);
                    path.pop();
                }

                Point topRightPoint = getTopRightPoint(currPoint, 1);
                if (isPointTraversable(board, topRightPoint, removedPieces) && !isPointNearStackTop(path, currPoint)) {
                    isLeaf = false;
                    path.push(topRightPoint);
                    generatePossiblePiecePaths(board, path, paths, removedPieces);
                    path.pop();
                }
            }

            if (piece.getColor() == PieceColor.BLACK || piece.isKing()) {
                Point bottomLeftPoint = getBottomLeftPoint(currPoint, 1);
                if (isPointTraversable(board, bottomLeftPoint, removedPieces) && !isPointNearStackTop(path, currPoint)) {
                    isLeaf = false;
                    path.push(bottomLeftPoint);
                    generatePossiblePiecePaths(board, path, paths, removedPieces);
                    path.pop();
                }

                Point bottomRightPoint = getBottomRightPoint(currPoint, 1);
                if (isPointTraversable(board, bottomRightPoint, removedPieces) && !isPointNearStackTop(path, currPoint)) {
                    isLeaf = false;
                    path.push(bottomRightPoint);
                    generatePossiblePiecePaths(board, path, paths, removedPieces);
                    path.pop();
                }
            }
        } else { // Okay, we have moved before already.
            if (piece.getColor() == PieceColor.WHITE || piece.isKing()) {
                Point topLeftPoint = getTopLeftPoint(currPoint, 2);
                if (isPointTraversable(board, topLeftPoint, removedPieces) && !isPointNearStackTop(path, currPoint)) {
                    isLeaf = false;
                    path.push(topLeftPoint);
                    removedPieces.add(topLeftPoint);
                    generatePossiblePiecePaths(board, path, paths, removedPieces);
                    path.pop();
                    removedPieces.remove(topLeftPoint);
                }

                Point topRightPoint = getTopRightPoint(currPoint, 2);
                if (isPointTraversable(board, topRightPoint, removedPieces) && !isPointNearStackTop(path, currPoint)) {
                    isLeaf = false;
                    path.push(topRightPoint);
                    removedPieces.add(topRightPoint);
                    generatePossiblePiecePaths(board, path, paths, removedPieces);
                    path.pop();
                    removedPieces.remove(topRightPoint);
                }
            }

            if (piece.getColor() == PieceColor.BLACK || piece.isKing()) {
                Point bottomLeftPoint = getBottomLeftPoint(currPoint, 2);
                if (isPointTraversable(board, bottomLeftPoint, removedPieces) && !isPointNearStackTop(path, currPoint)) {
                    isLeaf = false;
                    path.push(bottomLeftPoint);
                    removedPieces.add(bottomLeftPoint);
                    generatePossiblePiecePaths(board, path, paths, removedPieces);
                    path.pop();
                    removedPieces.add(bottomLeftPoint);
                }

                Point bottomRightPoint = getBottomRightPoint(currPoint, 2);
                if (isPointTraversable(board, bottomRightPoint, removedPieces) && !isPointNearStackTop(path, currPoint)) {
                    isLeaf = false;
                    path.push(bottomRightPoint);
                    removedPieces.add(bottomRightPoint);
                    generatePossiblePiecePaths(board, path, paths, removedPieces);
                    path.pop();
                    removedPieces.add(bottomRightPoint);
                }
            }
        }

        if (isLeaf) {
            paths.add(path.toArray(new Point[0]));
        }
    }

    private boolean isPointNearStackTop(Stack<Point> path, Point target)
    {
        int distance = 0;
        Stack<Point> pathClone = clonePointStack(path);
        while (!pathClone.empty()) {
            if (distance > 1) return false;
            else if (pathClone.pop().equals(target) && distance <= 1) return true;
            else distance++;
        }

        return false;
    }

    private Stack<Point> clonePointStack(Stack<Point> origStack)
    {
        Stack<Point> clonedStack = new Stack<>();
        Queue<Point> tmpQueue = new LinkedList<>();

        while (!origStack.empty()) tmpQueue.add(origStack.pop());
        while (!tmpQueue.isEmpty()) {
            Point item = tmpQueue.remove();
            origStack.push(item); // No need to clone because they're the same objects anyway from the original stack.
            clonedStack.push(item.clone());
        }

        return clonedStack;
    }

    private PieceColor getOppositeColor(PieceColor color)
    {
        return (color == PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;
    }

    private void printBoardValue(Board board)
    {
        System.out.println("Black pieces");
        for (Piece blackPiece : board.getBlackPieces()) {
            System.out.println("Point: " + blackPiece.getPoint());
        }

        System.out.println("White pieces");
        for (Piece whitePiece : board.getWhitePieces()) {
            System.out.println("Point: " + whitePiece.getPoint());
        }
    }

    private void drawBoard(Board board)
    {
        System.out.println("Board drawing");
        printBoardValue(board);
        System.out.println(board.hasPieceAt(new Point(0, 0)));

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (!board.hasPieceAt(new Point(x, y))) {
                    System.out.print("*");
                } else {
                    PieceColor pointColor = board.getPieceAt(new Point(x, y)).getColor();
                    if (pointColor == PieceColor.BLACK) {
                        System.out.print("x");
                    } else {
                        System.out.print("o");
                    }
                }
            }

            System.out.print("\n");
        }
    }

    private boolean isPointTraversable(Board board, Point target, HashSet<Point> removedPoints)
    {
        return !board.hasPieceAt(target) && isPointInBoard(target) && !removedPoints.contains(target);
    }

    private boolean isPointInBoard(Point target)
    {
        return (target.x >= 0 && target.x < 8) && (target.y >= 0 && target.y < 8);
    }

    private Point getTopLeftPoint(Point source, int distance)
    {
        return new Point(source.x - distance, source.y - distance);
    }

    private Point getTopRightPoint(Point source, int distance)
    {
        return new Point(source.x + distance, source.y - distance);
    }

    private Point getBottomLeftPoint(Point source, int distance)
    {
        return new Point(source.x - distance, source.y + distance);
    }

    private Point getBottomRightPoint(Point source, int distance)
    {
        return new Point(source.x + distance, source.y + distance);
    }
}
