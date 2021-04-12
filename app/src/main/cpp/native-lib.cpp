#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_in_iot_lab_dmscanner_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++ Rohan!!";
    return env->NewStringUTF(hello.c_str());
}
