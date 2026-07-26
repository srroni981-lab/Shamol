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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Fireplace
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.example.data.local.VideoItemEntity
import com.example.ui.theme.CashGreen
import com.example.ui.theme.CoinGold
import com.example.ui.theme.TikTokBlack
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokMagenta
import com.example.ui.theme.TikTokSurface
import com.example.ui.viewmodel.TokTokViewModel

@Composable
fun DiscoverScreen(
    viewModel: TokTokViewModel,
    videos: List<VideoItemEntity>
) {
    var searchQuery by remember { mutableStateOf("") }
    val trendingTags = listOf("#CreatorFund", "#DanceChallenge", "#WatchToEarn", "#TokTokLive", "#FunnyMemes")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TikTokBlack)
            .padding(top = 44.dp, start = 16.dp, end = 16.dp)
    ) {
        // Search Header
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search creators, videos, sounds...", color = Color.Gray, fontSize = 14.sp) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.Gray) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("discover_search_input"),
            shape = RoundedCornerShape(24.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = TikTokSurface,
                unfocusedContainerColor = TikTokSurface,
                focusedBorderColor = TikTokCyan,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Banner: Creator Fund Earning Campaign
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(Color(0xFF8A2387), Color(0xFFE94057), Color(0xFFF27121))
                    )
                )
                .clickable { viewModel.toggleMonetizationCenter(true) }
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = CoinGold, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("CREATOR FUND 2026", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = CoinGold)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Earn up to $500 Weekly!", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("Post videos & receive virtual gifts from viewers", fontSize = 12.sp, color = Color.White.copy(alpha = 0.9f))
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text("Join Now", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Trending Hashtags Row
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.TrendingUp, contentDescription = null, tint = TikTokCyan, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Trending Topics", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(trendingTags) { tag ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(TikTokSurface)
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(text = tag, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TikTokCyan)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Popular Short Videos Grid
        Text("Popular Videos", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Spacer(modifier = Modifier.height(10.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(videos) { video ->
                Box(
                    modifier = Modifier
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp))
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

                    // Top Coin Reward Badge
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.Black.copy(alpha = 0.7f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = CoinGold, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(2.dp))
                            Text("+${video.coinReward}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CoinGold)
                        }
                    }

                    // Bottom Overlay Stats
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = video.creatorHandle,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                            Text(formatCount(video.likesCount), fontSize = 11.sp, color = Color.LightGray)
                        }
                    }
                }
            }
        }
    }
}
