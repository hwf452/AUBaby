package com.halong.aubaby.push;

import java.util.Calendar;

import android.util.Log;

import com.baidu.frontia.FrontiaApplication;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/*
 * 如果您的工程中实现了Application的继承类，那么，您需要将父类改为com.baidu.frontia.FrontiaApplication。
 * 如果您没有实现Application的继承类，那么，请在AndroidManifest.xml的Application标签中增加属性： 
 * <application android:name="com.baidu.frontia.FrontiaApplication"
 * 。。。
 */
public class DemoApplication extends FrontiaApplication {
	private ImageLoader imageLoader = ImageLoader.getInstance();

	public ImageLoader getImageLoader() {
		return imageLoader;
	}

	public void setImageLoader(ImageLoader imageLoader) {
		
		this.imageLoader = imageLoader;
	}
	@Override
	public void onCreate() {
		Log.d("YYY", "start application at " + Calendar.getInstance().getTimeInMillis());
		super.onCreate();
		Log.d("YYY", "end application at " + Calendar.getInstance().getTimeInMillis());
		
		// 以下是您原先的代码实现，保持不变
		imageLoader.init(ImageLoaderConfiguration.createDefault(getBaseContext()));
	}

	
}
