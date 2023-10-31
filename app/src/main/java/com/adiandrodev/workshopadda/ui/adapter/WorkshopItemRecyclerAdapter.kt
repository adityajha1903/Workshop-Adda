package com.adiandrodev.workshopadda.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adiandrodev.workshopadda.R
import com.adiandrodev.workshopadda.databinding.RecyclerViewWorkshopItemBinding
import com.adiandrodev.workshopadda.util.Constants
import com.adiandrodev.workshopadda.data.model.Workshop

class WorkshopItemRecyclerAdapter(
    private val context: Context,
    private var allWorkshopList: ArrayList<Workshop>,
    private var appliedWorkshopList: ArrayList<Workshop>,
    private val isAppliedList: Boolean,
    private val workshopAppliedListener: (id: Int) -> Unit,
    private val workshopRemovedListener: (id: Int) -> Unit
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
            if (appliedWorkshopList.have(workshop.id)) {
                holder.applyBtn.setBackgroundColor(context.getColor(R.color.red))
                holder.applyBtn.text = context.resources.getString(R.string.remove)
                holder.applyBtn.setOnClickListener {
                    workshopRemovedListener.invoke(workshop.id)
                }
            } else {
                holder.applyBtn.setBackgroundColor(context.getColor(R.color.green))
                holder.applyBtn.text = context.resources.getString(R.string.apply)
                holder.applyBtn.setOnClickListener {
                    workshopAppliedListener.invoke(workshop.id)
                }
            }
        }
        holder.image.setImageDrawable(Constants.getWorkshopImageForId(context, workshop.id))
        holder.name.text = workshop.name
        holder.description.text = workshop.description
        holder.from.text = workshop.fromDate
        holder.to.text = workshop.toDate
    }

    private fun ArrayList<Workshop>.have(id: Int): Boolean {
        this.forEach {
            if (it.id == id) {
                return true
            }
        }
        return false
    }

    override fun getItemCount(): Int {
        return if (isAppliedList) {
            appliedWorkshopList.size
        } else {
            allWorkshopList.size
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun dataSetChanged(allWorkshopList: ArrayList<Workshop>, appliedWorkshopList: ArrayList<Workshop>) {
        this.allWorkshopList = allWorkshopList
        this.appliedWorkshopList = appliedWorkshopList
        notifyDataSetChanged()
    }
}