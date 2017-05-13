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
                        addPieceToPieces(blackPieces, row, col, PieceColor.BLACK);
                    } else {
                        addPieceToPieces(whitePieces, row, col, PieceColor.WHITE);
                    }
                } else {
                    if (col % 2 == 0) {
                        addPieceToPieces(whitePieces, row, col, PieceColor.WHITE);
                    } else {
                        addPieceToPieces(blackPieces, row, col, PieceColor.BLACK);
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

    /*public boolean hasPieceAt(Point p)
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

    public void movePiece(Point p, Point newPoint)
    {
        if (blackPieces.containsKey(p)) {

        } else if (whitePieces.containsKey(p)) {

        }
    }*/

    private void addPieceToPieces(HashMap<Point, Piece> pieces, int row, int col, PieceColor c)
    {
        Point p = new Point(row, col);
        pieces.put(p, new Piece(c, p));
    }
}
