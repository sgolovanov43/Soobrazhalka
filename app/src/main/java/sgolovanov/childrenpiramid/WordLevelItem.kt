package sgolovanov.childrenpiramid

/**
 * Created by Sergey on 19.06.2016.
 */
class WordLevelItem internal constructor(
    background: String?,
    animatedBackground: Int,
    list: String?,
    sound: Int,
    word: String
) : LevelItem(6, background, animatedBackground, list, sound, null) {
    @JvmField
    val symbolWidth: Int = 150

    init {
        val leftPos = (-word.length * symbolWidth) / 2f + symbolWidth / 2f
        val alphabetNum = (GameView.mRnd.nextFloat() * 2).toInt() + 1
        for ((i, element) in word.withIndex()) {
            addItem(PictureItem(leftPos + i * symbolWidth, 310f, element, alphabetNum))
        }
    }
}
