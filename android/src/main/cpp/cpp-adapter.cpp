#include <jni.h>
#include "TelephotoOnLoad.hpp"

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void*) {
  return margelo::nitro::telephoto::initialize(vm);
}
