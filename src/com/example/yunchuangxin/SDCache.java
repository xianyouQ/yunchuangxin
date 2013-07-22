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
 * @author OnePiece ��������࣬���ڹ�����
 * @��ʽ 
 *     ͼƬ����SD��������Ϊ����ID������������Ϣ���ض���ʽ��sharedPreference������shopId+name��Ϊȡ��shopName��key
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
	 *            �̵�ID
	 * @return ���������̵���Ϣ��ʵ����
	 */
	public ShopInfo getCacheShopInfo(int shopId) {
		ShopInfo shopInfo = new ShopInfo();
		// ����keyֵ
		String nameKey = shopId + "name";
		String telKey = shopId + "tel";
		String addressKey = shopId + "address";
		String introduceKey = shopId + "introduce";
		String pftIntroduceKey = shopId + "pftIntroduce";
		// PicSD��λ��
		String picPath = Environment.getExternalStorageDirectory() + "/"
				+ Constants.APP_FOLDER_NAME + "/" + shopId + Constants.PIC_EXT;
		// �������
		String shopName = shopPref.getString(nameKey, null);
		String shopTel = shopPref.getString(telKey, null);
		String shopAddress = shopPref.getString(addressKey, null);
		String shopIntroduce = shopPref.getString(introduceKey, null);
		String shopPftIntroduce = shopPref.getString(pftIntroduceKey, null);
		Bitmap shopPicture = getCacheShopPic(picPath);
		// �ŵ�����
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
	 *            ͼƬ��SD������·��
	 * @return ����ͼƬ
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
	 *            ��Ҫ�����ڱ��ص�shopInfoList
	 * @���� ��������Ϣ�����ڱ���
	 */
	public void setCacheShopInfo(ArrayList<ShopInfo> shopInfoList) {
		for (int i = 0; i < shopInfoList.size(); i++) {
			ShopInfo shopInfo = shopInfoList.get(i);
			int shopId = shopInfo.getShopId();
			// ����keyֵ
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
			// PicSD��λ��
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
	 *            ��������ȡ�ĸ���ʱ�䣬�����ж��Ƿ���Ҫ����
	 * @return �������ݷ���true�� �����������ݷ���false
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
	 * @return ����һ���洢ShopID��ArrayList
	 */
	public ArrayList<Integer> getCacheShopId() {
		ArrayList<Integer> shopId = new ArrayList<Integer>();
		for (int i = 0; i < 10; i++) {
			String locCacheIdKey = Constants.LOCCACHEIDPRE + i;
			int id = shopPref.getInt(locCacheIdKey, 0);
			// ID=0��ʾû�л��浽����
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
	 *            �̵�ID
	 * @���� ���̵��ID�ű��浽���أ����ڲ�ѯ���ػ���,���10��
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
