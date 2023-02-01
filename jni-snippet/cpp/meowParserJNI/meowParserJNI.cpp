#include "com_sdefaa_jni_MeowParser.h"
#include "meowParser.h"
#include <iostream>

void throwException(JNIEnv *env, const char *message)
{
    jclass jclazz_NullPointerException = env->FindClass("com/sdefaa/jni/ParserException");
    env->ThrowNew(jclazz_NullPointerException, message);
}

/*
 * Class:     com_sdefaa_jni_MeowParser
 * Method:    init
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_sdefaa_jni_MeowParser_init(JNIEnv *env, jclass clazz)
{
    init();
}

/*
 * Class:     com_sdefaa_jni_MeowParser
 * Method:    parser
 * Signature: (Lcom/sdefaa/jni/ParserContent;)Lcom/sdefaa/jni/ParserResult;
 */
JNIEXPORT jobject JNICALL Java_com_sdefaa_jni_MeowParser_parse(JNIEnv *env, jclass clazz, jobject parserContent)
{
    if (parserContent == NULL)
    {
        throwException(env, "Arguments#ParserContent is null");
        return NULL;
    }
    jclass jclazz_ParserConent = env->GetObjectClass(parserContent);
    jfieldID jfId_content = env->GetFieldID(jclazz_ParserConent, "content", "Ljava/lang/String;");
    jstring jstring_content = (jstring)env->GetObjectField(parserContent, jfId_content);
    if (jstring_content == NULL)
    {
        throwException(env, "ParserContent#content is null");
        return NULL;
    }
    const char *contentPtr = env->GetStringUTFChars(jstring_content, JNI_FALSE);
    char *meaning = parse(contentPtr);
    jclass jclazz_ParserResult = env->FindClass("com/sdefaa/jni/ParserResult");
    jmethodID jmId_constrctor = env->GetMethodID(jclazz_ParserResult, "<init>", "()V");
    jobject jobject_ParserResult = env->NewObject(jclazz_ParserResult, jmId_constrctor);
    jfieldID jfId_meaning = env->GetFieldID(jclazz_ParserResult, "meaning", "Ljava/lang/String;");
    jstring jstring_meaning = env->NewStringUTF(meaning);
    env->SetObjectField(jobject_ParserResult, jfId_meaning, jstring_meaning);
    return jobject_ParserResult;
}

/*
 * Class:     com_sdefaa_jni_MeowParser
 * Method:    destroy
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_sdefaa_jni_MeowParser_destroy(JNIEnv *env, jclass clazz)
{
    destroy();
}