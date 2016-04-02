package com.example.viewpager;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Timer;
import java.util.TimerTask;

import com.nineoldandroids.view.ViewHelper;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ImageView.ScaleType;

public class DynamicViewPager extends RelativeLayout {

	//viewpager 和下面的点
	private android.support.v4.view.ViewPager vp_imgs;
	private LinearLayout ll_dots;

	//缩放比例和平移距离
	private float mScale;
	private float mTrans;

	private static final float SCALE_MAX = 0.5f;
	private static final String TAG = "myviewpager";

	//存放imgs用于缩放
	private HashMap<Integer, View> mChildViews = new LinkedHashMap<Integer, View>();

	//左右img
	private View leftView;
	private View rightView;
	//imgd的url
	private String[] urls;
	//所有点
	private ImageView[] dots;

	private Context context;
	//当前页数
	private int currentPage=0;
	private OnPageClickListener onPageClickListener;
	public interface OnPageClickListener{
		public void onPageClick(int position);
	}
	//设置页面点击事件
	public void setOnPageClickListener(OnPageClickListener clickListener){
		this.onPageClickListener=clickListener;
	}
	/**
	 * 初始化
	 * @param context
	 * @param attrs
	 */
	public DynamicViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		View rootView = View.inflate(context, R.layout.myviewpager, this);
		vp_imgs = (ViewPager) rootView.findViewById(R.id.vp_imgs);
		ll_dots = (LinearLayout) rootView.findViewById(R.id.ll_dots);
	}
	/**
	 * 为viewpager设置参数
	 * @param urls
	 */
	public void setUrls(String [] urls){
		this.urls=urls;
		dots=new ImageView[urls.length];
		initDots();
		vp_imgs.setAdapter(new PagesAdapter());
		vp_imgs.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int index) {
				//当页面改变 修改点的属性
				for(int i=0;i<dots.length;i++){
					dots[i].setEnabled(true);
				}
				dots[index].setEnabled(false);
				currentPage=index;
			}
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				// TODO Auto-generated method stub
				//左右滑动的时候 可以设置动画效果
				float effectOffset = isSmall(positionOffset) ? 0
						: positionOffset;
				leftView = mChildViews.get(position);
				rightView = mChildViews.get(position + 1);
				animateStack(leftView, rightView, effectOffset,
						positionOffsetPixels);
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		//定时自动滑动
		Timer timer=new Timer();
		timer.schedule(task, 1000,3000);
		
	}
	private Handler handler =new Handler(){
			public void handleMessage(android.os.Message msg) {
				int nextPage=(currentPage+1)%(urls.length);
				vp_imgs.setCurrentItem(nextPage, true);
			};
	};
	private TimerTask task=new TimerTask() {
		@Override
		public void run() {
			handler.sendEmptyMessage(0x111);
		}
	};
	/**
	 * 填充小点
	 */
	private void initDots() {
		for(int i=0;i<urls.length;i++){
			ImageView dot=new ImageView(context);
			dot.setLayoutParams(new LayoutParams(30, 30));
			dot.setImageResource(R.drawable.dot);
			dot.setPadding(5, 5, 5, 5);
			ll_dots.addView(dot);
			dots[i]=dot;
		}
		dots[0].setEnabled(false);
	}
	//翻页动画
	private void animateStack(View leftView2, View rightView2,
			float effectOffset, int positionOffsetPixels) {
		if (rightView2 != null) {
			/**
			 * 缩小比例 如果手指从右到左的滑动（切换到后一个）：0.0~1.0，即从一半到最大
			 * 如果手指从左到右的滑动（切换到前一个）：1.0~0，即从最大到一半
			 */
			mScale = (1 - SCALE_MAX) * effectOffset + SCALE_MAX;
			ViewHelper.setScaleX(rightView2, mScale);
			ViewHelper.setScaleY(rightView2, mScale);
		}
		if (leftView2 != null) {
			mScale = (1 - SCALE_MAX) * (1-effectOffset) + SCALE_MAX;
			ViewHelper.setScaleX(leftView2, mScale);
			ViewHelper.setScaleY(leftView2, mScale);
		}
	}
	//判断有木有滑动
	private boolean isSmall(float positionOffset) {
		return Math.abs(positionOffset) < 0.0001;
	}
	//将page添加进来 用于滑动的view设置
	public void setObjectForPostion(View view, int position) {
		mChildViews.put(position, view);
	}
	/**
	 * 为viewpager填充数据
	 * @author root
	 *
	 */
	class PagesAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return urls.length;
		}
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0==arg1;
		}
		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			ImageView img=new ImageView(context);
			img.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					onPageClickListener.onPageClick(position);
				}
			});
			ImageLoader.getInstance().displayImage(urls[position], img);
			img.setScaleType(ScaleType.FIT_XY);
			container.addView(img);
			setObjectForPostion(img, position);
			return img;
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View)object);
		}
	}
}
