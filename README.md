AndroidMP3Recorder
==================

为Android提供MP3录音功能

实现思路讲解：[Android MP3录音实现](http://www.cnblogs.com/ct2011/p/4080193.html) 

# 1. 目录简介

- library： MP3录音实现源码库  
- sample ： 使用范例   

# 2. 使用方法
## 集成到项目中

```Groovy
dependencies {
    compile 'com.czt.mp3recorder:library:1.0@aar'
}
```

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
# 3. 关于音量部分的解释
音量的计算，来自于 [三星开发者文档-Displaying Sound Volume in Real-Time While Recording](http://developer.samsung.com/technical-doc/view.do?v=T000000086)    
里面对于音量的最大值设置为了4000，而我实际测验中发现大部分声音不超过2000，所以就在代码中暂时设置为2000。  
这方面没有找到相关资料，如果有人知道理论值之类的，请联系我(chentong.think@gmail.com) 完善此库，谢谢。

# 4. 关于so库的声明
so库本身没有任何限制，但受限于Android NDK的支持 
- arm armv7 支持Android 1.5 (API Level 3)及以上版本
- x86支持Android 2.3 (API Level 9)及以上版本

# 5. 常见问题声明
本库提供了arm armv7 支持， 即会有armeabi和armeabi-v7a两个文件夹。  
如果您当前使用的其他so文件只有两个文件夹中的一种，建议您进行拷贝，保证两个文件夹内都有so文件，避免引发`java.lang.UnsatisfiedLinkError`错误。 

以app使用了百度地图sdk为例：   
一般会引发`java.lang.UnsatisfiedLinkError: Couldn't load BaiduMapSDK_v3_2_0_15 from loader`错误。  
解决办法： 将armeabi文件夹下的 .so文件，一并copy至armeabi-v7a，使两个文件夹下都有相应文件。

# 6. License

    Copyright 2014 GavinCT

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
