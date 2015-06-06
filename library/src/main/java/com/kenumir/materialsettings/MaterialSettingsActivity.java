package com.kenumir.materialsettings;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.kenumir.materialsettings.storage.SimpleStorageInterface;
import com.kenumir.materialsettings.storage.StorageInterface;

import java.util.Map;

/**
 * Created by Kenumir on 2015-04-07.
 */
public abstract class MaterialSettingsActivity extends AppCompatActivity {

	private static String SAVE_PREFIX = "SSI_";

	private MaterialSettingsFragment fragment;
    public MaterialSettingsActivity context;

    public Toolbar toolbar;
    public CollapsingToolbarLayout toolbarLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_material_settings_base);
        context=this;

		toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarLayout= (CollapsingToolbarLayout) findViewById(R.id.toolbarLayout);

		setSupportActionBar(toolbar);

		fragment = (MaterialSettingsFragment) getSupportFragmentManager().findFragmentById(R.id.material_settings_fragment);

		if (savedInstanceState != null) {
			StorageInterface mStorageInterface = fragment.getStorageInterface();
			for(String key : savedInstanceState.keySet()) {
				if (key.startsWith(SAVE_PREFIX)) {
					String keyName = key.substring(SAVE_PREFIX.length());
					Object value = savedInstanceState.get(key);
					Log.i("tests", "k: " + key + " k2: " + keyName  +", t=" + value.getClass().getName() + ", v=" + value);
					if (value instanceof String) {
						mStorageInterface.save(keyName, (String) value);
					} else if (value instanceof Integer) {
						mStorageInterface.save(keyName, (Integer) value);
					} else if (value instanceof Float) {
						mStorageInterface.save(keyName, (Float) value);
					} else if (value instanceof Long) {
						mStorageInterface.save(keyName, (Long) value);
					} else if (value instanceof Boolean) {
						mStorageInterface.save(keyName, (Boolean) value);
					} else {
						mStorageInterface.save(keyName, value.toString());
					}
				}
			}
		}
	}

	//public abstract MaterialSettingsFragment initMaterialSettingsFragment();

	@Override
	public void onSaveInstanceState(Bundle outState) {
		StorageInterface si = getStorageInterface();
		if (si instanceof SimpleStorageInterface) {
			saveAll();
			Map<String, ?> all = ((SimpleStorageInterface) si).getAll();
			if (all.size() > 0) {
				// save to bundle
				for(String key : all.keySet()) {
					Object value = all.get(key);
					if (value instanceof String) {
						outState.putString(SAVE_PREFIX + key, (String) value);
					} else if (value instanceof Integer) {
						outState.putInt(SAVE_PREFIX + key, (Integer) value);
					} else if (value instanceof Float) {
						outState.putFloat(SAVE_PREFIX + key, (Float) value);
					} else if (value instanceof Long) {
						outState.putString(SAVE_PREFIX + key, (String) value);
					} else if (value instanceof Boolean) {
						outState.putBoolean(SAVE_PREFIX + key, (Boolean) value);
					} else {
						outState.putString(SAVE_PREFIX + key, value.toString());
					}
				}
			}
		}
		super.onSaveInstanceState(outState);
	}

	public void addItem(MaterialSettingsItem item) {
		if (fragment != null)
			fragment.addItem(item);
	}

	public void saveAll() {
		if (fragment != null) {
			fragment.saveAll();
		}
	}

	public MaterialSettingsItem getItem(String keyName) {
		if (fragment != null) {
			return fragment.getItem(keyName);
		} else {
			return null;
		}
	}

	public StorageInterface getStorageInterface() {
		if (fragment != null) {
			return fragment.getStorageInterface();
		} else {
			return null;
		}
	}

	public MaterialSettingsFragment getFragment() {
		return fragment;
	}

	public FrameLayout getContentFrame(MaterialSettingsFragment.ContentFrames frame) {
		if (fragment != null)
			return fragment.getContentFrame(frame);
		else
			return null;
	}

	public StorageInterface initStorageInterface() {
		return null;
	}



    public void setToolbarLayout(String title){

        toolbarLayout.setTitle(title);

        toolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
//        toolbarLayout.setContentScrimColor(Color.WHITE);
        toolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.ms_white));

    }

    public void setToolbar(int icon,String title,String subTitle,View.OnClickListener navigation,Toolbar.OnMenuItemClickListener menuItemClickListener){
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(icon);
        toolbar.setTitle(title);
        toolbar.setSubtitle(subTitle);



        toolbar.setNavigationOnClickListener(navigation);

        if (menuItemClickListener!=null){
            toolbar.setOnMenuItemClickListener(menuItemClickListener);

        }


    }


}
