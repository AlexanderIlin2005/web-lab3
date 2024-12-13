package beans;

import jakarta.enterprise.context.ApplicationScoped;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@ApplicationScoped
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
