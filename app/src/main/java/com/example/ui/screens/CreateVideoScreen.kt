package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import com.example.ui.theme.CoinGold
import com.example.ui.theme.TikTokBlack
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokMagenta
import com.example.ui.theme.TikTokSurface
import com.example.ui.viewmodel.TokTokViewModel

@Composable
fun CreateVideoScreen(
    viewModel: TokTokViewModel
) {
    var descriptionText by remember { mutableStateOf("Testing my new video! Earn free coins by watching ⚡ #TokTok #Viral #CreatorFund") }
    var musicTitle by remember { mutableStateOf("Original Sound - Roni") }
    var enableGifts by remember { mutableStateOf(true) }

    val colorOptions = listOf(
        Pair("#8A2387", "#E94057"),
        Pair("#000428", "#004e92"),
        Pair("#11998e", "#38ef7d"),
        Pair("#FF4E50", "#F9D423"),
        Pair("#4568DC", "#B06AB3")
    )
    var selectedColorIndex by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TikTokBlack)
            .padding(top = 44.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Create Video 🎥", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(TikTokSurface)
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.MusicNote, contentDescription = null, tint = TikTokCyan, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(musicTitle, fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Camera Viewfinder Preview Frame
        val activePair = colorOptions[selectedColorIndex]
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(
                            parseHexColor(activePair.first, Color.DarkGray),
                            parseHexColor(activePair.second, Color.Black)
                        )
                    )
                )
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.align(Alignment.TopEnd),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(Icons.Default.Cameraswitch, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = TikTokCyan, modifier = Modifier.size(24.dp))
            }

            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .border(4.dp, TikTokMagenta, CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Videocam, contentDescription = "Record", tint = TikTokMagenta, modifier = Modifier.size(32.dp))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("Tap to Record / Preview", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Select Theme Gradient Palette
        Text("Select Visual Gradient Theme", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.LightGray)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            colorOptions.forEachIndexed { index, pair ->
                val isSelected = index == selectedColorIndex
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(parseHexColor(pair.first, Color.Gray), parseHexColor(pair.second, Color.Black))
                            )
                        )
                        .border(
                            width = if (isSelected) 3.dp else 0.dp,
                            color = if (isSelected) CoinGold else Color.Transparent,
                            shape = CircleShape
                        )
                        .clickable { selectedColorIndex = index }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Caption Input
        Text("Caption & Hashtags", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.LightGray)
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = descriptionText,
            onValueChange = { descriptionText = it },
            placeholder = { Text("Describe your video...", color = Color.Gray) },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .testTag("video_caption_input"),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = TikTokSurface,
                unfocusedContainerColor = TikTokSurface,
                focusedBorderColor = TikTokMagenta,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Creator Fund & Gifts Toggle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(TikTokSurface)
                .padding(horizontal = 14.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CardGiftcard, contentDescription = null, tint = CoinGold, modifier = Modifier.size(22.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text("Allow Virtual Gifts & Monetization", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("Viewers can send Roses, Rockets, and Crowns", fontSize = 11.sp, color = Color.Gray)
                }
            }
            Switch(
                checked = enableGifts,
                onCheckedChange = { enableGifts = it },
                colors = SwitchDefaults.colors(checkedThumbColor = TikTokMagenta, checkedTrackColor = TikTokMagenta.copy(alpha = 0.4f))
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Post Button
        Button(
            onClick = {
                viewModel.uploadNewVideo(
                    description = descriptionText,
                    musicTitle = musicTitle,
                    colorStart = activePair.first,
                    colorEnd = activePair.second
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("post_video_btn"),
            colors = ButtonDefaults.buttonColors(containerColor = TikTokMagenta),
            shape = RoundedCornerShape(25.dp)
        ) {
            Text("Post Video & Start Earning 🚀", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}
