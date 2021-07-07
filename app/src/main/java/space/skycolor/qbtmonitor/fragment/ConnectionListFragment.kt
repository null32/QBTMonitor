package space.skycolor.qbtmonitor.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import space.skycolor.qbtmonitor.QBTApplication
import space.skycolor.qbtmonitor.R
import space.skycolor.qbtmonitor.adapter.ConnectionItemAdapter
import space.skycolor.qbtmonitor.databinding.FragmentConnectionListBinding
import space.skycolor.qbtmonitor.model.ConnectionViewModel
import space.skycolor.qbtmonitor.model.ConnectionViewModelFactory

class ConnectionListFragment : Fragment() {
    private var _binding: FragmentConnectionListBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView

    private val viewModel: ConnectionViewModel by activityViewModels {
        ConnectionViewModelFactory(
            (activity?.application as QBTApplication).database.connectionDao()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentConnectionListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.fragmentConnectionsRecycler
        recyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.connections.observe(viewLifecycleOwner, {
            newCons ->
                viewModel.UpdateVersionAll(newCons)
        })

        viewModel.extended.observe(viewLifecycleOwner, {
            newCons ->
                recyclerView.adapter = ConnectionItemAdapter(newCons!!) {
                    viewModel.RemoveConnection(it.base)
                }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.connection_list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.connection_list_menu_add -> {
                openAdd()
                return true
            }
            R.id.connection_list_menu_refresh -> {
                update()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openAdd() {
        this.findNavController().navigate(ConnectionListFragmentDirections.actionConnectionListFragmentToConnectionAddNewFragment())
    }

    private fun update() {
        viewModel.UpdateVersionAll()
    }
}