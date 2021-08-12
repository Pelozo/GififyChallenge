package net.pelozo.gifify.ui.home

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import net.pelozo.gifify.R
import net.pelozo.gifify.glideLoad
import net.pelozo.gifify.showSnackbar
import net.pelozo.gifify.ui.home.HomeViewModel.Event
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private val viewmodel: HomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        setUpToolbar(root.findViewById(R.id.toolbar))
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewmodel.uiState.collect { uiState ->
                    when (uiState) {
                        is Event.ShowLoading -> progressbar.visibility = View.VISIBLE
                        is Event.DismissLoading -> progressbar.visibility = View.GONE
                        is Event.ShowGifs -> view.findViewById<RecyclerView>(R.id.rv_gifs).adapter =
                            GifRecyclerviewAdapter(
                                uiState.gifs,
                                viewmodel
                            )
                        is Event.OpenShareDialog -> openShareDialog(uiState.url)
                        is Event.ShowSnackBar -> showSnackbar(uiState.text)//{Snackbar.make(view, "hey", Snackbar.LENGTH_SHORT).show()}
                    }
                }
            }
        }
    }

    private fun setUpToolbar(toolbar: Toolbar){
        toolbar.title = getString(R.string.nav_home)
        toolbar.inflateMenu(R.menu.toolbar_menu)
    }
    private fun openShareDialog(url: String){
        context?.let {
            val dialog = Dialog(it)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_share_gif)


            //set up dialog
            dialog.findViewById<ImageView>(R.id.iv_full_gif).glideLoad(url)
            dialog.findViewById<Button>(R.id.bt_close).setOnClickListener { dialog.dismiss() }
            dialog.findViewById<Button>(R.id.bt_share).setOnClickListener {

                val shareIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, getString(R.string.share_message, url, getString(R.string.app_name)))
                    setType("text/plain");
                }
                startActivity(Intent.createChooser(shareIntent, getString(R.string.send_to)))
            }


            //set dialog size
            val lp = WindowManager.LayoutParams()
            lp.copyFrom(dialog.window!!.attributes)
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.height = WindowManager.LayoutParams.MATCH_PARENT
            val window = dialog.window
            window!!.attributes = lp


            //display dialog
            dialog.show()
        }
    }

}