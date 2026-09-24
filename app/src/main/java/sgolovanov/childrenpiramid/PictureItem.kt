package sgolovanov.childrenpiramid;

/**
 * Created by Sergey on 04.02.2016.
 */
public class PictureItem {
    String pictureResource=null;
    String[] soundResource=null;
    //String soundResource2=null;
    float x0;
    float y0;
    int colorResource;
    boolean isBukva;
    static String symbols="АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
    static String[] symbolsSound1={
            "b1","b2","b3","b4","b5","b6","b7","b8","b9","b10",
            "b11","b12","b13","b14","b15","b16","b17","b18","b19","b20",
            "b21","b22","b23","b24","b25","b26","b27","b28","b29","b30",
            "b31","b32","b33"
    };
    static String[] symbolsSound2={
            "z1","z2","z3","z4","z5","z6","z7","z8","z9","z10",
            "z11","z12","z13","z14","z15","z16","z17","z18","z19","z20",
            "z21","z22","z23","z24","z25","z26","z27","z28","z29","z30",
            "z31","z32","z33"
    };
    static String[] alphabet1={
            "bukva1_a","bukva1_b","bukva1_v","bukva1_g","bukva1_d","bukva1_e","bukva1_yo","bukva1_zh","bukva1_z","bukva1_i","bukva1_ik",
            "bukva1_k","bukva1_l","bukva1_m","bukva1_n","bukva1_o","bukva1_p","bukva1_r","bukva1_s","bukva1_t","bukva1_u","bukva1_f",
            "bukva1_h","bukva1_c","bukva1_ch","bukva1_sh","bukva1_sch","bukva1_tz","bukva1_y","bukva1_mz","bukva1_ee","bukva1_yu","bukva1_ya",
    };
    static String[] alphabet2={
            "bukva2_a","bukva2_b","bukva2_v","bukva2_g","bukva2_d","bukva2_e","bukva2_yo","bukva2_zh","bukva2_z","bukva2_i","bukva2_ik",
            "bukva2_k","bukva2_l","bukva2_m","bukva2_n","bukva2_o","bukva2_p","bukva2_r","bukva2_s","bukva2_t","bukva2_u","bukva2_f",
            "bukva2_h","bukva2_c","bukva2_ch","bukva2_sh","bukva2_sch","bukva2_tz","bukva2_y","bukva2_mz","bukva2_ee","bukva2_yu","bukva2_ya",
    };
    static String[] alphabet3={
            "bukva_1","bukva_2","bukva_3","bukva_4","bukva_5","bukva_6","bukva_7","bukva_8","bukva_9","bukva_10",
            "bukva_11","bukva_12","bukva_13","bukva_14","bukva_15","bukva_16","bukva_17","bukva_18","bukva_19","bukva_20",
            "bukva_21","bukva_22","bukva_23","bukva_24","bukva_25","bukva_26","bukva_27","bukva_28","bukva_29","bukva_30",
            "bukva_31","bukva_32","bukva_33",
    };

    PictureItem(float x0, float y0, String pictureResource, String soundResource)
    {
        this.x0=x0;
        this.y0=y0;
        this.pictureResource=pictureResource;
        setSoundResource(soundResource);
        colorResource=0;
    }
    PictureItem(float x0, float y0, String pictureResource, int colorResource)
    {
        this.x0=x0;
        this.y0=y0;
        this.pictureResource=pictureResource;
        this.soundResource=null;
        this.colorResource=colorResource;
    }
    PictureItem(float x0, float y0, String pictureResource, String soundResource, int colorResource)
    {
        this.x0=x0;
        this.y0=y0;
        this.pictureResource=pictureResource;
        setSoundResource(soundResource);
        this.colorResource=colorResource;
    }
    PictureItem(float x0, float y0, String pictureResource, String[] soundResource, int colorResource)
    {
        this.x0=x0;
        this.y0=y0;
        this.pictureResource=pictureResource;
        this.soundResource=soundResource;
        //this.soundResource=soundResource;
        this.colorResource=colorResource;
        //this.soundResource2=soundResource2;
    }
    PictureItem(float x0, float y0, char symbol, int alphabetNum)
    {
        this.x0=x0;
        this.y0=y0;
        int symbolNum=symbolNumberByChar(symbol);
        if(alphabetNum==0)
            alphabetNum=GameView.mRnd.nextInt(2)+1;
        if(alphabetNum==1)
            pictureResource=alphabet1[symbolNum];
        else if(alphabetNum==2)
            pictureResource=alphabet2[symbolNum];
        else
            pictureResource=alphabet3[symbolNum];
        soundResource=new String[]{symbolsSound1[symbolNum],symbolsSound2[symbolNum]};
        isBukva=true;
        colorResource=symbolNum;
    }
    int symbolNumberByChar(char symbol)
    {
        return symbols.indexOf(symbol);
    }
    void setSoundResource(String soundResource)
    {
        if(soundResource==null)
            this.soundResource=null;
        else
            this.soundResource=new String[] {soundResource};
    }
}
