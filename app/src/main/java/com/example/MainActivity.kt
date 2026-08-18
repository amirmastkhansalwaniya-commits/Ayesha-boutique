package com.example

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.AppDatabase
import com.example.data.local.SettingsManager
import com.example.data.model.BoutiqueOrder
import com.example.data.model.Customer
import com.example.data.repository.BoutiqueRepository
import com.example.ui.components.BoutiqueTopBar
import com.example.ui.dialogs.CreateOrderDialog
import com.example.ui.dialogs.CustomerDialog
import com.example.ui.dialogs.OrderDetailDialog
import com.example.ui.screens.CustomerScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.FinancialScreen
import com.example.ui.screens.OrdersScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.LocalBoutiqueColors
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.BoutiqueViewModel
import com.example.ui.viewmodel.BoutiqueViewModelFactory

enum class BoutiqueNavTab(val title: String, val icon: ImageVector) {
    DASHBOARD("Atelier", Icons.Default.Dashboard),
    ORDERS("Orders", Icons.Default.Checkroom),
    CUSTOMERS("Clients", Icons.Default.People),
    FINANCES("Finances", Icons.Default.AccountBalanceWallet),
    SETTINGS("Settings", Icons.Default.Settings)
}

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: BoutiqueViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getDatabase(applicationContext)
        val repository = BoutiqueRepository(database.customerDao(), database.orderDao())
        val settingsManager = SettingsManager(applicationContext)
        val factory = BoutiqueViewModelFactory(repository, settingsManager)
        viewModel = ViewModelProvider(this, factory)[BoutiqueViewModel::class.java]

        setContent {
            val settings by viewModel.settings.collectAsStateWithLifecycle()

            MyApplicationTheme(
                customTheme = settings.selectedTheme,
                customFont = settings.selectedFont
            ) {
                BoutiqueMainApp(
                    viewModel = viewModel,
                    onCallCustomer = { phone ->
                        if (phone.isNotBlank()) {
                            val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:$phone")
                            }
                            startActivity(dialIntent)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun BoutiqueMainApp(
    viewModel: BoutiqueViewModel,
    onCallCustomer: (String) -> Unit
) {
    var currentTab by remember { mutableIntStateOf(0) }

    // Dialog state
    var showCreateOrderDialog by remember { mutableStateOf(false) }
    var orderToEdit by remember { mutableStateOf<BoutiqueOrder?>(null) }
    var orderDetailSelected by remember { mutableStateOf<BoutiqueOrder?>(null) }

    var showCustomerDialog by remember { mutableStateOf(false) }
    var customerToEdit by remember { mutableStateOf<Customer?>(null) }

    // Observable states
    val orders by viewModel.orders.collectAsStateWithLifecycle()
    val allCustomers by viewModel.allCustomers.collectAsStateWithLifecycle()
    val filteredCustomers by viewModel.filteredCustomers.collectAsStateWithLifecycle()
    val financialStats by viewModel.financialStats.collectAsStateWithLifecycle()
    val timelineStats by viewModel.timelineStats.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val customerSearchQuery by viewModel.customerSearchQuery.collectAsStateWithLifecycle()
    val selectedOrderFilter by viewModel.selectedOrderFilter.collectAsStateWithLifecycle()
    val settings by viewModel.settings.collectAsStateWithLifecycle()

    val colors = LocalBoutiqueColors.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = colors.darkBackground,
        topBar = {
            val activeSearch = if (currentTab == 2) customerSearchQuery else searchQuery
            val placeholder = if (currentTab == 2) "Search client by name or phone..." else "Search orders, customer, fabric..."
            val showSearchBar = currentTab != 4

            BoutiqueTopBar(
                searchQuery = activeSearch,
                onSearchQueryChange = { query ->
                    if (currentTab == 2) {
                        viewModel.setCustomerSearchQuery(query)
                    } else {
                        viewModel.setSearchQuery(query)
                    }
                },
                searchPlaceholder = placeholder,
                showSearch = showSearchBar,
                boutiqueName = settings.boutiqueName,
                boutiqueTagline = settings.boutiqueTagline,
                logoSymbol = settings.selectedLogo.symbol,
                customLogoPath = settings.customLogoPath,
                fontFamily = settings.selectedFont.fontFamily,
                onOpenSettings = { currentTab = 4 }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = colors.surfaceColor,
                contentColor = colors.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        colors.borderGold,
                        RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    )
            ) {
                BoutiqueNavTab.values().forEachIndexed { index, tab ->
                    val isSelected = currentTab == index
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { currentTab = index },
                        icon = {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = tab.title,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        label = {
                            Text(
                                text = tab.title,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = colors.darkBackground,
                            selectedTextColor = colors.brightGold,
                            indicatorColor = colors.primary,
                            unselectedIconColor = colors.textMutedColor,
                            unselectedTextColor = colors.textMutedColor
                        ),
                        modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(colors.darkBackground)
        ) {
            when (currentTab) {
                0 -> DashboardScreen(
                    orders = orders,
                    financialStats = financialStats,
                    timelineStats = timelineStats,
                    onOpenNewOrder = {
                        orderToEdit = null
                        showCreateOrderDialog = true
                    },
                    onOpenNewCustomer = {
                        customerToEdit = null
                        showCustomerDialog = true
                    },
                    onViewOrder = { order -> orderDetailSelected = order },
                    onAdvanceOrderStatus = { order, newStatus ->
                        viewModel.updateOrderStatus(order, newStatus)
                    },
                    onCallCustomer = onCallCustomer,
                    onNavigateToOrders = { currentTab = 1 },
                    currencySymbol = settings.currencySymbol,
                    boutiqueName = settings.boutiqueName
                )

                1 -> OrdersScreen(
                    orders = orders,
                    selectedFilter = selectedOrderFilter,
                    onSelectFilter = { viewModel.setOrderFilter(it) },
                    onOpenNewOrder = {
                        orderToEdit = null
                        showCreateOrderDialog = true
                    },
                    onViewOrder = { order -> orderDetailSelected = order },
                    onAdvanceOrderStatus = { order, newStatus ->
                        viewModel.updateOrderStatus(order, newStatus)
                    },
                    onCallCustomer = onCallCustomer,
                    currencySymbol = settings.currencySymbol
                )

                2 -> CustomerScreen(
                    customers = filteredCustomers,
                    onAddCustomer = {
                        customerToEdit = null
                        showCustomerDialog = true
                    },
                    onEditCustomer = { customer ->
                        customerToEdit = customer
                        showCustomerDialog = true
                    },
                    onDeleteCustomer = { customer ->
                        viewModel.deleteCustomer(customer)
                    },
                    onCallCustomer = onCallCustomer
                )

                3 -> FinancialScreen(
                    orders = orders,
                    financialStats = financialStats,
                    onRecordPayment = { order, amount ->
                        viewModel.recordAdditionalPayment(order, amount)
                    },
                    onViewOrder = { order -> orderDetailSelected = order },
                    currencySymbol = settings.currencySymbol
                )

                4 -> SettingsScreen(
                    settings = settings,
                    onUpdateTheme = { viewModel.updateTheme(it) },
                    onUpdateFont = { viewModel.updateFont(it) },
                    onUpdateLogo = { viewModel.updateLogo(it) },
                    onUpdateCustomLogoPath = { viewModel.updateCustomLogoPath(it) },
                    onUpdateBoutiqueName = { name, tagline -> viewModel.updateBoutiqueName(name, tagline) },
                    onUpdateCurrency = { viewModel.updateCurrency(it) }
                )
            }
        }
    }

    // Order Creation & Editing Dialog
    if (showCreateOrderDialog) {
        CreateOrderDialog(
            existingOrder = orderToEdit,
            customers = allCustomers,
            onDismiss = {
                showCreateOrderDialog = false
                orderToEdit = null
            },
            onSaveCustomer = { name, phone, address, chest, waist, hips, shoulder, sleeve, trouserLength, onComplete ->
                viewModel.saveCustomer(
                    name = name,
                    phone = phone,
                    address = address,
                    chest = chest,
                    waist = waist,
                    hips = hips,
                    shoulder = shoulder,
                    sleeve = sleeve,
                    trouserLength = trouserLength,
                    onComplete = onComplete
                )
            },
            onSaveOrder = { id, orderNumber, customerId, customerName, customerPhone, customerAddress, numberOfSuits, standardSize, suitType, fabricDetails, measurements, totalAmount, receivedAmount, dateTaken, dateDueForDelivery, orderStatus, specialInstructions ->
                viewModel.saveOrder(
                    id = id,
                    orderNumber = orderNumber,
                    customerId = customerId,
                    customerName = customerName,
                    customerPhone = customerPhone,
                    customerAddress = customerAddress,
                    numberOfSuits = numberOfSuits,
                    standardSize = standardSize,
                    suitType = suitType,
                    fabricDetails = fabricDetails,
                    measurements = measurements,
                    totalAmount = totalAmount,
                    receivedAmount = receivedAmount,
                    dateTaken = dateTaken,
                    dateDueForDelivery = dateDueForDelivery,
                    orderStatus = orderStatus,
                    specialInstructions = specialInstructions
                ) {
                    showCreateOrderDialog = false
                    orderToEdit = null
                }
            },
            currencySymbol = settings.currencySymbol
        )
    }

    // Order Detail & Invoice Modal
    orderDetailSelected?.let { order ->
        val latestOrder = orders.find { it.id == order.id } ?: order
        OrderDetailDialog(
            order = latestOrder,
            onDismiss = { orderDetailSelected = null },
            onEditOrder = {
                orderToEdit = latestOrder
                orderDetailSelected = null
                showCreateOrderDialog = true
            },
            onDeleteOrder = {
                viewModel.deleteOrder(latestOrder)
                orderDetailSelected = null
            },
            onUpdateStatus = { newStatus ->
                viewModel.updateOrderStatus(latestOrder, newStatus)
            },
            onRecordPayment = { amount ->
                viewModel.recordAdditionalPayment(latestOrder, amount)
            },
            receiptText = viewModel.generateReceiptText(latestOrder),
            currencySymbol = settings.currencySymbol
        )
    }

    // Customer Dialog
    if (showCustomerDialog) {
        CustomerDialog(
            customer = customerToEdit,
            onDismiss = {
                showCustomerDialog = false
                customerToEdit = null
            },
            onSave = { id, name, phone, address, email, notes, chest, waist, hips, shoulder, sleeve, trouserLength, unit ->
                viewModel.saveCustomer(
                    id = id,
                    name = name,
                    phone = phone,
                    address = address,
                    email = email,
                    notes = notes,
                    chest = chest,
                    waist = waist,
                    hips = hips,
                    shoulder = shoulder,
                    sleeve = sleeve,
                    trouserLength = trouserLength,
                    unit = unit
                )
                showCustomerDialog = false
                customerToEdit = null
            }
        )
    }
}
