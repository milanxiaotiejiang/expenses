package com.nominalista.expenses.about

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.nominalista.expenses.R
import com.nominalista.expenses.util.extensions.getSupportActionBarOrNull
import com.nominalista.expenses.util.extensions.requireApplication
import com.permissionx.guolindev.PermissionX
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

        Glide.with(this).load(R.mipmap.image_large_2).into(appLogoImageView)

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
            PermissionX.init(this)
                    .permissions(Manifest.permission.CALL_PHONE)
                    .request { allGranted, _, _ ->
                        if (allGranted) {
                            val intent = Intent(Intent.ACTION_CALL)
                            val data = Uri.parse("tel:$CALL_PHONE")
                            intent.data = data
                            startActivity(intent)
                        }
                    }
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
        private const val CALL_PHONE = "18734772024"
    }
}