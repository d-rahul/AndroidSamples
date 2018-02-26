package views;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RelativeViewNorm extends RelativeLayout {

    private Context mContext;

    private float mAmplitudeRatio;

    private int mAmplitudePos;
    private int mLayoutWidth;
    private int mLayoutHeight;
    private int mMaxWidth;
    private int mMinWidth;
    private int mAmplitudeAnimationDuration = 300;

    private List<NormalDraw_> mNormalDrawViews;
    private List<Integer> mSpacings;

    private AnimatorSet mAnimatorSet1;

    public RelativeViewNorm(Context context) {
        super(context);
        init(context);
    }

    public RelativeViewNorm(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RelativeViewNorm(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mLayoutWidth = w;
        mLayoutHeight = h;
    }

    public void init(Context context) {
        mNormalDrawViews = new ArrayList<>();
        mSpacings = new ArrayList<>();
        mContext = context;
        mAmplitudePos = 1;
    }

    public void addWaveView(float amplitude) {
        NormalDraw_ waveView = new NormalDraw_(mContext);
        int width = getWidthRandom();
        LayoutParams params = new LayoutParams(
                width,
                LayoutParams.WRAP_CONTENT
        );
        params.setMargins(getLeftSpacing(width), 0, 0, 0);
        waveView.setLayoutParams(params);
        waveView.setAmplitudeRatio(amplitude);
        this.addView(waveView);
        mNormalDrawViews.add(waveView);
        initAnim(waveView, amplitude);
    }

    public void removeWaveView(NormalDraw_ waveView) {
        this.removeView(waveView);
    }

    public void setAmplitudeRatio(float amplitudeRatio) {
        this.mAmplitudeRatio = getAmplitudeInRane100(amplitudeRatio);
    }

    public float getAmplitudeRatio() {
        return mAmplitudeRatio;
    }

    public int getLeftSpacing(int width) {

        if (mSpacings.size() == 0) {
            addSpacingForLeftMargin();
        }

        int spacingW = mSpacings.get(getRandomNumber(0, mSpacings.size()));

        if ((spacingW + width) > mLayoutWidth) {
            spacingW = spacingW - 50;
            if ((spacingW + width) > mLayoutWidth) {
                spacingW = spacingW - 50;
                if ((spacingW + width) > mLayoutWidth) {
                    spacingW = spacingW - 50;
                    if ((spacingW + width) > mLayoutWidth) {
                        spacingW = spacingW - 50;
                        if ((spacingW + width) > mLayoutWidth) {
                            spacingW = spacingW - 50;
                            if ((spacingW + width) > mLayoutWidth) {
                                spacingW = spacingW - 50;
                                if ((spacingW + width) > mLayoutWidth) {
                                    spacingW = 0;
                                }
                            }
                        }
                    }
                }
            }
        }
        return spacingW;
    }

    public int getWidthRandom() {

        if (mMaxWidth == 0) {
            mMaxWidth = (int) (mLayoutWidth * 0.8);
        }
        if (mMinWidth == 0) {
            mMinWidth = (int) (mLayoutWidth * 0.3);
        }

        return getRandomNumber(mMinWidth, mMaxWidth);
    }


    public int getRandomNumber(int from/*inclusive*/, int to/*exclusive*/) {
        Random r = new Random();
        int i1 = r.nextInt(to - from) + from;
        return i1;
    }

    public void addSpacingForLeftMargin() {

        for (int i = 0; i <= mLayoutWidth; i++) {
            if (i == 0) {
                mSpacings.add(i);
            } else if ((i % 50) == 0) {
                mSpacings.add(i);
            }
        }

    }

    public void initAnim(NormalDraw_ mWaveView, float amplitude) {
        ObjectAnimator amplitudeAnim = ObjectAnimator.ofFloat(
                mWaveView, "amplitudeRatio", 0.1f, amplitude);
        amplitudeAnim.setRepeatCount(1);
        amplitudeAnim.setRepeatMode(ValueAnimator.REVERSE);
        amplitudeAnim.setDuration(mAmplitudeAnimationDuration);
        amplitudeAnim.setInterpolator(new LinearInterpolator());
        amplitudeAnim.start();
    }

    public float getAmplitudeInRane100(float x) {
        float amp = x / 150;

        if (amp < 5) {
            return 5;
        } else if (amp > 100) {
            return 100;
        } else {
            return amp;
        }
    }

    public void addAmplitude(float amplitudeRatio) {
        setAmplitudeRatio(amplitudeRatio);
        addWaveView(mAmplitudeRatio);
        if (mNormalDrawViews.size() > 5) {
            removeWaveView(mNormalDrawViews.get(0));
        }
        mAmplitudePos++;
    }

}
