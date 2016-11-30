package com.session.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class GsonUtil
{
	public static String getString(JsonObject jsonObject, String key)
	{
		JsonElement obj = jsonObject.get(key);
		return obj != null ? obj.getAsString() : null;
	}
	
	public static boolean getBoolean(JsonObject jsonObject, String key, boolean defaultValue)
	{
		JsonPrimitive primitive = jsonObject.getAsJsonPrimitive(key);
		return primitive != null ? primitive.getAsBoolean() : defaultValue;
	}
	public static float getFloat(JsonObject jsonObject, String key, float defaultValue)
	{
		JsonPrimitive primitive = jsonObject.getAsJsonPrimitive(key);
		return primitive != null ? primitive.getAsFloat() : defaultValue;
	}
	public static float getFloat(JsonObject jsonObject, String key)
	{
		return getFloat(jsonObject, key, 0f);
	}
	
	public static double getDouble(JsonObject jsonObject, String key, double defaultValue)
	{
		JsonPrimitive primitive = jsonObject.getAsJsonPrimitive(key);
		return primitive != null ? primitive.getAsDouble() : defaultValue;
	}
	public static double getDouble(JsonObject jsonObject, String key)
	{
		return getDouble(jsonObject, key, 0);
	}
	
	public static int getInt(JsonObject jsonObject, String key, int defaultValue)
	{
		JsonPrimitive primitive = jsonObject.getAsJsonPrimitive(key);
		return primitive != null ? primitive.getAsInt() : defaultValue;
	}
	public static int getInt(JsonObject jsonObject, String key)
	{
		return getInt(jsonObject, key,0);
	}
	public static long getLong(JsonObject jsonObject, String key, long defaultVaule)
	{
		JsonPrimitive primitive = jsonObject.getAsJsonPrimitive(key);
		return primitive != null ? primitive.getAsLong() : defaultVaule;
	}
	public static long getLong(JsonObject jsonObject, String key)
	{
		return getLong(jsonObject, key, 0L);
	}
	public static char getChar(JsonObject jsonObject, String key, char defaultVaule)
	{
		JsonPrimitive primitive = jsonObject.getAsJsonPrimitive(key);
		return primitive != null ? primitive.getAsCharacter() : defaultVaule;
	}
	public static char getChar(JsonObject jsonObject, String key)
	{
		return getChar(jsonObject, key, '\0');
	}
	
	public static Date getDate(JsonObject jsonObject,String key, SimpleDateFormat sdf) throws ParseException
	{
		String timeStr = getString(jsonObject, key);
		if(timeStr != null)
		{
			return sdf.parse(timeStr);
		}else {
			return null;
		}
	}
	
	
}
