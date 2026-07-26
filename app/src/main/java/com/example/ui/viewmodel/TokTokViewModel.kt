package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.CommentEntity
import com.example.data.local.EarningTransactionEntity
import com.example.data.local.UserWalletEntity
import com.example.data.local.VideoItemEntity
import com.example.data.repository.TokTokRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

enum class TokTokTab {
    HOME, DISCOVER, CREATE, INBOX, PROFILE
}

data class GiftItem(
    val id: String,
    val name: String,
    val iconEmoji: String,
    val coinsCost: Long,
    val description: String
)

class TokTokViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TokTokRepository

    private val _currentTab = MutableStateFlow(TokTokTab.HOME)
    val currentTab: StateFlow<TokTokTab> = _currentTab.asStateFlow()

    private val _videos = MutableStateFlow<List<VideoItemEntity>>(emptyList())
    val videos: StateFlow<List<VideoItemEntity>> = _videos.asStateFlow()

    private val _userWallet = MutableStateFlow<UserWalletEntity?>(null)
    val userWallet: StateFlow<UserWalletEntity?> = _userWallet.asStateFlow()

    private val _transactions = MutableStateFlow<List<EarningTransactionEntity>>(emptyList())
    val transactions: StateFlow<List<EarningTransactionEntity>> = _transactions.asStateFlow()

    private val _currentFeedIndex = MutableStateFlow(0)
    val currentFeedIndex: StateFlow<Int> = _currentFeedIndex.asStateFlow()

    // Watch-to-Earn timer state (15s watch cycle)
    private val _watchTimerProgress = MutableStateFlow(0f)
    val watchTimerProgress: StateFlow<Float> = _watchTimerProgress.asStateFlow()

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    // Active modals
    private val _activeCommentVideo = MutableStateFlow<VideoItemEntity?>(null)
    val activeCommentVideo: StateFlow<VideoItemEntity?> = _activeCommentVideo.asStateFlow()

    private val _activeGiftVideo = MutableStateFlow<VideoItemEntity?>(null)
    val activeGiftVideo: StateFlow<VideoItemEntity?> = _activeGiftVideo.asStateFlow()

    private val _commentsForActiveVideo = MutableStateFlow<List<CommentEntity>>(emptyList())
    val commentsForActiveVideo: StateFlow<List<CommentEntity>> = _commentsForActiveVideo.asStateFlow()

    private val _showCashoutDialog = MutableStateFlow(false)
    val showCashoutDialog: StateFlow<Boolean> = _showCashoutDialog.asStateFlow()

    private val _showMonetizationCenter = MutableStateFlow(false)
    val showMonetizationCenter: StateFlow<Boolean> = _showMonetizationCenter.asStateFlow()

    private var watchTimerJob: Job? = null

    val giftOptions = listOf(
        GiftItem("g1", "Rose", "🌹", 1, "Popular budget gift"),
        GiftItem("g2", "Panda", "🐼", 10, "Cute panda dance"),
        GiftItem("g3", "Fireworks", "🎆", 100, "Vibrant sky spark"),
        GiftItem("g4", "Super Rocket", "🚀", 500, "Creator speed boost"),
        GiftItem("g5", "Golden Crown", "👑", 1000, "VIP Creator Crown")
    )

    init {
        val db = AppDatabase.getDatabase(application)
        repository = TokTokRepository(db)

        viewModelScope.launch {
            repository.initializePrepopulatedDataIfNeeded()
        }

        viewModelScope.launch {
            repository.allVideos.collectLatest { list ->
                _videos.value = list
            }
        }

        viewModelScope.launch {
            repository.userWallet.collectLatest { wallet ->
                _userWallet.value = wallet
            }
        }

        viewModelScope.launch {
            repository.transactions.collectLatest { list ->
                _transactions.value = list
            }
        }

        startWatchToEarnTimer()
    }

    fun selectTab(tab: TokTokTab) {
        _currentTab.value = tab
    }

    fun setCurrentFeedIndex(index: Int) {
        _currentFeedIndex.value = index
    }

    private fun startWatchToEarnTimer() {
        watchTimerJob?.cancel()
        watchTimerJob = viewModelScope.launch {
            var progress = 0f
            while (true) {
                delay(100)
                if (_currentTab.value == TokTokTab.HOME && _videos.value.isNotEmpty()) {
                    progress += 0.01f
                    if (progress >= 1.0f) {
                        progress = 0f
                        val rewardCoins = 15L
                        repository.awardWatchCoins(rewardCoins, "Watch-to-Earn Video Bonus")
                        showSnackbar("🎉 +$rewardCoins Coins Earned for watching!")
                    }
                    _watchTimerProgress.value = progress
                }
            }
        }
    }

    fun toggleLike(video: VideoItemEntity) {
        viewModelScope.launch {
            repository.toggleLike(video)
        }
    }

    fun toggleSave(video: VideoItemEntity) {
        viewModelScope.launch {
            repository.toggleSave(video)
        }
    }

    fun toggleFollow(creatorHandle: String, isFollowing: Boolean) {
        viewModelScope.launch {
            repository.toggleFollow(creatorHandle, isFollowing)
            val action = if (isFollowing) "Unfollowed" else "Following"
            showSnackbar("$action $creatorHandle")
        }
    }

    fun openComments(video: VideoItemEntity) {
        _activeCommentVideo.value = video
        viewModelScope.launch {
            repository.getCommentsForVideo(video.id).collectLatest { comments ->
                _commentsForActiveVideo.value = comments
            }
        }
    }

    fun closeComments() {
        _activeCommentVideo.value = null
    }

    fun postComment(videoId: String, text: String) {
        if (text.isBlank()) return
        viewModelScope.launch {
            repository.addComment(videoId, text.trim())
            showSnackbar("Comment posted!")
        }
    }

    fun openGiftSheet(video: VideoItemEntity) {
        _activeGiftVideo.value = video
    }

    fun closeGiftSheet() {
        _activeGiftVideo.value = null
    }

    fun sendGift(gift: GiftItem) {
        val targetVideo = _activeGiftVideo.value ?: return
        viewModelScope.launch {
            val success = repository.sendGiftToCreator(targetVideo, gift.name, gift.coinsCost)
            if (success) {
                showSnackbar("Sent ${gift.iconEmoji} ${gift.name} to ${targetVideo.creatorHandle}!")
                closeGiftSheet()
            } else {
                showSnackbar("Insufficient Coins! Watch videos or top-up.")
            }
        }
    }

    fun toggleCashoutDialog(show: Boolean) {
        _showCashoutDialog.value = show
    }

    fun toggleMonetizationCenter(show: Boolean) {
        _showMonetizationCenter.value = show
    }

    fun submitCashoutRequest(amountUsd: Double, payoutMethod: String, accountNumber: String) {
        viewModelScope.launch {
            val success = repository.requestCashout(amountUsd, payoutMethod, accountNumber)
            if (success) {
                showSnackbar("Cashout Request of $$amountUsd via $payoutMethod Submitted!")
                _showCashoutDialog.value = false
            } else {
                showSnackbar("Insufficient Balance for $$amountUsd cashout.")
            }
        }
    }

    fun uploadNewVideo(description: String, musicTitle: String, colorStart: String, colorEnd: String) {
        viewModelScope.launch {
            repository.uploadVideo(description, musicTitle, colorStart, colorEnd)
            showSnackbar("Video Posted Successfully! Earn Creator Fund Rewards! 🎥✨")
            _currentTab.value = TokTokTab.HOME
        }
    }

    fun showSnackbar(message: String) {
        _snackbarMessage.value = message
    }

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }
}
