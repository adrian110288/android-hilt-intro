/*
 * Copyright (c) 2020 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * This project and source code may use libraries or frameworks that are
 * released under various Open-Source licenses. Use of those libraries and
 * frameworks are governed by their own individual licenses.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.raywenderlich.rwnews.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.raywenderlich.rwnews.R
import com.raywenderlich.rwnews.model.NewsListModel
import com.raywenderlich.rwnews.presenter.NewsListPresenter
import com.raywenderlich.rwnews.ui.detail.NewsDetailFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


/**
 * This is the Fragment for displaying the list of news
 */
@AndroidEntryPoint
class NewsListFragment : Fragment(), NewsListView {

  @Inject
  lateinit var newsListPresenter: NewsListPresenter

  private lateinit var recyclerView: RecyclerView
  private lateinit var newsListViewAdapter: NewsListViewAdapter
  private val newsListModel = NewsListModel(emptyList())

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.news_list_layout, container, false)
    newsListPresenter.bind(this)
    initRecyclerView(view)
    return view
  }

  override fun onStart() {
    super.onStart()
    newsListPresenter.displayNewsList()
  }

  override fun displayNews(newsList: NewsListModel) {
    newsListModel.newsList = newsList.newsList
    newsListViewAdapter.notifyDataSetChanged()
  }

  override fun onDestroy() {
    super.onDestroy()
    newsListPresenter.unbind()
  }

  private fun initRecyclerView(view: View) {
    recyclerView = view.findViewById(R.id.recycler_view)
    recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    val dividerItemDecoration = DividerItemDecoration(
        context,
        LinearLayoutManager.VERTICAL
    )
    recyclerView.addItemDecoration(dividerItemDecoration)
    newsListViewAdapter = NewsListViewAdapter(newsListModel) { selectedNews ->
      val bundle = Bundle().apply {
        putLong(NewsDetailFragment.NEWS_ID, selectedNews?.id ?: -1)
      }
      fragmentManager?.run {
        beginTransaction()
            .replace(R.id.anchor, NewsDetailFragment.create(bundle))
            .addToBackStack(null)
            .commit()
      }
    }
    recyclerView.adapter = newsListViewAdapter
  }
}