package heraldry.render;

import heraldry.model.Tincture;
import heraldry.render.paint.Color;
import heraldry.render.paint.Paint;

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

    Paint getCounterchangedPaint(Tincture firstTincture, Tincture secondTincture);

    Color getMobileBorderColor();
}
