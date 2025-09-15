package com.example.asteroidapp.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun dimenToSp(value: Int): TextUnit {
    return dimensionResource(id = value).value.sp
}
