#include "es_hicsuntleon_collen_ndkopencvtest1_OpenCVNativeClass.h"

JNIEXPORT jint JNICALL Java_es_hicsuntleon_collen_ndkopencvtest1_OpenCVNativeClass_convertGray
  (JNIEnv *env, jclass obj, jlong addrRgba, jlong addrGray) {
    Mat &mRgb = *(Mat*)addrRgba;
    Mat mGray = *(Mat*)addrGray;
    jint retVal;
    int conv = toGray(mRgb, mGray);
    retVal = (jint)conv;
    return retVal;
}

int toGray(Mat img, Mat &gray) {
    cvtColor(img, gray, CV_RGBA2GRAY);
    if (gray.rows == img.rows && gray.cols == img.cols) {
        return 1;
    }
    return 0;
}


