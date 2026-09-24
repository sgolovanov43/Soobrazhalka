package sgolovanov.childrenpiramid

/**
 * Created by Sergey on 05.02.2016.
 */
open class LevelItem {
    var background: String? = null
    var list: String?
    var sound: Int = 0
    var items: MutableList<PictureItem> = mutableListOf()
    var animatedBackground: Int = 0
    var levelType: Int
    var isBukvy: Boolean = false

    //int backgroundRes=0;
    internal constructor(
        background: String?,
        list: String?,
        sound: Int = 0,
        items: Array<PictureItem>?
    ) {
        this.levelType = 1
        this.background = background
        this.list = list
        this.sound = sound
        setItemsByArray(items)
    }

    internal constructor(
        levelType: Int,
        background: String?,
        list: String?,
        sound: Int = 0,
        items: Array<PictureItem>?
    ) {
        this.levelType = levelType
        this.background = background
        this.list = list
        this.sound = sound
        setItemsByArray(items)
    }

    /*LevelItem(int levelType, int backgroundRes, String list, String sound, PictureItem items[])
    {
        this.levelType=levelType;
        this.backgroundRes=backgroundRes;
        this.list=list;
        this.sound=sound;
        setItemsByArray(items);
    }*/
    internal constructor(
        levelType: Int,
        background: String?,
        animatedBackground: Int,
        list: String?,
        sound: Int = 0,
        items: Array<PictureItem>?
    ) {
        this.levelType = levelType
        this.background = background
        this.animatedBackground = animatedBackground
        this.list = list
        this.sound = sound
        setItemsByArray(items)
    }

    /*LevelItem(int levelType, int background, int animatedBackground, String list, String sound, PictureItem items[])
    {
        this.levelType=levelType;
        this.backgroundRes=background;
        this.animatedBackground=animatedBackground;
        this.list=list;
        this.sound=sound;
        setItemsByArray(items);
    }*/
    fun setItemsByArray(itemsByArray: Array<PictureItem>?) {
        items.clear()
        if (itemsByArray == null) return
        for (i in itemsByArray.indices) {
            items.add(itemsByArray[i])
        }
    }

    fun addItem(item: PictureItem) {
        items.add(item)
    }
}
