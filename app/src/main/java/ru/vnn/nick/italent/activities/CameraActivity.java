package ru.vnn.nick.italent.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import ru.vnn.nick.italent.PhotoPreviewActivity;
import ru.vnn.nick.italent.PhotoPreviewActivity_;
import ru.vnn.nick.italent.R;
import ru.vnn.nick.italent.listeners.NextRevisionListener;

@SuppressWarnings("deprecation")
@TargetApi(26)
//@EActivity(R.layout.activity_camera)
public class CameraActivity extends AppCompatActivity implements
        SurfaceHolder.Callback,
        Camera.PreviewCallback,
        View.OnClickListener,
        Camera.AutoFocusCallback,
        Camera.PictureCallback,
        SensorEventListener {
    private static final int REQUEST_PERMISSION = 200;
    private static final int PERMISSIOS_WRITE = 444;
    private static final int PICK_IMAGE = 333;
    private static final int PHOTO_PREVIEW = 335;
    private static final String TAG = "CameraActivity";

    private ImageButton btnBack;
    private ImageButton btnCapture;
    private ImageButton btnStorage;
    private ImageButton btnVideo;
    private ImageButton btnSwitchCamera;
    private SurfaceView preview;
    private SurfaceHolder surfaceHolder;

    private int frontCameraId = 702; // Init value
    private int backCameraId = 701; // Init value
    private int activeCameraId = 700;

    private Camera camera;

    float photoAzimut;
    float photoPitch;
    float photoRoll;
    float rad2degree = 57.2958f;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        getWindow().setStatusBarColor(getColor(R.color.semioverlay));
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        Log.d(TAG, "DISPLAY SIZE: " +
                getWindow().getWindowManager().getDefaultDisplay().getWidth() + " " + getWindowManager().getDefaultDisplay().getHeight());
        setContentView(R.layout.activity_camera);

        if (!(ActivityCompat.checkSelfPermission(
                CameraActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        )) {
            ActivityCompat.requestPermissions(
                    CameraActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSIOS_WRITE);
        }

        init();

//        openCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (camera == null) {
            reopenCamera();
        }

        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();

        closeCamera();

        mSensorManager.unregisterListener(this);
    }

    private void openCamera() {
        if (activeCameraId != 700)
            camera = Camera.open(activeCameraId);
    }

    private void closeCamera() {

        if (camera != null) {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;

            activeCameraId = 700;
        }
    }

    private void init() {
        Log.d(TAG, "Init() ");

        btnBack = findViewById(R.id.bt_camera_back);
        btnCapture = findViewById(R.id.bt_camera_click);
        btnStorage = findViewById(R.id.bt_camera_gallery);
        btnSwitchCamera = findViewById(R.id.bt_camera_switch);
        btnVideo = findViewById(R.id.bt_camera_video);

        preview = findViewById(R.id.camera_preview);

        setListeners();

        if (!checkPermission()) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_PERMISSION);

        } else {
            try {
                findCameras();

                surfaceHolder = preview.getHolder();
                surfaceHolder.addCallback(this);
                surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

                openCamera();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                finish();
            }
        }
    }

    private void setListeners() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnSwitchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (camera != null) {
                    if (activeCameraId == backCameraId) {
                        closeCamera();
                        activeCameraId = frontCameraId;
                    } else if (activeCameraId == frontCameraId) {
                        closeCamera();
                        activeCameraId = backCameraId;
                    } else {
                        closeCamera(); // set to default value
                    }
                }

                if (camera == null) {
                    openCamera();
                    surfaceCreated(surfaceHolder);
                }
            }
        });

        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoAzimut = azimut * rad2degree;
                photoPitch = pitch * rad2degree;
                photoRoll = roll * rad2degree;
