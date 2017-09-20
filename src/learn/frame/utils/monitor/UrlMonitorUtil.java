package learn.frame.utils.monitor;

import java.net.HttpURLConnection;
import java.net.URL;

import learn.frame.utils.StringUtil;

import org.apache.log4j.Logger;

/** 
 * 监测软件应用模块，监测url的状态
 * @auther huangXiaoLin.huangxl@strongit.com.cn
 * 2016-3-25 上午10:56:43
 */
public class UrlMonitorUtil {
	
	private static URL url;  
	private static HttpURLConnection conn;  
	private static int state = -1;
	
	/**
	 * 将监测到的软件应用状态放入缓存
	 * @auther huangXiaoLin.huangxl@strongit.com.cn
	 * 2016-3-28 上午11:12:49
	 */
//	public static void putUrlToCache(String devCode) {
//		JSONObject jsonObj = CacheUtil.getCacheData(devCode);
//		if (jsonObj != null) {
//			boolean isBmsIdOk = false;//标示软件应用的子系统标示是否正确
//			//获取子系统 标示
//			String bmsId = jsonObj.getString("bmsId");
//			if (!StringUtil.isAllNullOrEmpty(bmsId)) {
//				String[] bmsArr = bmsId.split("\\|");
//				if (bmsArr.length >= 2) {
//					String urlStr = bmsArr[1];//url
//					if (bmsArr[1].length() > 4) {
//						isBmsIdOk = true;
//						urlStr = urlStr.substring(4, urlStr.length());
//						boolean flag = isConnect(urlStr);
//						if (flag) {
//							jsonObj.put("state"+ConstSysID.SYS_ID_DEV_NET, ConstDevComn.DEV_COMN_STATE_ONLINE);
//							jsonObj.put("isOnline"+ConstSysID.SYS_ID_DEV_NET, ConstDevComn.DEV_COMN_STATE_ONLINE);//是否在线，0：不在线，1：在线
//						} else {
//							jsonObj.put("state"+ConstSysID.SYS_ID_DEV_NET, ConstDevComn.DEV_COMN_STATE_OFFLINE);
//							jsonObj.put("isOnline"+ConstSysID.SYS_ID_DEV_NET, ConstDevComn.DEV_COMN_STATE_OFFLINE);//是否在线，0：不在线，1：在线
//						}
//						CacheUtil.putCacheData(devCode, jsonObj.toJSONString());
//					}
//				}
//			}
//			if (!isBmsIdOk) {
//				log.error("软件应用子系统标示错误==="+bmsId);
//			}
//		}
//	}
	
	/**
	 * 监测一个url地址是否能够访问
	 * @auther huangXiaoLin.huangxl@strongit.com.cn
	 * 2016-3-25 上午11:01:40
	 */
	public static synchronized boolean isConnect(String urlStr) {
		//如果传入的url标示为空，则认为不能监测
		if (StringUtil.isNullOrEmpty(urlStr)) {                         
	   		return false;                   
		}  
		boolean flag = false;//是否能够访问该url标示
		int count = 0; 
		while (count < 5) {
			try {
				url = new URL(urlStr);
				conn = (HttpURLConnection)url.openConnection();
				
				//conn.setConnectTimeout(5000);
				//System.out.println("连接超时时间--"+conn.getConnectTimeout());
				
				state = conn.getResponseCode(); 
				//状态码返回200代表连接成功
				if (state == HttpURLConnection.HTTP_OK) {
					flag = true;
				}  
				break;  
			} catch (Exception e) {
				count++;   
				urlStr = null;  
			}
		}  
		return flag;  
	} 
	
	public static void main(String[] args) {
		UrlMonitorUtil.isConnect("http://www.dangdang.com/");
	}
}
