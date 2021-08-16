package net.pelozo.gifify.ui.fragments.home

import android.os.Bundle
import android.view.*
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
import net.pelozo.gifify.model.Gif
import net.pelozo.gifify.showSnackbar
import net.pelozo.gifify.ui.adapters.GifPagingAdapter
import net.pelozo.gifify.ui.adapters.GifPagingAdapter.GifListener
import net.pelozo.gifify.ui.dialogs.ShareDialog
import net.pelozo.gifify.ui.fragments.home.HomeViewModel.Event
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : Fragment(), GifListener {

    private val viewmodel: HomeViewModel by viewModel()
    lateinit var adapter: GifPagingAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)


        setUpToolbar(root.findViewById(R.id.toolbar))
        setUpRecycler(root.findViewById(R.id.rv_gifs))

        handleEvents()
        loadGifs()

        return root
    }


    private fun setUpRecycler(recyclerView: RecyclerView) {

        //adapter with listener to pass events to viewmodel
        adapter = GifPagingAdapter(this)
        recyclerView.adapter = adapter

        //display text when list is empty.
        adapter.addLoadStateListener { loadState ->
            if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && adapter.itemCount < 1) {
                recyclerView.isVisible = false
                tv_no_results?.isVisible = true
            } else {
                recyclerView.isVisible = true
                tv_no_results?.isVisible = false
            }
        }

    }

    private fun handleEvents(){
        //handle events from viewmodel
        lifecycleScope.launchWhenStarted {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewmodel.eventsFlow.collect { event ->
                    when (event) {
                        is Event.ShowLoading -> pb_endless.visibility = View.VISIBLE
                        is Event.DismissLoading -> pb_endless.visibility = View.GONE
                        is Event.OpenShareDialog -> openShareDialog(event.url)
                        is Event.ShowMsgAddedToFavs -> showSnackbar(getString(R.string.gif_added_favs))
                    }
                }
            }
        }
    }

    private fun loadGifs(query: String? = null){
        lifecycleScope.launch {
            viewmodel.getGifs(query).collect{ pagingData ->
                adapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
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
                loadGifs()
                return true
            }
        })

    }

    private fun openShareDialog(url: String){
        ShareDialog(url).show(childFragmentManager, null)
    }

    override fun onGifClicked(gif: Gif) {
        viewmodel.gifClicked(gif)
    }

    override fun onGifLongClicked(gif: Gif) {
        viewmodel.gifLongClicked(gif)
    }
}