package com.session.dgjx.response;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.session.common.BaseResponseData;
import com.session.common.utils.DateUtil;
import com.session.common.utils.GsonUtil;
import com.session.dgjx.enity.BranchSchool;
import com.session.dgjx.enity.Car;
import com.session.dgjx.enity.Course;
import com.session.dgjx.enity.Order;
import com.session.dgjx.enity.Student;
import com.session.dgjx.enity.Trainer;
import com.session.dgjx.enity.Yard;

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
		order.setRemark(GsonUtil.getString(obj, "remark"));
		order.setOrderDuration(GsonUtil.getInt(obj, "orderDuration"));
		order.setIsEval(GsonUtil.getString(obj, "isEval"));
		order.setBeginTime(GsonUtil.getDate(obj, "beginTime", DateUtil.NETWORK_TIME_SDF));
		order.setEndTime(GsonUtil.getDate(obj, "endTime", DateUtil.NETWORK_TIME_SDF));
		order.setSubmitTime(GsonUtil.getDate(obj, "submitTime", DateUtil.NETWORK_TIME_SDF));
		Student student = new Student();
		student.setAccount(GsonUtil.getString(obj, "studentAccount"));
		student.setName(GsonUtil.getString(obj, "studentName"));
		student.setPhotoUrl(GsonUtil.getString(obj, "studentPhotoUrl"));
		student.setPhone(GsonUtil.getString(obj, "studentPhone"));
		student.setProgress(GsonUtil.getString(obj, "progress"));
		student.setApplyTime(GsonUtil.getDate(obj, "applyTime", DateUtil.NETWORK_TIME_SDF));
		BranchSchool branchSchool = new BranchSchool();
		branchSchool.setId(GsonUtil.getLong(obj, "branchSchoolId"));
		branchSchool.setName(GsonUtil.getString(obj, "branchSchoolName"));
		student.setBranchSchool(branchSchool);
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
		BranchSchool trainerBranchSchool = new BranchSchool();
		trainerBranchSchool.setId(GsonUtil.getLong(obj, "trainerBranchSchoolId"));
		trainerBranchSchool.setName(GsonUtil.getString(obj, "trainerBranchSchoolName"));
		trainer.setBranchSchool(trainerBranchSchool);
		order.setTrainer(trainer);
		Yard yard = new Yard();
		yard.setId(GsonUtil.getLong(obj, "yardId"));
		yard.setName(GsonUtil.getString(obj, "yardName"));
		order.setYard(yard);
	}
	
}
