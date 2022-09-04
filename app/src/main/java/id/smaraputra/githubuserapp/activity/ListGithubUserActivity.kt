package id.smaraputra.githubuserapp.activity

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import android.widget.SearchView
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import id.smaraputra.githubuserapp.R
import id.smaraputra.githubuserapp.SettingPreferences
import id.smaraputra.githubuserapp.adapter.ListUserAdapter
import id.smaraputra.githubuserapp.databinding.ActivityListGithubUserBinding
import id.smaraputra.githubuserapp.response.ItemsItem
import id.smaraputra.githubuserapp.viewmodel.MainViewModel
import id.smaraputra.githubuserapp.viewmodel.SettingViewModel
import id.smaraputra.githubuserapp.viewmodel.SettingViewModelFactory

class ListGithubUserActivity : AppCompatActivity() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private lateinit var binding: ActivityListGithubUserBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var settingViewModel: SettingViewModel
    private var orientation: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListGithubUserBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        title = getString(R.string.title_act_list)
        orientation = this.resources.configuration.orientation

        val pref = SettingPreferences.getInstance(dataStore)
        settingViewModel = ViewModelProvider(this, SettingViewModelFactory(pref)).get(
            SettingViewModel::class.java
        )

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)
        mainViewModel.userList.observe(this) { userList ->
            showRecyclerList(userList)
            if(userList.isNullOrEmpty()){
                binding.statusSearch.visibility = View.VISIBLE
            }else{
                binding.statusSearch.visibility = View.GONE
            }
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        startSearchView()
        if(!mainViewModel.firstList){
            mainViewModel.searchUserData(this@ListGithubUserActivity, "a")
            mainViewModel.saveStateList(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.action_bar_menu_list, menu)
        val item = menu.findItem(R.id.mySwitch)
        val mySwitch = item.actionView.findViewById<SwitchCompat>(R.id.switchForActionBar)

        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                mySwitch.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                mySwitch.isChecked = false
            }
        }
        mySwitch.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            settingViewModel.saveThemeSetting(isChecked)
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.myFavoriteButton -> {
                val intentFavorite = Intent(this@ListGithubUserActivity, FavoriteGithubUserActivity::class.java)
                startActivity(intentFavorite)
                true
            }
            else -> true
        }
    }

    private fun startSearchView() {
        binding.searchUser.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if(query.isNotEmpty()){
                    mainViewModel.searchUserData(this@ListGithubUserActivity,query)
                }
                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {
                if(newText != mainViewModel.searchWord && newText.isNotEmpty()){
                    mainViewModel.saveSearchWord(newText)
                    mainViewModel.searchUserData(this@ListGithubUserActivity, newText)
                }
                return false
            }
        })
    }

    private fun showRecyclerList(listUser: List<ItemsItem>?) {
        binding.rvUser.setHasFixedSize(true)
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            binding.rvUser.layoutManager = LinearLayoutManager(this)
        } else {
            binding.rvUser.layoutManager = GridLayoutManager(this,2)
        }
        val listUserAdapter = listUser?.let { ListUserAdapter(this, it) }
        binding.rvUser.adapter = listUserAdapter

        listUserAdapter?.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: String) {
                val moveWithObjectIntent = Intent(this@ListGithubUserActivity, DetailGithubUserActivity::class.java)
                moveWithObjectIntent.putExtra(DETAIL_USER, data)
                startActivity(moveWithObjectIntent)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.loadingList.visibility = View.VISIBLE
        } else {
            binding.loadingList.visibility = View.GONE
        }
    }

    companion object {
        const val DETAIL_USER = "detail_user"
    }
}