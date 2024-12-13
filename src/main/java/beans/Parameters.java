package beans;

import jakarta.enterprise.context.ApplicationScoped;

import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Named("parameters")
@ApplicationScoped
@AllArgsConstructor
@NoArgsConstructor
public class Parameters implements Serializable {

    @Inject
    transient private AreaChecker areaChecker;
    @Inject
    transient private Model model;
    @Getter @Setter
    private double x;
    @Getter @Setter
    private double hiddenX;
    @Getter @Setter
    private double y;
    @Getter @Setter
    private double[] rs;

    public Parameters(double x, double y, double[] rs) {
        this.x = x;
        this.y = y;
        this.rs = rs;
    }

    public void submit() {

        for (double r: rs) {
            double xToUse = hiddenX != 0 ? hiddenX : x;
            Point point = new Point(xToUse, y, r);

            final long start = System.nanoTime();

            final boolean res = areaChecker.checkPoint(point);

            PointAttempt attempt = new PointAttempt();
            attempt.setPoint(point);
            attempt.setSuccess(res);
            attempt.setAttemptTime(Instant.now());
            attempt.setProcessTime((System.nanoTime() - start) / 1000d);
            model.add(attempt);
            System.out.println(attempt);
        }
    }

}

