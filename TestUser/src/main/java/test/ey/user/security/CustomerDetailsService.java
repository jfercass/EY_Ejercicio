package test.ey.user.security;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import test.ey.user.entity.User;
import test.ey.user.exception.UserException;
import test.ey.user.repository.UserRepository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Optional;

@Service
@Log4j2
public class CustomerDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Resource(name = "requestScopedBean")
    private UserRequestInfo userRequestInfo;

    @Override
    public UserDetails loadUserByUsername(String subject) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByUsrEmail(subject);

        if(optionalUser.isPresent()) {
            userRequestInfo.setUser(optionalUser.get());
            return new org.springframework.security.core.userdetails.User(optionalUser.get().getUsrEmail(), optionalUser.get().getUsrPassword(), new ArrayList<>());
        } else {
            throw new UserException("User not found");
        }
    }

}
