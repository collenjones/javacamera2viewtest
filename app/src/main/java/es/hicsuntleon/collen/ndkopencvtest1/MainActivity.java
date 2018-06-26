package es.hicsuntleon.collen.ndkopencvtest1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
  private static final String TAG = "MainActivity";
  CameraBridgeViewBase javaCameraView;
  Mat mRgba, mGray;

  BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
    @Override
    public void onManagerConnected(int status) {
      Log.i(TAG, "Called onManagerConnected");
      switch (status) {
        case LoaderCallbackInterface.SUCCESS:
          // This must be called after BaseLoaderCallback SUCCESS or linker error will occur
          System.loadLibrary("native-lib");
          javaCameraView.enableView();
          break;
        default:
          super.onManagerConnected(status);
          break;
      }
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    Log.i(TAG, "Called onCreate");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    javaCameraView = findViewById(R.id.java_camera_view);
    javaCameraView.setVisibility(View.VISIBLE);
    javaCameraView.setCvCameraViewListener(this);
    javaCameraView.enableFpsMeter();
  }

  @Override
  protected void onPause() {
    Log.i(TAG, "Called onPause");
    super.onPause();
    if (javaCameraView != null) {
      javaCameraView.disableView();
    }
  }

  @Override
  protected void onDestroy() {
    Log.i(TAG, "Called onDestroy");
    super.onDestroy();
    if (javaCameraView != null) {
      javaCameraView.disableView();
    }
  }

  @Override
  protected void onResume() {
    Log.i(TAG, "Called onResume");
    super.onResume();
    if (OpenCVLoader.initDebug()) {
      Log.d(TAG, "OpenCV library found inside package. Using it!");
      mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
    } else {
      Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization.");
      boolean success = OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoaderCallback);
      if (success) {
        Log.d(TAG, "Asynchronous initialization succeeded!");
      } else {
        Log.d(TAG, "Asynchronous initialization failed...");
      }
    }
  }

  @Override
  public void onCameraViewStarted(int width, int height) {
    Log.i(TAG, "Called onCameraViewStarted");
    mRgba = new Mat(height, width, CvType.CV_8UC4);
    mGray = new Mat(height, width, CvType.CV_8UC1);
  }

  @Override
  public void onCameraViewStopped() {
    Log.i(TAG, "Called onCameraViewStopped");
    mRgba.release();
    mGray.release();
  }

  @Override
  public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
    Log.i(TAG, "Called onCameraFrame");
    mRgba = inputFrame.rgba();
    OpenCVNativeClass.convertGray(mRgba.getNativeObjAddr(), mGray.getNativeObjAddr());
    return mGray;
  }

}
