package test.ey.user.service;

import test.ey.user.dto.LoginDTO;
import test.ey.user.dto.TokenDTO;
import test.ey.user.dto.UserDTO;

import java.util.Map;

public interface UserService {
    public UserDTO getUser(Long id);
    public String login_(LoginDTO loginDTO);

    public UserDTO login(LoginDTO loginDTO);
    public UserDTO createUser(UserDTO user);
}
