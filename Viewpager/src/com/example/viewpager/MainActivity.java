package com.example.viewpager;

import com.example.viewpager.DynamicViewPager.OnPageClickListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

public class MainActivity extends Activity {
	
	com.example.viewpager.DynamicViewPager dynamicView;

	 String[] urls = new String[] {
			"http://images.juheapi.com/jztk/c1c2subject1/26.jpg",
			"http://images.juheapi.com/jztk/c1c2subject1/28.jpg",
			"http://images.juheapi.com/jztk/c1c2subject1/30.jpg",
			"http://images.juheapi.com/jztk/c1c2subject1/40.jpg" };
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		dynamicView=(DynamicViewPager) this.findViewById(R.id.dynamicView);
		dynamicView.setUrls(urls);
		dynamicView.setOnPageClickListener(new OnPageClickListener() {
			@Override
			public void onPageClick(int position) {
				// TODO Auto-generated method stub
				Toast.makeText(MainActivity.this, position+"", 1).show();
			}
		});
	}

}
