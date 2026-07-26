package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.CommentEntity
import com.example.data.local.EarningTransactionEntity
import com.example.data.local.UserWalletEntity
import com.example.data.local.VideoItemEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class TokTokRepository(private val db: AppDatabase) {

    val allVideos: Flow<List<VideoItemEntity>> = db.videoDao().getAllVideos()
    val userWallet: Flow<UserWalletEntity?> = db.walletDao().getUserWallet()
    val transactions: Flow<List<EarningTransactionEntity>> = db.transactionDao().getAllTransactions()

    fun getCommentsForVideo(videoId: String): Flow<List<CommentEntity>> {
        return db.commentDao().getCommentsForVideo(videoId)
    }

    suspend fun initializePrepopulatedDataIfNeeded() {
        val existingVideos = db.videoDao().getAllVideos().firstOrNull()
        if (existingVideos.isNullOrEmpty()) {
            val sampleVideos = listOf(
                VideoItemEntity(
                    id = "v1",
                    creatorHandle = "@cyber_dancer",
                    creatorName = "Sora Cyber",
                    creatorAvatarUrl = "res/drawable/img_creator_avatar1_1785078463372.jpg",
                    videoDescription = "New viral synthwave shuffle dance routine! 🔥 Try this step and earn 50 Coins! #TokTokTrend #DanceChallenge #Viral",
                    musicTitle = "Neon Pulse (Original Mix)",
                    musicAuthor = "Synthetix",
                    videoThumbnailUrl = "res/drawable/img_video_thumb1_1785078481096.jpg",
                    gradientColorHexStart = "#8A2387",
                    gradientColorHexEnd = "#E94057",
                    likesCount = 142500,
                    commentsCount = 3820,
                    sharesCount = 12400,
                    giftsCount = 520,
                    isLiked = false,
                    isSponsored = false,
                    coinReward = 15
                ),
                VideoItemEntity(
                    id = "v2",
                    creatorHandle = "@tech_mira",
                    creatorName = "Mira Code Studio",
                    creatorAvatarUrl = "",
                    videoDescription = "How to build an Earning Video App on Android with Jetpack Compose 🚀💻 Send gifts to support my tutorials!",
                    musicTitle = "Lofi Code Beats Vol. 4",
                    musicAuthor = "ChillHop Beats",
                    videoThumbnailUrl = "",
                    gradientColorHexStart = "#000428",
                    gradientColorHexEnd = "#004e92",
                    likesCount = 89200,
                    commentsCount = 1940,
                    sharesCount = 5620,
                    giftsCount = 890,
                    isLiked = true,
                    isSponsored = false,
                    coinReward = 20
                ),
                VideoItemEntity(
                    id = "v3",
                    creatorHandle = "@sponsored_brand",
                    creatorName = "TokTok Creator Fund",
                    creatorAvatarUrl = "",
                    videoDescription = "⚡ SPONSORED: Watch & Share videos daily to win up to $100 Cash directly in your Wallet! Tap Gift to boost payout rates.",
                    musicTitle = "Creator Rewards Anthem 2026",
                    musicAuthor = "TokTok Official",
                    videoThumbnailUrl = "",
                    gradientColorHexStart = "#11998e",
                    gradientColorHexEnd = "#38ef7d",
                    likesCount = 320100,
                    commentsCount = 8740,
                    sharesCount = 34100,
                    giftsCount = 1250,
                    isLiked = false,
                    isSponsored = true,
                    coinReward = 50
                ),
                VideoItemEntity(
                    id = "v4",
                    creatorHandle = "@funny_billi",
                    creatorName = "Cat Vibes Studio",
                    creatorAvatarUrl = "",
                    videoDescription = "When the WiFi disconnects right as you were about to withdraw your earnings 😂😹 #CatMemes #Funny #Relatable",
                    musicTitle = "Funny Cat Whistle Theme",
                    musicAuthor = "Meme Audio",
                    videoThumbnailUrl = "",
                    gradientColorHexStart = "#FF4E50",
                    gradientColorHexEnd = "#F9D423",
                    likesCount = 512000,
                    commentsCount = 12400,
                    sharesCount = 45000,
                    giftsCount = 2100,
                    isLiked = false,
                    isSponsored = false,
                    coinReward = 10
                )
            )
            db.videoDao().insertAll(sampleVideos)

            // Seed initial comments
            db.commentDao().insertComment(
                CommentEntity(videoId = "v1", userHandle = "@dance_king", userName = "Robi", userAvatarUrl = "", commentText = "Insane energy! Sent 5 Roses 🌹🔥", likesCount = 42)
            )
            db.commentDao().insertComment(
                CommentEntity(videoId = "v1", userHandle = "@rahul_vlogs", userName = "Rahul", userAvatarUrl = "", commentText = "Just cashed out my $10 earnings! This app really pays 👌", likesCount = 128)
            )
            db.commentDao().insertComment(
                CommentEntity(videoId = "v2", userHandle = "@dev_sam", userName = "Sam", userAvatarUrl = "", commentText = "Loved the Jetpack Compose setup tutorial! Subbed!", likesCount = 19)
            )
        }

        val existingWallet = db.walletDao().getUserWallet().firstOrNull()
        if (existingWallet == null) {
            db.walletDao().insertOrUpdateWallet(
                UserWalletEntity(
                    userId = "me",
                    userHandle = "@my_toktok_pro",
                    userName = "Roni Bangladesh",
                    coinsBalance = 1500,
                    usdBalance = 7.50,
                    totalGiftsSent = 5,
                    totalGiftsReceived = 32,
                    watchEarnedCoins = 250,
                    dailyStreakDays = 3
                )
            )

            // Seed initial transaction
            db.transactionDao().insertTransaction(
                EarningTransactionEntity(
                    title = "Daily Check-in Reward",
                    category = "DAILY_CHECKIN",
                    coinsChange = 100,
                    usdChange = 0.50,
                    status = "COMPLETED"
                )
            )
        }
    }

    suspend fun toggleLike(video: VideoItemEntity) {
        val newIsLiked = !video.isLiked
        val delta = if (newIsLiked) 1 else -1
        db.videoDao().updateLikeStatus(video.id, newIsLiked, delta)
    }

    suspend fun toggleSave(video: VideoItemEntity) {
        db.videoDao().updateSaveStatus(video.id, !video.isSaved)
    }

    suspend fun toggleFollow(creatorHandle: String, currentFollowStatus: Boolean) {
        db.videoDao().updateFollowStatus(creatorHandle, !currentFollowStatus)
    }

    suspend fun addComment(videoId: String, commentText: String) {
        val comment = CommentEntity(
            videoId = videoId,
            userHandle = "@my_toktok_pro",
            userName = "Roni Bangladesh",
            userAvatarUrl = "",
            commentText = commentText,
            likesCount = 0
        )
        db.commentDao().insertComment(comment)
    }

    suspend fun sendGiftToCreator(video: VideoItemEntity, giftName: String, giftCoinsCost: Long): Boolean {
        val currentWallet = db.walletDao().getUserWallet().firstOrNull() ?: return false
        if (currentWallet.coinsBalance >= giftCoinsCost) {
            // Deduct coins
            val updated = db.walletDao().deductCoins(giftCoinsCost)
            if (updated > 0) {
                // Increment video gift counter
                db.videoDao().incrementGifts(video.id, 1)

                // Record transaction
                db.transactionDao().insertTransaction(
                    EarningTransactionEntity(
                        title = "Sent $giftName to ${video.creatorHandle}",
                        category = "GIFT_SENT",
                        coinsChange = -giftCoinsCost,
                        usdChange = -(giftCoinsCost * 0.005),
                        status = "COMPLETED"
                    )
                )
                return true
            }
        }
        return false
    }

    suspend fun awardWatchCoins(coinsAmount: Long, reason: String = "Watch-to-Earn Video Bonus") {
        val usdGain = coinsAmount * 0.005
        db.walletDao().addCoinsAndUsd(coinsAmount, usdGain)
        db.transactionDao().insertTransaction(
            EarningTransactionEntity(
                title = reason,
                category = "WATCH_EARN",
                coinsChange = coinsAmount,
                usdChange = usdGain,
                status = "COMPLETED"
            )
        )
    }

    suspend fun requestCashout(amountUsd: Double, payoutMethod: String, accountNumber: String): Boolean {
        val currentWallet = db.walletDao().getUserWallet().firstOrNull() ?: return false
        if (currentWallet.usdBalance >= amountUsd) {
            val coinsDeduct = (amountUsd / 0.005).toLong()
            db.walletDao().addCoinsAndUsd(-coinsDeduct, -amountUsd)
            db.transactionDao().insertTransaction(
                EarningTransactionEntity(
                    title = "Withdrawal to $payoutMethod ($accountNumber)",
                    category = "CASHOUT",
                    coinsChange = -coinsDeduct,
                    usdChange = -amountUsd,
                    status = "PENDING"
                )
            )
            return true
        }
        return false
    }

    suspend fun uploadVideo(
        description: String,
        musicTitle: String,
        gradientStartHex: String,
        gradientEndHex: String
    ) {
        val newVideo = VideoItemEntity(
            id = "user_v_${System.currentTimeMillis()}",
            creatorHandle = "@my_toktok_pro",
            creatorName = "Roni Bangladesh",
            creatorAvatarUrl = "",
            videoDescription = description,
            musicTitle = musicTitle,
            musicAuthor = "Original Sound - Roni",
            videoThumbnailUrl = "",
            gradientColorHexStart = gradientStartHex,
            gradientColorHexEnd = gradientEndHex,
            likesCount = 0,
            commentsCount = 0,
            sharesCount = 0,
            giftsCount = 0,
            coinReward = 25
        )
        db.videoDao().insertVideo(newVideo)
    }
}

