package com.bangkit.leafsense.ui.leaf

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.leafsense.R
import com.bangkit.leafsense.data.api.ApiConfig
import com.bangkit.leafsense.data.response.ArticlesResponse
import com.bangkit.leafsense.databinding.ActivityTeaBinding
import com.bangkit.leafsense.ui.adapter.ArticlesAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CoffeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTeaBinding
    private lateinit var articlesAdapter: ArticlesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        fetchArticles()
    }

    private fun setupRecyclerView() {
        binding.verticalRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun fetchArticles() {
        ApiConfig.getApiService().getArticles().enqueue(object : Callback<ArticlesResponse> {
            override fun onResponse(call: Call<ArticlesResponse>, response: Response<ArticlesResponse>) {
                if (response.isSuccessful) {
                    response.body()?.data?.let { articles ->
                        val filteredArticles = articles.filterNotNull().filter { it.plantType == "Kopi" }
                        if (filteredArticles.isNotEmpty()) {
                            articlesAdapter = ArticlesAdapter(filteredArticles)
                            binding.verticalRecyclerView.adapter = articlesAdapter
                        } else {
                            Toast.makeText(this@CoffeActivity, "No articles found for Kopi", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this@CoffeActivity, "Failed to load articles", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ArticlesResponse>, t: Throwable) {
                Toast.makeText(this@CoffeActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}