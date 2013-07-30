package com.pac.pacpapers.adapters;

import java.util.ArrayList;

import com.koushikdutta.urlimageviewhelper.UrlImageViewCallback;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.pac.pacpapers.R;
import com.pac.pacpapers.types.Wallpaper;
import com.pac.pacpapers.types.WallpaperCategory;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WallpaperPreviewAdapter extends ArrayAdapter<WallpaperCategory> {
	public final int SHOW_ALL_CATS = -1;
	
	ArrayList<WallpaperCategory> mList;
	ArrayList<Wallpaper> mWallList;

	private int mCat = -1;
	
	public WallpaperPreviewAdapter(Context context, int resource,  
			ArrayList<WallpaperCategory> objects) {
		super(context, resource,  objects);
		// TODO init this 
		mList = objects;
		init_arrays();
	}

	private void init_arrays(){
		mWallList = new ArrayList<Wallpaper>();
		if (mCat == SHOW_ALL_CATS){
			for (int i = 0; i < mList.size() ; i++){
				ArrayList<Wallpaper> mTemp = (ArrayList<Wallpaper>) mList.get(i).getWallpapers();
				for (int j = 0; j<mTemp.size();j++){
					// TODO Loop it up and ADD ALL THE WALLPAPERS
					mWallList.add(mTemp.get(j));
				}
			}
		} else {
			// Only add the current cat
			mWallList = (ArrayList<Wallpaper>) mList.get(mCat).getWallpapers();
		}
		
		//TODO refresh the list CLEAR ALL and start over (image cache should stop any crapyness)
		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO draw a veiw that looks epic sauces... 
		
		// TODO recylce the old views
		
		// TODO Fix Tylers broken stuffs
		Log.d("lsitcrap"," " + mWallList.size());
		

        View v = View.inflate(getContext(), R.layout.thumbnail, null);
        
        TextView name = (TextView) v.findViewById(R.id.name);
        TextView author = (TextView) v.findViewById(R.id.author);
        ImageView thumbnail = (ImageView) v.findViewById(R.id.thumb);
        
        name.setText("BOOBIES");
        author.setText("OF DOOM");
        
        //thumbnail.setAnimation(null);
        // yep, that's it. it handles the downloading and showing an interstitial image automagically.
        UrlImageViewHelper.setUrlDrawable(thumbnail, mWallList.get(position).getThumbUrl(), R.drawable.ic_launcher, new UrlImageViewCallback() {
            @Override
            public void onLoaded(ImageView imageView, Drawable loadedBitmap, String url, boolean loadedFromCache) {
        		Log.d("lsitcrap","loaded " + url);
        		
            }

        });       
        Log.d("lsitcrap"," " + v.getWidth() + " " + v.getHeight());
        return v;
	}
	
	

}
