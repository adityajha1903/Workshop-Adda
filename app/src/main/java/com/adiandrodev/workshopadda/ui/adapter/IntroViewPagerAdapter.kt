package com.adiandrodev.workshopadda.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.adiandrodev.workshopadda.R
import com.adiandrodev.workshopadda.databinding.IntroSliderLayoutBinding

class IntroViewPagerAdapter(context: Context):
    RecyclerView.Adapter<IntroViewPagerAdapter.ViewHolder>() {

    class ViewHolder(binding: IntroSliderLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        val title = binding.featureTitleTV
        val image = binding.introImageIV
        val des = binding.featureDescriptionTV
    }

    private val introImages = arrayOf(
        ResourcesCompat.getDrawable(context.resources, R.drawable.intro_img_1, null),
        ResourcesCompat.getDrawable(context.resources, R.drawable.intro_img_2, null),
        ResourcesCompat.getDrawable(context.resources, R.drawable.intro_img_3, null),
        ResourcesCompat.getDrawable(context.resources, R.drawable.intro_img_4, null)
    )

    private val titles = arrayOf(
        context.resources.getString(R.string.intro_title_1),
        context.resources.getString(R.string.intro_title_2),
        context.resources.getString(R.string.intro_title_3),
        context.resources.getString(R.string.intro_title_4),
    )

    private val descriptions = arrayOf(
        context.resources.getString(R.string.intro_des_1),
        context.resources.getString(R.string.intro_des_2),
        context.resources.getString(R.string.intro_des_3),
        context.resources.getString(R.string.intro_des_4),
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(IntroSliderLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = titles[position]
        holder.des.text = descriptions[position]
        holder.image.setImageDrawable(introImages[position])
    }

    override fun getItemCount(): Int {
        return 4
    }

}
