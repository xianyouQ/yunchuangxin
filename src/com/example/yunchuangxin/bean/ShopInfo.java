package com.example.yunchuangxin.bean;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * @author OnePiece
 * @param shopId
 *            �̵�id
 * @param shopName
 *            �̵�����
 * @param shopTel
 *            �̵�绰
 * @param shopAddress
 *            �̵��ַ
 * @param shopPicture
 *            �̵�ͼƬ
 * @param shopIntroduce
 *            �̵����
 * @param shopPftIntroduce
 *            �̵��Ż���Ϣ
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
