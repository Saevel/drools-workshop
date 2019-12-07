package prv.saevel.drools.workshop.users;

import org.drools.core.reteoo.KieComponentFactory;
import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@ComponentScan
public class UsersServiceTestContext {
    @Bean(destroyMethod = "dispose")
    public KieContainer kieContainer(){
        return KieServices.get().getKieClasspathContainer();
    }

    @Bean(name = "userRules")
    public KieBase userRules(@Autowired KieContainer container){
        return container.newKieBase("userRules", null);
    }

    @Bean(name = "userRulesSession", destroyMethod = "dispose")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public KieSession userRulesSession(@Autowired @Qualifier("userRules") KieBase userRules,
                                       @Autowired UsersService usersService){
        KieSession session =  userRules.newKieSession();
        session.setGlobal("usersService", usersService);
        return session;
    }
}
