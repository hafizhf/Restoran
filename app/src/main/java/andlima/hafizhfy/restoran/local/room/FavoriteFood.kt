package andlima.hafizhfy.restoran.local.room

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class FavoriteFood (
    @PrimaryKey(autoGenerate = false)
    var id: Int?,

    @ColumnInfo(name = "nama_makanan")
    val nama_makanan: String?,

    @ColumnInfo(name = "harga")
    val harga: String?,

    @ColumnInfo(name = "gambar")
    val gambar: String?,

    @ColumnInfo(name = "deskripsi")
    val deskripsi: String?,

    @ColumnInfo(name = "jumlah", defaultValue = "1")
    val jumlah: String?,

    @ColumnInfo(name = "ownerId")
    val ownerId: String?
) : Parcelable