/*
 * Copyright (c) 2018. http://moswag@cytex.com- All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * If you use or distribute this project then you MUST ADD A COPY OF LICENCE
 * along with the project.
 *  Written by Webster Moswa <webstermoswa11@Gmail.com>, 2018.
 */

package com.cytex.moswag.util;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class FontText extends TextView {

    String TAG = getClass().getName();
    private Context mContext;
    private String ttfName;

    public FontText(Context context) {
        super(context);
        this.mContext = context;
    }

    public FontText(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.mContext = context;

        // Typeface.createFromAsset doesn't work in the layout editor.
        // Skipping...
        if (isInEditMode()) {
            return;
        }

        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            this.ttfName = attrs.getAttributeValue(Utils.ATTRIBUTE_SCHEMA,
                    Utils.ATTRIBUTE_TTF_KEY);

            if (null != ttfName)
                init();
        }

    }

    public FontText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
    }

    private void init() {
        Typeface font = Utils.getFonts(mContext, ttfName);
        if (null != font)
            setTypeface(font);
    }

    @Override
    public void setTypeface(Typeface tf) {

        super.setTypeface(tf);
    }

}