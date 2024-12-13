package area_zone;

import beans.Point;

import java.io.Serializable;

import lombok.Data;

@Data
public abstract class Area implements Serializable {
    private Area nextDecorator;

    protected boolean cheakArea(Point point) {
        return false;
    }

    public boolean cheakAreaDecorate(Point point) {
        Area decorate = nextDecorator;
        if(cheakArea(point)) return true;
        while (decorate != null){
            if(decorate.cheakArea(point)) return true;
            decorate = decorate.nextDecorator;
        }
        return false;
    }

    public void addArea(Area area) {
        if(nextDecorator == null){
            setNextDecorator(area);
            return;
        }
        Area decorate = nextDecorator;
        while (decorate.nextDecorator != null) {
            decorate= decorate.nextDecorator;
        }
        decorate.nextDecorator = area;
    }

}
