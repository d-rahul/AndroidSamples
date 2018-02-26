package views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;

public class NormalDraw_ extends View {
    int axisYLength;
    int axisYStart;
    int viewH;
    int viewW;

    double lb = -3.5d;
    double maxValue = NormalCalc.phi(0.0d);
    double ub = 3.5d;

    float[] pdfCurve;

    //100 is max height
    private float mAmplitudeRatio = 100.0f;
    private int fillColor;

    public String[] mWaveColor = {"#90FF0F00", "#90FF5500", "#90FF8700", "#90FF0000", "#90FF2400", "#90FF3F00", "#90FF3700", "#90FF6D00", "#90FFA700"};

    public NormalDraw_(Context context) {
        super(context);
        calcValues();
    }

    public NormalDraw_(Context context, AttributeSet attrs) {
        super(context, attrs);
        calcValues();
    }

    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.viewW = w;
        this.viewH = h;
    }

    public void setAmplitudeRatio(float amplitudeRatio) {
        if (mAmplitudeRatio != amplitudeRatio) {
            mAmplitudeRatio = amplitudeRatio;
            invalidate();
        }
    }

    public float getAmplitudeRatio() {
        return mAmplitudeRatio;
    }

    protected void onDraw(Canvas canvas) {
        int j;
//        this.axisYStart = (this.viewH * 8) / 10;
//        this.axisYLength = (this.viewH * 7) / 10;
        this.axisYStart = this.viewH;
        this.axisYLength = (int) ((this.viewH / 100) * mAmplitudeRatio);
        Paint curvePaint = new Paint(1);
        curvePaint.setStyle(Style.FILL);
        curvePaint.setStrokeWidth(2.0f);
        curvePaint.setColor(fillColor);
        Path path = new Path();
        for (j = 0; j < this.pdfCurve.length; j++) {
            if (j == 0) {
                path.moveTo(0.0f, (float) this.axisYStart);
            } else if (j < this.pdfCurve.length - 1) {
                path.quadTo((float) ((this.viewW * j) / 100), ((float) this.axisYStart) - (this.pdfCurve[j] * ((float) this.axisYLength)), (float) (((j + 1) * this.viewW) / 100), ((float) this.axisYStart) - (this.pdfCurve[j + 1] * ((float) this.axisYLength)));
            } else {
                path.lineTo((float) this.viewW, ((float) this.axisYStart) - (this.pdfCurve[j] * ((float) this.axisYLength)));
            }
        }
        canvas.drawPath(path, curvePaint);
    }

    public void calcValues() {
        fillColor = getRandomeColor();
        double incrementSize = (this.ub - this.lb) / 100.0d;
        System.out.println(Double.toString(this.maxValue));
        this.pdfCurve = new float[100];
        for (int i = 0; i < 100; i++) {
            this.pdfCurve[i] = (float) (NormalCalc.phi(this.lb + (((double) i) * incrementSize)) / this.maxValue);
        }
    }

    public int getRandomeColor() {
        Random r = new Random();
        int i1 = r.nextInt(9 - 0) + 0;
        return Color.parseColor(mWaveColor[i1]);
    }
}
