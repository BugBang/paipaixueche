package com.session.dgjp.trainer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.session.common.utils.PhotoUtil;
import com.session.dgjp.R;
import com.session.dgjp.enity.BranchSchool;
import com.session.dgjp.enity.Trainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TrainerAdapter extends BaseAdapter implements Filterable
{
	private final static String TAG = TrainerAdapter.class.getSimpleName();
	private List<Trainer> list;
	private List<Trainer> filteredList;
	private Context context;
	private LayoutInflater inflater;
	private OrderListener onBookListener;
	private Filter filter;

	public TrainerAdapter(Context context, OrderListener onBookListener)
	{
		super();
		this.context = context;
		this.onBookListener = onBookListener;
		inflater = LayoutInflater.from(context);
	}

	public List<Trainer> getList()
	{
		return list;
	}

	public void setList(List<Trainer> list)
	{
		this.list = list;
	}

	@Override
	public int getCount()
	{
		return filteredList != null ? filteredList.size() : 0;
	}

	@Override
	public Trainer getItem(int position)
	{
		return filteredList.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder = null;
		if (convertView == null)
		{
			convertView = inflater.inflate(R.layout.trainer_item, parent, false);
			ImageView headIv = (ImageView) convertView.findViewById(R.id.ivHead);
			TextView schoolTv = (TextView) convertView.findViewById(R.id.school);
			TextView trainerTv = (TextView) convertView.findViewById(R.id.trainer);
			TextView timesTv = (TextView) convertView.findViewById(R.id.times);
			RatingBar ratingBar = (RatingBar) convertView.findViewById(R.id.rating_bar);
			TextView operationTv = (TextView) convertView.findViewById(R.id.operation);
			TextView evalTv = (TextView) convertView.findViewById(R.id.eval);
			ImageView hintIv = (ImageView)convertView.findViewById(R.id.hint);

			operationTv.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View view)
				{
					if(onBookListener != null)
					{
						onBookListener.onOrder((Trainer)view.getTag());
					}
				}
			});
			holder = new ViewHolder(headIv, hintIv, schoolTv, trainerTv, timesTv, operationTv, ratingBar, evalTv);
			convertView.setTag(holder);
		} else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		Trainer item = filteredList.get(position);
		BranchSchool branchSchool = item.getBranchSchool();
		holder.getSchoolTv().setText(branchSchool != null ? branchSchool.getName() : "");
		holder.getTrainerTv().setText(context.getString(R.string.trainer_name, item.getName()));
		holder.getTimesTv().setText(String.valueOf(item.getOrderTimes()));
		holder.getRatingBar().setRating(item.getEval());
		holder.getEvalTv().setText(context.getString(R.string.eval, item.getEval()));
		holder.getOperationTv().setTag(item);

		PhotoUtil.showCirclePhoto(context, holder.getHeadIv(), item.getPhotoUrl(), R.drawable.img_def_head);
		ImageView hintIv = holder.getHintIv();
		if(Trainer.TYPE_LATEST.equals(item.getTrainerType()))
		{
			hintIv.setImageResource(R.drawable.last_order);
			hintIv.setVisibility(View.VISIBLE);
		}else {
			hintIv.setVisibility(View.INVISIBLE);
		}
		return convertView;
	}

	private static class ViewHolder
	{
		private ImageView headIv, hintIv;
		private TextView schoolTv, trainerTv, timesTv, operationTv, evalTv;
		private RatingBar ratingBar;

		public ViewHolder(ImageView headIv, ImageView hintIv, TextView schoolTv, TextView trainerTv, TextView timesTv, TextView operationTv, RatingBar ratingBar,TextView evalTv)
		{
			super();
			this.headIv = headIv;
			this.hintIv = hintIv;
			this.schoolTv = schoolTv;
			this.trainerTv = trainerTv;
			this.timesTv = timesTv;
			this.operationTv = operationTv;
			this.ratingBar = ratingBar;
			this.evalTv = evalTv;
		}

		public ImageView getHeadIv()
		{
			return headIv;
		}

		public TextView getTimesTv()
		{
			return timesTv;
		}

		public TextView getTrainerTv()
		{
			return trainerTv;
		}

		public TextView getSchoolTv()
		{
			return schoolTv;
		}

		public TextView getOperationTv()
		{
			return operationTv;
		}

		public RatingBar getRatingBar()
		{
			return ratingBar;
		}

		public TextView getEvalTv()
		{
			return evalTv;
		}

		public ImageView getHintIv() {
			return hintIv;
		}
		
	}

	/**
	 * 预约按钮的回调接口
	 * @author YJ Liang
	 * 2016  上午8:55:35
	 */
	public interface OrderListener{
		public abstract void onOrder(Trainer trainer);
	}

	@Override
	public Filter getFilter()
	{
		if(filter == null)
		{
			filter = new Filter(){
				
				@Override
				protected FilterResults performFiltering(CharSequence constraint) {
					FilterResults results = new FilterResults();
					List<Trainer> filteredList = null;
					String constraintStr = null;
					if (constraint == null || (constraintStr = constraint.toString().trim()).isEmpty()) {
						filteredList = list;
					} else {
						constraint = constraintStr.toUpperCase(Locale.SIMPLIFIED_CHINESE);
						filteredList = new ArrayList<Trainer>();
						if (list != null) {
							int size = list.size();
							for (int i = 0; i < size; i++) {
								Trainer item = list.get(i);
								String name = item.getName();
								if ((name != null && name.toUpperCase(
										Locale.SIMPLIFIED_CHINESE).contains(
										constraint))) {
									filteredList.add(item);
								}
							}
						}
					}
					results.values = filteredList;
					if(filteredList != null)
					{
						results.count = filteredList.size();
					}else{
						results.count = 0;
					}
					return results;
				}

				@Override
				protected void publishResults(CharSequence constraint,
						FilterResults results) {
					filteredList = (List<Trainer>) results.values;
					notifyDataSetChanged();
				}};
		}
		return filter;
	}

}
