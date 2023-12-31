package org.bmsk.androidcoroutinehandson.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.bmsk.androidcoroutinehandson.databinding.FragmentMainBinding

class ImageSearchFragment : Fragment() {
    private lateinit var imageSearchViewModel: ImageSearchViewModel
    private val adapter: ImageSearchAdapter = ImageSearchAdapter {
        imageSearchViewModel.toggle(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageSearchViewModel =
            ViewModelProvider(requireActivity())[ImageSearchViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentMainBinding.inflate(inflater, container, false)
        val root = binding.root

        // lifecycleScope // 프래그먼트의 라이프사이클
        // viewLifecycleOwner.lifecycleScope // 뷰의 라이프사이클 (프래그먼트가 더 이상 보이지 않을 경우에도 라이프사이클스코프가 남아있을 수 있다.)
        viewLifecycleOwner.lifecycleScope.launch {
            imageSearchViewModel.pagingDataFlow
                .collectLatest { items ->
                    adapter.submitData(items)
                }
        }

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(context, 4)
        binding.search.setOnClickListener {
            val query = binding.editText.text.trim().toString()
            imageSearchViewModel.handleQuery(query)
        }

        return root
    }
}