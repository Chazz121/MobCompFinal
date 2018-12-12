package com.example.charles.mobilecomputingfinal;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;


public class FaceDetectActivity extends AppCompatActivity {
    Bitmap mBitmap;
    ImageView imageview;
    Button btn;
    Canvas tempCanvas;
    Paint myRectPaint;
    Bitmap tempBitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_detect);
        imageview = (ImageView)findViewById(R.id.imageView);
        String fileLocation = getIntent().getExtras().getString("imageLocation");
        mBitmap = BitmapFactory.decodeFile(fileLocation);
        btn = (Button) findViewById(R.id.button);
        if(mBitmap != null && imageview != null){
            imageview.setImageBitmap(mBitmap);
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable=true;

        myRectPaint = new Paint();
        myRectPaint.setStrokeWidth(5);
        myRectPaint.setColor(Color.RED);
        myRectPaint.setStyle(Paint.Style.STROKE);

        tempBitmap = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), Bitmap.Config.RGB_565);
        tempCanvas = new Canvas(tempBitmap);
        tempCanvas.drawBitmap(tempBitmap, 0, 0, null);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                FaceDetector faceDetector = new
                        FaceDetector.Builder(getApplicationContext())
                        .setTrackingEnabled(false)
                        .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                        .setMode(FaceDetector.FAST_MODE)
                        .build();
                if(!faceDetector.isOperational()){
                    new AlertDialog.Builder(imageview.getContext()).setMessage("Could not set up the face detector!").show();
                    return;
                }
                Frame frame = new Frame.Builder().setBitmap(mBitmap).build();
                SparseArray<Face> faces = faceDetector.detect(frame);
                //Toast.makeText(FaceDetectActivity.this,"there were "+ faces.size()+" detected",Toast.LENGTH_LONG);
                for(int i=0; i<faces.size(); i++) {
                    Face face = faces.valueAt(i);
                    float x1 = face.getPosition().x;
                    float y1 = face.getPosition().y;
                    float x2 = x1 + face.getWidth();
                    float y2 = y1 + face.getHeight();
                    tempCanvas.drawRoundRect(new RectF(x1, y1, x2, y2), 2, 2, myRectPaint);
                }
                imageview.setImageDrawable(new BitmapDrawable(getResources(),tempBitmap));
            }
        });





    }


}
