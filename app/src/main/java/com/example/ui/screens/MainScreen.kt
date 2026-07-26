package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.CashoutDialog
import com.example.ui.components.CommentsBottomSheet
import com.example.ui.components.GiftBottomSheet
import com.example.ui.theme.TikTokBlack
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokMagenta
import com.example.ui.viewmodel.TokTokTab
import com.example.ui.viewmodel.TokTokViewModel

@Composable
fun MainScreen(
    viewModel: TokTokViewModel
) {
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val videos by viewModel.videos.collectAsStateWithLifecycle()
    val wallet by viewModel.userWallet.collectAsStateWithLifecycle()
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()
    val watchProgress by viewModel.watchTimerProgress.collectAsStateWithLifecycle()
    val snackbarMessage by viewModel.snackbarMessage.collectAsStateWithLifecycle()

    val activeGiftVideo by viewModel.activeGiftVideo.collectAsStateWithLifecycle()
    val activeCommentVideo by viewModel.activeCommentVideo.collectAsStateWithLifecycle()
    val activeComments by viewModel.commentsForActiveVideo.collectAsStateWithLifecycle()
    val showCashoutDialog by viewModel.showCashoutDialog.collectAsStateWithLifecycle()
    val showMonetizationCenter by viewModel.showMonetizationCenter.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackbar()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = TikTokBlack,
        bottomBar = {
            TokTokBottomNavigation(
                currentTab = currentTab,
                onSelectTab = { tab -> viewModel.selectTab(tab) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                TokTokTab.HOME -> HomeFeedScreen(
                    viewModel = viewModel,
                    videos = videos,
                    wallet = wallet,
                    watchProgress = watchProgress
                )
                TokTokTab.DISCOVER -> DiscoverScreen(
                    viewModel = viewModel,
                    videos = videos
                )
                TokTokTab.CREATE -> CreateVideoScreen(
                    viewModel = viewModel
                )
                TokTokTab.INBOX -> InboxScreen(
                    transactions = transactions
                )
                TokTokTab.PROFILE -> ProfileScreen(
                    viewModel = viewModel,
                    wallet = wallet,
                    videos = videos
                )
            }

            // Gift Bottom Sheet
            activeGiftVideo?.let { video ->
                GiftBottomSheet(
                    video = video,
                    giftOptions = viewModel.giftOptions,
                    wallet = wallet,
                    onDismiss = { viewModel.closeGiftSheet() },
                    onSendGift = { gift -> viewModel.sendGift(gift) }
                )
            }

            // Comments Bottom Sheet
            activeCommentVideo?.let { video ->
                CommentsBottomSheet(
                    video = video,
                    comments = activeComments,
                    onDismiss = { viewModel.closeComments() },
                    onPostComment = { text -> viewModel.postComment(video.id, text) }
                )
            }

            // Cashout Withdrawal Dialog
            if (showCashoutDialog) {
                CashoutDialog(
                    wallet = wallet,
                    onDismiss = { viewModel.toggleCashoutDialog(false) },
                    onSubmitCashout = { amountUsd, method, accountNo ->
                        viewModel.submitCashoutRequest(amountUsd, method, accountNo)
                    }
                )
            }

            // Monetization Studio Sheet
            if (showMonetizationCenter) {
                MonetizationScreen(
                    wallet = wallet,
                    onDismiss = { viewModel.toggleMonetizationCenter(false) },
                    onOpenCashout = { viewModel.toggleCashoutDialog(true) }
                )
            }
        }
    }
}

@Composable
fun TokTokBottomNavigation(
    currentTab: TokTokTab,
    onSelectTab: (TokTokTab) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(TikTokBlack)
            .navigationBarsPadding()
            .height(60.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Home
        BottomNavItem(
            label = "Home",
            isSelected = currentTab == TokTokTab.HOME,
            activeIcon = Icons.Filled.Home,
            inactiveIcon = Icons.Outlined.Home,
            onClick = { onSelectTab(TokTokTab.HOME) },
            testTag = "nav_home_tab"
        )

        // Discover
        BottomNavItem(
            label = "Discover",
            isSelected = currentTab == TokTokTab.DISCOVER,
            activeIcon = Icons.Filled.Search,
            inactiveIcon = Icons.Outlined.Search,
            onClick = { onSelectTab(TokTokTab.DISCOVER) },
            testTag = "nav_discover_tab"
        )

        // Create (+) Button
        Box(
            modifier = Modifier
                .width(46.dp)
                .height(30.dp)
                .clip(RoundedCornerShape(10.dp))
                .clickable { onSelectTab(TokTokTab.CREATE) }
                .testTag("nav_create_tab"),
            contentAlignment = Alignment.Center
        ) {
            // TikTok style cyan & magenta overlapping button
            Row(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .background(TikTokCyan)
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .background(TikTokMagenta)
                )
            }

            Box(
                modifier = Modifier
                    .width(38.dp)
                    .height(28.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Create Video",
                    tint = Color.Black,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Inbox
        BottomNavItem(
            label = "Inbox",
            isSelected = currentTab == TokTokTab.INBOX,
            activeIcon = Icons.Filled.Inbox,
            inactiveIcon = Icons.Outlined.Inbox,
            onClick = { onSelectTab(TokTokTab.INBOX) },
            testTag = "nav_inbox_tab"
        )

        // Profile
        BottomNavItem(
            label = "Profile",
            isSelected = currentTab == TokTokTab.PROFILE,
            activeIcon = Icons.Filled.Person,
            inactiveIcon = Icons.Outlined.Person,
            onClick = { onSelectTab(TokTokTab.PROFILE) },
            testTag = "nav_profile_tab"
        )
    }
}

@Composable
fun BottomNavItem(
    label: String,
    isSelected: Boolean,
    activeIcon: androidx.compose.ui.graphics.vector.ImageVector,
    inactiveIcon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    testTag: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp)
            .testTag(testTag)
    ) {
        Icon(
            imageVector = if (isSelected) activeIcon else inactiveIcon,
            contentDescription = label,
            tint = if (isSelected) Color.White else Color.Gray,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) Color.White else Color.Gray
        )
    }
}
