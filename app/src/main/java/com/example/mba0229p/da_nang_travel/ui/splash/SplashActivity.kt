package com.example.mba0229p.da_nang_travel.ui.splash

import android.content.Intent
import android.os.Bundle
import com.example.mba0229p.da_nang_travel.R
import com.example.mba0229p.da_nang_travel.extension.moveActivity
import com.example.mba0229p.da_nang_travel.ui.base.BaseActivity
import com.example.mba0229p.da_nang_travel.ui.main.MainActivity
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class SplashActivity : BaseActivity() {
    var disposable: Disposable? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        disposable = Observable
                .interval(1, TimeUnit.SECONDS)
                .subscribe {
                    moveActivity(Intent(this, MainActivity::class.java))
                    disposable?.dispose()
                    finish()
                }
//        moveActivity(Intent(this, MainActivity::class.java))
    }
}
