package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.SettingsManager
import com.example.data.model.AppFontPreset
import com.example.data.model.AppLogoPreset
import com.example.data.model.AppThemePreset
import com.example.data.model.BodyMeasurements
import com.example.data.model.BoutiqueOrder
import com.example.data.model.CustomAppSettings
import com.example.data.model.Customer
import com.example.data.model.OrderStatus
import com.example.data.model.PaymentStatus
import com.example.data.repository.BoutiqueRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

enum class OrderFilterTab(val title: String) {
    ALL("All Orders"),
    ACTIVE("Active"),
    CUTTING("Cutting"),
    STITCHING("Stitching"),
    FITTING("Fitting"),
    READY("Ready"),
    DELIVERED("Delivered"),
    UNPAID("Pending Dues")
}

data class FinancialStats(
    val totalRevenue: Double = 0.0,
    val totalCollected: Double = 0.0,
    val totalPending: Double = 0.0,
    val fullyPaidCount: Int = 0,
    val partialPaidCount: Int = 0,
    val unpaidCount: Int = 0
)

data class TimelineStats(
    val totalActive: Int = 0,
    val readyForDelivery: Int = 0,
    val dueToday: Int = 0,
    val overdue: Int = 0,
    val dueThisWeek: Int = 0
)

class BoutiqueViewModel(
    private val repository: BoutiqueRepository,
    private val settingsManager: SettingsManager
) : ViewModel() {

    val settings: StateFlow<CustomAppSettings> = settingsManager.settings

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _customerSearchQuery = MutableStateFlow("")
    val customerSearchQuery: StateFlow<String> = _customerSearchQuery.asStateFlow()

    private val _selectedOrderFilter = MutableStateFlow(OrderFilterTab.ALL)
    val selectedOrderFilter: StateFlow<OrderFilterTab> = _selectedOrderFilter.asStateFlow()

    val allCustomers: StateFlow<List<Customer>> = repository.allCustomers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val filteredCustomers: StateFlow<List<Customer>> = _customerSearchQuery
        .flatMapLatest { query -> repository.searchCustomers(query) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val rawOrders = _searchQuery
        .flatMapLatest { query -> repository.searchOrders(query) }

    val orders: StateFlow<List<BoutiqueOrder>> = combine(rawOrders, _selectedOrderFilter) { list, filter ->
        when (filter) {
            OrderFilterTab.ALL -> list
            OrderFilterTab.ACTIVE -> list.filter { it.orderStatus != OrderStatus.DELIVERED && it.orderStatus != OrderStatus.CANCELLED }
            OrderFilterTab.CUTTING -> list.filter { it.orderStatus == OrderStatus.CUTTING }
            OrderFilterTab.STITCHING -> list.filter { it.orderStatus == OrderStatus.STITCHING }
            OrderFilterTab.FITTING -> list.filter { it.orderStatus == OrderStatus.READY_FOR_FITTING }
            OrderFilterTab.READY -> list.filter { it.orderStatus == OrderStatus.READY_FOR_DELIVERY }
            OrderFilterTab.DELIVERED -> list.filter { it.orderStatus == OrderStatus.DELIVERED }
            OrderFilterTab.UNPAID -> list.filter { it.pendingAmount > 0 }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val financialStats: StateFlow<FinancialStats> = repository.allOrders.combine(_searchQuery) { orders, _ ->
        var totalRev = 0.0
        var totalRec = 0.0
        var totalPen = 0.0
        var paidCnt = 0
        var partialCnt = 0
        var unpaidCnt = 0

        orders.forEach { order ->
            totalRev += order.totalAmount
            totalRec += order.receivedAmount
            totalPen += order.pendingAmount

            when (order.paymentStatus) {
                PaymentStatus.PAID -> paidCnt++
                PaymentStatus.PARTIAL -> partialCnt++
                PaymentStatus.UNPAID -> unpaidCnt++
            }
        }

        FinancialStats(
            totalRevenue = totalRev,
            totalCollected = totalRec,
            totalPending = totalPen,
            fullyPaidCount = paidCnt,
            partialPaidCount = partialCnt,
            unpaidCount = unpaidCnt
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FinancialStats())

    val timelineStats: StateFlow<TimelineStats> = repository.allOrders.combine(_searchQuery) { orders, _ ->
        val now = System.currentTimeMillis()
        val oneDay = TimeUnit.DAYS.toMillis(1)
        val sevenDays = TimeUnit.DAYS.toMillis(7)

        var activeCnt = 0
        var readyCnt = 0
        var todayCnt = 0
        var overdueCnt = 0
        var weekCnt = 0

        orders.forEach { order ->
            if (order.orderStatus != OrderStatus.DELIVERED && order.orderStatus != OrderStatus.CANCELLED) {
                activeCnt++
                if (order.orderStatus == OrderStatus.READY_FOR_DELIVERY) readyCnt++

                val diff = order.dateDueForDelivery - now
                if (diff < 0) {
                    overdueCnt++
                } else if (diff <= oneDay) {
                    todayCnt++
                } else if (diff <= sevenDays) {
                    weekCnt++
                }
            }
        }

        TimelineStats(
            totalActive = activeCnt,
            readyForDelivery = readyCnt,
            dueToday = todayCnt,
            overdue = overdueCnt,
            dueThisWeek = weekCnt
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TimelineStats())

    init {
        viewModelScope.launch {
            repository.seedSampleDataIfEmpty()
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setCustomerSearchQuery(query: String) {
        _customerSearchQuery.value = query
    }

    fun setOrderFilter(filter: OrderFilterTab) {
        _selectedOrderFilter.value = filter
    }

    fun saveCustomer(
        id: Long = 0,
        name: String,
        phone: String,
        address: String,
        email: String = "",
        notes: String = "",
        chest: Double? = null,
        waist: Double? = null,
        hips: Double? = null,
        shoulder: Double? = null,
        sleeve: Double? = null,
        trouserLength: Double? = null,
        unit: String = "in",
        onComplete: (Long) -> Unit = {}
    ) {
        viewModelScope.launch {
            val customer = Customer(
                id = id,
                name = name.trim(),
                phone = phone.trim(),
                address = address.trim(),
                email = email.trim(),
                notes = notes.trim(),
                defaultChest = chest,
                defaultWaist = waist,
                defaultHips = hips,
                defaultShoulder = shoulder,
                defaultSleeve = sleeve,
                defaultTrouserLength = trouserLength,
                defaultMeasurementUnit = unit
            )
            if (id == 0L) {
                val newId = repository.insertCustomer(customer)
                onComplete(newId)
            } else {
                repository.updateCustomer(customer)
                onComplete(id)
            }
        }
    }

    fun deleteCustomer(customer: Customer) {
        viewModelScope.launch {
            repository.deleteCustomer(customer)
        }
    }

    fun updateTheme(theme: AppThemePreset) {
        settingsManager.setTheme(theme)
    }

    fun updateFont(font: AppFontPreset) {
        settingsManager.setFont(font)
    }

    fun updateLogo(logo: AppLogoPreset) {
        settingsManager.setLogo(logo)
    }

    fun updateCustomLogoPath(path: String?) {
        settingsManager.setCustomLogoPath(path)
    }

    fun updateBoutiqueName(name: String, tagline: String) {
        settingsManager.setBoutiqueDetails(name, tagline)
    }

    fun updateCurrency(symbol: String) {
        settingsManager.setCurrency(symbol)
    }

    fun saveOrder(
        id: Long = 0,
        orderNumber: String = "",
        customerId: Long,
        customerName: String,
        customerPhone: String,
        customerAddress: String,
        numberOfSuits: Int,
        standardSize: String,
        suitType: String,
        fabricDetails: String,
        measurements: BodyMeasurements,
        totalAmount: Double,
        receivedAmount: Double,
        dateTaken: Long,
        dateDueForDelivery: Long,
        orderStatus: OrderStatus,
        specialInstructions: String,
        onComplete: () -> Unit = {}
    ) {
        viewModelScope.launch {
            val finalOrderNum = if (orderNumber.isBlank()) {
                val timestamp = System.currentTimeMillis() % 10000
                "AY-${SimpleDateFormat("yy", Locale.getDefault()).format(Date())}-$timestamp"
            } else orderNumber

            val order = BoutiqueOrder(
                id = id,
                orderNumber = finalOrderNum,
                customerId = customerId,
                customerName = customerName.trim(),
                customerPhone = customerPhone.trim(),
                customerAddress = customerAddress.trim(),
                numberOfSuits = numberOfSuits.coerceAtLeast(1),
                standardSize = standardSize,
                suitType = suitType.trim(),
                fabricDetails = fabricDetails.trim(),
                customMeasurements = measurements,
                totalAmount = totalAmount,
                receivedAmount = receivedAmount,
                dateTaken = dateTaken,
                dateDueForDelivery = dateDueForDelivery,
                orderStatus = orderStatus,
                specialInstructions = specialInstructions.trim()
            )

            if (id == 0L) {
                repository.insertOrder(order)
            } else {
                repository.updateOrder(order)
            }
            onComplete()
        }
    }

    fun updateOrderStatus(order: BoutiqueOrder, newStatus: OrderStatus) {
        viewModelScope.launch {
            repository.updateOrder(order.copy(orderStatus = newStatus))
        }
    }

    fun recordAdditionalPayment(order: BoutiqueOrder, additionalAmount: Double) {
        viewModelScope.launch {
            val newReceived = order.receivedAmount + additionalAmount
            repository.updateOrder(order.copy(receivedAmount = newReceived))
        }
    }

    fun deleteOrder(order: BoutiqueOrder) {
        viewModelScope.launch {
            repository.deleteOrder(order)
        }
    }

    fun updateSettings(newSettings: CustomAppSettings) {
        settingsManager.updateSettings(newSettings)
    }

    fun setTheme(theme: AppThemePreset) {
        settingsManager.setTheme(theme)
    }

    fun setFont(font: AppFontPreset) {
        settingsManager.setFont(font)
    }

    fun setLogo(logo: AppLogoPreset) {
        settingsManager.setLogo(logo)
    }

    fun setBoutiqueDetails(name: String, tagline: String) {
        settingsManager.setBoutiqueDetails(name, tagline)
    }

    fun setCurrency(currency: String) {
        settingsManager.setCurrency(currency)
    }

    fun generateReceiptText(order: BoutiqueOrder): String {
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val cur = settings.value.currencySymbol
        val boutiqueName = settings.value.boutiqueName
        val tagline = settings.value.boutiqueTagline
        return """
            ⚜️ *${boutiqueName.uppercase()}* ⚜️
            *${tagline}*
            ━━━━━━━━━━━━━━━━━━━
            *Order No:* ${order.orderNumber}
            *Customer:* ${order.customerName}
            *Contact:* ${order.customerPhone}
            *Address:* ${order.customerAddress}
            
            👗 *GARMENT SPECIFICATIONS*
            • *Suit Type:* ${order.suitType}
            • *Quantity:* ${order.numberOfSuits} Suit(s)
            • *Standard Size:* ${order.standardSize}
            • *Fabric / Design:* ${if (order.fabricDetails.isNotBlank()) order.fabricDetails else "Bespoke Cut"}
            
            📏 *BODY MEASUREMENTS (${order.customMeasurements.unit})*
            • Chest: ${order.customMeasurements.chest ?: "—"}
            • Waist: ${order.customMeasurements.waist ?: "—"}
            • Hips: ${order.customMeasurements.hips ?: "—"}
            • Shoulder: ${order.customMeasurements.shoulder ?: "—"}
            • Sleeve: ${order.customMeasurements.sleeve ?: "—"}
            • Trouser Length: ${order.customMeasurements.trouserLength ?: "—"}
            ${if (order.customMeasurements.neck != null) "• Neck: ${order.customMeasurements.neck}" else ""}
            ${if (order.customMeasurements.inseam != null) "• Inseam: ${order.customMeasurements.inseam}" else ""}
            
            📅 *TIMELINE & SCHEDULE*
            • *Booking Date:* ${dateFormat.format(Date(order.dateTaken))}
            • *Delivery Date:* ${dateFormat.format(Date(order.dateDueForDelivery))}
            • *Current Status:* ${order.orderStatus.label}
            
            💰 *FINANCIAL ACCOUNTING*
            • *Total Amount:* $cur${String.format(Locale.US, "%.2f", order.totalAmount)}
            • *Advance Received:* $cur${String.format(Locale.US, "%.2f", order.receivedAmount)}
            • *Balance Pending:* $cur${String.format(Locale.US, "%.2f", order.pendingAmount)}
            • *Payment Status:* ${order.paymentStatus.label}
            
            ${if (order.specialInstructions.isNotBlank()) "📝 *Notes:* ${order.specialInstructions}\n" else ""}
            ━━━━━━━━━━━━━━━━━━━
            _Thank you for choosing ${boutiqueName}._
            _This app created by Amir Khan_
        """.trimIndent()
    }
}

class BoutiqueViewModelFactory(
    private val repository: BoutiqueRepository,
    private val settingsManager: SettingsManager
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BoutiqueViewModel::class.java)) {
            return BoutiqueViewModel(repository, settingsManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
