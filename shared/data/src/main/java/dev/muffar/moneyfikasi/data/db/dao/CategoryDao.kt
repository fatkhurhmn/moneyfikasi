package dev.muffar.moneyfikasi.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import dev.muffar.moneyfikasi.data.db.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface CategoryDao {
    @Upsert
    suspend fun save(category: CategoryEntity)

    @Upsert
    suspend fun saveAll(categories: List<CategoryEntity>)

    @Query("UPDATE categories SET is_active = :isActive WHERE id = :id")
    suspend fun update(id: UUID, isActive: Boolean)

    @Query("DELETE FROM categories WHERE id = :id")
    suspend fun delete(id: UUID)

    @Query("DELETE FROM categories")
    suspend fun deleteAll()

    @Query("SELECT * FROM categories")
    fun getAll(): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM categories WHERE id = :id")
    suspend fun getById(id: UUID): CategoryEntity?
}