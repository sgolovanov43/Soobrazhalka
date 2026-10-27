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
    items: Array<PictureItem>
) : LevelItem(7, background, animatedBackground, list, sound.orZero(), null) {
    var symbolWidth: Float = 100f
    var sidesItems = IntArray(items.size)

    init {
        var sumItems = 0
        for (i in items.indices) {
            sumItems += items[i].colorResource
            sidesItems[i] = items[i].colorResource
        }
        val columns =
            if (sumItems < 4)  2
            else if (sumItems < 7)  3
            else  4
        val rows = sumItems / columns + (if ((sumItems % columns) == 0) 0 else 1)
        val objectsIndexes = GameView.getRandomIndexes(sumItems)
        val symbolWidth1 = 620f / (rows + (if (items.size > 3) 2.5f else 1.5f))
        val symbolWidth2 = 1200f / (columns + 2.5f)
        symbolWidth = min(symbolWidth1, symbolWidth2)
        var topItems = (620 - (rows - 1) * symbolWidth) / 2f
        if (items.size == 3) topItems += symbolWidth / 2
        var firstItemNum = 0
        for (i in items.indices) {
            for (j in 0..<items[i].colorResource) {
                val col = objectsIndexes[firstItemNum + j] % columns
                val row = objectsIndexes[firstItemNum + j] / columns
                val leftRowItems =
                    (-(if (row == rows - 1) (sumItems - columns * row) else columns) * symbolWidth) / 2f + symbolWidth / 2f

                addItem(
                    PictureItem(
                        leftRowItems + col * symbolWidth,
                        topItems + row * symbolWidth,
                        items[i].pictureResource,
                        items[i].soundResource,
                        i
                    )
                )
            }
            firstItemNum += items[i].colorResource
        }
    }
}
