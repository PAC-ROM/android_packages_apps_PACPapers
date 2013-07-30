
package com.pac.pacpapers;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewCallback;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.pac.pacpapers.adapters.NavigationBarCategoryAdapater;
import com.pac.pacpapers.parsers.ManifestXmlParser;
import com.pac.pacpapers.types.Wallpaper;
import com.pac.pacpapers.types.WallpaperCategory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class WallpaperActivity extends Activity {

    public final String TAG = "PacPapers";
    protected static final String MANIFEST = "wallpaper_manifest.xml";
    protected static final int THUMBS_TO_SHOW = 4;

    /*
     * pull the manifest from the web server specified in config.xml or pull
     * wallpaper_manifest.xml from local assets/ folder for testing
     */
    public static final boolean USE_LOCAL_MANIFEST = false;

    ArrayList<WallpaperCategory> categories = null;
    ProgressDialog mLoadingDialog;
    WallpaperPreviewFragment mPreviewFragment;
    NavigationBarCategoryAdapater mCategoryAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLoadingDialog = new ProgressDialog(this);
        mLoadingDialog.setCancelable(false);
        mLoadingDialog.setIndeterminate(true);
        mLoadingDialog.setMessage("Retreiving wallpapers from server...");

        mLoadingDialog.show();
        new LoadWallpaperManifest().execute();
    }

    protected void loadPreviewFragment() {
        mPreviewFragment = new WallpaperPreviewFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(android.R.id.content, mPreviewFragment);
        ft.commit();

        ActionBar ab = getActionBar();
        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        ab.setListNavigationCallbacks(mCategoryAdapter = new NavigationBarCategoryAdapater(
                getApplicationContext(),
                categories),
                new ActionBar.OnNavigationListener() {
                    @Override
                    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                        setCategory(itemPosition);
                        return true;
                    }
                });
        ab.setDisplayShowTitleEnabled(false);
    }

    protected void setCategory(int cat) {
        mPreviewFragment.setCategory(cat);
    }

    public static class WallpaperPreviewFragment extends Fragment {

        static final String TAG = "PreviewFragment";
        WallpaperActivity mActivity;
        View mView;

        public int currentPage = -1;
        TextView pageNum;
        public int selectedCategory = 0; // *should* be <ALL> wallpapers

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            mActivity = (WallpaperActivity) getActivity();
        }

        public void setCategory(int cat) {
            selectedCategory = cat;
            currentPage = -1;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            mView = inflater.inflate(R.layout.activity_wallpaper, container, false);
            pageNum = (TextView) mView.findViewById(R.id.textView1);
            return mView;
        }

        public ArrayList<WallpaperCategory> getCategories() {
            return mActivity.mCategoryAdapter.getCategories();
        }

        protected Wallpaper getWallpaper(int realIndex) {
            return getCategories().get(selectedCategory).getWallpapers().get(realIndex);
        }

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

    public static String getDlDir(Context c) {
        String configFolder = getResourceString(c, R.string.config_wallpaper_download_loc);
        if (configFolder != null && !configFolder.isEmpty()) {
            return new File(Environment.getExternalStorageDirectory(), configFolder)
                    .getAbsolutePath() + "/";
        } else {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
    }

    public static String getSvDir(Context c) {
        String configFolder = getResourceString(c, R.string.config_wallpaper_sdcard_dl_location);
        if (configFolder != null && !configFolder.isEmpty()) {
            return new File(Environment.getExternalStorageDirectory(), configFolder)
                    .getAbsolutePath() + "/";
        } else {
            return null;
        }
    }

    protected String getWallpaperDestinationPath() {
        String configFolder = getResourceString(R.string.config_wallpaper_sdcard_dl_location);
        if (configFolder != null && !configFolder.isEmpty()) {
            return new File(Environment.getExternalStorageDirectory(), configFolder)
                    .getAbsolutePath();
        }
        // couldn't find resource?
        return null;
    }

    protected String getResourceString(int stringId) {
        return getApplicationContext().getResources().getString(stringId);
    }

    public static String getResourceString(Context c, int id) {
        return c.getResources().getString(id);
    }

    private class LoadWallpaperManifest extends
            AsyncTask<Void, Boolean, ArrayList<WallpaperCategory>> {

        @Override
        protected ArrayList<WallpaperCategory> doInBackground(Void... v) {

            try {
                InputStream input = null;

                if (USE_LOCAL_MANIFEST) {
                    input = getApplicationContext().getAssets().open(MANIFEST);
                } else {
                    URL url = new URL(getResourceString(R.string.config_wallpaper_manifest_url));
                    URLConnection connection = url.openConnection();
                    connection.connect();
                    int fileLength = connection.getContentLength();
                    input = new BufferedInputStream(url.openStream());
                }
                
                OutputStream output = getApplicationContext().openFileOutput(
                        MANIFEST, MODE_PRIVATE);
                byte data[] = new byte[1024];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();

                // file finished downloading, parse it!
                ManifestXmlParser parser = new ManifestXmlParser();
                return parser.parse(new File(getApplicationContext().getFilesDir(), MANIFEST),
                        getApplicationContext());
            } catch (Exception e) {
                Log.d(TAG, "Exception!", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<WallpaperCategory> result) {
            categories = result;
            if (categories != null)
                loadPreviewFragment();

            mLoadingDialog.cancel();
            super.onPostExecute(result);
        }
    }

}
