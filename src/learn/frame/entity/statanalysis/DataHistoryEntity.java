package learn.frame.entity.statanalysis;

import java.io.Serializable;

/** 
 * 设备数据历史
 * @auther huangxl
 * @Date 2016-1-20 下午02:58:47
 */
public class DataHistoryEntity implements Serializable, Comparable<DataHistoryEntity> {
	
	private static final long serialVersionUID = -8229895857039736580L;
	
	private String historyId;
	//设备ID
	private String deviceId;
	//总流量
	private String totalFlow;
	//入总流量
	private String inTotalFlow;
	//出总流量
	private String outTotalFlow;
	//cpu使用率
	private String cpuUse;
	//物理内存可用率
	private String freeRam;
	//数据创建时间
	private String createTime;
	//设备类型
	private Integer deviceType;
	//用于清理临时队列的时间标志
	private long timeFlag;
	
	
	public String getHistoryId() {
		return historyId;
	}
	public void setHistoryId(String historyId) {
		this.historyId = historyId;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getTotalFlow() {
		return totalFlow;
	}
	public void setTotalFlow(String totalFlow) {
		this.totalFlow = totalFlow;
	}
	public String getInTotalFlow() {
		return inTotalFlow;
	}
	public void setInTotalFlow(String inTotalFlow) {
		this.inTotalFlow = inTotalFlow;
	}
	public String getOutTotalFlow() {
		return outTotalFlow;
	}
	public void setOutTotalFlow(String outTotalFlow) {
		this.outTotalFlow = outTotalFlow;
	}
	public String getCpuUse() {
		return cpuUse;
	}
	public void setCpuUse(String cpuUse) {
		this.cpuUse = cpuUse;
	}
	public String getFreeRam() {
		return freeRam;
	}
	public void setFreeRam(String freeRam) {
		this.freeRam = freeRam;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public Integer getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(Integer deviceType) {
		this.deviceType = deviceType;
	}
	
	public long getTimeFlag() {
		return timeFlag;
	}
	public void setTimeFlag(long timeFlag) {
		this.timeFlag = timeFlag;
	}
	
	@Override
	public int compareTo(DataHistoryEntity o) {
		return 0;
	}
}
