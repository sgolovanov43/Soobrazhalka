package sgolovanov.childrenpiramid;

/**
 * Created by Sergey on 19.06.2016.
 */
public class MoveToSidesLevelItem extends LevelItem {
    float symbolWidth=100;
    int[] sidesItems=null;
    MoveToSidesLevelItem(String background, int animatedBackground, String list, String sound, PictureItem[] items) {
        super(7, background, animatedBackground, list, sound, null);
        sidesItems=new int[items.length];
        int sumItems=0;
        for(int i=0;i<items.length;i++) {
            sumItems+=items[i].colorResource;
            sidesItems[i]=items[i].colorResource;
        }
        int colums;
        if(sumItems<4)
            colums=2;
        else if(sumItems<7)
            colums=3;
        else
            colums=4;
        int rows=sumItems/colums+((sumItems%colums)==0?0:1);
        int[] objectsIndexes=GameView.getRandomIndexes(sumItems);
        float symbolWidth1=620f/(rows+(items.length>3?2.5f:1.5f));
        float symbolWidth2=1200f/(colums+2.5f);
        symbolWidth=Math.min(symbolWidth1,symbolWidth2);
        float topItems=(620-(rows-1)*symbolWidth)/2f;
        if(items.length==3)
            topItems+=symbolWidth/2;
        int firstItemNum=0;
        for(int i=0;i<items.length;i++)
        {
            for(int j=0;j<items[i].colorResource;j++) {
                int col=objectsIndexes[firstItemNum+j]%colums;
                int row=objectsIndexes[firstItemNum+j]/colums;
                float leftRowItems=(-((row==rows-1)?(sumItems-colums*row):colums)*symbolWidth)/2f+symbolWidth/2f;

                addItem(new PictureItem(leftRowItems+col*symbolWidth, topItems+row*symbolWidth, items[i].pictureResource, items[i].soundResource, i));
            }
            firstItemNum+=items[i].colorResource;
        }
    }

    public float getSymbolWidth()
    {
        return symbolWidth;
    }
}
