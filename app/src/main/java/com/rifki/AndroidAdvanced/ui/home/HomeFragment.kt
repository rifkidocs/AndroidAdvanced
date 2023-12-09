package com.rifki.AndroidAdvanced.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rifki.AndroidAdvanced.R
import com.rifki.AndroidAdvanced.retrofit.ApiService
import com.rifki.AndroidAdvanced.retrofit.HomeModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private val TAG: String = "HomeFragment"
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HomeModelAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = HomeModelAdapter()
        recyclerView.adapter = adapter

        getDataFromApi()
    }



    private fun getDataFromApi() {
        ApiService.endPoint.getPhotos()
            .enqueue(object : Callback<List<HomeModel>> {
                override fun onResponse(
                    call: Call<List<HomeModel>>,
                    response: Response<List<HomeModel>>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        result?.let { adapter.submitList(it) }
                    }
                }

                override fun onFailure(call: Call<List<HomeModel>>, t: Throwable) {
                    Log.e(TAG, t.toString())
                }
            })
    }
}

class HomeModelAdapter : ListAdapter<HomeModel, HomeModelAdapter.ViewHolder>(HomeModelDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_model, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.titleTextView.text = item.title
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
    }

    private class HomeModelDiffCallback : DiffUtil.ItemCallback<HomeModel>() {
        override fun areItemsTheSame(oldItem: HomeModel, newItem: HomeModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: HomeModel, newItem: HomeModel): Boolean {
            return oldItem == newItem
        }
    }
}
