package com.mohakchavan.applists.database.dao.inventory

import androidx.room.*
import com.mohakchavan.applists.database.entity.inventory.Item
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertItem(item: Item)

    @Query("SELECT * FROM item WHERE id = :id")
    fun getItem(id: Int): Flow<Item>

    @Query("SELECT * FROM item ORDER BY name ASC")
    fun getAllItems(): Flow<List<Item>>

    @Update
    suspend fun updateItem(item: Item)

    @Delete
    suspend fun deleteItem(item: Item)

}