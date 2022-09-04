package id.smaraputra.githubuserapp.viewmodel

import org.junit.Before
import org.junit.Test
import junit.framework.TestCase.assertEquals

class MainViewModelTest {
    private lateinit var mainViewModel: MainViewModel

    @Before
    fun preparation() {
        mainViewModel = MainViewModel()
    }

    @Test
    fun checkLengthSearchedWord() {
        val newWord = "testing"
        mainViewModel.saveSearchWord(newWord)
        assertEquals(mainViewModel.searchWord.length, newWord.length)
    }

    @Test
    fun checkSavedSearchedWord() {
        val newWord = "Smaraputra"
        mainViewModel.saveSearchWord(newWord)
        assertEquals(mainViewModel.searchWord, "Smaraputra")
    }
}