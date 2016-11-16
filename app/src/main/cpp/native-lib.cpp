#include <jni.h>
#include <string>

extern "C"
jstring
Java_team11_healthcombinder_Timeline_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
