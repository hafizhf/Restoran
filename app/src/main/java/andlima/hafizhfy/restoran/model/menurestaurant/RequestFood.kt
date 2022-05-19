package andlima.hafizhfy.restoran.model.menurestaurant

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RequestFood(
    @SerializedName("deskripsi")
    val deskripsi: String,
    @SerializedName("harga")
    val harga: String,
    @SerializedName("nama_makanan")
    val namaMakanan: String
) : Parcelable
