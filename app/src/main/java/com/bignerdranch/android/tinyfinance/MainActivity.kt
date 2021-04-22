package com.bignerdranch.android.tinyfinance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.bignerdranch.android.tinyfinance.fragment.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), Communicator, TransactionFragment.Callbacks {

    //Declare fragments
    private val dashboardFragment = DashboardFragment()
    private val addFragment = AddFragment()
    private val transactionFragment = TransactionFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Load dashboard as the default page
        currentFragment(dashboardFragment)

        //Load user selection for navigation
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

    //Add fragment to callbacks
    override fun onRecordSelected(recordId: Int) {
        val fragment = DetailFragment.newInstance(recordId)
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit()

    }

    //Load fragments
    private fun currentFragment(fragment: Fragment){
        supportFragmentManager.popBackStack()
        val trans = supportFragmentManager.beginTransaction()

        //The if-else statement is to check if user is clicking transaction
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

    //Use communicator to transfer message
    override fun passMessage(message: String) {
        val bundle = Bundle()
        bundle.putString("message", message)
        val trans = this.supportFragmentManager.beginTransaction()
        trans.replace(R.id.fragment_container, transactionFragment)
        trans.commit()
    }
}