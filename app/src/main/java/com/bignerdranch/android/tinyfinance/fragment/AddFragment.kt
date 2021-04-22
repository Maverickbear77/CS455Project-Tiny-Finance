package com.bignerdranch.android.tinyfinance.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bignerdranch.android.tinyfinance.R
import android.widget.Toast
import com.bignerdranch.android.tinyfinance.data.Record
import com.bignerdranch.android.tinyfinance.database.RecordRepository

private const val TAG = "Add"

class AddFragment : Fragment() {

    private lateinit var record: Record
    private lateinit var dropdownMenu: Spinner
    private lateinit var shopField: EditText
    private lateinit var amountField: EditText
    private lateinit var dateField: EditText
    private lateinit var saveButton: Button
    private lateinit var memoField: EditText
    private lateinit var communicator: Communicator

    private val recordRepository = RecordRepository.get()
    //Flag to check if user enter amount
    private var amountEntered = false

    private val categories = listOf<String>("Food",
                                        "Housing",
                                        "Transportation",
                                        "Entertainment",
                                        "Utilities",
                                        "Insurance",
                                        "Education",
                                        "Supplies",
                                        "Personal",
                                        "Others")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Declare a record
        record = Record(0)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add, container, false)
        //Find elements in xml file
        dropdownMenu = view.findViewById(R.id.category) as Spinner
        shopField = view.findViewById(R.id.store) as EditText
        amountField = view.findViewById(R.id.amount) as EditText
        dateField = view.findViewById(R.id.date) as EditText
        saveButton = view.findViewById(R.id.save_button) as Button
        memoField = view.findViewById(R.id.memo) as EditText

        dropdownMenu.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, categories)

        return view
    }

    override fun onStart() {
        super.onStart()
        resetInput()

        //Check user input from drop-down menu
        dropdownMenu.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                record.category = categories.get(position)
                Log.d(TAG, "HI "+record.category)
            }
        }

        //Check user input
        val shopWatcher = object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var shop = s.toString()
                if (shop.length > 20)
                {
                    record.shop = shop.substring(0, 20)
                }
                else
                {
                    record.shop = shop
                }
                Log.d(TAG, "HI "+ record.shop)

            }

            override fun afterTextChanged(s: Editable?) {}
        }

        //Check user input
        val amountWatcher = object : TextWatcher{
            override fun beforeTextChanged(sequence: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(sequence: CharSequence?, start: Int, before: Int, count: Int) {
                if (sequence.toString() == "" && amountEntered)
                {
                    record.amount = 0.0
                }

                if (sequence.toString() != "")
                {
                    var number = sequence.toString().toDouble()
                    record.amount = (number * 100).toInt() / 100.0
                    amountEntered = true
                    Log.d(TAG, "HI "+record.amount)
                }
            }

            override fun afterTextChanged(sequence: Editable?) {}
        }

        //Check user input
        val dateWatcher = object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                record.date = s.toString()
                Log.d(TAG, "HI "+record.date)
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        //Check user input
        val memoWatcher = object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var memo = s.toString()
                if (memo.length > 30)
                {
                    record.memo = memo.substring(0, 30)
                }
                else
                {
                    record.memo = memo
                }
                Log.d(TAG, "HI "+record.memo)
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        //Set listeners
        shopField.addTextChangedListener(shopWatcher)
        amountField.addTextChangedListener(amountWatcher)
        dateField.addTextChangedListener(dateWatcher)
        memoField.addTextChangedListener(memoWatcher)

        saveButton.setOnClickListener {
            if (isValid())
            {
                //Insert new record
                recordRepository.addNewRecord(record)
                //Call communicator
                communicator = activity as Communicator
                //Pass the message to communicator
                communicator.passMessage("New record created successfully")
            }
        }
    }

    //This function is to valide user input
    private fun isValid(): Boolean{
        //If user enter nothing
        if (record.shop == "" || record.amount == 0.0 || record.date == "")
        {
            Toast.makeText(activity, R.string.empty_record, Toast.LENGTH_SHORT).show()
            return false
        }
        //If date format is not valid
        else if (record.date.length != 10 || record.date[4] != '-' || record.date[7] != '-')
        {
            Toast.makeText(activity, R.string.wrong_date, Toast.LENGTH_SHORT).show()
            return false
        }
        //If input of date is not valid
        else if (record.date.substring(0, 4).toDouble() > 2021 ||
                record.date.substring(5, 7).toDouble() > 12 ||
                record.date.substring(8, 10).toDouble() > 31)
        {
            Toast.makeText(activity, R.string.wrong_date, Toast.LENGTH_SHORT).show()
            return false
        }
        else
        {
            return true
        }
    }

    //Reset all input
    private fun resetInput(){
        shopField.setText(null)
        amountField.setText(null)
        memoField.setText(null)
        dateField.setText(null)
        dropdownMenu.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, categories)
    }
}

