package space.skycolor.qbtmonitor.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import space.skycolor.qbtmonitor.QBTApplication
import space.skycolor.qbtmonitor.databinding.FragmentConnectionAddNewBinding
import space.skycolor.qbtmonitor.model.ConnectionExtended
import space.skycolor.qbtmonitor.model.ConnectionViewModel
import space.skycolor.qbtmonitor.model.ConnectionViewModelFactory

class ConnectionAddNewFragment : Fragment() {
    private var _binding: FragmentConnectionAddNewBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ConnectionViewModel by activityViewModels {
        ConnectionViewModelFactory(
            (activity?.application as QBTApplication).database.connectionDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentConnectionAddNewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.connectionAddConfirmBtn.setOnClickListener { confirmAdd() }
    }

    private fun confirmAdd() {
        viewModel.AddConnection(viewModel.getNewConnectionEntity(
            binding.connectionAddTitleEdit.text.toString(),
            binding.connectionAddAddressEdit.text.toString(),
            binding.connectionAddUserEdit.text.toString(),
            binding.connectionAddPassEdit.text.toString()
        ))

        this.findNavController().navigate(ConnectionAddNewFragmentDirections.actionConnectionAddNewFragmentToConnectionListFragment())
    }
}