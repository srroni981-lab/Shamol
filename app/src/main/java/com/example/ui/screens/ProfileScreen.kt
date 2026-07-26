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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.data.local.VideoItemEntity
import com.example.ui.theme.CashGreen
import com.example.ui.theme.CoinGold
import com.example.ui.theme.TikTokBlack
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokMagenta
import com.example.ui.theme.TikTokSurface
import com.example.ui.viewmodel.TokTokViewModel

@Composable
fun ProfileScreen(
    viewModel: TokTokViewModel,
    wallet: UserWalletEntity?,
    videos: List<VideoItemEntity>
) {
    var selectedVideoTab by remember { mutableStateOf("POSTS") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TikTokBlack)
            .padding(top = 44.dp, start = 16.dp, end = 16.dp)
    ) {
        // User Info Header
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(TikTokSurface),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile Avatar",
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = wallet?.userName ?: "Roni Bangladesh",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(Icons.Default.Verified, contentDescription = "Verified", tint = TikTokCyan, modifier = Modifier.size(18.dp))
            }

            Text(
                text = wallet?.userHandle ?: "@my_toktok_pro",
                fontSize = 13.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Followers Stats Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ProfileStat("142", "Following")
                ProfileStat("28.4K", "Followers")
                ProfileStat("152K", "Likes")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // CREATOR WALLET & EARNING HUB CARD (The requested Earning Feature!)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF2A2A38), Color(0xFF1A1A24))
                    )
                )
                .padding(16.dp)
        ) {
            Column {
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
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Creator Wallet & Earnings",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(CoinGold.copy(alpha = 0.2f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Level 3 Creator",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = CoinGold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Coins Balance", fontSize = 11.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = CoinGold, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${wallet?.coinsBalance ?: 0} 🪙",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = CoinGold
                            )
                        }
                    }

                    Column {
                        Text("Available Cash (USD)", fontSize = 11.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "$${String.format("%.2f", wallet?.usdBalance ?: 0.0)} USD",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = CashGreen
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Action Buttons: Cashout / Withdraw & Analytics
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { viewModel.toggleCashoutDialog(true) },
                        modifier = Modifier
                            .weight(1f)
                            .height(42.dp)
                            .testTag("withdraw_cashout_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = CashGreen),
                        shape = RoundedCornerShape(21.dp)
                    ) {
                        Text("Withdraw Cash 💸", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    }

                    Button(
                        onClick = { viewModel.toggleMonetizationCenter(true) },
                        modifier = Modifier
                            .weight(1f)
                            .height(42.dp)
                            .testTag("monetization_analytics_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = TikTokMagenta),
                        shape = RoundedCornerShape(21.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Analytics, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Creator Studio", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Videos Tabs (My Videos | Liked | Bookmarked)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            IconButton(onClick = { selectedVideoTab = "POSTS" }) {
                Icon(Icons.Default.GridOn, contentDescription = "Posts", tint = if (selectedVideoTab == "POSTS") TikTokCyan else Color.Gray)
            }
            IconButton(onClick = { selectedVideoTab = "LIKED" }) {
                Icon(Icons.Default.Favorite, contentDescription = "Liked", tint = if (selectedVideoTab == "LIKED") TikTokMagenta else Color.Gray)
            }
            IconButton(onClick = { selectedVideoTab = "SAVED" }) {
                Icon(Icons.Default.Bookmark, contentDescription = "Saved", tint = if (selectedVideoTab == "SAVED") CoinGold else Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        val displayVideos = when (selectedVideoTab) {
            "LIKED" -> videos.filter { it.isLiked }
            "SAVED" -> videos.filter { it.isSaved }
            else -> videos.filter { it.creatorHandle == "@my_toktok_pro" || it.creatorHandle == "@cyber_dancer" }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(displayVideos) { video ->
                Box(
                    modifier = Modifier
                        .height(140.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(TikTokSurface)
                        .clickable { viewModel.selectTab(com.example.ui.viewmodel.TokTokTab.HOME) }
                ) {
                    val colorStart = parseHexColor(video.gradientColorHexStart, Color(0xFF2A2A35))
                    val colorEnd = parseHexColor(video.gradientColorHexEnd, Color(0xFF121216))

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Brush.verticalGradient(listOf(colorStart, colorEnd)))
                    )

                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                        Text(formatCount(video.likesCount), fontSize = 10.sp, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileStat(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text(label, fontSize = 11.sp, color = Color.Gray)
    }
}
