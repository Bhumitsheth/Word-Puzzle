package com.wordpuzzle.app.android.main.ui.leader_board.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.graphics.Color
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.wordpuzzle.app.android.R
import com.wordpuzzle.app.android.databinding.RowWeeklyLayoutBinding
import com.wordpuzzle.app.android.service.model.response.WeeklyListModel

class WeeklyListAdapter(
    private var list: List<WeeklyListModel>
) : RecyclerView.Adapter<WeeklyListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<RowWeeklyLayoutBinding>(LayoutInflater.from(parent.context), R.layout.row_weekly_layout, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val adjustedPosition = position +3
        holder.itemViewHolder.data = list[position]

        when (position) {
            0 -> {
                // Get the drawable resource for position 0
                holder.itemViewHolder.guideline.visibility = View.VISIBLE
                holder.itemViewHolder.ivCrown.visibility = View.VISIBLE
                setRootViewBackground(holder.itemViewHolder.rootLayout, R.drawable.ic_one_score_rectangle_shape_svg)
                holder.itemViewHolder.tvPlayerName.setTextColor(ContextCompat.getColor(holder.itemViewHolder.rootLayout.context, R.color.white))
                holder.itemViewHolder.tvScore.setTextColor(ContextCompat.getColor(holder.itemViewHolder.rootLayout.context, R.color.selectPageNoColor))
            }
            1 -> {
                // Get the drawable resource for position 1
                holder.itemViewHolder.guideline.visibility = View.GONE
                holder.itemViewHolder.ivCrown.visibility = View.GONE
                setRootViewBackground(holder.itemViewHolder.rootLayout, R.drawable.ic_two_score_rectangle_shape_svg)
                holder.itemViewHolder.tvPlayerName.setTextColor(ContextCompat.getColor(holder.itemViewHolder.rootLayout.context, R.color.white))
                holder.itemViewHolder.tvScore.setTextColor(ContextCompat.getColor(holder.itemViewHolder.rootLayout.context, R.color.silverColor))
            }
            2 -> {
                // Get the drawable resource for position 2
                holder.itemViewHolder.guideline.visibility = View.GONE
                holder.itemViewHolder.ivCrown.visibility = View.GONE
                setRootViewBackground(holder.itemViewHolder.rootLayout, R.drawable.ic_three_score_rectangle_shape_svg)
                holder.itemViewHolder.tvPlayerName.setTextColor(ContextCompat.getColor(holder.itemViewHolder.rootLayout.context, R.color.white))
                holder.itemViewHolder.tvScore.setTextColor(ContextCompat.getColor(holder.itemViewHolder.rootLayout.context, R.color.white))
            }
            else -> {
                // Get the default drawable resource for other positions
                holder.itemViewHolder.guideline.visibility = View.GONE
                holder.itemViewHolder.ivCrown.visibility = View.GONE
                setRootViewBackground(holder.itemViewHolder.rootLayout, R.drawable.ic_score_rectangle_shape_svg)
            }
        }
    }

    private fun setRootViewBackground(constraintLayout: ConstraintLayout, bgShape: Int) {
        val drawable: Drawable? = ContextCompat.getDrawable(constraintLayout.context, bgShape)
        constraintLayout.background = drawable
    }

    override fun getItemCount(): Int {
        return list.size
//        return if (list.size >= 4) list.size - 3 else 0
    }

    inner class ViewHolder(val itemViewHolder: RowWeeklyLayoutBinding) :
        RecyclerView.ViewHolder(itemViewHolder.root), View.OnClickListener {
        override fun onClick(v: View) {
        }
    }
}
