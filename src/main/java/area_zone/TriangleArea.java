package area_zone;

import beans.Point;

import lombok.Data;

@Data
public class TriangleArea extends Area {

    public TriangleArea(Area area) {
        addArea(area);
    }

    @Override
    protected boolean cheakArea(Point point) {
        return (point.getX() >= 0 && point.getY() <= 0) &&
                (point.getY() >= (point.getX()/2 - point.getR()/2));
    }

    // y=0 x=r
        // y = x*k - r/2
    // k = (y+r/2)/x  k = (0 + r/2)/r
    // k = 1/2
    // y >= x/2 - r/2
}
