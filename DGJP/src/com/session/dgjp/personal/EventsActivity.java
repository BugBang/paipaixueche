package com.session.dgjp.personal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.session.common.utils.SharedPreferencesUtil;
import com.session.common.utils.TextUtil;
import com.session.dgjp.R;

/**
 * Created by user on 2017-02-09.
 */
public class EventsActivity extends Activity implements AMap.OnCameraChangeListener {
    private AMap aMap;
    private MapView mapView;
    private MarkerOptions mMarkerOptions;
    private ImageView mBack;
    private String mFormatAddress;
//    private Marker mMarker;
    public static final int LOCATION_SUCCESS = 2;
    public static final int LOCATION_FAIL = 3;
    private int id;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events_activity);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        id = getIntent().getIntExtra("id", -1);
        position = getIntent().getIntExtra("position", -1);
        initLocal();
        init();
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLatitude, mLongitude), 19));

        //        mMarkerOptions = new MarkerOptions();
        //        mMarkerOptions.position(new LatLng(mLatitude, mLongitude));
        //        mMarkerOptions.title("当前位置");
        //        mMarkerOptions.visible(true);
        //        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.location_marker));
        //        mMarkerOptions.icon(bitmapDescriptor);
        //        aMap.addMarker(mMarkerOptions);

    }

    private float mLatitude;
    private float mLongitude;

    private void initLocal() {
        mLatitude = SharedPreferencesUtil.getFloat(SharedPreferencesUtil.Latitude, -1f);
        mLongitude = SharedPreferencesUtil.getFloat(SharedPreferencesUtil.Longitude, -1f);
    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
        mBack = (ImageView) findViewById(R.id.iv_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (id != -1 && !TextUtil.isEmpty(mFormatAddress)) {
                    Intent intent = new Intent();
                    intent.putExtra("id", id);
                    intent.putExtra("position", position);
                    intent.putExtra("address", mFormatAddress);
                    setResult(LOCATION_SUCCESS,intent);
                    finish();
                }else {
                    setResult(LOCATION_FAIL);
                    finish();
                }
            }
        });
    }

    /**
     * amap添加一些事件监听器
     */
    private void setUpMap() {
        aMap.setOnCameraChangeListener(this);// 对amap添加移动地图事件监听器
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    /**
     * 对正在移动地图事件回调
     */
    @Override
    public void onCameraChange(final CameraPosition cameraPosition) {
        aMap.clear();
        GeocodeSearch geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {

            @Override
            public void onGeocodeSearched(GeocodeResult result, int rCode) {
            }

            @Override
            public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
                mFormatAddress = result.getRegeocodeAddress().getFormatAddress();
            }
        });
        LatLonPoint lp = new LatLonPoint(cameraPosition.target.latitude, cameraPosition.target.longitude);
        RegeocodeQuery query = new RegeocodeQuery(lp, 200, GeocodeSearch.AMAP);
        geocoderSearch.getFromLocationAsyn(query);

    }

    /**
     * 对移动地图结束事件回调
     *
     * @Override
     */
    public void onCameraChangeFinish(CameraPosition cameraPosition) {

        if (!TextUtil.isEmpty(mFormatAddress)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(EventsActivity.this, "已选中 : "+mFormatAddress, Toast.LENGTH_LONG).show();
                }
            }, 500);

        }
        //        GeocodeSearch geocoderSearch = new GeocodeSearch(this);
        //        geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener(){
        //
        //            @Override
        //            public void onGeocodeSearched(GeocodeResult result, int rCode) {
        //            }
        //
        //            @Override
        //            public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        //                String formatAddress = result.getRegeocodeAddress().getFormatAddress();
        //                Logger.i("formatAddress:"+formatAddress);
        //            }});
        //        LatLonPoint lp = new LatLonPoint(cameraPosition.target.latitude,cameraPosition.target.longitude);
        //        RegeocodeQuery query = new RegeocodeQuery(lp, 200,GeocodeSearch.AMAP);
        //        geocoderSearch.getFromLocationAsyn(query);
    }
}

