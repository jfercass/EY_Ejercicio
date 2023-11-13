package test.ey.user.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import test.ey.user.TestUserApplication;
import test.ey.user.config.TestConfig;
import test.ey.user.dto.UserDTO;
import test.ey.user.dto.UserResponseDTO;
import test.ey.user.security.UserRequestInfo;
import test.ey.user.security.jwt.JwtFilter;
import test.ey.user.service.PhoneServiceImpl;
import test.ey.user.service.UserServiceImpl;
import test.ey.user.utils.UserMock;

import java.nio.charset.Charset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class)
//@ContextConfiguration(classes = SecurityConfig.class)
//@WebAppConfiguration
@ActiveProfiles("test")
@ContextConfiguration(classes = { TestUserApplication.class, TestConfig.class })
class UserControllerTest {

    @Autowired
    private WebApplicationContext context;
  //  @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserServiceImpl userService;
    @MockBean
    private PhoneServiceImpl phoneService;

    @MockBean(name = "requestScopedBean")
    private UserRequestInfo userRequestInfo;
    @MockBean
    private JwtFilter jwtFilter;

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .alwaysDo(print())
                .apply(springSecurity())
                .build();
    }

    @DisplayName("Test OK - create a user")
    @Test
    void createUserOk() throws Exception {
        String name = "Jose";
        String pass = "Ac12345678";

        UserDTO userDTO = UserMock.getUser(name, pass);
        UserResponseDTO userDTOWithoutPass = UserMock.getUser(1L, name);

        when(userService.createUser(any(UserDTO.class))).thenReturn(userDTOWithoutPass);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/user/create")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(UserMock.userToJson(userDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        UserResponseDTO user = UserMock.jsonToUser2(response.getContentAsString());
        assertEquals(name, user.getName());
    //    assertTrue(!Objects.isNull(user.getPassword()));
        assertEquals(1, user.getId());

    }

    @DisplayName("Test ERROR - create user - empty password")
    @Test
    void createUserEmptyPassword() throws Exception {
        String name = "Jose";

        UserDTO userDTOWithoutPass = UserMock.getUser(1L, name);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/user/create")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(UserMock.userToJson(userDTOWithoutPass))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

    }

    @Test
    void createUser() {
    }
}