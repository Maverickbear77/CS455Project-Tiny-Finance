package com.bignerdranch.android.tinyfinance.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.tinyfinance.R
import com.bignerdranch.android.tinyfinance.data.Record


class TransactionFragment: Fragment() {

    interface Callbacks {
        fun onRecordSelected(recordId: Int)
    }

    private lateinit var recordRecyclerView: RecyclerView
    private var adapter: RecordAdapter? = RecordAdapter(emptyList())
    private var callbacks: Callbacks? = null
    private val recordListViewModel: RecordListViewModel by lazy{
        ViewModelProviders.of(this).get(RecordListViewModel::class.java)
    }

    companion object{
        fun newInstance(): TransactionFragment{
            return TransactionFragment()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_transaction, container, false)

        recordRecyclerView = view.findViewById(R.id.record_recycler_view) as RecyclerView
        recordRecyclerView.layoutManager = LinearLayoutManager(context)
        recordRecyclerView.adapter = adapter

        /*System crush if the code below executed
        val message = arguments?.getString("message")
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()*/

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recordListViewModel.recordsLiveData.observe(
            viewLifecycleOwner,
            Observer { records ->
                records?.let{
                    updateUI(records)
                }
            }
        )
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    private fun updateUI(records: List<Record>){
        adapter = RecordAdapter(records)
        recordRecyclerView.adapter = adapter
    }

    private inner class RecordHolder(view: View)
        :RecyclerView.ViewHolder(view), View.OnClickListener{
        private val shopTextView: TextView = itemView.findViewById(R.id.record_shop_name)
        private val amountTextView: TextView = itemView.findViewById(R.id.record_purchase_amount)
        private val dateTextView: TextView = itemView.findViewById(R.id.record_purchase_date)
        private lateinit var record: Record

        init{
            itemView.setOnClickListener(this)
        }

        fun bind(record: Record){
            this.record = record
            shopTextView.text = "Purchase at " + this.record.shop
            amountTextView.text = "Total Amount: $" + this.record.amount.toString()
            dateTextView.text = "Date: " + this.record.date
        }

        override fun onClick(v: View?) {
            callbacks?.onRecordSelected(record.id)
        }
    }

    private inner class RecordAdapter(var records: List<Record>)
        :RecyclerView.Adapter<RecordHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordHolder {
            val view = layoutInflater.inflate(R.layout.list_record, parent, false)
            return RecordHolder(view)
        }

        override fun getItemCount() = records.size

        override fun onBindViewHolder(holder: RecordHolder, position: Int) {
            val record = records[position]
           holder.bind(record)
        }
    }
}