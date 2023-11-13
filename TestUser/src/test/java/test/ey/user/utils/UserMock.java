package test.ey.user.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import test.ey.user.dto.UserDTO;
import test.ey.user.dto.UserResponseDTO;

public class UserMock {

    public static UserDTO getUser(String name, String password) throws JsonProcessingException {
        UserDTO userDTO = new UserDTO();
        userDTO.setName(name);
        userDTO.setPassword(password);
        userDTO.setEmail(name.concat("@mock.com"));
        return userDTO;
    }

    public static UserResponseDTO getUser(Long id, String name) throws JsonProcessingException {
        UserResponseDTO userDTO = new UserResponseDTO();
        userDTO.setName(name);
        userDTO.setEmail(name.concat("@mock.com"));
        userDTO.setId(id);
        return userDTO;
    }

    public static UserDTO jsonToUser(String jsonUser) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonUser, UserDTO.class);
    }

    public static UserResponseDTO jsonToUser2(String jsonUser) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonUser, UserResponseDTO.class);
    }

    public static String userToJson(UserDTO userDTO) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(userDTO);
    }

}
