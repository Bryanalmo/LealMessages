package com.bryanalvarez.lealmessages.view.ui.fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.bryanalvarez.lealmessages.R
import com.bryanalvarez.lealmessages.model.Transaction
import com.bryanalvarez.lealmessages.view.adapters.TransactionListener
import com.bryanalvarez.lealmessages.view.adapters.TransactionsAdapter
import com.bryanalvarez.lealmessages.viewmodel.TransactionsViewModel
import kotlinx.android.synthetic.main.fragment_transactions2.*
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.snackbar.Snackbar
import io.realm.RealmResults





/**
 * A simple [Fragment] subclass.
 */
class TransactionsFragment : Fragment(), TransactionListener {


    private lateinit var transactionsAdapter: TransactionsAdapter
    private lateinit var viewModel: TransactionsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transactions2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity ?: return

        viewModel =  ViewModelProviders.of(activity).get(TransactionsViewModel::class.java)
        viewModel.refresh(view.context, false)


        transactionsAdapter = TransactionsAdapter(this)

        rvTransactions.apply {
            layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
            adapter = transactionsAdapter
            addItemDecoration(DividerItemDecoration(rvTransactions.getContext(), DividerItemDecoration.VERTICAL))
        }


        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(transactionsAdapter, view.context))
        itemTouchHelper.attachToRecyclerView(rvTransactions)

        observeViewModel()

        fabDeleteAll.setOnClickListener {
            setAnimationDeleteAll()
        }


    }

    fun setAnimationDeleteAll(){
        val animation = AnimationUtils.loadAnimation(activity, R.anim.delete_animation)
        rvTransactions.startAnimation(animation)
        animation.setAnimationListener(object: Animation.AnimationListener{
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                val snack = Snackbar.make(rvTransactions,"All the transactions've been deleted",Snackbar.LENGTH_SHORT)
                snack.show()
                viewModel.deleteAll()
            }

            override fun onAnimationStart(animation: Animation?) {

            }

        })
    }

    fun observeViewModel(){
        viewModel.listTransactions.observe(this, Observer<RealmResults<Transaction>>{
                transactions -> transactionsAdapter.updateData(transactions)
            Log.d("MILOG","data Change")
        })

        viewModel.isLoading.observe(this, Observer<Boolean>{
            if(it) rlBaseTransactions.visibility = View.INVISIBLE
            else rlBaseTransactions.visibility = View.VISIBLE
        })
    }


    override fun onTransactionClicked(transaction: Transaction, position: Int) {
        var bundle = bundleOf("transaction" to transaction)
        viewModel.updateTransactionRead(transaction)
        findNavController().navigate(R.id.transactionsDetailFragmentDialog, bundle)
    }

    override fun onTransactionDeleted(transaction: Transaction) {
        val snack = Snackbar.make(rvTransactions,"Transaction # ${transaction.transactionId} deleted",Snackbar.LENGTH_SHORT)
        snack.show()
        viewModel.deleteTransaction(transaction)
    }



}
