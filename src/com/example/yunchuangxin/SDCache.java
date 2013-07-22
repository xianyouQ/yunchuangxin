package com.example.yunchuangxin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.preference.PreferenceManager;

import com.example.yunchuangxin.bean.ShopInfo;

/**
 * @author OnePiece 缓存管理类，用于管理缓存
 * @格式 
 *     图片存在SD卡，名字为店铺ID。店铺其他信息以特定格式存sharedPreference，例如shopId+name作为取出shopName的key
 */
public class SDCache {

	private static SDCache mInstance = null;
	private SharedPreferences shopPref = null;
	private Editor editor = null;

	private SDCache(Context ctx) {
		shopPref = PreferenceManager.getDefaultSharedPreferences(ctx);
		editor = shopPref.edit();
	}

	public static SDCache getInstance(Context ctx) {
		if (mInstance == null) {
			mInstance = new SDCache(ctx);
		}
		return mInstance;
	}

	/**
	 * @param shopId
	 *            商店ID
	 * @return 包含所有商店信息的实体类
	 */
	public ShopInfo getCacheShopInfo(int shopId) {
		ShopInfo shopInfo = new ShopInfo();
		// 设置key值
		String nameKey = shopId + "name";
		String telKey = shopId + "tel";
		String addressKey = shopId + "address";
		String introduceKey = shopId + "introduce";
		String pftIntroduceKey = shopId + "pftIntroduce";
		// PicSD卡位置
		String picPath = Environment.getExternalStorageDirectory() + "/"
				+ Constants.APP_FOLDER_NAME + "/" + shopId + Constants.PIC_EXT;
		// 获得数据
		String shopName = shopPref.getString(nameKey, null);
		String shopTel = shopPref.getString(telKey, null);
		String shopAddress = shopPref.getString(addressKey, null);
		String shopIntroduce = shopPref.getString(introduceKey, null);
		String shopPftIntroduce = shopPref.getString(pftIntroduceKey, null);
		Bitmap shopPicture = getCacheShopPic(picPath);
		// 放到类中
		shopInfo.setShopId(shopId);
		shopInfo.setShopName(shopName);
		shopInfo.setShopTel(shopTel);
		shopInfo.setShopAddress(shopAddress);
		shopInfo.setShopIntroduce(shopIntroduce);
		shopInfo.setShopPftIntroduce(shopPftIntroduce);
		shopInfo.setShopPicture(shopPicture);
		return shopInfo;
	}

	/**
	 * @param picPath
	 *            图片在SD卡保存路径
	 * @return 店铺图片
	 */
	private Bitmap getCacheShopPic(String picPath) {
		try {
			File shopPic = new File(picPath);
			InputStream inputStream = null;
			inputStream = new FileInputStream(shopPic);
			Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
			return bitmap;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @param shopInfoList
	 *            将要保存在本地的shopInfoList
	 * @功能 将店铺信息保存在本地
	 */
	public void setCacheShopInfo(ArrayList<ShopInfo> shopInfoList) {
		for (int i = 0; i < shopInfoList.size(); i++) {
			ShopInfo shopInfo = shopInfoList.get(i);
			int shopId = shopInfo.getShopId();
			// 设置key值
			String nameKey = shopId + "name";
			String telKey = shopId + "tel";
			String addressKey = shopId + "address";
			String introduceKey = shopId + "introduce";
			String pftIntroduceKey = shopId + "pftIntroduce";
			editor.putString(nameKey, shopInfo.getShopName());
			editor.putString(telKey, shopInfo.getShopTel());
			editor.putString(addressKey, shopInfo.getShopAddress());
			editor.putString(introduceKey, shopInfo.getShopIntroduce());
			editor.putString(pftIntroduceKey, shopInfo.getShopPftIntroduce());
			// PicSD卡位置
			String picPath = Environment.getExternalStorageDirectory() + "/"
					+ Constants.APP_FOLDER_NAME + "/" + shopId
					+ Constants.PIC_EXT;
			FileOutputStream out = null;
			Bitmap shopPic = shopInfo.getShopPicture();
			try {
				out = new FileOutputStream(picPath);
				shopPic.compress(Bitmap.CompressFormat.JPEG, 100, out);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					out.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				out = null;
			}
		}
		editor.commit();

	}

	/**
	 * @param updateTime
	 *            服务器获取的更新时间，用于判断是否需要更新
	 * @return 最新数据返回true， 不是最新数据返回false
	 */
	public boolean isTheLatestCacheData(String updateTime) {
		String locUpdateTime = shopPref.getString(Constants.UPDATE_TIME_KEY,
				null);
		if (updateTime.equals(locUpdateTime)) {
			return true;
		} else
			return false;
	}

	/**
	 * @return 返回一个存储ShopID的ArrayList
	 */
	public ArrayList<Integer> getCacheShopId() {
		ArrayList<Integer> shopId = new ArrayList<Integer>();
		for (int i = 0; i < 10; i++) {
			String locCacheIdKey = Constants.LOCCACHEIDPRE + i;
			int id = shopPref.getInt(locCacheIdKey, 0);
			// ID=0表示没有缓存到本地
			if (id != 0) {
				shopId.set(i, id);
			} else {
				break;
			}
		}
		return shopId;
	}

	/**
	 * @param shopId
	 *            商店ID
	 * @功能 将商店的ID号保存到本地，用于查询本地缓存,最多10个
	 */
	public void setCacheShopId(ArrayList<Integer> shopId) {
		for (int i = 0; i < 10; i++) {
			if (shopId.get(i) != null) {
				editor.putInt(Constants.LOCCACHEIDPRE + i, (int) shopId.get(i));
			}
		}
		editor.commit();
	}

}
