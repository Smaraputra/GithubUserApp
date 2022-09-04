package id.smaraputra.githubuserapp.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import id.smaraputra.githubuserapp.fragment.FollowFragment

class DetailUserPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    var username: String = ""
    override fun createFragment(position: Int): Fragment {
        val fragments = FollowFragment()
        fragments.arguments = Bundle().apply {
            putInt(FollowFragment.ARG_SECTION_NUMBER, position + 1)
            putString(FollowFragment.ARG_NAME, username)
        }
        return fragments
    }

    override fun getItemCount(): Int {
        return 2
    }
}