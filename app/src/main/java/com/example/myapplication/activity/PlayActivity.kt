package com.example.myapplication.activity

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import com.example.myapplication.R
import com.example.myapplication.base.BaseActivity
import kotlinx.android.synthetic.main.activity_play.*

/**
 * 播放音频文件
 * 一般使用 MediaPlayer 类实现，可以用于播放网络、本地以及应用程序安装包中的音频。
 * 工作流程，首先需要创建一个 MediaPlayer 对象，然后调用 setDataSource() 设置音频文件的路径，
 * 再调用 prepare() 使 MediaPlayer 进入准备状态，接下来调用 start() 播放音频，pause() 暂停，reset() 停止播放。
 *
 * 播放视频文件
 * 主要使用 VideoView 类实现，它将视频的显示和控制集于一身。
 * VideoView 不支持直接播放 assets 目录下的视频资源，但像音频、视频之类的资源文件也可以放在 res 目录下的 raw 目录内。
 *
 * VideoView 的背后仍然是使用 MediaPlayer 类对视频文件进行控制的，
 * 并且 VideoView 在视频格式的支持以及播放效率方面都存在着较大的不足，所以更适合用于播放一些游戏的片头动画，或是某个应用的视频宣传。
 */

class PlayActivity : BaseActivity() {

    /**
     * 播放音频(1) or 播放视频(2)
     */
    private var type = 1

    /**
     * 类初始化时，先创建了一个 MediaPlayer 实例。
     */
    private val mediaPlayer = MediaPlayer()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        initView()
        initData()
    }

    private fun initData() {
        // 为 MediaPlayer 对象进行初始化操作
        initMediaPlayer()
        initVideoView()
    }

    private fun initVideoView() {
        // 将 raw 目录下的 mp4 文件解析成了一个 Uri 对象，这里使用的写法是 Android 要求的固定写法。
        val uri = Uri.parse("android.resource://$packageName/${R.raw.video}")
        // 初始化 VideoView 完成
        videoView.setVideoURI(uri)
    }

    /**
     * 这里测试使用应用程序安装包中的音频，借助 AssetManager 类提供的接口对 assets 目录下的文件进行读取。
     */
    private fun initMediaPlayer() {
        // 通过 getAssets() 得到一个 AssetMessager 的实例，AssetMessager 可用于读取 assets 目录下的任何资源。
        val assetMessager = assets
        // 调用 openFd() 将音频文件句柄打开
        val fd = assetMessager.openFd("music.mp3")
        mediaPlayer.setDataSource(fd.fileDescriptor,fd.startOffset,fd.length)
        mediaPlayer.prepare()
    }

    /**
     * MediaPlayer 类中常用的控制方法:
     * mediaPlayer.setDataSource() 设置要播放的音频文件的位置
     * mediaPlayer.prepare() 开始播放前调用，已完成准备工作。
     * mediaPlayer.start() 开始或继续播放音频
     * mediaPlayer.pause() 暂停播放
     * mediaPlayer.reset() 将 MediaPlayer 对象重置到刚刚创建的状态
     * mediaPlayer.seekTo() 从指定的位置开始播放音频
     * mediaPlayer.stop() 停止播放音频。调用后的 MediaPlayer 对象无法再播放音频。
     * mediaPlayer.release() 释放 与 MediaPlayer 对象相关的资源
     * mediaPlayer.isPlaying 判断当前 MediaPlayer 是否正在播放音频
     * mediaPlayer.duration 获取载入的音频文件的时长
     *
     *
     * VideoView 的常用方法:
     * videoView.setVideoPath() 设置要播放的视频文件的位置
     * videoView.start() 开始或继续播放视频
     * videoView.pause() 暂停播放视频
     * videoView.resume() 将视频从头开始播放
     * videoView.seekTo() 从制定的位置开始播放视频
     * videoView.isPlaying 判断当前是否正在播放视频
     * videoView.duration 获取载入的视频文件的时长
     *
     */
    private fun initView() {
        btnType.setOnClickListener{
            if (type == 1){
                type = 2
                btnPlay.setText("视频 Play")
                btnPause.setText("视频 Pause")
                btnStop.setText("视频 Stop")
            }else if (type == 2){
                type = 1
                btnPlay.setText("音频 Play")
                btnPause.setText("音频 Pause")
                btnStop.setText("音频 Stop")
            }else {
                type = -1
                btnPlay.setText("-1")
                btnPause.setText("-1")
                btnStop.setText("-1")
            }
        }

        btnPlay.setOnClickListener{
            if (type == 1){
                if (!mediaPlayer.isPlaying){
                    mediaPlayer.start()  // 开始播放
                }
            }else if (type == 2){
                if(!videoView.isPlaying){
                    videoView.start() // 开始播放
                }
            }else {
                Toast.makeText(this,"error",Toast.LENGTH_SHORT).show()
            }
        }

        btnPause.setOnClickListener{
            if (type == 1){
                if (mediaPlayer.isPlaying){
                    mediaPlayer.pause()  // 暂停播放
                }
            }else if (type == 2){
                if(videoView.isPlaying){
                    videoView.pause() // 暂停播放
                }
            }else {
                Toast.makeText(this,"error",Toast.LENGTH_SHORT).show()
            }
        }

        btnStop.setOnClickListener{
            if (type == 1){
                if (mediaPlayer.isPlaying){
                    mediaPlayer.reset() // 停止播放
                    initMediaPlayer()
                }
            }else if (type == 2){
                if(videoView.isPlaying){
                    videoView.resume() // 重新播放
                }
            }else {
                Toast.makeText(this,"error",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        mediaPlayer.stop()
        mediaPlayer.release()

        videoView.suspend()
    }

    companion object{
        fun actionStart(context: Context){
            val intent = Intent(context, PlayActivity::class.java)
            context.startActivity(intent)
        }
    }
}