package com.wordpuzzle.app.android.main.ui.select_book_pdf.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.wordpuzzle.app.android.R
import com.wordpuzzle.app.android.databinding.RowSelectBookPdfLayoutBinding
import com.wordpuzzle.app.android.main.ui.select_book_pdf.SelectBookPdfActivity
import com.wordpuzzle.app.android.service.model.response.BookListModel

class SelectBookPdfAdapter(
    private var list: List<BookListModel>, private var activity: SelectBookPdfActivity
) : RecyclerView.Adapter<SelectBookPdfAdapter.ViewHolder>() {
    var selectedPosition = -1
//    private lateinit var listener: RecyclerViewItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<RowSelectBookPdfLayoutBinding>(
            LayoutInflater.from(parent.context),
            R.layout.row_select_book_pdf_layout,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemViewHolder.data = list[position]
        if (list[position].isSelected == true) {
//            holder.itemViewHolder.icArrow.setImageResource(R.drawable.ic_arrow_up)
            holder.itemViewHolder.icArrow.visibility = View.GONE
        } else {
            holder.itemViewHolder.icArrow.visibility = View.VISIBLE
            holder.itemViewHolder.icArrow.setImageResource(R.drawable.ic_arrow_down)
        }

        if (selectedPosition == position) {
            holder.itemViewHolder.icArrow.visibility = View.GONE
//            holder.itemViewHolder.icArrow.setImageResource(R.drawable.ic_arrow_up)
            holder.itemViewHolder.constraintViewLayout.visibility = View.VISIBLE
            holder.itemViewHolder.rootLayout.setBackgroundResource(R.drawable.ic_select_book_pdf_details_rectangle_shape)
        } else {
            holder.itemViewHolder.icArrow.visibility = View.VISIBLE
            holder.itemViewHolder.icArrow.setImageResource(R.drawable.ic_arrow_down)
            holder.itemViewHolder.constraintViewLayout.visibility = View.GONE
            holder.itemViewHolder.rootLayout.setBackgroundResource(R.drawable.ic_select_book_pdf_rectangle_shape)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    // A function to bind the onclickListener.
//    fun setRecyclerViewItemClickListener(listener: RecyclerViewItemClickListener) {
//        this.listener = listener
//    }

    fun filterList(filterList: ArrayList<BookListModel>) {
        list = filterList
        notifyDataSetChanged()
    }

    inner class ViewHolder(val itemViewHolder: RowSelectBookPdfLayoutBinding) :
        RecyclerView.ViewHolder(itemViewHolder.root), View.OnClickListener {
        init {
            itemViewHolder.rootLayout.setOnClickListener(this)
            itemViewHolder.icArrow.setOnClickListener(this)
            itemViewHolder.ivOk.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            when (v) {
                itemViewHolder.rootLayout -> {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        if (selectedPosition != position) {
                            val previouslySelectedPosition = selectedPosition
                            selectedPosition = position
                            notifyItemChanged(previouslySelectedPosition)
                            notifyItemChanged(selectedPosition)
//                            listener.onItemClick(position, AppConstants.ITEM_CLICK, itemViewHolder.icArrow)
                        }
                    }
                }

                itemViewHolder.icArrow -> {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        if (selectedPosition != position) {
                            val previouslySelectedPosition = selectedPosition
                            selectedPosition = position
                            notifyItemChanged(previouslySelectedPosition)
                            notifyItemChanged(selectedPosition)
//                            listener.onItemClick(position, AppConstants.ITEM_CLICK, itemViewHolder.icArrow)
                        }
                    }
                }

                itemViewHolder.ivOk -> {
                    activity.viewModel.bookData(list[adapterPosition].id, list[adapterPosition].ebookName, itemViewHolder.spnSelectPageNo.selectedItem.toString())
//                    val intent = Intent(itemViewHolder.root.context, LoadingAnimationActivity::class.java)
//                    intent.putExtra(AppConstants.bookId, list[adapterPosition].id)
//                    intent.putExtra(AppConstants.bookName, list[adapterPosition].ebookName)
//                    intent.putExtra(AppConstants.selectPageNo, itemViewHolder.spnSelectPageNo.selectedItem.toString())
//                    itemViewHolder.root.context.startActivity(intent)
                }
            }
        }
    }
}