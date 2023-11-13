package test.ey.user.security;

import io.jsonwebtoken.Claims;
import lombok.Getter;
import lombok.Setter;
import test.ey.user.entity.User;

import java.util.Date;

@Getter
@Setter
public class UserRequestInfo {
    private User user = null;
    private Claims claims = null;
    private String subject = null;
    private Date issuedAt= null;
    private String token = null;
}
