package learn.frame.entity.attendman;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 周值班表
 * 2015-11-21 下午10:30:28
 * @author huangxl
 */
@Entity
@Table(name="t_duty")
public class DutyEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String dutyId;
	private String leader;
	private String cadres;
	private String guard;
	private String dutyTime;
	
	@Id
	@GeneratedValue(generator="idGenerator")
	@GenericGenerator(name="idGenerator", strategy="uuid")
	@Column(name="duty_id", nullable=false, unique=true, length=32)
	public String getDutyId() {
		return dutyId;
	}
	public void setDutyId(String dutyId) {
		this.dutyId = dutyId;
	}
	
	@Column(name="leader", nullable=false, length=40)
	public String getLeader() {
		return leader;
	}
	public void setLeader(String leader) {
		this.leader = leader;
	}
	
	@Column(name="cadres", nullable=false)
	public String getCadres() {
		return cadres;
	}
	public void setCadres(String cadres) {
		this.cadres = cadres;
	}
	
	@Column(name="guard", nullable=false)
	public String getGuard() {
		return guard;
	}
	public void setGuard(String guard) {
		this.guard = guard;
	}
	
	@Column(name="duty_time")
	public String getDutyTime() {
		return dutyTime;
	}
	public void setDutyTime(String dutyTime) {
		this.dutyTime = dutyTime;
	}
}
