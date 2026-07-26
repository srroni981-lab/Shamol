package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.EarningTransactionEntity
import com.example.ui.theme.CashGreen
import com.example.ui.theme.CoinGold
import com.example.ui.theme.TikTokBlack
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokMagenta
import com.example.ui.theme.TikTokSurface

@Composable
fun InboxScreen(
    transactions: List<EarningTransactionEntity>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TikTokBlack)
            .padding(top = 44.dp, start = 16.dp, end = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Inbox & Earnings Activity 📩", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Icon(Icons.Default.Notifications, contentDescription = null, tint = TikTokCyan, modifier = Modifier.size(24.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Quick Filter Pills
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(TikTokMagenta)
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text("All Activity", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(TikTokSurface)
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text("Gifts & Rewards", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color.LightGray)
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(TikTokSurface)
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text("Cashouts", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color.LightGray)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(transactions) { tx ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(TikTokSurface)
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val iconVector = when (tx.category) {
                        "GIFT_SENT", "GIFT_RECEIVED" -> Icons.Default.CardGiftcard
                        "WATCH_EARN" -> Icons.Default.MonetizationOn
                        "CASHOUT" -> Icons.Default.AccountBalanceWallet
                        else -> Icons.Default.Notifications
                    }

                    val iconBg = when (tx.category) {
                        "GIFT_SENT", "GIFT_RECEIVED" -> TikTokMagenta
                        "WATCH_EARN" -> CoinGold
                        "CASHOUT" -> CashGreen
                        else -> TikTokCyan
                    }

                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(iconBg.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(iconVector, contentDescription = null, tint = iconBg, modifier = Modifier.size(22.dp))
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(tx.title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Status: ${tx.status}",
                            fontSize = 11.sp,
                            color = if (tx.status == "COMPLETED") CashGreen else CoinGold
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        if (tx.coinsChange != 0L) {
                            Text(
                                text = "${if (tx.coinsChange > 0) "+" else ""}${tx.coinsChange} 🪙",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (tx.coinsChange > 0) CoinGold else Color.White
                            )
                        }
                        if (tx.usdChange != 0.0) {
                            Text(
                                text = "${if (tx.usdChange > 0) "+" else ""}$${String.format("%.2f", tx.usdChange)}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = CashGreen
                            )
                        }
                    }
                }
            }
        }
    }
}
