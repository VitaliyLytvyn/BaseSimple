package com.me.basesimple.presentation


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.me.basesimple.R
import com.me.basesimple.global.extention.inflate
import com.me.basesimple.presentation.entity.RepoView
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
