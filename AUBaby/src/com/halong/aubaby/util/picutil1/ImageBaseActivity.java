
package com.halong.aubaby.util.picutil1;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;

import com.halong.aubaby.BackActivity;
import com.halong.aubaby.R;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @author 清除缓存
 */
public abstract class ImageBaseActivity extends BackActivity {

	protected ImageLoader imageLoader = ImageLoader.getInstance();

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.item_clear_memory_cache:
				imageLoader.clearMemoryCache();
				return true;
			case R.id.item_clear_disc_cache:
				imageLoader.clearDiscCache();
				return true;
			default:
				return false;
		}
	}
}
