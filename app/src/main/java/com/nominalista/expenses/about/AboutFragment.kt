package com.nominalista.expenses.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.nominalista.expenses.R
import com.nominalista.expenses.util.extensions.getSupportActionBarOrNull
import com.nominalista.expenses.util.extensions.requireApplication
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_about.*

class AboutFragment : Fragment() {
    private val disposables = CompositeDisposable()

    private lateinit var model: AboutFragmentModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupModel()
    }

    private fun setupModel() {
        val factory = AboutFragmentModel.Factory(requireApplication())
        model = ViewModelProviders.of(this, factory).get(AboutFragmentModel::class.java)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupActionBar()
        setupListeners()
    }

    private fun setupActionBar() {
        val actionBar = getSupportActionBarOrNull() ?: return
        actionBar.setTitle(R.string.about)
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_24dp)
        setHasOptionsMenu(true)
    }

    private fun setupListeners() {
        contactButton.setOnClickListener {
            // TODO: 2020/12/8 打电话给我
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposables.clear()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> backSelected()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun backSelected(): Boolean {
        requireActivity().onBackPressed()
        return true
    }

    companion object {
        private const val TAG = "AboutFragment"
    }
}