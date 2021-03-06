package com.kenumir.materialsettings;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kenumir.materialsettings.storage.StorageInterface;

/**
 * Created by Kenumir on 2015-03-16.
 */
public abstract class MaterialSettingsItem {

	protected MaterialSettingsFragment mContext;
	protected MaterialSettingsFragment mMaterialSettings;
	protected String name;

    public   View v;
	public MaterialSettingsItem(MaterialSettingsFragment ctx, String name) {
		this.mContext = ctx;
		//if (ctx instanceof MaterialSettingsFragment)
		//	this.mMaterialSettings = (MaterialSettingsFragment) ctx;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public View initView(ViewGroup parent, int res) {
		return LayoutInflater.from(mContext.getActivity()).inflate(res, parent, false);
	}

	public View getView(ViewGroup parent) {
		if (getViewResource() > 0) {
			 v = initView(parent, getViewResource());
			setupView(v);

			return v;
		} else
			return null;
	}

   public void  setVisibility(int visibility){
       if (v!=null){
           v.setVisibility(visibility);
       }


   }


	public void setMaterialSettings(MaterialSettingsFragment m) {
		mMaterialSettings = m;
	}

	public StorageInterface getStorageInterface() {
		if (mMaterialSettings != null)
			return mMaterialSettings.getStorageInterface();
		else
			return null;
	}

	public abstract int getViewResource();
	public abstract void setupView(View v);
	public abstract void save();

}
