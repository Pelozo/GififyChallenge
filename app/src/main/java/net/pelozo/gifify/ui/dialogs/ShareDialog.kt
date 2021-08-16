package net.pelozo.gifify.ui.dialogs

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import net.pelozo.gifify.R
import net.pelozo.gifify.glideLoad

class ShareDialog(private val url: String): DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.dialog_share_gif, container)

        //set up dialog
        rootView.findViewById<ImageView>(R.id.iv_full_gif).glideLoad(url)
        rootView.findViewById<Button>(R.id.bt_close).setOnClickListener { dismiss() }
        rootView.findViewById<Button>(R.id.bt_share).setOnClickListener {
            val shareIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(
                    Intent.EXTRA_TEXT, getString(
                        R.string.share_message,
                        url,
                        getString(R.string.app_name)
                    )
                )
                setType("text/plain");
            }
            startActivity(Intent.createChooser(shareIntent, getString(R.string.send_to)))
        }
        return rootView
    }

    override fun onResume() {
        super.onResume()
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog?.window?.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        val window = dialog?.window
        window!!.attributes = lp

    }

}