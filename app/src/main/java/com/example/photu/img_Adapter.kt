package com.example.photu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class img_Adapter: ListAdapter<Image,ImgViewHolder>(DiffCallback) {

    companion object {
        val DiffCallback = object: DiffUtil.ItemCallback<Image>() {
            override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
                return oldItem.uri == newItem.uri
            }

            override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
               return oldItem.name == newItem.name
            }

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImgViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.image_container,parent,false)
        return ImgViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImgViewHolder, position: Int) {
        val thisImage = getItem(position)
        Glide.with(holder.imgview)
            .load(thisImage.uri)
            .thumbnail(.33f)
            .centerCrop()
            .into(holder.imgview)
    }
}

class ImgViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val imgview: ImageView
    init {
        imgview = view.findViewById(R.id.imageView)
    }
}