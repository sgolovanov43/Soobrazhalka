package sgolovanov.childrenpiramid;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergey on 05.02.2016.
 */
public class LevelItem {
    String background=null;
    String list;
    String sound;
    List<PictureItem> items=null;
    int animatedBackground=0;
    int levelType;
    boolean isBukvy=false;
    //int backgroundRes=0;
    LevelItem(String background, String list, String sound, PictureItem items[])
    {
        this.levelType=1;
        this.background=background;
        this.list=list;
        this.sound=sound;
        setItemsByArray(items);
    }
    LevelItem(int levelType, String background, String list, String sound, PictureItem items[])
    {
        this.levelType=levelType;
        this.background=background;
        this.list=list;
        this.sound=sound;
        setItemsByArray(items);
    }
    /*LevelItem(int levelType, int backgroundRes, String list, String sound, PictureItem items[])
    {
        this.levelType=levelType;
        this.backgroundRes=backgroundRes;
        this.list=list;
        this.sound=sound;
        setItemsByArray(items);
    }*/
    LevelItem(int levelType, String background, int animatedBackground, String list, String sound, PictureItem items[])
    {
        this.levelType=levelType;
        this.background=background;
        this.animatedBackground=animatedBackground;
        this.list=list;
        this.sound=sound;
        setItemsByArray(items);
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

    void setItemsByArray(PictureItem[] itemsByArray)
    {
        if(items==null)
            items=new ArrayList<PictureItem>();
        else
            items.clear();
        if(itemsByArray==null)
            return;
        for(int i=0;i<itemsByArray.length;i++)
        {
            items.add(itemsByArray[i]);
        }
    }
    public void addItem(PictureItem item)
    {
        if(items==null)
            items=new ArrayList<PictureItem>();
        items.add(item);
    }
}
