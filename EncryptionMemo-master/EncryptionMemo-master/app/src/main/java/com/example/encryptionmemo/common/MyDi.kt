package com.example.encryptionmemo.common

import com.example.encryptionmemo.model.database.MemoData
import com.example.encryptionmemo.model.database.MemoDatabase
import com.example.encryptionmemo.viewModel.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

var dbPart = module {
    factory { MemoDatabase.getInstance(get()).getDB() }
}

var viewModelPart = module {
    viewModel {
        MainViewModel(get())
    }

    viewModel {
        AddMemoViewModel(get())
    }

    viewModel {
        (index: Int, password: String) ->
        ViewMemoViewModel(index, password, get())
    }

    viewModel {
            (index: Int, password: String) ->
        EditMemoViewModel(index, password, get())
    }
}

var myDiModule = listOf(
    dbPart,
    viewModelPart
)
