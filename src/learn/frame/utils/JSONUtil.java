package learn.frame.utils;

import java.util.Collection;
import java.util.Date;

import learn.frame.entity.uums.RoleEntity;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import org.apache.log4j.Logger;

/**
 * 此类实现json格式与java对象的互相转换工具类，使用的jar包是json-lib.jar
 * @Date 2016-1-10 下午2:38:08
 * @author huangxl
 */
public class JSONUtil {
	
	private static final Logger log = Logger.getLogger(JSONUtil.class);
	
	/**
	 * 将对象转换为json格式的字符串，对象里不包含Date对象
	 * @Date 2016-1-10下午2:40:51
	 * @param obj 对象
	 * @return json串，如果obj为null则返回null
	 */
	public static String toJSONStr(Object obj) {
		return toJSONStr(obj, false);
	}
	
	/**
	 * 将对象转换成json格式的字符串
	 * @Date 2016-8-13下午4:09:07
	 * @param obj 对象
	 * @param isParseDate 是否解析日期，如果为true代表解析Date对象（日期格式为yy-mm-dd HH:mm:ss），或者不解析Date对象
	 * @return json串，如果obj为null则返回null
	 */
	public static String toJSONStr(Object obj, boolean isParseDate) {
		String jsonStr = null;
		//解析日期
		if (isParseDate) {
			jsonStr = parseDateToJSONStr(obj);
		} else {
			try {
				//是list或set集合
				if (obj instanceof Collection) {
					jsonStr = JSONArray.fromObject(obj).toString();
				} else {
					jsonStr = JSONObject.fromObject(obj).toString();//对于null对象会返回null
				}
			} catch (JSONException e) {
				log.error(e);
			}
		}
		return jsonStr;
	}
	
	
	
	/**
	 * 解析java对象为json字符串，该对象里包含Date对象，解析后的日期格式为yy-mm-dd HH:mm:ss
	 * @Date 2016-1-30下午11:30:56
	 * @param obj 对象
	 * @return json字符串，如果obj为null则返回null
	 */
	private static String parseDateToJSONStr(Object obj) {
		String jsonStr = null;
		try {
			JsonConfig jsonCon = new JsonConfig();
//			jsonCon.setExcludes(new String[]{"createTime", "relvo"});//设置不需要转换成json的字段
			//格式化日期类型
			jsonCon.registerJsonValueProcessor(Date.class, new JsonValueProcessor() {
				/**
				 * @param str 字段名称
				 * @param obj 日期对象
				 * @param jc JsonConfig对象
				 */
				@Override
				public Object processObjectValue(String str, Object obj, JsonConfig jc) {
					if (obj == null) {
						return null;
					}
					if (obj instanceof Date) {
						return DateUtil.formateTime((Date)obj);
					}
					return obj.toString();
				}
				@Override
				public Object processArrayValue(Object obj, JsonConfig jc) {
					if (obj == null) {
						return null;
					}
					if (obj instanceof Date) {
						return DateUtil.formateTime((Date)obj);
					}
					return obj.toString();
				}
			});
			//是list或set集合
			if (obj instanceof Collection) {
				jsonStr = JSONArray.fromObject(obj, jsonCon).toString();
			} else {
				jsonStr = JSONObject.fromObject(obj, jsonCon).toString();
			}
		} catch (JSONException e) {
			log.error(e);
		}
		return jsonStr;
	}
	
	/**
	 * 实现解析json字符串为JSONObject对象，如果不能解析该字符串为json串则返回null
	 * <p>使用json-lib-jdk.jar包(net.sf.json)解析json字符串</p>
	 * <p>在获取值时使用getXXX()方法如果指定的键不存在会抛出JSONException异常，
	 * 可以使用该类提供的optXXX()方法，该方法 不存在指定的键时不会抛出异常，会返回默认
	 * 值，甚至也可自己定义默认值。 对于字符串类型默认返回空字符串，对于int型和long型会返回0</p>
	 * @Date 2016-1-30下午11:23:56
	 * @param jsonStr json格式的字符串
	 * @return JSONObject对象
	 */
	public static JSONObject parseJSON(String jsonStr) {
		JSONObject jsonObj = null;
		if (!StringUtil.isNullOrEmpty(jsonStr)) {
			try {
				jsonObj = JSONObject.fromObject(jsonStr);
			} catch (JSONException e) {
				log.error(e);
			}
		}
		return jsonObj;
	}
	
	/**
	 * 实现解析json字符串为JSONObject对象，如果不能解析该字符串为json则为null
	 * <p>使用org.json包解析json字符串</p>
	 * <p>在获取值时使用getXXX()方法如果指定的键不存在会抛出JSONException异常，
	 * 可以使用该类提供的optXXX()方法，该方法 不存在指定的键时不会抛出异常，会返回默认
	 * 值，甚至也可自己定义默认值。 对于字符串类型默认返回空字符串，对于int型和long型会返回0</p>
	 * @Date 2016-5-6下午11:41:03
	 * @return JSONObject对象
	 * @return
	 */
	public static org.json.JSONObject parseJSONByOrg(String jsonStr) {
		org.json.JSONObject jsonObj = null;
		if (!StringUtil.isNullOrEmpty(jsonStr)) {
			try {
				jsonObj = new org.json.JSONObject(jsonStr);
			} catch (org.json.JSONException e) {
				log.error(e);
			}
		}
		return jsonObj;
	}
	
	
	public static void main(String[] args) {
		RoleEntity role = new RoleEntity();
		role.setCreateTime(new Date());
		role.setId("abcid");
//		role.setRoleLevel(2);
		
		role =null;
		System.out.println(toJSONStr(null));
		
		String jsonStr = "{\"roleNote\":\"\",\"createTime\":\"2016-05-07 09:58:57\",\"createUser\":\"米老鼠\"}";
	
		JSONObject jsonObj = parseJSON(jsonStr);
		System.out.println(jsonObj.optString("roleLevel") == "");
	}
}
