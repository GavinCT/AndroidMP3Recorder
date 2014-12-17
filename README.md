AndroidMP3Recorder
==================

为Android提供MP3录音功能

实现思路讲解：[Android MP3录音实现](http://www.cnblogs.com/ct2011/p/4080193.html) 
# 1. 目录简介
- AndroidMP3RecorderLibrary： MP3录音实现源码库  
- AndroidMP3RecorderSample ： 使用范例  
- libs.zip ： 打包好的libs目录，包括jar包&so库，方便集成到自己项目中。   

# 2. 使用方法
## 集成到项目中
两种方式：
### 1. 使用libs.zip
**温馨提示：只下载此zip可以使用Chrome插件[GitHub Mate](http://www.v2ex.com/t/93706)**  

解压后在libs目录下有：  
androidmp3recorderlibrary.jar 和支持arm、armv7、x86 CPU的so库    
直接拷贝jar包&so库到自己工程的libs文件夹下即可  
###  2. 使用AndroidMP3RecorderLibrary
直接将Library与自己工程关联即可。
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

# 5. License

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
