package com.session.common;

import java.io.Serializable;

import android.util.Log;

import com.google.gson.Gson;

public class BaseResponseData implements Serializable
{

	private static final long serialVersionUID = 1L;
	
	/**
	 * 子类重写该方法，实现将json字符中key-value映射到成员变量中，并添加@Override注解
	 * @author YJ Liang
	 * 2016  上午10:32:07
	 * @param gson
	 * @param jsonStr
	 */
	public void parseData(String jsonStr)throws Exception
	{
	}
}
