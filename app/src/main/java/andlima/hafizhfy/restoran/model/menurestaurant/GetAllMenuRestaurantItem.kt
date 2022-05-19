package andlima.hafizhfy.restoran.model.menurestaurant


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GetAllMenuRestaurantItem(
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("deskripsi")
    val deskripsi: String,
    @SerializedName("gambar")
    val gambar: String,
    @SerializedName("harga")
    val harga: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("nama_makanan")
    val namaMakanan: String
) : Parcelable