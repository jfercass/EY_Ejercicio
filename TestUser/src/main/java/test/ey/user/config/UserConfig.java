package test.ey.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;
import test.ey.user.security.UserRequestInfo;

@Configuration
public class UserConfig {

    @Bean
    @RequestScope
    public UserRequestInfo requestScopedBean() {
        return new UserRequestInfo();
    }
}
