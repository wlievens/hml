package heraldry.render;

import heraldry.render.path.Path;
import heraldry.util.GeometryUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Getter
@ToString
@RequiredArgsConstructor
public final class OrdinaryContour
{
    private final RenderContour contour;

    private final Path spine;
}
