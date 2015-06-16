package com.halong.aubaby.tab3;

/**
 * 获取视频或像片页面
 */

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.halong.aubaby.contant.Keys;
import com.halong.aubaby.photosalbum.ImgFileListActivity;
import com.halong.aubaby.util.SharedPreferencesHelper;
import com.halong.aubaby.R;

import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore.Images.Media;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;

@SuppressLint({ "NewApi", "SimpleDateFormat" })
public class GetPicOrVideo extends Activity implements Callback {

	/**
	 * 声明相关控件
	 * 
	 */
	private Camera mCamera;
	private static final String TAG = "MainActivity";
	private String path;

	private Integer cameraCount = 0;
	public boolean isCameraBack = true;
	private int cameraPosition = 1;// 0代表前置摄像头，1代表后置摄像头
	private Button btn2, btn4, btn5, btn6, btn7, getImageButton, btn9, btn10,
			btn11;
	private Button getLocalPicOrVideoButton, vedioOrPicButton;// 获取本地视频或照片按钮,拍照或录制视频按钮
	public static final int RESULTCODE = 1;
	private SurfaceView mSurfaceView;
	private MediaRecorder mMediaRecorder;
	private boolean isRecording = false;
	public static final int MEDIA_TYPE_IMAGE = 1;
	boolean isFlashOn = true;
	public static final int MEDIA_TYPE_VIDEO = 2;
	private boolean isCamera = true;
	private boolean isVideo = false;
	private ImageView mVideoRecordOn, mPicshow;

	private SurfaceHolder mHolder;

	private Chronometer chronometer;
	ArrayList<String> filelist;

	@SuppressWarnings("deprecation")
	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/**
		 * 查找控件
		 * 
		 */

		setContentView(R.layout.activity_getpicorvideo);

		initView();

