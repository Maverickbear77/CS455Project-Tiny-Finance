package com.bignerdranch.android.tinyfinance.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import com.bignerdranch.android.tinyfinance.R
import com.bignerdranch.android.tinyfinance.data.Record
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.*

private lateinit var barChart: BarChart
private lateinit var dateButton: Button
private lateinit var categoryButton: Button

class DashboardFragment: Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        barChart = view.findViewById(R.id.barChart)
        dateButton = view.findViewById(R.id.sort_date) as Button
        categoryButton = view.findViewById(R.id.sort_category) as Button

        setBarChartData("date")

        dateButton.setOnClickListener {
            setBarChartData("date")
        }

        categoryButton.setOnClickListener {
            setBarChartData("category")
        }

        return view
    }

    fun setBarChartData(type: String){
        val xvalue = mutableListOf<String>()
        var label = ""

        if (type == "category")
        {
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
        }
        else
        {
            xvalue.add("Personal")
            xvalue.add("Personal")
            xvalue.add("Personal")
            xvalue.add("Personal")
            xvalue.add("Personal")
            xvalue.add("Personal")
            xvalue.add("Personal")
            xvalue.add("Personal")
            xvalue.add("Personal")
            xvalue.add("P")
            label = "Expense for each day in the last 10 days"
        }

        val entry = mutableListOf<BarEntry>()
        entry.add(BarEntry(20f, 0))
        entry.add(BarEntry(50f, 1))
        entry.add(BarEntry(70f, 2))
        entry.add(BarEntry(30f, 3))
        entry.add(BarEntry(20f, 4))
        entry.add(BarEntry(37f, 5))
        entry.add(BarEntry(58f, 6))
        entry.add(BarEntry(62f, 7))
        entry.add(BarEntry(11f, 8))
        entry.add(BarEntry(11f, 9))

        val dataset = BarDataSet(entry, label)
        dataset.color = resources.getColor(R.color.bar_chart)


        val data =BarData(xvalue, dataset)
        barChart.data = data
        barChart.setBackgroundColor(resources.getColor(R.color.white))
        barChart.animateXY(2000, 2000)
    }
}