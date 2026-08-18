package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BoutiqueOrder
import com.example.data.model.OrderStatus
import com.example.ui.components.OrderCard
import com.example.ui.theme.LocalBoutiqueColors
import com.example.ui.viewmodel.OrderFilterTab

@Composable
fun OrdersScreen(
    orders: List<BoutiqueOrder>,
    selectedFilter: OrderFilterTab,
    onSelectFilter: (OrderFilterTab) -> Unit,
    onOpenNewOrder: () -> Unit,
    onViewOrder: (BoutiqueOrder) -> Unit,
    onAdvanceOrderStatus: (BoutiqueOrder, OrderStatus) -> Unit,
    onCallCustomer: (String) -> Unit,
    currencySymbol: String = "₹"
) {
    val colors = LocalBoutiqueColors.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.darkBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Filter Chips Scrollable Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OrderFilterTab.values().forEach { tab ->
                    val isSelected = selectedFilter == tab
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) colors.primary else colors.cardBackground)
                            .border(
                                1.dp,
                                if (isSelected) colors.brightGold else colors.borderGold.copy(alpha = 0.4f),
                                RoundedCornerShape(20.dp)
                            )
                            .clickable { onSelectFilter(tab) }
                            .padding(horizontal = 14.dp, vertical = 7.dp)
                            .testTag("filter_chip_${tab.name}")
                    ) {
                        Text(
                            text = tab.title,
                            color = if (isSelected) colors.darkBackground else colors.textColor,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Orders list
            if (orders.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Checkroom,
                            contentDescription = null,
                            tint = colors.primary,
                            modifier = Modifier.size(54.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No matching orders found",
                            color = colors.textColor,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Tap the gold '+' button below to create a new order",
                            color = colors.textMutedColor,
                            fontSize = 12.sp
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 90.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(orders, key = { it.id }) { order ->
                        OrderCard(
                            order = order,
                            onViewDetails = { onViewOrder(order) },
                            onAdvanceStatus = { newStatus -> onAdvanceOrderStatus(order, newStatus) },
                            onCallCustomer = onCallCustomer,
                            currencySymbol = currencySymbol
                        )
                    }
                }
            }
        }

        // Floating Action Button
        FloatingActionButton(
            onClick = onOpenNewOrder,
            containerColor = colors.primary,
            contentColor = colors.darkBackground,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 80.dp, end = 20.dp)
                .testTag("fab_new_order")
        ) {
            Icon(Icons.Default.Add, contentDescription = "New Order", modifier = Modifier.size(24.dp))
        }
    }
}
