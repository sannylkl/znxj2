package com.jiarui.znxj.threadpool;
import java.util.concurrent.ThreadFactory;

public class NormalPriorityThreadFactory implements ThreadFactory
{
	@Override
	public Thread newThread(Runnable r)
	{
		// TODO Auto-generated method stub
		Thread t = new Thread(r);
		t.setPriority(Thread.NORM_PRIORITY);
		return t;
	}
}
