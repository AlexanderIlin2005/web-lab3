package beans;

import util.CustomFormatter;

import jakarta.enterprise.context.ApplicationScoped;

import jakarta.inject.Named;

import java.time.Instant;
import java.time.ZoneId;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Named("attempt")
@ApplicationScoped
@NoArgsConstructor
@AllArgsConstructor
public class PointAttempt implements Serializable {

    @Setter @Getter
    private Instant attemptTime;
    @Setter @Getter
    private ZoneId zoneId;
    @Setter @Getter
    private double processTime;
    @Setter @Getter
    private boolean success;

    @Setter @Getter
    private Point point;

    public String getTimeStampFormatted() {
        return attemptTime.atZone(zoneId).format(CustomFormatter.getFormatterAtOffset(zoneId));
    }

}
