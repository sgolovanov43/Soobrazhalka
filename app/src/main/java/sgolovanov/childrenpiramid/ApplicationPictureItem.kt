package sgolovanov.childrenpiramid

/**
 * Created by Sergey on 15.06.2016.
 */
class ApplicationPictureItem : PictureItem {
    @JvmField
    var listX0: Float
    @JvmField
    var listY0: Float
    @JvmField
    var rotateAngle: Int = 0

    internal constructor(
        x0: Float,
        y0: Float,
        pictureResource: String?,
        soundResource: Int,
        listX0: Float,
        listY0: Float,
        rotateAngle: Int
    ) : super(x0, y0, pictureResource, soundResource) {
        this.listX0 = listX0
        this.listY0 = listY0
        this.rotateAngle = rotateAngle
    }

    internal constructor(
        x0: Float,
        y0: Float,
        pictureResource: String?,
        soundResource: Int,
        listX0: Float,
        listY0: Float
    ) : super(x0, y0, pictureResource, soundResource) {
        this.listX0 = listX0
        this.listY0 = listY0
        //this.rotateAngle=0;
    }
}
