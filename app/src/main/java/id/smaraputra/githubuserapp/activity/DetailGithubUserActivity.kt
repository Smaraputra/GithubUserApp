package id.smaraputra.githubuserapp.activity

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.res.ResourcesCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import id.smaraputra.githubuserapp.R
import id.smaraputra.githubuserapp.SettingPreferences
import id.smaraputra.githubuserapp.adapter.DetailUserPagerAdapter
import id.smaraputra.githubuserapp.database.FavoriteModel
import id.smaraputra.githubuserapp.databinding.ActivityDetailGithubUserBinding
import id.smaraputra.githubuserapp.response.DetailUserResponse
import id.smaraputra.githubuserapp.viewmodel.*

class DetailGithubUserActivity : AppCompatActivity() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private lateinit var binding: ActivityDetailGithubUserBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var settingViewModel: SettingViewModel
    private var favoriteModel: FavoriteModel = FavoriteModel()
    private lateinit var favoriteAddDeleteViewModel: FavoriteAddDeleteViewModel
    private lateinit var favoriteViewModel: FavoriteViewModel
    private var notYetFav : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailGithubUserBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        title = getString(R.string.title_act_detail)

        val usernameGithub: String = intent.getStringExtra(DETAIL_USER).toString()
        val detailUserPagerAdapter = DetailUserPagerAdapter(this)
        detailUserPagerAdapter.username = usernameGithub
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = detailUserPagerAdapter
        val tabs: TabLayout = binding.tabDetail
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        binding.fabAddFavorite.setOnClickListener{
            addDeleteFavorite()
        }

        favoriteViewModel = ViewModelProvider(this, FavoriteViewModelFactory.getInstance(this.application)).get(
            FavoriteViewModel::class.java
        )
        favoriteAddDeleteViewModel = ViewModelProvider(this, FavoriteViewModelFactory.getInstance(this.application)).get(
            FavoriteAddDeleteViewModel::class.java
        )

        val pref = SettingPreferences.getInstance(dataStore)
        settingViewModel = ViewModelProvider(this, SettingViewModelFactory(pref)).get(
            SettingViewModel::class.java
        )

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MainViewModel::class.java
        )
        mainViewModel.detailUser.observe(this) { detailUser ->
            showDetailData(detailUser)
        }
        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        if(!mainViewModel.firstDetail){
            mainViewModel.searchDetailUser(this@DetailGithubUserActivity ,usernameGithub)
            mainViewModel.saveStateDetail(true)
        }
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

    private fun showDetailData (response: DetailUserResponse?){
        response?.id?.let {
            favoriteViewModel.checkFavorite(it).observe(this) { checkFavorite ->
                if(checkFavorite.isNullOrEmpty()){
                    notYetFav = 1
                    binding.fabAddFavorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                }else{
                    notYetFav = 0
                    binding.fabAddFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
                }
            }
        }

        favoriteModel.let { favoriteModel ->
            favoriteModel.idGithub = response?.id
            favoriteModel.avatarLink = response?.avatarUrl
            favoriteModel.repoLink = response?.htmlUrl
            favoriteModel.type = response?.type
            favoriteModel.username = response?.login
        }

        Glide.with(this@DetailGithubUserActivity)
            .asBitmap()
            .load(response?.avatarUrl)
            .placeholder(R.drawable.portrait_placeholder)
            .error(R.drawable.ic_baseline_sync_problem_24)
            .into(binding.avatarUserDetail)

        binding.apply {
            nameDetail.text = if(!response?.name.isNullOrBlank()){response?.name}else{"Not Inputted"}
            usernameDetail.text = if(!response?.login.isNullOrBlank()){response?.login}else{"Not Inputted"}
            companyDetail.text = if(!response?.company.isNullOrBlank()){response?.company}else{"Not Inputted"}
            locationDetail.text = if(!response?.location.isNullOrBlank()){response?.location}else{"Not Inputted"}
            repositoryDetail.text = response?.publicRepos.toString()
            followersDetail.text = response?.followers.toString()
            followingDetail.text = response?.following.toString()
        }

    }

    private fun addDeleteFavorite(){
        if(notYetFav==0){
            deleteConfirmation()
        }else{
            favoriteAddDeleteViewModel.insert(favoriteModel)
            Toast.makeText(this, getString(R.string.success_add), Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.loadingDetail.visibility = View.VISIBLE
        } else {
            binding.loadingDetail.visibility = View.GONE
        }
    }

    private fun deleteConfirmation(){
        val alertDialog: AlertDialog = this.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(getString(R.string.delete_confirmation_title))
            builder.setMessage(getString(R.string.delete_confirmation_message))
            builder.apply {
                setPositiveButton(getString(R.string.yes)) { _, _ ->
                    favoriteAddDeleteViewModel.delete(favoriteModel)
                    Toast.makeText(this@DetailGithubUserActivity, getString(R.string.success_removed), Toast.LENGTH_SHORT).show()
                }
                setNegativeButton(getString(R.string.no)) { _, _ ->

                }
            }
            builder.create()
        }
        alertDialog.show()
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ResourcesCompat.getColor(
            resources, R.color.green_soft, null))
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ResourcesCompat.getColor(
            resources, R.color.green_soft, null))
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.detail_user_sub_followers,
            R.string.detail_user_sub_following
        )
        const val DETAIL_USER = "detail_user"
    }
}