package com.session.dgjx.response;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.session.common.BaseListResponseData;
import com.session.common.utils.DateUtil;
import com.session.common.utils.GsonUtil;
import com.session.dgjx.enity.Car;
import com.session.dgjx.enity.Course;
import com.session.dgjx.enity.EndFlag;
import com.session.dgjx.enity.Order;
import com.session.dgjx.enity.Student;
import com.session.dgjx.enity.Trainer;
import com.session.dgjx.enity.Yard;

public class OrderListResponseData extends BaseListResponseData
{
	private static final long serialVersionUID = 1L;
	private List<Order> list;
	@Override
	public void parseData(String jsonStr)throws Exception
	{
		JsonParser parser = new JsonParser();
		JsonObject dataObj = (JsonObject)parser.parse(jsonStr);
		setEndFlag(GsonUtil.getInt(dataObj, ENDFLAG, EndFlag.Flag_1.getValue()));
		JsonArray array = dataObj.getAsJsonArray(LIST);
		list = new ArrayList<Order>();
		if(array != null)
		{
			for(int i=0; i<array.size(); i++)
			{
				JsonObject obj = (JsonObject)array.get(i);
				Order order = new Order();
				String courseCode = GsonUtil.getString(obj, "course");
				for(Course course : Course.values())
				{
					if(course.getCode().equals(courseCode))
					{
						order.setCourse(course);
						break;
					}
				}
				//如果课程为空，可能是后台增加了新的课程或课程信息修改了，需要升级app
				if(order.getCourse() == null)
				{
					continue;
				}
				order.setId(GsonUtil.getString(obj, "id"));
				order.setStatus(GsonUtil.getString(obj, "status"));
				order.setStatusName(GsonUtil.getString(obj, "statusName"));
				order.setNextOperate(GsonUtil.getString(obj, "nextOperate"));
				order.setNextOperateName(GsonUtil.getString(obj, "nextOperateName"));
				String carno = GsonUtil.getString(obj, "carno");
				Car car = new Car();
				car.setCarno(carno);
				order.setCar(car);
				order.setRemark(GsonUtil.getString(obj, "remark"));
				order.setOrderDuration(GsonUtil.getInt(obj, "orderDuration"));
				order.setIsEval(GsonUtil.getString(obj, "isEval"));
				order.setBeginTime(GsonUtil.getDate(obj, "beginTime", DateUtil.NETWORK_TIME_SDF));
				order.setEndTime(GsonUtil.getDate(obj, "endTime", DateUtil.NETWORK_TIME_SDF));
				order.setSubmitTime(GsonUtil.getDate(obj, "submitTime", DateUtil.NETWORK_TIME_SDF));
				Student student = new Student();
				student.setAccount(GsonUtil.getString(obj, "studentAccount"));
				student.setName(GsonUtil.getString(obj, "studentName"));
				String progress = GsonUtil.getString(obj, "progress");
				student.setProgress(progress);
				student.setApplyTime(GsonUtil.getDate(obj, "applyTime", DateUtil.NETWORK_TIME_SDF));
				student.setPhotoUrl(GsonUtil.getString(obj, "photoUrl"));
				order.setStudent(student);
				Trainer trainer = new Trainer();
				trainer.setAccount(GsonUtil.getString(obj, "trainerAccount"));
				trainer.setName(GsonUtil.getString(obj, "trainerName"));
				trainer.setPhone(GsonUtil.getString(obj, "phone"));
				order.setTrainer(trainer);
				Yard yard = new Yard();
				yard.setId(GsonUtil.getLong(obj, "yardId"));
				yard.setName(GsonUtil.getString(obj, "yardName"));
				order.setYard(yard);
				list.add(order);
			}
		}
	}
	public List<Order> getList()
	{
		return list;
	}
	
}
