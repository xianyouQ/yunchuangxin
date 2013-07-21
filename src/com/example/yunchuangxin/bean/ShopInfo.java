package com.example.yunchuangxin.bean;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * @author OnePiece
 * @param shopId
 *            商店id
 * @param shopName
 *            商店名字
 * @param shopTel
 *            商店电话
 * @param shopAddress
 *            商店地址
 * @param shopPicture
 *            商店图片
 * @param shopIntroduce
 *            商店介绍
 * @param shopPftIntroduce
 *            商店优惠信息
 */
public class ShopInfo {

	private int shopId;
	private String shopName;
	private String shopTel;
	private String shopAddress;
	private Bitmap shopPicture;
	private String shopIntroduce;
	private String shopPftIntroduce;

	public int getShopId() {
		return shopId;
	}

	public void setShopId(int shopId) {
		this.shopId = shopId;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getShopTel() {
		return shopTel;
	}

	public void setShopTel(String shopTel) {
		this.shopTel = shopTel;
	}

	public String getShopAddress() {
		return shopAddress;
	}

	public void setShopAddress(String shopAddress) {
		this.shopAddress = shopAddress;
	}

	public Bitmap getShopPicture() {
		return shopPicture;
	}

	public void setShopPicture(Bitmap shopPicture) {
		this.shopPicture = shopPicture;
	}

	public String getShopIntroduce() {
		return shopIntroduce;
	}

	public void setShopIntroduce(String shopIntroduce) {
		this.shopIntroduce = shopIntroduce;
	}

	public String getShopPftIntroduce() {
		return shopPftIntroduce;
	}

	public void setShopPftIntroduce(String shopPftIntroduce) {
		this.shopPftIntroduce = shopPftIntroduce;
	}

}
