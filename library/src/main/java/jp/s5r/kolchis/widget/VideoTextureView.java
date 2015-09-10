package jp.s5r.kolchis.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.TextureView;

public class VideoTextureView extends TextureView implements ViewRatioSettable {

    private static final float MAX_ASPECT_RATIO_DEFORMATION_PERCENT = 0.01f;

    private float aspectRatio;

    public VideoTextureView(Context context) {
        super(context);
    }

    public VideoTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setAspectRatio(float aspectRatio) {
        if (this.aspectRatio != aspectRatio) {
            this.aspectRatio = aspectRatio;
            requestLayout();
        }
    }

    @Override
    public float getAspectRatio() {
        return aspectRatio;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        if (aspectRatio != 0) {
            float viewAspectRatio = (float) width / height;
            float aspectDeformation = aspectRatio / viewAspectRatio - 1;
            if (aspectDeformation > MAX_ASPECT_RATIO_DEFORMATION_PERCENT) {
                height = (int) (width / aspectRatio);
            } else if (aspectDeformation < -MAX_ASPECT_RATIO_DEFORMATION_PERCENT) {
                width = (int) (height * aspectRatio);
            }
        }
        setMeasuredDimension(width, height);
    }
}
