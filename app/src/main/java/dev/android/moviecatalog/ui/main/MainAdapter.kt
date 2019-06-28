package dev.android.moviecatalog.ui.main

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import dev.android.moviecatalog.R
import dev.android.moviecatalog.db.MovieDb
import dev.android.moviecatalog.models.Movie
import dev.android.moviecatalog.utils.Constants
import java.text.SimpleDateFormat
import java.util.*


class MainAdapter(private val list: ArrayList<Movie>, private val context: Context, private val movieDb: MovieDb) :
    RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    private val favoriteList = movieDb.loadFavorite()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int = list.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (holder.title.lineCount == 1) {
            holder.description.maxLines = 4
        } else if (holder.title.lineCount == 2) {
            holder.description.maxLines = 3
        }

        Glide
            .with(context)
            .load(Constants.imageURL + list[position].posterPath)
            .into(holder.image)

        holder.title.text = list[position].title
        holder.description.text = list[position].overview
        if (list[position].releaseDate != "") {
            holder.date.text = formatDate(list[position].releaseDate)
        }
        if (favoriteList?.contains(list[position].id)!!) {
            showFavorite(holder, true)
        } else showFavorite(holder, false)

        var flag = false
        holder.favoriteBtn.setOnClickListener {
            flag = !flag
            if (flag) {
                movieDb.addFavorite(list[position].id, context)
            } else {
                movieDb.deleteFavorite(list[position].id, context)
            }
            showFavorite(holder, flag)
        }

        holder.itemView.setOnClickListener {
            Toast.makeText(context, list[position].title, Toast.LENGTH_LONG).show()
        }
    }


    private fun formatDate(date: String): String {
        val oldDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val newDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return newDateFormat.format(oldDateFormat.parse(date))
    }


    private fun showFavorite(holder: ViewHolder, flag: Boolean) {
        if (flag)
            holder.favoriteBtn.setBackgroundResource(R.drawable.ic_heart_fill)
        else
            holder.favoriteBtn.setBackgroundResource(R.drawable.ic_heart)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image = itemView.findViewById<ImageView>(R.id.movie_image)!!
        var title = itemView.findViewById<TextView>(R.id.movie_name)
        var description = itemView.findViewById<TextView>(R.id.movie_description)
        var date = itemView.findViewById<TextView>(R.id.movie_date_txt)
        var favoriteBtn = itemView.findViewById<ImageButton>(R.id.favorite_btn)
    }
}