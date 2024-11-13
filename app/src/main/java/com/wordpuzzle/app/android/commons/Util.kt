package com.wordpuzzle.app.android.commons

import android.graphics.Color
import java.util.Random

/**
 * Created by abdularis on 23/06/17.
 */
object Util {
    const val NULL_CHAR = '\u0000'
    private val sRand = Random()
    @JvmStatic
    fun getRandomColorWithAlpha(alpha: Int): Int {
        val r = randomInt % 256
        val g = randomInt % 256
        val b = randomInt % 256
        return Color.argb(alpha, r, g, b)
    }

    val randomChar: Char
        get() =// ASCII A = 65 - Z = 90
            getRandomIntRange(65, 90).toChar()

    /**
     * generate random integer between min and max (inclusive)
     * example: min = 5, max = 7 output would be (5, 6, 7)
     *
     * @param min minimum integer number to be generated
     * @param max maximum integer number to be generated (inclusive)
     * @return integer between min - max
     */
    @JvmStatic
    fun getRandomIntRange(min: Int, max: Int): Int {
        return min + randomInt % (max - min + 1)
    }

    @JvmStatic
    val randomInt: Int
        get() = Math.abs(sRand.nextInt())

    @JvmStatic
    fun getIndexLength(start: GridIndex, end: GridIndex): Int {
        val x = Math.abs(start.col - end.col)
        val y = Math.abs(start.row - end.row)
        return Math.max(x, y) + 1
    }

    @JvmStatic
    fun <T> randomizeList(list: MutableList<T>) {
        val count = list.size
        for (i in 0 until count) {
            val randIdx = getRandomIntRange(Math.min(i + 1, count - 1), count - 1)
            val temp = list[randIdx]
            list[randIdx] = list[i]
            list[i] = temp
        }
    }

    @JvmStatic
    fun getReverseString(str: String): String {
        val out = StringBuilder()
        for (i in str.length - 1 downTo 0) out.append(str[i])
        return out.toString()
    }

    /**
     * Isi slot / element yang masih kosong dengan karakter acak
     *
     */
    @JvmStatic
    fun fillNullCharWidthRandom(gridArr: Array<CharArray?>?) {
        for (i in gridArr!!.indices) {
            for (j in gridArr[i]!!.indices) {
                if (gridArr[i]!![j] == NULL_CHAR) gridArr[i]!![j] = randomChar
            }
        }
    }

    /**
     * Urutkan list strings dari panjang string yang terbesar ke terkecil
     *
     */
    fun sortByLength(strings: MutableList<String>) {
        for (i in strings.indices) {
            for (j in i + 1 until strings.size) {
                if (strings[j].length > strings[i].length) {
                    val temp = strings[j]
                    strings[j] = strings[i]
                    strings[i] = temp
                }
            }
        }
    }
}
