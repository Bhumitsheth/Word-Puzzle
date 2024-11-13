package com.wordpuzzle.app.android.main.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.wordpuzzle.app.android.constants.AppConstants
import com.wordpuzzle.app.android.R
import com.wordpuzzle.app.android.databinding.RowNavigationDrawerLayoutBinding
import com.wordpuzzle.app.android.di.implementor.RecyclerViewItemClickListener
import com.wordpuzzle.app.android.service.model.common.*

class NavigationDrawerAdapter(
    private val list: List<NavigationDrawerViewDataModel>,
) : RecyclerView.Adapter<NavigationDrawerAdapter.ViewHolder>() {
    private lateinit var onClickListener: RecyclerViewItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<RowNavigationDrawerLayoutBinding>(
            LayoutInflater.from(parent.context),
            R.layout.row_navigation_drawer_layout,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val navigationDrawerData = list[position]
        holder.itemViewHolder.data = navigationDrawerData
    }

    override fun getItemCount(): Int {
        return list.size
    }

    // A function to bind the onclickListener.
    fun setRecyclerViewItemClickListener(onClickListener: RecyclerViewItemClickListener) {
        this.onClickListener = onClickListener
    }

    inner class ViewHolder(val itemViewHolder: RowNavigationDrawerLayoutBinding) :
        RecyclerView.ViewHolder(itemViewHolder.root), View.OnClickListener {

        init {
            itemViewHolder.rootLayout.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            when (v) {
                itemViewHolder.rootLayout -> {
                    onClickListener.onItemClick(adapterPosition, AppConstants.ITEM_CLICK, itemViewHolder.root)
                }
            }
        }
    }
}
