package com.romanpulov.library.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.xml.transform.Source;

/**
 * Created on 05.10.2015.
 */

public class BarChart extends View {

    private static int AXES_PADDING = 30;

    private Paint mAxesPaint;
    private Paint mAxesTextPaint;

    private List<Series> mSeriesList = new ArrayList<>();

    public Series getSeries(int location) {
        return mSeriesList.get(location);
    }

    public Series addSeries() {
        Series newSeries = new Series();
        mSeriesList.add(newSeries);
        return newSeries;
    }

    public static class ChartValueBounds {
        public Double getMinX() {
            return minX;
        }

        public Double getMinY() {
            return minY;
        }

        public Double getMaxX() {
            return maxX;
        }

        public Double getMaxY() {
            return maxY;
        }

        private Double minX;
        private Double minY;
        private Double maxX;
        private Double maxY;

        public ChartValueBounds (Double minX, Double minY, Double maxX, Double maxY) {
            this.minX = minX;
            this.minY = minY;
            this.maxX = maxX;
            this.maxY = maxY;
        }

        public void setBounds (Double minX, Double minY, Double maxX, Double maxY) {
            this.minX = minX;
            this.minY = minY;
            this.maxX = maxX;
            this.maxY = maxY;
        }

        public Double[] getBounds() {
            return new Double[] {minX, minY, maxX, maxY};
        }
    }

    public static class Series {
        private List<ChartValue> mData = new ArrayList<>();

        private ChartValueBounds mValueBounds;

        public void updateValueBounds() {
            Double minX = Double.MAX_VALUE;
            Double minY = Double.MAX_VALUE;
            Double maxX = Double.MIN_VALUE;
            Double maxY = Double.MIN_VALUE;
            for (ChartValue v : mData) {
                if (minX > v.x)
                    minX = v.x;
                if (minY > v.y)
                    minY = v.y;
                if (maxX < v.x)
                    maxX = v.x;
                if (maxY < v.y)
                    maxY = v.y;
            }
            if (null == mValueBounds) {
                mValueBounds = new ChartValueBounds(minX, minY, maxX, maxY);
            } else
                mValueBounds.setBounds(minX, minY, maxX, maxY);
        }

        public ChartValueBounds getValueBounds() {
            return mValueBounds;
        }

        public boolean add(ChartValue value) {
            return mData.add(value);
        }

        public ChartValue get(int location) {
            return mData.get(location);
        }

        public List<ChartValue> getList() {
            return mData;
        }

        public boolean addXY(Double x, String xLabel, Double y) {
            return add(new ChartValue(x, xLabel, y));
        }

    }

    public static class ChartValue implements Comparable {
        public Double x;
        public String xLabel;
        public Double y;

        @Override
        public int compareTo(Object another) {
            if (another instanceof ChartValue)
                return x.compareTo(((ChartValue) another).x);
            else
                return 0;
        }

        public ChartValue(Double x, String xLabel, Double y) {
            this.x = x;
            this.xLabel = xLabel;
            this.y = y;
        }
    }

    public void sortSeries(int seriesIndex) {
        Collections.sort(mSeriesList.get(seriesIndex).getList());
    }

    public static class ChartLayout {

    }

    public BarChart(Context context) {
        super(context, null);
    }

    public BarChart(Context context, AttributeSet attrs) {
        super(context, attrs);

        mAxesPaint = new Paint();
        mAxesPaint.setStyle(Paint.Style.STROKE);
        mAxesTextPaint = new Paint();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final int width = getWidth();
        final int height = getHeight();

        Rect axesTextBounds = new Rect();
        mAxesPaint.getTextBounds("00000", 0, 4, axesTextBounds);

        int offsetLeft = axesTextBounds.width();
        int offsetTop = axesTextBounds.height() / 2;
        int offsetBottom = 2 * axesTextBounds.height();
        int offsetRight  = axesTextBounds.height() / 2;

        Rect chartBounds = new Rect(offsetLeft, offsetTop, width - offsetRight, height - offsetBottom);
        canvas.drawRect(chartBounds, mAxesPaint);

        canvas.drawText(toString(), 0, 0, mAxesTextPaint);
    }
}
