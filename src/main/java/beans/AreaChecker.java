package beans;

import area_zone.Area;
import area_zone.RectangleArea;
import area_zone.SectorArea;
import area_zone.TriangleArea;

import jakarta.ejb.Singleton;

import jakarta.inject.Named;

import java.io.Serializable;

@Singleton
@Named
public class AreaChecker implements Serializable {

    private final static Area area = new RectangleArea(new TriangleArea(new SectorArea(null)));

    public boolean checkPoint(Point point) {
        return area.cheakAreaDecorate(point);
    }

}
