package learn.frame.monitor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

/**
 *
 * @Date 2015-12-3 下午10:22:06
 * @author huangxl
 */
public class TestJMX {
	public static void main(String[] args) {
		String jmxUrl = "service:jmx:rmi:///jndi/rmi://localhost:8999/jmxrmi";//tomcat jmx url
		
		try {
			JMXServiceURL serviceUrl = new JMXServiceURL(jmxUrl);
			Map<String, String[]> map = new HashMap<String, String[]>();
			String[] credentials = new String[] { "monitorRole" , "QED" };
	        map.put("jmx.remote.credentials", credentials);
	        
	        JMXConnector connector = JMXConnectorFactory.connect(serviceUrl, map);
	        MBeanServerConnection  mbsc = connector.getMBeanServerConnection();
			
	      //端口最好是动态取得
           ObjectName threadObjName = new ObjectName("Catalina:type=ThreadPool,name=http-8888");
           MBeanInfo mbInfo = mbsc.getMBeanInfo(threadObjName);
           String attrName = "currentThreadCount";//tomcat的线程数对应的属性值
           MBeanAttributeInfo[] mbAttributes = mbInfo.getAttributes();
           System.out.println("currentThreadCount:"+mbsc.getAttribute(threadObjName, attrName));
           
         //heap
           for(int j=0;j <mbsc.getDomains().length;j++){ 
               System.out.println("###########"+mbsc.getDomains()[j]); 
           } 
           Set MBeanset = mbsc.queryMBeans(null, null);
           System.out.println("MBeanset.size() : " + MBeanset.size());
           Iterator MBeansetIterator = MBeanset.iterator();
           while (MBeansetIterator.hasNext()) { 
               ObjectInstance objectInstance = (ObjectInstance)MBeansetIterator.next();
               ObjectName objectName = objectInstance.getObjectName();
               String canonicalName = objectName.getCanonicalName();
               System.out.println("canonicalName : " + canonicalName); 
               if (canonicalName.equals("Catalina:host=localhost,type=Cluster"))      {  
                   // Get details of cluster MBeans
                   System.out.println("Cluster MBeans Details:");
                   System.out.println("========================================="); 
                   //getMBeansDetails(canonicalName);
                   String canonicalKeyPropList = objectName.getCanonicalKeyPropertyListString();
              }
           }
           //------------------------- system ----------------------
           ObjectName runtimeObjName = new ObjectName("java.lang:type=Runtime");
           System.out.println("厂商:"+ (String)mbsc.getAttribute(runtimeObjName, "VmVendor"));
           System.out.println("程序:"+ (String)mbsc.getAttribute(runtimeObjName, "VmName"));
           System.out.println("版本:"+ (String)mbsc.getAttribute(runtimeObjName, "VmVersion"));
           Date starttime=new Date((Long)mbsc.getAttribute(runtimeObjName, "StartTime"));
           SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
           System.out.println("启动时间:"+df.format(starttime));
            
           Long timespan=(Long)mbsc.getAttribute(runtimeObjName, "Uptime");
          // System.out.println("连续工作时间:"+JMXTest.formatTimeSpan(timespan));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
