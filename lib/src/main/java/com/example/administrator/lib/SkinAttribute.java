package com.example.administrator.lib;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.lib.utils.SkinResourcesUtils;
import com.example.administrator.lib.utils.SkinThemeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;

/**
 * Created by Administrator on 2020/9/2.
 */

public class SkinAttribute {

    private static final List<String> mAttributes = new ArrayList<>();

    static {
        mAttributes.add("background");
        mAttributes.add("src");
        mAttributes.add("textColor");
        mAttributes.add("drawableLeft");
        mAttributes.add("drawableTop");
        mAttributes.add("drawableRight");
        mAttributes.add("drawableBottom");
    }

    //记录换肤需要操作的View与属性信息
    private List<SkinView> mSkinViews = new ArrayList<>();

    public void look(View view, AttributeSet attrs) {
        List<SkinPair> mSkinPars = new ArrayList<>();
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            String attributeName = attrs.getAttributeName(i);
            if (mAttributes.contains(attributeName)) {
                String attributeValue = attrs.getAttributeValue(i);
                //#号固定写法的不可以进行换肤
                if (attributeValue.startsWith("#")) {
                    continue;
                }
                int resId;
                //搜集？ @符号开头的资源id
                if (attributeName.startsWith("?")) {
                    int attrId = Integer.parseInt(attributeValue.substring(1));
                    resId = SkinThemeUtils.getResId(view.getContext(), new int[]{attrId})[0];
                } else {
                    resId = Integer.parseInt(attributeValue.substring(1));
                }
                mSkinPars.add(new SkinPair(attributeName, resId));
            }
        }


        if (!mSkinPars.isEmpty() || view instanceof SkinViewSupport) {
            SkinView skinView = new SkinView(view, mSkinPars);
            // 如果选择过皮肤 ，调用 一次 applySkin 加载皮肤的资源
            skinView.applySkin();
            mSkinViews.add(skinView);
        }

    }

    static class SkinView {
        //view自身
        View view;
        //view的属性，以及id
        List<SkinPair> skinPairs;

        public SkinView(View view, List<SkinPair> skinPairs) {
            this.view = view;
            this.skinPairs = skinPairs;
        }

        /**
         * 对一个View中的所有的属性进行修改
         */
        public void applySkin() {
            applySkinSupport();
            for (SkinPair skinPair : skinPairs) {
                Drawable left = null, top = null, right = null, bottom = null;
                switch (skinPair.attributeName) {
                    case "background":
                        Object background = SkinResourcesUtils.getInstance().getBackground(skinPair.resId);
                        if (background instanceof Integer) {
                            view.setBackgroundColor((int) background);
                        } else {
                            view.setBackground((Drawable) background);
                        }
                        break;
                    case "src":
                        Object backgroundSrc = SkinResourcesUtils.getInstance().getBackground(skinPair.resId);
                        if (backgroundSrc instanceof Integer) {
                            ((ImageView) view).setImageDrawable(new ColorDrawable((Integer) backgroundSrc));
                        } else {
                            ((ImageView) view).setImageDrawable((Drawable) backgroundSrc);
                        }
                        break;
                    case "textColor":
                        ((TextView) view).setTextColor(SkinResourcesUtils.getInstance().getColorStateList(skinPair.resId));
                        break;
                    case "drawableLeft":
                        left = SkinResourcesUtils.getInstance().getDrawable(skinPair.resId);
                        break;
                    case "drawableTop":
                        top = SkinResourcesUtils.getInstance().getDrawable(skinPair.resId);
                        break;
                    case "drawableRight":
                        right = SkinResourcesUtils.getInstance().getDrawable(skinPair.resId);
                        break;
                    case "drawableBottom":
                        bottom = SkinResourcesUtils.getInstance().getDrawable(skinPair.resId);
                        break;

                }
                if (null != left || null != right || null != top || null != bottom) {
                    ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(left, top, right,
                            bottom);
                }
            }
        }

        private void applySkinSupport() {
            if (view instanceof SkinViewSupport) {
                ((SkinViewSupport) view).applySkin();
            }
        }
    }

    static class SkinPair {
        //View的资源名称 textColor等。
        String attributeName;
        // view的资源名称对应的Id
        int resId;

        public SkinPair(String attributeName, int resId) {
            this.attributeName = attributeName;
            this.resId = resId;
        }
    }


    /**
     * 对所有的view中的所有的属性进行皮肤修改
     */
    public void applySkin() {
        for (SkinView mSkinView : mSkinViews) {
            mSkinView.applySkin();
        }
    }

    private static final String TAG = "SkinAttribute";
}
