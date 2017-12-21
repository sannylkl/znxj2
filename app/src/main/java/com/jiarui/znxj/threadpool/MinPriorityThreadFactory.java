package com.jiarui.znxj.threadpool;
import java.util.concurrent.ThreadFactory;

public class MinPriorityThreadFactory implements ThreadFactory
{
	@Override
	public Thread newThread(Runnable r)
	{
		// TODO Auto-generated method stub
		Thread t = new Thread(r);
		t.setPriority(Thread.MIN_PRIORITY);
		return t;
	}
}
