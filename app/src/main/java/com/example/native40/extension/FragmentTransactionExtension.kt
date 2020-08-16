/*
 * Copyright (c) 2020. yono-ms
 */

package com.example.native40.extension

import androidx.fragment.app.FragmentTransaction
import com.example.native40.R

fun FragmentTransaction.setCustomAnimationsNav(): FragmentTransaction {
    return this.setCustomAnimations(
        R.anim.nav_enter,
        R.anim.nav_exit,
        R.anim.nav_pop_enter,
        R.anim.nav_pop_exit
    )
}

fun FragmentTransaction.setCustomAnimationsReplace(): FragmentTransaction {
    return this.setCustomAnimations(
        R.anim.replace_enter,
        R.anim.replace_exit
    )
}