package com.example.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

enum class OrderStatus(val label: String, val step: Int) {
    ORDER_TAKEN("Order Taken", 1),
    CUTTING("Cutting", 2),
    STITCHING("Stitching", 3),
    READY_FOR_FITTING("Fitting Ready", 4),
    READY_FOR_DELIVERY("Ready for Delivery", 5),
    DELIVERED("Delivered", 6),
    CANCELLED("Cancelled", 0)
}

enum class PaymentStatus(val label: String) {
    PAID("Fully Paid"),
    PARTIAL("Partial Advance"),
    UNPAID("Unpaid")
}

@Entity(tableName = "boutique_orders")
data class BoutiqueOrder(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val orderNumber: String,
    val customerId: Long,
    val customerName: String,
    val customerPhone: String,
    val customerAddress: String = "",

    // Module 2: Order & Measurement Tracking
    val numberOfSuits: Int = 1,
    val standardSize: String = "Custom", // "XS", "S", "M", "L", "XL", "2XL", "Custom"
    val suitType: String = "Bridal Lehanga", // Garment style
    val fabricDetails: String = "",

    @Embedded(prefix = "meas_")
    val customMeasurements: BodyMeasurements = BodyMeasurements(),

    // Module 3: Financial Accounting
    val totalAmount: Double = 0.0,
    val receivedAmount: Double = 0.0,
    val pendingAmount: Double = 0.0, // totalAmount - receivedAmount
    val paymentStatus: PaymentStatus = PaymentStatus.UNPAID,

    // Module 4: Timeline Scheduling
    val dateTaken: Long = System.currentTimeMillis(),
    val dateDueForDelivery: Long = System.currentTimeMillis() + (7L * 24 * 60 * 60 * 1000),
    val orderStatus: OrderStatus = OrderStatus.ORDER_TAKEN,

    val specialInstructions: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
