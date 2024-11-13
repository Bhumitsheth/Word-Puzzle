package com.wordpuzzle.app.android.main.ui.leader_board.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.wordpuzzle.app.android.R
import com.wordpuzzle.app.android.databinding.RowLeaderBoardLayoutBinding
import com.wordpuzzle.app.android.service.model.static_model.ScoreModel

class LeaderBoardAdapter  (
    private var list: List<ScoreModel>
) : RecyclerView.Adapter<LeaderBoardAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<RowLeaderBoardLayoutBinding>(LayoutInflater.from(parent.context), R.layout.row_leader_board_layout, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val adjustedPosition = position +3
        holder.itemViewHolder.data = list[adjustedPosition]
    }

    override fun getItemCount(): Int {
        return if (list.size > 4) list.size - 3 else 0
    }

    inner class ViewHolder(val itemViewHolder: RowLeaderBoardLayoutBinding) :
        RecyclerView.ViewHolder(itemViewHolder.root), View.OnClickListener {

        init {
            itemViewHolder.rootLayout.setOnClickListener(this)
        }

        override fun onClick(v: View) {

        }
    }
}