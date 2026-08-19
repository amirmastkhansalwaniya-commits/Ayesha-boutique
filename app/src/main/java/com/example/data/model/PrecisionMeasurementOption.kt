package com.example.data.model

enum class PrecisionCategory(val displayName: String, val hindiName: String, val iconEmoji: String) {
    ALL("All Options", "सभी विकल्प", "✨"),
    UPPER_BODY("Upper / Kurtis & Blouse", "कुर्ती, ब्लाउज व टॉप", "👚"),
    SLEEVE_COLLAR("Sleeves & Neck", "बाजू, गला व कॉलर", "🪡"),
    LOWER_BODY("Bottoms & Trousers", "पैंट, सलवार व बॉटम", "👖"),
    LEHENGA_GOWN("Lehenga & Gowns", "लहंगा, घेरा व गाउन", "👗"),
    MENS_COAT("Suits & Sherwanis", "कोट, शेरवानी व जेंट्स", "🤵"),
    CUSTOM("Custom Tailored", "कस्टम नाप", "📏")
}

data class PrecisionMeasurementOption(
    val id: String,
    val name: String,
    val hindiName: String,
    val category: PrecisionCategory,
    val description: String = "",
    val defaultUnit: String = "in",
    val isSystemPreset: Boolean = true
) {
    val displayLabel: String
        get() = if (hindiName.isNotBlank()) "$name ($hindiName)" else name
}

object PrecisionMeasurementCatalog {

