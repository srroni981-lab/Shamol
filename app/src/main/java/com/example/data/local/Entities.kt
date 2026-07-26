package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "videos")
data class VideoItemEntity(
    @PrimaryKey val id: String,
    val creatorHandle: String,
    val creatorName: String,
    val creatorAvatarUrl: String,
    val videoDescription: String,
    val musicTitle: String,
    val musicAuthor: String,
    val videoThumbnailUrl: String,
    val gradientColorHexStart: String,
    val gradientColorHexEnd: String,
    var likesCount: Long,
    var commentsCount: Long,
    var sharesCount: Long,
    var giftsCount: Long,
    var isLiked: Boolean = false,
    var isSaved: Boolean = false,
    var isFollowing: Boolean = false,
    val isSponsored: Boolean = false,
    val coinReward: Int = 10,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "comments")
data class CommentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val videoId: String,
    val userHandle: String,
    val userName: String,
    val userAvatarUrl: String,
    val commentText: String,
    val likesCount: Int = 0,
    val isLiked: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_wallet")
data class UserWalletEntity(
    @PrimaryKey val userId: String = "me",
    val userHandle: String = "@creator_pro",
    val userName: String = "Alex Rivers",
    val userAvatarUrl: String = "",
    val coinsBalance: Long = 2850,
    val usdBalance: Double = 14.25,
    val totalGiftsSent: Int = 18,
    val totalGiftsReceived: Int = 124,
    val watchEarnedCoins: Long = 450,
    val dailyStreakDays: Int = 5,
    val lastCheckInDate: Long = System.currentTimeMillis()
)

@Entity(tableName = "earning_transactions")
data class EarningTransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val category: String, // "GIFT_SENT", "GIFT_RECEIVED", "WATCH_EARN", "DAILY_CHECKIN", "CASHOUT"
    val coinsChange: Long,
    val usdChange: Double,
    val status: String, // "COMPLETED", "PENDING"
    val timestamp: Long = System.currentTimeMillis()
)
