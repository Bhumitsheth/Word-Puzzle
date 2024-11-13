/*------------------------------------------
 * --- simple math helper (Java version) ---
 * @author abdul_aris_r
 * @date 04 Juni 2016
 *------------------------------------------
 */
package com.wordpuzzle.app.android.commons.math

class Vec2 {
    @JvmField
    var x: Float
    @JvmField
    var y: Float

    @JvmOverloads
    constructor(x: Float = 0.0f, y: Float = 0.0f) {
        this.x = x
        this.y = y
    }

    constructor(v: Vec2) {
        x = v.x
        y = v.y
    }

    fun add(v: Vec2) {
        x += v.x
        y += v.y
    }

    fun add(x: Float, y: Float) {
        this.x += x
        this.y += y
    }

    fun sub(v: Vec2) {
        x -= v.x
        y -= v.y
    }

    fun sub(x: Float, y: Float) {
        this.x -= x
        this.y -= y
    }

    /*
	 * hasil dot product dari dua vector
	 */
    fun dot(v: Vec2): Float {
        return x * v.x + y * v.y
    }

    /*
	 * panjang vector
	 */
    fun length(): Float {
        return Math.sqrt((x * x + y * y).toDouble()).toFloat()
    }

    /*
	 * normalize vector dan return vector baru
	 * (normal vector yaitu vector yang memiliki length = 1)
	 */
    fun normalize() {
        val len = length()
        x /= len
        y /= len
    }

    operator fun set(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    companion object {
        @JvmField
        val Right = Vec2(1f, 0f)

        /*
	 * static function
	 */
        fun add(v1: Vec2, v2: Vec2): Vec2 {
            return Vec2(v1.x + v2.x, v1.y + v2.y)
        }

        fun add(v1: Vec2, x: Float, y: Float): Vec2 {
            return Vec2(v1.x + x, v1.y + y)
        }

        @JvmStatic
        fun sub(v1: Vec2, v2: Vec2): Vec2 {
            return Vec2(v1.x - v2.x, v1.y - v2.y)
        }

        fun sub(v1: Vec2, x: Float, y: Float): Vec2 {
            return Vec2(v1.x - x, v1.y - y)
        }

        fun div(v1: Vec2, div: Float): Vec2 {
            return Vec2(v1.x / div, v1.y / div)
        }

        fun div(v1: Vec2, v2: Vec2): Vec2 {
            return Vec2(v1.x / v2.x, v1.y / v2.y)
        }

        fun div(v1: Vec2, x: Float, y: Float): Vec2 {
            return Vec2(v1.x / x, v1.y / y)
        }

        /*
	 * kalkulasikan normal dari dua vector
	 * (normal yaitu vector yang tegak lurus terhadap bidang)
	 */
        fun normal(v1: Vec2, v2: Vec2): Vec2 {
            return Vec2(-(v1.y - v2.y), v1.x - v2.x)
        }

        @JvmStatic
        fun normalize(v: Vec2): Vec2 {
            val len = v.length()
            return Vec2(v.x / len, v.y / len)
        }
    }
}
