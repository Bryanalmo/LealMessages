package com.bryanalvarez.lealmessages.view.ui.fragments


import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.bryanalvarez.lealmessages.R
import com.bryanalvarez.lealmessages.model.Detail
import com.bryanalvarez.lealmessages.model.Transaction
import com.bryanalvarez.lealmessages.model.User
import com.bryanalvarez.lealmessages.viewmodel.TransactionDetailViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_transaction_detail.*
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.LocalDate

/**
 * A simple [Fragment] subclass.
 */
class TransactionDetailFragment : DialogFragment() {

    private lateinit var viewModel: TransactionDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transaction_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbarTransaction.navigationIcon= ContextCompat.getDrawable(view.context, R.drawable.ic_close_black_24dp)
        toolbarTransaction.setTitleTextColor(Color.WHITE)
        toolbarTransaction.setNavigationOnClickListener{
            dismiss()
        }

        val transaction = arguments?.getSerializable("transaction") as Transaction

        viewModel = ViewModelProviders.of(this).get(TransactionDetailViewModel::class.java)
        viewModel.getUserData(view.context, transaction.userId)
        viewModel.getDetailData(view.context, transaction.transactionId)
        observeViewModel()

        toolbarTransaction.title =  " Transaction #${transaction.transactionId}"
        tvDetailTranTitle.text =  " Transaction #${transaction.transactionId}"
        tvDetailTranCommerce.text = transaction.commerce?.name
        tvDetailTranDate.text = transaction.date
        tvDetailTranBranch.text = transaction.branch
    }

    fun observeViewModel(){
        viewModel.user.observe(this, Observer<User>{
                user ->
                    Glide.with(ivUserPhoto.context)
                        .load(user.photo)
                        .apply(RequestOptions.circleCropTransform())
                        .into(ivUserPhoto)
                    var year = user.birthDay.substring(0,4).toInt()
                    var month = user.birthDay.substring(5,7).toInt()
                    var day = user.birthDay.substring(8,10).toInt()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        tvUserBirthDay.text = LocalDate.of(year, month, day).toString()
                    }else{
                        tvUserBirthDay.text = user.birthDay
                    }
                    tvUserName.text = user.name


        })

        viewModel.detail.observe(this, Observer<Detail>{
                detail ->
                val format = NumberFormat.getCurrencyInstance();
                tvDetailTranPoints.text = detail.points.toString()
                tvDetailTranValue.text = format.format(detail.value)


        })

        viewModel.isLoading.observe(this, Observer<Boolean>{
            if(it != null) rlBaseDetailTransaction.visibility = View.INVISIBLE
        })
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }


}
