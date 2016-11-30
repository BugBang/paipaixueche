package com.session.dgjp.helper;

import java.util.List;

import com.session.common.BaseRequest;
import com.session.common.BaseResponse;
import com.session.common.utils.LogUtil;
import com.session.dgjp.AppInstance;
import com.session.dgjp.R;
import com.session.dgjp.enity.BranchSchool;
import com.session.dgjp.request.BranchSchoolListRequestData;
import com.session.dgjp.response.BranchSchoolListResponseData;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

/**
 * 分校工具类
 * 
 * @author YJ Liang 2016 上午11:18:31
 */
public class BranchSchoolHelper
{
	private final static String TAG = BranchSchoolHelper.class.getSimpleName();
	private static List<BranchSchool> schools;
	private BranchSchoolListener onBranchSchoolListener;
	private final static int GET_SCHOOLS_SUCCESS = 1;
	private final static int GET_SCHOOLS_FAIL = 2;
	private Handler handler;

	public BranchSchoolHelper()
	{
		super();
	}

	public BranchSchoolHelper(BranchSchoolListener onBranchSchoolListener)
	{
		super();
		this.onBranchSchoolListener = onBranchSchoolListener;
	}


	/**
	 * 同步获取分校列表，该方法需要放在子线程中执行
	 * 
	 * @author YJ Liang 2016 下午5:15:15
	 * @return
	 * @throws Exception
	 */
	public List<BranchSchool> getBranchSchoolsSync(Double longitude, Double latitude) throws Exception
	{
		synchronized (BranchSchoolHelper.class)
		{
			if (schools == null || schools.isEmpty())
			{
				BranchSchoolListRequestData requestData = new BranchSchoolListRequestData();
				requestData.setLatitude(longitude);
				requestData.setLongitude(latitude);
				requestData.setPageSize(Integer.MAX_VALUE);
				BaseRequest request = new BaseRequest(requestData);
				BaseResponse<BranchSchoolListResponseData> response = request.sendRequest(BranchSchoolListResponseData.class);
				schools = response.getResponseData().getList();
			}
			return schools;
		}
	}

	/**
	 * 异步获取分校列表
	 * 
	 * @author YJ Liang 2016 上午9:44:30
	 */
	public void getBranchSchoolsAsync(final Double longitude, final Double latitude)
	{
		if (schools == null || schools.isEmpty())
		{
			if(handler == null)
			{
				handler = new Handler()
				{
					@Override
					public void handleMessage(Message msg)
					{
						switch (msg.what)
						{
							case GET_SCHOOLS_SUCCESS:
								if (onBranchSchoolListener != null)
								{
									onBranchSchoolListener.onGetBranchSchoolsSuccess(schools);
								}
								break;
							case GET_SCHOOLS_FAIL:
								if (onBranchSchoolListener != null)
								{
									BaseResponse<BranchSchoolListResponseData> response = (BaseResponse<BranchSchoolListResponseData>) msg.obj;
									if (response != null)
									{
										onBranchSchoolListener.onGetBranchSchoolsFail(response.getCode(), response.getMsg());
									} else
									{
										onBranchSchoolListener.onGetBranchSchoolsFail(BaseResponse.CODE_UNKNOWN_ERROR, AppInstance.getInstance().getString(R.string.default_error_msg));
									}
								}
								break;
							default:
								super.handleMessage(msg);
								break;
						}
					}

				};
			}
			AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable()
			{
				@Override
				public void run()
				{
					synchronized (BranchSchoolHelper.class)
					{
						BranchSchoolListRequestData requestData = new BranchSchoolListRequestData();
						requestData.setLatitude(longitude);
						requestData.setLongitude(latitude);
						BaseRequest request = new BaseRequest(requestData);
						try
						{
							BaseResponse<BranchSchoolListResponseData> response = request.sendRequest(BranchSchoolListResponseData.class);
							if (response.getCode() == BaseResponse.CODE_SUCCESS)
							{
								schools = response.getResponseData().getList();
								handler.sendEmptyMessage(GET_SCHOOLS_SUCCESS);
							} else
							{
								Message msg = Message.obtain();
								msg.what = GET_SCHOOLS_FAIL;
								msg.obj = response;
								handler.sendMessage(msg);
							}
						} catch (Exception e)
						{
							handler.sendEmptyMessage(GET_SCHOOLS_FAIL);
							LogUtil.e(TAG, e.toString(), e);
						}
					}
				}
			});
		} else
		{
			if (onBranchSchoolListener != null)
			{
				onBranchSchoolListener.onGetBranchSchoolsSuccess(schools);
			}
		}
	}

	public synchronized static List<BranchSchool> getBranchSchools()
	{
		return schools;
	}
	
	public synchronized static void setBranchSchools(List<BranchSchool> schools)
	{
		synchronized (BranchSchoolHelper.class)
		{
			BranchSchoolHelper.schools = schools;
		}
	}

	/**
	 * 获取分校列表的结果回调接口
	 * 
	 * @author YJ Liang 2016 上午9:15:35
	 */
	public static interface BranchSchoolListener
	{
		/**
		 * 获取分校列表成功的回调方法 注意：如果需要对schools的元素个数或循序改变，需要先复制一个新的list，再改新的list
		 * 
		 * @author YJ Liang 2016 上午9:16:09
		 * @param schools
		 */
		public abstract void onGetBranchSchoolsSuccess(List<BranchSchool> schools);

		/**
		 * 获取分校列表失败的回调方法
		 * 
		 * @author YJ Liang 2016 上午9:16:32
		 * @param code
		 * @param msg
		 */
		public abstract void onGetBranchSchoolsFail(int code, String msg);
	}
}
