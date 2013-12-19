/**
 * 
 */
package com.islamsamak.aqarmap.ui.viewmodel;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.islamsamak.aqarmap.R;
import com.islamsamak.aqarmap.model.City;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * @author islam
 * 
 */
public class ProductAdapter<T> extends BaseAdapter {

	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

	private ImageLoader mImageLoader = null;

	private LayoutInflater mInflater = null;

	private List<T> mItemsList = null;

	private DisplayImageOptions mOptions;

	private class ViewHolder {

		public TextView text;

		public ImageView image;
	}

	public ProductAdapter(Context context, List<T> itemsList,
			ImageLoader imageLoader) {

		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		mImageLoader = imageLoader;

		mItemsList = itemsList;

		mOptions = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher)
				.resetViewBeforeLoading(true).cacheOnDisc(true)
				.cacheInMemory(true).imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(300)).build();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return mItemsList.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		return mItemsList.get(position);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = convertView;

		final ViewHolder holder;

		if (convertView == null) {

			view = mInflater.inflate(R.layout.simple_list_item, parent, false);

			holder = new ViewHolder();

			holder.text = (TextView) view.findViewById(android.R.id.text1);

			holder.image = (ImageView) view.findViewById(R.id.image);

			view.setTag(holder);

		} else {

			holder = (ViewHolder) view.getTag();
		}

		// FIXME: Enhance this bad logic by creating a new model contains the
		// common field between Favorit and Cart

		String imageUrl = null;

		String title = null;

		T item = mItemsList.get(position);

		if (item instanceof City) {

			title = ((City) item).getTitle();

			imageUrl = ((City) item).getThumbnail();
		}

		if (!TextUtils.isEmpty(title)) {

			holder.text.setText(title);
		}

		if (!TextUtils.isEmpty(imageUrl) && mImageLoader.isInited()) {

			mImageLoader.displayImage(imageUrl, holder.image, mOptions,
					animateFirstListener);
		}

		return view;
	}

	protected static class AnimateFirstDisplayListener extends
			SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {

			if (loadedImage != null) {

				ImageView imageView = (ImageView) view;

				boolean firstDisplay = !displayedImages.contains(imageUri);

				if (firstDisplay) {

					FadeInBitmapDisplayer.animate(imageView, 500);

					displayedImages.add(imageUri);
				}
			}
		}
	}
}
