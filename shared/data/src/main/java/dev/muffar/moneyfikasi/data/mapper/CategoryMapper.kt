package dev.muffar.moneyfikasi.data.mapper

import dev.muffar.moneyfikasi.data.db.entity.CategoryEntity
import dev.muffar.moneyfikasi.domain.model.Category

fun CategoryEntity.toModel(): Category {
    return Category(
        id = id,
        name = name,
        icon = icon,
        type = type,
        isActive = isActive,
    )
}

fun Category.toEntity(): CategoryEntity {
    return CategoryEntity(
        id = id,
        name = name,
        icon = icon,
        type = type,
        isActive = isActive,
    )
}

fun List<CategoryEntity>.mapToModel(): List<Category> {
    return map { it.toModel() }
}

fun List<Category>.mapToEntity(): List<CategoryEntity> {
    return map { it.toEntity() }
}