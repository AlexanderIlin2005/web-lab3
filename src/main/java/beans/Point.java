package beans;

import javax.enterprise.context.SessionScoped;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@SessionScoped
@NoArgsConstructor
@AllArgsConstructor
public class Point implements Serializable {

    @Getter @Setter
    private double x;
    @Getter @Setter
    private double y;
    @Getter @Setter
    private double r;

}
