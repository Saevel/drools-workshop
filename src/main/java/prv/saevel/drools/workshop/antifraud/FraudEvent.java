package prv.saevel.drools.workshop.antifraud;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

@Data
@AllArgsConstructor
public class FraudEvent {

    private long accountId;

    private FraudEventType type;

    @Setter
    private boolean processed;

    private long occurrence;
}