package com.session.dgjp.school;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Filter.FilterListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.InfoWindowAdapter;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.session.common.BaseFragment;
import com.session.dgjp.R;
import com.session.dgjp.enity.BranchSchool;
import com.session.dgjp.helper.BranchSchoolHelper;
import com.session.dgjp.helper.BranchSchoolHelper.BranchSchoolListener;
import com.session.dgjp.school.SchoolAdapter.OrderListener;
import com.session.dgjp.trainer.TrainerListActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class SchoolListFragment extends BaseFragment implements OrderListener, BranchSchoolListener, AMapLocationListener, OnMarkerClickListener, OnItemClickListener, InfoWindowAdapter, FilterListener {

    //地图
    private final static float ZOOM_LEVEL = 16f;
    private View mapLayout;
    private MapView mapView;
    private AMap map;
    private AMapLocationClient locationClient;
    private AutoCompleteTextView mapSchoolSearchTv;
    private ArrayAdapter<BranchSchool> mapSchoolAdapter;
    private View mapSchoolResetBtn;
    private AMapLocation location;
    private AMapLocation lastLocation;//上一次距离超过一百米的定位
    //分校列表
    private View schoolListLayout;
    private SchoolAdapter listSchoolAdapter;
    //private EditText searchEt;
    private EditText listSchoolSearchEt;
    private View listSchoolResetBtn;
    private TextView noDataTv;
    private ListView listView;
    private OnMapListChangeListener onMapListChangeListener;//地图和列表切换的回调接口
    private boolean schoolFlag = true;//分校是否加载成功的标记
    private BranchSchoolHelper helper;
    private List<BranchSchool> schools;
    private BranchSchool branchSchool;//被预约的分校

    private Map<BranchSchool, MarkerHolder> markerMap = new HashMap<BranchSchool, SchoolListFragment.MarkerHolder>();
    private MarkerHolder myMarkerHolder;
    private LayoutInflater inflater;


    @Override
    protected int getContentRes() {
        return R.layout.school_list_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        inflater = LayoutInflater.from(getActivity());
        mapLayout = view.findViewById(R.id.map_layout);
        mapLayout.findViewById(R.id.ivLocate).setOnClickListener(this);
        mapLayout.findViewById(R.id.ivTrainerList).setOnClickListener(this);
        mapLayout.findViewById(R.id.zoom_out).setOnClickListener(this);
        mapLayout.findViewById(R.id.zoom_in).setOnClickListener(this);
        // 获取地图控件引用
        mapView = (MapView) mapLayout.findViewById(R.id.map);
        // 在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
        mapView.onCreate(savedInstanceState);
        map = mapView.getMap();
        setUpMap();
        mapSchoolSearchTv = (AutoCompleteTextView) mapLayout.findViewById(R.id._search);
        mapSchoolSearchTv.setOnItemClickListener(this);
        mapSchoolResetBtn = mapLayout.findViewById(R.id._reset);
        mapSchoolResetBtn.setOnClickListener(this);
        mapSchoolSearchTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 0) {
                    mapSchoolResetBtn.setVisibility(View.GONE);
                } else {
                    mapSchoolResetBtn.setVisibility(View.VISIBLE);
                }
            }
        });
        schoolListLayout = view.findViewById(R.id.school_list_layout);
        noDataTv = (TextView) view.findViewById(R.id.no_data);
        listView = (ListView) schoolListLayout.findViewById(R.id.list_view);
        listSchoolAdapter = new SchoolAdapter(getActivity(), this);
        listView.setAdapter(listSchoolAdapter);
        schoolListLayout.findViewById(R.id.ivMap).setOnClickListener(this);
        listSchoolSearchEt = (EditText) schoolListLayout.findViewById(R.id.search);
        listSchoolResetBtn = schoolListLayout.findViewById(R.id.reset);
        listSchoolResetBtn.setOnClickListener(this);
        listSchoolSearchEt.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //endFlag = EndFlag.Flag_1.getValue();
                if (editable.length() == 0) {
                    listSchoolResetBtn.setVisibility(View.GONE);
                } else {
                    listSchoolResetBtn.setVisibility(View.VISIBLE);
                }
                listSchoolAdapter.getFilter().filter(editable.toString(), SchoolListFragment.this);
            }
        });

        //启动定位
        locationClient = new AMapLocationClient(act);
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        // 设置定位监听
        locationClient.setLocationListener(this);
        // 设置为高精度定位模式
        mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
        // 设置定位参数
        mLocationOption.setInterval(30 * 1000);
        mLocationOption.setNeedAddress(true);
        locationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        locationClient.startLocation();
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            final int errorCode = amapLocation.getErrorCode();
            if (errorCode == AMapLocation.LOCATION_SUCCESS) {
                location = amapLocation;//保留最近的定位
                LatLng latLng = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
                if (myMarkerHolder == null) {
                    View view = View.inflate(act, R.layout.my_location_marker, null);
                    myMarkerHolder = new MarkerHolder();
                    myMarkerHolder.setView(view);
                    MarkerOptions options = new MarkerOptions();
                    options.icon(BitmapDescriptorFactory.fromView(view));
                    options.position(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude()));
                    options.title(getString(R.string.my_location));
                    Marker marker = map.addMarker(options);
                    myMarkerHolder.setMarker(marker);
                    map.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
                } else {
                    myMarkerHolder.getMarker().setPosition(latLng);
                }
                if (schools != null) {
                    calculateAndSort(true);
                    //map.invalidate();
                }
            } else {
                String errText = "定位失败," + errorCode + ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        } else {
            Log.e("AmapErr", "定位失败，未知错误");
        }
        if (schoolFlag) {
            schoolFlag = false;
            if (helper == null) {
                helper = new BranchSchoolHelper(this);
            }
            //加载分校列表
            //			progressDialog.setMessage(getText(R.string.querying));
            //			progressDialog.show();
            helper.getBranchSchoolsAsync(null, null);
        }
    }

    /**
     * 计算距离，排序
     *
     * @param flag，是否需要计算距离，true表示需要计算距离并排序，false表示不计算距离只排序
     * @author YJ Liang
     * 2016  上午10:08:33
     */
    private void calculateAndSort(boolean flag) {
        if (location != null) {
            LatLng startLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            if (flag) {
                if (lastLocation != null) {
                    /*如果location和lastLocation之间的距离小于50米，则不需要重新计算分校/分店的距离，否则需要计算分校的距离，
					 * 且lastLocation指向location
					 */
                    LatLng lastLatLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                    if (AMapUtils.calculateLineDistance(startLatLng, lastLatLng) < 50f) {
                        return;
                    } else {
                        lastLocation = location;
                    }
                } else {
                    lastLocation = location;
                }
            }
            List<BranchSchool> schools1 = new ArrayList<BranchSchool>();//上次预约分校(分店)或报名分校(分店)
            List<BranchSchool> schools2 = new ArrayList<BranchSchool>();//有经纬度的分校
            List<BranchSchool> schools3 = new ArrayList<BranchSchool>();//其它分校
            List<BranchSchool> schools4 = new ArrayList<BranchSchool>();//有经纬度的分店
            List<BranchSchool> schools5 = new ArrayList<BranchSchool>();//其它分店
            int size = schools.size();
            for (int i = 0; i < size; i++) {
                BranchSchool school = schools.get(i);
                LatLng endLatLng = school.getLatLng();
                String branchSchoolType = school.getBranchSchoolType();
                String type = school.getType();
                if (endLatLng != null) {
                    //计算结果单位是m，转换成km
                    if (flag) {
                        school.setDistance(AMapUtils.calculateLineDistance(startLatLng, endLatLng) / 1000);
                    }
                    if (BranchSchool.TYPE_APPLY.equals(branchSchoolType) || BranchSchool.TYPE_LATEST.equals(branchSchoolType)) {
                        schools1.add(school);
                    } else {
                        if (BranchSchool.TYPE_SCHOOL.equals(type)) {
                            schools2.add(school);
                        } else if (BranchSchool.TYPE_STORE.equals(type)) {
                            schools4.add(school);
                        } else {

                        }
                    }
                } else {
                    if (BranchSchool.TYPE_SCHOOL.equals(type)) {
                        schools3.add(school);
                    } else if (BranchSchool.TYPE_STORE.equals(type)) {
                        schools5.add(school);
                    } else {

                    }
                }
            }

            Collections.sort(schools2);
            Collections.sort(schools4);
            schools.clear();
            schools.addAll(schools1);
            schools.addAll(schools2);
            schools.addAll(schools3);
            schools.addAll(schools4);
            schools.addAll(schools5);
            listSchoolAdapter.getFilter().filter(listSchoolSearchEt.getText().toString().trim(), this);
            if (mapSchoolAdapter != null) {
                mapSchoolAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 设置一些AMap的属性
     */
    private void setUpMap() {
        // 设置默认定位按钮是否显示
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.getUiSettings().setZoomControlsEnabled(false);
        map.moveCamera(CameraUpdateFactory.zoomTo(ZOOM_LEVEL));
        // 设置自定义InfoWindow样式
        map.setInfoWindowAdapter(this);
        // 设置点击marker事件监听器
        map.setOnMarkerClickListener(this);
    }


    @Override
    public void onOrder(BranchSchool branchSchool) {
        this.branchSchool = branchSchool;
        if (branchSchool.getTrainerTotal() > 0) {
            Intent intent = new Intent(getActivity(), TrainerListActivity.class);
            intent.putExtra(BranchSchool.class.getName(), branchSchool);
            startActivity(intent);
        } else {
            toast(R.string.school_no_trainer, Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && schoolFlag && helper != null) {
            schoolFlag = false;
            //加载分校列表
            progressDialog.setMessage(getText(R.string.querying));
            progressDialog.show();
            helper.getBranchSchoolsAsync(null, null);
        }
    }

    @Override
    public void onGetBranchSchoolsSuccess(List<BranchSchool> list) {
        schoolFlag = false;
        if (schools == null) {
            schools = new ArrayList<BranchSchool>();
        }
        schools.clear();
        if (list != null){
            schools.addAll(list);
            initSchoolList();
            progressDialog.dismiss();
        }
    }

    @Override
    public void onGetBranchSchoolsFail(int code, String msg) {
        schoolFlag = true;
        toast(msg, R.string.default_error_msg, Toast.LENGTH_SHORT);
        if (schools == null) {
            schools = new ArrayList<BranchSchool>();
        }
        if (schools.isEmpty() || schools.get(0).getId() != account.getBranchSchoolId()) {
            schools.clear();
            BranchSchool branchSchool = new BranchSchool();
            branchSchool.setId(account.getBranchSchoolId());
            branchSchool.setName(account.getBranchSchoolName());
            branchSchool.setAddress(account.getBranchSchoolAddress());
            branchSchool.setPhotoUrl(account.getBranchSchoolPhotoUrl());
            branchSchool.setSmallPhotoUrl(account.getBranchSchoolSmallPhotoUrl());
            branchSchool.setPhone(account.getBranchSchoolPhone());
            branchSchool.setLongitude(account.getBranchSchoolLongitude());
            branchSchool.setLatitude(account.getBranchSchoolLatitude());
            branchSchool.setBranchSchoolType(BranchSchool.TYPE_APPLY);
            branchSchool.setTrainerTotal(account.getTrainerTotal());
            schools.add(branchSchool);
            initSchoolList();
        }
        progressDialog.dismiss();
		/*if(BaseResponse.CODE_LOGIN_FAIL == code || BaseResponse.CODE_SESSION_FAIL == code)
		{
			toLogin();
		}*/
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }


    @Override
    public void onDestroyView() {
        locationClient.onDestroy();
        mapView.onDestroy();
        super.onDestroyView();
    }

    private void initSchoolList() {
        //清空之前除我的定位外的marker
        Collection<MarkerHolder> holders = markerMap.values();
        Iterator<MarkerHolder> iterator = holders.iterator();
        while (iterator.hasNext()) {
            iterator.next().getMarker().remove();
        }
        markerMap.clear();
        lastLocation = null;
        calculateAndSort(true);
        addMarkers(schools);
        mapSchoolAdapter = new ArrayAdapter<BranchSchool>(getActivity(), R.layout.drop_down_textview_2, schools);
        mapSchoolSearchTv.setAdapter(mapSchoolAdapter);
        Resources resources = getResources();
        int width = view.findViewById(R.id.search_layout).getWidth() + resources.getDimensionPixelOffset(R.dimen.view_10);
        int offset = -resources.getDimensionPixelOffset(R.dimen.view_30);
        mapSchoolSearchTv.setDropDownWidth(width);
        mapSchoolSearchTv.setDropDownHorizontalOffset(offset);
        listSchoolAdapter.setList(schools);
        listSchoolAdapter.getFilter().filter(listSchoolSearchEt.getText().toString().trim(), this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivMap: // 地图
                schoolListLayout.setVisibility(View.GONE);
                mapLayout.setVisibility(View.VISIBLE);
                if (onMapListChangeListener != null) {
                    onMapListChangeListener.onChange(OnMapListChangeListener.MAP);
                }
                break;
            case R.id.ivTrainerList: // 教练列表
                switchToList();
                break;
            case R.id.reset:
                listSchoolSearchEt.setText("");
                break;
            case R.id._reset:
                mapSchoolSearchTv.setText("");
                break;
            case R.id.ivLocate:
                if (location != null) {
                    //我的位置显示在地主中央
                    map.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
                }
                break;
            case R.id.zoom_in:
                map.moveCamera(CameraUpdateFactory.zoomBy(2));
                break;
            case R.id.zoom_out:
                map.moveCamera(CameraUpdateFactory.zoomBy(-2));
                break;
            default:
                super.onClick(v);
                break;
        }
    }

    private void switchToList() {
        mapLayout.setVisibility(View.GONE);
        schoolListLayout.setVisibility(View.VISIBLE);
        if (onMapListChangeListener != null) {
            onMapListChangeListener.onChange(OnMapListChangeListener.LIST);
        }
    }

    /**
     * 预约成功的回调接口
     * @author YJ Liang
     * 2016  上午8:55:05
     */
	/*public static interface OnOrderSuccessListener
	{
		public abstract void OnOrderSuccess();
	}*/


    /**
     * 对marker标注点点击响应事件
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        //处理上一次被点击的marker
        //查询当前被点击的maker对应的分校
        Entry<BranchSchool, MarkerHolder> entry = getEntry(marker);
        if (entry != null) {
            map.moveCamera(CameraUpdateFactory.zoomTo(ZOOM_LEVEL));
            listSchoolSearchEt.setText(entry.getKey().getName());
            switchToList();
        }
        return false;
    }


    /**
     * 添加标记
     */
    private void addMarkers(List<BranchSchool> list) {
        for (int i = 0; i < list.size(); i++) {
            BranchSchool branchSchool = list.get(i);
            LatLng latLng = branchSchool.getLatLng();
            if (latLng != null) {
                MarkerOptions options = new MarkerOptions();
                options.position(latLng);
                options.title(branchSchool.getName());
                MarkerHolder holder = getMarkerHolder();
                options.icon(BitmapDescriptorFactory.fromView(holder.getView()));
                Marker marker = map.addMarker(options);
                holder.setMarker(marker);
                markerMap.put(branchSchool, holder);
                if (BranchSchool.TYPE_APPLY.equals(branchSchool.getBranchSchoolType()) || BranchSchool.TYPE_LATEST.equals(branchSchool.getBranchSchoolType())) {
                    marker.showInfoWindow();
                }
            }
        }
    }

    private MarkerHolder getMarkerHolder() {
        View view = inflater.inflate(R.layout.school_marker, null);
        MarkerHolder holder = new MarkerHolder();
        holder.setView(view);
        return holder;
    }

    public void setOnMapListChangeListener(OnMapListChangeListener onMapListChangeListener) {
        this.onMapListChangeListener = onMapListChangeListener;
    }

    public static interface OnMapListChangeListener {
        public final static int MAP = 1;
        public final static int LIST = 2;

        /**
         * 地图和教练列表转换的回调方法
         *
         * @param code {@link #LIST} {@link #MAP}
         * @author YJ Liang
         * 2016  上午10:06:15
         */
        public abstract void onChange(int code);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BranchSchool school = (BranchSchool) parent.getAdapter().getItem(position);
        LatLng latLng = school.getLatLng();
        if (latLng != null) {
            map.moveCamera(CameraUpdateFactory.changeLatLng(school.getLatLng()));
            map.moveCamera(CameraUpdateFactory.zoomTo(ZOOM_LEVEL));
            markerMap.get(school).getMarker().showInfoWindow();
        } else {
            toast(R.string.school_no_location, Toast.LENGTH_SHORT);
        }
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(parent.getApplicationWindowToken(), 0);
        }
    }

    private static class MarkerHolder {
        private Marker marker;
        private View view;
        private TextView infoWindowTv;

        public Marker getMarker() {
            return marker;
        }

        public void setMarker(Marker marker) {
            this.marker = marker;
        }

        public View getView() {
            return view;
        }

        public void setView(View view) {
            this.view = view;
        }

        public TextView getInfoWindowTv() {
            return infoWindowTv;
        }

        public void setInfoWindowTv(TextView infoWindowTv) {
            this.infoWindowTv = infoWindowTv;
        }
    }

    @Override
    public View getInfoContents(Marker arg0) {
        return null;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        //查询当前被点击的maker对应的分校
        Entry<BranchSchool, MarkerHolder> entry = getEntry(marker);
        if (entry != null) {
            MarkerHolder markerHolder = entry.getValue();
            BranchSchool branchSchool = entry.getKey();
            String type = branchSchool.getBranchSchoolType();
            TextView infoWindowTv = markerHolder.getInfoWindowTv();
            if (infoWindowTv == null) {
                infoWindowTv = (TextView) inflater.inflate(R.layout.info_window, null);
                markerHolder.setInfoWindowTv(infoWindowTv);
            }
            if (BranchSchool.TYPE_APPLY.equals(type)) {
                double distance = branchSchool.getDistance();
                if (distance < 0) {
                    infoWindowTv.setText(R.string.your_apply_school);
                } else {
                    infoWindowTv.setText(getString(R.string.your_apply_school_km, distance));
                }
            } else if (BranchSchool.TYPE_LATEST.equals(type)) {
                double distance = branchSchool.getDistance();
                if (distance < 0) {
                    infoWindowTv.setText(R.string.your_last_school);
                } else {
                    infoWindowTv.setText(getString(R.string.your_last_school_km, distance));
                }
            } else {
                infoWindowTv.setText(branchSchool.getName());
            }
            return infoWindowTv;
        } else if (myMarkerHolder.getMarker().equals(marker)) {
            TextView infoWindowTv = myMarkerHolder.getInfoWindowTv();
            if (infoWindowTv == null) {
                infoWindowTv = (TextView) inflater.inflate(R.layout.info_window, null);
                infoWindowTv.setTextColor(getResources().getColor(R.color.red_FC7D0D));
                infoWindowTv.setText(R.string.my_location);
                myMarkerHolder.setInfoWindowTv(infoWindowTv);
            }
			/*if(location != null)
			{
				infoWindowTv.setText(getString(R.string.my_detail_location,location.getCity()+location.getDistrict()+location.getStreet()+location.getStreetNum()));
			}*/
            return infoWindowTv;
        } else {
            return null;
        }


    }

    private Entry<BranchSchool, MarkerHolder> getEntry(Marker marker) {
        Set<Entry<BranchSchool, MarkerHolder>> set = markerMap.entrySet();
        Iterator<Entry<BranchSchool, MarkerHolder>> iterator = set.iterator();
        while (iterator.hasNext()) {
            Entry<BranchSchool, MarkerHolder> entry = iterator.next();
            MarkerHolder markerHolder = entry.getValue();
            if (marker.equals(markerHolder.getMarker())) {
                return entry;
            }
        }
        return null;
    }

    @Override
    public void onFilterComplete(int count) {
        if (count > 0) {
            noDataTv.setVisibility(View.GONE);
        } else {
            noDataTv.setVisibility(View.VISIBLE);
        }
    }

    public void updateLatestSchool() {
        //找出之前的上一次预约分校
		/*int size = schools.size();
		for(int i=0; i<size; i++)
		{
			BranchSchool school = schools.get(i);
			if(BranchSchool.TYPE_LATEST.equals(school.getBranchSchoolType()))
			{
				if(!branchSchool.equals(school))
				{
					school.setBranchSchoolType(BranchSchool.TYPE_OTHER);
					branchSchool.setBranchSchoolType(BranchSchool.TYPE_LATEST);
					schools.remove(branchSchool);
					schools.add(0, branchSchool);
					calculateDistance();
					break;
				}
			}
		}*/
        if (branchSchool == null) {
            return;
        }
        if (BranchSchool.TYPE_APPLY.equals(branchSchool.getBranchSchoolType())) {
            //如果该分校是报名分校，只需要改成上次预约分校即可
            branchSchool.setBranchSchoolType(BranchSchool.TYPE_LATEST);
            listSchoolAdapter.notifyDataSetChanged();
            if (mapSchoolAdapter != null) {
                mapSchoolAdapter.notifyDataSetChanged();
            }
        } else if (BranchSchool.TYPE_OTHER.equals(branchSchool.getBranchSchoolType())) {
            //如果该分校是其它分校，这需要将其设为预约分校并重新排序
            schools.get(0).setBranchSchoolType(BranchSchool.TYPE_OTHER);
            branchSchool.setBranchSchoolType(BranchSchool.TYPE_LATEST);
            calculateAndSort(false);
        } else {
            //如果该分校本来就是上次预约的分校，则不需要更新
        }
    }

}
