package net.pelozo.gifify.ui.home

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
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
    lateinit var adapter: GifAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        setUpToolbar(root.findViewById(R.id.toolbar))

        val recyclerView = root.findViewById<RecyclerView>(R.id.rv_gifs)
        adapter = GifAdapter(viewmodel)

        recyclerView.adapter = adapter

        //display text when list is empty.
        adapter.addLoadStateListener { loadState ->
            if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && adapter.itemCount < 1) {
                recyclerView?.isVisible = false
                tv_no_results?.isVisible = true
            } else {
                recyclerView?.isVisible = true
                tv_no_results?.isVisible = false
            }
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //handle events from viewmodel
        lifecycleScope.launchWhenStarted {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewmodel.eventsFlow.collect { event ->
                    when (event) {
                        is Event.ShowLoading -> pb_endless.visibility = View.VISIBLE
                        is Event.DismissLoading -> pb_endless.visibility = View.GONE
                        is Event.OpenShareDialog -> openShareDialog(event.url)
                        is Event.ShowSnackBar -> showSnackbar(event.text)
                    }
                }
            }
        }
        loadGifs()
    }

    private fun loadGifs(query: String? = null){
        lifecycleScope.launch {
            viewmodel.getGifs(query).collect{ pagingData ->
                adapter.submitData(pagingData)

            }
        }
    }

    private fun setUpToolbar(toolbar: Toolbar){
        toolbar.title = getString(R.string.nav_home)
        toolbar.inflateMenu(R.menu.toolbar_menu)
        val searchview = toolbar.menu.findItem(R.id.searchView).actionView as SearchView

        //add listener used to search gifs
        searchview.apply{
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    if (query.isNotEmpty()) {
                        loadGifs(query)
                        return true
                    }
                    return false
                }

                override fun onQueryTextChange(p0: String): Boolean {
                    //not used
                    return false
                }
            })

        }

        //when closing searchview return to trends
        val item = toolbar.menu.findItem(R.id.searchView)
        item.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(menuItem: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(menuItem: MenuItem?): Boolean {
                //TODO when no
                loadGifs()
                return true
            }
        })

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


            //set dialog size
            val lp = WindowManager.LayoutParams()
            lp.copyFrom(dialog.window!!.attributes)
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            val window = dialog.window
            window!!.attributes = lp


            //display dialog
            dialog.show()
        }
    }



}