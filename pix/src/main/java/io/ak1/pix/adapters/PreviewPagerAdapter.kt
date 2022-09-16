package io.ak1.pix.adapters

import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import io.ak1.pix.ui.fragments.PageFragment

class PreviewPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
//    override fun getItemCount(): Int = 10
//
//    override fun createFragment(position: Int): Fragment = PreviewPagerFragment()


    private val items: ArrayList<String> = ArrayList<String>()

    init {
        items.add("first item...")
        items.add("second item...")
    }

    override fun createFragment(position: Int): PageFragment {
//        val itemId = items.itemId(position)
//        val itemText = items.getItemById(itemId)
        return PageFragment.newInstance("itemText", "2")
    }

    override fun getItemCount(): Int = items.size
    override fun getItemId(position: Int): Long = position.toLong()
    override fun containsItem(itemId: Long): Boolean = items.contains(itemId.toString())
}