package app.utils.helper;

import app.modules.board.Board;

public class Pair
{
    public Board board;
    public int score;

    public Pair(Board board, int score)
    {
        this.board = board;
        this.score = score;
    }
}
