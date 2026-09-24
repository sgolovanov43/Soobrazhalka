package sgolovanov.childrenpiramid

import kotlin.math.min

/**
 * Created by Sergey on 19.06.2016.
 */
class MoveToSidesLevelItem internal constructor(
    background: String?,
    animatedBackground: Int,
    list: String?,
    sound: Int?,
    items: Array<PictureItem?>
) : LevelItem(7, background, animatedBackground, list, sound.orZero(), null) {
    @JvmField
    var symbolWidth: Float = 100f
    @JvmField
    var sidesItems: IntArray? = null

    init {
        sidesItems = IntArray(items.size)
        var sumItems = 0
        for (i in items.indices) {
            sumItems += items[i]!!.colorResource
            sidesItems!![i] = items[i]!!.colorResource
        }
        val colums: Int
        if (sumItems < 4) colums = 2
        else if (sumItems < 7) colums = 3
        else colums = 4
        val rows = sumItems / colums + (if ((sumItems % colums) == 0) 0 else 1)
        val objectsIndexes = GameView.getRandomIndexes(sumItems)
        val symbolWidth1 = 620f / (rows + (if (items.size > 3) 2.5f else 1.5f))
        val symbolWidth2 = 1200f / (colums + 2.5f)
        symbolWidth = min(symbolWidth1, symbolWidth2)
        var topItems = (620 - (rows - 1) * symbolWidth) / 2f
        if (items.size == 3) topItems += symbolWidth / 2
        var firstItemNum = 0
        for (i in items.indices) {
            for (j in 0..<items[i]!!.colorResource) {
                val col = objectsIndexes[firstItemNum + j] % colums
                val row = objectsIndexes[firstItemNum + j] / colums
                val leftRowItems =
                    (-(if (row == rows - 1) (sumItems - colums * row) else colums) * symbolWidth) / 2f + symbolWidth / 2f

                addItem(
                    PictureItem(
                        leftRowItems + col * symbolWidth,
                        topItems + row * symbolWidth,
                        items[i]!!.pictureResource,
                        items[i]!!.soundResource,
                        i
                    )
                )
            }
            firstItemNum += items[i]!!.colorResource
        }
    }
}
