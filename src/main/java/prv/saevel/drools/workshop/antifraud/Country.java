package prv.saevel.drools.workshop.antifraud;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Country {

    private String name;

    private boolean blacklisted;
}
