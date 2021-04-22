package com.bignerdranch.android.tinyfinance.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bignerdranch.android.tinyfinance.R
import com.bignerdranch.android.tinyfinance.data.Record

private const val ARG_RECORD_ID = "record_id"

class DetailFragment : Fragment() {

    private lateinit var record: Record
    private lateinit var dropdownMenu: Spinner
    private lateinit var shopField: EditText
    private lateinit var amountField: EditText
    private lateinit var dateField: EditText
    private lateinit var saveButton: Button
    private lateinit var memoField: EditText
    private lateinit var deleteButton: Button

    //Initialize view model
    private val recordDetailViewModel: RecordDetailViewModel by lazy {
        ViewModelProviders.of(this).get(RecordDetailViewModel::class.java)
    }

    //Flag to check if user enter amount or not
    private var amountEntered = false

    //Category option
    private val categories = mutableListOf("Food",
                                            "Housing",
                                            "Transportation",
                                            "Entertainment",
                                            "Utilities",
                                            "Insurance",
                                            "Education",
                                            "Supplies",
                                            "Personal",
                                            "Others")

    companion object{
        fun newInstance(recordId: Int): DetailFragment{
            val args = Bundle().apply {
                putSerializable(ARG_RECORD_ID, recordId)
            }
            return DetailFragment().apply{
                arguments = args
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        record = Record(0)
        val recordId: Int = arguments?.getSerializable(ARG_RECORD_ID) as Int
        recordDetailViewModel.loadRecord(recordId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail, container, false)
        //Find elements in xml file
        dropdownMenu = view.findViewById(R.id.detail_category) as Spinner
        shopField = view.findViewById(R.id.detail_store) as EditText
        amountField = view.findViewById(R.id.detail_amount) as EditText
        dateField = view.findViewById(R.id.detail_date) as EditText
        memoField = view.findViewById(R.id.detail_memo) as EditText
        saveButton = view.findViewById(R.id.detail_save_button) as Button
        deleteButton = view.findViewById(R.id.detail_delete_button) as Button

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Communicate with database
        recordDetailViewModel.recordLiveData.observe(
            viewLifecycleOwner,
            Observer{record ->
                record?.let{
                    this.record = record
                    updateUI()
                }
            }
        )
    }

    override fun onStart() {
        super.onStart()

        //Check user selection from the drop-down menu
        dropdownMenu.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                record.category = categories.get(position)
            }
        }

        //Check user input
        val shopWatcher = object : TextWatcher {
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
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        //Check user input
        val amountWatcher = object : TextWatcher {
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
                }
            }

            override fun afterTextChanged(sequence: Editable?) {}
        }

        //Check user input
        val dateWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                record.date = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        //Check user input
        val memoWatcher = object : TextWatcher {
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
                recordDetailViewModel.updateRecord(record)
                Toast.makeText(activity, R.string.update_message, Toast.LENGTH_SHORT).show()
            }
        }
        deleteButton.setOnClickListener {
            recordDetailViewModel.deleteRecord(record)
            resetInput()
            Toast.makeText(activity, R.string.delete_message, Toast.LENGTH_SHORT).show()
        }
    }

    //Update user input
    private fun updateUI(){
        shopField.setText(record.shop)
        amountField.setText(record.amount.toString())
        dateField.setText(record.date)
        memoField.setText(record.memo)

        updateCategory(record.category)

        val arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, categories)
        dropdownMenu.adapter = arrayAdapter

    }

    private fun updateCategory(item: String){
        categories.remove(item)
        categories.add(0, item)
    }

    //Valide input data
    private fun isValid(): Boolean{
        //If user input nothing
        if (record.shop == "" || record.amount == 0.0 || record.date == "")
        {
            Toast.makeText(activity, R.string.empty_record, Toast.LENGTH_SHORT).show()
            return false
        }
        //Check date format
        else if (record.date.length != 10 || record.date[4] != '-' || record.date[7] != '-')
        {
            Toast.makeText(activity, R.string.wrong_date, Toast.LENGTH_SHORT).show()
            return false
        }
        //Check date input
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

    //Reset input
    private fun resetInput(){
        shopField.setText(null)
        amountField.setText(null)
        memoField.setText(null)
        dateField.setText(null)
        dropdownMenu.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, categories)
    }
}

