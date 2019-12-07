package prv.saevel.drools.workshop.users;

import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public class UsersService {

    @Autowired
    private CrudRepository<User, Long> userRepository;

    @Autowired
    @Qualifier("userRulesSession")
    private KieSession session;

    public void executeUserRules(){

        for(User user: userRepository.findAll()){
            session.insert(user);
        }

        session.fireAllRules();
    }

    public User save(User user){
        return userRepository.save(user);
    }
}
