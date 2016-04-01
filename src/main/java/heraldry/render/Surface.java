package heraldry.render;

import heraldry.render.path.Path;
import heraldry.util.GeometryUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.awt.geom.Area;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
@ToString
@RequiredArgsConstructor
public class Surface
{
    private final List<Path> positives;
    private final List<Path> negatives;

    public Surface(Path... paths)
    {
        this(Arrays.asList(paths), Collections.emptyList());
    }

    public Area createArea()
    {
        Area area = new Area();
        positives.stream().map(GeometryUtils::convertPathToArea).forEach(area::add);
        negatives.stream().map(GeometryUtils::convertPathToArea).forEach(area::subtract);
        return area;
    }
}
