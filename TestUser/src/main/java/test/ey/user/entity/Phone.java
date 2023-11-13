package test.ey.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "PHONE")
@Getter
@Setter
public class Phone {

   @Id
   @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "phone_gen")
   @SequenceGenerator(name = "phone_gen", sequenceName = "PHONE_SEQ", allocationSize = 1)
   @Column(name = "PHO_ID")
   private Long phoId;
    @Column(name = "USR_ID")
    private Long usrId;
    @Column(name = "PHO_NUMBER")
    private String phoNumber;
    @Column(name = "PHO_CITY_CODE")
    private String phoCityCode;
    @Column(name = "PHO_COUNTRY_CODE")
    private String phoCountryCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USR_ID", nullable = false, insertable = false, updatable = false, referencedColumnName = "USR_ID")
    private User user;

}
