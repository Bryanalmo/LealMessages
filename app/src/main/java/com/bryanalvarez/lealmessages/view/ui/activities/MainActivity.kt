package com.bryanalvarez.lealmessages.view.ui.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.bryanalvarez.lealmessages.R
import com.bryanalvarez.lealmessages.viewmodel.TransactionsViewModel
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: TransactionsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        configNav()

        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(config)
        viewModel = ViewModelProviders.of(this).get(TransactionsViewModel::class.java)

        fabRefresh.setOnClickListener {
            viewModel.refresh(this, true)
        }

    }


    fun configNav() {
        NavigationUI.setupWithNavController(bottomAppBar, Navigation.findNavController(this, R.id.fragContent))
    }
}
