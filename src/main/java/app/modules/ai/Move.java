package app.modules.ai;


import app.utils.helper.Point;

import java.util.ArrayList;

public class Move
{
    private ArrayList<Point> movementPoints;

    public Move() {}

    public void addPoint(Point p)
    {
        movementPoints.add(p);
    }

    public Point[] getPoints()
    {
        return movementPoints.toArray(new Point[0]);
    }
}
