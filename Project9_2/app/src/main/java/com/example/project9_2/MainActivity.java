package com.example.project9_2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity2";

    private ImageButton ibZoomIn, ibZoomOut, ibRotate, ibBright, ibDark, ibGray;
    private MyGraphicView graphicView;
    private static float scaleX = 1, scaleY = 1;
    private static float angle = 0;
    private static float color = 1;
    private static float saturation = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("미니 포토샵");

        LinearLayout pictureLayout = (LinearLayout) findViewById(R.id.pictureLayout);
        graphicView = (MyGraphicView) new MyGraphicView(this);
        pictureLayout.addView(graphicView);

        init();
        initLr();
    }

    public void init() {
        ibZoomIn = findViewById(R.id.ibZoomIn);
        ibZoomOut = findViewById(R.id.ibZoomOut);
        ibRotate = findViewById(R.id.ibRotate);
        ibBright = findViewById(R.id.ibBright);
        ibDark = findViewById(R.id.ibDark);
        ibGray = findViewById(R.id.ibGray);
    }

    public void initLr() {
        ibZoomIn.setOnClickListener(v -> {
            scaleX = scaleX + 0.2f;
            scaleY = scaleY + 0.2f;
            graphicView.invalidate();
        });
        ibZoomOut.setOnClickListener(v -> {
            scaleX = scaleX - 0.2f;
            scaleY = scaleY - 0.2f;
            graphicView.invalidate();
        });
        ibRotate.setOnClickListener(v -> {
            angle = angle + 20;
            graphicView.invalidate();
        });
        ibBright.setOnClickListener(v -> {
            color = color + 0.2f;
            graphicView.invalidate();
        });
        ibDark.setOnClickListener(v -> {
            color = color - 0.2f;
            graphicView.invalidate();
        });
        ibGray.setOnClickListener(v -> {
            if (saturation == 0) {
                saturation = 1;
            } else {
                saturation = 0;
            }
            graphicView.invalidate();
        });
    }

    private static class MyGraphicView extends View {
        public MyGraphicView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            int cenX = this.getWidth() / 2;
            int cenY = this.getHeight() / 2;
            canvas.scale(scaleX, scaleY, cenX, cenY);
            canvas.rotate(angle, cenX, cenY);

            Paint paint = new Paint();
            float[] array = {color, 0, 0, 0, 0,
                    0, color, 0, 0, 0,
                    0, 0, color, 0, 0,
                    0, 0, 0, 1, 0,};

            ColorMatrix cm = new ColorMatrix(array);

            if (saturation == 0) {
                cm.setSaturation(saturation);
            }

            paint.setColorFilter(new ColorMatrixColorFilter(cm));

            Bitmap picture = BitmapFactory.decodeResource(getResources(), R.drawable.dog);

            int picX = (this.getWidth() - picture.getWidth()) / 2;
            int picY = (this.getHeight() - picture.getHeight()) / 2;

            canvas.drawBitmap(picture, picX, picY, paint);

            picture.recycle();
        }
    }
}