package com.us.telemedicine.presentation


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.us.telemedicine.R
import com.us.telemedicine.global.extention.inflate
import com.us.telemedicine.presentation.entity.RepoView
import kotlinx.android.synthetic.main.row_repo.view.*

import javax.inject.Inject
import kotlin.properties.Delegates

class RepoesAdapter
@Inject constructor() : RecyclerView.Adapter<RepoesAdapter.ViewHolder>() {

    internal var collection: List<RepoView> by Delegates.observable(emptyList()) {
        _, _, _ -> notifyDataSetChanged()
    }

    internal var clickListener: (RepoView) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(parent.inflate(R.layout.row_repo))

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) =
            viewHolder.bind(collection[position], clickListener)

    override fun getItemCount() = collection.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(repoView: RepoView, clickListener: (RepoView) -> Unit) {
            itemView.textViewId.text = repoView.id.toString()
            itemView.textViewNme.text = repoView.name
            itemView.setOnClickListener { clickListener(repoView) }
        }
    }
}
