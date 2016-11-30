package com.session.common;

import com.session.dgjp.R;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

public class BaseDialog extends Dialog {

	public BaseDialog(Context context) {
		super(context);
		setContentView(R.layout.act_home);
	}

	public BaseDialog(Context context, int theme) {
		super(context, theme);
		setContentView(R.layout.act_home);
	}

	public static class Builder {
		private Context mContext;
		private final BaseDialog mDialog;
		private CharSequence mTitle;
		private CharSequence mMessage;
		private CharSequence mPositiveText;
		private CharSequence mNeutralText;
		private CharSequence mNegativeText;
		private DialogInterface.OnClickListener mPositivClickListener;
		private DialogInterface.OnClickListener mNeutralClickListener;
		private DialogInterface.OnClickListener mNegativeClickListener;

		public Builder(Context context) {
			this.mContext = context;
			mDialog = new BaseDialog(mContext, R.style.DialogBase);
		}

		public Builder setMessage(String message) {
			this.mMessage = message;
			return this;
		}

		public Builder setMessage(int message) {
			this.mMessage = mContext.getText(message);
			return this;
		}

		public Builder setTitle(int title) {
			this.mTitle = mContext.getText(title);
			return this;
		}

		public Builder setTitle(String title) {
			this.mTitle = title;
			return this;
		}

		public Builder setPositiveButton(int positiveButtonText, DialogInterface.OnClickListener listener) {
			this.mPositiveText = mContext.getText(positiveButtonText);
			this.mPositivClickListener = listener;
			return this;
		}

		public Builder setPositiveButton(CharSequence positiveButtonText, DialogInterface.OnClickListener listener) {
			this.mPositiveText = positiveButtonText;
			this.mPositivClickListener = listener;
			return this;
		}

		public Builder setNeutralButton(int textId, final OnClickListener listener) {
			this.mNeutralText = mContext.getText(textId);
			this.mNeutralClickListener = listener;
			return this;
		}

		public Builder setNeutralButton(CharSequence text, final OnClickListener listener) {
			this.mNeutralText = text;
			this.mNeutralClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(int negativeButtonText, DialogInterface.OnClickListener listener) {
			this.mNegativeText = mContext.getText(negativeButtonText);
			this.mNegativeClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(CharSequence negativeButtonText, DialogInterface.OnClickListener listener) {
			this.mNegativeText = negativeButtonText;
			this.mNegativeClickListener = listener;
			return this;
		}

		public Builder setButtons(CharSequence positive, CharSequence neutral, CharSequence negative,
				DialogInterface.OnClickListener listener) {
			this.mPositiveText = positive;
			this.mNeutralText = neutral;
			this.mNegativeText = negative;
			this.mPositivClickListener = listener;
			this.mNeutralClickListener = listener;
			this.mNegativeClickListener = listener;
			return this;
		}

		public BaseDialog create() {
			View layout = View.inflate(mContext, R.layout.dialog_base, null);
			mDialog.addContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			TextView tvTitle = (TextView) layout.findViewById(R.id.tvTitle);
			if (null != mTitle) {
				tvTitle.setText(mTitle);
			} else {
				tvTitle.setVisibility(View.GONE);
			}
			TextView tvMsg = (TextView) layout.findViewById(R.id.tvMsg);
			if (mMessage != null) {
				tvMsg.setText(mMessage);
			} else {
				tvMsg.setVisibility(View.GONE);
			}
			if (null == mPositiveText && null == mNeutralText && null == mNegativeText) {
				// 没有按钮的时候点击Dialog隐藏Dialog
				layout.setOnClickListener(mListener);
				layout.findViewById(R.id.llDialogButton).setVisibility(View.GONE);
				mDialog.setContentView(layout);
				return mDialog;
			} else {
				layout.findViewById(R.id.llDialogButton).setVisibility(View.VISIBLE);
			}
			Button btnPositive = (Button) layout.findViewById(R.id.btnPositive);
			if (mPositiveText != null) {
				btnPositive.setText(mPositiveText);
				if (mPositivClickListener != null) {
					btnPositive.setOnClickListener(mListener);
				}
			} else {
				btnPositive.setVisibility(View.GONE);
			}
			Button btnNeutral = (Button) layout.findViewById(R.id.btnNeutral);
			if (mNeutralText != null) {
				btnNeutral.setText(mPositiveText);
				if (mNeutralClickListener != null) {
					btnNeutral.setOnClickListener(mListener);
				}
			} else {
				btnNeutral.setVisibility(View.GONE);
			}
			Button btnNegative = (Button) layout.findViewById(R.id.btnNegative);
			if (mNegativeText != null) {
				btnNegative.setText(mNegativeText);
				if (mNegativeClickListener != null) {
					btnNegative.setOnClickListener(mListener);
				}
			} else {
				btnNegative.setVisibility(View.GONE);
			}
			mDialog.setContentView(layout);
			return mDialog;
		}

		private View.OnClickListener mListener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.btnPositive:
					mPositivClickListener.onClick(mDialog, DialogInterface.BUTTON_POSITIVE);
					break;
				case R.id.btnNeutral:
					mNeutralClickListener.onClick(mDialog, DialogInterface.BUTTON_NEUTRAL);
					break;
				case R.id.btnNegative:
					mNegativeClickListener.onClick(mDialog, DialogInterface.BUTTON_NEGATIVE);
					break;
				default:
					mDialog.dismiss();
					break;
				}
			}
		};
	}

}
