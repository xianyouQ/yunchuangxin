package com.example.yunchuangxin;

import android.os.Bundle;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;

public class TabMainActivity extends TabActivity {
	
	private TabHost host;
	private ImageView tool_btn1;
	private ImageView tool_btn2;
	private ImageView tool_btn3;
	private static final String TAB_HOME_MAP = "tab_home_map";
	private static final String TAB_COLLECT = "tab_collect";
	private static final String TAB_PREFERENTIAL = "tab_preferential";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.tab_main);
		tool_btn1 = (ImageView) findViewById(R.id.tool_btn1);
		tool_btn2 = (ImageView) findViewById(R.id.tool_btn2);
		tool_btn3 = (ImageView) findViewById(R.id.tool_btn3);

		tool_btn1.setOnClickListener(new MyOnClickListener());
		tool_btn2.setOnClickListener(new MyOnClickListener());
		tool_btn3.setOnClickListener(new MyOnClickListener());

		host = getTabHost();
		TabSpec spec_home = host.newTabSpec(TAB_HOME_MAP).setIndicator(TAB_HOME_MAP);
		spec_home.setContent(new Intent(this, HomeMapActivity.class));
		host.addTab(spec_home); // 添加主页选项卡
		TabSpec spec_msg = host.newTabSpec(TAB_COLLECT).setIndicator(TAB_COLLECT);
		spec_msg.setContent(new Intent(this, CollectActivity.class));
		host.addTab(spec_msg);
		TabSpec spec_info = host.newTabSpec(TAB_PREFERENTIAL).setIndicator(TAB_PREFERENTIAL);
		spec_info.setContent(new Intent(this, PreferentialActivity.class));
		host.addTab(spec_info);

	}

	private class MyOnClickListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			int id = arg0.getId();
			switch (id) {
			case R.id.tool_btn1:
				host.setCurrentTabByTag(TAB_HOME_MAP);
				break;

			case R.id.tool_btn2:
				host.setCurrentTabByTag(TAB_COLLECT);
				break;
			case R.id.tool_btn3:
				host.setCurrentTabByTag(TAB_PREFERENTIAL);
				break;
			}
		}
	}

}
