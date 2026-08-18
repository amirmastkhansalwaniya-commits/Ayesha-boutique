package com.example.data.repository

import com.example.data.local.CustomerDao
import com.example.data.local.OrderDao
import com.example.data.model.BodyMeasurements
import com.example.data.model.BoutiqueOrder
import com.example.data.model.Customer
import com.example.data.model.OrderStatus
import com.example.data.model.PaymentStatus
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.TimeUnit

class BoutiqueRepository(
    private val customerDao: CustomerDao,
    private val orderDao: OrderDao
) {
    val allCustomers: Flow<List<Customer>> = customerDao.getAllCustomers()
    val allOrders: Flow<List<BoutiqueOrder>> = orderDao.getAllOrders()
    val activeOrders: Flow<List<BoutiqueOrder>> = orderDao.getActiveOrders()

    fun searchCustomers(query: String): Flow<List<Customer>> =
        if (query.isBlank()) customerDao.getAllCustomers() else customerDao.searchCustomers(query)

    fun searchOrders(query: String): Flow<List<BoutiqueOrder>> =
        if (query.isBlank()) orderDao.getAllOrders() else orderDao.searchOrders(query)

    fun getOrdersForCustomer(customerId: Long): Flow<List<BoutiqueOrder>> =
        orderDao.getOrdersForCustomer(customerId)

    suspend fun getCustomerById(id: Long): Customer? = customerDao.getCustomerById(id)
    suspend fun getOrderById(id: Long): BoutiqueOrder? = orderDao.getOrderById(id)

    suspend fun insertCustomer(customer: Customer): Long = customerDao.insertCustomer(customer)
    suspend fun updateCustomer(customer: Customer) = customerDao.updateCustomer(customer)
    suspend fun deleteCustomer(customer: Customer) = customerDao.deleteCustomer(customer)

    suspend fun insertOrder(order: BoutiqueOrder): Long {
        val pending = (order.totalAmount - order.receivedAmount).coerceAtLeast(0.0)
        val pStatus = when {
            order.receivedAmount >= order.totalAmount && order.totalAmount > 0 -> PaymentStatus.PAID
            order.receivedAmount > 0 -> PaymentStatus.PARTIAL
            else -> PaymentStatus.UNPAID
        }
        val computedOrder = order.copy(
            pendingAmount = pending,
            paymentStatus = pStatus,
            updatedAt = System.currentTimeMillis()
        )
        return orderDao.insertOrder(computedOrder)
    }

    suspend fun updateOrder(order: BoutiqueOrder) {
        val pending = (order.totalAmount - order.receivedAmount).coerceAtLeast(0.0)
        val pStatus = when {
            order.receivedAmount >= order.totalAmount && order.totalAmount > 0 -> PaymentStatus.PAID
            order.receivedAmount > 0 -> PaymentStatus.PARTIAL
            else -> PaymentStatus.UNPAID
        }
        val computedOrder = order.copy(
            pendingAmount = pending,
            paymentStatus = pStatus,
            updatedAt = System.currentTimeMillis()
        )
        orderDao.updateOrder(computedOrder)
    }

    suspend fun deleteOrder(order: BoutiqueOrder) = orderDao.deleteOrder(order)

    suspend fun seedSampleDataIfEmpty() {
        val count = customerDao.getCustomerCount()
        if (count == 0) {
            val now = System.currentTimeMillis()
            val day = TimeUnit.DAYS.toMillis(1)

            // Seed Customers
            val c1Id = customerDao.insertCustomer(
                Customer(
                    name = "Fatima Al-Zahra",
                    phone = "+1 (555) 382-9102",
                    address = "Villa 14, Royal Palm Avenue, Beverly Hills",
                    email = "fatima.zahra@example.com",
                    notes = "VIP client. Prefers gold zari embroidery and heavy silk finish.",
                    defaultChest = 38.0,
                    defaultWaist = 30.0,
                    defaultHips = 40.5,
                    defaultShoulder = 15.5,
                    defaultSleeve = 22.0,
                    defaultTrouserLength = 39.0,
                    defaultMeasurementUnit = "in"
                )
            )

            val c2Id = customerDao.insertCustomer(
                Customer(
                    name = "Amina Noor",
                    phone = "+1 (555) 749-3011",
                    address = "45 Magnolia Crescent, Manhattan",
                    email = "amina.noor@example.com",
                    notes = "Prefers lightweight chiffon and pastel linings.",
                    defaultChest = 36.0,
                    defaultWaist = 28.5,
                    defaultHips = 38.0,
                    defaultShoulder = 14.5,
                    defaultSleeve = 21.0,
                    defaultTrouserLength = 38.0,
                    defaultMeasurementUnit = "in"
                )
            )

            val c3Id = customerDao.insertCustomer(
                Customer(
                    name = "Zainab Malik",
                    phone = "+1 (555) 892-4419",
                    address = "78 Kensington Lane, London Suite",
                    email = "zainab.m@example.com",
                    notes = "Wedding order for sister's reception. Strict deadline.",
                    defaultChest = 40.0,
                    defaultWaist = 32.0,
                    defaultHips = 42.0,
                    defaultShoulder = 16.0,
                    defaultSleeve = 23.0,
                    defaultTrouserLength = 40.0,
                    defaultMeasurementUnit = "in"
                )
            )

            // Seed Orders
            orderDao.insertOrder(
                BoutiqueOrder(
                    orderNumber = "AY-2026-001",
                    customerId = c1Id,
                    customerName = "Fatima Al-Zahra",
                    customerPhone = "+1 (555) 382-9102",
                    customerAddress = "Villa 14, Royal Palm Avenue",
                    numberOfSuits = 2,
                    standardSize = "Custom",
                    suitType = "Bridal Lehanga & Kurti",
                    fabricDetails = "Pure Banarasi Raw Silk with Antique Gold Zari & Dabka Embroidery",
                    customMeasurements = BodyMeasurements(
                        chest = 38.0,
                        waist = 30.0,
                        hips = 40.5,
                        shoulder = 15.5,
                        sleeve = 22.0,
                        trouserLength = 39.0,
                        neck = 7.5,
                        inseam = 28.0,
                        armhole = 17.0,
                        thigh = 22.0,
                        unit = "in",
                        measurementNotes = "Deep back neckline with handmade tassels."
                    ),
                    totalAmount = 2400.0,
                    receivedAmount = 1500.0,
                    pendingAmount = 900.0,
                    paymentStatus = PaymentStatus.PARTIAL,
                    dateTaken = now - (2 * day),
                    dateDueForDelivery = now + (3 * day),
                    orderStatus = OrderStatus.STITCHING,
                    specialInstructions = "Double lining on blouse with gold piping."
                )
            )

            orderDao.insertOrder(
                BoutiqueOrder(
                    orderNumber = "AY-2026-002",
                    customerId = c2Id,
                    customerName = "Amina Noor",
                    customerPhone = "+1 (555) 749-3011",
                    customerAddress = "45 Magnolia Crescent",
                    numberOfSuits = 1,
                    standardSize = "M",
                    suitType = "3-Piece Anarkali Suit",
                    fabricDetails = "Georgette with Mukaish handwork and organza dupatta",
                    customMeasurements = BodyMeasurements(
                        chest = 36.0,
                        waist = 28.5,
                        hips = 38.0,
                        shoulder = 14.5,
                        sleeve = 21.0,
                        trouserLength = 38.0,
                        neck = 6.5,
                        unit = "in"
                    ),
                    totalAmount = 850.0,
                    receivedAmount = 850.0,
                    pendingAmount = 0.0,
                    paymentStatus = PaymentStatus.PAID,
                    dateTaken = now - (4 * day),
                    dateDueForDelivery = now + (1 * day),
                    orderStatus = OrderStatus.READY_FOR_DELIVERY,
                    specialInstructions = "Call before dispatch."
                )
            )

            orderDao.insertOrder(
                BoutiqueOrder(
                    orderNumber = "AY-2026-003",
                    customerId = c3Id,
                    customerName = "Zainab Malik",
                    customerPhone = "+1 (555) 892-4419",
                    customerAddress = "78 Kensington Lane",
                    numberOfSuits = 3,
                    standardSize = "Custom",
                    suitType = "Embroidered Velvet Gown & Shawl",
                    fabricDetails = "Deep Maroon Micro-Velvet with Kashmiri Tilla and Pearl work",
                    customMeasurements = BodyMeasurements(
                        chest = 40.0,
                        waist = 32.0,
                        hips = 42.0,
                        shoulder = 16.0,
                        sleeve = 23.0,
                        trouserLength = 40.0,
                        neck = 7.0,
                        unit = "in",
                        measurementNotes = "Floor length gown with high-collar back."
                    ),
                    totalAmount = 3200.0,
                    receivedAmount = 1000.0,
                    pendingAmount = 2200.0,
                    paymentStatus = PaymentStatus.PARTIAL,
                    dateTaken = now - (1 * day),
                    dateDueForDelivery = now + (6 * day),
                    orderStatus = OrderStatus.CUTTING,
                    specialInstructions = "Fitting scheduled on Thursday."
                )
            )
        }
    }
}
