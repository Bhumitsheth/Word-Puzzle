package com.wordpuzzle.app.android.utils

import android.Manifest
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import com.wordpuzzle.app.android.R
import com.wordpuzzle.app.android.constants.AppConstants
import com.wordpuzzle.app.android.databinding.ActivityCameraPermissionBinding
import com.wordpuzzle.app.android.di.implementor.DialogButtonClickListener
import com.wordpuzzle.app.android.enums.Image
import com.wordpuzzle.app.android.main.base.BaseMainActivity
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.Objects
@RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
class CameraGalleryActivity : BaseMainActivity<ActivityCameraPermissionBinding>() {
    private val TAG = "CameraGalleryActivity"
    private val CAMERA_REQUEST = 0
    private val ACTION_REQUEST_GALLERY = 1

    //image upload
    private var imagePath = ""
    private var fullGalleryPath: String = ""
    private val compress = false
    private var mImageCaptureUri: Uri? = null
    private val pdfPngType = 0

    companion object {
        var image: Image = Image.ALL
    }

    private val FLAG_IS_SQUARE = false
    private var file: File? = null

    override fun getLayoutId() = R.layout.activity_camera_permission

    override fun create(savedInstanceState: Bundle?) {
        chkPermission()
    }

    override fun getBaseVm() = null

    override fun initDependencies() {

    }

