package com.example.moiz.presentation.billing.edit

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moiz.R
import com.example.moiz.data.UserDataStore
import com.example.moiz.databinding.FragmentEditBillingBinding
import com.example.moiz.domain.model.Currency
import com.example.moiz.domain.model.InputCostEntity
import com.example.moiz.presentation.billing.add.AddBillingAdapter
import com.example.moiz.presentation.createTravelList.SpinnerAdapter
import com.example.moiz.presentation.util.FileResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

@AndroidEntryPoint
class EditBillingFragment : Fragment() {

    private lateinit var binding: FragmentEditBillingBinding
    private val viewModel: EditBillingViewModel by viewModels()
    private val args: EditBillingFragmentArgs by navArgs()
    private var currencyList = ArrayList<Currency>()
    private lateinit var adapter: AddBillingAdapter
    var tempImgFile = arrayListOf<FileResult>()
    var camUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentEditBillingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        UserDataStore.getUserToken(requireContext()).asLiveData().observe(viewLifecycleOwner) {
            viewModel.getBillingDetail(args.billingId, "Bearer $it")
        }

        initViews()
    }

    private fun itemOnClick(data: InputCostEntity) {
        viewModel.updateCost(data)
        binding.etPrice.setText(viewModel.totalAmount.toString())
    }

    private fun itemOnChange(data: InputCostEntity) {
        binding.etPrice.setText(viewModel.totalAmount.toString())
    }

    private fun initViews() = with(binding) {
        adapter = AddBillingAdapter(::itemOnClick, ::itemOnChange)
        rvPaidForMembers.layoutManager = LinearLayoutManager(context)
        rvPaidForMembers.adapter = adapter

        viewModel.isValidated.observe(viewLifecycleOwner) {
            tvAddBilling.isEnabled = it
            tvAddBilling.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    if (it) R.color.color_F55C5C else R.color.color_F1F0F0
                )
            )
        }

        viewModel.paramList.observe(viewLifecycleOwner) { data ->
            etName.setText(data.title)
            etPrice.setText(viewModel.totalAmount.toString())
            tvPickerDate.text = data.paid_date
            when (data.category) {
                "food" -> ivCategory.setImageResource(R.drawable.ic_category_food)
                "transportation" -> ivCategory.setImageResource(R.drawable.ic_category_transportation)
                "hotel" -> ivCategory.setImageResource(R.drawable.ic_category_hotel)
                "market" -> ivCategory.setImageResource(R.drawable.ic_category_market)
                "shopping" -> ivCategory.setImageResource(R.drawable.ic_category_shopping)
                else -> ivCategory.setImageResource(R.drawable.ic_category_other)
            }

            viewModel.members.observe(viewLifecycleOwner) { members ->
                members?.let {
                    CoroutineScope(Dispatchers.Main).launch {
                        val temp = ArrayList<InputCostEntity>()
                        withContext(Dispatchers.Default) {
                            data.settlements.forEach { settlement ->
                                members.forEach { member ->
                                    if (member.id == settlement?.user)
                                        temp.add(
                                            InputCostEntity(
                                                settlement?.user!!, true,
                                                member.name.toString(), settlement.amount!!
                                            )
                                        )
                                }
                            }
                        }
                        adapter.submitList(temp)
                    }

                    val membersAdapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        it.map { it.name }
                    )

                    spMembers.adapter = membersAdapter
                    members.forEach { member ->
                        if (member.id == viewModel.paramList.value?.paid_by) {
                            spMembers.setSelection(it.indexOf(member))
                        }
                    }
                    spMembers.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onNothingSelected(parent: AdapterView<*>?) {
                                Timber.d("Nothing Selected")
                            }

                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                viewModel.updateParam(4, it[position].id!!)
                            }
                        }
                }
            }

        }

        currencyList = arrayListOf(
            Currency("$", "USD"),
            Currency("€", "EUR"),
            Currency("₩", "KRW"),
            Currency("¥", "JPY"),
            Currency("£", "GBP"),
        )

        binding.spnCurrency.adapter =
            SpinnerAdapter(requireContext(), R.layout.spinner_currency_item_view, currencyList)
        currencyList.forEach { data ->
            if (data.currencyText == viewModel.paramList.value?.currency) {
                spnCurrency.setSelection(currencyList.indexOf(data))
            }
        }
        binding.spnCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val currency = binding.spnCurrency.getItemAtPosition(p2) as Currency
                viewModel.updateParam(2, currency.currencyText)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }
}