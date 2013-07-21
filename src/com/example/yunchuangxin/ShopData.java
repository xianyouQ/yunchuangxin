package com.example.yunchuangxin;

import com.example.yunchuangxin.bean.ShopInfo;

import android.content.Context;

public class ShopData {
	
	private static ShopData mInstance;
	private Context ctx;
	
	private ShopData ( Context ctx ){
		this.ctx = ctx;
	}
	
	public static ShopData getInstance ( Context ctx ) {
		if (mInstance == null) {
			mInstance = new ShopData(ctx);
		}
		return mInstance;
	}
	
	public ShopInfo getShopInfo( int shopId ) {
		return null;
	}

}
