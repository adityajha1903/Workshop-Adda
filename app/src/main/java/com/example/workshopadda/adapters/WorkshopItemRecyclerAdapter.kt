package com.example.workshopadda.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.workshopadda.R
import com.example.workshopadda.databinding.RecyclerViewWorkshopItemBinding
import com.example.workshopadda.models.Workshop
import com.example.workshopadda.utils.Constants

class WorkshopItemRecyclerAdapter(
    private val context: Context,
    private val allWorkshopList: ArrayList<Workshop>,
    private val appliedWorkshopList: ArrayList<Workshop>,
    private val isAppliedList: Boolean,
    private val workshopAppliedListener: (id: Int) -> Unit
): RecyclerView.Adapter<WorkshopItemRecyclerAdapter.ViewHolder>() {

    class ViewHolder(binding: RecyclerViewWorkshopItemBinding): RecyclerView.ViewHolder(binding.root) {
        val image = binding.workshopImageIv
        val name = binding.workshopNameTv
        val description = binding.workshopDescriptionTv
        val from = binding.fromTv
        val to = binding.toTv
        val applyBtn = binding.applyBtn
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RecyclerViewWorkshopItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val workshop : Workshop
        if (isAppliedList) {
            workshop = appliedWorkshopList[position]
            holder.applyBtn.visibility = View.GONE
        } else {
            workshop = allWorkshopList[position]
            holder.applyBtn.setBackgroundColor(context.getColor(R.color.green))
            holder.applyBtn.text = context.resources.getString(R.string.apply)
            holder.applyBtn.isClickable = true
            holder.applyBtn.isFocusable = true
            holder.applyBtn.setOnClickListener {
                workshopAppliedListener.invoke(workshop.id)
            }
            appliedWorkshopList.forEach{ appliedWorkshop ->
                if (workshop.id == appliedWorkshop.id) {
                    holder.applyBtn.setBackgroundColor(context.getColor(R.color.grey))
                    holder.applyBtn.text = context.resources.getString(R.string.applied)
                    holder.applyBtn.isClickable = false
                    holder.applyBtn.isFocusable = false
                }
            }
        }
        holder.image.setImageDrawable(Constants.getWorkshopImageForId(context, workshop.id))
        holder.name.text = workshop.name
        holder.description.text = workshop.description
        holder.from.text = workshop.fromDate
        holder.to.text = workshop.toDate
    }

    override fun getItemCount(): Int {
        return if (isAppliedList) {
            appliedWorkshopList.size
        } else {
            allWorkshopList.size
        }
    }
}