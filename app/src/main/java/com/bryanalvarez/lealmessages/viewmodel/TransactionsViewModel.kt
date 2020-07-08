package com.bryanalvarez.lealmessages.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bryanalvarez.lealmessages.model.Transaction
import com.bryanalvarez.lealmessages.network.APICall
import com.bryanalvarez.lealmessages.network.Callback
import io.realm.RealmResults
import java.lang.Exception

class TransactionsViewModel: ViewModel(){

    var apiCall = APICall()
    var listTransactions: MutableLiveData<RealmResults<Transaction>> = MutableLiveData()
    var isLoading = MutableLiveData<Boolean>()

    fun refresh(context: Context, refreshFromAPI: Boolean){
        isLoading.postValue(false)
        if(refreshFromAPI){
            deleteAll()
        }
        getAPIData(context, refreshFromAPI)
    }

    fun getAPIData(context: Context, refreshFromAPI: Boolean){
        apiCall.getTransactions(context, refreshFromAPI, object : Callback<RealmResults<Transaction>>{
            override fun onSuccess(result: RealmResults<Transaction>?) {
                listTransactions.postValue(result)
                processFinished()
            }

            override fun onFailed(exception: Exception) {
                processFinished()
            }
        })
    }

    fun processFinished(){
        isLoading.postValue(true)
    }

    fun updateTransactionRead(transaction: Transaction){
        val updatedTransactions = apiCall.updateTransactionRead(transaction)
        listTransactions.postValue(updatedTransactions)

    }

    fun deleteTransaction(transaction: Transaction){
        val updatedTransactions = apiCall.deleteTransaction(transaction)
        //updatedTransactions?.remove(transaction)
        listTransactions.postValue(updatedTransactions)
    }

    fun deleteAll(){
        val updatedTransactions = apiCall.deleteAll()
        listTransactions.postValue(updatedTransactions)
    }
}