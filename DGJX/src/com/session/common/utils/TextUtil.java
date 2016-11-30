package com.session.common.utils;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class TextUtil
{
	/**
	 * 判断字符串时候为null或""，改方法会将字符串两边的空格去掉
	 * @author YJ Liang
	 * 2016  上午11:17:19
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str)
	{
		if(str != null && !(str=str.trim()).isEmpty())
		{
			return false;
		}else {
			return true;
		}
	}
	
	/**
	 * 将集合转化为以逗号分隔的字符串，每个元素的值为null或item.toString()的值
	 * @author YJ Liang
	 * 2016  上午11:16:46
	 * @param list
	 * @return
	 */
	public static String listToString(List<?> list)
	{
		if(list == null)
		{
			return null;
		}else {
			String str = "";
			int size = list.size();
			for(int i=0; i<size; i++)
			{
				Object item = list.get(i);
				if(i!=size-1)
				{
					str += (item != null ? item.toString() : "null") + ",";
				}else{
					str += (item != null ? item.toString() : "null");
				}
			}
			return str;
		}
	}
	
	/**
	 * 将以逗号分隔的字符串转化为对应的数据组
	 * @author YJ Liang
	 * 2016  上午11:15:56
	 * @param str
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> stringToList(String str, Class<T> clazz)throws Exception
	{
		List<T> list = new ArrayList<T>();
		if(!isEmpty(str))
		{
			String arr[] = str.split(",");
			for(int i=0; i<arr.length; i++)
			{
				Constructor<T> constructor = clazz.getConstructor(String.class);
				list.add(constructor.newInstance(arr[i]));
			}
		}
		return list;
			
	}
}