		chronometer
				.setOnChronometerTickListener(new OnChronometerTickListenerImpl());
		/**
		 * 从SD卡获取像片或视频
		 */
		getLocalPicOrVideoButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isVideo) {
					Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
					intent.addCategory(Intent.CATEGORY_OPENABLE);
					intent.setType("video/*");
					startActivityForResult(intent, Keys.GET_PIC_OR_VIDEO);
				} else {
					Intent intent = new Intent().setClass(GetPicOrVideo.this,
							ImgFileListActivity.class);
					startActivityForResult(intent, Keys.GET_PIC_OR_VIDEO);
				}
			}
		});
		/**
		 * 点击拍照或摄像
		 */
		btn2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// 点击拍照或拍摄视频按钮
				if (isVideo) {

					chronometer.setVisibility(View.VISIBLE);
					mVideoRecordOn.setVisibility(View.VISIBLE);
					// 假如正在摄影，停止摄影
					if (isRecording) {

						// 停止录像并释放camera44

						mMediaRecorder.stop(); // 停止录像
						chronometer.stop();// 停止计时

						releaseMediaRecorder(); // 释放MediaRecorder对象

						mCamera.lock(); // 将控制权从MediaRecorder 交回camera

						// 通知用户录像已停止

						isRecording = false;

						btn6.setFocusable(true);
						btn6.setClickable(true);
						vedioOrPicButton.setFocusable(true);
						vedioOrPicButton.setClickable(true);
						btn9.setVisibility(View.GONE);
						btn10.setVisibility(View.VISIBLE);
						btn11.setVisibility(View.VISIBLE);

					} else {
						// 开始拍摄
						// 初始化视频camera
						btn6.setFocusable(false);
						btn6.setClickable(false);
						vedioOrPicButton.setFocusable(false);
						vedioOrPicButton.setClickable(false);
						if (prepareVideoRecorder()) {
							FrameLayout.LayoutParams btn5LayoutParams = (LayoutParams) btn5
									.getLayoutParams();

							btn5LayoutParams.setMargins(
									getResources().getDimensionPixelSize(
											R.dimen.leftDip15),
									getResources().getDimensionPixelSize(
											R.dimen.topDip15), 0, 0);
							btn5LayoutParams.gravity = Gravity.LEFT;

							btn5.setLayoutParams(btn5LayoutParams);

							FrameLayout.LayoutParams btn6LayoutParams = (LayoutParams) btn6
									.getLayoutParams();

							btn6LayoutParams.setMargins(
									0,
									getResources().getDimensionPixelSize(
											R.dimen.leftDip15),
									getResources().getDimensionPixelSize(
											R.dimen.topDip15), 0);

							btn6LayoutParams.gravity = Gravity.RIGHT;

							btn6.setLayoutParams(btn6LayoutParams);
							vedioOrPicButton.setVisibility(View.GONE);

							// Camera已可用并解锁，MediaRecorder已就绪,

							// 现在可以开始录像

							mMediaRecorder.start();

							// 通知用户录像已开始

							isRecording = true;
							btn4.setVisibility(View.GONE);
							btn9.setBackgroundResource(R.drawable.btn43_selector);
							btn9.setVisibility(View.VISIBLE);
							getLocalPicOrVideoButton.setVisibility(View.GONE);
							chronometer.setBase(SystemClock.elapsedRealtime());// 复位键
							// chronometer.setFormat("%s");// 更改时间显示格式
							chronometer.start();// 开始计时
							btn10.setVisibility(View.GONE);
							btn11.setVisibility(View.GONE);

						} else {

							// 准备未能完成，释放camera

							releaseMediaRecorder();

							// 通知用户

						}

					}
					// 点击获取图片

				} else if (isCamera) {
					mCamera.takePicture(null, null, mPicture);

				}

			}

		});
		// 点击选择要获取图片或像片
		vedioOrPicButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isCamera) {

					isVideo = true;
					isCamera = false;
					btn2.setBackgroundResource(R.drawable.btn37_selector);
					vedioOrPicButton
							.setBackgroundResource(R.drawable.btn36_selector);
				} else if (isVideo) {
					isCamera = true;
					isVideo = false;
					chronometer.setVisibility(View.GONE);
					mVideoRecordOn.setVisibility(View.GONE);

					btn2.setBackgroundResource(R.drawable.btn39_selector);
					vedioOrPicButton
							.setBackgroundResource(R.drawable.btn40_selector);
				}

			}
		});
		// 左上角关闭按钮，返回
		btn4.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		// 右边选择自动或关闭闪光灯，当拍摄时，闪光灯常亮
		btn5.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Camera.Parameters parameters = mCamera.getParameters();
				if (isCamera) {

					if (isFlashOn) {
						parameters.setFlashMode("off");
						isFlashOn = false;
						btn5.setText("关闭");
					} else {
						parameters.setFlashMode("auto");
						isFlashOn = true;
						btn5.setText("自动");
					}
				} else if (isVideo) {
					if (isFlashOn) {
						parameters.setFlashMode("torch");
						isFlashOn = false;
						btn5.setText("开启");
					} else {
						parameters.setFlashMode("auto");
						isFlashOn = true;
						btn5.setText("自动");
					}

				}

				mCamera.setParameters(parameters);

			}
		});
		// 前后摄像头切换
		btn6.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (isCameraBack) {
					isCameraBack = false;
				} else {
					isCameraBack = true;
				}

				CameraInfo cameraInfo = new CameraInfo();
				for (int i = 0; i < cameraCount; i++) {

					Camera.getCameraInfo(i, cameraInfo);// 得到每一个摄像头的信息
					if (cameraPosition == 1) {
						// 现在是后置，变更为前置
						if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {// 代表摄像头的方位，CAMERA_FACING_FRONT前置
																							// CAMERA_FACING_BACK后置
							mCamera.stopPreview();// 停掉原来摄像头的预览
							mCamera.release();// 释放资源
							mCamera = null;// 取消原来摄像头

							mCamera = Camera.open(i);// 打开当前选中的摄像头
							try {
								cameraPosition = 0;
								deal(mCamera);
								mCamera.setPreviewDisplay(mSurfaceView
										.getHolder());// 通过surfaceview显示取景画面
								//
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							mCamera.startPreview();// 开始预览
							break;
						}
					} else {
						// 现在是前置， 变更为后置
						if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {// 代表摄像头的方位，CAMERA_FACING_FRONT前置
																						// CAMERA_FACING_BACK后置
							mCamera.stopPreview();// 停掉原来摄像头的预览
							mCamera.release();// 释放资源
							mCamera = null;// 取消原来摄像头
							mCamera = Camera.open(i);// 打开当前选中的摄像头
							try {
								cameraPosition = 1;
								deal(mCamera);
								mCamera.setPreviewDisplay(mSurfaceView
										.getHolder());// 通过surfaceview显示取景画面
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							mCamera.startPreview();// 开始预览

							break;
						}
					}

				}

			}
		});
		// 摄影后点左下角的关闭按钮
		btn9.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		// 取消摄影后的结果
		btn10.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				path = null;
				getLocalPicOrVideoButton.setVisibility(View.VISIBLE);
				btn10.setVisibility(View.GONE);
				btn11.setVisibility(View.GONE);
				vedioOrPicButton.setVisibility(View.VISIBLE);

				FrameLayout.LayoutParams btn5LayoutParams = (LayoutParams) btn5
						.getLayoutParams();

				btn5LayoutParams.setMargins(0, getResources()
						.getDimensionPixelSize(R.dimen.leftDip15),
						getResources().getDimensionPixelSize(R.dimen.topDip16),
						0);
				btn5LayoutParams.gravity = Gravity.RIGHT;

				btn5.setLayoutParams(btn5LayoutParams);

				FrameLayout.LayoutParams btn6LayoutParams = (LayoutParams) btn6
						.getLayoutParams();

				btn6LayoutParams.setMargins(0, getResources()
						.getDimensionPixelSize(R.dimen.topDip15),
						getResources().getDimensionPixelSize(R.dimen.topDip17),
						0);

				btn6LayoutParams.gravity = Gravity.RIGHT;

				btn6.setLayoutParams(btn6LayoutParams);
				btn4.setVisibility(View.VISIBLE);
				chronometer.setBase(SystemClock.elapsedRealtime());// 复位键
				chronometer.setVisibility(View.GONE);
				mVideoRecordOn.setVisibility(View.GONE);

			}
		});
		// 获取视频，把搂据传到相关页面
		btn11.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (path != null) {
					// Intent intent = new Intent();
					//
					// intent.putExtra("data", path);
					//
					// setResult(RESULTCODE, intent);
					//
					// finish();
				}

			}
		});

	}

	private void initView() {
		// TODO Auto-generated method stub
		chronometer = (Chronometer) findViewById(R.id.chronometer1);

		getLocalPicOrVideoButton = (Button) findViewById(R.id.getLocalPicOrVideoBtn);
		btn2 = (Button) findViewById(R.id.imageView2);
		vedioOrPicButton = (Button) findViewById(R.id.vedioOrPicBtn);
		// vedioOrPicButton是否显示。有些情况vedioOrPicButton不需要显示
		if (SharedPreferencesHelper.getStringValue(this, Keys.IS_OPEN_VEDIO)
				.equals(Keys.VEDIO_OPENED)) {
			vedioOrPicButton.setVisibility(View.VISIBLE);
		} else {
			vedioOrPicButton.setVisibility(View.GONE);
		}
		btn4 = (Button) findViewById(R.id.button1);
		btn5 = (Button) findViewById(R.id.button2);
		btn6 = (Button) findViewById(R.id.button3);
		btn7 = (Button) findViewById(R.id.imageView4);
		getImageButton = (Button) findViewById(R.id.getImageBtn);
		btn9 = (Button) findViewById(R.id.imageView6);
		btn10 = (Button) findViewById(R.id.imageView7);
		btn11 = (Button) findViewById(R.id.videoGood);
		filelist = new ArrayList<String>();

		mVideoRecordOn = (ImageView) findViewById(R.id.videoRecordOn);
		mPicshow = (ImageView) findViewById(R.id.picshow);
		// 创建Preview view并将其设为activity中的内容

		mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView1);

		// new CameraPreview(this, mCamera,rotation);

		mHolder = mSurfaceView.getHolder();

		mHolder.addCallback(this);

		// 已过期的设置，但版本低于3.0的Android还需要

		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		// 获取摄像头个数，api10以后提供
		cameraCount = Camera.getNumberOfCameras();
		// 假如只有一个摄像头，把前后切换摄像头的按钮隐藏掉
		if (cameraCount == 1) {
			btn6.setVisibility(View.GONE);
		}
	}

	/**
	 * 摄像头前后切换要处理相关方法，假如是后置摄像头，转90度，假如是前置摄像头，转270度
	 * 
	 * @param camera
	 * @return
	 */
	public Camera deal(Camera camera) {
		// 设置camera预览的角度，因为默认图片是倾斜90度的

		camera.setDisplayOrientation(90);
		try {
			Camera.Parameters parameters = camera.getParameters();

			int bestWidth = 320;
			int bestHight = 240;
			List<Size> list = parameters.getSupportedPictureSizes();
			if (list.size() > 1) {
				for (Size size : list) {
					Log.i("size", "width" + size.width + "x" + size.height);
					if (size.width > bestWidth && size.height > bestHight) {
						bestHight = size.height;
						bestWidth = size.width;
					}
				}
			}
			Log.i("size", "bestwidth" + bestWidth + "x" + bestHight);

			if (bestWidth > 320 && bestHight > 240) {

				parameters.setPictureSize(bestWidth, bestHight);
			}

			List<Size> list1 = parameters.getSupportedPictureSizes();
			if (list1.size() > 1) {
				for (Size size : list1) {
					Log.i("size", "width" + size.width + "x" + size.height);
					if (size.width > bestWidth && size.height > bestHight) {
						bestHight = size.height;
						bestWidth = size.width;
					}
				}
			}
			Log.i("size", "videowidth" + bestWidth + "x" + bestHight);
			List<String> list2 = parameters.getSupportedFlashModes();
			if (list2.size() > 1) {
				for (int i = 0; i < list2.size(); i++) {
					Log.i("flash", list2.get(i));
				}
				parameters.setFlashMode("auto");
				btn5.setClickable(true);

			} else {
				btn5.setClickable(false);
			}

			camera.setParameters(parameters);
		} catch (Exception e) {
			// TODO: handle exception
		}

		return camera;
	}

	/** 检查设备是否提供摄像头 */

	private boolean checkCameraHardware(Context context) {

		if (context.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA)) {

			// 摄像头存在

			return true;

		} else {

			// 摄像头不存在

			return false;

		}

	}

	/** 安全获取Camera对象实例的方法 */

	@SuppressLint("NewApi")
	public Camera getCameraInstance() {

		Camera c = null;

		try {
			if (isCameraBack) {
				c = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);// 打开摄像头

			} else {
				c = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);// 打开摄像头

			}

		}

		catch (Exception e) {

			// 摄像头不可用（正被占用或不存在）
			Log.i("openCamera", "摄像头不可用（正被占用或不存在）");

		}

		return c; // 不可用则返回null

	}

	/**
	 * 点击获取图片回调的方法
	 */

	private PictureCallback mPicture = new PictureCallback() {

		@Override
		public void onPictureTaken(final byte[] data, Camera camera) {

			final Uri imageFileUri = getContentResolver().insert(
					Media.EXTERNAL_CONTENT_URI, new ContentValues());

			try {
				OutputStream imageFileOs = getContentResolver()
						.openOutputStream(imageFileUri);
				imageFileOs.write(data);
				imageFileOs.flush();
				imageFileOs.close();

			} catch (Exception e) {
				// TODO: handle exception
			}

			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inPreferredConfig = Config.RGB_565;
			opts.inSampleSize = 3;
			Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length,
					opts);
			Matrix matrixs = new Matrix();
			int orientations = 0;
			if (cameraPosition == 1) {
				if (orientations > 325 || orientations <= 45) {
					Log.v("time", "Surface.ROTATION_0;" + orientations);
					matrixs.setRotate(90);
				} else if (orientations > 45 && orientations <= 135) {
					Log.v("time", " Surface.ROTATION_270" + orientations);
					matrixs.setRotate(180);
				} else if (orientations > 135 && orientations < 225) {
					Log.v("time", "Surface.ROTATION_180;" + orientations);
					matrixs.setRotate(270);
				} else {
					Log.v("time", "Surface.ROTATION_90" + orientations);
					matrixs.setRotate(0);
				}
			} else {
				if (orientations > 325 || orientations <= 45) {
					Log.v("time", "Surface.ROTATION_0;" + orientations);
					matrixs.setRotate(270);
				} else if (orientations > 45 && orientations <= 135) {
					Log.v("time", " Surface.ROTATION_270" + orientations);
					matrixs.setRotate(0);
				} else if (orientations > 135 && orientations < 225) {
					Log.v("time", "Surface.ROTATION_180;" + orientations);
					matrixs.setRotate(90);
				} else {
					Log.v("time", "Surface.ROTATION_90" + orientations);
					matrixs.setRotate(180);
				}

			}
			bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
					bmp.getHeight(), matrixs, true);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);// (0 -
			byte[] data1 = stream.toByteArray();

			final File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);

			try {

				FileOutputStream fos = new FileOutputStream(pictureFile);

				fos.write(data1);
				fos.flush();
				fos.close();

			} catch (FileNotFoundException e) {

				Log.d(TAG, "File not found: " + e.getMessage());

			} catch (IOException e) {

				Log.d(TAG, "Error accessing file: " + e.getMessage());

			}
			filelist.add(pictureFile.getAbsolutePath());

			btn7.setVisibility(View.VISIBLE);
			getImageButton.setVisibility(View.VISIBLE);
			mPicshow.setImageBitmap(bmp);
			mSurfaceView.setVisibility(View.GONE);
			btn4.setVisibility(View.GONE);
			btn5.setVisibility(View.GONE);
			btn6.setVisibility(View.GONE);
			getLocalPicOrVideoButton.setVisibility(View.GONE);
			vedioOrPicButton.setVisibility(View.GONE);
			mPicshow.setVisibility(View.VISIBLE);
			// 取消获取图片，重新拍照片
			btn7.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					btn7.setVisibility(View.GONE);
					getImageButton.setVisibility(View.GONE);

					mSurfaceView.setVisibility(View.VISIBLE);
					btn4.setVisibility(View.VISIBLE);
					btn5.setVisibility(View.VISIBLE);
					btn6.setVisibility(View.VISIBLE);
					getLocalPicOrVideoButton.setVisibility(View.VISIBLE);
					vedioOrPicButton.setVisibility(View.VISIBLE);
					mPicshow.setVisibility(View.GONE);

				}
			});
			// 右下角的选择按钮，点击后把数据传到相关页面
			getImageButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					sendfiles();
				}
			});

			// camera.startPreview();

		}

	};

	// 初始化摄像机，设备相关参擞，开始拍摄
	@SuppressLint("NewApi")
	private boolean prepareVideoRecorder() {

		mMediaRecorder = new MediaRecorder();

		// 第1步：解锁并将摄像头指向MediaRecorder

		mCamera.unlock();

		mMediaRecorder.setCamera(mCamera);

		mMediaRecorder.setPreviewDisplay(mHolder.getSurface());
		mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC); // 录音源为麦克风
		mMediaRecorder.setMaxDuration(120000000);// 最大期限
		mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA); // 从照相机采集视频
		mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
		mMediaRecorder.setVideoSize(640, 480);

		mMediaRecorder.setVideoFrameRate(30); // 每秒3帧

		mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H263); // 设置视频编码方式
		mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		mMediaRecorder.setVideoEncodingBitRate(3000000);
		mMediaRecorder.setAudioEncodingBitRate(196000);
		if (cameraPosition == 1) {
			mMediaRecorder.setOrientationHint(90);
		} else {
			mMediaRecorder.setOrientationHint(270);
		}

		path = getOutputMediaFile(MEDIA_TYPE_VIDEO).getAbsolutePath();
		// Toast.makeText(this, path, Toast.LENGTH_LONG).show();

		mMediaRecorder.setOutputFile(path);// 保存路径

		try {
			mMediaRecorder.prepare();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mCamera.lock();
		}

		return true;

	}

	/**
	 * activity暂停时，假如正在摄影，停止摄影
	 */

	@Override
	protected void onPause() {

		super.onPause();

		releaseMediaRecorder(); // 如果正在使用MediaRecorder，首先需要释放它。

	}

	// 停止摄影时调用这个方法

	private void releaseMediaRecorder() {

		if (mMediaRecorder != null) {

			mMediaRecorder.reset(); // 清除recorder配置

			mMediaRecorder.release(); // 释放recorder对象

			mMediaRecorder = null;

			// 为后续使用锁定摄像头

		}

	}

	/** 为保存图片或视频创建File */

	private static File getOutputMediaFile(int type) {

		// 安全起见，在使用前应该

		// 用Environment.getExternalStorageState()检查SD卡是否已装入

		File mediaStorageDir = new File(Environment
				.getExternalStorageDirectory().getAbsoluteFile() + Keys.AUBABY);

		// 如果期望图片在应用程序卸载后还存在、且能被其它应用程序共享，

		// 则此保存位置最合适

		// 如果不存在的话，则创建存储目录

		if (!mediaStorageDir.exists()) {

			if (!mediaStorageDir.mkdirs()) {

				Log.d("MyCameraApp", "failed to create directory");

				return null;

			}

		}

		// 创建媒体文件名

		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());

		File mediaFile;

		if (type == MEDIA_TYPE_IMAGE) {

			mediaFile = new File(mediaStorageDir + File.separator +

			"image_" + timeStamp + ".jpg");

		} else if (type == MEDIA_TYPE_VIDEO) {

			mediaFile = new File(mediaStorageDir + File.separator +

			"video_" + timeStamp + ".mp4");

		} else {

			return null;

		}

		return mediaFile;

	}

	/**
	 * @desc <pre>
	 * 旋转图片
	 * </pre>
	 * @author Weiliang Hu
	 * @date 2013-9-18
	 * @param angle
	 * @param bitmap
	 * @return
	 */
	public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		System.out.println("angle2=" + angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		bitmap.recycle();
		return resizedBitmap;
	}

	/**
	 * surfaceview 回调的三个方法
	 */

	@SuppressLint("NewApi")
	public void surfaceCreated(SurfaceHolder holder) {

		// 创建Camera实例
		if (checkCameraHardware(this)) {
			mCamera = getCameraInstance();
		}

		// surface已被创建，现在把预览画面的位置通知摄像头
		Camera.Parameters parameters = mCamera.getParameters();

		try {

			mCamera.setDisplayOrientation(90);

			int bestWidth = 2048;
			int bestHight = 1536;
			List<Size> list = parameters.getSupportedPictureSizes();
			if (list != null) {
				for (Size size : list) {
					Log.i("size", "width" + size.width + "x" + size.height);
					if (size.width > bestWidth && size.height > bestHight) {
						bestHight = size.height;
						bestWidth = size.width;
					}
				}
			}
			Log.i("size", "bestwidth" + bestWidth + "x" + bestHight);

			if (bestWidth > 2048 && bestHight > 1536) {

				parameters.setPictureSize(bestWidth, bestHight);
			}

			List<String> list2 = parameters.getSupportedFlashModes();
			if (list2 != null) {

				parameters.setFlashMode("auto");
				btn5.setText("自动");

			} else {
				btn5.setVisibility(View.GONE);
			}

			mCamera.setParameters(parameters);

			mCamera.setPreviewDisplay(holder);

			mCamera.startPreview();

		} catch (IOException e) {

			Log.d(TAG, "Error setting camera preview: " + e.getMessage());
			if (mCamera != null) {
				mCamera.release();
			}
		}

	}

	public void surfaceDestroyed(SurfaceHolder holder) {

		// 空代码。注意在activity中释放摄像头预览对象
		if (mMediaRecorder != null) {

			mMediaRecorder.reset(); // 清除recorder配置

			mMediaRecorder.release(); // 释放recorder对象

			mMediaRecorder = null;
			mCamera.lock(); // 将控制权从MediaRecorder 交回camera

		}

		mCamera.stopPreview();
		mCamera.release();

	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mMediaRecorder != null) {

			mMediaRecorder.reset(); // 清除recorder配置

			mMediaRecorder.release(); // 释放recorder对象

			mMediaRecorder = null;

		}
		if (mCamera != null) {
			mCamera.release();
		}
	}

	/**
	 * 摄影时记录时间的监听器
	 * 
	 * @author hwf452-pc
	 * 
	 */

	class OnChronometerTickListenerImpl implements // 计时监听事件，随时随地的监听时间的变化
			OnChronometerTickListener {
		@Override
		public void onChronometerTick(Chronometer chronometer) {
			// String time = chronometer.getText().toString();

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Keys.CLOSE_ACTIVITY) {
			setResult(resultCode, data);
			finish();
		}

	}

	/**
	 * FIXME 亲只需要在这个方法把选中的文档目录已list的形式传过去即可
	 * 
	 * @param view
	 */
	public void sendfiles() {
		Intent intent = new Intent(this, PublishActivity.class);
		Bundle bundle = new Bundle();
		bundle.putStringArrayList(Keys.PHOTOS_LIST_TO_OTHER_ACTIVITY, filelist);
		intent.putExtras(bundle);
		setResult(Keys.CLOSE_ACTIVITY, intent);
		finish();

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			// land do nothing is ok

		} else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			// port do nothing is ok
		}
	}
}


/**
 * 计时使用方法 public class ButtonClickListener implements View.OnClickListener {
 * 
 * @Override public void onClick(View v) { switch (v.getId()) { case
 *           R.id.btn_start: chronometer.start();// 开始计时 break; case
 *           R.id.btn_stop: chronometer.stop();// 停止计时 break; case
 *           R.id.btn_base:
 *           chronometer.setBase(SystemClock.elapsedRealtime());// 复位键 break;
 *           case R.id.btn_format: chronometer.setFormat("显示时间：%s.");// 更改时间显示格式
 *           break; default: break; } } } }
 * 
 *           详细出处参考：http://www.jb51.net/article/33109.htm
 */
