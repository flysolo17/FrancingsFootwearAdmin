package com.ketchupzz.francingsfootwearadmin.views.nav.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.ketchupzz.francingsfootwear.utils.LoadingDialog
import com.ketchupzz.francingsfootwearadmin.R
import com.ketchupzz.francingsfootwearadmin.databinding.FragmentOrdersBinding
import com.ketchupzz.francingsfootwearadmin.model.transaction.TransactionStatus
import com.ketchupzz.francingsfootwearadmin.viewmodels.TransactionViewModel


class OrdersFragment : Fragment() {
    private lateinit var binding : FragmentOrdersBinding
    private lateinit var loadingDialog: LoadingDialog
    private val transactionViewModel by viewModels<TransactionViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrdersBinding.inflate(inflater,container,false)
        loadingDialog = LoadingDialog(binding.root.context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        attachTabs()
    }


    private fun attachTabs() {
        val indicator = PurchasesTabAdapter(this, TransactionStatus.entries)
        TabLayoutMediator(binding.tabLayout,binding.pager2.apply { adapter = indicator },true) {tab,position ->
            tab.text = TransactionStatus.entries[position].toString().replace("_"," ")
        }.attach()
    }

    class PurchasesTabAdapter(val fragment: Fragment, private val statusList : List<TransactionStatus>) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int {
            return statusList.size
        }
        override fun createFragment(position: Int): Fragment {
            val fragment = OrderByStatusFragment()
            fragment.arguments = Bundle().apply {
                putInt(POSITION,position)
            }
            return fragment
        }
    }


}