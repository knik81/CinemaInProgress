package com.example.data.entity

import com.example.entity.StaffsInterface
import com.example.entity.StaffsItemInterface

data class Staffs(
    override val item: List<StaffsItemInterface>
): StaffsInterface
