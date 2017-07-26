/*
 * Copyright (c) 2017 Jalotsav
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jalotsav.fireconfig.utils

import android.view.WindowManager
import android.app.Activity
import android.content.Context
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.view.View
import com.jalotsav.fireconfig.R

/**
 * Created by Jalotsav on 7/22/2017.
 */
object ValidationUtils {

    // Check validation for Password
    fun validatePassword(context: Context, mTxtinptlyotPaswrd: TextInputLayout, mTxtinptEtPaswrd: TextInputEditText, errorMsg: String, charCount: Int): Boolean {

        if (mTxtinptEtPaswrd.text.toString().trim().isEmpty()) {
            mTxtinptlyotPaswrd.isErrorEnabled = true
            mTxtinptlyotPaswrd.error = errorMsg
            requestFocus(context, mTxtinptEtPaswrd)
            return false
        } else if (mTxtinptEtPaswrd.text.toString().trim().length < charCount) {
            mTxtinptlyotPaswrd.isErrorEnabled = true
            mTxtinptlyotPaswrd.error = context.getString(R.string.password_long_charctr_msg, charCount)
            requestFocus(context, mTxtinptEtPaswrd)
            return false
        } else {
            mTxtinptlyotPaswrd.error = null
            mTxtinptlyotPaswrd.isErrorEnabled = false
            return true
        }
    }

    private fun requestFocus(context: Context, view: View) {
        if (view.requestFocus())
            (context as Activity).window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    }
}