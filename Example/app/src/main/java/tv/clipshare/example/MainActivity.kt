package tv.clipshare.example

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import tv.clipshare.example.databinding.ActivityMainBinding
import com.snapscreen.mobile.Snapscreen
import com.snapscreen.mobile.model.snap.SportEventSnapResultEntry
import com.snapscreen.mobile.snap.SnapActivity
import com.snapscreen.mobile.snap.SnapActivityResultListener
import com.snapscreen.mobile.snap.SnapConfiguration
import com.snapscreen.mobile.snap.SnapFragment

class MainActivity : AppCompatActivity(), SnapActivityResultListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.snapSportsOperatorButton.setOnClickListener {
            val intent = SnapActivity.intentForSportsOperator(this, SnapConfiguration(), this);
            startActivity(intent)
        }
    }

    override fun snapActivityDidSnapSportEvent(
        activity: SnapActivity,
        fragment: SnapFragment,
        sportEvent: SportEventSnapResultEntry
    ) {
        activity.finish()

        AlertDialog.Builder(this, 0)
            .setTitle("Snapped Sport Event")
            .setMessage("Snapped Sport Event with ID ${sportEvent.sportEvent?.eventId} on channel ${sportEvent.tvChannel?.name}")
            .setNeutralButton("Show ClipShare") { dialog, which ->
                val intent = Intent(this, ClipShareActivity::class.java).apply {
                    putExtra("tvChannelId", sportEvent.tvChannel?.id)
                    putExtra("snapTimestamp", sportEvent.timestamp)
                    putExtra("authorizationHeader", Snapscreen.instance?.authorizationHeader)
                    putExtra("epgUnitNameFallback", sportEvent.sportEvent?.league ?: sportEvent.sportEvent?.tournament ?: sportEvent.sportEvent?.sport)
                    putExtra("tvChannelLogoUrl", sportEvent.tvChannel?.logoURL)
                    putExtra("tvChannelName", sportEvent.tvChannel?.name)
                }
                startActivity(intent)
            }
            .setPositiveButton("OK") { _, _ ->
            }
            .setCancelable(false)
            .create()
            .show()
    }

}