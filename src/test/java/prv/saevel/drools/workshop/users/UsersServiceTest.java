package prv.saevel.drools.workshop.users;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import prv.saevel.drools.workshop.model.Account;
import prv.saevel.drools.workshop.model.Currency;
import prv.saevel.drools.workshop.model.User;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {UsersServiceTestContext.class})
public class UsersServiceTest {

    @Autowired
    private UsersService usersService;

    @Autowired
    private CrudRepository<User, Long> usersRepository;

    private List<User> usersWithoutAccounts = Arrays.asList(
            new User(1L, "Brazil", null, true),
            new User(2L, "USA", null, false),
            new User(3L, "Brazil", null, true),
            new User(4L, "France", null, false)
    );

    @Test
    public void shouldAssignDefaultAccountsToUsersWithoutAny(){
        for (User user : usersWithoutAccounts) {
            usersRepository.save(user);
        }

        usersService.executeUserRules();
        Iterable<User> processedUsers = usersRepository.findAll();

        for(User processedUser : processedUsers){
            verifyDefaultAccount(processedUser);
        }
    }

    private void verifyDefaultAccount(User processedUser) {
        assertNotNull("User: " + processedUser.getId() + " has null accounts", processedUser.getAccounts());

        Account account = processedUser.getAccounts().get(0);

        assertNotNull("User: " + processedUser.getId() + " has empty accounts", account);

        assertEquals(
                "User's " + processedUser.getId() + "first account does not have the approprite id",
                processedUser.getId(),
                account.getUserId()
        );

        assertEquals(
                "User's " + processedUser.getId() + " first account does not have USD as the default currenty",
                Currency.USD,
                account.getCurrency()
        );

        assertTrue(
                "User's " + processedUser.getId() + " first account does not have the empty balance",
                Math.abs(account.getBalance()) <= 0.01
        );
    }
}
