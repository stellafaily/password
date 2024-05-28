package com.example.encryptionmemo.model.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.encryptionmemo.model.database.MemoData.Companion.INDEX
import com.example.encryptionmemo.model.database.MemoData.Companion.MEMO_TABLE_NAME
import kotlinx.android.parcel.Parcelize

@Entity(
    tableName = MEMO_TABLE_NAME,
    indices = [Index(value = [INDEX])])
@Parcelize
data class MemoData(@ColumnInfo(name = "hasEnc") var hasEnc: Boolean = true,
                    @ColumnInfo(name = "title") var title: String = "",
                    @ColumnInfo(name = "editedAt") var editedAt: Long = 0L,
                    @ColumnInfo(name = "backgroundId") var backgroundId: Int = 0,
                    @ColumnInfo(name = "openData") var openData: String = "",
                    @ColumnInfo(name = "encData") var encData: ByteArray?,
                    @ColumnInfo(name = "encData2") var encData2: ByteArray?,
                    @ColumnInfo(name = "upVersion") var isChecked: Boolean = true
): Parcelable {
    @PrimaryKey(autoGenerate = true) var index: Int = 0

    constructor() : this(true, "", 0L, 0, "", null, null,true)
    companion object{
        const val MEMO_TABLE_NAME = "tableMemo"
        const val INDEX = "index"
    }
}