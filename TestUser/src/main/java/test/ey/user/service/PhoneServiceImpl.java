package test.ey.user.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import test.ey.user.dto.PhoneDTO;
import test.ey.user.entity.Phone;
import test.ey.user.entity.User;
import test.ey.user.repository.PhoneRepository;
import test.ey.user.repository.UserRepository;
import test.ey.user.security.UserRequestInfo;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
public class PhoneServiceImpl implements PhoneService{

    @Autowired
    private PhoneRepository phoneRepository;
    @Autowired
    private UserRepository userRepository;


    @Override
    public List<PhoneDTO> getList(Long id) {
        List<Phone> phones = phoneRepository.findAllByUsrId(id);
        return phones.stream().map(phone -> new PhoneDTO(phone.getPhoNumber(), phone.getPhoCityCode(), phone.getPhoCountryCode())).collect(Collectors.toList());
    }

    @Override
    public PhoneDTO add(Long usrId, PhoneDTO phoneDTO) {
        Optional<User> user = userRepository.findById(usrId);
        Phone phone = new Phone();
      /*  User user = new User();
        user.setUsrId(usrId);

        phone.setUser(user);*/
        phone.setUsrId(usrId);
        phone.setPhoNumber(phoneDTO.getPhoNumber());
        phone.setPhoCityCode(phoneDTO.getPhoCityCode());
        phone.setPhoCountryCode(phoneDTO.getPhoCountryCode());
        phone = phoneRepository.saveAndFlush(phone);
        return new PhoneDTO(phone.getPhoNumber(), phone.getPhoCityCode(), phone.getPhoCountryCode());
    }
}