//                captureStillPicture();
                Log.d(TAG, "takePicture(). Camera is null: " + (camera == null));
                if (camera != null) {
                    camera.takePicture(null, null, null, CameraActivity.this);
                }

            }
        });

        btnStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(ActivityCompat.checkSelfPermission(
                        CameraActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
                )) {
                    ActivityCompat.requestPermissions(
                            CameraActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            PICK_IMAGE);
                } else {
                    getPhoto();
                }
            }
        });

        btnVideo.setOnClickListener(new NextRevisionListener());
    }

    private void getPhoto() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        startActivityForResult(chooserIntent, PICK_IMAGE);
    }

    private void findCameras() {
        int count = Camera.getNumberOfCameras();
        Camera.CameraInfo info = new Camera.CameraInfo();
        for (int cameraId = 0; cameraId < count; cameraId++) {
            Camera.getCameraInfo(cameraId, info);

            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                backCameraId = cameraId;
            } else if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                frontCameraId = cameraId;
            }
        }

        if (backCameraId != 701) {
            activeCameraId = backCameraId;
        } else if (frontCameraId != 702) {
            activeCameraId = frontCameraId;
        }

        if (activeCameraId == 700)
            throw new RuntimeException("Not found back or front cameras.");
    }

    private boolean checkPermission() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        if (camera == null || surfaceHolder == null) {
            return;
        }
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.setPreviewCallback(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Camera.Size previewSize = camera.getParameters().getPreviewSize();
        float aspect = (float) previewSize.width / previewSize.height;

        int previewSurfaceWidth = preview.getWidth();
        int previewSurfaceHeight = preview.getHeight();

        ViewGroup.LayoutParams lp = preview.getLayoutParams();

        camera.setDisplayOrientation(90);
        lp.height = previewSurfaceHeight;
        lp.width = (int) (previewSurfaceHeight / aspect);

        preview.setLayoutParams(lp);
        camera.startPreview();
        Log.d(TAG, "Preview started.");
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void onClick(View v) {
        if (v == preview) {
            camera.autoFocus(this);
        }
    }

    @Override
    public void onAutoFocus(boolean paramBoolean, Camera paramCamera) {
        if (paramBoolean) {
            // если удалось сфокусироваться, делаем снимок
//            paramCamera.takePicture(null, null, null, this);

        }
    }

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {

    }

    @Override
    public void onPictureTaken(byte[] bytes, Camera camera) {
        if (!(ActivityCompat.checkSelfPermission(
                CameraActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        )) {
            ActivityCompat.requestPermissions(
                    CameraActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSIOS_WRITE);
        }

        Bitmap photo = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager != null) {
            for (Sensor sensor : sensorManager.getSensorList(Sensor.TYPE_ROTATION_VECTOR)) {
                if (sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
                    Log.e(TAG, "ROTATION: " + photoAzimut + " " + photoPitch + " " + photoRoll);
                }
            }
        }
        if (((int) photoPitch) == 0 && ((int) photoRoll) != 0) {
//            photo = rotate(photo, (int) photoRoll);
        } else {
            photo = rotate(photo, 90);
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        photo.recycle();

        File saveDir = new File(Environment.getExternalStorageDirectory().getPath() + "/iTalent/");
        Log.d(TAG, "Path: " + saveDir);
        if (!saveDir.exists()) {
            Log.d(TAG, "saved: " + saveDir.mkdirs());
        }

        String filename = String.format(Environment.getExternalStorageDirectory().getPath() + "/iTalent/%d.jpg", System.currentTimeMillis());
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(filename);
            os.write(byteArray);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent photoPreview = new Intent(this, PhotoPreviewActivity_.class);
        photoPreview.putExtra("photo", Uri.parse(filename));
        closeCamera();

        startActivityForResult(photoPreview, 200);
    }

    @Override
    public synchronized void onRequestPermissionsResult(int requestCode,
                                                        @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Permissions granted.");
                    findCameras();

                    surfaceHolder = preview.getHolder();
                    surfaceHolder.addCallback(this);
                    surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

                    openCamera();
                }
                break;
            case PICK_IMAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Permissions granted.");
                    getPhoto();
                }
                break;
            case PERMISSIOS_WRITE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Permissions granted.");
                }

        }
    }

    private void reopenCamera() {
        if (camera != null) {
            closeCamera();
        }

        if (camera == null) {
            findCameras();
            openCamera();
            surfaceCreated(surfaceHolder);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult() code: " + resultCode + " request code: " + requestCode);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE) {
                Uri photoUri = data.getData();
                Log.d(TAG, data.getDataString());

                if (photoUri != null) {
                    Intent photoPreviewActivity = new Intent(this, PhotoPreviewActivity_.class);

                    photoPreviewActivity.putExtra("photo", photoUri);
                    startActivityForResult(photoPreviewActivity, PHOTO_PREVIEW);
                }
            }
        }
        reopenCamera();
    }


    public static Bitmap rotate(Bitmap bitmap, int degree) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix mtx = new Matrix();
        mtx.setRotate(degree);

        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }

    private SensorManager mSensorManager;
    Sensor accelerometer;
    Sensor magnetometer;
    float azimut;
    float pitch;
    float roll;
    float[] mGravity;
    float[] mGeomagnetic;
    float[] mRotation;
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            mGravity = event.values;
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            mGeomagnetic = event.values;
        if (mGravity != null && mGeomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                azimut = orientation[0]; // orientation contains: azimut, pitch and roll
                pitch = orientation[1];
                roll = orientation[2];
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
