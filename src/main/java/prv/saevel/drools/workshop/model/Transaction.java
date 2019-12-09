package prv.saevel.drools.workshop.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Transaction {

    private long id;

    private long accountId;

    private long timestamp;

    private String country;

    private double amount;

    private Currency currency;
}
