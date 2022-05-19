package andlima.hafizhfy.restoran.view.main.adapter

import andlima.hafizhfy.restoran.R
import andlima.hafizhfy.restoran.local.room.FavoriteFood
import andlima.hafizhfy.restoran.model.menurestaurant.GetAllMenuRestaurantItem
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_cart.view.*

class AdapterFoodFavorite(
    private var onClick: (GetAllMenuRestaurantItem) -> Unit
) : RecyclerView.Adapter<AdapterFoodFavorite.ViewHolder>() {

    private var foodData: List<FavoriteFood>? = null

    fun setFavoriteFoodList(foodList: List<FavoriteFood>) {
        this.foodData = foodList
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return ViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView.context).load(foodData!![position].gambar)
            .into(holder.itemView.iv_thumbnail_makanan)
        holder.itemView.tv_nama_makanan.text = foodData!![position].nama_makanan
        holder.itemView.tv_harga.text = "Rp " + foodData!![position].harga

        holder.itemView.item.setOnClickListener {
            val matchWithApiModel = GetAllMenuRestaurantItem(
                "",
                foodData!![position].deskripsi!!,
                foodData!![position].gambar!!,
                foodData!![position].harga!!,
                "",
                foodData!![position].nama_makanan!!
            )

            onClick(matchWithApiModel)
        }

        holder.itemView.btn_increase_amount.setOnClickListener {
            var currentAmount = holder.itemView.et_amount.text.toString().toInt()
            currentAmount++
            holder.itemView.et_amount.setText(currentAmount.toString())
            if (currentAmount > 1) {
                holder.itemView.btn_decrease_amount.setTextColor(R.color.default_grey)
            }
        }

        holder.itemView.btn_decrease_amount.setOnClickListener {
            var currentAmount = holder.itemView.et_amount.text.toString().toInt()
            currentAmount--
            if (currentAmount > 1) {
                holder.itemView.et_amount.setText(currentAmount.toString())
            }

            if (currentAmount == 1) {
                holder.itemView.et_amount.setText(currentAmount.toString())
                holder.itemView.btn_decrease_amount.setTextColor(R.color.white)
            }
        }
    }

    override fun getItemCount(): Int {
        return if (foodData != null) {
            foodData!!.size
        } else {
            0
        }
    }


}