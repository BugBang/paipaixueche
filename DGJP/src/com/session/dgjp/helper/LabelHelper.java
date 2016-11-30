package com.session.dgjp.helper;

import java.util.List;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.session.common.BaseRequest;
import com.session.common.BaseResponse;
import com.session.common.utils.LogUtil;
import com.session.dgjp.AppInstance;
import com.session.dgjp.R;
import com.session.dgjp.enity.Label;
import com.session.dgjp.request.LabelListRequestData;
import com.session.dgjp.response.LabelListResponseData;

/**
 * 评价标签工具类
 * 
 * @author YJ Liang 2016 下午4:45:48
 */
public class LabelHelper
{
	private final static String TAG = LabelHelper.class.getSimpleName();
	private static List<Label> labels;
	private LabelListener labelListener;
	private final static int GET_LABELS_SUCCESS = 1;
	private final static int GET_LABELS_FAIL = 2;
	private Handler handler;

	public LabelHelper()
	{
		super();
	}

	public LabelHelper(LabelListener labelListener)
	{
		super();
		this.labelListener = labelListener;
	}

	/**
	 * 同步获取标签列表，该方法需要放在子线程中执行
	 * 
	 * @author YJ Liang 2016 下午5:06:08
	 * @return
	 * @throws Exception
	 */
	public List<Label> getLabelsSync() throws Exception
	{
		synchronized (LabelHelper.class)
		{
			if (labels == null || labels.isEmpty())
			{
				LabelListRequestData requestData = new LabelListRequestData();
				BaseRequest request = new BaseRequest(requestData);
				BaseResponse<LabelListResponseData> response = request.sendRequest(LabelListResponseData.class);
				labels = response.getResponseData().getList();
			}
			return labels;
		}
	}

	/**
	 * 异步获取标签列表
	 * 
	 * @author YJ Liang 2016 上午9:44:30
	 */
	public void getLabelsAsync()
	{
		if (labels == null || labels.isEmpty())
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
						case GET_LABELS_SUCCESS:
							if (labelListener != null)
							{
								labelListener.onGetLabelsSuccess(labels);
							}
							break;
						case GET_LABELS_FAIL:
							if (labelListener != null)
							{
								BaseResponse<LabelListResponseData> response = (BaseResponse<LabelListResponseData>) msg.obj;
								if (response != null)
								{
									labelListener.onGetLabelsFail(response.getCode(), response.getMsg());
								} else
								{
									labelListener.onGetLabelsFail(BaseResponse.CODE_UNKNOWN_ERROR, AppInstance.getInstance().getString(R.string.default_error_msg));
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
					synchronized (LabelHelper.class)
					{
						LabelListRequestData requestData = new LabelListRequestData();
						BaseRequest request = new BaseRequest(requestData);
						try
						{
							BaseResponse<LabelListResponseData> response = request.sendRequest(LabelListResponseData.class);
							if (response.getCode() == BaseResponse.CODE_SUCCESS)
							{
								labels = response.getResponseData().getList();
								handler.sendEmptyMessage(GET_LABELS_SUCCESS);
							} else
							{
								Message msg = Message.obtain();
								msg.what = GET_LABELS_FAIL;
								msg.obj = response;
								handler.sendMessage(msg);
							}
						} catch (Exception e)
						{
							handler.sendEmptyMessage(GET_LABELS_FAIL);
							LogUtil.e(TAG, e.toString(), e);
						}
					}
				}
			});
		} else
		{
			if (labelListener != null)
			{
				labelListener.onGetLabelsSuccess(labels);
			}
		}
	}

	public synchronized static List<Label> getLabels()
	{
		return labels;
	}
	
	public synchronized static void setLabels(List<Label> labels)
	{
		synchronized (LabelHelper.class)
		{
			LabelHelper.labels = labels;
		}
	}

	/**
	 * 获取标签列表的结果回调接口
	 * 
	 * @author YJ Liang 2016 上午9:15:35
	 */
	public static interface LabelListener
	{
		/**
		 * 获取标签列表成功的回调方法 注意：如果需要对labels的元素个数或循序改变，需要先复制一个新的list，再改新的list
		 * 
		 * @author YJ Liang 2016 上午9:16:09
		 * @param labels
		 */
		public abstract void onGetLabelsSuccess(List<Label> labels);

		/**
		 * 获取标签列表失败的回调方法
		 * 
		 * @author YJ Liang 2016 上午9:16:32
		 * @param code
		 * @param msg
		 */
		public abstract void onGetLabelsFail(int code, String msg);
	}
}
