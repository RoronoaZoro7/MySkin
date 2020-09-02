package com.example.administrator.lib.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

/**
 * Created by Administrator on 2020/9/2.
 */

public class SkinResourcesUtils {
//    从此包路径中拿取资源
    private String mSkinPkgName;
//    是否使用皮肤包
    private boolean isDefaultSkin = true;

//    自己包里的resource
    private Resources  mAppResources;
//    皮肤包的resource
    private Resources mSkinResources;

    public SkinResourcesUtils(Context context) {
       mAppResources = context.getResources();
    }

    private static SkinResourcesUtils instance;

    public static void init(Context context){
        if(instance == null){
            synchronized (SkinResourcesUtils.class){
                if(instance == null){
                    instance = new SkinResourcesUtils(context);
                }
            }
        }
    }

    public static SkinResourcesUtils getInstance() {
        return instance;
    }

    public void reset(){
        mSkinResources  = null;
        mSkinPkgName = "";
        isDefaultSkin = true;
    }

    /**
     * 赋值皮肤包resources
     * @param resources 皮肤包rescues
     * @param pkgName 包路径
     */
    public void applySkin(Resources resources, String pkgName) {
        mSkinResources = resources;
        mSkinPkgName = pkgName;
        //是否使用默认皮肤
        isDefaultSkin = TextUtils.isEmpty(pkgName) || resources == null;
    }

    /**
     * 1.通过原始app中的resId(R.color.XX)获取到自己的 名字
     * 2.根据名字和类型获取皮肤包中的ID
     */
    public int getIdentifier(int resId){
        if(isDefaultSkin){
            return resId;
        }
        String resourceEntryName = mAppResources.getResourceEntryName(resId);
        String resourceTypeName = mAppResources.getResourceTypeName(resId);
        int identifier = mSkinResources.getIdentifier(resourceEntryName, resourceTypeName, mSkinPkgName);
        return identifier;
    }

    /**
     * 输入主APP的ID，到皮肤APK文件中去找到对应ID的颜色值
     * @param resId
     * @return
     */
    public int getColor(int resId){
        if(isDefaultSkin){
            return mAppResources.getColor(resId);
        }
        int skinId=getIdentifier(resId);
        if(skinId==0){
            return mAppResources.getColor(resId);
        }
        return mSkinResources.getColor(skinId);
    }

    public ColorStateList getColorStateList(int resId) {
        if (isDefaultSkin) {
            return mAppResources.getColorStateList(resId);
        }
        int skinId = getIdentifier(resId);
        if (skinId == 0) {
            return mAppResources.getColorStateList(resId);
        }
        return mSkinResources.getColorStateList(skinId);
    }

    public Drawable getDrawable(int resId) {
        if (isDefaultSkin) {
            return mAppResources.getDrawable(resId);
        }
        //通过 app的resource 获取id 对应的 资源名 与 资源类型
        //找到 皮肤包 匹配 的 资源名资源类型 的 皮肤包的 资源 ID
        int skinId = getIdentifier(resId);
        if (skinId == 0) {
            return mAppResources.getDrawable(resId);
        }
        return mSkinResources.getDrawable(skinId);
    }


    /**
     * 可能是Color 也可能是drawable
     *
     * @return
     */
    public Object getBackground(int resId) {
        String resourceTypeName = mAppResources.getResourceTypeName(resId);

        if ("color".equals(resourceTypeName)) {
            return getColor(resId);
        } else {
            // drawable
            return getDrawable(resId);
        }
    }


}
