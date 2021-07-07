package space.skycolor.qbtmonitor.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import space.skycolor.qbtmonitor.databinding.TorrentListItemBinding
import space.skycolor.qbtmonitor.model.TorrentExtended

class TorrentItemAdapter(
    private val dataset: List<TorrentExtended>)
    : RecyclerView.Adapter<TorrentItemAdapter.ViewHolder>() {
    class ViewHolder(val binding: TorrentListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterLayout = TorrentListItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.conModel = dataset[position]
//        holder.itemView.setOnClickListener {
//            holder.itemView.findNavController().navigate(ConnectionListFragmentDirections.actionConnectionListFragmentToTorrentListFragment(dataset[position].base.id))
//        }
    }

    override fun getItemCount(): Int {
        return dataset.count()
    }
}