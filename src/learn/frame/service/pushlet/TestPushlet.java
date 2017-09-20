package learn.frame.service.pushlet;

import nl.justobjects.pushlet.core.Dispatcher;
import nl.justobjects.pushlet.core.Event;


/**
 *
 * @Date 2016-6-11 下午9:39:43
 */
public class TestPushlet extends Thread {
	
	@Override
	public void run() {
		while (true) {
			try {
				System.out.println("===========");
	            Event event = Event.createDataEvent("/pushletEvnet");  
	            //event.setField("name", "pluslet message"); 
	            String message = "pushlet推送信息";
	            	//需对中文进行转码
				event.setField("name", new String(message.getBytes("UTF-8"), "ISO-8859-1"));
				Dispatcher.getInstance().multicast(event);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/* public static class HwPlushlet extends EventPullSource {
        // 休眠五秒  
        @Override  
        protected long getSleepTime() {
            return 2000;  
        }  
        @Override  
        protected Event pullEvent() {
        	System.out.println("===========");
            Event event = Event.createDataEvent("/pushletEvnet");  
            //event.setField("name", "pluslet message"); 
            String message = "pushlet推送信息";
            try {
            	Dispatcher
            	//需对中文进行转码
				event.setField("name", new String(message.getBytes("UTF-8"), "ISO-8859-1"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} 
            return event;  
        }  
    }*/
}
