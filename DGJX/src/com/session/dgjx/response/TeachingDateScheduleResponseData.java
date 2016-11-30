package com.session.dgjx.response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.session.common.BaseListResponseData;
import com.session.common.utils.DateUtil;
import com.session.common.utils.GsonUtil;
import com.session.dgjx.enity.EndFlag;
import com.session.dgjx.enity.OrderDate;

public class TeachingDateScheduleResponseData extends BaseListResponseData
{
	private static final long serialVersionUID = 1L;
	
	private List<OrderDate> list;

	public List<OrderDate> getList()
	{
		return list;
	}

	public void setList(List<OrderDate> list)
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
		list = new ArrayList<OrderDate>();
		if(array != null)
		{
			for(int i=0; i<array.size(); i++)
			{
				Date date = GsonUtil.getDate((JsonObject)array.get(i), "teachingTime", DateUtil.NETWORK_DATE_SDF);
				list.add(new OrderDate(date));
			}
		}
	}
}
