package net.pelozo.gifify.ui.fragments.favorites


import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import net.pelozo.gifify.MainCoroutineRule
import net.pelozo.gifify.model.Gif
import net.pelozo.gifify.repositories.GifRepository
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertEquals

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class FavoritesViewModelTest: KoinTest{


    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    lateinit var repo: GifRepository
    lateinit var viewmodel: FavoritesViewModel


    val gifList = listOf(
        Gif("0118999",
            "A title",
            "http://fakeOriginal.gif",
            "http://fakeDownsized.gif"),
        Gif(" 881999",
            "Another Title!",
            "http://fakeOriginalElectricBoogaloo.gif",
            "http://fakeDownsizedNowIsPersonal.gif")
    )

    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this);
        viewmodel = FavoritesViewModel(repo)
    }

    @After
    fun after(){
        Mockito.validateMockitoUsage();
    }

    @Test
    fun `getFavorites returns empty list successfully`() = runBlocking {
        //given
        Mockito.`when`(repo.getFavorites()).thenReturn(flow {emit(listOf<Gif>())})
        //when
        val result = viewmodel.getGifs()
        //then
        assertEquals(emptyList(), result.first())
    }

    @Test
    fun `getFavorites returns gif list successfully`() = runBlocking {
        //given
        Mockito.`when`(repo.getFavorites()).thenReturn(flow{emit(gifList)})
        //when
        val result = viewmodel.getGifs()
        //then
        assertEquals(gifList[0].title, result.first()[0].title)
        assertEquals(gifList[1].urlImageOriginal, result.first()[1].urlImageOriginal)
    }

    @Test
    fun `Send OpenDeleteDialog Event after gifLongClick() is called successfully`() = runBlocking {
        //given
        val expected = FavoritesViewModel.Event.OpenDeleteDialog(gifList[0])
        //when
        viewmodel.gifLongClicked(gifList[0])
        //then
        assertEquals(expected, viewmodel.eventsFlow.first())
    }

    @Test
    fun `Send OpenShareDialog Event after gifClick() is called successfully`() = runBlocking {
        //given
        val expected = FavoritesViewModel.Event.OpenShareDialog(gifList[0].urlImageOriginal)
        //When
        viewmodel.gifClicked(gifList[0])
        //then
        assertEquals(expected, viewmodel.eventsFlow.first())
    }

    @Test
    fun `Call repository after deleteGif() is called successfully`() = runBlocking {
        //when
        viewmodel.deleteGif(gifList[0])
        //then
        verify(repo, times(1)).deleteFavorite(gifList[0])
    }



}

