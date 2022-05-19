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
import kotlinx.android.synthetic.main.item_food.view.*

class AdapterFood(private var onClick: (GetAllMenuRestaurantItem)->Unit)
    : RecyclerView.Adapter<AdapterFood.ViewHolder>() {

    private var foodData: List<GetAllMenuRestaurantItem>? = null
    private var foodDataFav: List<FavoriteFood>? = null

    fun setFoodList(foodList: List<GetAllMenuRestaurantItem>) {
        this.foodData = foodList
    }

    fun setFoodListFav(foodList: List<FavoriteFood>) {
        this.foodDataFav = foodList
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterFood.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_food, parent, false)
        return ViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AdapterFood.ViewHolder, position: Int) {

        Glide.with(holder.itemView.context).load(foodData!![position].gambar)
            .into(holder.itemView.iv_thumbnail_makanan)

        holder.itemView.tv_nama_makanan.text = foodData!![position].namaMakanan
        holder.itemView.tv_harga.text = "Rp " + foodData!![position].harga

        holder.itemView.item.setOnClickListener {
            onClick(foodData!![position])
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