    val defaultOptions: List<PrecisionMeasurementOption> = listOf(
        // Upper Body & Kurtis
        PrecisionMeasurementOption(
            id = "front_neck",
            name = "Front Neck Depth",
            hindiName = "आगे का गला गहराई",
            category = PrecisionCategory.UPPER_BODY,
            description = "From shoulder to front neckline depth"
        ),
        PrecisionMeasurementOption(
            id = "back_neck",
            name = "Back Neck Depth",
            hindiName = "पीछे का गला गहराई",
            category = PrecisionCategory.UPPER_BODY,
            description = "From shoulder to back neck depth"
        ),
        PrecisionMeasurementOption(
            id = "neck_width",
            name = "Neck Width / Spread",
            hindiName = "गला चौड़ाई / फैलाव",
            category = PrecisionCategory.UPPER_BODY,
            description = "Horizontal spread distance of neckline"
        ),
        PrecisionMeasurementOption(
            id = "kameez_length",
            name = "Kameez / Kurti Length",
            hindiName = "कुर्ती / कमीज लंबाई",
            category = PrecisionCategory.UPPER_BODY,
            description = "Shoulder to bottom hem of shirt or kurti"
        ),
        PrecisionMeasurementOption(
            id = "choli_length",
            name = "Choli / Blouse Length",
            hindiName = "ब्लाउज / चोली लंबाई",
            category = PrecisionCategory.UPPER_BODY,
            description = "Shoulder to waist bottom hem of blouse"
        ),
        PrecisionMeasurementOption(
            id = "upper_chest",
            name = "Upper Chest / High Bust",
            hindiName = "अपर चेस्ट / हाई बस्ट",
            category = PrecisionCategory.UPPER_BODY,
            description = "Chest circumference measured directly under armpits"
        ),
        PrecisionMeasurementOption(
            id = "underbust",
            name = "Underbust Ribcage",
            hindiName = "अंडरबस्ट / रिब",
            category = PrecisionCategory.UPPER_BODY,
            description = "Directly under the bust line"
        ),
        PrecisionMeasurementOption(
            id = "bust_point",
            name = "Bust Point / Apex Depth",
            hindiName = "बस्ट पॉइंट / डार्ट गहराई",
            category = PrecisionCategory.UPPER_BODY,
            description = "Shoulder point down to bust apex"
        ),
        PrecisionMeasurementOption(
            id = "apex_distance",
            name = "Apex to Apex Distance",
            hindiName = "डार्ट से डार्ट दूरी",
            category = PrecisionCategory.UPPER_BODY,
            description = "Distance between left and right bust points"
        ),
        PrecisionMeasurementOption(
            id = "cross_back",
            name = "Cross Back / Teera",
            hindiName = "तीरा / क्रॉस बैक",
            category = PrecisionCategory.UPPER_BODY,
            description = "Across back shoulder blades"
        ),
        PrecisionMeasurementOption(
            id = "cross_front",
            name = "Cross Front",
            hindiName = "आगे का तीरा",
            category = PrecisionCategory.UPPER_BODY,
            description = "Across front chest above bust"
        ),
        PrecisionMeasurementOption(
            id = "shoulder_to_waist",
            name = "Shoulder to Waist",
            hindiName = "कंधे से कमर लंबाई",
            category = PrecisionCategory.UPPER_BODY,
            description = "Vertical length from shoulder to natural waistline"
        ),
        PrecisionMeasurementOption(
            id = "side_slit",
            name = "Side Slit / Chaak Length",
            hindiName = "चाक / साइड कट",
            category = PrecisionCategory.UPPER_BODY,
            description = "Opening height from armpit/waist down to chaak slit"
        ),
        PrecisionMeasurementOption(
            id = "daman_flare",
            name = "Daman / Hem Width",
            hindiName = "दामन / नीचे का घेर",
            category = PrecisionCategory.UPPER_BODY,
            description = "Straight width of lower shirt or kurti daman"
        ),
        PrecisionMeasurementOption(
            id = "front_dart",
            name = "Front Dart Width",
            hindiName = "आगे की डार्ट",
            category = PrecisionCategory.UPPER_BODY,
            description = "Width and intake of front vertical shaping darts"
        ),
        PrecisionMeasurementOption(
            id = "back_dart",
            name = "Back Dart Width",
            hindiName = "पीछे की डार्ट",
            category = PrecisionCategory.UPPER_BODY,
            description = "Width and intake of back waist shaping darts"
        ),
        PrecisionMeasurementOption(
            id = "shoulder_slope",
            name = "Shoulder Slope",
            hindiName = "कंधा ढलान",
            category = PrecisionCategory.UPPER_BODY,
            description = "Drop angle from neck collar point to outer shoulder tip"
        ),

        // Sleeves & Necklines
        PrecisionMeasurementOption(
            id = "bicep",
            name = "Bicep / Muscle Round",
            hindiName = "डोला / बाइसेप गोलाई",
            category = PrecisionCategory.SLEEVE_COLLAR,
            description = "Upper arm circumference at fullest point"
        ),
        PrecisionMeasurementOption(
            id = "elbow",
            name = "Elbow Round",
            hindiName = "कोहनी गोलाई",
            category = PrecisionCategory.SLEEVE_COLLAR,
            description = "Circumference at the elbow"
        ),
        PrecisionMeasurementOption(
            id = "wrist_cuff",
            name = "Wrist / Sleeve Cuff",
            hindiName = "कलाई / कफ गोलाई",
            category = PrecisionCategory.SLEEVE_COLLAR,
            description = "Sleeve bottom opening round"
        ),
        PrecisionMeasurementOption(
            id = "armhole_depth",
            name = "Armhole Depth",
            hindiName = "मुड्ढा गहराई",
            category = PrecisionCategory.SLEEVE_COLLAR,
            description = "Straight vertical depth of armhole scye"
        ),
        PrecisionMeasurementOption(
            id = "armhole_round",
            name = "Armhole Circumference",
            hindiName = "मुड्ढा गोलाई",
            category = PrecisionCategory.SLEEVE_COLLAR,
            description = "Total circular curve measurement around shoulder scye"
        ),
        PrecisionMeasurementOption(
            id = "cap_sleeve",
            name = "Cap Sleeve Length",
            hindiName = "कैप बाजू लंबाई",
            category = PrecisionCategory.SLEEVE_COLLAR,
            description = "Short cap sleeve length covering shoulder tip"
        ),
        PrecisionMeasurementOption(
            id = "half_sleeve",
            name = "Half Sleeve Length",
            hindiName = "हाफ बाजू लंबाई",
            category = PrecisionCategory.SLEEVE_COLLAR,
            description = "Shoulder to upper mid-arm"
        ),
        PrecisionMeasurementOption(
            id = "three_quarter_sleeve",
            name = "3/4th Sleeve Length",
            hindiName = "पौना बाजू लंबाई",
            category = PrecisionCategory.SLEEVE_COLLAR,
            description = "Shoulder to below elbow length"
        ),
        PrecisionMeasurementOption(
            id = "full_sleeve",
            name = "Full Sleeve Length",
            hindiName = "फुल बाजू लंबाई",
            category = PrecisionCategory.SLEEVE_COLLAR,
            description = "Shoulder bone to wrist joint"
        ),
        PrecisionMeasurementOption(
            id = "puff_flare",
            name = "Puff / Bell Sleeve Flare",
            hindiName = "पफ / बेल बाजू चौड़ाई",
            category = PrecisionCategory.SLEEVE_COLLAR,
            description = "Circumference width of balloon or bell sleeve cuff"
        ),
        PrecisionMeasurementOption(
            id = "collar_band",
            name = "Collar Band / Neck Round",
            hindiName = "कॉलर / गला गोलाई",
            category = PrecisionCategory.SLEEVE_COLLAR,
            description = "Full neck circumference for bandhgala or shirt collar"
        ),
        PrecisionMeasurementOption(
            id = "collar_stand_height",
            name = "Stand Collar Height",
            hindiName = "स्टैंड कॉलर ऊंचाई",
            category = PrecisionCategory.SLEEVE_COLLAR,
            description = "Height of mandarin / Chinese standing collar"
        ),
        PrecisionMeasurementOption(
            id = "strap_width",
            name = "Shoulder Strap Width",
            hindiName = "स्ट्रैप चौड़ाई",
            category = PrecisionCategory.SLEEVE_COLLAR,
            description = "Width of sleeveless strap or noodle strap"
        ),

        // Lower Body & Trousers
        PrecisionMeasurementOption(
            id = "ankle_mohri",
            name = "Ankle / Bottom (Mohri)",
            hindiName = "मोरी / पांयचा",
            category = PrecisionCategory.LOWER_BODY,
            description = "Bottom cuff opening of trouser, pant, or salwar"
        ),
        PrecisionMeasurementOption(
            id = "thigh",
            name = "Thigh Round",
            hindiName = "जांघ / थाई",
            category = PrecisionCategory.LOWER_BODY,
            description = "Full circumference at the top of the leg"
        ),
        PrecisionMeasurementOption(
            id = "crotch_asan",
            name = "Crotch / Asan Depth",
            hindiName = "आसन / क्रॉच गहराई",
            category = PrecisionCategory.LOWER_BODY,
            description = "Waistband down to crotch seam junction"
        ),
        PrecisionMeasurementOption(
            id = "knee_round",
            name = "Knee Round",
            hindiName = "घुटना गोलाई",
            category = PrecisionCategory.LOWER_BODY,
            description = "Circumference at the knee"
        ),
        PrecisionMeasurementOption(
            id = "calf_round",
            name = "Calf Round",
            hindiName = "पिंडली गोलाई",
            category = PrecisionCategory.LOWER_BODY,
            description = "Circumference at widest point of calf"
        ),
        PrecisionMeasurementOption(
            id = "outseam",
            name = "Trouser Outseam",
            hindiName = "पैंट बाहरी लंबाई",
            category = PrecisionCategory.LOWER_BODY,
            description = "Top of waistband down outer leg to floor/shoe"
        ),
        PrecisionMeasurementOption(
            id = "inseam_bottom",
            name = "Trouser Inseam",
            hindiName = "पैंट भीतरी लंबाई",
            category = PrecisionCategory.LOWER_BODY,
            description = "Inner crotch joint to bottom hemline"
        ),
        PrecisionMeasurementOption(
            id = "low_waist",
            name = "Low Waist / Belt Line",
            hindiName = "लो-कमर / बेल्ट गोलाई",
            category = PrecisionCategory.LOWER_BODY,
            description = "Waist circumference 2-3 inches below navel"
        ),
        PrecisionMeasurementOption(
            id = "high_waist_rise",
            name = "High Waist Rise",
            hindiName = "हाई वेस्ट राइज",
            category = PrecisionCategory.LOWER_BODY,
            description = "Height from crotch point up to high waistband"
        ),
        PrecisionMeasurementOption(
            id = "salwar_length",
            name = "Salwar Length",
            hindiName = "सलवार लंबाई",
            category = PrecisionCategory.LOWER_BODY,
            description = "Waist down to foot for pleated salwar"
        ),
        PrecisionMeasurementOption(
            id = "salwar_poncha",
            name = "Salwar Poncha Width",
            hindiName = "सलवार पोंचा चौड़ाई",
            category = PrecisionCategory.LOWER_BODY,
            description = "Stiffened bottom hem border width"
        ),
        PrecisionMeasurementOption(
            id = "churidar_length",
            name = "Churidar Length (Extra Churis)",
            hindiName = "चूड़ीदार लंबाई (चूड़ी सहित)",
            category = PrecisionCategory.LOWER_BODY,
            description = "Extended bottom length to form gathered churis at ankle"
        ),
        PrecisionMeasurementOption(
            id = "sharara_knee_joint",
            name = "Sharara Knee Joint Height",
            hindiName = "शरारा घुटना जोड़",
            category = PrecisionCategory.LOWER_BODY,
            description = "Waist down to upper flare joint seam at knee"
        ),
        PrecisionMeasurementOption(
            id = "sharara_flare",
            name = "Sharara / Gharara Flare Round",
            hindiName = "शरारा / गरारा घेर",
            category = PrecisionCategory.LOWER_BODY,
            description = "Total bottom circumference of each leg flare"
        ),

        // Lehenga, Gowns & Flairs
        PrecisionMeasurementOption(
            id = "lehenga_length",
            name = "Lehenga / Skirt Length",
            hindiName = "लहंगा / स्कर्ट लंबाई",
            category = PrecisionCategory.LEHENGA_GOWN,
            description = "Waist tying line down to desired floor brush"
        ),
        PrecisionMeasurementOption(
            id = "flare_ghera",
            name = "Flare / Ghera Round (Meters)",
            hindiName = "घेरा / फ्लेयर (मीटर)",
            category = PrecisionCategory.LEHENGA_GOWN,
            description = "Total bottom round sweep circumference in meters or inches"
        ),
        PrecisionMeasurementOption(
            id = "kali_count",
            name = "Kali Count & Top Width",
            hindiName = "कली संख्या व चौड़ाई",
            category = PrecisionCategory.LEHENGA_GOWN,
            description = "Number of kalis (panels) and individual panel width"
        ),
        PrecisionMeasurementOption(
            id = "waist_to_floor",
            name = "Waist to Floor with Heels",
            hindiName = "कमर से फर्श (हील सहित)",
            category = PrecisionCategory.LEHENGA_GOWN,
            description = "Length from natural waist to floor wearing bridal heels"
        ),
        PrecisionMeasurementOption(
            id = "gown_length",
            name = "Evening Gown Full Length",
            hindiName = "गाउन फुल लंबाई",
            category = PrecisionCategory.LEHENGA_GOWN,
            description = "Shoulder point straight down to floor hem"
        ),
        PrecisionMeasurementOption(
            id = "can_can_height",
            name = "Can-Can Net Height",
            hindiName = "कैन-कैन ऊंचाई",
            category = PrecisionCategory.LEHENGA_GOWN,
            description = "Internal stiffening skirt placement height"
        ),
        PrecisionMeasurementOption(
            id = "trail_length",
            name = "Gown Trail / Train Length",
            hindiName = "गाउन ट्रेन / टेल लंबाई",
            category = PrecisionCategory.LEHENGA_GOWN,
            description = "Extended back floor train length"
        ),
        PrecisionMeasurementOption(
            id = "dupatta_spec",
            name = "Dupatta Length & Width",
            hindiName = "दुपट्टा लंबाई व चौड़ाई",
            category = PrecisionCategory.LEHENGA_GOWN,
            description = "Finished dimensions of matching drape / stole"
        ),

        // Men's Suits, Sherwanis & Coats
        PrecisionMeasurementOption(
            id = "coat_length",
            name = "Suit Coat / Blazer Length",
            hindiName = "कोट / ब्लेज़र लंबाई",
            category = PrecisionCategory.MENS_COAT,
            description = "Base of back collar down to coat hemline"
        ),
        PrecisionMeasurementOption(
            id = "sherwani_length",
            name = "Sherwani Length",
            hindiName = "शेरवानी लंबाई",
            category = PrecisionCategory.MENS_COAT,
            description = "Neck base down to below knee hemline"
        ),
        PrecisionMeasurementOption(
            id = "waistcoat_length",
            name = "Nehru Jacket / Waistcoat Length",
            hindiName = "वास्कट / बंडी लंबाई",
            category = PrecisionCategory.MENS_COAT,
            description = "Shoulder base to jacket bottom"
        ),
        PrecisionMeasurementOption(
            id = "lapel_width",
            name = "Lapel Width",
            hindiName = "लैपल / कॉलर चौड़ाई",
            category = PrecisionCategory.MENS_COAT,
            description = "Width of jacket lapel notch"
        ),
        PrecisionMeasurementOption(
            id = "shirt_collar",
            name = "Shirt Collar Size",
            hindiName = "शर्ट कॉलर साइज",
            category = PrecisionCategory.MENS_COAT,
            description = "Exact neck band circumference"
        ),
        PrecisionMeasurementOption(
            id = "chest_looseness",
            name = "Chest Looseness / Fit Allowance",
            hindiName = "चेस्ट लूजिंग / फिटिंग",
            category = PrecisionCategory.MENS_COAT,
            description = "Ease added over body measurement for comfort"
        ),
        PrecisionMeasurementOption(
            id = "vent_cut",
            name = "Vent Cut Depth (Side/Center)",
            hindiName = "चाक / वेंट ओपनिंग",
            category = PrecisionCategory.MENS_COAT,
            description = "Back center or double side vent slit height"
        )
    )

