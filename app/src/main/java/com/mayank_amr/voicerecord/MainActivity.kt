package com.mayank_amr.voicerecord

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.mayank_amr.voicerecord.adapters.ViewPagerAdapter
import com.mayank_amr.voicerecord.recorder.RecorderFragment
import com.mayank_amr.voicerecord.recordings.RecordingsFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), TabLayout.OnTabSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //checkUserPermission()
        settingUpTabs()
    }

    /*----------------------------*** Setting the tabs ***----------------------------------------*/
    private fun settingUpTabs() {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(RecorderFragment(), "Recorder") // adding fragment to adapterlist
        adapter.addFragment(RecordingsFragment(), "Recordings")
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)

        // Adding Tab ChangeListener...................
        tabLayout.addOnTabSelectedListener(this)

    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        //Toast.makeText(this, tab?.text, Toast.LENGTH_SHORT).show()
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabReselected(tab: TabLayout.Tab?) {

    }
}