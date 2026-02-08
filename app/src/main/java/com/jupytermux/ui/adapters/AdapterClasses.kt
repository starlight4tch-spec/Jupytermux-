package com.jupytermux.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jupytermux.R
import com.jupytermux.data.CellEntity

class NotebookCellAdapter(
    private val onExecuteClick: (Int, CellEntity) -> Unit
) : ListAdapter<CellEntity, NotebookCellAdapter.CellViewHolder>(CellDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CellViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notebook_cell, parent, false)
        return CellViewHolder(view)
    }

    override fun onBindViewHolder(holder: CellViewHolder, position: Int) {
        holder.bind(currentList[position], position, onExecuteClick)
    }

    class CellViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cellTypeLabel = itemView.findViewById<TextView>(R.id.cellTypeLabel)
        private val cellSource = itemView.findViewById<EditText>(R.id.cellSource)
        private val cellOutput = itemView.findViewById<TextView>(R.id.cellOutput)
        private val btnExecute = itemView.findViewById<Button>(R.id.btnExecuteCell)
        private val btnDelete = itemView.findViewById<ImageButton>(R.id.btnDeleteCell)
        private val outputContainer = itemView.findViewById<LinearLayout>(R.id.outputContainer)

        fun bind(cell: CellEntity, position: Int, onExecute: (Int, CellEntity) -> Unit) {
            cellTypeLabel.text = cell.cellType.uppercase()
            cellSource.setText(cell.source)
            
            if (cell.cellType == "code") {
                btnExecute.visibility = View.VISIBLE
                outputContainer.visibility = if (cell.output != null) View.VISIBLE else View.GONE
                
                if (cell.output != null) {
                    cellOutput.text = cell.output
                }
                
                btnExecute.setOnClickListener {
                    onExecute(position, cell)
                }
            } else {
                btnExecute.visibility = View.GONE
                outputContainer.visibility = View.GONE
            }
        }
    }

    private class CellDiffCallback : DiffUtil.ItemCallback<CellEntity>() {
        override fun areItemsTheSame(oldItem: CellEntity, newItem: CellEntity) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: CellEntity, newItem: CellEntity) = oldItem == newItem
    }
}

class NotebookBrowserAdapter(
    private val onNotebookClick: (com.jupytermux.data.NotebookEntity) -> Unit,
    private val onDeleteClick: (com.jupytermux.data.NotebookEntity) -> Unit,
    private val onFavoriteClick: (com.jupytermux.data.NotebookEntity) -> Unit
) : ListAdapter<com.jupytermux.data.NotebookEntity, NotebookBrowserAdapter.NotebookViewHolder>(NotebookDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotebookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notebook_browser, parent, false)
        return NotebookViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotebookViewHolder, position: Int) {
        holder.bind(currentList[position], onNotebookClick, onDeleteClick, onFavoriteClick)
    }

    class NotebookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val notebookName = itemView.findViewById<TextView>(R.id.notebookName)
        private val notebookDate = itemView.findViewById<TextView>(R.id.notebookDate)
        private val btnOpen = itemView.findViewById<Button>(R.id.btnOpenNotebook)
        private val btnDelete = itemView.findViewById<ImageButton>(R.id.btnDeleteNotebook)
        private val btnFavorite = itemView.findViewById<ImageButton>(R.id.btnFavoriteNotebook)

        fun bind(
            notebook: com.jupytermux.data.NotebookEntity,
            onOpen: (com.jupytermux.data.NotebookEntity) -> Unit,
            onDelete: (com.jupytermux.data.NotebookEntity) -> Unit,
            onFavorite: (com.jupytermux.data.NotebookEntity) -> Unit
        ) {
            notebookName.text = notebook.name
            notebookDate.text = java.text.SimpleDateFormat("MM/dd HH:mm", java.util.Locale.getDefault())
                .format(java.util.Date(notebook.updatedAt))
            
            btnFavorite.setImageResource(if (notebook.isFavorite) R.drawable.ic_star_filled else R.drawable.ic_star_empty)
            
            btnOpen.setOnClickListener { onOpen(notebook) }
            btnDelete.setOnClickListener { onDelete(notebook) }
            btnFavorite.setOnClickListener { onFavorite(notebook) }
        }
    }

    private class NotebookDiffCallback : DiffUtil.ItemCallback<com.jupytermux.data.NotebookEntity>() {
        override fun areItemsTheSame(oldItem: com.jupytermux.data.NotebookEntity, newItem: com.jupytermux.data.NotebookEntity) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: com.jupytermux.data.NotebookEntity, newItem: com.jupytermux.data.NotebookEntity) = oldItem == newItem
    }
}
