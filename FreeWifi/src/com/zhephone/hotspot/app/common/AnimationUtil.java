package com.zhephone.hotspot.app.common;

import java.lang.reflect.Method;

import android.view.View;
import android.view.View.MeasureSpec;
import android.view.animation.Animation;
import android.view.animation.Transformation;
/**
 * 动画工具
 * @author zhoubenhua
 *
 */
public class AnimationUtil {
	/**
	 * 控件展开动画
	 * @param 需要展开动画的控件
	 * @param expand true是展开 false是收缩
	 * @param duration  动画持续时间
	 * @return
	 */
	public static Animation expandAnimation(final View v, final boolean expand,int duration) {
		try {
			Method m = v.getClass().getDeclaredMethod("onMeasure", int.class,
					int.class);
			m.setAccessible(true);
			m.invoke(v,
					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
					MeasureSpec.makeMeasureSpec(
							((View) v.getParent()).getMeasuredWidth(),			
							MeasureSpec.AT_MOST));
		} catch (Exception e) {
			e.printStackTrace();
		}

		final int initialHeight = v.getMeasuredHeight();

		if (expand) {
			v.getLayoutParams().height = 0;
		} else {
			v.getLayoutParams().height = initialHeight;
		}
		v.setVisibility(View.VISIBLE);

		Animation a = new Animation() {
			protected void applyTransformation(float interpolatedTime,
					Transformation t) {
				int newHeight = 0;
				if (expand) {
					newHeight = (int) (initialHeight * interpolatedTime);
				} else {
					newHeight = (int) (initialHeight * (1 - interpolatedTime));
				}
				v.getLayoutParams().height = newHeight;
				v.requestLayout();

				if (interpolatedTime == 1 && !expand)
					v.setVisibility(View.GONE);
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};
		a.setDuration(duration);
		//v.requestLayout();
//		a.setAnimationListener(new AnimationListener(){
//          public void onAnimationEnd(Animation arg0) {
//          }
//
//          @Override
//          public void onAnimationRepeat(Animation animation) {
//              // TODO Auto-generated method stub
//          }
//
//          @Override
//          public void onAnimationStart(Animation animation) {
//              if(expand) {
//            	  animationListener.showListener();
//              } else {
//            	  animationListener.closeListener(); 
//              }
//          }
//		});
		return a;
	}
	
	/**
	 * 文字展开动画
	 * @param 需要展开动画的控件
	 * @param expand true是展开 false是收缩
	 * @param duration  动画持续时间
	 * @param height  控件高度
	 * @return
	 */
	public static Animation textExpandAnimation(final View v, final boolean expand,int duration,int height) {
		final int initialHeight = height;

		if (expand) {
			v.getLayoutParams().height = 0;
		} else {
			v.getLayoutParams().height = initialHeight;
		}
		v.setVisibility(View.VISIBLE);

		Animation a = new Animation() {
			protected void applyTransformation(float interpolatedTime,
					Transformation t) {
				int newHeight = 0;
				if (expand) {
					newHeight = (int) (initialHeight * interpolatedTime);
				} else {
					newHeight = (int) (initialHeight * (1 - interpolatedTime));
				}
				
				v.getLayoutParams().height = newHeight;
				v.requestLayout();	
				if (interpolatedTime == 1 && !expand)
					v.setVisibility(View.GONE);
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};
		a.setDuration(duration);
//		a.setAnimationListener(new AnimationListener(){
//          public void onAnimationEnd(Animation arg0) {
//          }
//
//          @Override
//          public void onAnimationRepeat(Animation animation) {
//              // TODO Auto-generated method stub
//          }
//
//          @Override
//          public void onAnimationStart(Animation animation) {
//              if(expand) {
//            	  animationListener.showListener();
//              } else {
//            	  animationListener.closeListener(); 
//              }
//          }
//		});
		return a;
	}
	
	public static int getViewWidth(View  v ) {
		try {
			Method m = v.getClass().getDeclaredMethod("onMeasure", int.class,
					int.class);
			m.setAccessible(true);
			m.invoke(v,
					MeasureSpec.makeMeasureSpec(MeasureSpec.UNSPECIFIED, 0),
					MeasureSpec.makeMeasureSpec(
							((View) v.getParent()).getMeasuredWidth(),			
							MeasureSpec.AT_MOST));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return v.getMeasuredWidth();
	}
	
	public interface ViewAnimationListener {
      public void showListener();
      public void closeListener();
	}

}
