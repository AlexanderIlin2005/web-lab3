package area_zone;

import beans.Point;

import lombok.Data;

@Data
public class SectorArea extends Area {

    public SectorArea(Area area) {
        addArea(area);
    }
    @Override
    protected boolean cheakArea(Point point) {
        return (point.getX() <= 0 && point.getY() >= 0) &&
                ((Math.pow(point.getX(), 2) + Math.pow(point.getY(), 2)) <= (Math.pow(point.getR() / 2, 2)));
    }

}

