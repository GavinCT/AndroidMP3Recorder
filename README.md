AndroidMP3Recorder
==================

为Android提供MP3录音功能
# 目录简介
- AndroidMP3RecorderLibrary： MP3录音实现源码库  
- AndroidMP3RecorderSample ： 使用范例  
- libs.zip ： 打包好的libs目录，包括jar包&so库，方便集成到自己项目中。  
# 使用方法
## 集成到项目中
###  使用AndroidMP3RecorderLibrary
直接将Library与自己工程关联即可。
### 使用libs.zip
解压后在libs目录下有：  
androidmp3recorderlibrary.jar 和支持arm、armv7、x86 CPU的so库    
直接拷贝jar包&so库到自己工程的libs文件夹下即可
## 录音功能实现
1. 创建MP3Recorder， 传入录音文件的File对象。 
	```java
	MP3Recorder mRecorder = new MP3Recorder(new File(Environment.getExternalStorageDirectory(),"test.mp3"));
	```
2. 开始录音： 调用MP3Recorder对象的start()方法。
	```java
	mRecorder.start();
	```
3. 停止录音： 调用MP3Recorder对象的stop()方法。
	```java
	mRecorder.stop();
	```

代码示例见：AndroidMP3RecorderSample
