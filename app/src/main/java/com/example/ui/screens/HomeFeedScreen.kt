package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.data.local.UserWalletEntity
import com.example.data.local.VideoItemEntity
import com.example.ui.theme.CoinGold
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokMagenta
import com.example.ui.theme.TikTokSurface
import com.example.ui.viewmodel.TokTokViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomeFeedScreen(
    viewModel: TokTokViewModel,
    videos: List<VideoItemEntity>,
    wallet: UserWalletEntity?,
    watchProgress: Float
) {
    val pagerState = rememberPagerState(pageCount = { videos.size })
    var feedTabState by remember { mutableStateOf("FOR_YOU") }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            viewModel.setCurrentFeedIndex(page)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        if (videos.isNotEmpty()) {
            VerticalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                VideoFeedPage(
                    video = videos[page],
                    viewModel = viewModel
                )
            }
        }

        // Top Navigation Bar (Following | For You + Watch to Earn floating badge)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 44.dp, start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Watch-to-Earn Timer Pill on top left
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Black.copy(alpha = 0.6f))
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(20.dp)) {
                    CircularProgressIndicator(
                        progress = { watchProgress },
                        modifier = Modifier.fillMaxSize(),
                        color = CoinGold,
                        trackColor = Color.White.copy(alpha = 0.2f),
                        strokeWidth = 2.5.dp
                    )
                    Icon(
                        imageVector = Icons.Default.MonetizationOn,
                        contentDescription = "Coin",
                        tint = CoinGold,
                        modifier = Modifier.size(12.dp)
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${wallet?.coinsBalance ?: 0} 🪙",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = CoinGold
                )
            }

            // Feed Tabs (Following | For You)
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Following",
                    fontSize = 16.sp,
                    fontWeight = if (feedTabState == "FOLLOWING") FontWeight.Bold else FontWeight.Normal,
                    color = if (feedTabState == "FOLLOWING") Color.White else Color.Gray,
                    modifier = Modifier.clickable { feedTabState = "FOLLOWING" }
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "For You",
                    fontSize = 16.sp,
                    fontWeight = if (feedTabState == "FOR_YOU") FontWeight.Bold else FontWeight.Normal,
                    color = if (feedTabState == "FOR_YOU") Color.White else Color.Gray,
                    modifier = Modifier.clickable { feedTabState = "FOR_YOU" }
                )
            }

            // Empty spacer for symmetry
            Spacer(modifier = Modifier.width(60.dp))
        }
    }
}

