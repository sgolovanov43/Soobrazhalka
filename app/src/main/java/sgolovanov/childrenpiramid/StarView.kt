package sgolovanov.childrenpiramid

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.widget.AppCompatImageView
import java.util.Random

/**
 * Created by Sergey on 08.06.2016.
 */
class StarView(context: Context) : AppCompatImageView(context) {
    init {
        val mRnd = Random()
        starBitmaps?.let { starBitmaps ->
            setImageBitmap(starBitmaps[mRnd.nextInt(starsRes.size)])
        }
    }

    companion object {
        var starBitmaps: Array<Bitmap?>? = null

        var starsRes: IntArray = intArrayOf(
            R.drawable.back_star1,
            R.drawable.back_star2,
            R.drawable.back_star3,
            R.drawable.back_star4,
            R.drawable.back_star5,
            R.drawable.back_star6
        )

        fun init(context: Context) {
            if (starBitmaps == null) {
                starBitmaps = arrayOfNulls(starsRes.size)
                for (i in starsRes.indices)
                    starBitmaps?.set(
                        i,
                        BitmapFactory.decodeResource(
                            context.resources,
                            starsRes[i]
                        )
                    )
            }
        }
    }
}
