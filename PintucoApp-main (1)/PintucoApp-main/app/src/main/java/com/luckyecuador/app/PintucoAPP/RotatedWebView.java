package com.luckyecuador.app.PintucoAPP;



import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;


public class RotatedWebView extends WebView {
    public RotatedWebView(Context context) {
        super(context);
    }

    public RotatedWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RotatedWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredWidth = MeasureSpec.getSize(heightMeasureSpec);
        int desiredHeight = MeasureSpec.getSize(widthMeasureSpec);
        setRotation(90);

        setTranslationX(-500);
        setMeasuredDimension(desiredHeight, desiredWidth);
    }
}