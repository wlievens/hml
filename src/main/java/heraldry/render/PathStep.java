package heraldry.render;

public interface PathStep
{
    double getMinX();

    double getMinY();

    double getMaxX();

    double getMaxY();

    double getStartX();

    double getStartY();

    double getEndX();

    double getEndY();

    PathStep offset(double x, double y);
    
    PathStep inverse();
}
