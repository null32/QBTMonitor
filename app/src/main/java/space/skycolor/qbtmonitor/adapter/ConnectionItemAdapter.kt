package space.skycolor.qbtmonitor.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import space.skycolor.qbtmonitor.databinding.ConnectionListItemBinding
import space.skycolor.qbtmonitor.fragment.ConnectionListFragmentDirections
import space.skycolor.qbtmonitor.model.ConnectionExtended

class ConnectionItemAdapter(
    private val dataset: List<ConnectionExtended>,
    private val deleteFun: (item: ConnectionExtended) -> Unit)
    : RecyclerView.Adapter<ConnectionItemAdapter.ViewHolder>() {
        class ViewHolder(val binding: ConnectionListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterLayout = ConnectionListItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.conModel = dataset[position]
        holder.itemView.setOnClickListener {
            holder.itemView.findNavController().navigate(ConnectionListFragmentDirections.actionConnectionListFragmentToTorrentListFragment(dataset[position].base.id))
        }
        holder.itemView.setOnLongClickListener {
            MaterialAlertDialogBuilder(holder.binding.root.context)
                .setTitle("Delete?")
                .setMessage("Delete ${dataset[position].base.title}")
                .setPositiveButton("Yes") { dialog, _ ->
                    deleteFun(dataset[position])
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int {
        return dataset.count()
    }
}