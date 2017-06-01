package app.modules.board;

import app.utils.enums.PieceColor;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Board
{
    private HashMap<Point, Piece> blackPieces;
    private HashMap<Point, Piece> whitePieces;

    public Board()
    {
        blackPieces = new HashMap<>();
        whitePieces = new HashMap<>();

        // Set the position of the pieces.
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 8; col++) {
                if (row == 1) {
                    if (col % 2 == 0) {
                        addPieceToPieces(blackPieces, row, col);
                    } else {
                        addPieceToPieces(whitePieces, row, col);
                    }
                } else {
                    if (col % 2 == 0) {
                        addPieceToPieces(whitePieces, row, col);
                    } else {
                        addPieceToPieces(blackPieces, row, col);
                    }
                }
            }
        }
    }

    public Point[] getBlackPieces()
    {
        return Arrays.stream(new ArrayList<>(blackPieces.keySet()).toArray()).toArray(Point[]::new);
    }

    public Point[] getWhitePieces()
    {
        return Arrays.stream(new ArrayList<>(whitePieces.keySet()).toArray()).toArray(Point[]::new);
    }

    public boolean hasPieceAt(Point p)
    {
        return (blackPieces.containsKey(p)) || (whitePieces.containsKey(p));
    }

    public Piece getPieceAt(Point p)
    {
        if (blackPieces.containsKey(p)) {
            return blackPieces.get(p);
        } else if (whitePieces.containsKey(p)) {
            return whitePieces.get(p);
        }

        return null;
    }

    public void removePieceAt(Point p)
    {
        if (blackPieces.containsKey(p)) {
            blackPieces.remove(p);
        } else if (whitePieces.containsKey(p)) {
            whitePieces.remove(p);
        } else {
            throw new NullPointerException("Piece does not exist at (" + p.x + ", " + p.y + ".");
        }
    }

    public void movePieceTo(Point p, Point newPoint)
    {
        if (blackPieces.containsKey(p)) {
            Piece piece = blackPieces.get(p);
            piece.setPoint(newPoint);
            blackPieces.remove(p);
            blackPieces.put(newPoint, piece);
        } else if (whitePieces.containsKey(p)) {
            Piece piece = whitePieces.get(p);
            piece.setPoint(newPoint);
            whitePieces.remove(p);
            whitePieces.put(newPoint, piece);
        } else {
            throw new NullPointerException("Piece does not exist at (" + p.x + ", " + p.y + ".");
        }
    }

    public int getBoardValue(PieceColor color)
    {
        HashMap<Point, Piece> currentPieces;
        if (color == PieceColor.BLACK) currentPieces = blackPieces;
        else currentPieces = whitePieces;

        int totalValue = 0;

        // The value of each piece is determined by its distance from the center of the board with
        // the center having a value of 1.
        for (Point p : currentPieces.keySet()) {
            // We get the distance of each coordinate to the nearest coordinate edge of a piece to help compute
            // the value of each piece. The coordinate edge is where the board ends in a given coordinate. For
            // example, the x-coordinate edge starts from the leftmost cell to the rightmost cell (disregarding
            // the y-position). On the other hand, the y-coordinate edge starts from the uppermost cell to the
            // bottommost cell (disregarding the x-position).
            int xDistanceFromEdge = (p.x > 3) ? (4 - (p.x - 3)) : p.x;
            int yDistanceFromEdge = (p.y > 3) ? (4 - (p.y - 3)) : p.y;
            totalValue += 4 - Math.min(xDistanceFromEdge, yDistanceFromEdge);
        }

        return totalValue;
    }

    private void addPieceToPieces(HashMap<Point, Piece> pieces, int row, int col)
    {
        Point p = new Point(row, col);
        if (pieces == blackPieces) pieces.put(p, new Piece(PieceColor.BLACK, p));
        else if (pieces == whitePieces) pieces.put(p, new Piece(PieceColor.WHITE, p));
    }
}
