package com.example.yunchuangxin;

import java.util.ArrayList;

import com.example.yunchuangxin.adapter.CollectGridViewAdapter;
import com.example.yunchuangxin.bean.GridViewInfo;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class CollectActivity extends Activity{
	
	private GridView collectGridView;
	private ArrayList<GridViewInfo> list;
	private PopupWindow popWindow;
	private CollectGridViewAdapter gridViewAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.collect_activity);
		LinearLayout itemMenu = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.collect_gridview_menu, null);
		for ( int i = 0; i < itemMenu.getChildCount(); i++) {
			itemMenu.getChildAt(i).setOnClickListener(new ItemMenuFunctionOnClickListener());
		}
		popWindow = new PopupWindow(itemMenu,LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		popWindow.setFocusable(true);
		popWindow.setBackgroundDrawable(new BitmapDrawable());
		
		collectGridView = (GridView) findViewById(R.id.collect_gridview);
		
		list = getDataList();
		gridViewAdapter = new CollectGridViewAdapter(this, list);
		
		collectGridView.setAdapter( gridViewAdapter );
		collectGridView.setOnItemClickListener(new MyGridViewItemOnClickListener());
		collectGridView.setOnItemLongClickListener(new MyGridViewOnItemLongClickListener());
		
	}
	
	//testData
	private ArrayList<GridViewInfo> getDataList() {
		ArrayList<GridViewInfo> dataList = new ArrayList<GridViewInfo>();
		
		GridViewInfo info1 = new GridViewInfo();
		String n1 = "name1";
		Drawable pic1D = getResources().getDrawable(R.drawable.gridview_item_bg);
		BitmapDrawable bd = (BitmapDrawable) pic1D;
		Bitmap pic1 = bd.getBitmap();
		info1.setShopName(n1);
		info1.setShopPicture(pic1);
		
		GridViewInfo info2 = new GridViewInfo();
		String n2 = "name2";
		info2.setShopName(n2);
		info2.setShopPicture(pic1);
		GridViewInfo info3 = new GridViewInfo();
		String n3 = "name3";
		info3.setShopName(n3);
		info3.setShopPicture(pic1);
		dataList.add(info1);
		dataList.add(info2);
		dataList.add(info3);

		
		return dataList;
	}
	
	private ArrayList<GridViewInfo> getCacheCollectDataList() {
		return null;
	}
	
	private class MyGridViewItemOnClickListener implements OnItemClickListener {

		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			// TODO Auto-generated method stub
			int id = arg1.getId();
			for ( int i = 0; i < list.size(); i++ ) {
				if ( id == i ) {
					Toast.makeText(CollectActivity.this, i+" is onclick", Toast.LENGTH_SHORT).show();
					Intent it = new Intent(CollectActivity.this, ShopDetailActivity.class);
					it.putExtra("shopId", list.get(position).getShopId());
					startActivity(it);
				}
			}
		}
		
	}
	
	private int longClickPosition;
	
	private class MyGridViewOnItemLongClickListener implements OnItemLongClickListener {

		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			// TODO Auto-generated method stub
			int id = arg1.getId();
			for ( int i = 0; i < list.size(); i++ ) {
				if ( id == i ) {
					Toast.makeText(CollectActivity.this, i+" is onlongclick", Toast.LENGTH_SHORT).show();
					popWindow.showAtLocation(collectGridView, Gravity.CENTER_VERTICAL, 0, 0);
					longClickPosition = arg2;
				}
			}
			return false;
		}
		
	}
	
	private class ItemMenuFunctionOnClickListener implements OnClickListener {

		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			int id = arg0.getId();
			switch (id) {
			case R.id.item_menu_func_del:
				Toast.makeText(CollectActivity.this, " del onclick" +longClickPosition, Toast.LENGTH_SHORT).show();
				list.remove(longClickPosition);
				gridViewAdapter.notifyDataSetChanged();
				break;
			}
			if ( popWindow.isShowing() ) {
				popWindow.dismiss();
			}
		}
		
	}

}
