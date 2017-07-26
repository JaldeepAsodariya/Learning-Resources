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

package com.jalotsav.fireconfig

import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatButton
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.jalotsav.fireconfig.common.AppConstants
import com.jalotsav.fireconfig.utils.ValidationUtils

/**
 * Created by Jalotsav on 7/22/2017.
 */
class ActvtyMainFire : AppCompatActivity() {

    var mCrdntrlyot: CoordinatorLayout? = null
    var mTxtinptlyotPaswrd: TextInputLayout? = null
    var mTxtinptlyotCnfrmPaswrd:TextInputLayout? = null
    var mTxtinptEtPaswrd: TextInputEditText? = null
    var mTxtinptEtCnfrmPaswrd:TextInputEditText? = null
    var mAppcmptbtnConfirm: AppCompatButton? = null
    var mTvTitle: TextView? = null
    var mPrgrsbrMain: ProgressBar? = null

    var mFireRemoteConfig: FirebaseRemoteConfig? = null
    var mPasswordCharLength: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lo_actvty_main_fire)

        mCrdntrlyot = findViewById<View>(R.id.cordntrlyot_fireconfig) as CoordinatorLayout
        mTxtinptlyotPaswrd = findViewById<View>(R.id.txtinputlyot_fireconfig_password) as TextInputLayout
        mTxtinptlyotCnfrmPaswrd = findViewById<View>(R.id.txtinputlyot_fireconfig_confirmpassword) as TextInputLayout
        mTxtinptEtPaswrd = findViewById<View>(R.id.txtinptet_fireconfig_password) as TextInputEditText
        mTxtinptEtCnfrmPaswrd = findViewById<View>(R.id.txtinptet_fireconfig_confirmpassword) as TextInputEditText
        mAppcmptbtnConfirm = findViewById<View>(R.id.appcmptbtn_fireconfig_confirm) as AppCompatButton
        mTvTitle = findViewById<View>(R.id.tv_fireconfig_title) as TextView
        mPrgrsbrMain = findViewById<View>(R.id.prgrsbr_fireconfig) as ProgressBar

        // Remote Config Init
        mFireRemoteConfig = FirebaseRemoteConfig.getInstance()
        val mConfigSettings = FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build()
        mFireRemoteConfig?.setConfigSettings(mConfigSettings)
        mFireRemoteConfig?.setDefaults(R.xml.remote_config_defaults)

        // Set Defaults configurations
        updateUIFireConfig()

        // Fetch remote config.
        fetchConfig()

        mAppcmptbtnConfirm?.setOnClickListener({ checkAllValidation() })
    }

    // Fetch Remote Config for get API Root URL
    private fun fetchConfig() {

        mPrgrsbrMain?.visibility = View.VISIBLE
        var cacheExpiration: Long = 3600 // 1 hour in seconds
        if (mFireRemoteConfig?.info?.configSettings?.isDeveloperModeEnabled!!)
            cacheExpiration = 0

        mFireRemoteConfig?.fetch(cacheExpiration)?.addOnCompleteListener(this, object : OnCompleteListener<Void> {
            override fun onComplete(task: Task<Void>) {

                mPrgrsbrMain?.visibility = View.GONE
                if (task.isSuccessful)
                    mFireRemoteConfig?.activateFetched()

                updateUIFireConfig()
            }
        })
    }

    // Update UI with new Default/Remote configurations
    private fun updateUIFireConfig() {

        mPasswordCharLength = mFireRemoteConfig!!.getLong(AppConstants.FIRECONFIG_PASSWORD_CHAR_LENGTH)
        mTvTitle?.visibility = if (mFireRemoteConfig!!.getBoolean(AppConstants.FIRECONFIG_TITLE_VISIBILITY)) View.VISIBLE else View.GONE
    }

    private fun checkAllValidation() {

        if (!ValidationUtils.validatePassword(this, mTxtinptlyotPaswrd as TextInputLayout, mTxtinptEtPaswrd as TextInputEditText, getString(R.string.entr_password_sml), mPasswordCharLength.toInt()))
            return

        if (!ValidationUtils.validatePassword(this, mTxtinptlyotCnfrmPaswrd as TextInputLayout, mTxtinptEtCnfrmPaswrd as TextInputEditText, getString(R.string.entr_confirm_password_sml), mPasswordCharLength.toInt()))
            return

        if (mTxtinptEtPaswrd?.text.toString().trim() == mTxtinptEtCnfrmPaswrd?.text.toString().trim())
            Snackbar.make(mCrdntrlyot!!, getString(R.string.password_matchd_sml), Snackbar.LENGTH_LONG).show()
        else
            Snackbar.make(mCrdntrlyot!!, getString(R.string.password_not_match), Snackbar.LENGTH_LONG).show()
    }
}
