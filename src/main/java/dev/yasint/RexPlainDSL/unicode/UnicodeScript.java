package dev.yasint.RexPlainDSL.unicode;

/**
 * Adds typings for supported unicode scripts in RE2. Java does
 * have these typings in {@link Character.UnicodeScript}
 * but we need to be precise and only expose what's available.
 */
public enum UnicodeScript {

    // General Categories supported by RE2

    OTHER("C"),
    CONTROL("Cc"),
    FORMAT("Cf"),
    PRIVATE_USE("Co"),
    SURROGATE("Cs"),
    LETTER("L"),
    LOWERCASE_LETTER("Ll"),
    MODIFIER_LETTER("Lm"),
    OTHER_LETTER("Lo"),
    TITLECASE_LETTER("Lt"),
    UPPERCASE_LETTER("Lu"),
    MARK("M"),
    SPACING_MARK("Mc"),
    ENCLOSING_MARK("Me"),
    NON_SPACING_MARK("Mn"),
    NUMBER("N"),
    DECIMAL_NUMBER("Nd"),
    LETTER_NUMBER("Nl"),
    OTHER_NUMBER("No"),
    PUNCTUATION("P"),
    CONNECTOR_PUNCTUATION("Pc"),
    DASH_PUNCTUATION("Pd"),
    CLOSE_PUNCTUATION("Pe"),
    FINAL_PUNCTUATION("Pf"),
    INITIAL_PUNCTUATION("Pi"),
    OTHER_PUNCTUATION("Po"),
    OPEN_PUNCTUATION("Ps"),
    SYMBOL("S"),
    CURRENCY_SYMBOL("Sc"),
    MODIFIER_SYMBOL("Sk"),
    MATH_SYMBOL("Sm"),
    OTHER_SYMBOL("So"),
    SEPARATOR("Z"),
    LINE_SEPARATOR("Zl"),
    PARAGRAPH_SEPARATOR("Zp"),
    SPACE_SEPARATOR("Zs"),

    // Scripts supported by RE2

