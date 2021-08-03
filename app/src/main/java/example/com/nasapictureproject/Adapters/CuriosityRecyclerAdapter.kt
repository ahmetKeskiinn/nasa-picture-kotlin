package example.com.nasapictureproject.Adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import example.com.nasapictureproject.Models.Photo
import example.com.nasapictureproject.R
import example.com.nasapictureproject.Utils.ImageDownloadTask


class CuriosityRecyclerAdapter(val context: Context, val activity: Activity) : View.OnClickListener, ListAdapter<Photo, CuriosityRecyclerAdapter.CuriosityHolder>(
        diffCallback
) {
    private lateinit var customLayout: View
    private val TAG = "CURIOSITYADAPTER"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CuriosityHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
                R.layout.curiosity_item, parent,
                false
        )
        return CuriosityHolder(itemView)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: CuriosityHolder, position: Int) {
        val media = "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885_960_720.jpg"
        if (media !== null) {
            Glide.with(holder.avatar)
                    .load(getItem(position).img_src)
                    .into(holder.avatar)
        }
        holder.download.isClickable = true
        holder.download.setOnClickListener(this)
        holder.download.setOnClickListener {
            askDownload(position, "download")
        }

    }

    private fun askDownload(position: Int, type: String): View.OnClickListener? {
        var builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.downloadTitle)
        builder.setMessage(R.string.wantToDownload)
        builder.setPositiveButton(R.string.yes) { dialog, which ->
            val download: ImageDownloadTask
            download = ImageDownloadTask(context, getItem(position).img_src, activity)
            download.execute()
            dialog.cancel()
        }
        builder.setNegativeButton(R.string.no) { dialog, which ->
            dialog.cancel()

        }
        builder.show()
        return null
    }


    @SuppressLint("ResourceType")
    private fun showDetails(position: Int) {
        var builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val v: View = activity.layoutInflater.inflate(R.layout.detail_dialog_content, null)
        val detailImage: ImageView = v.findViewById(R.id.dialogContentImage)
        val cameraFullName: TextView = v.findViewById(R.id.CamerafullNameTw)
        val camerName: TextView = v.findViewById(R.id.cameraNameTw)
        val cameraID: TextView = v.findViewById(R.id.cameraIDTw)
        val earthDate: TextView = v.findViewById(R.id.earthDateTw)
        val ID: TextView = v.findViewById(R.id.idTw)
        val roverID: TextView = v.findViewById(R.id.roverIDTw)
        val roverLanding: TextView = v.findViewById(R.id.roverLandDateTw)
        val roverLaunch: TextView = v.findViewById(R.id.roverLaunchDateTw)
        val roverName: TextView = v.findViewById(R.id.roverNameTw)
        val roverStatus: TextView = v.findViewById(R.id.roverStatusTw)
        Glide.with(v).load(getItem(position).img_src).into(detailImage)
        cameraFullName.text = getItem(position).camera.full_name
        camerName.text = getItem(position).camera.name
        cameraID.text = getItem(position).camera.id.toString()
        earthDate.text = getItem(position).earth_date
        ID.text = getItem(position).id.toString()
        roverID.text = getItem(position).rover.id.toString()
        roverLanding.text = getItem(position).rover.landing_date
        roverLaunch.text = getItem(position).rover.launch_date
        roverName.text = getItem(position).rover.name
        roverStatus.text = getItem(position).rover.status
        builder.setView(v)

        builder.show()
    }

    companion object {
        private const val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1
    }


    fun getDeveloperAt(position: Int) = getItem(position)


    inner class CuriosityHolder(iv: View) : RecyclerView.ViewHolder(iv) {

        val avatar: ImageView = itemView.findViewById(R.id.curisityItemImage)
        val download: ImageView = itemView.findViewById(R.id.downloadButton)

    }

    override fun onClick(v: View?) {
    }
}

private val diffCallback = object : DiffUtil.ItemCallback<Photo>() {
    override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
        TODO("Not yet implemented")
    }

    override fun areContentsTheSame(
            oldItem: Photo,
            newItem: Photo
    ): Boolean {
        TODO("Not yet implemented")
    }
}