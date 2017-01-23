package com.session.dgjp.response;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.orhanobut.logger.Logger;
import com.session.common.BaseListResponseData;
import com.session.common.utils.GsonUtil;
import com.session.common.utils.LogUtil;
import com.session.common.utils.TextUtil;
import com.session.dgjp.enity.BranchSchool;
import com.session.dgjp.enity.EndFlag;
import com.session.dgjp.enity.Trainer;
import com.session.dgjp.helper.BranchSchoolHelper;

import java.util.ArrayList;
import java.util.List;

public class TrainerListResponseData extends BaseListResponseData {
    private static final long serialVersionUID = 1L;
    private List<Trainer> list;

    public List<Trainer> getList() {
        return list;
    }

    @Override
    public void parseData(String jsonStr) {
        Logger.i("json = " + jsonStr );
        JsonParser parser = new JsonParser();
        JsonObject dataObj = (JsonObject) parser.parse(jsonStr);
        setEndFlag(GsonUtil.getInt(dataObj, ENDFLAG, EndFlag.Flag_1.getValue()));
        JsonArray array = dataObj.getAsJsonArray(LIST);
        list = new ArrayList<Trainer>();
        if (array != null && array.size() > 0) {
            List<BranchSchool> schools = BranchSchoolHelper.getBranchSchools();
            for (int i = 0; i < array.size(); i++) {
                Trainer trainer = new Trainer();
                JsonObject obj = (JsonObject) array.get(i);
                trainer.setAccount(GsonUtil.getString(obj, "account"));
                trainer.setName(GsonUtil.getString(obj, "name"));
                long id = GsonUtil.getLong(obj, "branchSchoolId");
                if (schools != null) {
                    for (int j = 0; i < schools.size(); j++) {
                        BranchSchool school = schools.get(j);
                        if (school.getId() == id) {
                            trainer.setBranchSchool(school);
                            break;
                        }
                    }
                }
                //如果在列表中没有对应的学校，就从json中解析
                if (trainer.getBranchSchool() == null) {
                    BranchSchool school = new BranchSchool();
                    school.setId(id);
                    school.setName(GsonUtil.getString(obj, "branchSchoolName"));
                    school.setDriverSchoolName(GsonUtil.getString(obj, "driverSchoolName"));
                    trainer.setBranchSchool(school);
                }
                trainer.setOrderTimes(GsonUtil.getInt(obj, "orderTimes"));
                trainer.setEval(GsonUtil.getFloat(obj, "eval"));
                trainer.setPhotoUrl(GsonUtil.getString(obj, "photoUrl"));
                trainer.setPhone(GsonUtil.getString(obj, "phone"));
                trainer.setTrainerType(GsonUtil.getString(obj, "accountType"));
                String timePeriod = GsonUtil.getString(obj, "timePeroid");
                try {
                    trainer.setTimePeroid(TextUtil.stringToList(timePeriod, Integer.class));
                    list.add(trainer);
                } catch (Exception e) {
                    LogUtil.e(TrainerListResponseData.class.getSimpleName(), e.toString(), e);
                }
            }
        }
    }

}
