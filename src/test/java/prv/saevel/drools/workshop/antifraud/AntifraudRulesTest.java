package prv.saevel.drools.workshop.antifraud;

import org.drools.core.QueryResultsRowImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.Row;
import prv.saevel.drools.workshop.model.Account;
import prv.saevel.drools.workshop.model.Currency;
import prv.saevel.drools.workshop.model.Transaction;
import prv.saevel.drools.workshop.model.User;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class AntifraudRulesTest {

    private Stream<Country> blacklistedCountries = Stream.of(
            new Country("North Corea", true),
            new Country("Iran", true),
            new Country("Venezuela", true),
            new Country("Cayman Islands", true),
            new Country("Virgin Islands", true)
    );

    private LongStream truestedUsers = LongStream.of(1, 2, 3 , 4, 5);

    public KieBase kieBase = KieServices.get().newKieClasspathContainer().getKieBase("antifraudRules");

    public KieSession session;

    private AccountServiceStub accountService;

    @Before
    public void setUp(){
        session = kieBase.newKieSession();
        accountService = new AccountServiceStub();
        session.setGlobal("accountService", accountService);
    }

    @After
    public void tearDown(){
        session.dispose();
    }

    @Test
    public void shouldAddFraudEventForTransactionFromBlacklistedCountry(){
        Account account = new Account(1, 1, 0.0, Currency.EUR);
        simulateTransactionFromBlacklistedCountry(account, session);

        session.insert(account);
        blacklistedCountries.forEach(session::insert);

        session.fireAllRules();

        QueryResults results = session.getQueryResults(
                "findFraudEvent",
                new Object[]{account.getId(), FraudEventType.TRANSACTION_FROM_BLACKLISTED_COUNTRY}
        );

        assertEquals(
                "The number of Transaction From Blacklisted Country events for given accountId: " + account.getId() + "is not 1.",
                1,
                results.size()
        );
    }

    @Test
    public void shouldCreateFraudEventForAccountEmptyingTransaction(){
        Account account = new Account(1, 1, 100.0, Currency.EUR);
        simulateAccountEmptying(account, session);

        session.insert(account);

        session.fireAllRules();

        QueryResults results = session.getQueryResults(
                "findFraudEvent",
                new Object[]{account.getId(), FraudEventType.ACCOUNT_EMPTYING}
        );

        assertEquals(
                "The number of Account Emptying events for given accountId: " + account.getId() + "is not 1.",
                1,
                results.size()
        );
    }

    @Test
    public void shouldCreateFraudEventIfTransactionsFromDifferentCountriesUnder3mins(){
        Account account = new Account(1, 1, 100.0, Currency.EUR);

        simulateTwoTransactionsFromDifferentCountriesWithinThreeMins(session, account);
        session.insert(account);

        session.fireAllRules();

        QueryResults results = session.getQueryResults(
                "findFraudEvent",
                new Object[]{account.getId(), FraudEventType.TRANSACTION_FROM_MULTIPLE_COUNTRIES}
        );

        assertEquals(
                "The number of Transaction From Multiple Countries FraudEvents for : " + account.getId() + "is not 1.",
                1,
                results.size()
        );
    }

    @Test
    public void shouldBlockAnUntrustedUserIfHisScoreReachesTwo() throws InterruptedException {
        User user = new User(1, "Brazil", new LinkedList<>(), false);
        Account account = new Account(1, user.getId(), 100.0, Currency.USD);

        simulateAccountEmptying(account, session);
        simulateTransactionFromBlacklistedCountry(account, session);
        simulateTwoTransactionsFromDifferentCountriesWithinThreeMins(session, account);

        session.insert(user);
        session.insert(account);

        session.fireAllRules();

        List<Long> blockedAccountIds = accountService.getBlockedAccountIds();

        assertNotNull("Blocked account ids are null", blockedAccountIds);
        assertFalse("Blocked account ids are empty", blockedAccountIds.isEmpty());

        Long blockedAccountOne = blockedAccountIds.get(0);

        assertEquals("The given account was not blocked", (long) blockedAccountOne, account.getId());
    }

    @Test
    public void shouldNotBlockATrustedUserIfHisScoreReachesTwo() throws InterruptedException {
        User user = new User(1, "Brazil", new LinkedList<>(), true);
        Account account = new Account(1, user.getId(), 100.0, Currency.USD);

        simulateAccountEmptying(account, session);
        simulateTransactionFromBlacklistedCountry(account, session);
        simulateTwoTransactionsFromDifferentCountriesWithinThreeMins(session, account);

        session.insert(user);
        session.insert(account);

        session.fireAllRules();

        List<Long> blockedAccountIds = accountService.getBlockedAccountIds();

        assertNotNull("Blocked account ids are null", blockedAccountIds);
        assertTrue("The user's account was blocked despite his trusted status", blockedAccountIds.isEmpty());
    }

    @Test
    public void shouldBlockATrustedUserIfHisScoreReachesThree() throws InterruptedException {
        User user = new User(1, "Brazil", new LinkedList<>(), false);
        Account account = new Account(1, user.getId(), 100.0, Currency.USD);

        simulateAccountEmptying(account, session);
        simulateTransactionFromBlacklistedCountry(account, session);
        simulateTwoTransactionsFromDifferentCountriesWithinThreeMins(session, account);
        simulateTwoTransactionsFromDifferentCountriesWithinThreeMins(session, account);
        simulateTwoTransactionsFromDifferentCountriesWithinThreeMins(session, account);
        simulateTwoTransactionsFromDifferentCountriesWithinThreeMins(session, account);

        session.insert(user);
        session.insert(account);

        session.fireAllRules();

        List<Long> blockedAccountIds = accountService.getBlockedAccountIds();

        assertNotNull("Blocked account ids are null", blockedAccountIds);
        assertFalse("Blocked account ids are empty", blockedAccountIds.isEmpty());

        Long blockedAccountOne = blockedAccountIds.get(0);

        assertEquals("The given account was not blocked", (long) blockedAccountOne, account.getId());
    }

    private void simulateTwoTransactionsFromDifferentCountriesWithinThreeMins(KieSession session,
                                                                              Account account){
        long now = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        Transaction transactionOne = new Transaction(
                1234L,
                account.getId(),
                now,
                "France",
                9,
                Currency.USD
        );
        Transaction transactionTwo = new Transaction(
                1234L,
                account.getId(),
                now + 120,
                "Germany",
                9,
                Currency.USD
        );

        session.insert(transactionOne);
        session.insert(transactionTwo);
    }

    private void simulateAccountEmptying(Account account, KieSession session) {

        Transaction transaction = new Transaction(
                1234L,
                account.getId(),
                LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
                "France",
                0.99 * account.getBalance(),
                Currency.USD
        );

        session.insert(transaction);
    }

    private void simulateTransactionFromBlacklistedCountry(Account account, KieSession session) {
        Transaction transaction = new Transaction(
                1234L,
                account.getId(),
                LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
                "Iran",
                12.0,
                Currency.USD
        );
        session.insert(transaction);
    }
}
