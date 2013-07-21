package com.example.yunchuangxin.adapter;

import java.util.ArrayList;

import com.example.yunchuangxin.R;
import com.example.yunchuangxin.adapter.CollectGridViewAdapter.ViewHolder;
import com.example.yunchuangxin.bean.GridViewInfo;
import com.example.yunchuangxin.bean.ShopInfo;

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

public class PreferentialListViewAdapter extends BaseAdapter{
	
	private Context ctx;
	private ArrayList<ShopInfo> list;
	
	public PreferentialListViewAdapter ( Context context , ArrayList<ShopInfo> list ) {
		this.ctx = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(ctx.getApplicationContext())
					.inflate(R.layout.preferential_list_item, null);
			holder = new ViewHolder();
			holder.shopPicture = (ImageView) convertView
					.findViewById(R.id.p_shop_pic);
			holder.shopName = (TextView) convertView.findViewById(R.id.p_shop_name);
			holder.shopPftIntroduce = (TextView) convertView.findViewById(R.id.p_shop_pftIntroduce);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Bitmap shopPicBm= list.get(position).getShopPicture(); 
		Drawable shopPicD = new BitmapDrawable(ctx.getResources(), shopPicBm);
		holder.shopPicture.setBackgroundDrawable(shopPicD);
		holder.shopName.setText(list.get(position).getShopName());
		holder.shopPftIntroduce.setText(list.get(position).getShopPftIntroduce());
		
		return convertView;
	}
	
	final class ViewHolder {
		ImageView shopPicture;
		TextView shopName;
		TextView shopPftIntroduce;
	}
	

}
