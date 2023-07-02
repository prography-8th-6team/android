package com.example.moiz.presentation.billing.edit

import android.app.Activity
import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moiz.R
import com.example.moiz.data.UserDataStore
import com.example.moiz.databinding.FragmentAddBillingBinding
import com.example.moiz.databinding.FragmentEditBillingBinding
import com.example.moiz.domain.model.Currency
import com.example.moiz.domain.model.InputCostEntity
import com.example.moiz.presentation.billing.BillingViewModel
import com.example.moiz.presentation.billing.add.AddBillingAdapter
import com.example.moiz.presentation.billing.add.AddBillingFragmentArgs
import com.example.moiz.presentation.billing.detail.BillingDetailViewModel
import com.example.moiz.presentation.createTravelList.SpinnerAdapter
import com.example.moiz.presentation.util.FileResult
import com.example.moiz.presentation.util.PermissionUtil
import com.example.moiz.presentation.util.getFileInfo
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar

@AndroidEntryPoint
class EditBillingFragment : Fragment() {

    private lateinit var binding: FragmentEditBillingBinding
    private val viewModel: EditBillingViewModel by viewModels()
    private val args: EditBillingFragmentArgs by navArgs()

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

    private fun initViews() = with(binding) {
        viewModel.data.observe(viewLifecycleOwner) { data ->
            etName.setText(data.title)
            etPrice.setText(data.total_amount)
            tvPickerDate.text = data.paid_date
            setImgCnt(data.images?.size!!)
            when (data.category) {
                "food" -> ivCategory.setImageResource(R.drawable.ic_category_food)
                "transportation" -> ivCategory.setImageResource(R.drawable.ic_category_transportation)
                "hotel" -> ivCategory.setImageResource(R.drawable.ic_category_hotel)
                "market" -> ivCategory.setImageResource(R.drawable.ic_category_market)
                "shopping" -> ivCategory.setImageResource(R.drawable.ic_category_shopping)
                else -> ivCategory.setImageResource(R.drawable.ic_category_other)
            }
        }
    }

    private fun setImgCnt(cnt: Int) {
        if (cnt == 0) {
            binding.ivImg.setImageResource(R.drawable.ic_unselect_img)
            binding.tvImgCnt.visibility = View.GONE
            return
        }

        binding.ivImg.setImageResource(R.drawable.ic_select_img)
        binding.tvImgCnt.visibility = View.VISIBLE
        binding.tvImgCnt.text = cnt.toString()
    }
}