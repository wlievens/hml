package heraldry.render.path;

import heraldry.render.Point;

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
    
    double getLength();
    
    Point sample(double ratio);
}
