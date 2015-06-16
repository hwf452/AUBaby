package com.halong.aubaby.contant;

import java.io.File;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.halong.aubaby.R;


public class DownLoadAUBabyFile {
	
	public  void downLoadFile(final Context mContext,String localFileName, final String netFileName,
			String fileUrl, final TextView progressTextView) {
		// TODO Auto-generated method stub
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
				Toast.makeText(mContext, R.string.can_not_creat_directory, Toast.LENGTH_SHORT).show();

				return;

			}

		}
		localFileName = "/" + localFileName;

		String apkPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + Keys.AUBABY + localFileName;
		File f = new File(apkPath);
		if (f.exists()) {
			mContext.startActivity(AndroidFileUtil.openFile(f));
			return;
			// f.delete();
		}
		FinalHttp fh = new FinalHttp();
		fh.download(fileUrl, apkPath, new AjaxCallBack<File>() {
			@Override
			public void onStart() {
				super.onStart();
				Toast.makeText(mContext, "开始下载", Toast.LENGTH_SHORT).show();
				progressTextView.setText("进度：" + 0 + "%");
				progressTextView.setVisibility(View.VISIBLE);
			}

			@SuppressLint("DefaultLocale")
			@Override
			public void onLoading(long count, long current) {
				super.onLoading(count, current);
				int progress = 0;
				if (current != count && current != 0) {
					progress = (int) (current / (float) count * 100);
				} else {
					progress = 100;
				}
				progressTextView.setText("进度：" + progress + "%");

			}

			@Override
			public void onSuccess(File t) {
				super.onSuccess(t);
				Toast.makeText(mContext, "下载完成", Toast.LENGTH_SHORT).show();
				onDownLoadSuccess();
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				Toast.makeText(mContext, netFileName + "下载失败",
						Toast.LENGTH_SHORT).show();
				progressTextView.setText("下载失败");
			}
		});

	}
	public  void onDownLoadSuccess() {
		// TODO Auto-generated method stub

	}
	public boolean fileIsExists(String fileName) {
		fileName = "/" + fileName;
		String filePath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + Keys.AUBABY + fileName;
		File f = new File(filePath);
		return f.exists();
	}
}
