package com.pac.pacpapers.ui;

import java.util.ArrayList;

import com.pac.pacpapers.R;
import com.pac.pacpapers.WallpaperActivity;
import com.pac.pacpapers.types.WallpaperCategory;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

public class WallpaperPreviewFragment extends Fragment {

    static final String TAG = "PreviewFragment";
    
    View mView;
    GridView mGrid;
    TextView pageNum;

    ArrayList<WallpaperCategory> mArray;
    public int selectedCategory = 0; 

    public void setCategory(int cat) {
        selectedCategory = cat;
        
        //update this bad boy!
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.activity_wallpaper, container, false);
        pageNum = (TextView) mView.findViewById(R.id.textView1);
        pageNum.setText("total :");
        
        mGrid = (GridView) mView.findViewById(R.id.GridView1);
        // TODO attach the adapter with the lasy ass loader
        
        // TODO update the view to show the list
        
        return mView;
    }
    
    /*
    public ArrayList<WallpaperCategory> getCategories() {
        return mActivity.mCategoryAdapter.getCategories();
    }
     
    protected Wallpaper getWallpaper(int realIndex) {
        return getCategories().get(selectedCategory).getWallpapers().get(realIndex);
    }
    */
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*class ThumbnailClickListener implements View.OnClickListener {
        Wallpaper wall;

        public ThumbnailClickListener(Wallpaper wallpaper) {
            this.wall = wallpaper;
        }

        @Override
        public void onClick(View v) {
            Intent preview = new Intent(mActivity, Preview.class);
            preview.putExtra("wp", wall.getUrl());
            startActivity(preview);
        }
    }*/
}

