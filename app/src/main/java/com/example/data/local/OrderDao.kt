package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.BoutiqueOrder
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Query("SELECT * FROM boutique_orders ORDER BY dateDueForDelivery ASC, id DESC")
    fun getAllOrders(): Flow<List<BoutiqueOrder>>

    @Query("SELECT * FROM boutique_orders WHERE id = :orderId")
    suspend fun getOrderById(orderId: Long): BoutiqueOrder?

    @Query("SELECT * FROM boutique_orders WHERE customerId = :customerId ORDER BY dateTaken DESC")
    fun getOrdersForCustomer(customerId: Long): Flow<List<BoutiqueOrder>>

    @Query("SELECT * FROM boutique_orders WHERE orderStatus != 'DELIVERED' AND orderStatus != 'CANCELLED' ORDER BY dateDueForDelivery ASC")
    fun getActiveOrders(): Flow<List<BoutiqueOrder>>

    @Query("SELECT * FROM boutique_orders WHERE customerName LIKE '%' || :query || '%' OR orderNumber LIKE '%' || :query || '%' OR customerPhone LIKE '%' || :query || '%' ORDER BY dateDueForDelivery ASC")
    fun searchOrders(query: String): Flow<List<BoutiqueOrder>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: BoutiqueOrder): Long

    @Update
    suspend fun updateOrder(order: BoutiqueOrder)

    @Delete
    suspend fun deleteOrder(order: BoutiqueOrder)

    @Query("SELECT COUNT(*) FROM boutique_orders")
    suspend fun getOrderCount(): Int

    @Query("SELECT COUNT(*) FROM boutique_orders WHERE customerId = :customerId")
    suspend fun getOrderCountForCustomer(customerId: Long): Int
}
