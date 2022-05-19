package andlima.hafizhfy.restoran.model.menurestaurant

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PutFood(
    @SerializedName("deskripsi")
    val deskripsi: String,
    @SerializedName("gambar")
    val gambar: String,
    @SerializedName("harga")
    val harga: String,
    @SerializedName("nama_makanan")
    val namaMakanan: String
) : Parcelable
