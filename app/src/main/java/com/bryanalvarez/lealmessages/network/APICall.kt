package com.bryanalvarez.lealmessages.network

import android.content.Context
import android.os.Build
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bryanalvarez.lealmessages.model.Commerce
import com.bryanalvarez.lealmessages.model.Detail
import com.bryanalvarez.lealmessages.model.Transaction
import com.bryanalvarez.lealmessages.model.User
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import io.realm.kotlin.where
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDate


class APICall {

    lateinit var stringRequest: StringRequest
    lateinit var request: RequestQueue
    var transactionsNumber = 0
    var transactionsSaved = 0
    var realm: Realm

    init {
        realm = Realm.getDefaultInstance()
    }

    fun getTransactions(context: Context, refreshFromAPI: Boolean, callback: Callback<RealmResults<Transaction>>){
        var transactions = realm.where(Transaction::class.java).findAll()
        if(!(transactions.size > 0) || refreshFromAPI){
            getTransactionsAPI(context, callback)
        }else{
            uploadDataFromRealm(callback)
        }

    }

    fun getTransactionsAPI(context: Context, callback: Callback<RealmResults<Transaction>>){
        request = Volley.newRequestQueue(context)
        var url = "https://mobiletest.leal.co/transactions".replace(" ", "%20")
        stringRequest = StringRequest(Request.Method.GET, url,
            Response.Listener<String> { response ->
                var responseJSON: JSONArray = JSONArray(response)
                for (i in 0 until responseJSON.length()) {
                    val transactionData = responseJSON.getJSONObject(i)
                    val id = transactionData.getInt("id")
                    var date = transactionData.getString("createdDate")
                    val userId = transactionData.getString("userId")
                    val branch = transactionData.getJSONObject("branch").getString("name")
                    val read = (i<20)
                    var year = date.substring(0,4).toInt()
                    var month = date.substring(5,7).toInt()
                    var day = date.substring(8,10).toInt()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        date = LocalDate.of(year, month, day).toString()
                    }

                    val commerceData = transactionData.getJSONObject("commerce")
                    val commerceName = commerceData.getString("name")
                    val commerceId = commerceData.getString("id")
                    val commerce = Commerce(commerceId, commerceName)


                    val transaction = Transaction(id, commerce, date, userId, branch,!read)
                    addToRealm(transaction)
                }
                uploadDataFromRealm(callback)

            },
            Response.ErrorListener { error ->
                Log.e("Error",error.message)
            }
        )

        request.add(stringRequest)
    }

    fun getTransactionDetail(context: Context,transactionId: Int, callback: Callback<Detail>){
        request = Volley.newRequestQueue(context)
        var url = "https://mobiletest.leal.co/transactions/${transactionId}/info".replace(" ", "%20")
        stringRequest = StringRequest(Request.Method.GET, url,
            Response.Listener<String> { response ->
                var responseJSON = JSONObject(response)
                val points = responseJSON.getInt("points")
                val value = responseJSON.getInt("value")
                val detail = Detail(value, points)
                callback.onSuccess(detail)

            },
            Response.ErrorListener { error ->
                Log.e("Error",error.message)
            }
        )
        request.add(stringRequest)
    }

    fun addToRealm(transaction: Transaction){
        realm.beginTransaction()
        realm.copyToRealm(transaction)
        realm.commitTransaction()
    }

    fun uploadDataFromRealm(callback: Callback<RealmResults<Transaction>>){
        var transactions = realm.where(Transaction::class.java).sort("transactionId", Sort.ASCENDING)
            .findAll()
        callback.onSuccess(transactions)
    }

    fun deleteTransaction(transaction: Transaction): RealmResults<Transaction>? {
        val rows = realm.where(Transaction::class.java!!).equalTo("transactionId", transaction.transactionId).findFirst()
        realm.beginTransaction()
        rows?.deleteFromRealm()
        realm.commitTransaction()
        return realm.where(Transaction::class.java)
                .sort("transactionId", Sort.ASCENDING)
                .findAll()
    }

    fun deleteAll(): RealmResults<Transaction>? {
        val rows = realm.where(Transaction::class.java!!).findAll()
        realm.beginTransaction()
        rows.deleteAllFromRealm()
        realm.commitTransaction()
        return realm.where(Transaction::class.java).findAll()
    }

    fun updateTransactionRead(transaction: Transaction): RealmResults<Transaction>? {
        val transactionUp = realm.where(Transaction::class.java!!).equalTo("transactionId", transaction.transactionId).findFirst()
        realm.beginTransaction()
        if(transaction != null){
            transactionUp?.read = true
        }
        realm.commitTransaction()
        return realm.where(Transaction::class.java)
                .sort("transactionId", Sort.ASCENDING)
                .findAll()
    }

    fun getUserData(context: Context, idUser: String,callback: Callback<User>){
        request = Volley.newRequestQueue(context)
        var url = "https://mobiletest.leal.co/users/${idUser}".replace(" ", "%20")
        stringRequest = StringRequest(Request.Method.GET, url,
            Response.Listener<String> { response ->
                var responseJSON: JSONObject = JSONObject(response)
                val id = responseJSON.getString("id")
                val name = responseJSON.getString("name")
                val birthday = responseJSON.getString("birthday")
                val photo = responseJSON.getString("photo")

                var user = User(id,name,photo,birthday)

                callback.onSuccess(user)

            },
            Response.ErrorListener { error ->
                Log.e("Error",error.message)
            }
        )

        request.add(stringRequest)
    }
}