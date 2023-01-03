package com.htnguyen.ihealth.view.profile

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import androidx.lifecycle.viewModelScope
import com.htnguyen.ihealth.base.BaseViewModel
import com.htnguyen.ihealth.model.GalleryPicture
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class GalleryViewModel : BaseViewModel() {
    private var startingRow = 0
    private var rowsToLoad = 0
    private var allLoaded = false


    @ExperimentalCoroutinesApi
    fun getImagesFromGallery(
        context: Context,
        pageSize: Int,
        list: (List<GalleryPicture>) -> Unit
    ) {
        viewModelScope.launch {
            flow {
                emit(fetchGalleryImages(context, pageSize))
            }.catch {
                it.printStackTrace()
            }.collect {
                list(it)
            }
        }

    }

    fun getVideosFromGallery(
        context: Context,
        pageSize: Int,
        list: (List<GalleryPicture>) -> Unit
    ) {
        viewModelScope.launch {
            flow {
                emit(fetchGalleryVideos(context, pageSize))
            }.catch {
                it.printStackTrace()
            }.collect {
                list(it)
            }
        }

    }

    fun getGallerySize(context: Context): Int {
        val cursor = getGalleryCursor(context)
        val rows = cursor!!.count
        cursor.close()
        return rows
    }

    private fun fetchGalleryImages(context: Context, pageSize: Int): List<GalleryPicture> {
        val cursor = getGalleryCursor(context)

        if (cursor != null && !allLoaded) {
            val totalRows = cursor.count
            val galleryImageUrls = ArrayList<GalleryPicture>(totalRows)
            allLoaded = rowsToLoad == totalRows
            if (rowsToLoad < pageSize) {
                rowsToLoad = pageSize
            }

            for (i in startingRow until rowsToLoad) {
                cursor.moveToPosition(i)
                val dataColumnIndex =
                    cursor.getColumnIndex(MediaStore.Images.Media._ID) //get column index

                val imageURI = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    cursor.getLong(dataColumnIndex))

                val path = imageURI.toString()
                galleryImageUrls.add(GalleryPicture(path)) //get Image path from column index

            }

            startingRow = rowsToLoad

            if (pageSize > totalRows || rowsToLoad >= totalRows)
                rowsToLoad = totalRows
            else {
                if (totalRows - rowsToLoad <= pageSize)
                    rowsToLoad = totalRows
                else
                    rowsToLoad += pageSize
            }

            cursor.close()

            return galleryImageUrls
        }

        return emptyList()
    }

    private fun getGalleryCursor(context: Context): Cursor? {
        val externalUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val columns = arrayOf(MediaStore.MediaColumns._ID, MediaStore.MediaColumns.DATE_MODIFIED)
        val orderBy = MediaStore.MediaColumns.DATE_MODIFIED //order data by modified
        return context.contentResolver
            .query(
                externalUri,
                columns,
                null,
                null,
                "$orderBy DESC"
            )//get all data in Cursor by sorting in DESC order
    }

    private fun fetchGalleryVideos(context: Context, pageSize: Int): List<GalleryPicture> {
        val cursor = getGalleryCursor(context)

        if (cursor != null && !allLoaded) {
            val totalRows = cursor.count
            val galleryVideoUrls = ArrayList<GalleryPicture>(totalRows)
            allLoaded = rowsToLoad == totalRows
            if (rowsToLoad < pageSize) {
                rowsToLoad = pageSize
            }

            for (i in startingRow until rowsToLoad) {
                cursor.moveToPosition(i)
                val dataColumnIndex =
                    cursor.getColumnIndex(MediaStore.Video.Media._ID) //get column index

                val imageURI = ContentUris.withAppendedId(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    cursor.getLong(dataColumnIndex))

                val path = imageURI.toString()
                galleryVideoUrls.add(GalleryPicture(path)) //get Image path from column index

            }

            startingRow = rowsToLoad

            if (pageSize > totalRows || rowsToLoad >= totalRows)
                rowsToLoad = totalRows
            else {
                if (totalRows - rowsToLoad <= pageSize)
                    rowsToLoad = totalRows
                else
                    rowsToLoad += pageSize
            }

            cursor.close()
            return galleryVideoUrls
        }

        return emptyList()
    }
}