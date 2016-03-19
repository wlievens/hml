package heraldry.render;

import heraldry.model.Tincture;

public interface Painter
{
    Color getOrdinaryBorderColor();

    Color getOuterBorderColor();

    double getChiefHeight();

    double getFretMargin();

    double getFretSizeStep();

    double getFrettyPatternSize();

    double getGridPatternSize();

    double getLinePeriodFactor();

    double getOrdinaryThickness();

    Paint getPaint(Tincture tincture);
}
