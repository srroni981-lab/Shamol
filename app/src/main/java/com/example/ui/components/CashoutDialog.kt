package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.UserWalletEntity
import com.example.ui.theme.CashGreen
import com.example.ui.theme.CoinGold
import com.example.ui.theme.TikTokBlack
import com.example.ui.theme.TikTokMagenta
import com.example.ui.theme.TikTokSurface

@Composable
fun CashoutDialog(
    wallet: UserWalletEntity?,
    onDismiss: () -> Unit,
    onSubmitCashout: (amountUsd: Double, method: String, accountNo: String) -> Unit
) {
    val methods = listOf("bKash 📱", "Nagad 💸", "Bank Transfer 🏦", "PayPal 🌐", "Binance Pay ⚡")
    val defaultAmounts = listOf(5.0, 10.0, 25.0, 50.0, 100.0)

    var selectedMethod by remember { mutableStateOf(methods[0]) }
    var selectedAmount by remember { mutableStateOf(defaultAmounts[1]) }
    var accountNumber by remember { mutableStateOf("01700000000") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp)),
            color = TikTokBlack,
            contentColor = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AccountBalanceWallet,
                            contentDescription = "Wallet",
                            tint = CashGreen,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Cash Out Earnings 💸",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_cashout_dialog_btn")
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Available Balance
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(TikTokSurface)
                        .padding(16.dp)
                ) {
                    Column {
                        Text(text = "Available USD Balance", fontSize = 12.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "$${String.format("%.2f", wallet?.usdBalance ?: 0.0)} USD",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = CashGreen
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Select Method
                Text(text = "Select Payment Method", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.LightGray)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    methods.take(3).forEach { m ->
                        val isSelected = m == selectedMethod
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) TikTokMagenta else TikTokSurface)
                                .clickable { selectedMethod = m }
                                .padding(vertical = 10.dp, horizontal = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = m,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Select Amount
                Text(text = "Select Cashout Amount", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.LightGray)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    defaultAmounts.forEach { amt ->
                        val isSelected = amt == selectedAmount
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) CoinGold else TikTokSurface)
                                .clickable { selectedAmount = amt }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$${amt.toInt()}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.Black else Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Account Number / Wallet Address Input
                Text(text = "Account / Mobile Number", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.LightGray)
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = accountNumber,
                    onValueChange = { accountNumber = it },
                    placeholder = { Text("e.g. 017xxxxxxxx", color = Color.Gray) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("cashout_account_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = TikTokSurface,
                        unfocusedContainerColor = TikTokSurface,
                        focusedBorderColor = CashGreen,
                        unfocusedBorderColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Submit Button
                Button(
                    onClick = {
                        onSubmitCashout(selectedAmount, selectedMethod, accountNumber)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("submit_cashout_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = CashGreen),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text(
                        text = "Withdraw $$selectedAmount USD Now 🚀",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
        }
    }
}
