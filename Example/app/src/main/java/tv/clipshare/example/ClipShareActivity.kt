package tv.clipshare.example

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import tv.clipshare.example.databinding.ActivitySnapBinding
import tv.clipshare.mobile.model.Clip
import tv.clipshare.mobile.ui.ClipShareFragment
import tv.clipshare.mobile.ui.ClipShareFragmentResultListener

class ClipShareActivity: AppCompatActivity(), ClipShareFragmentResultListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tvChannelId = intent.getIntExtra("tvChannelId", 0)
        val snapTimestamp = intent.getLongExtra("snapTimestamp", 0)
        val authorizationHeader = intent.getStringExtra("authorizationHeader") ?: ""
        val epgUnitNameFallback = intent.getStringExtra("epgUnitNameFallback")
        val tvChannelLogoUrl = intent.getStringExtra("tvChannelLogoUrl")
        val tvChannelName = intent.getStringExtra("tvChannelName")

        val binding = ActivitySnapBinding.inflate(layoutInflater)
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(
            R.id.frame, ClipShareFragment.newInstance(
            tvChannelId = tvChannelId,
            tvChannelLogoUrl = tvChannelLogoUrl,
            tvChannelName = tvChannelName,
            epgUnitNameFallback = epgUnitNameFallback,
            snapTimestamp = snapTimestamp,
            snapscreenAuthorizationHeader = authorizationHeader,
            listener = this
        ), "ClipShareFragmentTag")
        ft.commitAllowingStateLoss()
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun clipShareFragmentDidFailLoadingClip(fragment: ClipShareFragment) {
    }

    override fun clipShareFragmentDidFailWithTooShortClip(fragment: ClipShareFragment) {
        AlertDialog.Builder(this, 0)
            .setTitle("Error")
            .setMessage("Failed creating Clip")
            .setPositiveButton("OK") { _, _ ->
                finish()
            }
            .setCancelable(false)
            .create()
            .show()
    }

    override fun clipShareFragmentDidFailCreatingClip(fragment: ClipShareFragment) {
        AlertDialog.Builder(this, 0)
            .setTitle("Error")
            .setMessage("Failed creating Clip")
            .setPositiveButton("OK") { _, _ ->
                finish()
            }
            .setCancelable(false)
            .create()
            .show()
    }

    override fun clipShareFragmentDidCreateClip(fragment: ClipShareFragment, clip: Clip) {
        AlertDialog.Builder(this, 0)
            .setTitle("Created Clip")
            .setMessage("Successfully created clip")
            .setNeutralButton("Copy Clip Video URL") { _, _ ->
                val videoURL = clip.videoURL ?: return@setNeutralButton
                val clipboard = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData: ClipData = ClipData.newPlainText("Video URL", videoURL)
                clipboard.setPrimaryClip(clipData)
            }
            .setPositiveButton("OK") { _, _ ->
            }
            .setCancelable(false)
            .create()
            .show()
    }
}