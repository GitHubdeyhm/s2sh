package learn.frame.service.attendman;

import java.util.List;

import learn.frame.common.base.IBaseService;
import learn.frame.entity.attendman.DutyEntity;

public interface IDutyService extends IBaseService<DutyEntity> {
	
	public List<DutyEntity> findByWeeks(String weeks);

}