@Composable
fun VideoFeedPage(
    video: VideoItemEntity,
    viewModel: TokTokViewModel
) {
    var isDoubleTapHeartVisible by remember { mutableStateOf(false) }
    var doubleTapOffset by remember { mutableStateOf(Offset.Zero) }
    val coroutineScope = rememberCoroutineScope()

    // Rotating vinyl transition
    val infiniteTransition = rememberInfiniteTransition(label = "vinyl")
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = { offset ->
                        doubleTapOffset = offset
                        isDoubleTapHeartVisible = true
                        if (!video.isLiked) {
                            viewModel.toggleLike(video)
                        }
                        coroutineScope.launch {
                            delay(800)
                            isDoubleTapHeartVisible = false
                        }
                    }
                )
            }
    ) {
        // Video Surface Background (Custom rich gradient or generated video thumb)
        if (video.videoThumbnailUrl.isNotEmpty()) {
            AsyncImage(
                model = parseDrawableRes(video.videoThumbnailUrl),
                contentDescription = "Video preview",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            // High-fidelity animated canvas background for video simulation
            val colorStart = parseHexColor(video.gradientColorHexStart, Color(0xFF1E1E24))
            val colorEnd = parseHexColor(video.gradientColorHexEnd, Color(0xFF121216))

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(colorStart, colorEnd, Color.Black)
                        )
                    )
            )
        }

        // Simulated video progress bar at bottom edge
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(3.dp)
                .background(Color.White.copy(alpha = 0.3f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.45f)
                    .height(3.dp)
                    .background(TikTokMagenta)
            )
        }

        // Double-tap heart animation overlay
        if (isDoubleTapHeartVisible) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Liked",
                tint = TikTokMagenta,
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.Center)
            )
        }

        // Overlay: Sponsored Badge if sponsored
        if (video.isSponsored) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 90.dp, start = 16.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(TikTokMagenta.copy(alpha = 0.9f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Sponsored • 2x Watch Rewards",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // Creator Bottom Overlay (Handle, description, music ticker)
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth(0.78f)
                .padding(start = 16.dp, bottom = 80.dp)
        ) {
            // Creator handle & Follow button
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = video.creatorHandle,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = video.creatorName,
                    fontSize = 13.sp,
                    color = Color.LightGray
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Caption Description
            Text(
                text = video.videoDescription,
                fontSize = 14.sp,
                color = Color.White,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Music Sound Ticker
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = "Music",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${video.musicTitle} - ${video.musicAuthor}",
                    fontSize = 13.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Engagement Sidebar on Right Side
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 12.dp, bottom = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Creator Avatar with + Follow Button
            Box(contentAlignment = Alignment.BottomCenter) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .border(1.5.dp, Color.White, CircleShape)
                        .background(TikTokSurface),
                    contentAlignment = Alignment.Center
                ) {
                    if (video.creatorAvatarUrl.isNotEmpty()) {
                        AsyncImage(
                            model = parseDrawableRes(video.creatorAvatarUrl),
                            contentDescription = "Avatar",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Avatar",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                // Follow + Badge
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(if (video.isFollowing) TikTokCyan else TikTokMagenta)
                        .clickable {
                            viewModel.toggleFollow(video.creatorHandle, video.isFollowing)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (video.isFollowing) Icons.Default.Check else Icons.Default.Add,
                        contentDescription = "Follow",
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            // Like Button
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(
                    onClick = { viewModel.toggleLike(video) },
                    modifier = Modifier
                        .size(44.dp)
                        .testTag("like_button_${video.id}")
                ) {
                    Icon(
                        imageVector = if (video.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Like",
                        tint = if (video.isLiked) TikTokMagenta else Color.White,
                        modifier = Modifier.size(34.dp)
                    )
                }
                Text(
                    text = formatCount(video.likesCount),
                    fontSize = 12.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            // Comment Button
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(
                    onClick = { viewModel.openComments(video) },
                    modifier = Modifier
                        .size(44.dp)
                        .testTag("comment_button_${video.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Comment,
                        contentDescription = "Comment",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Text(
                    text = formatCount(video.commentsCount),
                    fontSize = 12.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            // Bookmark / Save Button
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(
                    onClick = { viewModel.toggleSave(video) },
                    modifier = Modifier.size(44.dp)
                ) {
                    Icon(
                        imageVector = if (video.isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Save",
                        tint = if (video.isSaved) CoinGold else Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Text(
                    text = if (video.isSaved) "Saved" else "Save",
                    fontSize = 12.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }

            // Send Virtual Gift Button (TikTok Earning feature!)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(TikTokMagenta, CoinGold)
                            )
                        )
                        .clickable { viewModel.openGiftSheet(video) }
                        .testTag("gift_button_${video.id}"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CardGiftcard,
                        contentDescription = "Gift",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Gift 🎁",
                    fontSize = 12.sp,
                    color = CoinGold,
                    fontWeight = FontWeight.Bold
                )
            }

            // Rotating Vinyl Disc with music sound icon
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .rotate(rotationAngle)
                    .clip(CircleShape)
                    .background(Color.Black)
                    .border(10.dp, Color(0xFF222222), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = "Record",
                    tint = TikTokCyan,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

fun formatCount(count: Long): String {
    return when {
        count >= 1_000_000 -> String.format("%.1fM", count / 1_000_000.0)
        count >= 1_000 -> String.format("%.1fK", count / 1_000.0)
        else -> count.toString()
    }
}

fun parseDrawableRes(path: String): Int {
    if (path.contains("img_creator_avatar1")) {
        return R.drawable.img_creator_avatar1_1785078463372
    }
    if (path.contains("img_video_thumb1")) {
        return R.drawable.img_video_thumb1_1785078481096
    }
    return R.drawable.ic_launcher_foreground
}

fun parseHexColor(hex: String, default: Color): Color {
    return try {
        Color(android.graphics.Color.parseColor(hex))
    } catch (e: Exception) {
        default
    }
}
