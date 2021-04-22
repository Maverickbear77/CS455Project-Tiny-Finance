package com.bignerdranch.android.tinyfinance.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.bignerdranch.android.tinyfinance.R
import com.bignerdranch.android.tinyfinance.data.Record
import com.bignerdranch.android.tinyfinance.database.RecordRepository
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.*
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "DASHBOARD"

class DashboardFragment: Fragment() {

    private lateinit var barChart: BarChart
    private lateinit var dateButton: Button
    private lateinit var categoryButton: Button
    private lateinit var records: List<Record>

    private var dateFormat = SimpleDateFormat("yyyy-MM-dd")
    private val recordRepository = RecordRepository.get()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        barChart = view.findViewById(R.id.barChart)
        dateButton = view.findViewById(R.id.sort_date) as Button
        categoryButton = view.findViewById(R.id.sort_category) as Button

        //Get today's date
        var todayDate = Calendar.getInstance()
        //Get the date of one day before
        todayDate.add(Calendar.DAY_OF_YEAR, -1)
        //Set it to endDate
        var endDate = dateFormat.format(todayDate.time).toString()
        //Get the date of ten day before
        todayDate.add(Calendar.DAY_OF_YEAR, -9)
        //Set it to startDate
        var startDate = dateFormat.format(todayDate.time).toString()

        //Debugging purpose
        Log.d(TAG, startDate+" "+endDate)

        //Communicate with database
        recordRepository.getRecordByDate(startDate, endDate).observe(
                viewLifecycleOwner,
                androidx.lifecycle.Observer { it
                    records = it
                    setBarChartData("date")
                }
        )

        //Set listener
        dateButton.setOnClickListener {
            setBarChartData("date")
        }

        categoryButton.setOnClickListener {
            setBarChartData("category")
        }

        return view
    }

    private fun setBarChartData(type: String){
        val xvalue = mutableListOf<String>()
        var yvalue = mutableListOf(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f)
        var label = ""
        var date = Calendar.getInstance()

        //If user sort it by category
        if (type == "category")
        {
            //Initialize x axis
            xvalue.add("F")
            xvalue.add("H")
            xvalue.add("T")
            xvalue.add("En")
            xvalue.add("U")
            xvalue.add("I")
            xvalue.add("Ed")
            xvalue.add("S")
            xvalue.add("P")
            xvalue.add("O")
            label = "Expense for each category in the last 10 days"

            //Read each record
            for (i in 0 until records.size)
            {
                //Check category of each record and organize data for y axis
                when (records[i].category)
                {
                    "Food" -> yvalue[0] += records[i].amount.toFloat()
                    "Housing" -> yvalue[1] += records[i].amount.toFloat()
                    "Transportation" -> yvalue[2] += records[i].amount.toFloat()
                    "Entertainment" -> yvalue[3] += records[i].amount.toFloat()
                    "Utilities" -> yvalue[4] += records[i].amount.toFloat()
                    "Insurance" -> yvalue[5] += records[i].amount.toFloat()
                    "Education" -> yvalue[6] += records[i].amount.toFloat()
                    "Supplies" -> yvalue[7] += records[i].amount.toFloat()
                    "Personal" -> yvalue[8] += records[i].amount.toFloat()
                    "Others" -> yvalue[9] += records[i].amount.toFloat()
                }
            }
        }
        //User sort it by date
        else
        {
            //Initialize x axis with date
            for (i in 0 until yvalue.size)
            {
                date.add(Calendar.DAY_OF_YEAR, -1)
                xvalue.add(0, dateFormat.format(date.time).toString())
            }

            label = "Expense for each day in the last 10 days"

            //Check each record
            for (i in 0 until records.size)
            {
                //Set value for y axis
                when (records[i].date)
                {
                    xvalue[0] -> yvalue[0] += records[i].amount.toFloat()
                    xvalue[1] -> yvalue[1] += records[i].amount.toFloat()
                    xvalue[2] -> yvalue[2] += records[i].amount.toFloat()
                    xvalue[3] -> yvalue[3] += records[i].amount.toFloat()
                    xvalue[4] -> yvalue[4] += records[i].amount.toFloat()
                    xvalue[5] -> yvalue[5] += records[i].amount.toFloat()
                    xvalue[6] -> yvalue[6] += records[i].amount.toFloat()
                    xvalue[7] -> yvalue[7] += records[i].amount.toFloat()
                    xvalue[8] -> yvalue[8] += records[i].amount.toFloat()
                    xvalue[9] -> yvalue[9] += records[i].amount.toFloat()
                }
            }
        }

        val entry = mutableListOf<BarEntry>()
        //Initialize data for bar chart
        for (i in 0 until yvalue.size)
        {
            entry.add(BarEntry(yvalue[i], i))
        }

        val dataset = BarDataSet(entry, label)
        //Set values and color for bar chart
        dataset.color = resources.getColor(R.color.bar_chart)
        
        val data = BarData(xvalue, dataset)
        barChart.data = data
        barChart.setBackgroundColor(resources.getColor(R.color.white))
        barChart.animateXY(2000, 2000)
    }
}