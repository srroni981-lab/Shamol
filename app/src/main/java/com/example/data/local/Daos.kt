package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoDao {
    @Query("SELECT * FROM videos ORDER BY id ASC")
    fun getAllVideos(): Flow<List<VideoItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideo(video: VideoItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(videos: List<VideoItemEntity>)

    @Update
    suspend fun updateVideo(video: VideoItemEntity)

    @Query("UPDATE videos SET likesCount = likesCount + :delta, isLiked = :isLiked WHERE id = :videoId")
    suspend fun updateLikeStatus(videoId: String, isLiked: Boolean, delta: Int)

    @Query("UPDATE videos SET isSaved = :isSaved WHERE id = :videoId")
    suspend fun updateSaveStatus(videoId: String, isSaved: Boolean)

    @Query("UPDATE videos SET isFollowing = :isFollowing WHERE creatorHandle = :creatorHandle")
    suspend fun updateFollowStatus(creatorHandle: String, isFollowing: Boolean)

    @Query("UPDATE videos SET giftsCount = giftsCount + :giftsAmount WHERE id = :videoId")
    suspend fun incrementGifts(videoId: String, giftsAmount: Int)
}

@Dao
interface CommentDao {
    @Query("SELECT * FROM comments WHERE videoId = :videoId ORDER BY timestamp DESC")
    fun getCommentsForVideo(videoId: String): Flow<List<CommentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(comment: CommentEntity)
}

@Dao
interface WalletDao {
    @Query("SELECT * FROM user_wallet WHERE userId = 'me'")
    fun getUserWallet(): Flow<UserWalletEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateWallet(wallet: UserWalletEntity)

    @Query("UPDATE user_wallet SET coinsBalance = coinsBalance + :coinsDelta, usdBalance = usdBalance + :usdDelta WHERE userId = 'me'")
    suspend fun addCoinsAndUsd(coinsDelta: Long, usdDelta: Double)

    @Query("UPDATE user_wallet SET coinsBalance = coinsBalance - :coinsDelta WHERE userId = 'me' AND coinsBalance >= :coinsDelta")
    suspend fun deductCoins(coinsDelta: Long): Int
}

@Dao
interface TransactionDao {
    @Query("SELECT * FROM earning_transactions ORDER BY timestamp DESC")
    fun getAllTransactions(): Flow<List<EarningTransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: EarningTransactionEntity)
}
