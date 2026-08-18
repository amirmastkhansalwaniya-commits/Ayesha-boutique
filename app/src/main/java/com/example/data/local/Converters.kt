package com.example.data.local

import androidx.room.TypeConverter
import com.example.data.model.OrderStatus
import com.example.data.model.PaymentStatus

class Converters {
    @TypeConverter
    fun fromOrderStatus(status: OrderStatus): String = status.name

    @TypeConverter
    fun toOrderStatus(value: String): OrderStatus {
        return try {
            OrderStatus.valueOf(value)
        } catch (e: Exception) {
            OrderStatus.ORDER_TAKEN
        }
    }

    @TypeConverter
    fun fromPaymentStatus(status: PaymentStatus): String = status.name

    @TypeConverter
    fun toPaymentStatus(value: String): PaymentStatus {
        return try {
            PaymentStatus.valueOf(value)
        } catch (e: Exception) {
            PaymentStatus.UNPAID
        }
    }
}
