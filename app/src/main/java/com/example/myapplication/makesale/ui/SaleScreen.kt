package com.example.myapplication.makesale.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog


@Composable
fun SaleScreen(saleViewModel: SaleViewModel) {
    Box(modifier = Modifier.fillMaxSize()) {

        val showDialog: Boolean by saleViewModel.showDialog.observeAsState(false)
        MainSaleView(saleViewModel)
        AddSaleDialog(
            showDialog,
            onDismiss = { saleViewModel.onDialogClose() },
            onSaleAdded = { saleViewModel.createItem(it) })
        FabDialog(
            Modifier
                .align(Alignment.BottomEnd)
                .padding(10.dp), saleViewModel
        )

    }
}

@Composable
fun MainSaleView(saleViewModel: SaleViewModel) {
    val saleListState = saleViewModel.saleListFlow.collectAsState()
    val lazyListState = rememberLazyListState()

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        LazyColumn(
            //modifier = Modifier.fillMaxHeight(),
            state = lazyListState
        ) {
            items(items = saleListState.value,
                key = { it.id },
                itemContent = { item ->
                val currentItem by rememberUpdatedState(item)
                Card(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                        .pointerInput(Unit) {
                            detectTapGestures(onLongPress = { saleViewModel.removeItem(currentItem) })
                        },
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier
                                .weight(1f)
                                .padding(8.dp),
                            text = item.title
                        )
                        TextButton(onClick = {
                            val index = saleListState.value.indexOf(item)
                            saleViewModel.setAddQuantity(index)
                        }) {
                            Icon(Icons.Filled.Add, contentDescription = "")
                        }
                        Text(
                            modifier = Modifier
                                .padding(8.dp)
                                .width(20.dp),
                            text = item.quantity.toString()
                        )
                        TextButton(onClick = {
                            val index = saleListState.value.indexOf(item)
                            saleViewModel.setSubtractQuantity(index)
                        }) {
                            Icon(Icons.Filled.Add, contentDescription = "")
                        }
                        Checkbox(
                            checked = item.selectedCheckBox,
                            onCheckedChange = {
                                val index = saleListState.value.indexOf(item)
                                saleViewModel.setCheckBox(index, it)
                            }
                        )
                    }
                }

            })
        }
    }
}

@Composable
fun FabDialog(modifier: Modifier, saleViewModel: SaleViewModel) {
    FloatingActionButton(onClick = {
        saleViewModel.onShowDialogClick()
    }, modifier = modifier) {
        Icon(Icons.Filled.Add, contentDescription = "")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSaleDialog(show: Boolean, onDismiss: () -> Unit, onSaleAdded: (String) -> Unit) {
    var mySale by remember { mutableStateOf("") }
    if (show) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Add Sale",
                    fontSize = 18.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.size(16.dp))
                TextField(
                    value = mySale,
                    onValueChange = { mySale = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.size(16.dp))
                Button(onClick = {
                    onSaleAdded(mySale)
                    mySale = ""
                }, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Add Sale")
                }
            }
        }
    }
}


