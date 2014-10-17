#include "lame_3.99.5_libmp3lame/lame.h"
#include "com_czt_mp3recorder_util_LameUtil.h"
#include <stdio.h>
#include <jni.h>

static lame_global_flags *lame = NULL;

JNIEXPORT void JNICALL Java_com_czt_mp3recorder_util_LameUtil_init(
		JNIEnv *env, jclass cls, jint inSamplerate, jint inChannel, jint outSamplerate, jint outBitrate, jint quality) {
	if (lame != NULL) {
		lame_close(lame);
		lame = NULL;
	}
	lame = lame_init();
	lame_set_in_samplerate(lame, inSamplerate);
	lame_set_num_channels(lame, inChannel);//输入流的声道
	lame_set_out_samplerate(lame, outSamplerate);
	lame_set_brate(lame, outBitrate);
	lame_set_quality(lame, quality);
	lame_init_params(lame);
}

JNIEXPORT jint JNICALL Java_com_czt_mp3recorder_util_LameUtil_encode(
		JNIEnv *env, jclass cls, jshortArray buffer_l, jshortArray buffer_r,
		jint samples, jbyteArray mp3buf) {
	jshort* j_buffer_l = (*env)->GetShortArrayElements(env, buffer_l, NULL);

	jshort* j_buffer_r = (*env)->GetShortArrayElements(env, buffer_r, NULL);

	const jsize mp3buf_size = (*env)->GetArrayLength(env, mp3buf);
	jbyte* j_mp3buf = (*env)->GetByteArrayElements(env, mp3buf, NULL);

	int result = lame_encode_buffer(lame, j_buffer_l, j_buffer_r,
			samples, j_mp3buf, mp3buf_size);

	(*env)->ReleaseShortArrayElements(env, buffer_l, j_buffer_l, 0);
	(*env)->ReleaseShortArrayElements(env, buffer_r, j_buffer_r, 0);
	(*env)->ReleaseByteArrayElements(env, mp3buf, j_mp3buf, 0);

	return result;
}

JNIEXPORT jint JNICALL Java_com_czt_mp3recorder_util_LameUtil_flush(
		JNIEnv *env, jclass cls, jbyteArray mp3buf) {
	const jsize mp3buf_size = (*env)->GetArrayLength(env, mp3buf);
	jbyte* j_mp3buf = (*env)->GetByteArrayElements(env, mp3buf, NULL);

	int result = lame_encode_flush(lame, j_mp3buf, mp3buf_size);

	(*env)->ReleaseByteArrayElements(env, mp3buf, j_mp3buf, 0);

	return result;
}

JNIEXPORT void JNICALL Java_com_czt_mp3recorder_util_LameUtil_close
(JNIEnv *env, jclass cls) {
	lame_close(lame);
	lame = NULL;
}