    ADLAM("Adlam"),
    AHOM("Ahom"),
    ANATOLIAN_HIEROGLYPHS("Anatolian_Hieroglyphs"),
    ARABIC("Arabic"),
    ARMENIAN("Armenian"),
    AVESTAN("Avestan"),
    BALINESE("Balinese"),
    BAMUM("Bamum"),
    BASSA_VAH("Bassa_Vah"),
    BATAK("Batak"),
    BENGALI("Bengali"),
    BHAIKSUKI("Bhaiksuki"),
    BOPOMOFO("Bopomofo"),
    BRAHMI("Brahmi"),
    BRAILLE("Braille"),
    BUGINESE("Buginese"),
    BUHID("Buhid"),
    CANADIAN_ABORIGINAL("Canadian_Aboriginal"),
    CARIAN("Carian"),
    CAUCASIAN_ALBANIAN("Caucasian_Albanian"),
    CHAKMA("Chakma"),
    CHAM("Cham"),
    CHEROKEE("Cherokee"),
    CHORASMIAN("Chorasmian"),
    COMMON("Common"),
    COPTIC("Coptic"),
    CUNEIFORM("Cuneiform"),
    CYPRIOT("Cypriot"),
    CYRILLIC("Cyrillic"),
    DESERET("Deseret"),
    DEVANAGARI("Devanagari"),
    DIVES_AKURU("Dives_Akuru"),
    DOGRA("Dogra"),
    DUPLOYAN("Duployan"),
    EGYPTIAN_HIEROGLYPHS("Egyptian_Hieroglyphs"),
    ELBASAN("Elbasan"),
    ELYMAIC("Elymaic"),
    ETHIOPIC("Ethiopic"),
    GEORGIAN("Georgian"),
    GLAGOLITIC("Glagolitic"),
    GOTHIC("Gothic"),
    GRANTHA("Grantha"),
    GREEK("Greek"),
    GUJARATI("Gujarati"),
    GUNJALA_GONDI("Gunjala_Gondi"),
    GURMUKHI("Gurmukhi"),
    HAN("Han"),
    HANGUL("Hangul"),
    HANIFI_ROHINGYA("Hanifi_Rohingya"),
    HANUNOO("Hanunoo"),
    HATRAN("Hatran"),
    HEBREW("Hebrew"),
    HIRAGANA("Hiragana"),
    IMPERIAL_ARAMAIC("Imperial_Aramaic"),
    INHERITED("Inherited"),
    INSCRIPTIONAL_PAHLAVI("Inscriptional_Pahlavi"),
    INSCRIPTIONAL_PARTHIAN("Inscriptional_Parthian"),
    JAVANESE("Javanese"),
    KAITHI("Kaithi"),
    KANNADA("Kannada"),
    KATAKANA("Katakana"),
    KAYAH_LI("Kayah_Li"),
    KHAROSHTHI("Kharoshthi"),
    KHITAN_SMALL_SCRIPT("Khitan_Small_Script"),
    KHMER("Khmer"),
    KHOJKI("Khojki"),
    KHUDAWADI("Khudawadi"),
    LAO("Lao"),
    LATIN("Latin"),
    LEPCHA("Lepcha"),
    LIMBU("Limbu"),
    LINEAR_A("Linear_A"),
    LINEAR_B("Linear_B"),
    LISU("Lisu"),
    LYCIAN("Lycian"),
    LYDIAN("Lydian"),
    MAHAJANI("Mahajani"),
    MAKASAR("Makasar"),
    MALAYALAM("Malayalam"),
    MANDAIC("Mandaic"),
    MANICHAEAN("Manichaean"),
    MARCHEN("Marchen"),
    MASARAM_GONDI("Masaram_Gondi"),
    MEDEFAIDRIN("Medefaidrin"),
    MEETEI_MAYEK("Meetei_Mayek"),
    MENDE_KIKAKUI("Mende_Kikakui"),
    MEROITIC_CURSIVE("Meroitic_Cursive"),
    MEROITIC_HIEROGLYPHS("Meroitic_Hieroglyphs"),
    MIAO("Miao"),
    MODI("Modi"),
    MONGOLIAN("Mongolian"),
    MRO("Mro"),
    MULTANI("Multani"),
    MYANMAR("Myanmar"),
    NABATAEAN("Nabataean"),
    NANDINAGARI("Nandinagari"),
    NEW_TAI_LUE("New_Tai_Lue"),
    NEWA("Newa"),
    NKO("Nko"),
    NUSHU("Nushu"),
    NYIAKENG_PUACHUE_HMONG("Nyiakeng_Puachue_Hmong"),
    OGHAM("Ogham"),
    OL_CHIKI("Ol_Chiki"),
    OLD_HUNGARIAN("Old_Hungarian"),
    OLD_ITALIC("Old_Italic"),
    OLD_NORTH_ARABIAN("Old_North_Arabian"),
    OLD_PERMIC("Old_Permic"),
    OLD_PERSIAN("Old_Persian"),
    OLD_SOGDIAN("Old_Sogdian"),
    OLD_SOUTH_ARABIAN("Old_South_Arabian"),
    OLD_TURKIC("Old_Turkic"),
    ORIYA("Oriya"),
    OSAGE("Osage"),
    OSMANYA("Osmanya"),
    PAHAWH_HMONG("Pahawh_Hmong"),
    PALMYRENE("Palmyrene"),
    PAU_CIN_HAU("Pau_Cin_Hau"),
    PHAGS_PA("Phags_Pa"),
    PHOENICIAN("Phoenician"),
    PSALTER_PAHLAVI("Psalter_Pahlavi"),
    REJANG("Rejang"),
    RUNIC("Runic"),
    SAMARITAN("Samaritan"),
    SAURASHTRA("Saurashtra"),
    SHARADA("Sharada"),
    SHAVIAN("Shavian"),
    SIDDHAM("Siddham"),
    SIGNWRITING("SignWriting"),
    SINHALA("Sinhala"),
    SOGDIAN("Sogdian"),
    SORA_SOMPENG("Sora_Sompeng"),
    SOYOMBO("Soyombo"),
    SUNDANESE("Sundanese"),
    SYLOTI_NAGRI("Syloti_Nagri"),
    SYRIAC("Syriac"),
    TAGALOG("Tagalog"),
    TAGBANWA("Tagbanwa"),
    TAI_LE("Tai_Le"),
    TAI_THAM("Tai_Tham"),
    TAI_VIET("Tai_Viet"),
    TAKRI("Takri"),
    TAMIL("Tamil"),
    TANGUT("Tangut"),
    TELUGU("Telugu"),
    THAANA("Thaana"),
    THAI("Thai"),
    TIBETAN("Tibetan"),
    TIFINAGH("Tifinagh"),
    TIRHUTA("Tirhuta"),
    UGARITIC("Ugaritic"),
    VAI("Vai"),
    WANCHO("Wancho"),
    WARANG_CITI("Warang_Citi"),
    YEZIDI("Yezidi"),
    YI("Yi"),
    ZANABAZAR_SQUARE("Zanabazar_Square");

    private String block;

    UnicodeScript(String block) {
        this.block = block;
    }

    public String getBlock() {
        return block;
    }

}