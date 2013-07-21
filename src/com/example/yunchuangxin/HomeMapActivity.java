package com.example.yunchuangxin;

import java.util.ArrayList;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class HomeMapActivity extends Activity {

	// ��λ���
	LocationClient mLocClient;
	LocationData locData = null;
	public MyLocationListenner myListener = new MyLocationListenner();

	// ��λͼ��
	MyLocationOverlay myLocationOverlay = null;
	// ��������ͼ��

	double mLon1 = 116.400244;
	double mLat1 = 39.963175;
	double mLon2 = 116.369199;
	double mLat2 = 39.942821;
	double mLon3 = 116.425541;
	double mLat3 = 39.939723;
	double mLon4 = 116.401394;
	double mLat4 = 39.906965;
	double mLon5 = 116.402096;
	double mLat5 = 39.942057;

	// ��ͼ��أ�ʹ�ü̳�MapView��MyLocationMapViewĿ������дtouch�¼�ʵ�����ݴ���
	// ���������touch�¼���������̳У�ֱ��ʹ��MapView����
	MapView mMapView = null; // ��ͼView
	private MapController mMapController = null;

	// UI���
	OnCheckedChangeListener radioButtonListener = null;
	Button requestLocButton = null;
	TextView classifyButton = null;
	LinearLayout classFood = null;
	LinearLayout classKtv = null;
	private PopupWindow popWindow = null;
	boolean isRequest = false;// �Ƿ��ֶ���������λ
	boolean isFirstLoc = true;// �Ƿ��״ζ�λ
	boolean isLocationClientStop = false;

	private MyOverlay mOverlay = null;
	private PopupOverlay pop = null;
	private ArrayList<OverlayItem> mItems = null;
	private TextView popupText = null;
	private View viewCache = null;
	private View popupInfo = null;
	private View popupLeft = null;
	private View popupRight = null;
	private Button button = null;
	private MapView.LayoutParams layoutParam = null;
	private OverlayItem mCurItem = null;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_map_activity);
		//���൯��
		View popView = LayoutInflater.from(this).inflate(R.layout.classify_pop,
				null);
		popWindow = new PopupWindow(popView, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		popWindow.setFocusable(true);
		popWindow.setBackgroundDrawable(new BitmapDrawable());

		requestLocButton = (Button) findViewById(R.id.RequestLocbutton);
		classifyButton = (TextView) findViewById(R.id.classifyButton);
		classFood = (LinearLayout) popView.findViewById(R.id.class_food);
		classKtv = (LinearLayout) popView.findViewById(R.id.class_ktv);

		OnClickListener btnClickListener = new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				int id = v.getId();
				switch (id) {
				case R.id.RequestLocbutton:
					// �ֶ���λ����
					requestLocClick();
					break;
				case R.id.classifyButton:
					// ��������˵�
					popWindow.showAsDropDown(classifyButton);
					break;
				case R.id.class_food:
					Toast.makeText(HomeMapActivity.this, "food onclick", Toast.LENGTH_SHORT).show();
					popWindow.dismiss();
					break;
				case R.id.class_ktv:
					Toast.makeText(HomeMapActivity.this, "ktv onclick", Toast.LENGTH_SHORT).show();
					popWindow.dismiss();
					break;
				}
			}
		};
		requestLocButton.setOnClickListener(btnClickListener);
		classifyButton.setOnClickListener(btnClickListener);
		classFood.setOnClickListener(btnClickListener);
		classKtv.setOnClickListener(btnClickListener);

		// ��ͼ��ʼ��
		mMapView = (MapView) findViewById(R.id.bmapView);
		mMapController = mMapView.getController();
		//���ó�ʼ����λ��
		mMapController.setCenter(new GeoPoint(
				(int) (30.68162 * 1e6),
				(int) (104.107002 * 1e6)));
		mMapView.getController().setZoom(14);
		mMapView.getController().enableClick(true);
		mMapView.setBuiltInZoomControls(false);
		// ���� ��������ͼ��

		// ��λ��ʼ��
		mLocClient = new LocationClient(getApplicationContext());
		locData = new LocationData();
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// ��gps
		option.setCoorType("bd09ll"); // ������������
		option.setScanSpan(5000);
		mLocClient.setLocOption(option);
		mLocClient.start();

		// ��λͼ���ʼ��
		myLocationOverlay = new MyLocationOverlay(mMapView);
		// ���ö�λ����
		myLocationOverlay.setData(locData);
		// ��Ӷ�λͼ��
		mMapView.getOverlays().add(myLocationOverlay);
		myLocationOverlay.enableCompass();
		// �޸Ķ�λ���ݺ�ˢ��ͼ����Ч

		mMapView.refresh();
		initOverlay();

	}

	/**
	 * �ֶ�����һ�ζ�λ����
	 */
	public void requestLocClick() {
		isRequest = true;
		mLocClient.requestLocation();
		Toast.makeText(HomeMapActivity.this, "���ڶ�λ��", Toast.LENGTH_SHORT)
				.show();
	}

	/**
	 * �޸�λ��ͼ��
	 * 
	 * @param marker
	 */
	public void modifyLocationOverlayIcon(Drawable marker) {
		// ������markerΪnullʱ��ʹ��Ĭ��ͼ�����
		myLocationOverlay.setMarker(marker);
		// �޸�ͼ�㣬��Ҫˢ��MapView��Ч
		mMapView.refresh();
	}

	/**
	 * ������������ͼ��
	 */

	/**
	 * ��λSDK��������
	 */
	public class MyLocationListenner implements BDLocationListener {

		public void onReceiveLocation(BDLocation location) {
			if (location == null || isLocationClientStop) {
				System.out.println("kong   gps  info");
				return;
			}
			System.out.println(" gps  info");
			locData.latitude = location.getLatitude();
			locData.longitude = location.getLongitude();
			// �������ʾ��λ����Ȧ����accuracy��ֵΪ0����
			locData.accuracy = location.getRadius();
			locData.direction = location.getDerect();
			// ���¶�λ����
			myLocationOverlay.setData(locData);
			// ����ͼ������ִ��ˢ�º���Ч
			mMapView.refresh();
			// ���ֶ�����������״ζ�λʱ���ƶ�����λ��
			if (isRequest || isFirstLoc) {
				// �ƶ���ͼ����λ��
				mMapController.animateTo(new GeoPoint(
						(int) (locData.latitude * 1e6),
						(int) (locData.longitude * 1e6)));
				isRequest = false;
			}
			// �״ζ�λ���
			isFirstLoc = false;
		}

		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}
		}
	}

	// �̳�MyLocationOverlay��дdispatchTapʵ�ֵ������

	public void initOverlay() {
		/**
		 * �����Զ���overlay
		 */
		mOverlay = new MyOverlay(getResources().getDrawable(
				R.drawable.icon_marka), mMapView);
		/**
		 * ׼��overlay ����
		 */
		GeoPoint p1 = new GeoPoint((int) (mLat1 * 1E6), (int) (mLon1 * 1E6));
		OverlayItem item1 = new OverlayItem(p1, "������1", "");
		/**
		 * ����overlayͼ�꣬�粻���ã���ʹ�ô���ItemizedOverlayʱ��Ĭ��ͼ��.
		 */
		item1.setMarker(getResources().getDrawable(R.drawable.icon_marka));

		GeoPoint p2 = new GeoPoint((int) (mLat2 * 1E6), (int) (mLon2 * 1E6));
		OverlayItem item2 = new OverlayItem(p2, "������2", "");
		item2.setMarker(getResources().getDrawable(R.drawable.icon_markb));

		GeoPoint p3 = new GeoPoint((int) (mLat3 * 1E6), (int) (mLon3 * 1E6));
		OverlayItem item3 = new OverlayItem(p3, "������3", "");
		item3.setMarker(getResources().getDrawable(R.drawable.icon_markc));

		GeoPoint p4 = new GeoPoint((int) (mLat4 * 1E6), (int) (mLon4 * 1E6));
		OverlayItem item4 = new OverlayItem(p4, "������4", "");
		item4.setMarker(getResources().getDrawable(R.drawable.icon_markd));

		GeoPoint p5 = new GeoPoint((int) (mLat5 * 1E6), (int) (mLon5 * 1E6));
		OverlayItem item5 = new OverlayItem(p5, "������5", "");
		item5.setMarker(getResources().getDrawable(R.drawable.icon_gcoding));
		/**
		 * ��item ��ӵ�overlay�� ע�⣺ ͬһ��itmeֻ��addһ��
		 */
		mOverlay.addItem(item1);
		mOverlay.addItem(item2);
		mOverlay.addItem(item3);
		mOverlay.addItem(item4);
		mOverlay.addItem(item5);
		/**
		 * ��������item���Ա�overlay��reset���������
		 */
		mItems = new ArrayList<OverlayItem>();
		mItems.addAll(mOverlay.getAllItem());
		/**
		 * ��overlay �����MapView��
		 */
		mMapView.getOverlays().add(mOverlay);
		/**
		 * ˢ�µ�ͼ
		 */
		mMapView.refresh();

		/**
		 * ���ͼ����Զ���View.
		 */

		viewCache = getLayoutInflater()
				.inflate(R.layout.custom_text_view, null);
		popupInfo = (View) viewCache.findViewById(R.id.popinfo);
		popupLeft = (View) viewCache.findViewById(R.id.popleft);
		popupRight = (View) viewCache.findViewById(R.id.popright);
		popupText = (TextView) viewCache.findViewById(R.id.textcache);

		button = new Button(this);
		button.setBackgroundResource(R.drawable.popup);

		/**
		 * ����һ��popupoverlay
		 */
		PopupClickListener popListener = new PopupClickListener() {

			public void onClickedPopup(int index) {
				if (index == 0) {
					// ����itemλ��
					pop.hidePop();
					GeoPoint p = new GeoPoint(mCurItem.getPoint()
							.getLatitudeE6() + 5000, mCurItem.getPoint()
							.getLongitudeE6() + 5000);
					mCurItem.setGeoPoint(p);
					mOverlay.updateItem(mCurItem);
					mMapView.refresh();
				} else if (index == 2) {
					// ����ͼ��
					mCurItem.setMarker(getResources().getDrawable(
							R.drawable.nav_turn_via_1));
					mOverlay.updateItem(mCurItem);
					mMapView.refresh();
				}
			}
		};
		pop = new PopupOverlay(mMapView, popListener);

	}

	/**
	 * �������Overlay
	 * 
	 * @param view
	 */
	public void clearOverlay(View view) {
		mOverlay.removeAll();
		if (pop != null) {
			pop.hidePop();
		}
		mMapView.removeView(button);
		mMapView.refresh();
	}

	/**
	 * �������Overlay
	 * 
	 * @param view
	 */
	public void resetOverlay(View view) {
		clearOverlay(null);
		// ����add overlay
		mOverlay.addItem(mItems);
		mMapView.refresh();
	}

	protected void onPause() {
		isLocationClientStop = true;
		mMapView.onPause();
		super.onPause();
	}

	protected void onResume() {
		isLocationClientStop = false;
		mMapView.onResume();
		super.onResume();
	}

	protected void onDestroy() {
		// �˳�ʱ���ٶ�λ
		if (mLocClient != null)
			mLocClient.stop();
		isLocationClientStop = true;
		mMapView.destroy();
		super.onDestroy();
	}

	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);

	}

	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mMapView.onRestoreInstanceState(savedInstanceState);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public class MyOverlay extends ItemizedOverlay {

		public MyOverlay(Drawable defaultMarker, MapView mapView) {
			super(defaultMarker, mapView);
		}

		public boolean onTap(int index) {
			OverlayItem item = getItem(index);
			mCurItem = item;
			if (index == 4) {
				button.setText("����һ��ϵͳ�ؼ�");
				GeoPoint pt = new GeoPoint((int) (mLat5 * 1E6),
						(int) (mLon5 * 1E6));
				// �������ֲ���
				layoutParam = new MapView.LayoutParams(
				// �ؼ���,�̳���ViewGroup.LayoutParams
						MapView.LayoutParams.WRAP_CONTENT,
						// �ؼ���,�̳���ViewGroup.LayoutParams
						MapView.LayoutParams.WRAP_CONTENT,
						// ʹ�ؼ��̶���ĳ������λ��
						pt, 0, -32,
						// �ؼ����뷽ʽ
						MapView.LayoutParams.BOTTOM_CENTER);
				// ���View��MapView��
				mMapView.addView(button, layoutParam);
			} else {
				popupText.setText(getItem(index).getTitle());
				Bitmap[] bitMaps = { BMapUtil.getBitmapFromView(popupLeft),
						BMapUtil.getBitmapFromView(popupInfo),
						BMapUtil.getBitmapFromView(popupRight) };
				pop.showPopup(bitMaps, item.getPoint(), 32);
			}
			return true;
		}

		public boolean onTap(GeoPoint pt, MapView mMapView) {
			if (pop != null) {
				pop.hidePop();
				mMapView.removeView(button);
			}
			return false;
		}

	}

}
