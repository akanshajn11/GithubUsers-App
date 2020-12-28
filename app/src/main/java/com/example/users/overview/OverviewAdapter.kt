package com.example.users.overview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.users.R
import com.example.users.network.Item
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_user.view.*


/*
Adapter: Contains the logic to filter data in recycler view. It creates view holder and fills it with data for
Recycler View to display. It adapts the input data into something Recycler View can use.
The Adapter class extends  RecyclerView.Adapter. In the argument for RecyclerView.Adapter we pass the
view holder type the adapter will use.
 */

/*We are passing the list of items we need to display in the constructor for adapter class*/

class OverviewAdapter(private val itemList: List<Item>) :
    RecyclerView.Adapter<OverviewAdapter.OverviewHolder>() {

/*View Holder :  This class contains information about the row layout and meta data for a row like its position
  in the Recycler View. The references for all units of row layout are cached in the view holder so that it can be reused.
  View holder represents a single row in the list. One instance of view object is passed to it as argument. We pass the same view object
  to the super class also since it needs it internally. The super class it extends is RecyclerView.ViewHolder
 */

    class OverviewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val login: TextView = itemView.title
        val avatarUrl: ImageView = itemView.cover
        val htmlUrl: TextView = itemView.githubLink

    }

/*
   onCreateViewHolder() :  Called when a new view holder needs to be created
   parent : Recycler View
   viewType : It is used only when we need different types of row layout in same view
 */

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OverviewHolder {
        /*
        We need to create a view object here. Since View Holder class needs it to create a view holder.
        Layout Inflater : Used to convert a layout into an object
        context : The activity hosting the recycler view
        attachToRoot : Set to false since we need not attach the view object to recycler view now. Recycler view will do it
         */
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_user, parent, false)
        return OverviewHolder(itemView)
    }

/*
    onBindViewHolder() : Fills the data in the view holder.
    holder : The view holder where data will be filled
    position : The position of item in the list so adapter would know which data to fill
 */

    override fun onBindViewHolder(holder: OverviewHolder, position: Int) {

        /*
        currentItem : Item at the required position
        All the values in the current item will be bind to the view holder
         */

        val currentItem = itemList[position]
        holder.login.text = currentItem.login
        holder.htmlUrl.text = currentItem.htmlUrl

        /*
        load(): Provide the current image info here
        into(): Provide the holder location where we need to load this current image
         */

        Picasso.with(holder.avatarUrl.context)
            .load(currentItem.avatarUrl)
            .into(holder.avatarUrl)
    }

/*
getItemCount() : Returns the number of items in the list
 */

    override fun getItemCount() = itemList.size

}