package dev.muffar.moneyfikasi.transaction.add_edit.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.twotone.Category
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.muffar.moneyfikasi.common_ui.component.IconByName
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.resource.R

@Composable
fun CategoryPicker(
    modifier: Modifier = Modifier,
    categories: List<Category>,
    onClick: (Category) -> Unit,
    onClose: () -> Unit,
    ondAdd: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically
            ){
                IconButton(onClick = onClose) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = null
                    )
                }
                Text(
                    text = stringResource(R.string.select_category),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 20.sp
                    )
                )
            }
            IconButton(onClick = ondAdd) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = null
                )
            }
        }

        if (categories.isNotEmpty()) {
            LazyColumn(
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(categories.size) {
                    val category = categories[it]
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onClick(category)
                                onClose()
                            }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(MaterialTheme.shapes.medium)
                                .background(Color(category.color))
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            IconByName(
                                name = category.icon,
                                tint = MaterialTheme.colorScheme.background
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = category.name)
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Icon(
                    imageVector = Icons.TwoTone.Category,
                    contentDescription = stringResource(R.string.no_categories),
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                    modifier = Modifier.size(100.dp)
                )
                Text(text = stringResource(R.string.no_categories))
            }
        }
    }
}