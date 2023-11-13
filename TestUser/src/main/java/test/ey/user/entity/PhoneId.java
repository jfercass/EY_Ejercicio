package test.ey.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PhoneId implements Serializable {
    private Long usrId;
    private String phoNumber;

}
