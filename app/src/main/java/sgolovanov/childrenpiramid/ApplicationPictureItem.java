package sgolovanov.childrenpiramid;

/**
 * Created by Sergey on 15.06.2016.
 */
public class ApplicationPictureItem extends PictureItem {
    float listX0;
    float listY0;
    int rotateAngle=0;
    ApplicationPictureItem(float x0, float y0, String pictureResource, String soundResource, float listX0, float listY0, int rotateAngle) {
        super(x0, y0, pictureResource, soundResource);
        this.listX0=listX0;
        this.listY0=listY0;
        this.rotateAngle=rotateAngle;
    }
    ApplicationPictureItem(float x0, float y0, String pictureResource, String soundResource, float listX0, float listY0) {
        super(x0, y0, pictureResource, soundResource);
        this.listX0=listX0;
        this.listY0=listY0;
        //this.rotateAngle=0;
    }
}
