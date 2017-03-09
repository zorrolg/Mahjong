package com.citywar.util;

import java.util.LinkedHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SafeLinkedHashMap<K, V> extends LinkedHashMap<K, V> {

	private static final long serialVersionUID = 1L;
	
	private final Lock lock = new ReentrantLock(); 
	
	@Override
	public V get(Object key) {
		try
		{
			lock.lock();
			return super.get(key);   
		}
		finally
		{
			lock.unlock();
		}
	}
	
	@Override
	public V put(K arg0, V arg1) {
		try
		{
			lock.lock();
			return super.put(arg0, arg1);
		}
		finally
		{
			lock.unlock();
		}
	}
	
	@Override
	public V remove(Object paramObject) {
		try
		{
			lock.lock();
			return super.remove(paramObject);
		}
		finally
		{
			lock.unlock();
		}
	}
}
