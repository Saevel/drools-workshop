package prv.saevel.drools.workshop.antifraud;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FraudScore {

    private long accountId;

    private double score;
}
