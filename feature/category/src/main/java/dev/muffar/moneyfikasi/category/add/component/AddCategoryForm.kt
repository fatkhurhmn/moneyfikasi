package dev.muffar.moneyfikasi.category.add.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.CommonTextInput
import dev.muffar.moneyfikasi.resource.R

@Composable
fun AddCategoryForm(
    modifier: Modifier = Modifier,
    name: String,
    icon: String,
    color: Long,
    onNameChange: (String) -> Unit,
    onIconClick: () -> Unit,
    onColorClick: () -> Unit,
    onSubmit: () -> Unit,
) {
    Column(
        modifier = modifier.padding(16.dp)
    ) {
        CommonTextInput(
            modifier = Modifier.fillMaxWidth(),
            value = name,
            onValueChange = onNameChange,
            label = stringResource(R.string.name),
            placeholder = stringResource(R.string.enter_category_name),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
        ) {
            Column(
                modifier = Modifier
            ) {
                Text(
                    text = stringResource(R.string.icon),
                    style = MaterialTheme.typography.labelLarge
                )
                Spacer(modifier = Modifier.height(4.dp))
                IconPicker(
                    icon = icon,
                    color = color,
                    onClick = onIconClick
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(R.string.color),
                    style = MaterialTheme.typography.labelLarge
                )
                Spacer(modifier = Modifier.height(4.dp))
                ColorPicker(
                    color = color,
                    onClick = onColorClick
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = MaterialTheme.shapes.large,
            onClick = onSubmit,
        ) {
            Text(text = stringResource(R.string.save))
        }
    }
}