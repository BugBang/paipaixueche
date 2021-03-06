package com.session.dgjp.trainer;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.session.common.BaseRequest;
import com.session.common.BaseResponse;
import com.session.common.utils.DateUtil;
import com.session.common.utils.LogUtil;
import com.session.common.utils.TextUtil;
import com.session.common.utils.ToastUtil;
import com.session.dgjp.R;
import com.session.dgjp.enity.Car;
import com.session.dgjp.enity.Teaching;
import com.session.dgjp.enity.TeachingSchedule;
import com.session.dgjp.enity.Trainer;
import com.session.dgjp.login.LoginActivity;
import com.session.dgjp.request.CarScheduleRequestData;
import com.session.dgjp.response.CarScheduleResponseData;
import com.session.dgjp.view.NoScrollGridView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderOptionDialog extends Dialog implements View.OnClickListener, OnItemSelectedListener, OnItemClickListener {
    private final static String TAG = OrderOptionDialog.class.getSimpleName();
    private Spinner dateSpinner;
    private TeachingAdapter teachingAdapter;
    private TeachingSchedule teachingSchedule;
    //private final static int LIMIT = 2;//一次最多只能选择两个时间段
    private List<List<Teaching>> list = new ArrayList<List<Teaching>>();
    private List<Teaching> latestSelectedTeachings = new ArrayList<Teaching>();
    private List<Teaching> selectedTeachings = new ArrayList<Teaching>();
    private OnConfirmListener onConfirmListener;
    private Trainer trainer;
    private View timeLayout;
    private TextView timeTv;//显示用户预约的时间
    private CarAdapter carAdapter;
    private TextView noDataTv;
    private Car latestSelectedCar;
    private Car selectedCar;
    private List<Car> cars;
    private String dateStr, timeSlotStr;
    private ProgressDialog progressDialog;
    private Context context;
    private int lastSelectedPosition = 0;//最后一次点击确定时选择的日期序号
    private int selectedPosition = 0;//当前日期序号
    private final static int GET_CARS_SUCCESS = 1;
    private final static int GET_CARS_FAIL = 2;
    private final static int SHOW_PROGRESS_DIALOG = 3;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_CARS_SUCCESS:
                    List<Car> cars = (List<Car>) msg.obj;
                    carAdapter.setList(cars);
                    carAdapter.notifyDataSetChanged();
                    if (cars != null && !cars.isEmpty()) {
                        noDataTv.setVisibility(View.GONE);
                    } else {
                        noDataTv.setVisibility(View.VISIBLE);
                    }
                    break;
                case GET_CARS_FAIL:
                    carAdapter.setList(null);
                    carAdapter.notifyDataSetChanged();
                    BaseResponse<CarScheduleResponseData> baseResponse = (BaseResponse<CarScheduleResponseData>) msg.obj;
                    if (baseResponse != null) {
                        if (baseResponse.toLogin()) {
                            context.startActivity(new Intent(context, LoginActivity.class));
                        }
                        ToastUtil.toast(context, baseResponse.getMsg(), R.string.query_datas_fail, Toast.LENGTH_SHORT);
                    } else {
                        ToastUtil.toast(context, R.string.query_datas_fail, Toast.LENGTH_SHORT);
                    }
                    noDataTv.setVisibility(View.VISIBLE);
                    break;
                case SHOW_PROGRESS_DIALOG:
                    progressDialog.setMessage(context.getText(R.string.querying));
                    progressDialog.show();
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }

    };


    public OrderOptionDialog(Context context) {
        super(context, R.style.FullScreenDialg);
        this.context = getContext();
        teachingAdapter = new TeachingAdapter(this.context);
        teachingAdapter.setSelectedTeachings(selectedTeachings);
        setContentView(R.layout.order_option_dialog);
        progressDialog = new ProgressDialog(this.context);
        progressDialog.setCancelable(false);
        timeLayout = findViewById(R.id.time_layout);
        timeTv = (TextView) timeLayout.findViewById(R.id.time);
        noDataTv = (TextView) findViewById(R.id.no_data);
        ListView listView = (ListView) findViewById(R.id.list_view);
        carAdapter = new CarAdapter(context);
        listView.setAdapter(carAdapter);
        listView.setOnItemClickListener(this);
        NoScrollGridView gridView = (NoScrollGridView) findViewById(R.id.grid_view);
        gridView.setAdapter(teachingAdapter);
        gridView.setOnItemClickListener(this);
        dateSpinner = (Spinner) findViewById(R.id.date_spinner);
        dateSpinner.setOnItemSelectedListener(this);
        findViewById(R.id.confirm).setOnClickListener(this);
        findViewById(R.id.cancel).setOnClickListener(this);
        findViewById(R.id.ivTitleLeft).setOnClickListener(this);
        ((TextView) findViewById(R.id.tvTitle)).setText(R.string.book);
    }

    public void setOnConfirmListener(OnConfirmListener onConfirmListener) {
        this.onConfirmListener = onConfirmListener;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public void setTeachingSchedule(TeachingSchedule teachingSchedule) {
        this.teachingSchedule = teachingSchedule;
        list.clear();
        //转换数据结构
        List<Teaching> teachings = teachingSchedule.getTeachingList();
        List<OrderDate> orderDates = new ArrayList<OrderDate>();
        for (int i = 0; i < teachings.size(); i++) {
            Teaching teaching = teachings.get(i);
            Date date = teaching.getTeachingTime();
            orderDates.add(new OrderDate(date));
            List<Teaching> _teachings = new ArrayList<Teaching>();
            List<Integer> timePeroid = trainer.getTimePeroid();
            for (int j = 0; j < timePeroid.size(); j++) {
                Teaching _teaching = new Teaching();
                _teaching.setTeachingTime(date);
                List<Integer> timeSlot = new ArrayList<Integer>();
                timeSlot.add(timePeroid.get(j));
                _teaching.setTimeSlot(timeSlot);
                _teachings.add(_teaching);
            }
            list.add(_teachings);
        }
        ArrayAdapter<OrderDate> orderDateAdapter = new ArrayAdapter<OrderDate>(context, R.layout.spinner_tv, orderDates);
        orderDateAdapter.setDropDownViewResource(R.layout.drop_down_textview);
        dateSpinner.setAdapter(orderDateAdapter);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.confirm:
                lastSelectedPosition = selectedPosition;
                latestSelectedTeachings.clear();
                latestSelectedTeachings.addAll(selectedTeachings);
                latestSelectedCar = selectedCar;
                cars = carAdapter.getList();
                if (onConfirmListener != null) {
                    onConfirmListener.onConfirm(selectedTeachings, selectedCar, (String) timeTv.getTag());
                }
                dismiss();
                break;
            case R.id.cancel:
            case R.id.ivTitleLeft:
                dismiss();
                break;
            default:
                break;
        }
    }

    private static class OrderDate {
        private Date date;

        public OrderDate(Date date) {
            super();
            this.date = date;
        }

        @Override
        public String toString() {
            return DateUtil.LOCAL_SIMPLE_SDF.format(date);
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedPosition = position;
        teachingAdapter.setList(list.get(position));
        teachingAdapter.setTeachings(teachingSchedule.getTeachingList());
        teachingAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.grid_view) {
            if (view.isEnabled()) {
                CheckedTextView checkedTextView = (CheckedTextView) view;
                Teaching item = (Teaching) checkedTextView.getTag();
                if (!checkedTextView.isChecked()) {
                    if (selectedTeachings.isEmpty()) {
                        //如果之前没有选择时段，则直接添加时段
                        selectedTeachings.add(item);
                    } else {
                        Teaching startTeaching = selectedTeachings.get(0);
                        if (startTeaching.getTeachingTime().equals(item.getTeachingTime())) {
                            //如果日期相同，则判断时段
                            int itemTimeSlot = item.getTimeSlot().get(0);
                            int startTimeSlot = startTeaching.getTimeSlot().get(0);
                            if (itemTimeSlot < startTimeSlot) {
                                if (itemTimeSlot == startTimeSlot - 1) {
                                    //如果开始时段和最新选择的时段相连，则在集合前面添加最新选择的时段，并判断集合元素个数是否大于2，如果是，则将最后的元素删除
                                    selectedTeachings.add(0, item);
                                    /*while(selectedTeachings.size()>LIMIT)
									{
										selectedTeachings.remove(selectedTeachings.size()-1);
									}*/
                                } else {
                                    //不相连，则直接清空集合并添加最新选择的时段
                                    selectedTeachings.clear();
                                    selectedTeachings.add(item);
                                }
                            } else {
                                int endTimeSlot = selectedTeachings.get(selectedTeachings.size() - 1).getTimeSlot().get(0);
                                if (itemTimeSlot == endTimeSlot + 1) {
                                    //如果结束时段和最新选择的时段相连，则在集合末尾添加最新选择的时段，并判断集合元素个数是否大于2，如果是，则将前面的元素删除
                                    selectedTeachings.add(item);
									/*while(selectedTeachings.size()>LIMIT)
									{
										selectedTeachings.remove(0);
									}*/
                                } else {
                                    //不相连，则直接清空集合并添加最新选择的时段
                                    selectedTeachings.clear();
                                    selectedTeachings.add(item);
                                }
                            }
                        } else {
                            //日期不相同，直接清空之前的选择的时段，并添加新的时段
                            selectedTeachings.clear();
                            selectedTeachings.add(item);
                        }
                    }
                } else {
                    int i = selectedTeachings.indexOf(item);
                    if (i == 0 || i == selectedTeachings.size() - 1) {
                        selectedTeachings.remove(item);
                    } else {
                        ToastUtil.toast(context, R.string.period_must_be_consecutive, Toast.LENGTH_SHORT);
                        return;
                    }
                }
                teachingAdapter.notifyDataSetChanged();
                showSelectedTeaching();
            }
        } else {
            Car item = (Car) parent.getAdapter().getItem(position);
            if (item.equals(selectedCar)) {
                selectedCar = null;
            } else {
                selectedCar = item;
            }
            carAdapter.setSelectedCar(selectedCar);
            carAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 根据选择时段的变化，显示目前所选择的时段及可选车辆
     *
     * @author YJ Liang
     * 2016  上午11:03:26
     */
    private void showSelectedTeaching() {
        switch (selectedTeachings.size()) {
            case 0:
                timeTv.setText("");
                timeTv.setTag(null);
                timeLayout.setVisibility(View.GONE);
                carAdapter.setList(null);
                carAdapter.notifyDataSetChanged();
                dateStr = "";
                timeSlotStr = "";
                break;
            case 1: {
                Teaching teaching = selectedTeachings.get(0);
                int hour = teaching.getTimeSlot().get(0);
                String startTimeStr, endTimeStr;
                if (hour < 10) {
                    startTimeStr = "0" + hour + ":00";
                    endTimeStr = "0" + hour + ":59";
                } else {
                    startTimeStr = hour + ":00";
                    endTimeStr = hour + ":59";
                }
                String timeStr = context.getString(R.string.selected_order_time, DateUtil.LOCAL_SIMPLE_SDF.format(teaching.getTeachingTime()), startTimeStr, endTimeStr);
                timeTv.setText(timeStr);
                timeTv.setTag(timeStr);
                timeLayout.setVisibility(View.VISIBLE);
                dateStr = DateUtil.NETWORK_DATE_SDF.format(teaching.getTeachingTime());
                timeSlotStr = TextUtil.listToString(teaching.getTimeSlot());
                getCars(dateStr, timeSlotStr);
                break;
            }
            default: {
                int size = selectedTeachings.size();
                Teaching startTeaching = selectedTeachings.get(0);
                Teaching endTeaching = selectedTeachings.get(size - 1);
                String startTimeStr, endTimeStr;
                int startHour = startTeaching.getTimeSlot().get(0);
                int endHour = endTeaching.getTimeSlot().get(0);
                if (startHour < 10) {
                    startTimeStr = "0" + startHour + ":00";
                } else {
                    startTimeStr = startHour + ":00";
                }
                if (endHour < 10) {
                    endTimeStr = "0" + endHour + ":59";
                } else {
                    endTimeStr = endHour + ":59";
                }
                String timeStr = context.getString(R.string.selected_order_time, DateUtil.LOCAL_SIMPLE_SDF.format(startTeaching.getTeachingTime()), startTimeStr, endTimeStr);
                timeTv.setText(timeStr);
                timeTv.setTag(timeStr);
                timeLayout.setVisibility(View.VISIBLE);
                dateStr = DateUtil.NETWORK_DATE_SDF.format(startTeaching.getTeachingTime());
                List<Integer> list = new ArrayList<Integer>();
                for (int i = 0; i < size; i++) {
                    list.addAll(selectedTeachings.get(i).getTimeSlot());
                }
                timeSlotStr = TextUtil.listToString(list);
                getCars(dateStr, timeSlotStr);
                break;
            }
        }
    }


    private void getCars(String teachingTime, String timeSlot) {
        CarRunnable carRunnable = new CarRunnable(teachingTime, timeSlot);
        AsyncTask.SERIAL_EXECUTOR.execute(carRunnable);
    }

    private class CarRunnable implements Runnable {
        private String teachingTime;//教学日期
        private String timeSlot;
        ;//教学时段

        public CarRunnable(String teachingTime, String timeSlot) {
            super();
            this.teachingTime = teachingTime;
            this.timeSlot = timeSlot;
        }

        @Override
        public void run() {
            //如果是查询，需要延时，下拉刷新，不需要延时
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                LogUtil.e(TAG, e.toString(), e);
            }
            //如果用户所选择的日期和时段改变，则该任务无需执行
            if (teachingTime.equals(dateStr) && timeSlot.equals(timeSlotStr)) {
                handler.sendEmptyMessage(SHOW_PROGRESS_DIALOG);
                BaseRequest request = new BaseRequest();
                CarScheduleRequestData requestData = new CarScheduleRequestData();
                requestData.setBranchSchoolId(trainer.getBranchSchool().getId());
                requestData.setTeachingTime(teachingTime);
                requestData.setTimeSlot(timeSlot);
                requestData.setTrainerAccount(trainer.getAccount());
                request.setRequestData(requestData);
                try {
                    BaseResponse<CarScheduleResponseData> response = request.sendRequest(CarScheduleResponseData.class);
                    Message msg = Message.obtain();
                    if (response.getCode() == BaseResponse.CODE_SUCCESS) {
                        msg.what = GET_CARS_SUCCESS;
                        msg.obj = response.getResponseData().getList();
                    } else {
                        msg.what = GET_CARS_FAIL;
                        msg.obj = response;
                    }
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    handler.sendEmptyMessage(GET_CARS_FAIL);
                    LogUtil.e(TAG, e.toString(), e);
                } finally {
                    progressDialog.dismiss();
                }

            }
        }
    }


    @Override
    public void show() {
        super.show();
        dateSpinner.setSelection(lastSelectedPosition);
        selectedCar = latestSelectedCar;
        carAdapter.setSelectedCar(selectedCar);
        if (!selectedTeachings.equals(latestSelectedTeachings)) {
            //将上一次点击确定时选择的时段覆盖上一次选择的时段，同时车辆列表也要显示上一次点击确定时的车辆列表
            selectedTeachings.clear();
            selectedTeachings.addAll(latestSelectedTeachings);
            carAdapter.setList(cars);
            carAdapter.notifyDataSetChanged();
        } else {
            cars = carAdapter.getList();
        }
    }

    /**
     * 重置用户的选项
     *
     * @author YJ Liang
     * 2016  下午3:25:28
     */
    public void clear() {
        timeTv.setText("");
        timeTv.setTag(null);
        timeLayout.setVisibility(View.GONE);
        dateStr = "";
        timeSlotStr = "";
        selectedCar = null;
        latestSelectedCar = null;
        cars = null;
        carAdapter.setList(null);
        carAdapter.notifyDataSetChanged();
        lastSelectedPosition = selectedPosition = 0;
        dateSpinner.setSelection(lastSelectedPosition, true);
        selectedTeachings.clear();
        latestSelectedTeachings.clear();
        teachingAdapter.notifyDataSetChanged();
    }

    public static interface OnConfirmListener {
        /**
         * 点击确认按钮的回调方法
         *
         * @param teachings 选择的时段
         * @param car       选择的车辆
         * @param timeStr   选择时段的格式化时间字符串
         * @author YJ Liang
         * 2016  下午9:11:23
         */
        public abstract void onConfirm(List<Teaching> teachings, Car car, String timeStr);
    }

}
