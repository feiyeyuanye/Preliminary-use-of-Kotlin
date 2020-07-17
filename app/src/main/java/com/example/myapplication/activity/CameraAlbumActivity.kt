package com.example.myapplication.activity

import android.app.Activity
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.example.myapplication.R
import com.example.myapplication.base.BaseActivity
import kotlinx.android.synthetic.main.activity_cemara.*
import java.io.File

class CameraAlbumActivity : BaseActivity() {

    /**
     * 调用摄像头拍照
     */
    val takePhoto = 1
    lateinit var imageUri: Uri
    lateinit var outputImage: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cemara)

        // 在 NotificationActivity 测试通知的点击事件，会跳转到此页面，以下代码是当点击事件完成后取消通知的一种方式。
        // 显示地调用 NotificationManager 的 cancel() 取消通知。
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // 点击这个通知时，通知会自动取消。否则通知消息会一直留存在系统状态栏上。
        // 这个 1 是指在创建通知时给每条通知指定的 id，想取消哪条通知，就传入该通知的 id 即可。
        manager.cancel(1)

        initData()
    }

    private fun initData() {
        takePhoto()
    }

    /**
     * 调用摄像头拍照
     */
    private fun takePhoto() {
        btnTakePhoto.setOnClickListener{
            // 创建 File 对象，用于存储拍照后的照片，存放在手机 SD 卡的应用关联缓存目录下。
            // 应用关联目录：指 SD 卡中专门用于存放当前应用缓存数据的位置，
            // 调用 getExternalCacheDir() 可以得到这个目录，具体路径 /sdcard/Android/data/<package name>/cache。
            // 从 Android 6.0 开始，读写 SD 卡被列为危险权限，如果将图片存放在 SD 卡的任何其它目录，都要运行时权限处理，而使用应用关联目录则可以跳过这一步。
            // 另外，从 Android 10.0 开始，公有的 SD 卡目录已经不再允许被应用程序直接访问了，而是要使用作用域存储才行。
            outputImage = File(externalCacheDir,"output_image.jpg")
            if (outputImage.exists()){
                outputImage.delete()
            }
            outputImage.createNewFile()
            // 从 Android 7.0 系统开始，直接使用本地真实路径的 Uri 被认为是不安全的，会抛出一个 FileUriExposedException 异常。
            // 而 FileProvider 则是一种特殊的 ContentProvider，它使用了和 ContentProvider 类似的机制来对数据进行保护，
            // 可以选择性地将封装过的 Uri 共享给外部，从而提高了应用的安全性。
            imageUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                // 调用 FileProvider.getUriForFile() 将 File 对象转换成一个封装过的 Uri 对象。
                // 第二个参数可以是任意唯一的字符串，第三个参数是刚刚创建的 File 对象。
                // FileProvider 要记得对它在 AndroidManifest.xml 文件内进行注册才行。
                FileProvider.getUriForFile(this,"com.example.myapplication.fileprovider",outputImage)
            }else {
                // 如果运行设备系统版本低于 Android 7.0，就调用 Uri 的 fromFile() 将 File 对象转换成 Uri 对象，
                // 这个 Uri 对象标识着 output_image.jpg 的本地真实路径。
                Uri.fromFile(outputImage)
            }
            // 启动相机程序
            val intent = Intent("android.media.action.IMAGE_CAPTURE")
            intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri)  // 指定图片的输出地址，填入刚得到的 Uri 对象。
            startActivityForResult(intent,takePhoto) // 一个隐式启动，照相机程序会被匹配打开，拍下的照片将会输出到 output_image.jpg 中。
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            takePhoto -> {
                if (resultCode == Activity.RESULT_OK){
                    // 调用 BitmapFactory 的 decodeStream() 将 output_image.jpg 这张照片解析成 Bitmap 对象
                    val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri))
                    // 将拍摄的照片显示出来
                    ivPhoto.setImageBitmap(rotateIfRequired(bitmap))
                }
            }
        }
    }

    /**
     * 照片旋转
     * 因为一些手机认为打开摄像头进行拍摄时手机就应该是横屏的，因此回到竖屏的情况下就会发生 90 度的旋转。
     */
    private fun rotateIfRequired(bitmap: Bitmap): Bitmap {
        val exif = ExifInterface(outputImage.path)
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL)
        return when(orientation){
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap,90)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap,180)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap,270)
            else -> bitmap
        }
    }

    private fun rotateBitmap(bitmap: Bitmap, degree: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        val rotatedBitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.width,bitmap.height,matrix,true)
        bitmap.recycle()  // 将不再需要的 Bitmap 对象回收
        return rotatedBitmap
    }

    companion object{
        fun actionStart(context: Context){
            val intent = Intent(context, CameraAlbumActivity::class.java)
            context.startActivity(intent)
        }
    }
}