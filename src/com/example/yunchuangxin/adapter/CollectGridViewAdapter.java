package com.example.yunchuangxin.adapter;

import java.util.ArrayList;

import com.example.yunchuangxin.R;
import com.example.yunchuangxin.bean.GridViewInfo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CollectGridViewAdapter extends BaseAdapter {

	ArrayList<GridViewInfo> list;
	Context ctx;

	public CollectGridViewAdapter(Context context, ArrayList<GridViewInfo> list) {
		this.ctx = context;
		this.list = list;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(ctx.getApplicationContext())
					.inflate(R.layout.collect_gridview_item, null);
			holder = new ViewHolder();
			holder.shopPicture = (ImageView) convertView
					.findViewById(R.id.shop_picture);
			holder.shopName = (TextView) convertView
					.findViewById(R.id.shop_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Bitmap shopPicBm = list.get(position).getShopPicture();
		Drawable shopPicD = new BitmapDrawable(ctx.getResources(), shopPicBm);
		holder.shopPicture.setBackgroundDrawable((shopPicD));
		holder.shopName.setText(list.get(position).getShopName());
		convertView.setId(position);

		return convertView;
	}

	final class ViewHolder {
		ImageView shopPicture;
		TextView shopName;
	}

}