    private var storagePermissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
    )

    private var storagePermissions33 = arrayOf(
        Manifest.permission.READ_MEDIA_IMAGES,
        Manifest.permission.READ_MEDIA_AUDIO,
        Manifest.permission.READ_MEDIA_VIDEO
    )

    /**
     * CHECK PERMISSION FOR READ,WRITE STORAGE AND CAMERA
     */
    private fun chkPermission() {
        if (!FormValidation.checkPermission(this, Manifest.permission.CAMERA)) {
            FormValidation.showPermissionsDialog(
                this,
                arrayOf<String>(Manifest.permission.CAMERA),
                AppConstants.REQUEST_CODE_CAMERA_PERMISSION
            )
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (FormValidation.checkPermission(
                    this,
                    Manifest.permission.READ_MEDIA_IMAGES
                ) && FormValidation.checkPermission(
                    this,
                    Manifest.permission.READ_MEDIA_AUDIO
                ) && FormValidation.checkPermission(this, Manifest.permission.READ_MEDIA_VIDEO)
            ) {
                FormValidation.showPermissionsDialog(
                    this, storagePermissions33, AppConstants.REQUEST_CODE_STORAGE_PERMISSION
                )
                return
            }
        } else {
            if (!(FormValidation.checkPermission(
                    this, Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) && FormValidation.checkPermission(
                    this, Manifest.permission.READ_EXTERNAL_STORAGE
                ))
            ) {
                FormValidation.showPermissionsDialog(
                    this, storagePermissions, AppConstants.REQUEST_CODE_STORAGE_PERMISSION
                )
                return
            }
        }
        permissionGrantedSuccessFully()
    }

    /**
     * if permission grand then show select option
     */
    private fun permissionGrantedSuccessFully() {
        if (image == Image.GALLERY) {
            launchGallery()
        } else if (image == Image.CAMERA) {
            launchCamera()
        } else {
            showImageChooserDialog()
        }
    }

    /**
     * if user cancel request for image or else something want to wrong
     */
    private fun resultFailed() {
        setResult(RESULT_CANCELED)
        finish()
    }

    /**
     * Image get success and send image path to previous activity
     *
     * @param imagePath image path get from camera or gallery
     */
    private fun resultSuccess(imagePath: String) {
        val intent = Intent()
        intent.putExtra(AppConstants.image, imagePath)
        setResult(RESULT_OK, intent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e(TAG, "requestCode=======$requestCode")
        Log.e(TAG, "resultCode=======$resultCode")
        Log.e(TAG, "onActivityResult=======$data")
        when (requestCode) {
            AppConstants.REQUEST_CODE_PERMISSION_RESULT -> if (FormValidation.checkPermission(
                    this@CameraGalleryActivity, Manifest.permission.CAMERA
                ) && FormValidation.checkPermission(
                    this@CameraGalleryActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) && FormValidation.checkPermission(
                    this@CameraGalleryActivity, Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                permissionGrantedSuccessFully()
            } else {
                resultFailed()
            }

            CAMERA_REQUEST -> if (resultCode == RESULT_OK) {
                try {
                    Log.e(TAG, "INSIDE CAMERA RESULT=======$mImageCaptureUri")
                    imagePath = getRealPathFromURI(mImageCaptureUri)!!
                    beginCrop(mImageCaptureUri!!)
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                    showErrorSnackBar(binding.rootLayout, resources.getString(R.string.dialog_file_not_found))
                    resultFailed()
                }
            } else {
                resultFailed()
            }

            ACTION_REQUEST_GALLERY -> if (resultCode == RESULT_OK) {
                Log.e(TAG, "INSIDE GALLERY RESULT=======")
                try {
                    val mGalleryImageCaptureUri = data?.data
                    mImageCaptureUri = data?.data
                    imagePath = getRealPathFromURI(mGalleryImageCaptureUri)!!
                    beginCrop(mImageCaptureUri!!)
                    Log.e(TAG, "IMAGE PATH GALLERY..$imagePath")
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                    showErrorSnackBar(binding.rootLayout, resources.getString(R.string.dialog_file_not_found))
                    resultFailed()
                }
            } else {
                resultFailed()
            }

            AppConstants.FLAG_CROP -> if (resultCode == RESULT_OK) {
                try {
                    imagePath = data?.getStringExtra(AppConstants.image)!!
                    Log.e(TAG, "IMAGE PATH GALLERY..$imagePath")
                    file = File(imagePath)
                    if (file!!.exists()) {
                        resultSuccess(imagePath)
                    } else {
                        imagePath = ""
                        showErrorSnackBar(binding.rootLayout, resources.getString(R.string.dialog_file_not_found))
                        resultFailed()
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                    showErrorSnackBar(binding.rootLayout, resources.getString(R.string.dialog_file_not_found))
                    resultFailed()
                }
            } else {
                resultFailed()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.e(TAG, "requestCode=======$requestCode")
        when (requestCode) {
            AppConstants.REQUEST_CODE_CAMERA_PERMISSION -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                chkPermission()
            } else {
                showConfirmationDialog(
                    this@CameraGalleryActivity,
                    object : DialogButtonClickListener {
                        override fun onPositiveButtonClick() {
                            if (isCameraPermissionDeny()) { //retry
                                chkPermission()
                            } else { //setting
                                navigateUserToAppSettingDetailsScreen()
                            }
                        }

                        override fun onNegativeButtonClick() { //i am sure
                            resultFailed()
                        }
                    },
                    resources.getString(R.string.text_camera_permission_msg_profile),
                    if (isCameraPermissionDeny()) resources.getString(R.string.retry) else resources.getString(
                        R.string.setting
                    ),
                    resources.getString(R.string.text_i_am_sure)
                )
            }

            AppConstants.REQUEST_CODE_STORAGE_PERMISSION -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    chkPermission()
                } else {
                    showConfirmationDialog(
                        this@CameraGalleryActivity,
                        object : DialogButtonClickListener {
                            override fun onPositiveButtonClick() {
                                if (isStoragePermissionDeny()) { //retry
                                    chkPermission()
                                } else { //setting
                                    navigateUserToAppSettingDetailsScreen()
                                }
                            }

                            override fun onNegativeButtonClick() { //i am sure
                                resultFailed()
                            }
                        },
                        resources.getString(R.string.text_save_profile_picture_msg),
                        if (isStoragePermissionDeny()) resources.getString(R.string.retry) else resources.getString(
                            R.string.setting
                        ),
                        resources.getString(R.string.text_i_am_sure)
                    )
                }
            }
        }
    }

    /**
     * check camera permission for user take image from camera
     */
    private fun isCameraPermissionDeny(): Boolean {
        return FormValidation.shouldShowRequestPermissionRationale(
            this, Manifest.permission.CAMERA
        )
    }

    /**
     * check storage permission fro user get image from gallery or storage
     */
    private fun isStoragePermissionDeny(): Boolean {
        return FormValidation.shouldShowRequestPermissionRationale(
            this@CameraGalleryActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    private fun navigateUserToAppSettingDetailsScreen() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, AppConstants.REQUEST_CODE_PERMISSION_RESULT)
    }

    /**************************************************************************************
     * ********************************* CAMERA *******************************************
     * ************************************************************************************
     * OPEN IMAGE CHOOSE DIALOG
     */
    private fun showImageChooserDialog() {
        val builder = AlertDialog.Builder(
            this@CameraGalleryActivity, R.style.MyAlertDialogStyle
        )
        val option = arrayOf<CharSequence>(
            resources.getString(R.string.text_gallery), resources.getString(R.string.text_camera)
        )
        builder.setCancelable(false)
        builder.setItems(
            option
        ) { dialog: DialogInterface, which: Int ->
            if (which == 0) {
                dialog.cancel()
                launchGallery()
            } else {
                dialog.cancel()
                launchCamera()
            }
        }
            .setPositiveButton(resources.getString(R.string.cancel)) { dialog: DialogInterface, id: Int ->
                dialog.cancel()
                resultFailed()
            }
        builder.show()
//        val dialog = Dialog(this)
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog.setCancelable(false)
//        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        dialog.setContentView(R.layout.dialog_gallery_camera_picker)
//
//        val ivClose = dialog.findViewById(R.id.ivClose) as ImageView
//        val clGallery = dialog.findViewById(R.id.clGallery) as ConstraintLayout
//        val clCamera = dialog.findViewById(R.id.clCamera) as ConstraintLayout
//
//        ivClose.setOnClickListener { dialog.dismiss() }
//
//        clGallery.setOnClickListener {
//            dialog.dismiss()
//            launchGallery()
//        }
//        clCamera.setOnClickListener {
//            dialog.dismiss()
//            launchCamera()
//        }
//        dialog.setOnDismissListener {
//            onBackPressed()
//        }
//        dialog.show()
    }

    /**
     * LAUNCH GALLERY FOR CHOOSE IMAGE
     */
    private fun launchGallery() {
        val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultGalleryLauncher.launch(i)
    }

    private var resultGalleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            Log.e(TAG, "INSIDE GALLERY RESULT=======")
            try {
                val data: Intent? = result.data
                val mGalleryImageCaptureUri = data?.data
                mImageCaptureUri = data?.data
                imagePath = getRealPathFromURI(mGalleryImageCaptureUri)!!
                beginCrop(mImageCaptureUri!!)
                Log.e(TAG, "IMAGE PATH GALLERY..$imagePath")

                fullGalleryPath = getRealPathFromURIGallery(mImageCaptureUri)!!
//                Toast.makeText(this, "Full Path: $fullPath", Toast.LENGTH_LONG).show()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                showErrorSnackBar(binding.rootLayout, resources.getString(R.string.dialog_file_not_found))
                resultFailed()
            }
        } else {
            resultFailed()
        }
    }


    /**
     * LAUNCH CAMERA FOR CAPTURE IMAGE
     */
    private fun launchCamera() {
        val cameraIntent = Intent(
            MediaStore.ACTION_IMAGE_CAPTURE
        )
        val filename =
            ("IMG_" + resources.getString(R.string.app_name) + "_" + System.currentTimeMillis() + ".jpg")
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, filename)
        val file = File(
            Objects.requireNonNull(getExternalFilesDir(null))!!.absolutePath + "/" + filename
        )
        mImageCaptureUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            FileProvider.getUriForFile(
                this@CameraGalleryActivity, "$packageName.provider", file
            )
        } else {
            Uri.fromFile(file)
        }
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri)
        startActivityForResult(cameraIntent, CAMERA_REQUEST)
    }

    /**
     * OPEN CROP ACTIVITY FOR CROP IMAGE
     *
     * @param source uri
     */
    private fun beginCrop(source: Uri) {
        Log.e(TAG, "....BEGIN CROP....")
        var file: File? = null
        file = File(AppConstants.IMAGE_ROOT_FOLDER)
        file!!.mkdirs()
        file = File(
            AppConstants.IMAGE_ROOT_FOLDER + "/" + AppConstants.IMAGE_FILE_NAME_PREFIX.replace(
                "X", FormValidation.getCurrentNanoTime()
            )
        )
        Log.e(TAG, "file::$file")
        val destination = Uri.fromFile(file)
        // Crop.of(source, destination).asSquare().start(this);
        startCrop(source)
    }

    @Throws(IOException::class)
    fun fixOrientation(mBitmap: Bitmap, isOrientation: Boolean): Bitmap {
        var mBitmap = mBitmap
        var rotatedBitmap: Bitmap? = null
        if (mBitmap.width > mBitmap.height) {
            val matrix = Matrix()
            if (isOrientation) {
                val ei = ExifInterface(imagePath)
                val orientation = ei.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED
                )
                when (orientation) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> rotatedBitmap = rotateImage(mBitmap, 90f)

                    ExifInterface.ORIENTATION_ROTATE_180 -> rotatedBitmap =
                        rotateImage(mBitmap, 180f)

                    ExifInterface.ORIENTATION_ROTATE_270 -> rotatedBitmap =
                        rotateImage(mBitmap, 270f)

                    ExifInterface.ORIENTATION_NORMAL -> rotatedBitmap = mBitmap
                    else -> rotatedBitmap = mBitmap
                }
            }
            mBitmap = Bitmap.createBitmap(
                rotatedBitmap!!, 0, 0, rotatedBitmap.width, rotatedBitmap.height, matrix, true
            )
        }
        return mBitmap
    }

    private fun rotateImage(source: Bitmap, angle: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            source, 0, 0, source.width, source.height, matrix, true
        )
    }

    fun bitmapConvertToFile(bitmap: Bitmap): File? {
        var fileOutputStream: FileOutputStream? = null
        var file: File? = null
        try {
            val filesDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)!!
            if (!filesDir.exists()) {
                filesDir.mkdirs()
            }
            val imgName = "IMG_" + SimpleDateFormat(
                "yyyyMMddHHmmss",
                Locale.ENGLISH
            ).format(Calendar.getInstance().time) + ".jpg"
            file = File(filesDir, imgName)
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    throw IOException("Cant able to create file")
                }
            }
            fileOutputStream = FileOutputStream(file)
            if (compress) bitmap.compress(
                Bitmap.CompressFormat.JPEG, 80, fileOutputStream
            ) else bitmap.compress(
                Bitmap.CompressFormat.JPEG, 100, fileOutputStream
            )
            val finalFile: File = file

            MediaScannerConnection.scanFile(
                this, arrayOf(file.absolutePath), null
            ) { path, uri ->
                runOnUiThread {
                    val i = Intent()
                    i.putExtra(AppConstants.image, finalFile.absolutePath)
                    i.putExtra("fullGalleryPath", fullGalleryPath)
                    i.putExtra(AppConstants.pdfPngType, pdfPngType)
                    setResult(RESULT_OK, i)
                    finish()
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.flush()
                    fileOutputStream.close()
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }
        return file
    }

    /**
     * START ACTIVITY
     *
     * @param imageUri
     */
    private fun startCrop(imageUri: Uri) {
        Log.e(TAG, "IMAGE URI...$imageUri")
//        val intent = Intent(this@CameraGalleryActivity, CropActivity::class.java)
//        intent.data = imageUri
//        intent.putExtra(AppConstants.FLAG_IS_SQUARE, FLAG_IS_SQUARE)
//        startActivityForResult(intent, AppConstants.FLAG_CROP)

        try {
            Log.e(TAG, "....BEGIN CROP....")
            var file: File? = null
            file = File(AppConstants.IMAGE_ROOT_FOLDER)
            file!!.mkdirs()
            file = File(
                AppConstants.IMAGE_ROOT_FOLDER + "/" + AppConstants.IMAGE_FILE_NAME_PREFIX.replace(
                    "X",
                    FormValidation.getCurrentNanoTime()
                )
            )
            Log.e(TAG, "file:: Name$file")
            val destination = Uri.fromFile(file)
            // Crop.of(source, destination).asSquare().start(this);
//        startCrop(source);
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, Uri.parse(imageUri.toString()))
            if (bitmap != null) {
                bitmapConvertToFile(fixOrientation(bitmap, true))
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    /**
     * GET ABSOLUTE PATH FROM URI
     *
     * @param contentUri image uri
     */
    private fun getRealPathFromURI(contentUri: Uri?): String? {
        val contentResolver = contentResolver ?: return null

        // Create file path inside app's data dir
        val filePath = (applicationInfo.dataDir + File.separator + System.currentTimeMillis())
        val file = File(filePath)
        try {
            val inputStream = contentResolver.openInputStream(contentUri!!) ?: return null
            val outputStream: OutputStream = FileOutputStream(file)
            val buf = ByteArray(1024)
            var len: Int
            while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
            outputStream.close()
            inputStream.close()
        } catch (ignore: IOException) {
            return null
        }
        return file.absolutePath
    }

    private fun getRealPathFromURIGallery(contentUri: Uri?): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = contentResolver.query(contentUri!!, proj, null, null, null)
        cursor?.moveToFirst()
        val columnIndex: Int = cursor!!.getColumnIndex(proj[0])
        val result: String = cursor.getString(columnIndex)
        cursor.close()
        return result
    }
}