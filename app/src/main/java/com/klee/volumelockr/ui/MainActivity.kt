package com.klee.volumelockr.ui

import android.app.AlertDialog
import android.app.Dialog
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.marginTop
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.fragment.app.DialogFragment
import com.google.android.material.color.DynamicColors
import com.klee.volumelockr.R
import com.klee.volumelockr.service.VolumeService

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.fragment_container_view)) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.navigationBars())
            // Apply the insets as padding to the view. Here, set all the dimensions
            // as appropriate to your layout. You can also update the view's margin if
            // more appropriate.
            view.updatePadding(insets.left, insets.top, insets.right, insets.bottom)

            // Return CONSUMED if you don't want the window insets to keep passing down
            // to descendant views.
            WindowInsetsCompat.CONSUMED
        }

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_main)) { v, insets ->
//            val bars = insets.getInsets(
//                WindowInsetsCompat.Type.systemBars()
//                        or WindowInsetsCompat.Type.displayCutout()
//            )
//            v.updatePadding(
//                left = bars.left,
//                top = bars.top,
//                right = bars.right,
//                bottom = bars.bottom,
//            )
//            WindowInsetsCompat.CONSUMED
//        }

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_main)) { v, windowInsets ->
//            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
//            // Apply the insets as a margin to the view. This solution sets
//            // only the bottom, left, and right dimensions, but you can apply whichever
//            // insets are appropriate to your layout. You can also update the view padding
//            // if that's more appropriate.
//            v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
//                topMargin = insets.top
//                leftMargin = insets.left
//                bottomMargin = insets.bottom
//                rightMargin = insets.right
//            }
//
//            // Return CONSUMED if you don't want want the window insets to keep passing
//            // down to descendant views.
//            WindowInsetsCompat.CONSUMED
//        }
    }

    override fun onResume() {
        super.onResume()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkDoNotDisturbPermission()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkDoNotDisturbPermission() {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (!notificationManager.isNotificationPolicyAccessGranted) {
            PolicyAccessDialog().show(supportFragmentManager, PolicyAccessDialog.TAG)
        }
    }

    class PolicyAccessDialog : DialogFragment() {
        companion object {
            const val TAG = "PolicyAccessDialog"
        }

        @RequiresApi(Build.VERSION_CODES.M)
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
            AlertDialog.Builder(requireContext())
                .setMessage(getString(R.string.dialog_policy_access_title))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.dialog_allow)) { _, _ ->
                    startActivity(Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS))
                }
                .create()
    }
}
