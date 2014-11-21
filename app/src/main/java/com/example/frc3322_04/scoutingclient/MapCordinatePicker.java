package com.example.frc3322_04.scoutingclient;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MapCordinatePicker extends FormWidget {
    ImageView map;
    double x;
    double y;
    MapCordinatePicker(Context context) {
        super(context);
        x = y = -1.0;
        this.setBackgroundColor(Color.BLUE);
        this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        map = new ImageView(context);
        map.setOnTouchListener(new OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                x = event.getX() / view.getWidth();
                y = event.getY() / view.getHeight();
                Log.i("AOUT", "Coordinates: " + String.valueOf(x) + ", " + String.valueOf(y));
                return true;
            }
        });
        //map.setOnTouchListener(new View.OnTouchListener());
        map.setImageResource(R.drawable.map);
        map.setScaleType(ImageView.ScaleType.FIT_XY);
        this.addView(map);
    }
    @Override
    public Tuple<Double,Double> getValue() {
        return new Tuple<Double, Double>(x,y);
    }
}