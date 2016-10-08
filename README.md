AndroidMP3Recorder[RETIRED]
==================
# 停止维护声明
*因个人精力原因，无法顾及到本库的维护和更新。*    
*需要的同学请自行fork进行改进，谢谢   ==> 实现思路讲解：[Android MP3录音实现](http://www.cnblogs.com/ct2011/p/4080193.html)*   

---

~~为Android提供MP3录音功能~~

~~实现思路讲解：[Android MP3录音实现](http://www.cnblogs.com/ct2011/p/4080193.html)~~

# ~~1. 目录简介~~

- ~~library： MP3录音实现源码库~~
- ~~sample ： 使用范例~~ 

# ~~2. 使用方法~~
## ~~集成到项目中~~

```Groovy
dependencies {
    compile 'com.czt.mp3recorder:library:1.0.4'
}
```

## ~~录音功能实现~~
1. ~~创建MP3Recorder， 传入录音文件的File对象。~~

	```java
	MP3Recorder mRecorder = new MP3Recorder(new File(Environment.getExternalStorageDirectory(),"test.mp3"));
	```
2. ~~开始录音： 调用MP3Recorder对象的start()方法。~~

	```java
	mRecorder.start();
	```
3. ~~停止录音： 调用MP3Recorder对象的stop()方法。~~

	```java
	mRecorder.stop();
	```

~~代码示例见：AndroidMP3RecorderSample~~
# ~~3. 关于音量部分的解释~~
~~音量的计算，来自于 [三星开发者文档-Displaying Sound Volume in Real-Time While Recording](http://developer.samsung.com/technical-doc/view.do?v=T000000086)~~    
~~里面对于音量的最大值设置为了4000，而我实际测验中发现大部分声音不超过2000，所以就在代码中暂时设置为2000。~~  
~~这方面没有找到相关资料，如果有人知道理论值之类的，请联系我(chentong.think@gmail.com) 完善此库，谢谢。~~

# ~~4. 关于so库的声明~~
~~so库本身没有任何限制，但受限于Android NDK的支持~~ 
- ~~arm armv7 支持Android 1.5 (API Level 3)及以上版本~~
- ~~x86支持Android 2.3 (API Level 9)及以上版本~~

# ~~5. 常见问题声明~~

## ~~使用so中的部分~~

~~本库提供了 arm mips x86 等多种so，如果您只需要其中的几种，可以在gradle中添加下面的语法：~~

```groovy
productFlavors {
  arm {
    ndk {
      abiFilters "armeabi-v7a", "armeabi"
    }
  }
  x86 {
    ndk {
      abiFilter "x86"
    }
  }
}
```

~~以上会在arm中接入armv7 arm包，最新的64位v8不会放入。 同时没有提供mips的flavor，也保证了没有mips的so。~~ ~~但最新的1.5.0插件不支持这种写法，且新版的ndk还处于试验阶段，所以一般使用了上述写法会报错，报错中给出了提示，即在gradle.properties中添加~~

```
android.useDeprecatedNdk=true
```

~~即可正常使用~~

## ~~遇到了 java.lang.UnsatisfiedLinkError错误~~

~~这种情况一般是so不全导致的。~~

~~以app使用了百度地图sdk为例：~~   

~~假如百度地图只提供了arm 的so ， 您使用了本库后会有arm armv7 armv8等多种库，这样打包后会产生armeabi、armeabi-v7a、armeabi-v8a等多个文件夹，但百度地图在armv7 v8下并没有so，这样就会引发`java.lang.UnsatisfiedLinkError: Couldn't load BaiduMapSDK_v3_2_0_15 from loader`错误。~~  

~~解决办法有两种：~~

- ~~联系其他库的提供者补全~~
- ~~如果不行的话，可以利用上面提到的abiFilters来过滤掉本库的so，这样只提供arm一般是可以兼容的。~~


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
