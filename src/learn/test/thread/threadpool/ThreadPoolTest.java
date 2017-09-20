package learn.test.thread.threadpool;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 线程池测试
 * @Date 2017-4-22下午11:06:04
 */
public class ThreadPoolTest {

	public static void main(String[] args) throws Exception {
		//Scanner sc = new Scanner(System.in);
		String content = "F:\\learn";
		System.out.println("输入内容："+content);
		
		//创建一个线程池
		ExecutorService pool = Executors.newCachedThreadPool();
		MatchCounter counter = new MatchCounter(new File(content), "", pool);
		Future<Integer> res = pool.submit(counter);
		System.out.println(res.get());
		pool.shutdown();
		System.out.println("-----------");
	}
}

class MatchCounter implements Callable<Integer> {
	
	//目录
	private File dir;
	private String keyword;
	private ExecutorService pool;
	private int count;
	
	/**
	 * @Date 2017-4-22下午11:20:42
	 * @param dir
	 * @param keyword
	 * @param pool
	 */
	public MatchCounter(File dir, String keyword, ExecutorService pool) {
		this.dir = dir;
		this.keyword = keyword;
		this.pool = pool;
	}

	@Override
	public Integer call() {
		count = 0;
		//获取该目录下的第一级文件和目录，当dir代表一个文件时返回null
		File[] files = dir.listFiles();
		if (files == null) {
			return 0;
		}
		//保存结果的list
		List<Future<Integer>> results = new ArrayList<>();
		for (File f : files) {
			//代表一个目录
			if (f.isDirectory()) {
				MatchCounter counter = new MatchCounter(f, keyword, pool);
				Future<Integer> res = pool.submit(counter);
				results.add(res);
			} else {
				count++;
			}
		}
		for (Future<Integer> res : results) {
			try {
				count += res.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		System.out.println("count="+count);
		return count;
	}
	
}
