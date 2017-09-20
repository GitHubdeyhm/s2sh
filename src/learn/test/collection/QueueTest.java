package learn.test.collection;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * 队列测试
 * @Date 2017-4-29上午12:02:59
 */
public class QueueTest {

	public static void main(String[] args) {
		QueueTest test = new QueueTest();
		Integer empty = null;
		Queue<Integer> qu = new PriorityQueue<>();
		//qu.offer(empty);//NullPointerException
		qu.offer(6);
		qu.offer(-3);
		qu.offer(0);
		qu.offer(9);
		System.out.println(qu.size());
		System.out.println("PriorityQueue-->"+qu);//toString()方法不会升序输入队列的元素
		int size = qu.size();
		for (int i = 0; i < size; i++) {
			System.out.print(qu.poll()+"@@@@@@@");
		}
		System.out.println("\n"+qu.size());
		
		
		test.stack();
		
		Deque<Integer> deq = new LinkedList<>();
		deq.push(empty);
		System.out.println("LinkedList-->"+deq);
		
		BlockingQueue<String> bq = new PriorityBlockingQueue<>();
		BlockingQueue<String> abq = new ArrayBlockingQueue<>(2);
		abq.offer("a");
		abq.offer("b");
		abq.offer("c");
	}
	
	//模拟栈数据结构
	public void stack() {
		Deque<Integer> stack = new ArrayDeque<>();
		stack.push(1);
		stack.push(100);
		stack.push(40);
		System.out.println(stack);
		int size = stack.size();
		for (int i = 0; i < size; i++) {
			System.out.println("出栈："+stack.pop());
		}
	}
}
