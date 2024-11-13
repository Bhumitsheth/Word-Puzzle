package com.wordpuzzle.app.android.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.provider.MediaStore
import android.util.TypedValue
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class ViewUtil{

    companion object {
        fun drawableToBitmap(drawable: Drawable): Bitmap? {
            var bitmap: Bitmap? = null
            if (drawable is BitmapDrawable) {
                val bitmapDrawable: BitmapDrawable = drawable as BitmapDrawable
                if (bitmapDrawable.getBitmap() != null) {
                    return bitmapDrawable.getBitmap()
                }
            }
            bitmap = if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
                Bitmap.createBitmap(
                    1,
                    1,
                    Bitmap.Config.ARGB_8888
                ) // Single color bitmap will be created of 1x1 pixel
            } else {
                Bitmap.createBitmap(
                    drawable.intrinsicWidth,
                    drawable.intrinsicHeight,
                    Bitmap.Config.ARGB_8888
                )
            }
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            return bitmap
        }

        fun rotateImage(source: Bitmap?, angle: Float): Bitmap? {
            if(source != null){
                val matrix = Matrix()
                matrix.postRotate(angle)
                return Bitmap.createBitmap(
                    source, 0, 0, source.width, source.height,
                    matrix, true
                )
            }
            else{
                return null
            }

        }

        fun setTransparentStatusBarWithAdjustResizeAndFitSystemToWindow(view: View?){
            view?.let{ view-> ViewCompat.setOnApplyWindowInsetsListener(view) { view, insets ->
                ViewCompat.onApplyWindowInsets(
                    view,
                    insets.replaceSystemWindowInsets(
                        insets.systemWindowInsetLeft, 0,
                        insets.systemWindowInsetRight, insets.systemWindowInsetBottom
                    )
                )
            }
            }
        }

        fun setFullScreen(activity: Activity?, isFullScreen: Boolean){
            if(isFullScreen){
                activity?.window?.setFlags(
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                )
            }
            else{
                activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            }
        }

         fun hideStatusBar(activity: Activity?) {
            activity?.getWindow()?.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            val windowInsetsController = WindowCompat.getInsetsController(activity!!.window, activity!!.window.decorView)
            windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
        }

        fun dpToPx(dp: Float, context: Context?): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context?.resources?.displayMetrics
            ).toInt()
        }
        fun setSystemBarTheme(pActivity: Activity, pIsDark: Boolean) {
            // Fetch the current flags.
            val lFlags = pActivity.window.decorView.systemUiVisibility
            // Update the SystemUiVisibility dependening on whether we want a Light or Dark theme.
            pActivity.window.decorView.systemUiVisibility = if (pIsDark) lFlags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv() else lFlags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        fun imageViewAnimatedChange(c: Context, v: ImageView, new_image: Drawable?) {
            val anim_out = AnimationUtils.loadAnimation(c, android.R.anim.fade_out)
            val anim_in = AnimationUtils.loadAnimation(c, android.R.anim.fade_in)
            anim_out.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationRepeat(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    v.setImageDrawable(new_image)
                    anim_in.setAnimationListener(object : Animation.AnimationListener {
                        override fun onAnimationStart(animation: Animation) {}
                        override fun onAnimationRepeat(animation: Animation) {}
                        override fun onAnimationEnd(animation: Animation) {}
                    })
                    v.startAnimation(anim_in)
                }
            })
            v.startAnimation(anim_out)
        }

        fun fetchGalleryImages(context: Activity?): ArrayList<String> {

            val galleryImageUrls: ArrayList<String> = ArrayList()

            context?.let{
                val columns = arrayOf(MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID)//get all columns of type images
                val orderBy = MediaStore.Images.Media.DATE_TAKEN//order data by date

                val imagecursor = context?.managedQuery(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    columns,
                    null,
                    null,
                    orderBy + " DESC"
                )//get all data in Cursor by sorting in DESC order

                for (i in 0 until imagecursor!!.count) {
                    imagecursor.moveToPosition(i)
                    val dataColumnIndex =
                        imagecursor.getColumnIndex(MediaStore.Images.Media.DATA)//get column index
                    galleryImageUrls.add(imagecursor.getString(dataColumnIndex))//get Image from column index

                }
            }


            return galleryImageUrls
        }

        fun setColorIconMenuItem(color: Int, menuItem: MenuItem?, context: Context) {
            val drawable = menuItem?.icon
            if (drawable != null) {
                drawable.mutate()
                drawable.setColorFilter(
                    ContextCompat.getColor(context, color),
                    PorterDuff.Mode.SRC_ATOP
                )
            }
        }

        fun  getCroppedBitmap(bitmap: Bitmap) : Bitmap {
            val output = Bitmap.createBitmap(
                bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888
            );
            val canvas =  Canvas(output);

            val paint = Paint();
            val  rect = Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            canvas.drawCircle(
                bitmap.getWidth() / 2F,
                bitmap.getHeight() / 2F,
                bitmap.getWidth() / 2F,
                paint
            );
            paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);
            return output;
        }

        /*fun vibrate(context: Context?, time: Long) {
            try {
                if (Build.VERSION.SDK_INT >= 26) {
                    (context?.getSystemService(VIBRATOR_SERVICE) as Vibrator).vibrate(VibrationEffect.createOneShot(time, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    (context?.getSystemService(VIBRATOR_SERVICE) as Vibrator).vibrate(time)
                }
            } catch (e: Exception) {

            }
        }*/

    }

}
