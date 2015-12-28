package com.exemple.locdvd;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;


public class MonTextView extends TextView {
    public MonTextView(Context context) {
        super(context);
    }

    public MonTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.mesAttributs);
        boolean valeurMonAttribut1 = typedArray.getBoolean(R.styleable.mesAttributs_monAttribut1, false);
        String valeurMonAttribut2 = typedArray.getString(R.styleable.mesAttributs_monAttribut2);

    }
}
