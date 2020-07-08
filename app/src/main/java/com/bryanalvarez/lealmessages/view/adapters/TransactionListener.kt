package com.bryanalvarez.lealmessages.view.adapters

import com.bryanalvarez.lealmessages.model.Transaction

interface TransactionListener {
    fun onTransactionClicked(transaction: Transaction, position: Int )
    fun onTransactionDeleted(transaction: Transaction)
}