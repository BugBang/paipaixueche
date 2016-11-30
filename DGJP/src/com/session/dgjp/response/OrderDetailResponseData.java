package com.session.dgjp.response;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.session.common.BaseResponseData;
import com.session.common.utils.DateUtil;
import com.session.common.utils.GsonUtil;
import com.session.dgjp.enity.BranchSchool;
import com.session.dgjp.enity.Car;
import com.session.dgjp.enity.Course;
import com.session.dgjp.enity.Order;
import com.session.dgjp.enity.Student;
import com.session.dgjp.enity.Trainer;
import com.session.dgjp.enity.Yard;
import com.session.dgjp.helper.BranchSchoolHelper;

public class OrderDetailResponseData extends BaseResponseData
{
	private static final long serialVersionUID = 1L;
	private Order order;
	public Order getOrder()
	{
		return order;
	}
	@Override
	public void parseData(String jsonStr) throws Exception
	{
		JsonParser parser = new JsonParser();
		JsonObject obj = (JsonObject)parser.parse(jsonStr);
		order = new Order();
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
			throw new Exception("unknown course");
		}
		order.setId(GsonUtil.getString(obj, "id"));
		order.setStatus(GsonUtil.getString(obj, "status"));
		order.setStatusName(GsonUtil.getString(obj, "statusName"));
		order.setNextOperate(GsonUtil.getString(obj, "nextOperate"));
		order.setNextOperateName(GsonUtil.getString(obj, "nextOperateName"));
		Car car = new Car();
		car.setCarno(GsonUtil.getString(obj, "carno"));
		car.setName(GsonUtil.getString(obj, "carName"));
		car.setGearType(GsonUtil.getString(obj, "gearType"));
		car.setCarType(GsonUtil.getString(obj, "carType"));
		order.setCar(car);
		order.setFee(GsonUtil.getInt(obj, "fee"));
		order.setOriginalFee(GsonUtil.getInt(obj, "originalFee"));
		order.setPreferentialFee(GsonUtil.getInt(obj, "preferentialFee"));
		order.setRemark(GsonUtil.getString(obj, "remark"));
		order.setOrderDuration(GsonUtil.getInt(obj, "orderDuration"));
		order.setIsEval(GsonUtil.getString(obj, "isEval"));
		order.setBeginTime(GsonUtil.getDate(obj, "beginTime", DateUtil.NETWORK_TIME_SDF));
		order.setEndTime(GsonUtil.getDate(obj, "endTime", DateUtil.NETWORK_TIME_SDF));
		order.setSubmitTime(GsonUtil.getDate(obj, "submitTime", DateUtil.NETWORK_TIME_SDF));
		order.setRemainTime(GsonUtil.getInt(obj, "remainTime"));
		Student student = new Student();
		student.setAccount(GsonUtil.getString(obj, "studentAccount"));
		student.setName(GsonUtil.getString(obj, "studentName"));
		student.setPhotoUrl(GsonUtil.getString(obj, "studentPhotoUrl"));
		student.setPhone(GsonUtil.getString(obj, "studentPhone"));
		order.setStudent(student);
		Trainer trainer = new Trainer();
		trainer.setAccount(GsonUtil.getString(obj, "trainerAccount"));
		trainer.setName(GsonUtil.getString(obj, "trainerName"));
		trainer.setPhotoUrl(GsonUtil.getString(obj, "trainerPhotoUrl"));
		trainer.setPhone(GsonUtil.getString(obj, "trainerPhone"));
		float evalScore = GsonUtil.getFloat(obj, "evalScore");
		int evalTimes = GsonUtil.getInt(obj, "evalTimes");
		if(evalTimes==0)
		{
			trainer.setEval(0);
		}else{
			trainer.setEval(evalScore/evalTimes);
		}
		trainer.setOrderTimes(GsonUtil.getInt(obj, "orderTimes"));
		List<BranchSchool> branchSchools = BranchSchoolHelper.getBranchSchools();
		BranchSchool branchSchool = null;
		BranchSchool trainerBranchSchool = null; 
		long branchSchoolId = GsonUtil.getLong(obj, "branchSchoolId");
		long trainerBranchSchoolId = GsonUtil.getLong(obj, "trainerBranchSchoolId");
		if(branchSchools != null)
		{
			for(int i=0; i<branchSchools.size(); i++)
			{
				BranchSchool item = branchSchools.get(i);
				if(trainerBranchSchool != null && branchSchool != null)
				{
					break;
				}
				if(branchSchool == null && branchSchoolId == item.getId())
				{
					branchSchool = item;
				}
				if(trainerBranchSchool == null && trainerBranchSchoolId == item.getId())
				{
					trainerBranchSchool = item;
				}
			}
		}
		if(branchSchool==null)
		{
			branchSchool = new BranchSchool();
			branchSchool.setId(branchSchoolId);
			branchSchool.setName(GsonUtil.getString(obj, "branchSchoolName"));
		}
		if(trainerBranchSchool == null)
		{
			trainerBranchSchool = new BranchSchool();
			trainerBranchSchool.setId(trainerBranchSchoolId);
			trainerBranchSchool.setName(GsonUtil.getString(obj, "trainerBranchSchoolName"));
		}
		student.setBranchSchool(branchSchool);
		trainer.setBranchSchool(trainerBranchSchool);
		order.setTrainer(trainer);
		Yard yard = new Yard();
		yard.setId(GsonUtil.getLong(obj, "yardId"));
		yard.setName(GsonUtil.getString(obj, "yardName"));
		order.setYard(yard);
	}
	
}
