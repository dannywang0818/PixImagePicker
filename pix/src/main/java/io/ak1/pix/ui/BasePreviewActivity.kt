package io.ak1.pix.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import io.ak1.pix.adapters.PreviewPagerAdapter
import io.ak1.pix.databinding.ActivityMediaPreviewBinding

abstract class BasePreviewActivity<B : ActivityMediaPreviewBinding>(private val fragmentLayout: Int) :
    AppCompatActivity() {


    abstract val binding: B
    private lateinit var mPager: ViewPager2
    private var pagerAdapter: PreviewPagerAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        mPager = binding.pager
        mPager.registerOnPageChangeCallback(pageChangeCallback());
        pagerAdapter = PreviewPagerAdapter(this)
//        mPager.setAdapter(mAdapter)
    }

    private fun pageChangeCallback(): ViewPager2.OnPageChangeCallback =
        object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    // you are on the first page
                } else if (position == 1) {
                    // you are on the second page
                } else if (position == 2) {
                    // you are on the third page
                }
                super.onPageSelected(position)
            }
        }
}