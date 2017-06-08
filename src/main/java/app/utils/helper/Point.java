package app.utils.helper;

public class Point
{
    public int x;
    public int y;

    public Point(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    @Override public Point clone() { return new Point(x, y); }

    @Override public int hashCode()
    {
        return 31 * x + y;
    }

    @Override public boolean equals(Object obj)
    {
        if (obj instanceof Point) {
            Point p = (Point) obj;
            return (p.x == x) && (p.y == y);
        }

        return false;
    }
}