    fun getCategoryOptions(category: PrecisionCategory): List<PrecisionMeasurementOption> {
        return if (category == PrecisionCategory.ALL) defaultOptions else defaultOptions.filter { it.category == category }
    }

    /**
     * Recommends popular precision specs based on selected garment type
     */
    fun getGarmentRecommendations(garmentType: String): List<String> {
        val lower = garmentType.lowercase()
        return when {
            lower.contains("kurti") || lower.contains("suit") || lower.contains("kameez") -> listOf(
                "Front Neck Depth", "Back Neck Depth", "Kameez / Kurti Length", "Side Slit / Chaak Length", "Daman / Hem Width", "Ankle / Bottom (Mohri)", "Bicep / Muscle Round"
            )
            lower.contains("blouse") || lower.contains("choli") || lower.contains("saree") -> listOf(
                "Front Neck Depth", "Back Neck Depth", "Choli / Blouse Length", "Underbust Ribcage", "Bust Point / Apex Depth", "Apex to Apex Distance", "Bicep / Muscle Round"
            )
            lower.contains("pant") || lower.contains("trouser") || lower.contains("plazo") || lower.contains("palazzo") -> listOf(
                "Ankle / Bottom (Mohri)", "Thigh Round", "Crotch / Asan Depth", "Knee Round", "Calf Round", "Trouser Outseam", "Low Waist / Belt Line"
            )
            lower.contains("lehenga") || lower.contains("gown") || lower.contains("skirt") -> listOf(
                "Lehenga / Skirt Length", "Flare / Ghera Round (Meters)", "Waist to Floor with Heels", "Can-Can Net Height", "Kali Count & Top Width", "Choli / Blouse Length"
            )
            lower.contains("sherwani") || lower.contains("coat") || lower.contains("blazer") || lower.contains("nehru") -> listOf(
                "Suit Coat / Blazer Length", "Sherwani Length", "Cross Back / Teera", "Bicep / Muscle Round", "Lapel Width", "Collar Band / Neck Round", "Shirt Collar Size"
            )
            lower.contains("anarkali") -> listOf(
                "Kameez / Kurti Length", "Flare / Ghera Round (Meters)", "Choli / Blouse Length", "Front Neck Depth", "Back Neck Depth", "Bicep / Muscle Round", "Ankle / Bottom (Mohri)"
            )
            lower.contains("salwar") -> listOf(
                "Salwar Length", "Salwar Poncha Width", "Crotch / Asan Depth", "Ankle / Bottom (Mohri)", "Thigh Round"
            )
            else -> listOf(
                "Front Neck Depth", "Back Neck Depth", "Bicep / Muscle Round", "Flare / Ghera Round (Meters)", "Ankle / Bottom (Mohri)", "Thigh Round", "Crotch / Asan Depth"
            )
        }
    }
}

