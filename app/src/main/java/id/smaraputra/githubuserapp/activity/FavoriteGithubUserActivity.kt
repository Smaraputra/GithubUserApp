package id.smaraputra.githubuserapp.activity

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.CompoundButton
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
import id.smaraputra.githubuserapp.adapter.FavoriteAdapter
import id.smaraputra.githubuserapp.databinding.ActivityFavoriteGithubUserBinding
import id.smaraputra.githubuserapp.viewmodel.FavoriteViewModel
import id.smaraputra.githubuserapp.viewmodel.FavoriteViewModelFactory
import id.smaraputra.githubuserapp.viewmodel.SettingViewModel
import id.smaraputra.githubuserapp.viewmodel.SettingViewModelFactory

class FavoriteGithubUserActivity : AppCompatActivity() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private lateinit var binding: ActivityFavoriteGithubUserBinding
    private lateinit var settingViewModel: SettingViewModel
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var favoriteAdapter: FavoriteAdapter
    private var orientation: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteGithubUserBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        title = getString(R.string.title_act_favorite)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        orientation = this.resources.configuration.orientation

        val pref = SettingPreferences.getInstance(dataStore)
        settingViewModel = ViewModelProvider(this, SettingViewModelFactory(pref)).get(
            SettingViewModel::class.java
        )

        favoriteViewModel = ViewModelProvider(this, FavoriteViewModelFactory.getInstance(this.application)).get(
            FavoriteViewModel::class.java
        )
        favoriteViewModel.getAllFavorites().observe(this, { getAllFavorites ->
            favoriteAdapter.setListFavorite(getAllFavorites)
            if(getAllFavorites.isNullOrEmpty()){
                binding.statusFavorite.visibility = View.VISIBLE
            }else{
                binding.statusFavorite.visibility = View.GONE
            }
        })

        showFavorite()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.action_bar_menu_detail, menu)
        val item = menu.findItem(R.id.mySwitchDetail)
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

    private fun showFavorite(){
        favoriteAdapter = FavoriteAdapter(this)
        binding.rvUserFavorite.setHasFixedSize(true)
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            binding.rvUserFavorite.layoutManager = LinearLayoutManager(this)
        } else {
            binding.rvUserFavorite.layoutManager = GridLayoutManager(this,2)
        }
        binding.rvUserFavorite.adapter = favoriteAdapter

        favoriteAdapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback {
            override fun onItemClicked(data: String) {
                val moveWithObjectIntent = Intent(this@FavoriteGithubUserActivity, DetailGithubUserActivity::class.java)
                moveWithObjectIntent.putExtra(DETAIL_USER, data)
                startActivity(moveWithObjectIntent)
            }
        })
    }

    companion object {
        const val DETAIL_USER = "detail_user"
    }
}