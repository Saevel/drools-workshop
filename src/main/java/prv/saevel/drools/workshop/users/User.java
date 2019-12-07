package prv.saevel.drools.workshop.users;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class User {

    private long id;

    private String country;

    private List<Account> accounts;
}
