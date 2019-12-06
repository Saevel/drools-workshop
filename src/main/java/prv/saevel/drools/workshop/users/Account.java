package prv.saevel.drools.workshop.users;

import lombok.Data;

@Data
public class Account {

    private long userId;

    private double blance;

    private String currency;
}
