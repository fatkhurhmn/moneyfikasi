package dev.muffar.moneyfikasi.common_ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Square
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun IconByName(
    name: String,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current,
) {
    val icon: ImageVector? = remember(name) {
        try {
            val cl = Class.forName("androidx.compose.material.icons.filled.${name}Kt")
            val method = cl.declaredMethods.first()
            method.invoke(null, Icons.Filled) as ImageVector
        } catch (_: Throwable) {
            null
        }
    }
    if (icon != null) {
        Icon(
            imageVector = icon,
            contentDescription = "$name icon",
            modifier = modifier,
            tint = tint
        )
    } else {
        Icon(
            imageVector = Icons.Filled.Square,
            contentDescription = "$name icon",
            modifier = modifier,
            tint = tint
        )
    }
}