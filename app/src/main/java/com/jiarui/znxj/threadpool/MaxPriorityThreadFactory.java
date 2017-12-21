package com.jiarui.znxj.threadpool;

import java.util.concurrent.ThreadFactory;

public class MaxPriorityThreadFactory implements ThreadFactory
{
	@Override
	public Thread newThread(Runnable r)
	{
		// TODO Auto-generated method stub
		Thread t = new Thread(r);
		t.setPriority(Thread.MAX_PRIORITY);
		return t;
	}
}
