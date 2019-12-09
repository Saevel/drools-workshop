package prv.saevel.drools.workshop.antifraud;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class AccountServiceStub implements AccountService {

    @Getter
    private List<Long> blockedAccountIds = new ArrayList<>();

    @Override
    public void blockAccount(long accountId) {
        blockedAccountIds.add(accountId);
    }
}
