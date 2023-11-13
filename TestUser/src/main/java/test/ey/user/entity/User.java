package test.ey.user.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "USERS", uniqueConstraints = {@UniqueConstraint(name = "unique_name_password", columnNames = {"USR_NAME", "USR_PASSWORD"})})
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_gen")
    @SequenceGenerator(name = "user_gen", sequenceName = "USER_SEQ", allocationSize = 1)
    @Column(name = "USR_ID")
    private Long usrId;
    @Column(name = "USR_NAME")
    private String usrName;
    @Column(name = "USR_EMAIL", unique = true)
    private String usrEmail;
    @Column(name = "USR_PASSWORD")
    private String usrPassword;
    @Column(name = "USR_ROLE")
    private String usrRole;
    @OneToMany(mappedBy = "user")
    private List<Phone> phones;
    @CreationTimestamp
    private LocalDateTime creation;
    @UpdateTimestamp
    private LocalDateTime  modified;
    private LocalDateTime  lastLogin;
    private String token;
    private Boolean isActive = true;
}
