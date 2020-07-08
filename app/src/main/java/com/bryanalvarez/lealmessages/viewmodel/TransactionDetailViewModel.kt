package com.bryanalvarez.lealmessages.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bryanalvarez.lealmessages.model.Detail
import com.bryanalvarez.lealmessages.model.Transaction
import com.bryanalvarez.lealmessages.model.User
import com.bryanalvarez.lealmessages.network.APICall
import com.bryanalvarez.lealmessages.network.Callback
import java.lang.Exception

class TransactionDetailViewModel: ViewModel(){
    var apiCall = APICall()
    var user: MutableLiveData<User> = MutableLiveData()
    var detail: MutableLiveData<Detail> = MutableLiveData()
    var isLoading = MutableLiveData<Boolean>()

    fun getUserData(context: Context, idUser: String){
        apiCall.getUserData(context, idUser,object : Callback<User> {
            override fun onSuccess(result: User?) {
                user.postValue(result)
                processFinished()
            }

            override fun onFailed(exception: Exception) {
                processFinished()
            }
        })
    }

    fun getDetailData(context: Context, idTransaction: Int){
        apiCall.getTransactionDetail(context, idTransaction,object : Callback<Detail> {
            override fun onSuccess(result: Detail?) {
                detail.postValue(result)
                processFinished()
            }

            override fun onFailed(exception: Exception) {
                processFinished()
            }
        })
    }

    fun processFinished(){
        isLoading.value = true
    }
}