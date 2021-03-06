package com.example.frc3322_04.scoutingclient;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class MapCordinatePicker extends FormWidget {
    //TODO sometimes part of hte image gets cut off
    ImageView map;
    double x;
    double y;
    MapCordinatePicker(Context context,String keyValue) {
        super(context,"Tap to specify the location of the robot",keyValue);
        x = y = -1.0;
        this.setOrientation(VERTICAL);
        map = new ImageView(context) {
            @Override
            protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);
                Paint paint = new Paint();
                paint.setColor(Color.RED);
                paint.setStrokeWidth(2);
                if(x >= 0 && x < 1 && y >=0 && y < 1) {
                    canvas.drawCircle((float) x * this.getWidth(), (float) y * this.getHeight(),10, paint);
                }
            }
        };
        map.setScaleType(ImageView.ScaleType.FIT_CENTER);
        map.setAdjustViewBounds(true);
        map.setOnTouchListener(new OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                x = event.getX() / view.getWidth();
                y = event.getY() / view.getHeight();
                map.invalidate();
                Log.i("AOUT", "Coordinates: " + String.valueOf(x) + ", " + String.valueOf(y));
                return true;
            }
        });
        map.setImageResource(R.drawable.map);
        map.setScaleType(ImageView.ScaleType.FIT_XY);
        this.addView(map);
    }
    @Override
    public Tuple<Double,Double> getValue() {
        return new Tuple<Double, Double>(x,y);
    }
    @Override
    public boolean isFilled() {
        return x >=0.0 && y >= 0.0;
    }
}