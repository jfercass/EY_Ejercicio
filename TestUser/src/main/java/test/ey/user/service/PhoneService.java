package test.ey.user.service;

import test.ey.user.dto.PhoneDTO;
import test.ey.user.entity.Phone;

import java.util.List;

public interface PhoneService {

    public List<PhoneDTO> getList(Long id);

    public PhoneDTO add(Long usrId, PhoneDTO phoneDTO);
}
