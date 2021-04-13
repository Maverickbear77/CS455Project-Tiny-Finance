package com.bignerdranch.android.tinyfinance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.bignerdranch.android.tinyfinance.fragment.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), Communicator, TransactionFragment.Callbacks {

    private val dashboardFragment = DashboardFragment()
    private val addFragment = AddFragment()
    private val transactionFragment = TransactionFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        currentFragment(dashboardFragment)
        val navigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navigation.setOnNavigationItemSelectedListener {
            when(it.itemId)
            {
                R.id.ic_dashboard -> currentFragment(dashboardFragment)
                R.id.ic_add -> currentFragment(addFragment)
                R.id.ic_transaction -> currentFragment(transactionFragment)
            }
            true
        }
    }

    override fun onRecordSelected(recordId: Int) {
        val fragment = DetailFragment.newInstance(recordId)
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit()

    }

    private fun currentFragment(fragment: Fragment){
        supportFragmentManager.popBackStack()
        val trans = supportFragmentManager.beginTransaction()

        if (fragment == transactionFragment)
        {
            trans.replace(R.id.fragment_container, TransactionFragment.newInstance())
            trans.commit()
        }
        else
        {
            trans.replace(R.id.fragment_container, fragment)
            trans.commit()
        }
    }

    override fun passMessage(message: String) {
        val bundle = Bundle()
        bundle.putString("message", message)
        val trans = this.supportFragmentManager.beginTransaction()
        trans.replace(R.id.fragment_container, transactionFragment)
        trans.commit()
    }
}