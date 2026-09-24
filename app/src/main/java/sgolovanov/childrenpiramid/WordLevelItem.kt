package sgolovanov.childrenpiramid;

/**
 * Created by Sergey on 19.06.2016.
 */
public class WordLevelItem extends LevelItem {
    final int symbolWidth=150;
    WordLevelItem(String background, int animatedBackground, String list, String sound, String word) {
        super(6, background, animatedBackground, list, sound, null);
        float leftPos=(-word.length()*symbolWidth)/2f+symbolWidth/2f;
        int alphabetNum= (int) (GameView.mRnd.nextFloat()*2)+1;
        for(int i=0;i<word.length();i++)
        {
            addItem(new PictureItem(leftPos+i*symbolWidth,310,word.charAt(i),alphabetNum));
        }
    }
    public int getSymbolWidth()
    {
        return symbolWidth;
    }
}
