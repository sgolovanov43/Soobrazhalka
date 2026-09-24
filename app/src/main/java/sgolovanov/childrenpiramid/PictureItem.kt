package sgolovanov.childrenpiramid

/**
 * Created by Sergey on 04.02.2016.
 */
open class PictureItem {
    var pictureResource: String? = null
    var soundResource: Array<Int>? = null

    var x0: Float
    var y0: Float
    var colorResource: Int
    var isBukva: Boolean = false

    internal constructor(x0: Float, y0: Float, pictureResource: String?, soundResource: Int?) {
        this.x0 = x0
        this.y0 = y0
        this.pictureResource = pictureResource
        setSoundResource(soundResource.orZero())
        colorResource = 0
    }

    internal constructor(
        x0: Float,
        y0: Float,
        pictureResource: String?,
        soundResource: Int?,
        colorResource: Int
    ) {
        this.x0 = x0
        this.y0 = y0
        this.pictureResource = pictureResource
        setSoundResource(soundResource.orZero())
        this.colorResource = colorResource
    }

    internal constructor(
        x0: Float,
        y0: Float,
        pictureResource: String?,
        soundResource: Array<Int>?,
        colorResource: Int
    ) {
        this.x0 = x0
        this.y0 = y0
        this.pictureResource = pictureResource
        this.soundResource = soundResource
        //this.soundResource=soundResource;
        this.colorResource = colorResource
        //this.soundResource2=soundResource2;
    }

    internal constructor(x0: Float, y0: Float, symbol: Char, alphabetNum: Int) {
        var alphabetNum = alphabetNum
        this.x0 = x0
        this.y0 = y0
        val symbolNum = symbolNumberByChar(symbol)
        if (alphabetNum == 0) alphabetNum = GameView.mRnd.nextInt(2) + 1
        if (alphabetNum == 1) pictureResource = alphabet1[symbolNum]
        else if (alphabetNum == 2) pictureResource = alphabet2[symbolNum]
        else pictureResource = alphabet3[symbolNum]
        soundResource = arrayOf(symbolsSound1[symbolNum], symbolsSound2[symbolNum])
        isBukva = true
        colorResource = symbolNum
    }

    fun symbolNumberByChar(symbol: Char): Int {
        return symbols.indexOf(symbol)
    }

    fun setSoundResource(soundResId: Int) {
        soundResource = arrayOf(soundResId)
    }

    companion object {
        var symbols: String = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ"
        var symbolsSound1 = arrayOf(
            R.raw.b1, R.raw.b2, R.raw.b3, R.raw.b4, R.raw.b5, R.raw.b6, R.raw.b7, R.raw.b8, R.raw.b9, R.raw.b10,
            R.raw.b11, R.raw.b12, R.raw.b13, R.raw.b14, R.raw.b15, R.raw.b16, R.raw.b17, R.raw.b18, R.raw.b19, R.raw.b20,
            R.raw.b21, R.raw.b22, R.raw.b23, R.raw.b24, R.raw.b25, R.raw.b26, R.raw.b27, R.raw.b28, R.raw.b29, R.raw.b30,
            R.raw.b31, R.raw.b32, R.raw.b33
        )
        var symbolsSound2 = arrayOf(
            R.raw.z1, R.raw.z2, R.raw.z3, R.raw.z4, R.raw.z5, R.raw.z6, R.raw.z7, R.raw.z8, R.raw.z9, R.raw.z10,
            R.raw.z11, R.raw.z12, R.raw.z13, R.raw.z14, R.raw.z15, R.raw.z16, R.raw.z17, R.raw.z18, R.raw.z19, R.raw.z20,
            R.raw.z21, R.raw.z22, R.raw.z23, R.raw.z24, R.raw.z25, R.raw.z26, R.raw.z27, R.raw.z28, R.raw.z29, R.raw.z30,
            R.raw.z31, R.raw.z32, R.raw.z33
        )
        var alphabet1 = arrayOf(
            "bukva1_a",
            "bukva1_b",
            "bukva1_v",
            "bukva1_g",
            "bukva1_d",
            "bukva1_e",
            "bukva1_yo",
            "bukva1_zh",
            "bukva1_z",
            "bukva1_i",
            "bukva1_ik",
            "bukva1_k",
            "bukva1_l",
            "bukva1_m",
            "bukva1_n",
            "bukva1_o",
            "bukva1_p",
            "bukva1_r",
            "bukva1_s",
            "bukva1_t",
            "bukva1_u",
            "bukva1_f",
            "bukva1_h",
            "bukva1_c",
            "bukva1_ch",
            "bukva1_sh",
            "bukva1_sch",
            "bukva1_tz",
            "bukva1_y",
            "bukva1_mz",
            "bukva1_ee",
            "bukva1_yu",
            "bukva1_ya",
        )
        var alphabet2 = arrayOf(
            "bukva2_a",
            "bukva2_b",
            "bukva2_v",
            "bukva2_g",
            "bukva2_d",
            "bukva2_e",
            "bukva2_yo",
            "bukva2_zh",
            "bukva2_z",
            "bukva2_i",
            "bukva2_ik",
            "bukva2_k",
            "bukva2_l",
            "bukva2_m",
            "bukva2_n",
            "bukva2_o",
            "bukva2_p",
            "bukva2_r",
            "bukva2_s",
            "bukva2_t",
            "bukva2_u",
            "bukva2_f",
            "bukva2_h",
            "bukva2_c",
            "bukva2_ch",
            "bukva2_sh",
            "bukva2_sch",
            "bukva2_tz",
            "bukva2_y",
            "bukva2_mz",
            "bukva2_ee",
            "bukva2_yu",
            "bukva2_ya",
        )
        var alphabet3 = arrayOf(
            "bukva_1",
            "bukva_2",
            "bukva_3",
            "bukva_4",
            "bukva_5",
            "bukva_6",
            "bukva_7",
            "bukva_8",
            "bukva_9",
            "bukva_10",
            "bukva_11",
            "bukva_12",
            "bukva_13",
            "bukva_14",
            "bukva_15",
            "bukva_16",
            "bukva_17",
            "bukva_18",
            "bukva_19",
            "bukva_20",
            "bukva_21",
            "bukva_22",
            "bukva_23",
            "bukva_24",
            "bukva_25",
            "bukva_26",
            "bukva_27",
            "bukva_28",
            "bukva_29",
            "bukva_30",
            "bukva_31",
            "bukva_32",
            "bukva_33",
        )
    }
}
