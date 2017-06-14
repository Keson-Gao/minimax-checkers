package app.modules.board;

import app.utils.enums.PieceColor;
import app.utils.helper.Point;

import java.util.*;

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
                        addPieceToPieces(whitePieces, 7 - row, col);
                    }
                } else {
                    if (col % 2 == 0) {
                        addPieceToPieces(whitePieces, 7 - row, col);
                    } else {
                        addPieceToPieces(blackPieces, row, col);
                    }
                }
            }
        }
    }

    public Board(HashMap<Point, Piece> blackPieces, HashMap<Point, Piece> whitePieces)
    {
        this.blackPieces = blackPieces;
        this.whitePieces = whitePieces;
    }

    public Piece[] getBlackPieces()
    {
        return blackPieces.values().toArray(new Piece[0]);
    }

    public Piece[] getWhitePieces()
    {
        return whitePieces.values().toArray(new Piece[0]);
    }

    public boolean hasPieceAt(Point p)
    {
        return (blackPieces.containsKey(p)) || (whitePieces.containsKey(p)) && (p.x < 8 && p.y < 8);
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

    public void setKingPieceAt(Point p)
    {
        if (blackPieces.containsKey(p)) {
            blackPieces.get(p).setKing();
        } else if (whitePieces.containsKey(p)) {
            whitePieces.get(p).setKing();
        }
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
        Iterator it = currentPieces.entrySet().iterator();
        while (it.hasNext()) {
            // We get the distance of each coordinate to the nearest coordinate edge of a piece to help compute
            // the value of each piece. The coordinate edge is where the board ends in a given coordinate. For
            // example, the x-coordinate edge starts from the leftmost cell to the rightmost cell (disregarding
            // the y-position). On the other hand, the y-coordinate edge starts from the uppermost cell to the
            // bottommost cell (disregarding the x-position).
            Map.Entry piece = (Map.Entry) it.next();
            Point p = (Point) piece.getKey();
            int xDistanceFromEdge = (p.x > 3) ? (4 - (p.x - 3)) : p.x;
            int yDistanceFromEdge = (p.y > 3) ? (4 - (p.y - 3)) : p.y;
            int multiplier = (((Piece) piece.getValue()).isKing()) ? 2 : 1;

            totalValue += (4 - Math.min(xDistanceFromEdge, yDistanceFromEdge)) * multiplier;
            it.remove();
        }

        return totalValue;
    }

    @Override public Board clone()
    {
        return new Board(deepCopyHashMap(blackPieces), deepCopyHashMap(whitePieces));
    }

    private HashMap<Point, Piece> deepCopyHashMap(HashMap<Point, Piece> source)
    {
        HashMap<Point, Piece> target = new HashMap<>();
        Iterator it = source.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();

            target.put((Point) pair.getKey(), (Piece) pair.getValue());

            it.remove();
        }

        return target;
    }

    private void addPieceToPieces(HashMap<Point, Piece> pieces, int row, int col)
    {
        Point p = new Point(col, row);
        if (pieces == blackPieces) pieces.put(p, new Piece(PieceColor.BLACK, p));
        else if (pieces == whitePieces) pieces.put(p, new Piece(PieceColor.WHITE, p));
    }
}
