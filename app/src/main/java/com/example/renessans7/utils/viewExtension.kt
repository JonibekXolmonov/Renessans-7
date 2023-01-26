package com.example.renessans7.utils

import android.content.Context
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.progressindicator.CircularProgressIndicatorSpec
import com.google.android.material.progressindicator.IndeterminateDrawable
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.enableError(msg: String) {
    isErrorEnabled = true
    error = msg
}

fun AppCompatEditText.isNotBlank() = text.toString().isNotBlank()

fun TextInputEditText.isErrorOrBlank(layout: TextInputLayout): Boolean {
    return layout.isErrorEnabled || !isNotBlank()
}

fun EditText.isNotEmpty() = text.isNotEmpty()

fun TextInputEditText.isNotBlank() = text.toString().isNotBlank()

fun Fragment.toast(msg: String) {
    Toast.makeText(requireActivity(), msg, Toast.LENGTH_LONG).show()
}

private fun TabLayout.setUp(viewPager: ViewPager2, labels: List<String>) {
    if (labels.size != viewPager.adapter?.itemCount)
        throw Exception()

    TabLayoutMediator(this, viewPager) { tab, position ->
        tab.text = labels[position]
    }.attach()
}

fun View.show() = kotlin.run {
    visibility = View.VISIBLE
}

fun View.hide() = kotlin.run {
    visibility = View.GONE
}

fun View.invisible() = kotlin.run {
    visibility = View.INVISIBLE
}

fun MaterialCardView.setStroke(width: Int, color: Int) {
    strokeWidth = width
    strokeColor = color
}

fun MaterialButton.setLoading(context: Context) {
    val spec = CircularProgressIndicatorSpec(
        context,  /*attrs=*/
        null,
        0,
        com.google.android.material.R.style.Widget_Material3_CircularProgressIndicator_ExtraSmall
    )
    val progressIndicatorDrawable = IndeterminateDrawable.createCircularDrawable(context, spec)
    progressIndicatorDrawable.setColorFilter(
        0xffff0000.toInt(), android.graphics.PorterDuff.Mode.MULTIPLY
    )
    this.icon = progressIndicatorDrawable
}

fun MaterialButton.disableLoading() {
    this.icon = null
}

fun SwipeRefreshLayout.enableRefresh() {
    isRefreshing = true
}

fun SwipeRefreshLayout.disableRefresh() {
    isRefreshing = false
}

fun Fragment.back() {
    requireActivity().onBackPressed()
}

fun Long.getDuration() = String.format(
    "%02d:%02d", (this / 60) % 60, this % 60
)