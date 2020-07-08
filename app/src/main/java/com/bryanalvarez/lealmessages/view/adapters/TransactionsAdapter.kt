package com.bryanalvarez.lealmessages.view.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bryanalvarez.lealmessages.R
import com.bryanalvarez.lealmessages.model.Transaction
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*



class TransactionsAdapter(val transactionListener: TransactionListener): RecyclerView.Adapter<TransactionsAdapter.ViewHolder>(){

    var list = ArrayList<Transaction>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(LayoutInflater.from(parent.context)
        .inflate(R.layout.item_transaction, parent, false))

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaction = list[position] as Transaction
        holder.bind(transaction, transactionListener)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun updateData(data: List<Transaction>){
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        transactionListener.onTransactionDeleted(list.get(position))
        list.removeAt(position)
        notifyItemRemoved(position)
    }


    class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
        val tvCommerceName = itemView.findViewById<TextView>(R.id.tvCommerceName)
        val tvTransactionDate = itemView.findViewById<TextView>(R.id.tvTransactionDate)
        val tvBranchName = itemView.findViewById<TextView>(R.id.tvBranchName)
        val vDotRead = itemView.findViewById<View>(R.id.vDotRead)

        fun bind(transaction: Transaction, transactionListener: TransactionListener){

            tvCommerceName.text = transaction.commerce?.name
            tvTransactionDate.text = transaction.date
            tvBranchName.text = transaction.branch
            if(transaction.read){
                vDotRead.setBackgroundResource(R.drawable.shape_dot_read)
            }else{
                vDotRead.setBackgroundResource(R.drawable.shape_dot)
            }


            itemView.setOnClickListener{
                transactionListener.onTransactionClicked(transaction, adapterPosition)
            }
        }
    }
}