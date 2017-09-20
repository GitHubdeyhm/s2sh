/*package com.strongit.ibmsnet.util;

import java.io.IOException;
import java.lang.management.MemoryUsage;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.strongit.common.constant.dev.ConstDevComn;
import com.strongit.common.constant.system.ConstSysID;
import com.strongit.ibms.util.CacheUtil;
import com.strongmvc.util.StringUtil;

*//**  
 * 此类实现通过jmx监控tomcat中间件，获取tomcat相关信息
 * @Auther huangXiaoLin.huangxl@strongit.com.cn
 * 2015-12-7 上午09:22:33 
 *//*
public class TomcatMonitorUtil {
	
	private static final Logger log = Logger.getLogger(TomcatMonitorUtil.class);
    
	*//**
	 * 根据中间件设备编码获取监控的tomcat信息，返回从缓存得到的设备信息
	 * @Auther huangXiaoLin.huangxl@strongit.com.cn
	 * 2015-12-7 下午04:26:37
	 *//*
    public static void putTomcatToCache(String devCode) {
    	JSONObject jsonObj = CacheUtil.getCacheData(devCode);
		if (jsonObj != null) {
			//获取子系统 标示
			String bmsId = jsonObj.getString("bmsId");
			if (!StringUtil.isAllNullOrEmpty(bmsId)) {
				String[] bmsArr = bmsId.split("\\|");
				if (bmsArr.length >= 4) {
					String ip = bmsArr[1].split(":")[1];//ip
					String tomcatPort = bmsArr[2].split(":")[1];//tomcat程序访问端口
					String listenPort = bmsArr[3].split(":")[1];//监控端口
					connectJMX(ip, tomcatPort, listenPort, devCode, jsonObj);
				} else {
					log.error("中间件获取子系统标示错误==="+bmsId);
				}
			}
		}
    }
    
    *//**
     * 建立jmx监控连接，端口超出范围会报错，因此捕获Exception
     * @Auther huangXiaoLin.huangxl@strongit.com.cn
     * 2015-12-7 下午01:55:23
     * @param hostIP 主机IP地址
     * @param tomcatPort tomcat访问程序端口
     * @param listenPort jmx监控tomcat信息端口
     * @param devCode 设备编码
     * @param jsonObj 与设备编码对应的缓存对象数据
     *//*
    private static void connectJMX(String hostIP, String tomcatPort, String listenPort, String devCode, JSONObject jsonObj) {
    	JMXConnector connector = null;
        try {
        	//service:jmx:rmi:///jndi/rmi://192.168.2.106:8999/jmxrmi
        	String jmxURL = "service:jmx:rmi:///jndi/rmi://"+hostIP+":"+listenPort+"/jmxrmi";//tomcat jmx url
            JMXServiceURL serviceURL = new JMXServiceURL(jmxURL);
            Map<String, String[]> map = new HashMap<String, String[]>();
            String[] credentials = new String[] { "monitorRole" , "QED" };
            map.put("jmx.remote.credentials", credentials);
            connector = JMXConnectorFactory.connect(serviceURL, map);
            MBeanServerConnection mbsc = connector.getMBeanServerConnection();
            putMonitorInfo(mbsc, tomcatPort, devCode, jsonObj);
		} catch (Exception e) {
			//不能连接清空缓存
    		jsonObj.put("state"+ConstSysID.SYS_ID_DEV_NET, ConstDevComn.DEV_COMN_STATE_OFFLINE);
    		jsonObj.put("isOnline"+ConstSysID.SYS_ID_DEV_NET, ConstDevComn.DEV_COMN_STATE_OFFLINE);//是否在线，0：不在线，1：在线
    		jsonObj.put("heapUsable", "0");
    		jsonObj.put("heapUsed", "0");
    		jsonObj.put("connectUsed", "0");
    		jsonObj.put("connectUsable", "0");//可用连接数，未知，暂时设为1
    		jsonObj.put("threadsUsed", "0");
    		jsonObj.put("threadsUsable", "0");
    		CacheUtil.putCacheData(devCode, jsonObj.toJSONString());//重新放入缓存
			//log.error(e.getMessage());
		} finally {
			if (connector != null) {
				try {
					connector.close();
				} catch (IOException e) {
					log.error(e);
				}
			}
		}
    }
    
    *//**
     * 获取tomcat监控信息
     * @Auther huangXiaoLin.huangxl@strongit.com.cn
     * 2015-12-7 下午04:28:10
     * @param mbsc jmx连接对象
     * @param tomcatPort tomcat访问程序端口
     * @param devCode 设备编码
     * @param JsonObj 与设备编码对应的缓存设备数据
     *//*
    private static void putMonitorInfo(MBeanServerConnection mbsc, String tomcatPort, String devCode, JSONObject jsonObj) {
    	if (mbsc != null) {
			try {
				jsonObj.put("jdkVersion", System.getProperty("java.version"));//jdk版本号
				
				ObjectName tomcatVerObjName = new ObjectName("Catalina:type=Server");
				String tomcatVer = (String)mbsc.getAttribute(tomcatVerObjName, "serverInfo");
				jsonObj.put("middleVersion", tomcatVer);//tomcat版本号
				
				jsonObj.put("state"+ConstSysID.SYS_ID_DEV_NET, ConstDevComn.DEV_COMN_STATE_ONLINE);//设备状态设为正常
				jsonObj.put("isOnline"+ConstSysID.SYS_ID_DEV_NET, ConstDevComn.DEV_COMN_STATE_ONLINE);//是否在线，0：不在线，1：在线
				
				//堆内存
				ObjectName heapObjName = new ObjectName("java.lang:type=Memory");
		        MemoryUsage heapMemoryUsage = MemoryUsage.from((CompositeDataSupport)mbsc.getAttribute(heapObjName, "HeapMemoryUsage"));
		        long maxMemory = heapMemoryUsage.getMax();//堆最大值，单位为字节B
		       // long commitMemory = heapMemoryUsage.getCommitted();//堆当前分配，单位为字节B
		        long usedMemory = heapMemoryUsage.getUsed();//已使用的内存量，单位为字节B
		        //(double)usedMemory*100/commitMemory+"%"//堆使用率
		        //System.out.println("堆最大值："+maxMemory+"===堆当前分配:"+commitMemory+"==已使用："+usedMemory);
		        
		        jsonObj.put("heapUsable", String.valueOf(maxMemory-usedMemory));//空闲堆内存量，单位为字节B
		        jsonObj.put("heapUsed", String.valueOf(usedMemory));//已使用的内存量，单位为字节B
		        //String devName = jsonObj.getString("devName020");
		        
		       //会话数
	           ObjectName managerObjName = new ObjectName("Catalina:type=Manager,*");
	           Set<ObjectName> sessionSet = mbsc.queryNames(managerObjName, null);
	           int activeSessions = 0;//活动会话数
	           for (ObjectName obj : sessionSet) {
	        	   ObjectName objname = new ObjectName(obj.getCanonicalName());
	        	   Integer session = (Integer)mbsc.getAttribute(objname, "activeSessions");//每个应用的活动会话数
	        	   if (session != null) {
	        		   activeSessions = activeSessions + session;
	        	   }
	           }   
//	           Integer maxActiveSessions = (Integer)mbsc.getAttribute(objname, "maxActiveSessions");//最大会话数
        	   jsonObj.put("connectUsed", String.valueOf(activeSessions));
        	   jsonObj.put("connectUsable", "0");//可用连接数，未知，暂时设为1
        	   
	           //线程池
	           ObjectName threadpoolObjName = new ObjectName("Catalina:type=ThreadPool,*");
	           Set<ObjectName> threadSet = mbsc.queryNames(threadpoolObjName, null);
	           for (ObjectName obj : threadSet) {
	        	   String canName = obj.getCanonicalName();
	        	   //端口应该是四位数及以上。包括tomcat的访问端口：tomcat6、tomcat7
	        	   if ((tomcatPort.length() >= 4) && canName.contains(tomcatPort)) {
	        		   ObjectName objname = new ObjectName(canName);
	        		   Integer maxThreads = (Integer)mbsc.getAttribute(objname, "maxThreads");//最大线程数
	            	   Integer currentThreadCount = (Integer)mbsc.getAttribute(objname, "currentThreadCount");//当前线程数
	    	           jsonObj.put("threadsUsed", String.valueOf(currentThreadCount));
	    	           jsonObj.put("threadsUsable", String.valueOf(maxThreads - currentThreadCount));
	    	           break;
	    	           //System.out.println("当前线程数："+jsonObj.getString("threadsUsed")+"==可用线程数："+jsonObj.getString("threadsUsable"));
	        	   }
	           }
			} catch (Exception e) {
				log.error(e);
			}
    	}
    	CacheUtil.putCacheData(devCode, jsonObj.toJSONString());//重新放入缓存
    }
}
*/