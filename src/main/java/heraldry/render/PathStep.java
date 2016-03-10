package heraldry.render;

public interface PathStep
{
    double getMinX();

    double getMinY();

    double getMaxX();

    double getMaxY();

    PathStep offset(double x, double y);
}
