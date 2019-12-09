package prv.saevel.drools.workshop.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class User {

    private long id;

    private String country;

    private List<Account> accounts;

    private boolean trusted;
}
