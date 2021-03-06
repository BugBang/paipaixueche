package com.session.dgjp.login;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.session.common.BaseActivity;
import com.session.common.BaseDialog;
import com.session.common.BaseRequestTask;
import com.session.common.utils.NumberUtil;
import com.session.common.utils.SharedPreferencesUtil;
import com.session.dgjp.AppInstance;
import com.session.dgjp.CacheValues;
import com.session.dgjp.Constants;
import com.session.dgjp.MainActivity;
import com.session.dgjp.R;
import com.session.dgjp.enity.Account;
import com.session.dgjp.enity.BranchSchool;
import com.session.dgjp.enity.City;
import com.session.dgjp.request.GetAllCityRequestData;
import com.session.dgjp.request.GetCityBranchSchoolListRequestData;
import com.session.dgjp.request.GetVerifyCodeRequestData;
import com.session.dgjp.request.LoginRequestData;
import com.session.dgjp.request.RegisterRequestData;

import org.json.JSONObject;

import java.util.List;

/** 注册 */
public class RegisterActivity extends BaseActivity {

	private EditText etName, etIdCard, etPhone, etVerifyCode;
	private String name, idCard, phone, verifyCode;
	private long cityId, branchSchoolId;
	private TextView tvRegion, tvBranchSchool;
	private final String REGEX_ID = "^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$";
	private Button btnCaptcha, btnRegister;
	private final int TOTAL_TIME = 120;
	private int totalTime;

	@Override
	protected void init(Bundle savedInstanceState) {
		setContentView(R.layout.act_register);
		initTitle(R.string.register);
		etName = (EditText) findViewById(R.id.etName);
		etIdCard = (EditText) findViewById(R.id.etIdCard);
		etPhone = (EditText) findViewById(R.id.etPhone);
		etVerifyCode = (EditText) findViewById(R.id.etVerifyCode);
		tvRegion = (TextView) findViewById(R.id.tvRegion);
		tvRegion.setOnClickListener(this);
		tvBranchSchool = (TextView) findViewById(R.id.tvBranchSchool);
		tvBranchSchool.setOnClickListener(this);
		btnCaptcha = (Button) findViewById(R.id.btnCaptcha);
		btnCaptcha.setOnClickListener(this);
		btnRegister = (Button) findViewById(R.id.btnRegister);
		btnRegister.setOnClickListener(this);
	}

