package com.session.dgjp.response;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.session.common.BaseListResponseData;
import com.session.common.utils.DateUtil;
import com.session.common.utils.GsonUtil;
import com.session.common.utils.TextUtil;
import com.session.dgjp.enity.EndFlag;
import com.session.dgjp.enity.Teaching;
import com.session.dgjp.enity.TeachingSchedule;

public class TeachingScheduleResponseData extends BaseListResponseData
{
	private static final long serialVersionUID = 1L;
	
	private List<TeachingSchedule> list;

	public List<TeachingSchedule> getList()
	{
		return list;
	}

	public void setList(List<TeachingSchedule> list)
	{
		this.list = list;
	}

	@Override
	public void parseData(String jsonStr) throws Exception
	{
		super.parseData(jsonStr);
		JsonParser parser = new JsonParser();
		JsonObject dataObj = (JsonObject)parser.parse(jsonStr);
		setEndFlag(GsonUtil.getInt(dataObj, ENDFLAG,EndFlag.Flag_1.getValue()));
		JsonArray array = dataObj.getAsJsonArray(LIST);
		list = new ArrayList<TeachingSchedule>();
		if(array != null)
		{
			for(int i=0; i<array.size(); i++)
			{
				JsonObject obj = (JsonObject)array.get(i);
				TeachingSchedule teachingSchedule = new TeachingSchedule();
				teachingSchedule.setCourse(GsonUtil.getString(obj, "course"));
				JsonArray teachingJsonArray = obj.getAsJsonArray("teachingList");
				if(teachingJsonArray != null)
				{
					List<Teaching> teachingList = new ArrayList<Teaching>();
					for(int j=0; j<teachingJsonArray.size(); j++)
					{
						JsonObject teachingObj = (JsonObject)teachingJsonArray.get(j);
						Teaching teaching = new Teaching();
						teaching.setTeachingTime(GsonUtil.getDate(teachingObj, "teachingTime", DateUtil.NETWORK_DATE_SDF));
						String timeSlotStr = GsonUtil.getString(teachingObj, "timeSlot");
						teaching.setTimeSlot(TextUtil.stringToList(timeSlotStr, Integer.class));
						teachingList.add(teaching);
					}
					teachingSchedule.setTeaching(teachingList);
				}
				list.add(teachingSchedule);
			}
		}
	}
}
