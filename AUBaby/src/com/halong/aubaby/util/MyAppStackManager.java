package com.halong.aubaby.util;

import java.util.Stack;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

/**
 * 利用堆栈把activity保存起来，方便进行处理
 * @author lihua
 *
 */
public class MyAppStackManager {
	private static Stack<Activity> activityStack;
	private static MyAppStackManager instance;
	
	private MyAppStackManager(){}
	/**
	 * 单一实例
	 */
	public static MyAppStackManager getMyAppStackManager(){
		if(instance==null){
			instance=new MyAppStackManager();
		}
		return instance;
	}
	/**
	 * 添加Activity到堆栈
	 */
	public void addActivity(Activity activity){
		
		//System.out.println("123456");
		if(activityStack==null){
			activityStack=new Stack<Activity>();
		}
		activityStack.add(activity);
	}
	/**
	 * 获取当前Activity（堆栈中最后一个压入的）
	 */
	public Activity currentActivity(){
		Activity activity=activityStack.lastElement();
		return activity;
	}
	/**
	 * 结束当前Activity（堆栈中最后一个压入的）
	 */
	public void finishActivity(){
		Activity activity=activityStack.lastElement();
		finishActivity(activity);
	}
	/**
	 * 结束指定的Activity
	 */
	public void finishActivity(Activity activity){
		if(activity!=null){
			activityStack.remove(activity);
			
			activity=null;
		}
	}
	/**
	 * 结束指定类名的Activity
	 */
	public void finishActivity(Class<?> cls){
		for (Activity activity : activityStack) {
			if(activity.getClass().equals(cls) ){
				finishActivity(activity);
			}
		}
	}
	/**
	 * 保留第一个activity
	 */
	public void retainFristActivity(){
		for (int i = activityStack.size()-1; 0<i; i--){
            if (null != activityStack.get(i)){
            	activityStack.get(i).finish();
            }
	    }
		activityStack.clear();
	}
	/**
	 * 结束所有Activity
	 */
	public void finishAllActivity(){
		for (int i = 0, size = activityStack.size(); i < size; i++){
            if (null != activityStack.get(i)){
            	activityStack.get(i).finish();
            }
	    }
		activityStack.clear();
	}
	/**
	 * 退出应用程序
	 */
	public void AppExit(Context context) {
		try {
			finishAllActivity();
			ActivityManager activityMgr= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			activityMgr.restartPackage(context.getPackageName());
			System.exit(0);
		} catch (Exception e) {	}
	}

}
