package com.example.yunchuangxin;

import java.util.ArrayList;

import com.example.yunchuangxin.adapter.PreferentialListViewAdapter;
import com.example.yunchuangxin.bean.ShopInfo;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;

public class PreferentialActivity extends Activity {

	private ListView listView;
	private ArrayList<ShopInfo> dataList;
	private SDCache sdCache;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preferential_activity);

		sdCache = SDCache.getInstance(this);

		listView = (ListView) findViewById(R.id.preferential_list);

		dataList = getDataList();

		listView.setAdapter(new PreferentialListViewAdapter(this, dataList));
		listView.setOnItemClickListener(new MyListViewOnItemClickListener());
	}

	//test数据
	private ArrayList<ShopInfo> getDataList() {
		ArrayList<ShopInfo> list = new ArrayList<ShopInfo>();
		ShopInfo info1 = new ShopInfo();
		info1.setShopPftIntroduce("shop activities");
		info1.setShopName("shopName");
		Drawable shopPicD = getResources().getDrawable(R.drawable.shop_pic);
		BitmapDrawable bd = (BitmapDrawable) shopPicD;
		Bitmap bm = bd.getBitmap();
		info1.setShopPicture(bm);
		list.add(info1);
		list.add(info1);
		list.add(info1);
		list.add(info1);
		list.add(info1);
		list.add(info1);
		list.add(info1);
		list.add(info1);
		list.add(info1);
		list.add(info1);
		return list;
	}

	/**
	 * @return 返回装有本地shopInfo的List
	 */
	private ArrayList<ShopInfo> getCacheDataList() {
		ArrayList<ShopInfo> list = new ArrayList<ShopInfo>();
		ArrayList<Integer> shopIdList = sdCache.getCacheShopId();

		for (int i = 0; i < shopIdList.size(); i++) {
			ShopInfo shopInfo = new ShopInfo();
			shopInfo = sdCache.getCacheShopInfo(shopIdList.get(i));
			list.add(shopInfo);
		}

		return list;
	}

	/**
	 * @return 返回从网络下载的shopInfo的List
	 */
	private ArrayList<ShopInfo> getOnlineDataList() {
		return null;
	}

	private class MyListViewOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			Intent it = new Intent(PreferentialActivity.this,
					ShopDetailActivity.class);
			startActivity(it);
		}

	}

}
