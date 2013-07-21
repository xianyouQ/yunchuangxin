package com.example.yunchuangxin;

import com.example.yunchuangxin.bean.ShopInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class ShopDetailActivity extends Activity {

	private TextView tel;
	private TextView address;
	private TextView collectBtn;
	private ShopInfo shopInfo;
	private ShopData shopData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		shopData = ShopData.getInstance(this);
//		shopInfo = getShopInfo();

		tel = (TextView) findViewById(R.id.d_shop_phone);
		address = (TextView) findViewById(R.id.d_shop_address);
		collectBtn = (TextView) findViewById(R.id.d_collect_btn);

		tel.setOnClickListener(new MyOnClickListener());
		address.setOnClickListener(new MyOnClickListener());
		collectBtn.setOnClickListener(new MyOnClickListener());

		setContentView(R.layout.shop_detail_activity);
	}

	private class MyOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int id = v.getId();
			switch (id) {
			case R.id.d_shop_address:
				Toast.makeText(ShopDetailActivity.this, "address onClick",
						Toast.LENGTH_SHORT).show();
				break;
			case R.id.d_shop_phone:
				Toast.makeText(ShopDetailActivity.this, "phone onClick",
						Toast.LENGTH_SHORT).show();
				break;
			case R.id.d_collect_btn:
				Toast.makeText(ShopDetailActivity.this, "collectBtn onClick",
						Toast.LENGTH_SHORT).show();
				setCollectInfo();
				break;
			}
		}

	}

	private void setCollectInfo() {

	}

	private ShopInfo getShopInfo() {
		Intent it = getIntent();
		int shopId = it.getIntExtra("shopId", 0);
		ShopInfo shopInfo = shopData.getShopInfo(shopId);
		return shopInfo;
	}
}
