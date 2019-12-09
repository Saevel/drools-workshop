package prv.saevel.drools.workshop.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Account {

    private long id;

    private long userId;

    private double balance;

    private Currency currency;
}
