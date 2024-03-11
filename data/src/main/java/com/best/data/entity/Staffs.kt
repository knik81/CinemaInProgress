package com.best.data.entity

import com.best.entity.StaffsInterface
import com.best.entity.StaffsItemInterface

data class Staffs(
    override val item: List<StaffsItemInterface>
): StaffsInterface
