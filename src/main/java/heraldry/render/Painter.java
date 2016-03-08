package heraldry.render;

import heraldry.model.Tincture;

public interface Painter
{
    double getOrdinaryThickness();

    Paint getColor(Tincture tincture);
    
    Color getOrdinaryBorderColor();
    
    Color getOuterBorderColor();

    double getChequyPatternSize();

    double getFrettyPatternSize();

    double getFretMargin();

    double getFretSizeStep();

    double getLinePeriodFactor();
}
