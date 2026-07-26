package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.UserWalletEntity
import com.example.ui.theme.CashGreen
import com.example.ui.theme.CoinGold
import com.example.ui.theme.TikTokBlack
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokMagenta
import com.example.ui.theme.TikTokSurface

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonetizationScreen(
    wallet: UserWalletEntity?,
    onDismiss: () -> Unit,
    onOpenCashout: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = TikTokBlack,
        contentColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(640.dp)
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Analytics,
                        contentDescription = "Studio",
                        tint = TikTokMagenta,
                        modifier = Modifier.size(26.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Creator Monetization Studio 📊",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("close_monetization_btn")
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Close", tint = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Overview Banner Card
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color(0xFF8A2387), Color(0xFFE94057), Color(0xFFF27121))
                                )
                            )
                            .padding(20.dp)
                    ) {
                        Column {
                            Text("Total Creator Earnings", fontSize = 12.sp, color = Color.White.copy(alpha = 0.9f))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "$${String.format("%.2f", wallet?.usdBalance ?: 0.0)} USD",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = CoinGold, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${wallet?.coinsBalance ?: 0} Coins in Wallet",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CoinGold
                                )
                            }
                        }
                    }
                }

                // Creator Tier Progress
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(TikTokSurface)
                            .padding(16.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Creator Tier: Gold Pro 👑", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Text("80% Rev Share", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CashGreen)
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            LinearProgressIndicator(
                                progress = { 0.75f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = TikTokCyan,
                                trackColor = Color.DarkGray
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text("7,500 / 10,000 Views for Platinum VIP Tier (+90% Rev Share)", fontSize = 11.sp, color = Color.Gray)
                        }
                    }
                }

                // Revenue Sources Breakdown
                item {
                    Text("Revenue Sources", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(10.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        RevenueSourceItem(
                            title = "Virtual Live Gifts 🎁",
                            subTitle = "${wallet?.totalGiftsReceived ?: 0} Gifts Received from viewers",
                            amountStr = "+${(wallet?.totalGiftsReceived ?: 0) * 10} 🪙",
                            color = TikTokMagenta
                        )

                        RevenueSourceItem(
                            title = "Watch-to-Earn Rewards ⚡",
                            subTitle = "Earned by watching short video feeds",
                            amountStr = "+${wallet?.watchEarnedCoins ?: 0} 🪙",
                            color = CoinGold
                        )

                        RevenueSourceItem(
                            title = "Sponsored Ad Revenue Share 📺",
                            subTitle = "Monthly brand partner payouts",
                            amountStr = "+$5.00 USD",
                            color = CashGreen
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Button to Cashout
            Button(
                onClick = {
                    onDismiss()
                    onOpenCashout()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("monetization_withdraw_btn"),
                colors = ButtonDefaults.buttonColors(containerColor = CashGreen),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text("Withdraw Cashout Funds Now 🚀", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            }
        }
    }
}

@Composable
fun RevenueSourceItem(
    title: String,
    subTitle: String,
    amountStr: String,
    color: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(TikTokSurface)
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(2.dp))
            Text(subTitle, fontSize = 11.sp, color = Color.Gray)
        }
        Text(amountStr, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = color)
    }
}
