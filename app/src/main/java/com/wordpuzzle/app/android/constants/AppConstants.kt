package com.wordpuzzle.app.android.constants

import com.wordpuzzle.app.android.application.MyApplication
import java.util.Objects
import java.util.regex.Pattern

object AppConstants {
    var cookies: HashSet<String> = HashSet()
    val EMAIL_PATTERN: Pattern = Pattern.compile("^[a-zA-Z0-9]+(?:\\.[0-9a-z]+)*@[a-z0-9]{2,}(?:\\.\\.[a-z]{2,})+$")
//    // defining our own password pattern
//    val PASSWORD_PATTERN: Pattern = Pattern.compile(
//        "^" +
//                "(?=.*\\d)" + // at least 1 number
//                "(?=.*[A-Z])" +  // at least 1 capital letter
//                "(?=.*[@#$%^&+=])" +  // at least 1 special character
//                ".{8,}" +  // at least 8 characters
//                "$"
//    )

    val PASSWORD_PATTERN: Pattern = Pattern.compile(
        "^" +
                "(?=.*\\d)" +        // at least 1 number
                "(?=.*[a-z])" +      // at least 1 lower letter
                "(?=.*[A-Z])" +      // at least 1 capital letter
                "(?=.*[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?])" + // at least 1 special character from the provided set
                ".{8,}" +            // at least 8 characters
                "$"
    )

    /**
     * FROM FLAG
     */
    const val data: String = "data"
    const val gameRoundId: String = "game_round_id"
    const val pageNo: String = "page_no"
    const val score: String = "score"
    const val playTime: String = "play_time"
    const val isShowPopup: String = "is_show_popup"
    const val bookName: String = "book_name"
    const val selectPageNo: String = "select_page_no"
    const val durationMillisecond: String = "duration_millisecond"

    /**
     * API parameters
     */
    const val email: String = "email"
    const val fullName: String = "full_name"
    const val age: String = "age"
    const val generatePassword: String = "generate_password"
    const val alredyGeneratePassword: String = "already_generated_password"
    const val password: String = "password"
    const val bookId: String = "bookId"
    const val fromPageNo: String = "fromPageNo"
    const val toPageNo: String = "toPageNo"
    const val profileImage = "profile_image"
    const val startPageNo = "start_page_no"
    const val endPageNo = "end_page_no"
    const val bookid = "bookid"
    const val pageRange = "page_range"
    const val completionTime = "completion_time"
    const val puzzleId = "puzzle_id"
    const val isRepetitive = "is_repetative"
    const val completionDate = "completion_date"

    const val changePassword: String = "change_password"
    const val currentPassword: String = "current_password"
    const val newPassword: String = "new_password"

    const val isSoundEnable: String = "isSoundEnable"
    const val isMusicEnable: String = "isMusicEnable"




    const val ITEM_CLICK = 0
    const val ITEM_CLICK_OK = 1

    /**
     * RUN TIME PERMISSION REQUEST CODES
     */
    const val REQUEST_CODE_CAMERA_PERMISSION = 110
    const val REQUEST_CODE_STORAGE_PERMISSION = 111
    const val REQUEST_CODE_CAMERA_STORAGE_RESULT = 601

    /**
     * PERMISSION RESULT CODE
     */
    const val REQUEST_CODE_PERMISSION_RESULT = 600
    const val FLAG_CROP = 1314
    const val REQUEST_ADD_EDIT_PATIENT_DETAILS_ACTIVITY = 101

    var image = "image"
    const val IMAGE_FILE_NAME_PREFIX = "IMG_CAM" + "_X" + ".jpg"
    var pdfPngType = "pdfPngType"
    var FLAG_IS_SQUARE = "flag_is_square"

    /**
     * IMAGE/VIDEO DEFINE ROOT FOLDER
     */
    private val APP_ROOT_FOLDER: String = Objects.requireNonNull(MyApplication.context!!.filesDir).absolutePath + "/" + "WordPuzzle" + "/"
    val IMAGE_ROOT_FOLDER = APP_ROOT_FOLDER + "IMAGES"

    /**
     * DATE/TIME FORMAT
     */
    val DATE_YYYY_MM_DD_FORMAT = "yyyy-MM-dd"
}