package test.ey.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import test.ey.user.entity.Phone;
import test.ey.user.entity.PhoneId;

import java.util.List;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, Long> {

    public List<Phone> findAllByUsrId(Long usrId);
}
