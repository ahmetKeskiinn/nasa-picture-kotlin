package example.com.nasapictureproject.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import example.com.nasapictureproject.R

class CuriosityRecyclerAdapter(val countryList: MutableList<String>) : RecyclerView.Adapter<CuriosityRecyclerAdapter.ModelViewHolder>() {

    class ModelViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val flagImage: ImageView = view.findViewById(R.id.curisityItemImage)

        fun bindItems(item: String) {
            flagImage.setImageResource(R.drawable.ic_dashboard_black_24dp)
            //flagImage.setImageResource(item.flagImage)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.curiosity_item, parent, false)
        return ModelViewHolder(view)

    }

    override fun getItemCount(): Int {
        return countryList.size

    }

    override fun onBindViewHolder(holder: ModelViewHolder, position: Int) {
        holder.bindItems(countryList.get(position))


    }
}