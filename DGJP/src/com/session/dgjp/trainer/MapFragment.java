package com.session.dgjp.trainer;

import java.util.List;

import org.json.JSONObject;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.InfoWindowAdapter;
import com.amap.api.maps2d.AMap.OnInfoWindowClickListener;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.CoordinateConverter;
import com.amap.api.maps2d.CoordinateConverter.CoordType;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.session.common.BaseFragment;
import com.session.common.BaseRequestTask;
import com.session.common.utils.LogUtil;
import com.session.dgjp.Constants;
import com.session.dgjp.HomeActivity;
import com.session.dgjp.R;
import com.session.dgjp.enity.BranchSchool;
import com.session.dgjp.request.QueryBranchSchoolListRequestData;

/** 地图 */
public class MapFragment extends BaseFragment implements LocationSource, AMapLocationListener, OnInfoWindowClickListener, InfoWindowAdapter, OnMarkerClickListener {

	private MapView mMapView;
	private AMap aMap;
	private OnLocationChangedListener mListener;
	private AMapLocationClient mlocationClient;
	private AMapLocationClientOption mLocationOption;
	private CoordinateConverter converter;

	@Override
	protected int getContentRes() {
		return R.layout.fragment_map;
	}

	@Override
	protected void init(Bundle savedInstanceState) {
		LogUtil.e(TAG, "init");
		view.findViewById(R.id.ivLocate).setOnClickListener(this);
		view.findViewById(R.id.ivTrainerList).setOnClickListener(this);
		// 获取地图控件引用
		mMapView = (MapView) view.findViewById(R.id.map);
		// 在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
		mMapView.onCreate(savedInstanceState);
		initAMap();
		converter = new CoordinateConverter();
		// CoordType.GPS 待转换坐标类型
		converter.from(CoordType.GPS);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ivLocate: // 定位
			break;
		case R.id.ivTrainerList: // 教练列表
			//((HomeActivity) getActivity()).addTrainerListFragment();
			break;
		default:
			super.onClick(v);
			break;
		}
	}

	/** 初始化AMap对象 */
	private void initAMap() {
		if (aMap == null) {
			aMap = mMapView.getMap();
			setUpMap();
		}
	}

	/** 设置一些AMap的属性 */
	private void setUpMap() {
		// 自定义系统定位小蓝点
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		// 设置小蓝点的图标
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker));
		// 设置圆形的边框颜色
		myLocationStyle.strokeColor(Color.TRANSPARENT);
		// 设置圆形的填充颜色
		myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));
		// 设置小蓝点的锚点
		// myLocationStyle.anchor(0, 0);
		// 设置圆形的边框粗细
		myLocationStyle.strokeWidth(1.0f);
		aMap.setMyLocationStyle(myLocationStyle);
		// 设置定位监听
		aMap.setLocationSource(this);
		// 设置默认定位按钮是否显示
		aMap.getUiSettings().setMyLocationButtonEnabled(false);
		// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		aMap.setMyLocationEnabled(true);
		// 设置点击infoWindow事件监听器
		aMap.setOnInfoWindowClickListener(this);
		// 设置自定义InfoWindow样式
		aMap.setInfoWindowAdapter(this);
		// 设置点击marker事件监听器
		aMap.setOnMarkerClickListener(this);
	}

	@Override
	public void onDestroy() {
		LogUtil.e(TAG, "onDestroy");
		super.onDestroy();
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		mMapView.onDestroy();
	}

	@Override
	public void onResume() {
		LogUtil.e(TAG, "onResume");
		super.onResume();
		// 在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
		mMapView.onResume();
		queryBranchSchoolList();
	}

	@Override
	public void onPause() {
		LogUtil.e(TAG, "onPause");
		super.onPause();
		// 在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
		mMapView.onPause();
		deactivate();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		LogUtil.e(TAG, "onSaveInstanceState");
		super.onSaveInstanceState(outState);
		// 在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState(outState)，实现地图生命周期管理
		mMapView.onSaveInstanceState(outState);
	}

	/** 定位成功后回调函数 */
	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		if (mListener != null && amapLocation != null) {
			if (amapLocation != null && amapLocation.getErrorCode() == 0) {
				// 显示系统小蓝点
				mListener.onLocationChanged(amapLocation);
			} else {
				String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
				Log.e("AmapErr", errText);
			}
		}
	}

	/** 激活定位 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mlocationClient == null) {
			mlocationClient = new AMapLocationClient(act);
			mLocationOption = new AMapLocationClientOption();
			// 设置定位监听
			mlocationClient.setLocationListener(this);
			// 设置为高精度定位模式
			mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
			// 设置定位参数
			mlocationClient.setLocationOption(mLocationOption);
			// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
			// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
			// 在定位结束后，在合适的生命周期调用onDestroy()方法
			// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
			mlocationClient.startLocation();
		}
	}

	/** 停止定位 */
	@Override
	public void deactivate() {
		mListener = null;
		if (mlocationClient != null) {
			mlocationClient.stopLocation();
			mlocationClient.onDestroy();
		}
		mlocationClient = null;
	}

	/** 获取所有分校 */
	private void queryBranchSchoolList() {
		QueryBranchSchoolListRequestData requestData = new QueryBranchSchoolListRequestData();
		requestData.setAccount(account.getAccount());
		requestData.setLongitude(-1);
		requestData.setLatitude(-1);
		requestData.setPageIndex(1);
		requestData.setPageSize(9999);
		String data = new Gson().toJson(requestData);
		new BaseRequestTask() {

			@Override
			protected void onResponse(int code, String msg, String response) {
				try {
					switch (code) {
					case BaseRequestTask.CODE_SUCCESS:
						JSONObject object = new JSONObject(response);
						String listString = object.optString("list");
						List<BranchSchool> list = new Gson().fromJson(listString, new TypeToken<List<BranchSchool>>() {
						}.getType());
						if (list != null && list.size() > 0) {
							addMarkers(list);
						}
						break;
					case BaseRequestTask.CODE_SESSION_ABATE:
						queryBranchSchoolList();
						break;
					default:
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.request(Constants.URL_QUERY_BRANCH_SCHOOL_LIST, data, null, false);
	}

	/** 添加标记 */
	private void addMarkers(List<BranchSchool> list) {
		MarkerOptions options;
		for (BranchSchool branchSchool : list) {
			if (branchSchool.getLatitude() == 0 || branchSchool.getLongitude() == 0) {
				continue;
			}
			// sourceLatLng 待转换坐标点
			converter.coord(new LatLng(branchSchool.getLatitude(), branchSchool.getLongitude()));
			// 执行转换操作
			LatLng desLatLng = converter.convert();
			options = new MarkerOptions();
			options.position(desLatLng);
			options.title(branchSchool.getName());
			options.icon(BitmapDescriptorFactory.fromView(getMyView(branchSchool.getName())));
			aMap.addMarker(options);
		}
	}

	private View getMyView(String markText) {
		View view = View.inflate(act, R.layout.school_marker, null);
		//TextView tvMarker = (TextView) view.findViewById(R.id.tvMarker);
		//tvMarker.setText(markText);
		return view;
	}

	/** 监听自定义infowindow窗口的infowindow事件回调 */
	@Override
	public View getInfoWindow(Marker marker) {
		View infoWindow = View.inflate(act, R.layout.custom_info_window, null);
		render(marker, infoWindow);
		return infoWindow;
	}

	/** 监听自定义infowindow窗口的infocontents事件回调 */
	@Override
	public View getInfoContents(Marker marker) {
		return null;
	}

	/** 自定义infowinfow窗口 */
	public void render(Marker marker, View view) {
		TextView tvTitle = ((TextView) view.findViewById(R.id.title));
		tvTitle.setText(marker.getTitle());
	}

	/** 监听点击infowindow窗口事件回调 */
	@Override
	public void onInfoWindowClick(Marker marker) {
		//((HomeActivity) getActivity()).setSchoolName(marker.getTitle());
		//((HomeActivity) getActivity()).addTrainerListFragment();
	}

	/** 对marker标注点点击响应事件 */
	@Override
	public boolean onMarkerClick(Marker marker) {
		if (TextUtils.isEmpty(marker.getTitle())) {
			return false;
		}
		//((HomeActivity) getActivity()).setSchoolName(marker.getTitle());
		//((HomeActivity) getActivity()).addTrainerListFragment();
		return false;
	}

}