	/** 获取所有城市 */
	private void getAllCity() {
		ProgressDialog progressDialog = buildProcessDialog(null, "正在查询...", false);
		GetAllCityRequestData requestData = new GetAllCityRequestData();
		String data = new Gson().toJson(requestData);
		new BaseRequestTask() {

			@Override
			protected void onResponse(int code, String msg, String response) {
				try {
					switch (code) {
					case BaseRequestTask.CODE_SUCCESS:
						JSONObject object = new JSONObject(response);
						String listString = object.optString("list");
						final List<City> list = new Gson().fromJson(listString, new TypeToken<List<City>>() {
						}.getType());
						if (list != null && list.size() > 0) {
							CacheValues.setCities(list);
							showCities(list);
						}
						break;
					case BaseRequestTask.CODE_SESSION_ABATE:
						getAllCity();
						break;
					default:
						toastShort(msg);
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
					toastShort("网络异常，请稍后重试");
				}
			}
		}.request(Constants.URL_GET_ALL_CITY, data, progressDialog, true);
	}
	
	/** 获取指定城市下的分校 */
	private void getCityBranchSchoolList() {
		ProgressDialog progressDialog = buildProcessDialog(null, "正在查询...", false);
		GetCityBranchSchoolListRequestData requestData = new GetCityBranchSchoolListRequestData();
		requestData.setCityId(cityId);
		String data = new Gson().toJson(requestData);
		new BaseRequestTask() {
			
			@Override
			protected void onResponse(int code, String msg, String response) {
				try {
					switch (code) {
					case BaseRequestTask.CODE_SUCCESS:
						JSONObject object = new JSONObject(response);
						String listString = object.optString("list");
						final List<BranchSchool> list = new Gson().fromJson(listString, new TypeToken<List<BranchSchool>>() {
						}.getType());
						if (list != null && list.size() > 0) {
							CacheValues.setBranchSchools(cityId, list);
							showSchool(list);
						}
						break;
					case BaseRequestTask.CODE_SESSION_ABATE:
						getCityBranchSchoolList();
						break;
					default:
						toastShort(msg);
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
					toastShort("网络异常，请稍后重试");
				}
			}
		}.request(Constants.URL_GET_CITY_BRANCH_SCHOOL_LIST, data, progressDialog, true);
	}
	
	/** 弹出选择城市对话框 */
	private void showCities(final List<City> cities) {
		new ChooseCityDialog(ctx, cities) {
			
			@Override
			protected void onChoose(long id, String name) {
				if (cityId != id) {
					tvRegion.setText(name);
					cityId = id;
					branchSchoolId = 0;
					tvBranchSchool.setText("");
					tvBranchSchool.setHint("请选择报名分校");
				}
			}
		}.show();
	}
	
	/** 弹出选择分校对话框 */
	private void showSchool(final List<BranchSchool> schools) {
		new ChooseBranchSchoolDialog(ctx, schools) {
			
			@Override
			protected void onChoose(long id, String name) {
				tvBranchSchool.setText(name);
				branchSchoolId = id;
			}
		}.show();
	}

	/** 获取验证码 */
	private void getVerifyCode() {
		btnCaptcha.setEnabled(false);
		ProgressDialog progressDialog = buildProcessDialog(null, "正在获取验证码...", false);
		GetVerifyCodeRequestData requestData = new GetVerifyCodeRequestData();
		requestData.setAccount(phone);
		requestData.setAuthType(GetVerifyCodeRequestData.REGISTER);
		final String data = new Gson().toJson(requestData);
		new BaseRequestTask() {

			@Override
			protected void onResponse(int code, String msg, String response) {
				try {
					switch (code) {
					case BaseRequestTask.CODE_SUCCESS:
						toastShort("验证码发送到" + phone.substring(7, 11) + "手机，请注意查收！");
						totalTime = TOTAL_TIME;
						new CountDownTimer(TOTAL_TIME * 1000, 1000) {

							@Override
							public void onTick(long millisUntilFinished) {
								btnCaptcha.setText("获取验证码" + "(" + (--totalTime) + ")");
								btnCaptcha.setBackgroundResource(R.drawable.btn_white_enable);
								btnCaptcha.setTextColor(getResources().getColor(R.color.text_hint));
							}

							@Override
							public void onFinish() {
								btnCaptcha.setText("获取验证码");
								btnCaptcha.setBackgroundResource(R.drawable.btn_white);
								btnCaptcha.setTextColor(getResources().getColor(R.color.text_blue));
								btnCaptcha.setEnabled(true);
								totalTime = TOTAL_TIME;
							}
						}.start();
						break;
					case BaseRequestTask.CODE_SESSION_ABATE:
						getVerifyCode();
						break;
					default:
						toastShort(msg);
						btnCaptcha.setEnabled(true);
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
					toastShort("网络异常，请稍后重试");
					btnCaptcha.setEnabled(true);
				}
			}
		}.request(Constants.URL_GET_VERIFY_CODE, data, progressDialog, true);
	}

	/** 注册 */
	private void register() {
		if (!check()) {
			return;
		}
		btnRegister.setEnabled(false);
		ProgressDialog progressDialog = buildProcessDialog(null, "正在注册...", false);
		RegisterRequestData requestData = new RegisterRequestData();
		requestData.setName(name);
		requestData.setIdcard(idCard);
		requestData.setPhone(phone);
		requestData.setVerifyCode(verifyCode);
		requestData.setBranchSchoolId(branchSchoolId);
		String data = new Gson().toJson(requestData);
		new BaseRequestTask() {

			@Override
			protected void onResponse(int code, String msg, String response) {
				try {
					switch (code) {
					case BaseRequestTask.CODE_SUCCESS:
						toastShort("注册成功");
						login();
						break;
					case BaseRequestTask.CODE_SESSION_ABATE:
						register();
						break;
					default:
						toastShort(msg);
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
					toastShort("网络异常，请稍后重试");
				} finally {
					btnRegister.setEnabled(true);
				}
			}
		}.request(Constants.URL_REGISTER, data, progressDialog, true);
	}
	
	/** 登录 */
	private void login() {
		ProgressDialog progressDialog = buildProcessDialog(null, "正在登录...", false);
		LoginRequestData requestData = new LoginRequestData();
		requestData.setAccount(phone);
		requestData.setPassword(verifyCode);
		String data = new Gson().toJson(requestData);
		new BaseRequestTask() {

			@Override
			protected void onResponse(int code, String msg, String response) {
				try {
					switch (code) {
					case BaseRequestTask.CODE_SUCCESS:
						Account account = new Gson().fromJson(response, Account.class);
						if (account != null) {
							SharedPreferencesUtil.saveString(SharedPreferencesUtil.KEY_ACCOUNT, phone);
							SharedPreferencesUtil.saveString(SharedPreferencesUtil.KEY_PASSWORD, verifyCode);
							SharedPreferencesUtil.saveString(SharedPreferencesUtil.KEY_ACCOUNT_ACCOUNT, account.getAccount());
							AppInstance.getInstance().setAccount(account);
							gotoHome();
						} else {
							toastShort("登录失败，请稍后重试");
							finish();
						}
						break;
					case BaseRequestTask.CODE_SESSION_ABATE:
						login();
						break;
					default:
						toastShort(msg);
						finish();
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
					toastShort("网络异常，请稍后重试");
					finish();
				}
			}
		}.request(Constants.URL_LOGIN, data, progressDialog, true);
	}

	/** 进入主页 */
	private void gotoHome() {
		Intent in = new Intent(ctx, MainActivity.class);
		in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(in);
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tvRegion: // 选择所在地区
			List<City> cities = CacheValues.getCities();
			if (cities != null) {
				showCities(cities);
			} else {
				getAllCity();
			}
			break;
		case R.id.tvBranchSchool: // 选择报名分校
			if (cityId == 0) {
				toastShort("请选择所在地区");
				return;
			}
			List<BranchSchool> branchSchools = CacheValues.getBranchSchools(cityId);
			if (branchSchools != null) {
				showSchool(branchSchools);
			} else {
				getCityBranchSchoolList();
			}
			break;
		case R.id.btnCaptcha: // 获取验证码
			if (checkPhone()) {
				getVerifyCode();
			}
			break;
		case R.id.btnRegister: // 注册
			register();
			break;
		default:
			super.onClick(v);
		}
	}

	/** 校验手机号码 */
	private boolean checkPhone() {
		phone = etPhone.getText().toString().trim();
		if (TextUtils.isEmpty(phone)) {
			toastShort(R.string.toast_input_phone);
			etPhone.requestFocus();
			return false;
		} else if (phone.length() != 11) {
			toastShort(R.string.toast_input_right_phone);
			etPhone.requestFocus();
			return false;
		} else {
			if (!NumberUtil.isCellPhone(phone)) {
				toastShort(R.string.toast_input_right_phone);
				return false;
			}
		}
		return true;
	}

	/** 校验 */
	private boolean check() {
		name = etName.getText().toString().trim();
		idCard = etIdCard.getText().toString().trim();
		idCard = etIdCard.getText().toString().trim();
		verifyCode = etVerifyCode.getText().toString().trim();
		if (TextUtils.isEmpty(name)) {
			toastShort("请输入姓名");
			etName.requestFocus();
			return false;
		}
		if (TextUtils.isEmpty(idCard)) {
			toastShort("请输入身份证号码");
			etIdCard.requestFocus();
			return false;
		} else {
			boolean matches = idCard.matches(REGEX_ID);
			if (!matches) {
				toastShort("身份证号码格式有误，请检查");
				etIdCard.requestFocus();
				return false;
			}
		}
		if (branchSchoolId == 0) {
			toastShort("请选择报名分校");
			return false;
		}
		if (!checkPhone()) {
			return false;
		}
		if (TextUtils.isEmpty(verifyCode)) {
			toastShort("请输入验证码");
			etVerifyCode.requestFocus();
			return false;
		}
		return true;
	}

	@Override
	public void onBackPressed() {
		BaseDialog dialog = new BaseDialog.Builder(ctx).setTitle("提示").setMessage("您确定要取消注册吗？").setPositiveButton("确定", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				finish();
			}
		}).setNegativeButton("取消", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		}).create();
		dialog.show();
	}

}
