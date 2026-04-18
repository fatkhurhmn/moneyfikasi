package dev.muffar.moneyfikasi.domain.model

enum class Colors(val color: Long) {
    // Red
    Red10(0xFFEF5350),
    Red20(0xFFE53935),
    Red30(0xFFC62828),
    Red40(0xFFB71C1C),

    // Pink
    Pink10(0xFFEC407A),
    Pink20(0xFFD81B60),
    Pink30(0xFFAD1457),
    Pink40(0xFF880E4F),

    // Purple
    Purple10(0xFFAB47BC),
    Purple20(0xFF8E24AA),
    Purple30(0xFF6A1B9A),
    Purple40(0xFF4A148C),

    // Deep Purple
    DeepPurple10(0xFF7E57C2),
    DeepPurple20(0xFF5E35B1),
    DeepPurple30(0xFF4527A0),
    DeepPurple40(0xFF311B92),

    // Indigo
    Indigo10(0xFF5C6BC0),
    Indigo20(0xFF3949AB),
    Indigo30(0xFF283593),
    Indigo40(0xFF1A237E),

    // Blue
    Blue10(0xFF42A5F5),
    Blue20(0xFF1E88E5),
    Blue30(0xFF1565C0),
    Blue40(0xFF0D47A1),

    // Light Blue
    LightBlue10(0xFF29B6F6),
    LightBlue20(0xFF039BE5),
    LightBlue30(0xFF0277BD),
    LightBlue40(0xFF01579B),

    // Cyan
    Cyan10(0xFF26C6DA),
    Cyan20(0xFF00ACC1),
    Cyan30(0xFF00838F),
    Cyan40(0xFF006064),

    // Teal
    Teal10(0xFF26A69A),
    Teal20(0xFF00897B),
    Teal30(0xFF00695C),
    Teal40(0xFF004D40),

    // Green
    Green10(0xFF66BB6A),
    Green20(0xFF43A047),
    Green30(0xFF2E7D32),
    Green40(0xFF1B5E20),

    // Light Green
    LightGreen10(0xFF9CCC65),
    LightGreen20(0xFF7CB342),
    LightGreen30(0xFF558B2F),
    LightGreen40(0xFF33691E),

    // Lime
    Lime10(0xFFD4E157),
    Lime20(0xFFC0CA33),
    Lime30(0xFF9E9D24),
    Lime40(0xFF827717),

    // Yellow
    Yellow10(0xFFFFEB3B),
    Yellow20(0xFFFBC02D),
    Yellow30(0xFFF9A825),
    Yellow40(0xFFF57F17),

    // Amber
    Amber10(0xFFFFCA28),
    Amber20(0xFFFFB300),
    Amber30(0xFFFF8F00),
    Amber40(0xFFFF6F00),

    // Orange
    Orange10(0xFFFFA726),
    Orange20(0xFFFB8C00),
    Orange30(0xFFEF6C00),
    Orange40(0xFFE65100),

    // Deep Orange
    DeepOrange10(0xFFFF7043),
    DeepOrange20(0xFFF4511E),
    DeepOrange30(0xFFD84315),
    DeepOrange40(0xFFBF360C),

    // Brown
    Brown10(0xFF8D6E63),
    Brown20(0xFF6D4C41),
    Brown30(0xFF4E342E),
    Brown40(0xFF3E2723),

    // Grey
    Grey10(0xFFBDBDBD),
    Grey20(0xFF757575),
    Grey30(0xFF424242),
    Grey40(0xFF212121),

    // Blue Gray
    BlueGrey10(0xFF78909C),
    BlueGrey20(0xFF546E7A),
    BlueGrey30(0xFF37474F),
    BlueGrey40(0xFF263238);

    companion object {
        fun getColors(): List<Long> {
            return entries.map { it.color }
        }
    }
}