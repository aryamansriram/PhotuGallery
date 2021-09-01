package com.example.photu

import android.Manifest
import android.content.ContentUris
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {


    private lateinit var btn: Button
    private lateinit var rv: RecyclerView
    private lateinit var adapter: img_Adapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rv = findViewById(R.id.recyclerView)
        adapter = img_Adapter()
        rv.layoutManager = GridLayoutManager(applicationContext,3)
        rv.adapter = adapter

        btn = findViewById(R.id.showbtn)
        btn.setOnClickListener({ view -> openMediaStore(view as Button)})



    }

    private fun showImages(){
        var imglist: List<Image> = getImages(context = applicationContext)

        adapter.submitList(imglist)



    }


    private fun haveStoragePermission() =
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED


    private fun requestPermission() {
        if(!haveStoragePermission()){
            val permissions = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)

            ActivityCompat.requestPermissions(
                this,permissions, 1
            )
        }
    }
    private fun openMediaStore(btn: Button) {
        if (haveStoragePermission()) {
            showImages()
            btn.visibility = View.GONE

        } else {
            requestPermission()
        }
    }



    fun getImages(context:Context): List<Image>{

        val imgList = mutableListOf<Image>()

        val collection =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Images.Media.getContentUri(
                    MediaStore.VOLUME_EXTERNAL
                )
            } else {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.SIZE
        )
        val sortOrder = "${MediaStore.Images.Media.DISPLAY_NAME} ASC"

        val query = context.contentResolver.query(
            collection,
            projection,
            null,
            null,
            sortOrder
        )

        query?.use {
                cursor: Cursor ->
            val idCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val sizeCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)

            while(cursor.moveToNext()){
                val id = cursor.getLong(idCol)
                val name = cursor.getString(nameCol)
                val size = cursor.getInt(sizeCol)

                val contentUri: Uri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )
                imgList += Image(contentUri,name,size)
            }


        }

        return imgList

    }




}