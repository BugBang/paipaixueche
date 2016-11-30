package com.session.dgjp.sign;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.session.common.BXBaseAdapter;
import com.session.common.ViewHolder;
import com.session.dgjp.Constants;
import com.session.dgjp.R;
import com.session.dgjp.enity.SignSchoolList;
import com.session.dgjp.view.StarBar;

import java.util.ArrayList;
import java.util.List;

public class SignSchoolListAdapter extends BXBaseAdapter<SignSchoolList.SignSchoolListModel> implements Filterable {

    //过滤相关
    /**
     * This lock is also used by the filter
     * (see {@link #getFilter()} to make a synchronized copy of
     * the original array of data.
     * 过滤器上的锁可以同步复制原始数据。
     */
    private final Object mLock = new Object();

    // A copy of the original mObjects array, initialized from and then used instead as soon as
    // the mFilter ArrayFilter is used. mObjects will then only contain the filtered values.
    //对象数组的备份，当调用ArrayFilter的时候初始化和使用。此时，对象数组只包含已经过滤的数据。
    private ArrayList<SignSchoolList.SignSchoolListModel> mOriginalValues;
    private ArrayFilter mFilter;

    public SignSchoolListAdapter(List<SignSchoolList.SignSchoolListModel> listModel, Activity activity) {
        super(listModel, activity);
    }

    @Override
    public View getItemView(int position, View convertView, ViewGroup parent, SignSchoolList.SignSchoolListModel model) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.sign_school_list_item, null);
        }
        ImageView mIvClassPhoto = ViewHolder.get(convertView, R.id.iv_school_photo);
        TextView mTvSchoolTitle = ViewHolder.get(convertView, R.id.tv_school_title);
        TextView mSchoolLength = ViewHolder.get(convertView, R.id.tv_length);
        StarBar mStartBar = ViewHolder.get(convertView, R.id.starBar);
        TextView mTvSchoolSignNum = ViewHolder.get(convertView, R.id.tv_sign_num);
        TextView mTvSchoolAddress = ViewHolder.get(convertView, R.id.tv_school_address);
        TextView mTvSchoolTuition = ViewHolder.get(convertView, R.id.tv_tuition);
        if (model != null) {
            Glide.with(mActivity).load(Constants.TEST_URL + model.getSmallPhotoUrl()).into(mIvClassPhoto);
            mTvSchoolTitle.setText(mListModel.get(position).getName());
            //            if (TextUtil.isEmpty(mListModel.get(position).getDistance())){
            //                mSchoolLength.setText("未知");
            //            }else {
            mSchoolLength.setText("距离 " + mListModel.get(position).getDistance() + "千米");
            //            }
            mStartBar.setStarMark(mListModel.get(position).getScore());
            mTvSchoolSignNum.setText(mListModel.get(position).getCountNum() + "人报名");
            mTvSchoolAddress.setText(mListModel.get(position).getAddress());
            mTvSchoolTuition.setText("￥"+mListModel.get(position).getExpenses()+"元起");
        }
        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }


    private class ArrayFilter extends Filter {
        //执行刷选
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();//过滤的结果
            //原始数据备份为空时，上锁，同步复制原始数据
            if (mOriginalValues == null) {
                synchronized (mLock) {
                    mOriginalValues = new ArrayList<>(mListModel);
                }
            }
            //当首字母为空时
            if (prefix == null || prefix.length() == 0) {
                ArrayList<SignSchoolList.SignSchoolListModel> list;
                synchronized (mLock) {//同步复制一个原始备份数据
                    list = new ArrayList<>(mOriginalValues);
                }
                results.values = list;
                results.count = list.size();//此时返回的results就是原始的数据，不进行过滤
            } else {
                String prefixString = prefix.toString().toLowerCase();//转化为小写

                ArrayList<SignSchoolList.SignSchoolListModel> values;
                synchronized (mLock) {//同步复制一个原始备份数据
                    values = new ArrayList<>(mOriginalValues);
                }
                final int count = values.size();
                final ArrayList<SignSchoolList.SignSchoolListModel> newValues = new ArrayList<>();

                for (int i = 0; i < count; i++) {
                    final SignSchoolList.SignSchoolListModel value = values.get(i);//从List<User>中拿到User对象

                    //                    final String valueText = value.toString().toLowerCase();
                    final String valueText = value.getName().toLowerCase();//User对象的name属性作为过滤的参数
                    // First match against the whole, non-splitted value
                    if (valueText.startsWith(prefixString) || valueText.indexOf(prefixString.toString()) != -1) {//第一个字符是否匹配
                        newValues.add(value);//将这个item加入到数组对象中
                    } else {//处理首字符是空格
                        final String[] words = valueText.split(" ");
                        final int wordCount = words.length;

                        // Start at index 0, in case valueText starts with space(s)
                        for (int k = 0; k < wordCount; k++) {
                            if (words[k].startsWith(prefixString)) {//一旦找到匹配的就break，跳出for循环
                                newValues.add(value);
                                break;
                            }
                        }
                    }
                }
                results.values = newValues;//此时的results就是过滤后的List<User>数组
                results.count = newValues.size();
            }
            return results;
        }

        //刷选结果
        @Override
        protected void publishResults(CharSequence prefix, FilterResults results) {
            //noinspection unchecked
            mListModel = (List<SignSchoolList.SignSchoolListModel>) results.values;//此时，Adapter数据源就是过滤后的Results
            if (results.count > 0) {
                notifyDataSetChanged();//这个相当于从mDatas中删除了一些数据，只是数据的变化，故使用notifyDataSetChanged()
            } else {
                /**
                 * 数据容器变化 ----> notifyDataSetInValidated
                 容器中的数据变化  ---->  notifyDataSetChanged
                 */
                notifyDataSetInvalidated();//当results.count<=0时，此时数据源就是重新new出来的，说明原始的数据源已经失效了
            }
        }
    }
}
