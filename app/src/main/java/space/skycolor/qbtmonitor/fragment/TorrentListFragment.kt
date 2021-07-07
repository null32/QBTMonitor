package space.skycolor.qbtmonitor.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import space.skycolor.qbtmonitor.QBTApplication
import space.skycolor.qbtmonitor.adapter.TorrentItemAdapter
import space.skycolor.qbtmonitor.databinding.FragmentTorrentListBinding
import space.skycolor.qbtmonitor.model.ConnectionViewModel
import space.skycolor.qbtmonitor.model.ConnectionViewModelFactory
import space.skycolor.qbtmonitor.model.ServerStateExtended
import space.skycolor.qbtmonitor.model.TorrentExtended

class TorrentListFragment : Fragment() {
    var _binding: FragmentTorrentListBinding? = null
    val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView

    private val args by navArgs<TorrentListFragmentArgs>()
    private val connectionId by lazy { args.connectionIndex }

    private val viewModel: ConnectionViewModel by activityViewModels {
        ConnectionViewModelFactory(
            (activity?.application as QBTApplication).database.connectionDao()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTorrentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.fragmentTorrentsRecycler
        recyclerView.layoutManager = LinearLayoutManager(context)

//        viewModel.singleConnection(connectionId).observe(viewLifecycleOwner) {
//            binding.conModel = it.MainData?.let { it1 -> ServerStateExtended(it1.server_state) }
//            recyclerView.adapter = it.MainData?.torrents?.let { it1 -> TorrentItemAdapter(it1.map{ t -> TorrentExtended(t.value) }) }
//        }
        viewModel.extended.observe(viewLifecycleOwner) {
            val target = it?.filter { e -> e.base.id == connectionId }?.get(0)
            binding.conModel = ServerStateExtended(target?.MainData?.server_state)
            recyclerView.adapter =
                target?.MainData?.torrents?.map { e -> TorrentExtended(e.value) }?.let { it1 -> TorrentItemAdapter(it1) }
        }

        viewModel.UpdateMainData(connectionId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.connection_list_menu, menu)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when(item.itemId) {
//            R.id.connection_list_menu_add -> {
//                openAdd()
//                return true
//            }
//            R.id.connection_list_menu_refresh -> {
//                update()
//                return true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }
//
//    private fun openAdd() {
//        this.findNavController().navigate(ConnectionListFragmentDirections.actionConnectionListFragmentToConnectionAddNewFragment())
//    }
//
//    private fun update() {
//        viewModel.UpdateVersionAll()
//    }
}