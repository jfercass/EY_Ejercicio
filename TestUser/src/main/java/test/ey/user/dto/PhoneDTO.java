package test.ey.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
public class PhoneDTO {
    @NotBlank
    private String phoNumber;
    @NotBlank
    private String phoCityCode;
    @NotBlank
    private String phoCountryCode;
}
