package test.ey.user.service;

import com.fasterxml.jackson.datatype.jsr310.ser.ZoneIdSerializer;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import test.ey.user.dto.*;
import test.ey.user.entity.Phone;
import test.ey.user.entity.User;
import test.ey.user.exception.UserException;
import test.ey.user.repository.PhoneRepository;
import test.ey.user.repository.UserRepository;
import test.ey.user.security.CustomerDetailsService;
import test.ey.user.security.UserRequestInfo;
import test.ey.user.security.jwt.JwtUtil;
import test.ey.user.utils.Messages;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;
    private PhoneRepository phoneRepository;
    private PhoneService phoneService;
    private AuthenticationManager authenticationManager;
    private CustomerDetailsService customerDetailsService;
    private JwtUtil jwtUtil;
    private UserRequestInfo userRequestInfo;
    public UserServiceImpl(UserRepository userRepository, PhoneRepository phoneRepository, AuthenticationManager authenticationManager, CustomerDetailsService customerDetailsService, JwtUtil jwtUtil, UserRequestInfo userRequestInfo, PhoneService phoneService){
        this.userRepository  = userRepository;
        this.phoneRepository = phoneRepository;
        this.authenticationManager = authenticationManager;
        this.customerDetailsService = customerDetailsService;
        this.jwtUtil = jwtUtil;
        this.userRequestInfo = userRequestInfo;
        this.phoneService = phoneService;
    }

    @Override
    public UserDTO getUser(Long id){

        Optional<User> userOptional = userRepository.findById(id);

        if(userOptional.isPresent()){
            UserResponseDTO userDTO = convert(userOptional.get(), userRequestInfo.getToken());

            return userDTO;
        } else {
            log.error(Messages.user_get_error);
            throw new UserException(Messages.user_get_error);
        }

    }

    @Override
    public UserResponseDTO login(LoginDTO loginDTO) {
        String token = login_(loginDTO);

        User user = userRequestInfo.getUser();
        user.setLastLogin(LocalDateTime.now());
        user.setToken(token);
        user = userRepository.save(user);

        UserResponseDTO userResponseDTO = convert(user, token);

        return userResponseDTO;
    }

    @Override
    public String login_(LoginDTO loginDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
            );
            if(authentication.isAuthenticated()) {
                String token = jwtUtil.generateToken(userRequestInfo.getUser().getUsrEmail(), userRequestInfo.getUser().getUsrRole());
                return token;
            }
        } catch (Exception e) {
            throw new UserException("Login Error");
        }
        // retornar un json
        throw new UserException("Credenciales incorrectas");
    }


    @Override
    public UserResponseDTO createUser(UserDTO userDTO) {

        User user = new User();
        user.setUsrName(userDTO.getName());
        user.setUsrPassword(userDTO.getPassword());
        user.setUsrEmail(userDTO.getEmail());
        user.setUsrRole("user");
        UserResponseDTO userDTOAux = new UserResponseDTO();
        try {
            User userPersist = userRepository.save(user);

            userPersist.setPhones(new ArrayList<>());
            User finalUserPersist = userPersist;

            userDTO.getPhoneDtos().forEach(phoneDTO -> {
                Phone phone = new Phone();
                phone.setUsrId(finalUserPersist.getUsrId());
                phone.setPhoNumber(phoneDTO.getPhoNumber());
                phone.setPhoCityCode(phoneDTO.getPhoCityCode());
                phone.setPhoCountryCode(phoneDTO.getPhoCountryCode());
                phone.setUser(finalUserPersist);
                phoneRepository.save(phone);
                finalUserPersist.getPhones().add(phone);
            });

            userPersist.setLastLogin(userPersist.getCreation());

            LoginDTO loginDTO = new LoginDTO(userDTO.getEmail(), userDTO.getPassword());
            String token = login_(loginDTO);
            userPersist.setToken(token);
            userPersist = userRepository.save(userPersist);

            userDTOAux = convert(userPersist, token);

        }catch (Exception e){
            log.error("", e);
            throw e;
        }

        return userDTOAux;
    }

    private UserResponseDTO convert(User user, String token){
        UserResponseDTO userResponseDTO = new UserResponseDTO();

        userResponseDTO.setName(user.getUsrName());
        userResponseDTO.setEmail(user.getUsrEmail());
        userResponseDTO.setId(user.getUsrId());
        if(user.getPhones() != null)
            userResponseDTO.setPhoneDtos(user.getPhones().stream().map(phone -> new PhoneDTO(phone.getPhoNumber(), phone.getPhoCityCode(), phone.getPhoCountryCode())).collect(Collectors.toList()));
        userResponseDTO.setCreation(user.getCreation());
        userResponseDTO.setModified(user.getModified());
        userResponseDTO.setLastLogin(user.getLastLogin());
        userResponseDTO.setIsActive(user.getIsActive());
        userResponseDTO.setToken(token);

        return userResponseDTO;
    }

}
