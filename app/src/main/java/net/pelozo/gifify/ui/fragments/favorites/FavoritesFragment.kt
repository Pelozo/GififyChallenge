package net.pelozo.gifify.ui.fragments.favorites

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog.Builder
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_favorites.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import net.pelozo.gifify.R
import net.pelozo.gifify.model.Gif
import net.pelozo.gifify.ui.adapters.GifAdapter
import net.pelozo.gifify.ui.dialogs.ShareDialog
import net.pelozo.gifify.ui.fragments.favorites.FavoritesViewModel.Event
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment(), GifAdapter.GifListener {

    private val viewmodel: FavoritesViewModel by viewModel()
    val adapter: GifAdapter = GifAdapter(emptyList(), this)
    lateinit var recycler: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_favorites, container, false)

        setUpToolbar(root.findViewById(R.id.toolbar))

        recycler = root.findViewById(R.id.rv_favs)
        recycler.adapter = adapter

        handleEvents()
        return root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        lifecycleScope.launchWhenStarted {
            viewmodel.getGifs().collectLatest{ gifs ->
                tv_no_favs.isVisible = gifs.isEmpty()
                setUpRecycler(gifs)
            }
        }
    }

    private fun setUpRecycler(gifs: List<Gif>) {
        adapter.setData(gifs)
    }


    private fun setUpToolbar(toolbar: Toolbar) {
        toolbar.title = getString(R.string.nav_favorites)
    }

    override fun onGifClicked(gif: Gif) {
        viewmodel.gifClicked(gif)
    }

    override fun onGifLongClicked(gif: Gif) {
        viewmodel.gifLongClicked(gif)
    }

    private fun handleEvents(){
        //handle events from viewmodel
        lifecycleScope.launchWhenStarted {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewmodel.eventsFlow.collect { event ->
                    when (event) {
                        is Event.OpenShareDialog -> openShareDialog(event.url)
                        is Event.OpenDeleteDialog-> openDeleteDialog(event.gif)
                    }
                }
            }
        }
    }

    private fun openShareDialog(url: String){
        ShareDialog(url).show(childFragmentManager, null)
    }

    private fun openDeleteDialog(gif: Gif){
        val builder = Builder(requireContext())
        builder.setMessage(getString(R.string.delete_gif_confirmation, gif.title))
            .setPositiveButton(getString(R.string.yes)){ _, _ -> viewmodel.deleteGif(gif) }
            .setNegativeButton(getString(R.string.no)){ dialog, _ -> dialog.dismiss()}
            .show()

    }

}