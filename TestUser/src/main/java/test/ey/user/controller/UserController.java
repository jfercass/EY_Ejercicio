package test.ey.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import test.ey.user.dto.LoginDTO;
import test.ey.user.dto.PhoneDTO;
import test.ey.user.dto.TokenDTO;
import test.ey.user.dto.UserDTO;
import test.ey.user.security.UserRequestInfo;
import test.ey.user.service.PhoneService;
import test.ey.user.service.PhoneServiceImpl;
import test.ey.user.service.UserService;
import test.ey.user.service.UserServiceImpl;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController()
@RequestMapping("/user")
public class UserController {

    private UserService userService;
    private PhoneService phoneService;
    @Resource(name = "requestScopedBean")
    private UserRequestInfo userRequestInfo;

    @Autowired
    public UserController(UserServiceImpl userService, PhoneServiceImpl phoneService){
        this.userService = userService;
        this.phoneService = phoneService;
    }


    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO getUser(@PathVariable Long id){
        UserDTO userDTO = userService.getUser(id);
        return userDTO;
    }

    @PostMapping(path = "/create")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO){
        UserDTO userDTOPers = userService.createUser(userDTO);
        return new ResponseEntity<UserDTO>(userDTOPers, HttpStatus.CREATED);
    }

    @PostMapping(path = "/login")
    public UserDTO loginUser(@Valid @RequestBody LoginDTO loginDTO){
        return userService.login(loginDTO);
    }

    @GetMapping(path = "/{id}/phone", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PhoneDTO> getPhoneList(@PathVariable Long id){
        return phoneService.getList(id);
    }

    @PostMapping(path = "/{id}/phone", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PhoneDTO> getPhoneList(@PathVariable Long id, @Valid @RequestBody PhoneDTO phoneDTO){
        return new ResponseEntity<>(phoneService.add(id, phoneDTO), HttpStatus.CREATED);
    }

}
