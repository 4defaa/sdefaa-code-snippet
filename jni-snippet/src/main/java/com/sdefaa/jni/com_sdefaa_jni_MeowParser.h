/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_sdefaa_jni_MeowParser */

#ifndef _Included_com_sdefaa_jni_MeowParser
#define _Included_com_sdefaa_jni_MeowParser
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_sdefaa_jni_MeowParser
 * Method:    init
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_sdefaa_jni_MeowParser_init
  (JNIEnv *, jclass);

/*
 * Class:     com_sdefaa_jni_MeowParser
 * Method:    parse
 * Signature: (Lcom/sdefaa/jni/ParserContent;)Lcom/sdefaa/jni/ParserResult;
 */
JNIEXPORT jobject JNICALL Java_com_sdefaa_jni_MeowParser_parse
  (JNIEnv *, jclass, jobject);

/*
 * Class:     com_sdefaa_jni_MeowParser
 * Method:    destroy
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_sdefaa_jni_MeowParser_destroy
  (JNIEnv *, jclass);

#ifdef __cplusplus
}
#endif
#endif
