package sgolovanov.childrenpiramid

var levels: Array<LevelItem> = arrayOf(
    LevelItem(
        1, "raduga_background", -1, "list_4", R.raw.soberem_raketu, arrayOf(
            PictureItem((150 - 450).toFloat(), 404.5f, "raketa1", null),
            PictureItem(209.5f - 450, 186.5f, "raketa2", null),
            PictureItem(380.5f - 450, 519.5f, "raketa3", null),
            PictureItem(401.5f - 450, 269.5f, "raketa4", null),
            PictureItem(604.5f - 450, 205f, "raketa5", null),
            PictureItem(774.5f - 450, 133.5f, "raketa6", null)
        )
    ),

    LevelItem(
        1,
        "den_rozhdenia_back",
        -1,
        "den_rozhdenia_list",
        R.raw.den_rozhdeniya,
        arrayOf(
            PictureItem((654 - 515).toFloat(), 408f, "den_rozhdenia1", null),
            PictureItem(220.5f - 515, 423f, "den_rozhdenia2", null),
            PictureItem((420 - 515).toFloat(), 430f, "den_rozhdenia3", null),
            PictureItem(833.5f - 515, 418f, "den_rozhdenia4", null),
        )
    ),

    LevelItem(
        1, "back1_full", -3, "list_1", R.raw.slogi_piramidky, arrayOf(
            PictureItem(0f, 575.5f, "picture_1_1", R.raw.krasnyi),
            PictureItem(0f, 495f, "picture_1_2", R.raw.zelenyi),
            PictureItem(0f, 415f, "picture_1_3", R.raw.sinii),
            PictureItem(0f, 335f, "picture_1_4", R.raw.zheltyi),
            PictureItem(0f, 255f, "picture_1_5", R.raw.salatoavyi),
            PictureItem(0f, 175f, "picture_1_6", R.raw.fioletovyi),
            PictureItem(0f, 70f, "picture_1_7", R.raw.oranzhevyi)
        )
    ),  //piramidkaItems),

    LevelItem(
        1, "back13_full", 0, "list_4", R.raw.rasstav_griby, arrayOf(
            PictureItem(-250f, 320f, "grib1", R.raw.bolshoi),
            PictureItem(100f, 375f, "grib2", R.raw.srednii),
            PictureItem(360f, 430f, "grib3", R.raw.malenkii)
        )
    ),  //gribyItems), ///-----------------------------------------------

    WordLevelItem("back16_full", 0, "list_12", R.raw.mama, "МАМА"),

    LevelItem(
        1, "africa_background", 16, "list_4", R.raw.giraf, arrayOf(
            PictureItem(19f, 95f, "jiraf1", null),
            PictureItem(10f, 298f, "jiraf2", null),
            PictureItem(-92f, 431f, "jiraf3", null),
            PictureItem(-40f, 509f, "jiraf4", null),
            PictureItem(55f, 511f, "jiraf5", null)
        )
    ),  //zhirafikItems),

    LevelItem(
        1, "leto_oblaka", 0, "ezhik_list2", R.raw.chto_neset_ezik, arrayOf(
            PictureItem(-340f, 120f, "listik", R.raw.list),
            PictureItem(-30f, 170f, "morkovka2", R.raw.morkovka),
            PictureItem(180f, 360f, "yabloko2", R.raw.yabloko),
            PictureItem(400f, 160f, "gribok", R.raw.grib)
        )
    ),  //ezhikItems),

    LevelItem(
        1, "back34_full", -1, "tort_list", R.raw.level_tort, arrayOf(
            PictureItem(324.5f - 325, 441f, "tort1", null),
            PictureItem(313.5f - 325, 332.5f, "tort2", null),
            PictureItem(316f - 325, 231f, "tort3", null),
            PictureItem(318.5f - 325, 166.5f, "tort4", null),
            PictureItem(320f - 325, 109f, "tort5", null),
        )
    ),

    LevelItem(
        1, "back10_full", 11, "list_4", R.raw.soberi_busy, arrayOf(
            PictureItem(450f, 195f, "picture_4_4", null),
            PictureItem(341f, 316f, "picture_4_1", null),
            PictureItem(165f, 376f, "picture_4_3", null),
            PictureItem(0f, 400f, "picture_4_5", null),
            PictureItem(-165f, 376f, "picture_4_8", null),
            PictureItem(-341f, 316f, "picture_4_6", null),
            PictureItem(-450f, 196f, "picture_4_9", null)
        )
    ),  //busyItems),
    WordLevelItem("bottles_background", -3, "list_12", R.raw.papa, "ПАПА"),
    LevelItem(
        1,
        "bottles_background",  /*"back11_full"*/
        0,
        "list_5a",
        R.raw.posadim_cvetok,
        arrayOf(
            PictureItem(-335f, 191f, "picture_5_1", null),
            PictureItem(-162f, 290f, "picture_5_2", null),
            PictureItem(-2f, 195f, "picture_5_3", null),
            PictureItem(165f, 269f, "picture_5_4", null),
            PictureItem(330f, 191f, "picture_5_5", null)
        )
    ),  //cvetyVGorshkahItems),
    /*new MoveToSidesLevelItem("doska",0,"list_12","pravo_levo",new PictureItem[]{
            new PictureItem(0,0,"morkovka2",new String[]{"morkovka","nalevo"},3),
            new PictureItem(0,0,"curl_pomidor",new String[]{"pomidor","napravo"},3),
    }),*/
    LevelItem(
        1, "start_fone2", -3, "maski_list", R.raw.maski, arrayOf(
            PictureItem((390 - 570).toFloat(), 139f, "maski1", null),
            PictureItem(768.5f - 570, 181.5f, "maski2", null),
            PictureItem((964 - 570).toFloat(), 145.5f, "maski3", null),
            PictureItem(159.5f - 570, 162f, "maski4", null),
            PictureItem(583.5f - 570, 164f, "maski5", null)
        )
    ),
    LevelItem(
        1, "back1_full", 0, "list_1", R.raw.soberi_domik, arrayOf(
            PictureItem(290.5f - 300, 67.5f, "domik1", null),
            PictureItem((292 - 300).toFloat(), 219.5f, "domik2", null),
            PictureItem(302.5f - 300, 463f, "domik3", null),
            PictureItem((170 - 300).toFloat(), 444f, "domik4", null),
            PictureItem(432.5f - 300, 442.5f, "domik5", null)
        )
    ),
    LevelItem(
        1,
        "inoplanetyane_background",
        -1,
        "inoplanetyane_list",
        R.raw.smogeh_otpravit_kosmicheskie_apparaty_v_polet,
        arrayOf(
            PictureItem((870 - 500).toFloat(), 101f, "inoplanetyane_1", null),
            PictureItem((154 - 500).toFloat(), 369f, "inoplanetyane_2", null),
            PictureItem((282 - 500).toFloat(), 69f, "inoplanetyane_3", null),
            PictureItem((526 - 500).toFloat(), 208f, "inoplanetyane_4", null),
            PictureItem((842 - 500).toFloat(), 322f, "inoplanetyane_5", null)
        )
    ),
    LevelItem(
        1, "back4_full", -3, "list_4", R.raw.kukolki, arrayOf(
            PictureItem(-365f, 190f, "picture_6_1", null),
            PictureItem(-220f, 430f, "picture_6_3", null),
            PictureItem(-80f, 190f, "picture_6_2", null),
            PictureItem(100f, 430f, "picture_6_4", null),
            PictureItem(265f, 190f, "picture_6_6", null),
            PictureItem(410f, 430f, "picture_6_5", null)
        )
    ),  //kuklyItems),
    LevelItem(
        1, "savana", 24, "list_650x620", R.raw.leopard, arrayOf(
            PictureItem(119f, 130f, "leopard1", null),
            PictureItem(121f, 345f, "leopard2", null),
            PictureItem(224f, 434f, "leopard3", null),
            PictureItem(-193f, 321f, "leopard4", null),
            PictureItem(-19f, 337f, "leopard5", null),
            PictureItem(-165f, 457f, "leopard6", null),
            PictureItem(21f, 450f, "leopard7", null),
            PictureItem(-30f, 466f, "leopard8", null)
        )
    ),  //leopardItems),
    LevelItem(
        3, "fixiki_background", 8, "svetofor_list", R.raw.svetofor, arrayOf(
            PictureItem(
                (213 - 212).toFloat(),
                119f,
                "svetofor_krasnyj",
                R.raw.krasnyi,
                R.drawable.kraska_krasnaya
            ),
            PictureItem(
                (213 - 212).toFloat(),
                269f,
                "svetofor_zheltyj",
                R.raw.zheltyi,
                R.drawable.kraska_zheltaya
            ),
            PictureItem(
                (213 - 212).toFloat(),
                418f,
                "svetofor_zelenyj",
                R.raw.zelenyi,
                R.drawable.kraska_zelenaya
            ),
        )
    ),
    LevelItem(
        1, "back8_full", 0, "list_8", R.raw.ptichki, arrayOf(
            PictureItem(-325f, 263f, "picture_8_1", null),
            PictureItem(-130f, 320f, "picture_8_2", null),
            PictureItem(70f, 347f, "picture_8_3", null),
            PictureItem(236f, 318f, "picture_8_4", null),
            PictureItem(440f, 370f, "picture_8_5", null),
            PictureItem(350f, 120f, "picture_8_6", null)
        )
    ),  //ptichkiNaVetkeItems),
    WordLevelItem("back35_full", 0, "list_12", R.raw.mir, "МИР"),
    LevelItem(
        1, "back1_full", 13, "parovozik", R.raw.poravozik, arrayOf(
            PictureItem(-446f, 355f, "picture_9_2", null),
            PictureItem(-296f, 355f, "picture_9_4", null),
            PictureItem(-134f, 360f, "picture_9_5", null),
            PictureItem(20f, 360f, "picture_9_1", null),
            PictureItem(192f, 299f, "picture_9_3", null)
        )
    ),  //parovozikItems),
    LevelItem(
        1, "back19_full", -2, "list_4", R.raw.slozhi_deda, arrayOf(
            PictureItem(-106f, 153f, "ded_moroz1", null),
            PictureItem(-142f, 377f, "ded_moroz2", null),
            PictureItem(0f, 163f, "ded_moroz3", null),
            PictureItem(68f, 194f, "ded_moroz4", null),
            PictureItem(-64f, 407f, "ded_moroz5", null),
            PictureItem(141f, 558f, "ded_moroz6", null),
            PictureItem(97f, 415f, "ded_moroz7", null)
        )
    ),
    LevelItem(
        1, "kolobok_background", 0, "list_12", R.raw.kolobok, arrayOf(
            PictureItem((1025 - 660).toFloat(), 424.5f, "kolobok_babka", R.raw.baba),
            PictureItem((296 - 660).toFloat(), 390f, "kolobok_lisa", R.raw.lisa),
            PictureItem((487 - 660).toFloat(), 457f, "kolobok_medved", R.raw.medved),
            PictureItem((655 - 660).toFloat(), 248f, "kolobok_volk", R.raw.volk),
            PictureItem((756 - 660).toFloat(), 470f, "kolobok_zayac", R.raw.zayac),
            PictureItem((864 - 660).toFloat(), 354f, "kolobok_kolobok", R.raw.kolobok2),
            PictureItem((1113 - 660).toFloat(), 265f, "kolobok_ded", R.raw.ded)
        )
    ),

    LevelItem(
        1,
        "ovoschi_background",
        0,
        "clear_1200x620",
        R.raw.pologi_ovoshi_na_svou_ten,
        arrayOf(
            PictureItem((191 - 600).toFloat(), 158f, "curl_tykva", R.raw.tykva),
            PictureItem((177 - 600).toFloat(), 460f, "curl_pomidor", R.raw.pomidor),
            PictureItem((443 - 600).toFloat(), 275f, "curl_perchiki", R.raw.ostry_perec),
            PictureItem((565 - 600).toFloat(), 99f, "curl_ogurcy", R.raw.ogyrci),
            PictureItem((545 - 600).toFloat(), 507f, "curl_perec", R.raw.perec),
            PictureItem((788 - 600).toFloat(), 324f, "curl_kapusta", R.raw.kapysta),
            PictureItem((1031 - 600).toFloat(), 164f, "curl_brokkoli", R.raw.brokkoli),
            PictureItem((1031 - 600).toFloat(), 479f, "curl_baklazhan", R.raw.baklazhan)
        )
    ),
    LevelItem(
        1, "repka_background", -1, "list_4", R.raw.znaech_skazky, arrayOf(
            PictureItem((306 - 575).toFloat(), 330f, "repka1", R.raw.repka),
            PictureItem((532 - 575).toFloat(), 342f, "repka2", R.raw.dedka),
            PictureItem((663 - 575).toFloat(), 361f, "repka3", R.raw.babka),
            PictureItem((784 - 575).toFloat(), 394f, "repka4", R.raw.vnychka),
            PictureItem((875 - 575).toFloat(), 411f, "repka5", R.raw.zuchka),
            PictureItem((974 - 575).toFloat(), 427f, "repka6", R.raw.koshka),
            PictureItem((1067 - 575).toFloat(), 445f, "repka7", R.raw.myshka)
        )
    ),  //piramidkaItems),
    LevelItem(
        1, "morozhenye_background", -2, "list_4", R.raw.slozhi_snegurochku, arrayOf(
            PictureItem(0f, 90f, "snegurochka1", null),
            PictureItem(57f, 109f, "snegurochka2", null),
            PictureItem(-50f, 198f, "snegurochka3", null),
            PictureItem(90f, 315f, "snegurochka4", null),
            PictureItem(17f, 193f, "snegurochka5", null),
            PictureItem(-68f, 430f, "snegurochka6", null),
            PictureItem(71f, 423f, "snegurochka7", null)
        )
    ),
    LevelItem(
        1, "stars_texture", -1, "list_4", R.raw.rakety, arrayOf(
            PictureItem(-360f, 400f, "rakety1", null),
            PictureItem(-180f, 200f, "rakety5", null),
            PictureItem(0f, 350f, "rakety2", null),
            PictureItem(200f, 200f, "rakety3", null),
            PictureItem(400f, 400f, "rakety4", null)
        )
    ),  //raketyItems),
    LevelItem(
        1, "back34_full", 0, "list_3", R.raw.soberi_podarki, arrayOf(
            PictureItem(-55f, 104f, "picture_10_1", null),
            PictureItem(55f, 243f, "picture_10_2", null),
            PictureItem(-45f, 382f, "picture_10_3", null),
            PictureItem(-5f, 548f, "picture_10_4", null)
        )
    ),  //podarkiItems),
    WordLevelItem("back21_full", 0, "list_12", R.raw.dryg, "ДРУГ"),
    LevelItem(
        1, "obezyana_background", 0, "list_520x620", R.raw.obezyana, arrayOf(
            PictureItem(-82f, 142f, "obezyana1", null),
            PictureItem(-36f, 334f, "obezyana2", null),
            PictureItem(-53f, 389f, "obezyana3", null),
            PictureItem(-166f, 433f, "obezyana4", null),
            PictureItem(89f, 351f, "obezyana5", null),
            PictureItem(82f, 426f, "obezyana6", null),
            PictureItem(23f, 520f, "obezyana7", null),
            PictureItem(204f, 473f, "obezyana8", null)
        )
    ),  //obezyanaItems),
    LevelItem(
        1, "back26_full", 0, "odezhda_back", R.raw.odezhda, arrayOf(
            PictureItem(-226f, 384f, "odezhda_part1", R.raw.platie),
            PictureItem(-222f, 70f, "odezhda_part2", R.raw.shljapa),
            PictureItem(223f, 60f, "odezhda_part3", R.raw.beret),
            PictureItem(241f, 338f, "odezhda_part4", R.raw.palto),
            PictureItem(262f, 560f, "odezhda_part5", R.raw.sapogi)
        )
    ),  //odezhdaNaKuklahItems),
    LevelItem(
        1, "menu_background", -3, "list_4", R.raw.zaiki, arrayOf(
            PictureItem(-365f, 180f, "picture_11_1", null),
            PictureItem(-241f, 440f, "picture_11_2", null),
            PictureItem(-14f, 180f, "picture_11_5", null),
            PictureItem(98f, 440f, "picture_11_3", null),
            PictureItem(255f, 175f, "picture_11_4", null),
            PictureItem(410f, 440f, "picture_11_6", null)
        )
    ),  //zaikiItems),
    /*new MoveToSidesLevelItem("doska",0,"list_12","pravo_levo_verx",new PictureItem[]{
            new PictureItem(0,0,"morkovka2",new String[]{"morkovka","nalevo"},2),
            new PictureItem(0,0,"grib3",new String[]{"grib","napravo"},3),
            new PictureItem(0,0,"curl_pomidor",new String[]{"pomidor","verx"},3),
    }),*/
    LevelItem(
        1, "back7_full", -3, "list_4", R.raw.mashinki, arrayOf(
            PictureItem(-365f, 150f, "picture_12_1", null),
            PictureItem(-340f, 500f, "picture_12_2", null),
            PictureItem(-80f, 130f, "picture_12_3", null),
            PictureItem(45f, 510f, "picture_12_4", null),
            PictureItem(320f, 120f, "picture_12_5", null),
            PictureItem(390f, 500f, "picture_12_6", null),
            PictureItem(-80f, 285f, "picture_12_7", null)
        )
    ),
    LevelItem(
        1, "morozhenye_background", -2, "korablik_list", R.raw.morogenoe, arrayOf(
            PictureItem((125 - 400).toFloat(), 174f, "morozhenoe1", null),
            PictureItem((314 - 400).toFloat(), 178f, "morozhenoe2", null),
            PictureItem((514 - 400).toFloat(), 170f, "morozhenoe3", null),
            PictureItem((691 - 400).toFloat(), 183f, "morozhenoe4", null),
            PictureItem((123 - 400).toFloat(), 479f, "morozhenoe5", null),
            PictureItem((323 - 400).toFloat(), 467f, "morozhenoe6", null),
            PictureItem((508 - 400).toFloat(), 471f, "morozhenoe7", null),
            PictureItem((693 - 400).toFloat(), 473f, "morozhenoe8", null)
        )
    ),

    LevelItem(
        1, "back10_full", 0, "list_4", R.raw.cartiny, arrayOf(
            PictureItem(-365f, 160f, "picture_15_1", null),
            PictureItem(-365f, 450f, "picture_15_5", null),
            PictureItem(45f, 180f, "picture_15_2", null),
            PictureItem(45f, 460f, "picture_15_3", null),
            PictureItem(365f, 160f, "picture_15_4", null),
            PictureItem(365f, 450f, "picture_15_6", null)
        )
    ),
    /*new LevelItem(5,"back21_full","applicaciya_gribok_list","appl_grib",new PictureItem[]{
            new ApplicationPictureItem(0,357f,"applicaciya_gribok2",null,362,409),
            new ApplicationPictureItem(0,448.5f,"applicaciya_gribok1",null,248,114.5f),
            new ApplicationPictureItem(0,169f,"applicaciya_gribok3",null,113,418.5f,90),
    }),*/
    LevelItem(
        1, "christmas_texture", -2, "list_4", R.raw.soberi, arrayOf(
            PictureItem(-320f, 415f, "ded_moroz", R.raw.ded_moroz),
            PictureItem(-20f, 425f, "snegovik", R.raw.snegovik),
            PictureItem(400f, 413f, "snegurochka", R.raw.snegurochka),
            PictureItem(220f, 280f, "elochnaya_girlyanda", R.raw.girlyanda),
            PictureItem(60f, 103f, "elochnye_igrushki", R.raw.elochnye_igrushki),
            PictureItem(-238f, 122f, "svechka", R.raw.svechka),
            PictureItem(160f, 519f, "snezhnyi_shap", R.raw.snezhnyi_shap)
        )
    ),
    LevelItem(
        "back4_full", "polki", R.raw.banki_na_polkah, arrayOf(
            PictureItem(-100f, 110f, "banka1", null),
            PictureItem(100f, 105f, "banka2", null),
            PictureItem(-150f, 315f, "banka3", null),
            PictureItem(10f, 315f, "banka4", null),
            PictureItem(160f, 315f, "banka5", null),
            PictureItem(-100f, 520f, "banka6", null),
            PictureItem(100f, 520f, "banka7", null)
        )
    ),

    LevelItem(
        1, "stars_texture", -1, "list_860x620", R.raw.kosmos, arrayOf(
            PictureItem((748 - 430).toFloat(), 477f, "kosmos_kosmonavt", R.raw.kosmonavt),
            PictureItem((532 - 430).toFloat(), 481f, "kosmos_lunohod", R.raw.lynoxod),
            PictureItem((143 - 430).toFloat(), 460f, "kosmos_luna", R.raw.lyna),
            PictureItem((162 - 430).toFloat(), 109f, "kosmos_sputnik", R.raw.spytnik),
            PictureItem((373 - 430).toFloat(), 272f, "kosmos_raketa", R.raw.raketa),
            PictureItem((181 - 430).toFloat(), 253f, "kosmos_teleskop", R.raw.teleskop),
            PictureItem((653 - 430).toFloat(), 200f, "kosmos_solnce", R.raw.solnce)
        )
    ),  //raketyItems),
    LevelItem(
        1,
        "vannaya_komnata_background",
        14,
        "vannaya_komnata_list",
        R.raw.pomogi_rasstavit_veshi_v_vannoi,
        arrayOf(
            PictureItem((91 - 463).toFloat(), 266f, "vannaya_komnata_1", R.raw.zerkalo),
            PictureItem((832 - 463).toFloat(), 474f, "vannaya_komnata_2", R.raw.korzina_dlya_belya),
            PictureItem((205 - 463).toFloat(), 106f, "vannaya_komnata_3", R.raw.kyvshin),
            PictureItem((308 - 463).toFloat(), 103f, "vannaya_komnata_4", R.raw.shampoo),
            PictureItem((429 - 463).toFloat(), 88f, "vannaya_komnata_5", R.raw.zybnye_chetki),
            PictureItem((824 - 463).toFloat(), 245f, "vannaya_komnata_6", R.raw.polotence),
            PictureItem((651 - 463).toFloat(), 475f, "vannaya_komnata_7", R.raw.stiralnya_mashina)
        )
    ),
    LevelItem(
        1, "menu_background", -3, "list_4", R.raw.kloun, arrayOf(
            PictureItem(-112f, 96f, "picture_26_1", null),
            PictureItem(-75f, 187f, "picture_26_2", null),
            PictureItem(-48f, 274f, "picture_26_3", null),
            PictureItem(103f, 214f, "picture_26_4", null),
            PictureItem(-155f, 355f, "picture_26_5", null),
            PictureItem(0f, 390f, "picture_26_6", null),
            PictureItem(-58f, 554f, "picture_26_7", null),
            PictureItem(70f, 534f, "picture_26_8", null)
        )
    ),
    LevelItem(
        "back28_full", "list_4", R.raw.cifry, arrayOf(
            PictureItem(-450f, 160f, "cifra_1", R.raw.c1),
            PictureItem(-225f, 160f, "cifra_2", R.raw.c2),
            PictureItem(0f, 160f, "cifra_3", R.raw.c3),
            PictureItem(225f, 160f, "cifra_4", R.raw.c4),
            PictureItem(450f, 160f, "cifra_5", R.raw.c5),
            PictureItem(-337f, 450f, "cifra_6", R.raw.c6),
            PictureItem(-112f, 450f, "cifra_7", R.raw.c7),
            PictureItem(112f, 450f, "cifra_8", R.raw.c8),
            PictureItem(337f, 450f, "cifra_9", R.raw.c9)
        )
    ),

    LevelItem(
        1, "back34_full", -1, "zoo_magazin_list", R.raw.zoo_magazin, arrayOf(
            PictureItem(913f - 517, 157f, "zoo_magazin1", null),
            PictureItem(752.5f - 517, 470.5f, "zoo_magazin2", null),
            PictureItem(497f - 517, 159.5f, "zoo_magazin3", null),
            PictureItem(231.5f - 517, 210f, "zoo_magazin4", null),
            PictureItem(370.5f - 517, 530f, "zoo_magazin5", null),
        )
    ),

    LevelItem(
        "back2_full", "list_4", R.raw.agody, arrayOf(
            PictureItem(-410f, 160f, "picture_7_1", R.raw.vishnya),
            PictureItem(-140f, 160f, "picture_7_2", R.raw.klubnika),
            PictureItem(170f, 160f, "picture_7_3", R.raw.grusha),
            PictureItem(430f, 160f, "picture_7_4", R.raw.sliva),
            PictureItem(-410f, 450f, "picture_7_5", R.raw.yabloko),
            PictureItem(-130f, 450f, "picture_7_8", R.raw.abrikos),
            PictureItem(170f, 450f, "picture_7_7", R.raw.arbuz),
            PictureItem(430f, 450f, "picture_7_6", R.raw.smorodina)
        )
    ),
    /*new LevelItem(5,"back21_full","applicaciya_elka_list","appl_elka",new PictureItem[]{
            new ApplicationPictureItem(0,345.5f,"applicaciya_elka2",null,97.5f,425,-90),
            new ApplicationPictureItem(0,247,"applicaciya_elka3",null,146.5f,68),
            new ApplicationPictureItem(0,451.5f,"applicaciya_elka4",null,147.5f,204f,90),
            new ApplicationPictureItem(0,487.5f,"applicaciya_elka5",null,150.5f,152.5f),
            new ApplicationPictureItem(0,169.5f,"applicaciya_elka1",null,150,546.5f),
    }),*/
    LevelItem(
        1, "back6_full", -3, "list_4", R.raw.matreshki, arrayOf(
            PictureItem(-436f, 523f, "picture_16_1", null),
            PictureItem(-337f, 470f, "picture_16_2", null),
            PictureItem(-170f, 422f, "picture_16_3", null),
            PictureItem(65f, 360f, "picture_16_4", null),
            PictureItem(358f, 310f, "picture_16_5", null)
        )
    ),
    LevelItem(
        1, "back1_full", -3, "list_4", R.raw.figury, arrayOf(
            PictureItem(-365f, 160f, "picture_17_1", R.raw.kvadrat),
            PictureItem(-365f, 450f, "picture_17_2", R.raw.krug),
            PictureItem(45f, 180f, "picture_17_3", R.raw.oval),
            PictureItem(45f, 460f, "picture_17_4", R.raw.treugolnik),
            PictureItem(365f, 160f, "picture_17_5", R.raw.pryamougolnik),
            PictureItem(365f, 450f, "picture_17_6", R.raw.romb)
        )
    ),
    LevelItem(
        1,
        "mama_nas_kuhne_background",
        15,
        "mama_nas_kuhne_list",
        R.raw.pomogi_mame_rasstavit_na_kyxne,
        arrayOf(
            PictureItem((882 - 470).toFloat(), 197f, "mama_nas_kuhne_1", R.raw.terka),
            PictureItem((383 - 470).toFloat(), 340f, "mama_nas_kuhne_2", R.raw.tort),
            PictureItem((561 - 470).toFloat(), 93f, "mama_nas_kuhne_3", R.raw.lopatka2),
            PictureItem((612 - 470).toFloat(), 89f, "mama_nas_kuhne_4", R.raw.doska),
            PictureItem((671 - 470).toFloat(), 94f, "mama_nas_kuhne_5", R.raw.polovnik),
            PictureItem((199 - 470).toFloat(), 368f, "mama_nas_kuhne_6", R.raw.chaynik),
            PictureItem((272 - 470).toFloat(), 169f, "mama_nas_kuhne_7", R.raw.mikrovolnovka),
            PictureItem((164 - 470).toFloat(), 47f, "mama_nas_kuhne_8", R.raw.chaynik),
            PictureItem((558 - 470).toFloat(), 382f, "mama_nas_kuhne_9", R.raw.konfety)
        )
    ),
    LevelItem(
        "back5_full", "list_4", R.raw.ovoschi, arrayOf(
            PictureItem(-400f, 150f, "picture_18_1", R.raw.morkov),
            PictureItem(-134f, 120f, "picture_18_2", R.raw.pomidor),
            PictureItem(134f, 150f, "picture_18_3", R.raw.redis),
            PictureItem(400f, 150f, "picture_18_4", R.raw.goroh),
            PictureItem(-266f, 310f, "picture_18_5", R.raw.perec),
            PictureItem(0f, 310f, "picture_18_6", R.raw.ogurec),
            PictureItem(267f, 310f, "picture_18_7", R.raw.tykva),
            PictureItem(-350f, 500f, "picture_18_9", R.raw.kartofel),
            PictureItem(-120f, 470f, "picture_18_10", R.raw.baklazhan),
            PictureItem(130f, 470f, "picture_18_11", R.raw.chesnok),
            PictureItem(400f, 470f, "picture_18_8", R.raw.luk)
        )
    ),
    LevelItem(
        "back4_full", "list_4", R.raw.a_ti_znaesh, arrayOf(
            PictureItem(-365f, 100f, "dub", R.raw.dub),
            PictureItem(-380f, 320f, "ryabina", R.raw.ryabina),
            PictureItem(-380f, 520f, "sosna", R.raw.sosna),
            PictureItem(-120f, 120f, "bereza", R.raw.bereza),
            PictureItem(-70f, 285f, "el", R.raw.el),
            PictureItem(0f, 483f, "klen", R.raw.klen),
            PictureItem(380f, 115f, "olha", R.raw.olxa),
            PictureItem(350f, 430f, "lipa", R.raw.lipa),
            PictureItem(160f, 190f, "iva", R.raw.iva)
        )
    ),
    LevelItem(
        1, "back33_full", 9, "list_4", R.raw.domashnie_zhivotnye, arrayOf(
            PictureItem(-430f, 150f, "picture_19_6", R.raw.kurica),
            PictureItem(-140f, 150f, "picture_19_2", R.raw.korova),
            PictureItem(160f, 150f, "picture_19_3", R.raw.ovca),
            PictureItem(430f, 150f, "picture_19_4", R.raw.svanya),
            PictureItem(-430f, 450f, "picture_19_5", R.raw.osel),
            PictureItem(-140f, 450f, "picture_19_1", R.raw.loshad),
            PictureItem(160f, 450f, "picture_19_7", R.raw.byk),
            PictureItem(430f, 450f, "picture_19_8", R.raw.utka)
        )
    ),
    /*new LevelItem(5,"back21_full","applicaciya_domik_list","appl_dom",new PictureItem[]{
            new ApplicationPictureItem(108,147,"applicaciya_domik1",null,414.5f,458),
            new ApplicationPictureItem(0,180.5f,"applicaciya_domik2",null,232,112,180),
            new ApplicationPictureItem(0,394,"applicaciya_domik3",null,188.5f,470),
            new ApplicationPictureItem(0,389,"applicaciya_domik4",null,117.5f,278.5f),
            new ApplicationPictureItem(0,389,"applicaciya_domik5",null,343,217,38),
    }),*/
    LevelItem(
        1, "back32_full", 0, "list_4", R.raw.dikie_zhivotnye, arrayOf(
            PictureItem(-365f, 160f, "medved", R.raw.medved),
            PictureItem(-365f, 450f, "lisa", R.raw.lisa),
            PictureItem(0f, 180f, "belka2", R.raw.belka),
            PictureItem(0f, 460f, "zayats1", R.raw.zayac),
            PictureItem(365f, 160f, "ezh", R.raw.ezh),
            PictureItem(350f, 420f, "olen", R.raw.olen)
        )
    ),
    LevelItem(
        1, "back24_full", 11, "list_10", R.raw.zvezdochki, arrayOf(
            PictureItem(-10f, 230f, "picture_23_1", null),
            PictureItem(395f, 375f, "picture_23_2", null),
            PictureItem(-380f, 180f, "picture_23_3", null),
            PictureItem(290f, 145f, "picture_23_4", null),
            PictureItem(160f, 507f, "picture_23_5", null),
            PictureItem(-345f, 460f, "picture_23_6", null)
        )
    ),
    LevelItem(
        1, "back5_full", -3, "list_4", R.raw.mashinki2, arrayOf(
            PictureItem(-340f, 180f, "picture_24_1", R.raw.autobus),
            PictureItem(-200f, 420f, "picture_24_2", R.raw.vertolet),
            PictureItem(0f, 180f, "picture_24_3", R.raw.mashina),
            PictureItem(180f, 420f, "picture_24_4", R.raw.poezd),
            PictureItem(340f, 180f, "picture_24_5", R.raw.samoletik)
        )
    ),
    LevelItem(
        "back13_full", "list_4", R.raw.cvety, arrayOf(
            PictureItem(-410f, 400f, "picture_25_2", R.raw.mak),
            PictureItem(-215f, 400f, "picture_25_1", R.raw.kolokolchik),
            PictureItem(-10f, 400f, "picture_25_3", R.raw.vasilek),
            PictureItem(200f, 400f, "picture_25_4", R.raw.oduvanchik),
            PictureItem(410f, 400f, "picture_25_5", R.raw.romashka)
        )
    ),
    LevelItem(
        "back1_full", "handwashing_list", R.raw.pomogi_mame_povesit_belie, arrayOf(
            PictureItem(-211f, 143f, "handwashing1", null),
            PictureItem(0f, 191f, "handwashing2", null),
            PictureItem(206f, 136f, "handwashing3", null),
            PictureItem(-193f, 441f, "handwashing4", null),
            PictureItem(2f, 475f, "handwashing5", null),
            PictureItem(197f, 462f, "handwashing6", null)
        )
    ),
    LevelItem(
        "savana", "list_4", R.raw.kto_givet_v_garkix_stranax, arrayOf(
            PictureItem(359f, 141f, "exotic1", R.raw.lev2),
            PictureItem(58f, 154f, "exotic2", R.raw.verblyd),
            PictureItem(-219f, 162f, "exotic3", R.raw.kengyry),
            PictureItem(-386f, 380f, "exotic4", R.raw.giraf2),
            PictureItem(-59f, 455f, "exotic5", R.raw.begemot2),
            PictureItem(340f, 441f, "exotic6", R.raw.slon)
        )
    ),
    LevelItem(
        1, "bamboo_list", "list_600x620", R.raw.panda, arrayOf(
            PictureItem(-82f, 64f, "panda1", null),
            PictureItem(148f, 58f, "panda2", null),
            PictureItem(30f, 143f, "panda3", null),
            PictureItem(-157f, 307f, "panda4", null),
            PictureItem(19f, 375f, "panda5", null),
            PictureItem(190f, 371f, "panda6", null),
            PictureItem(-178f, 428f, "panda7", null),
            PictureItem(66f, 511f, "panda8", null)
        )
    ),
    LevelItem(
        3,
        "raduga_background",
        2,
        "raduga_list",
        R.raw.davai_vmeste_soberem_radygy,
        arrayOf(
            PictureItem(
                (600 - 600).toFloat(),
                72.5f,
                "raduga_krasnyj",
                R.raw.krasnyi,
                R.drawable.kraska_krasnaya
            ),
            PictureItem(
                (600 - 600).toFloat(),
                120.5f,
                "raduga_oranzhevyj",
                R.raw.oranzhevyi,
                R.drawable.kraska_oranzhevaya
            ),
            PictureItem(
                (600 - 600).toFloat(),
                165f,
                "raduga_zheltyj",
                R.raw.zheltyi,
                R.drawable.kraska_zheltaya
            ),
            PictureItem(
                (600 - 600).toFloat(),
                210f,
                "raduga_zelenyj",
                R.raw.zelenyi,
                R.drawable.kraska_zelenaya
            ),
            PictureItem(
                (600 - 600).toFloat(),
                256.5f,
                "raduga_goluboj",
                R.raw.goluboi,
                R.drawable.kraska_golubaya
            ),
            PictureItem(
                (600 - 600).toFloat(),
                304f,
                "raduga_sinij",
                R.raw.sinii,
                R.drawable.kraska_sinyaya
            ),
            PictureItem(
                (600 - 600).toFloat(),
                354.5f,
                "raduga_fioletovyj",
                R.raw.fioletovyi,
                R.drawable.kraska_malinovaya
            ),
        )
    ),
    /*new LevelItem("back13_full","rainbow_2","raduga",new PictureItem[]{
                new PictureItem(-308,477, "raduga_part1","krasnyi"),
                new PictureItem(-244,477, "raduga_part2","oranzhevyi"),
                new PictureItem(-186,477, "raduga_part3","zheltyi"),
                new PictureItem(-126,477, "raduga_part4","zelenyi"),
                new PictureItem(-64,477, "raduga_part5","goluboi"),
                new PictureItem(-3,477, "raduga_part6","sinii"),
                new PictureItem(53,477, "raduga_part7","fioletovyi")
        }),*/
    /*new LevelItem(5,"back21_full","applicacyya_cvetok_list","appl_cvetok",new PictureItem[]{

            new ApplicationPictureItem(95-95,531.5f,"applikaciya_cvetok_1",null,425,140),
            new ApplicationPictureItem(95-95,91.5f,"applikaciya_cvetok_3",null,130,133),
            new ApplicationPictureItem(93-95,93,"applikaciya_cvetok_2",null,425,460),
            new ApplicationPictureItem(89.5f-95,313,"applikaciya_cvetok_4",null,130,460),
            new ApplicationPictureItem(121.5f-95,335.5f,"applikaciya_cvetok_5",null,212,424),
            new ApplicationPictureItem(57.5f-95,252,"applikaciya_cvetok_6",null,49,424),
    }),*/
    LevelItem(
        "menu_background", "list_4", R.raw.mama_i_malysh, arrayOf(
            PictureItem(-450f, 160f, "sobaka2", R.raw.sobaka),
            PictureItem(-274f, 200f, "cenok3", R.raw.cenok),
            PictureItem(-100f, 160f, "koshka", R.raw.koshka),
            PictureItem(50f, 200f, "kotenok", R.raw.kotenok),
            PictureItem(250f, 160f, "utka", R.raw.utka2),
            PictureItem(450f, 210f, "utenok", R.raw.utenok),
            PictureItem(-450f, 470f, "kurica2", R.raw.kurica3),
            PictureItem(-260f, 510f, "cyplenok", R.raw.cyplenok),
            PictureItem(90f, 500f, "svinja4", R.raw.svanya),
            PictureItem(410f, 520f, "porosenok", R.raw.porosenok)
        )
    ),
    LevelItem(
        1, "back25_full", 6, "list_4", R.raw.sdelaem_risunok, arrayOf(
            PictureItem(-280f, 470f, "malchik", R.raw.malchik),
            PictureItem(-420f, 470f, "devochka", R.raw.devochka),
            PictureItem(50f, 350f, "dom", R.raw.dom),
            PictureItem(-310f, 100f, "oblako", R.raw.oblako),
            PictureItem(350f, 150f, "solnyshko", R.raw.solnyshko),
            PictureItem(380f, 490f, "zabor", R.raw.zabor),
            PictureItem(-350f, 265f, "bozhja_korovka", R.raw.bozhja_korovka)
        )
    ),
    LevelItem(
        "back31_full", "list_4", R.raw.mebel_v_komnate, arrayOf(
            PictureItem(-370f, 180f, "zerkalo", R.raw.zerkalo),
            PictureItem(-305f, 450f, "divan", R.raw.divan),
            PictureItem(-135f, 180f, "televizor", R.raw.televizor),
            PictureItem(50f, 450f, "krovat", R.raw.krovat),
            PictureItem(135f, 180f, "stol", R.raw.stol),
            PictureItem(380f, 450f, "kreslo", R.raw.kreslo),
            PictureItem(380f, 180f, "shkaf", R.raw.shkaf)
        )
    ),
    LevelItem(
        1, "back6_full", -3, "list_4", R.raw.razlozhi_odezhdu, arrayOf(
            PictureItem(-380f, 150f, "futbolka", R.raw.futbolka),
            PictureItem(-110f, 170f, "kolgoty", R.raw.kolgoty),
            PictureItem(170f, 150f, "kurtka", R.raw.kurtka),
            PictureItem(430f, 150f, "nosok", R.raw.nosok),
            PictureItem(-380f, 470f, "sapog", R.raw.sapog),
            PictureItem(-110f, 480f, "shapka", R.raw.shapka),
            PictureItem(170f, 470f, "sharf", R.raw.sharf),
            PictureItem(410f, 440f, "shtany", R.raw.shtany)
        )
    ),
    LevelItem(
        "back2_full", "list_4", R.raw.profeccii, arrayOf(
            PictureItem(-380f, 160f, "shveya", R.raw.shveya),
            PictureItem(-110f, 160f, "stolyar", R.raw.stolyar),
            PictureItem(160f, 150f, "moryak", R.raw.moryak),
            PictureItem(410f, 160f, "provadec", R.raw.prodavec),
            PictureItem(-380f, 460f, "vrach", R.raw.vrach),
            PictureItem(-170f, 450f, "povar", R.raw.povar),
            PictureItem(20f, 450f, "milicioner", R.raw.milicioner),
            PictureItem(220f, 450f, "pochtalon", R.raw.pochtalon),
            PictureItem(430f, 450f, "doyarka", R.raw.doyarka)
        )
    ),
    LevelItem(
        1, "back18_full", -4, "list_4", R.raw.ryby, arrayOf(
            PictureItem(-400f, 100f, "kambala", R.raw.kambala),
            PictureItem(-280f, 240f, "karas", R.raw.karas),
            PictureItem(-60f, 100f, "karp", R.raw.karp),
            PictureItem(50f, 240f, "ryba_mech", R.raw.ryba_mech),
            PictureItem(300f, 100f, "som", R.raw.som),
            PictureItem(400f, 220f, "okun", R.raw.okun),
            PictureItem(-420f, 420f, "skat", R.raw.skat),
            PictureItem(-210f, 540f, "schuka", R.raw.schuka),
            PictureItem(110f, 420f, "reef_shark", R.raw.reef_shark),
            PictureItem(450f, 460f, "morskoi_konek", R.raw.morskoi_konek)
        )
    ),
    /*new LevelItem(5,"back21_full","applicaciya_korablik_list","vyrezhem_korablik",new PictureItem[]{
                new ApplicationPictureItem(324.5f-267,509.5f,"applicaciya_korablik1",null,100.5f,331,90),
                new ApplicationPictureItem(181-267,217.5f,"applicaciya_korablik2",null,87,49,26),
                new ApplicationPictureItem(531.5f-267,491.5f,"applicaciya_korablik3",null,438.5f,381,90),
                new ApplicationPictureItem(262-267,342,"applicaciya_korablik4",null,289,453.5f,90),
                new ApplicationPictureItem(176.5f-267,213.5f,"applicaciya_korablik5",null,425.5f,558.5f,90),
                new ApplicationPictureItem(189-267,334.5f,"applicaciya_korablik6",null,425.5f,185.5f),
                new ApplicationPictureItem(324-267,334.5f,"applicaciya_korablik6",null,426.5f,65.5f),
                new ApplicationPictureItem(84.5f-267,121.5f,"applicaciya_korablik8",null,267,193.5f),
                new ApplicationPictureItem(138.5f-267,151.5f,"applicaciya_korablik9",null,346,267.5f),
                new ApplicationPictureItem(0-267,79,"applicaciya_korablik10",null,279.5f,60),
        }),*/
    /*new MoveToSidesLevelItem("doska",18,"list_12","pravo_levo_verx_niz",new PictureItem[]{
            new PictureItem(0,0,"picture_18_8",new String[]{"luk","nalevo"},2),
            new PictureItem(0,0,"picture_18_5",new String[]{"perec","napravo"},2),
            new PictureItem(0,0,"picture_18_10",new String[]{"baklazhan","verx"},2),
            new PictureItem(0,0,"picture_18_3",new String[]{"redis","vniz"},2),
    }),*/
    LevelItem(
        1, "back23_full", -4, "list_4", R.raw.morskie_zhivotnye, arrayOf(
            PictureItem(-420f, 160f, "dolphin", R.raw.delphin),
            PictureItem(-190f, 150f, "kit", R.raw.kit),
            PictureItem(-130f, 280f, "kasatka", R.raw.kasatka),
            PictureItem(80f, 140f, "korall", R.raw.korall),
            PictureItem(350f, 130f, "krab", R.raw.crab),
            PictureItem(190f, 320f, "krevetka", R.raw.krevetka),
            PictureItem(-450f, 470f, "meduza", R.raw.meduza),
            PictureItem(-210f, 480f, "morskaya_cherepaha", R.raw.cherepaha),
            PictureItem(90f, 500f, "osminog", R.raw.osminog),
            PictureItem(400f, 500f, "rak", R.raw.rak)
        )
    ),
    LevelItem(
        "back34_full", "list_4", R.raw.ekzot_frukty, arrayOf(
            PictureItem(-420f, 180f, "ananas", R.raw.ananas),
            PictureItem(-340f, 470f, "banany", R.raw.banan),
            PictureItem(110f, 140f, "lime", R.raw.lime),
            PictureItem(45f, 410f, "limon", R.raw.limon),
            PictureItem(365f, 150f, "mango", R.raw.mango),
            PictureItem(365f, 470f, "persik", R.raw.persik),
            PictureItem(-160f, 160f, "vinograd", R.raw.vinograd)
        )
    ),
    LevelItem(
        "back6_full", "list_4", R.raw.posuda, arrayOf(
            PictureItem(-350f, 140f, "chainik", R.raw.chainik),
            PictureItem(0f, 140f, "chashka", R.raw.chashka),
            PictureItem(350f, 140f, "kastrula", R.raw.kastrulja),
            PictureItem(-337f, 450f, "skovorodka", R.raw.skovoroda),
            PictureItem(0f, 450f, "tarelka", R.raw.tarelka),
            PictureItem(350f, 450f, "kuvshin", R.raw.kuvshyn)
        )
    ),
    LevelItem(
        "back5_full", "list_12", R.raw.sladosti, arrayOf(
            PictureItem(-420f, 140f, "ekler", R.raw.ekler),
            PictureItem(-190f, 280f, "kolechko", R.raw.kolechko),
            PictureItem(-30f, 100f, "konfety", R.raw.konfety),
            PictureItem(250f, 130f, "korzinka", R.raw.korzinochka),
            PictureItem(70f, 400f, "ledenec", R.raw.ledenec),
            PictureItem(480f, 140f, "morozhenoe", R.raw.morozhenoe),
            PictureItem(-120f, 520f, "pechenie", R.raw.pechenie),
            PictureItem(-420f, 480f, "shokolad", R.raw.shokolad),
            PictureItem(350f, 430f, "tort", R.raw.tort)
        )
    ),
    LevelItem(
        "back6_full", "list_12", R.raw.soberem_igrushki, arrayOf(
            PictureItem(-420f, 140f, "grabli", R.raw.grabli),
            PictureItem(-244f, 440f, "kukla", R.raw.kukla),
            PictureItem(-260f, 140f, "lopatka", R.raw.lopatka),
            PictureItem(30f, 100f, "mashinka", R.raw.mashinka),
            PictureItem(280f, 160f, "matryoshka", R.raw.matreshka),
            PictureItem(470f, 140f, "mjachik", R.raw.mjachik),
            PictureItem(-420f, 440f, "piramidka2", R.raw.piramidka),
            PictureItem(0f, 300f, "pogremushka", R.raw.pogremushka),
            PictureItem(430f, 450f, "ula", R.raw.ula),
            PictureItem(150f, 470f, "vederko", R.raw.vederko)
        )
    ),
    LevelItem(
        "back6_full", "list_4", R.raw.muz_instrumenty, arrayOf(
            PictureItem(-420f, 140f, "skripka", R.raw.skripka),
            PictureItem(370f, 450f, "baraban", R.raw.baraban),
            PictureItem(100f, 280f, "muz_treugolnik", R.raw.mus_treugolnik),
            PictureItem(45f, 120f, "fleita2", R.raw.fleita),
            PictureItem(365f, 150f, "garmon", R.raw.garmon),
            PictureItem(-350f, 430f, "gitara", R.raw.gitara),
            PictureItem(-100f, 120f, "balalaika", R.raw.balalaika),
            PictureItem(-80f, 500f, "truba", R.raw.truba)
        )
    ),
    LevelItem(
        1, "begemot_background", "list_860x620", R.raw.begemot, arrayOf(
            PictureItem(200f, 89f, "begemot1", null),
            PictureItem(199f, 281f, "begemot2", null),
            PictureItem(-86f, 278f, "begemot3", null),
            PictureItem(-164f, 382f, "begemot4", null),
            PictureItem(-332f, 275f, "begemot5", null),
            PictureItem(-44f, 500f, "begemot6", null),
            PictureItem(61f, 506f, "begemot7", null),
            PictureItem(226f, 432f, "begemot8", null),
            PictureItem(329f, 502f, "begemot9", null)
        )
    ),
    LevelItem(
        "back21_full", "list_4", R.raw.bukvy, arrayOf(
            PictureItem(-450f, 125f, 'А', 3),
            PictureItem(-150f, 125f, 'Б', 3),
            PictureItem(150f, 125f, 'В', 3),
            PictureItem(450f, 125f, 'Г', 3),
            PictureItem(-310f, 307f, 'Д', 3),
            PictureItem(0f, 310f, 'Е', 3),
            PictureItem(300f, 310f, 'Ё', 3),
            PictureItem(-450f, 517f, 'Ж', 3),
            PictureItem(-150f, 517f, 'З', 3),
            PictureItem(150f, 495f, 'И', 3),
            PictureItem(450f, 495f, 'Й', 3)
        )
    ),
    LevelItem(
        1, "back20_full", 18, "list_4", R.raw.bukvy, arrayOf(
            PictureItem(-450f, 110f, 'К', 3),
            PictureItem(-150f, 110f, 'Л', 3),
            PictureItem(150f, 110f, 'М', 3),
            PictureItem(440f, 110f, 'Н', 3),
            PictureItem(-300f, 310f, 'О', 3),
            PictureItem(-14f, 310f, 'П', 3),
            PictureItem(300f, 310f, 'Р', 3),
            PictureItem(-450f, 512f, 'С', 3),
            PictureItem(-150f, 512f, 'Т', 3),
            PictureItem(150f, 505f, 'У', 3),
            PictureItem(425f, 505f, 'Ф', 3)
        )
    ),
    LevelItem(
        1, "back17_full", 5, "list_4", R.raw.bukvy, arrayOf(
            PictureItem(-450f, 110f, 'Х', 3),
            PictureItem(-150f, 110f, 'Ц', 3),
            PictureItem(150f, 110f, 'Ч', 3),
            PictureItem(430f, 110f, 'Ш', 3),
            PictureItem(-300f, 310f, 'Щ', 3),
            PictureItem(0f, 310f, 'Ъ', 3),
            PictureItem(300f, 310f, 'Ы', 3),
            PictureItem(-450f, 512f, 'Ь', 3),
            PictureItem(-150f, 512f, 'Э', 3),
            PictureItem(150f, 512f, 'Ю', 3),
            PictureItem(450f, 512f, 'Я', 3)
        )
    ),
    LevelItem(
        "back6_full", "list_4", R.raw.mishki, arrayOf(
            PictureItem(-450f, 120f, "mishka1", null),
            PictureItem(-150f, 110f, "mishka2", null),
            PictureItem(170f, 100f, "mishka3", null),
            PictureItem(420f, 165f, "mishka4", null),
            PictureItem(-260f, 310f, "mishka5", null),
            PictureItem(-50f, 330f, "mishka6", null),
            PictureItem(230f, 290f, "mishka11", null),
            PictureItem(-450f, 460f, "mishka7", null),
            PictureItem(-150f, 530f, "mishka9", null),
            PictureItem(150f, 505f, "mishka8", null),
            PictureItem(390f, 460f, "mishka10", null)
        )
    ),
    LevelItem(
        "back34_full", "list_4", R.raw.pirozhnye, arrayOf(
            PictureItem(-290f, 160f, "pirozhnoe1", null),
            PictureItem(-290f, 470f, "pirozhnoe2", null),
            PictureItem(30f, 160f, "pirozhnoe3", null),
            PictureItem(30f, 470f, "pirozhnoe4", null),
            PictureItem(350f, 160f, "pirozhnoe5", null),
            PictureItem(350f, 470f, "pirozhnoe6", null)
        )
    ),
    WordLevelItem("back20_full", 0, "list_12", R.raw.babyshka, "БАБУШКА"),
    LevelItem(
        1, "back4_full", -3, "list_4", R.raw.etiketki, arrayOf(
            PictureItem(-30f, 350f, "price1", null),
            PictureItem(330f, 310f, "price3", null),
            PictureItem(-150f, 150f, "price2", null),
            PictureItem(210f, 180f, "price9", null),
            PictureItem(-260f, 310f, "price5", null),
            PictureItem(460f, 310f, "price6", null),
            PictureItem(-400f, 310f, "price7", null),
            PictureItem(90f, 310f, "price8", null),
            PictureItem(-150f, 450f, "price4", null),
            PictureItem(210f, 490f, "price10", null)
        )
    ),
    LevelItem(
        1, "back8_full", -3, "list_4", R.raw.instrumenty, arrayOf(
            PictureItem(40f, 260f, "drel", R.raw.drel),
            PictureItem(-360f, 230f, "molotok", R.raw.molotok),
            PictureItem(290f, 220f, "napilnik", R.raw.napilnik),
            PictureItem(420f, 210f, "pasanizhy", R.raw.pasanizhy),
            PictureItem(-220f, 290f, "nozhnicy", R.raw.nozhnicy),
            PictureItem(-50f, 240f, "ruletka", R.raw.ruletka),
            PictureItem(-460f, 270f, "razvodnoi_kluch", R.raw.razvodnoi_kluch),
            PictureItem(420f, 410f, "otvertka", R.raw.otvertka),
            PictureItem(-220f, 540f, "shtangelcirkul", R.raw.shtangelcirkul),
            PictureItem(180f, 550f, "pila", R.raw.pila)
        )
    ),
    LevelItem(
        1, "back35_full", -2, "list_4", R.raw.naidi_snezhinki, arrayOf(
            PictureItem(-410f, 218f, "snezhinka8", null),
            PictureItem(-120f, 160f, "snezhinka1", null),
            PictureItem(170f, 120f, "snezhinka5", null),
            PictureItem(430f, 115f, "snezhinka6", null),
            PictureItem(70f, 315f, "snezhinka9", null),
            PictureItem(400f, 360f, "snezhinka4", null),
            PictureItem(-380f, 500f, "snezhinka3", null),
            PictureItem(-130f, 464f, "snezhinka7", null),
            PictureItem(160f, 500f, "snezhinka2", null)
        )
    ),
    LevelItem(
        1, "back34_full", -1, "prihozhaya_list", R.raw.prihozhaya, arrayOf(
            PictureItem(134.5f - 418, 62.5f, "prihozhaya1", null),
            PictureItem(316.5f - 418, 62f, "prihozhaya2", null),
            PictureItem(682.5f - 418, 311.5f, "prihozhaya3", null),
            PictureItem(490.5f - 418, 268.5f, "prihozhaya4", null),
            PictureItem(533f - 418, 416.5f, "prihozhaya5", null),
            PictureItem(140f - 418, 401.5f, "prihozhaya6", null),
            PictureItem(141f - 418, 266f, "prihozhaya7", null),
            PictureItem(372.5f - 418, 418.5f, "prihozhaya8", null),
            PictureItem(304.5f - 418, 295f, "prihozhaya9", null),
            PictureItem(686f - 418, 70.5f, "prihozhaya10", null),
            PictureItem(498.5f - 418, 68f, "prihozhaya11", null),
        )
    ),

    LevelItem(
        1, "smeshariki", 10, "list_4", R.raw.smeshariki, arrayOf(
            PictureItem(-400f, 176f, "krosh", R.raw.krosh),
            PictureItem(-200f, 214f, "karkarych", R.raw.karkarych),
            PictureItem(0f, 283f, "pin", R.raw.pin),
            PictureItem(200f, 173f, "njusha", R.raw.nusha),
            PictureItem(200f, 352f, "bibi", R.raw.bibi),
            PictureItem(400f, 252f, "losjash", R.raw.losjash),
            PictureItem(-390f, 455f, "ezhik", R.raw.ezhik),
            PictureItem(-150f, 490f, "sovunia", R.raw.sovunia),
            PictureItem(100f, 500f, "kopatych", R.raw.kopatych),
            PictureItem(400f, 500f, "barash", R.raw.barash)

        )
    ),
    WordLevelItem("back26_full", 0, "list_12", R.raw.dedyshka, "ДЕДУШКА"),
    LevelItem(
        1, "olympic_rings", 18, "list_4", R.raw.kakim_sportom_zanimautsa, arrayOf(
            PictureItem(-365f, 150f, "football", R.raw.fooball),
            PictureItem(-220f, 460f, "basketball", R.raw.basketball),
            PictureItem(-60f, 150f, "box", R.raw.box),
            PictureItem(100f, 490f, "plavanie", R.raw.plavanje),
            PictureItem(265f, 150f, "hockey", R.raw.hockey),
            PictureItem(400f, 460f, "lyzhi", R.raw.lyzhi)
        )
    ),
    LevelItem(
        1, "back15_full", -2, "list_2", R.raw.ukras_elochku, arrayOf(
            PictureItem(100f, 300f, "picture_2_1", null),
            PictureItem(-100f, 240f, "picture_2_2", null),
            PictureItem(-20f, 400f, "picture_2_3", null),
            PictureItem(10f, 145f, "picture_2_4", null),
            PictureItem(120f, 470f, "picture_2_5", null),
            PictureItem(-150f, 490f, "picture_2_6", null),
            PictureItem(300f, 475f, "picture_2_7", null)
        )
    ),
    LevelItem(
        1, "zima_luna", 3, "list_13", R.raw.slozhi_snegovika, arrayOf(
            PictureItem(-37f, 82f, "picture_3_1", null),
            PictureItem(-23f, 180f, "picture_3_2", null),
            PictureItem(-19f, 362f, "picture_3_3", null),
            PictureItem(197f, 254f, "picture_3_4", null),
            PictureItem(-209f, 326f, "picture_3_5", null),
            PictureItem(-1f, 350f, "picture_3_6", null),
            PictureItem(-1f, 470f, "picture_3_7", null),
            PictureItem(-157f, 562f, "picture_3_8", null),
            PictureItem(123f, 554f, "picture_3_9", null)
        )
    ),
    LevelItem(
        1, "fixiki_background", 19, "children_list", R.raw.fixiki_kak_zovyt, arrayOf(
            PictureItem(-375f, 162f, "fixiki_igrek", R.raw.fixiki_igrek),
            PictureItem(-230f, 168f, "fixiki_fire", R.raw.fixiki_fier),
            PictureItem(3f, 230f, "fixiki_nolik", R.raw.fixiki_nolik),
            PictureItem(170f, 185f, "fixiki_simka", R.raw.fixiki_simka),
            PictureItem(380f, 170f, "fixiki_shpulya", R.raw.fixiki_shpulya),
            PictureItem(-315f, 467f, "fixiki_papus", R.raw.fixiki_papus),
            PictureItem(-90f, 460f, "fixiki_masya", R.raw.fixiki_masya),
            PictureItem(102f, 456f, "fixiki_verta", R.raw.fixiki_verta),
            PictureItem(340f, 462f, "fixiki_dedus", R.raw.fixiki_dedus)

        )
    ),
    LevelItem(
        1,
        "barboskiny_background",
        7,
        "children_list",
        R.raw.nazovi_barboskinyh,
        arrayOf(
            PictureItem(-425f, 381f, "barboskiny1", R.raw.dryzgok),
            PictureItem(-267f, 370f, "barboskiny5", R.raw.dedushka),
            PictureItem(-157f, 420f, "barboskiny3", R.raw.malish),
            PictureItem(-18f, 400f, "barboskiny_liza", R.raw.liza),
            PictureItem(78f, 388f, "barboskiny_papa", R.raw.papa_2),
            PictureItem(192f, 386f, "barboskiny_mama", R.raw.mama_2),
            PictureItem(317f, 388f, "barboskiny4", R.raw.roza),
            PictureItem(450f, 404f, "barboskiny2", R.raw.gena)
        )
    ),
    /*new LevelItem("domik_background","domik_list","soberi_domik",new PictureItem[]{
                new PictureItem(-173, 439, "domik1",null),
                new PictureItem(-157,431, "domik2",null),
                new PictureItem(31,427, "domik3",null),
                new PictureItem(32,444, "domik4",null),
                new PictureItem(-188,236, "domik5",null),
                new PictureItem(-97,96, "domik6",null),
                new PictureItem(42,225, "domik7",null)
        }),*/
    LevelItem(
        1, "korablik_background", 17, "list_4", R.raw.korablik, arrayOf(
            PictureItem(117f, 517f, "korablik1", R.raw.korma),
            PictureItem(97f, 293f, "korablik2", R.raw.machta),
            PictureItem(241f, 502f, "korablik3", R.raw.spasatelnyi_kryg),
            PictureItem(220f, 283f, "korablik4", R.raw.parus),
            PictureItem(-21f, 320f, "korablik5", R.raw.parus),
            PictureItem(45f, 59f, "korablik6", R.raw.flag)
        )
    ),
    LevelItem(
        "children_background", "children_list", R.raw.otkuda_eti_deti, arrayOf(
            PictureItem(-338f, 140f, "child_australia", R.raw.child_australia),
            PictureItem(-113f, 140f, "child_canada", R.raw.child_canada),
            PictureItem(108f, 140f, "child_china", R.raw.child_china),
            PictureItem(338f, 140f, "child_france", R.raw.child_france),
            PictureItem(-450f, 310f, "child_greenland", R.raw.child_greenland),
            PictureItem(-225f, 310f, "child_hawaii", R.raw.child_hawaii),
            PictureItem(-10f, 310f, "child_mexico", R.raw.child_mexico),
            PictureItem(225f, 310f, "child_japan", R.raw.child_japan),
            PictureItem(450f, 310f, "child_kenia", R.raw.child_keniya),
            PictureItem(-338f, 480f, "child_indie", R.raw.child_india),
            PictureItem(-113f, 480f, "child_peru", R.raw.child_peru),
            PictureItem(113f, 480f, "child_russia", R.raw.child_russia),
            PictureItem(338f, 480f, "child_usa", R.raw.child_usa)
        )
    ),

    LevelItem(
        1, "bottles_background", 0, "bottles_list", R.raw.bytilka, arrayOf(
            PictureItem(-425f, 443f, "bottle1", null),
            PictureItem(-320f, 246f, "bottle2", null),
            PictureItem(-250f, 489f, "bottle3", null),
            PictureItem(-130f, 323f, "bottle4", null),
            PictureItem(-13f, 478f, "bottle5", null),
            PictureItem(62f, 280f, "bottle6", null),
            PictureItem(156f, 474f, "bottle7", null),
            PictureItem(256f, 242f, "bottle8", null),
            PictureItem(354f, 496f, "bottle9", null),
            PictureItem(434f, 288f, "bottle10", null)
        )
    ),
    LevelItem(
        1, "luntik_background", 0, "list_4", R.raw.vspomni_geroe_vmyltfilma, arrayOf(
            PictureItem(-425f, 140f, "pchelenok", R.raw.pchelenok),
            PictureItem(-250f, 180f, "general_sher", R.raw.general_sher),
            PictureItem(-50f, 140f, "mila", R.raw.mila),
            PictureItem(180f, 160f, "kapa", R.raw.kapa),
            PictureItem(415f, 170f, "pauk_shnuk", R.raw.pauk_shnuk),
            PictureItem(-380f, 420f, "tetya_motya", R.raw.tetya_motya),
            PictureItem(-230f, 510f, "kornei_ivanovich", R.raw.kornei_ivanovich),
            PictureItem(-100f, 400f, "kuzya", R.raw.kuzya),
            PictureItem(70f, 460f, "luntik", R.raw.luntik),
            PictureItem(240f, 450f, "piyavka", R.raw.piyavka),
            PictureItem(390f, 460f, "gusenicy", R.raw.gusenicy)
        )
    ),
    /*new LevelItem(5,"back21_full","applicaciya_bozhiya_korovka_list","appl_bogyakorovka",new PictureItem[]{
            new ApplicationPictureItem(-40,295,"applicaciya_bozhiya_korovka1",null,195f,138,107),
            new ApplicationPictureItem(-103,336,"applicaciya_bozhiya_korovka3",null,103,438,4),
            new ApplicationPictureItem(104,334,"applicaciya_bozhiya_korovka2",null,290,436,-5),
            new ApplicationPictureItem(-60,259,"applicaciya_bozhiya_korovka4",null,244,227),
            new ApplicationPictureItem(-122,332,"applicaciya_bozhiya_korovka4",null,404,226),
            new ApplicationPictureItem(60,259,"applicaciya_bozhiya_korovka4",null,324,173),
            new ApplicationPictureItem(122,332,"applicaciya_bozhiya_korovka4",null,405,112),
            new ApplicationPictureItem(-74,424,"applicaciya_bozhiya_korovka4",null,476,173),
            new ApplicationPictureItem(74,424,"applicaciya_bozhiya_korovka4",null,476,43),
            new ApplicationPictureItem(-44,162,"applicaciya_bozhiya_korovka5",null,455,372),
            new ApplicationPictureItem(44,162,"applicaciya_bozhiya_korovka5",null,455,522),
    }),*/
    LevelItem(
        "back2_full", "platie0", R.raw.platiya, arrayOf(
            PictureItem(-398f, 185f, "platie12", null),
            PictureItem(-227f, 150f, "platie9", null),
            PictureItem(-50f, 187f, "platie11", null),
            PictureItem(258f, 192f, "platie10", null),
            PictureItem(440f, 188f, "platie6", null),
            PictureItem(117f, 328f, "platie8", null),
            PictureItem(-425f, 490f, "platie7", null),
            PictureItem(-222f, 478f, "platie3", null),
            PictureItem(-56f, 478f, "platie5", null),
            PictureItem(264f, 494f, "platie4", null),
            PictureItem(440f, 490f, "platie2", null)
        )
    ),
    LevelItem(
        1, "savana", "list_600x620", R.raw.lev, arrayOf(
            PictureItem(-38f, 99f, "lev1", null),
            PictureItem(-40f, 269f, "lev2", null),
            PictureItem(-204f, 276f, "lev3", null),
            PictureItem(121f, 291f, "lev4", null),
            PictureItem(-60f, 426f, "lev5", null),
            PictureItem(-184f, 521f, "lev6", null),
            PictureItem(-61f, 516f, "lev7", null),
            PictureItem(62f, 539f, "lev8", null),
            PictureItem(162f, 431f, "lev9", null),
            PictureItem(256f, 319f, "lev10", null)
        )
    ),
    LevelItem(
        1, "ocean_background", -4, "ocean_list", R.raw.rasstav_korabliki, arrayOf(
            PictureItem(115f, 400f, "boat1", null),
            PictureItem(-368f, 130f, "boat2", null),
            PictureItem(-85f, 244f, "boat3", null),
            PictureItem(373f, 472f, "boat4", null),
            PictureItem(164f, 141f, "boat5", null),
            PictureItem(-392f, 377f, "boat6", null),
            PictureItem(-175f, 501f, "boat7", null),
            PictureItem(383f, 265f, "boat8", null)
        )
    ),
    LevelItem(
        "trava_background", "list_4", R.raw.nazvania_ptihc, arrayOf(
            PictureItem(-326f, 441f, "pticy1", R.raw.pavlin),
            PictureItem(444f, 473f, "pticy2", R.raw.popygai),
            PictureItem(238f, 471f, "pticy3", R.raw.ydot),
            PictureItem(367f, 178f, "pticy4", R.raw.lebed),
            PictureItem(22f, 493f, "pticy5", R.raw.sinichka),
            PictureItem(-168f, 159f, "pticy6", R.raw.indyk),
            PictureItem(119f, 203f, "pticy7", R.raw.starays),
            PictureItem(-433f, 134f, "pticy8", R.raw.petyx)
        )
    ),
    LevelItem(
        3,
        "back21_full",
        -3,
        "karandashi_list",
        R.raw.kakoi_cvet_y_karandasha,
        arrayOf(
            PictureItem(
                (104 - 600).toFloat(),
                152f,
                "karandash_goluboi",
                R.raw.goluboi,
                R.drawable.kraska_golubaya
            ),
            PictureItem(
                (100 - 600).toFloat(),
                354f,
                "karandash_seryj",
                R.raw.seryi,
                R.drawable.kraska_seraya
            ),
            PictureItem(
                (303 - 600).toFloat(),
                152f,
                "karandash_krasnyj",
                R.raw.krasnyi,
                R.drawable.kraska_krasnaya
            ),
            PictureItem(
                (293 - 600).toFloat(),
                354f,
                "karandash_chernyj",
                R.raw.chernyi,
                R.drawable.kraska_chernaya
            ),
            PictureItem(
                (503 - 600).toFloat(),
                152f,
                "karandash_oranzhevyj",
                R.raw.oranzhevyi,
                R.drawable.kraska_oranzhevaya
            ),
            PictureItem(
                (491 - 600).toFloat(),
                354f,
                "karandash_belyj",
                R.raw.belyi,
                R.drawable.kraska_belaya
            ),
            PictureItem(
                (702 - 600).toFloat(),
                152f,
                "karandash_korichnevyj",
                R.raw.korichnevyi,
                R.drawable.kraska_korichnevaya
            ),
            PictureItem(
                (690 - 600).toFloat(),
                354f,
                "karandash_malinovyj",
                R.raw.malinovyi,
                R.drawable.kraska_malinovaya
            ),
            PictureItem(
                (901 - 600).toFloat(),
                152f,
                "karandash_zelenyj",
                R.raw.zelenyi,
                R.drawable.kraska_zelenaya
            ),
            PictureItem(
                (890 - 600).toFloat(),
                354f,
                "karandash_sinij",
                R.raw.sinii,
                R.drawable.kraska_sinyaya
            ),
            PictureItem(
                (1099 - 600).toFloat(),
                152f,
                "karandash_salatovyj",
                R.raw.salatoavyi,
                R.drawable.kraska_salatovaya
            ),
            PictureItem(
                (1088 - 600).toFloat(),
                354f,
                "karandash_zheltyj",
                R.raw.zheltyi,
                R.drawable.kraska_zheltaya
            ),
        )
    ),

    )
