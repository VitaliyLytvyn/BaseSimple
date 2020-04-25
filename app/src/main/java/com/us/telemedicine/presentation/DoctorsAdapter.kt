package com.us.telemedicine.presentation


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.us.telemedicine.databinding.RowDoctorBinding
import com.us.telemedicine.domain.entity.DoctorEntity
import javax.inject.Inject
import kotlin.properties.Delegates

class DoctorsAdapter
@Inject constructor() : RecyclerView.Adapter<DoctorsAdapter.ViewHolder>() {

    lateinit var bindind: RowDoctorBinding
    internal var collection: List<DoctorEntity> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    internal var clickListener: (DoctorEntity) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        bindind = RowDoctorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(bindind)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) =
        viewHolder.bind(collection[position], clickListener)

    override fun getItemCount() = collection.size

    class ViewHolder(private val binding: RowDoctorBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(doctorEntity: DoctorEntity, clickListener: (DoctorEntity) -> Unit) {
            binding.textViewId.text = doctorEntity.fullName
            itemView.setOnClickListener { clickListener(doctorEntity) }
        }
    }
}
