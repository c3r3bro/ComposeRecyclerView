package com.example.myapplication.makesale.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.makesale.ui.model.SaleModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class SaleViewModel @Inject constructor() : ViewModel() {
    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog: LiveData<Boolean> = _showDialog

    fun onDialogClose() {
        _showDialog.value = false
    }

    fun onShowDialogClick() {
        _showDialog.value = true
    }

    private var saleList = mutableStateListOf<SaleModel>()
    private val _saleListFlow = MutableStateFlow(saleList)
    val saleListFlow: StateFlow<List<SaleModel>> get() = _saleListFlow

    fun setCheckBox(index: Int, value: Boolean) {
        saleList[index] = saleList[index].copy(selectedCheckBox = value)
    }

    fun setAddQuantity(index: Int) {
        val quantity = saleList[index].quantity + 1
        saleList[index] = saleList[index].copy(quantity = quantity)
    }

    fun setSubtractQuantity(index: Int) {
        val quantity: Int = if (saleList[index].quantity > 1) {
            saleList[index].quantity - 1
        } else {
            1
        }
        saleList[index] = saleList[index].copy(quantity = quantity)
    }

    fun createItem(titleText: String) {
        _showDialog.value = false
        saleList.add(SaleModel(saleList.size, titleText))
    }

    fun removeItem(index: SaleModel) {
        saleList.remove(index)
    }


}