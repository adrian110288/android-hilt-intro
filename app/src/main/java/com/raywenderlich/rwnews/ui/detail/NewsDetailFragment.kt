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
package com.raywenderlich.rwnews.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.raywenderlich.rwnews.R
import com.raywenderlich.rwnews.presenter.NewsDetailPresenter
import com.raywenderlich.rwnews.repository.entity.News
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * The Fragment for the news detail
 */
@AndroidEntryPoint
class NewsDetailFragment : Fragment(), NewsDetailView {

  @Inject
  lateinit var newsDetailPresenter: NewsDetailPresenter

  private lateinit var newsTitleTextView: TextView
  private lateinit var newsBodyTextView: TextView

  companion object {

    const val NEWS_ID = "news_id"

    fun create(bundle: Bundle?): NewsDetailFragment = NewsDetailFragment().apply {
      arguments = bundle
    }
  }

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.news_detail_layout, container, false)
    newsDetailPresenter.bind(this)
    newsTitleTextView = view.findViewById(R.id.news_detail_title)
    newsBodyTextView = view.findViewById(R.id.news_detail_body)
    return view
  }

  override fun onStart() {
    super.onStart()
    arguments?.getLong(NEWS_ID)?.let { newsId ->
      newsDetailPresenter.displayNewsDetail(newsId)
    }
  }

  override fun displayNews(news: News) {
    newsTitleTextView.text = news.title
    newsBodyTextView.text = news.body
  }

  override fun onDestroy() {
    super.onDestroy()
    newsDetailPresenter.unbind()
  }
}