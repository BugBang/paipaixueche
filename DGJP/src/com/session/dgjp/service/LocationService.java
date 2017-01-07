package com.session.dgjp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.session.common.utils.SharedPreferencesUtil;

/**
 * Created by user on 2016-10-08.
 */
public class LocationService extends Service implements AMapLocationListener {

    public AMapLocationClient mlocationClient = null;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        init();
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        if(mlocationClient!=null){
            mlocationClient.onDestroy();
            mlocationClient = null;
            mLocationOption = null;
        }
        super.onDestroy();
    }

    private void init() {
        mlocationClient = new AMapLocationClient(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setOnceLocation(true);
        //设置定位监听
        mlocationClient.setLocationListener(this);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        //        mLocationOption.setInterval(2000);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
        mlocationClient.startLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                float latitude = (float) amapLocation.getLatitude();
                float longitude = (float) amapLocation.getLongitude();
                SharedPreferencesUtil.saveFloat(SharedPreferencesUtil.Latitude, latitude);
                SharedPreferencesUtil.saveFloat(SharedPreferencesUtil.Longitude, longitude);
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError","location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
                SharedPreferencesUtil.saveFloat(SharedPreferencesUtil.Latitude, -1f);
                SharedPreferencesUtil.saveFloat(SharedPreferencesUtil.Longitude, -1f);
            }
        }
    }

}
