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

	// 定位相关
	LocationClient mLocClient;
	LocationData locData = null;
	public MyLocationListenner myListener = new MyLocationListenner();

	// 定位图层
	MyLocationOverlay myLocationOverlay = null;
	// 弹出泡泡图层

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

	// 地图相关，使用继承MapView的MyLocationMapView目的是重写touch事件实现泡泡处理
	// 如果不处理touch事件，则无需继承，直接使用MapView即可
	MapView mMapView = null; // 地图View
	private MapController mMapController = null;

	// UI相关
	OnCheckedChangeListener radioButtonListener = null;
	Button requestLocButton = null;
	TextView classifyButton = null;
	LinearLayout classFood = null;
	LinearLayout classKtv = null;
	private PopupWindow popWindow = null;
	boolean isRequest = false;// 是否手动触发请求定位
	boolean isFirstLoc = true;// 是否首次定位
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
		//分类弹窗
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
					// 手动定位请求
					requestLocClick();
					break;
				case R.id.classifyButton:
					// 弹出分类菜单
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

		// 地图初始化
		mMapView = (MapView) findViewById(R.id.bmapView);
		mMapController = mMapView.getController();
		//设置初始中心位置
		mMapController.setCenter(new GeoPoint(
				(int) (30.68162 * 1e6),
				(int) (104.107002 * 1e6)));
		mMapView.getController().setZoom(14);
		mMapView.getController().enableClick(true);
		mMapView.setBuiltInZoomControls(false);
		// 创建 弹出泡泡图层

		// 定位初始化
		mLocClient = new LocationClient(getApplicationContext());
		locData = new LocationData();
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(5000);
		mLocClient.setLocOption(option);
		mLocClient.start();

		// 定位图层初始化
		myLocationOverlay = new MyLocationOverlay(mMapView);
		// 设置定位数据
		myLocationOverlay.setData(locData);
		// 添加定位图层
		mMapView.getOverlays().add(myLocationOverlay);
		myLocationOverlay.enableCompass();
		// 修改定位数据后刷新图层生效

		mMapView.refresh();
		initOverlay();

	}

	/**
	 * 手动触发一次定位请求
	 */
	public void requestLocClick() {
		isRequest = true;
		mLocClient.requestLocation();
		Toast.makeText(HomeMapActivity.this, "正在定位…", Toast.LENGTH_SHORT)
				.show();
	}

	/**
	 * 修改位置图标
	 * 
	 * @param marker
	 */
	public void modifyLocationOverlayIcon(Drawable marker) {
		// 当传入marker为null时，使用默认图标绘制
		myLocationOverlay.setMarker(marker);
		// 修改图层，需要刷新MapView生效
		mMapView.refresh();
	}

	/**
	 * 创建弹出泡泡图层
	 */

	/**
	 * 定位SDK监听函数
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
			// 如果不显示定位精度圈，将accuracy赋值为0即可
			locData.accuracy = location.getRadius();
			locData.direction = location.getDerect();
			// 更新定位数据
			myLocationOverlay.setData(locData);
			// 更新图层数据执行刷新后生效
			mMapView.refresh();
			// 是手动触发请求或首次定位时，移动到定位点
			if (isRequest || isFirstLoc) {
				// 移动地图到定位点
				mMapController.animateTo(new GeoPoint(
						(int) (locData.latitude * 1e6),
						(int) (locData.longitude * 1e6)));
				isRequest = false;
			}
			// 首次定位完成
			isFirstLoc = false;
		}

		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}
		}
	}

	// 继承MyLocationOverlay重写dispatchTap实现点击处理

	public void initOverlay() {
		/**
		 * 创建自定义overlay
		 */
		mOverlay = new MyOverlay(getResources().getDrawable(
				R.drawable.icon_marka), mMapView);
		/**
		 * 准备overlay 数据
		 */
		GeoPoint p1 = new GeoPoint((int) (mLat1 * 1E6), (int) (mLon1 * 1E6));
		OverlayItem item1 = new OverlayItem(p1, "覆盖物1", "");
		/**
		 * 设置overlay图标，如不设置，则使用创建ItemizedOverlay时的默认图标.
		 */
		item1.setMarker(getResources().getDrawable(R.drawable.icon_marka));

		GeoPoint p2 = new GeoPoint((int) (mLat2 * 1E6), (int) (mLon2 * 1E6));
		OverlayItem item2 = new OverlayItem(p2, "覆盖物2", "");
		item2.setMarker(getResources().getDrawable(R.drawable.icon_markb));

		GeoPoint p3 = new GeoPoint((int) (mLat3 * 1E6), (int) (mLon3 * 1E6));
		OverlayItem item3 = new OverlayItem(p3, "覆盖物3", "");
		item3.setMarker(getResources().getDrawable(R.drawable.icon_markc));

		GeoPoint p4 = new GeoPoint((int) (mLat4 * 1E6), (int) (mLon4 * 1E6));
		OverlayItem item4 = new OverlayItem(p4, "覆盖物4", "");
		item4.setMarker(getResources().getDrawable(R.drawable.icon_markd));

		GeoPoint p5 = new GeoPoint((int) (mLat5 * 1E6), (int) (mLon5 * 1E6));
		OverlayItem item5 = new OverlayItem(p5, "覆盖物5", "");
		item5.setMarker(getResources().getDrawable(R.drawable.icon_gcoding));
		/**
		 * 将item 添加到overlay中 注意： 同一个itme只能add一次
		 */
		mOverlay.addItem(item1);
		mOverlay.addItem(item2);
		mOverlay.addItem(item3);
		mOverlay.addItem(item4);
		mOverlay.addItem(item5);
		/**
		 * 保存所有item，以便overlay在reset后重新添加
		 */
		mItems = new ArrayList<OverlayItem>();
		mItems.addAll(mOverlay.getAllItem());
		/**
		 * 将overlay 添加至MapView中
		 */
		mMapView.getOverlays().add(mOverlay);
		/**
		 * 刷新地图
		 */
		mMapView.refresh();

		/**
		 * 向地图添加自定义View.
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
		 * 创建一个popupoverlay
		 */
		PopupClickListener popListener = new PopupClickListener() {

			public void onClickedPopup(int index) {
				if (index == 0) {
					// 更新item位置
					pop.hidePop();
					GeoPoint p = new GeoPoint(mCurItem.getPoint()
							.getLatitudeE6() + 5000, mCurItem.getPoint()
							.getLongitudeE6() + 5000);
					mCurItem.setGeoPoint(p);
					mOverlay.updateItem(mCurItem);
					mMapView.refresh();
				} else if (index == 2) {
					// 更新图标
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
	 * 清除所有Overlay
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
	 * 重新添加Overlay
	 * 
	 * @param view
	 */
	public void resetOverlay(View view) {
		clearOverlay(null);
		// 重新add overlay
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
		// 退出时销毁定位
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
				button.setText("这是一个系统控件");
				GeoPoint pt = new GeoPoint((int) (mLat5 * 1E6),
						(int) (mLon5 * 1E6));
				// 创建布局参数
				layoutParam = new MapView.LayoutParams(
				// 控件宽,继承自ViewGroup.LayoutParams
						MapView.LayoutParams.WRAP_CONTENT,
						// 控件高,继承自ViewGroup.LayoutParams
						MapView.LayoutParams.WRAP_CONTENT,
						// 使控件固定在某个地理位置
						pt, 0, -32,
						// 控件对齐方式
						MapView.LayoutParams.BOTTOM_CENTER);
				// 添加View到MapView中
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
