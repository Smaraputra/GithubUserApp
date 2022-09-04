package id.smaraputra.githubuserapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.smaraputra.githubuserapp.adapter.FollowerDetailAdapter
import id.smaraputra.githubuserapp.adapter.FollowingDetailAdapter
import id.smaraputra.githubuserapp.databinding.FragmentFollowBinding
import id.smaraputra.githubuserapp.response.FollowerUserResponse
import id.smaraputra.githubuserapp.response.FollowingUserResponse
import id.smaraputra.githubuserapp.viewmodel.MainViewModel

class FollowFragment : Fragment() {
    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)

        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)
        val username = arguments?.getString(ARG_NAME)

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)
        mainViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        if(index==2){
            mainViewModel.followingUser.observe(viewLifecycleOwner) { followingUser ->
                showFollowingRecycler(followingUser)
            }
        }else if(index==1){
            mainViewModel.followerUser.observe(viewLifecycleOwner) { followerUser ->
                showFollowerRecycler(followerUser)
            }
        }
        if(!mainViewModel.firstFollowing){
            username?.let { context?.let { it1 -> mainViewModel.showFollowing(it1, it) } }
            mainViewModel.saveStateFollowing(true)
        }
        if(!mainViewModel.firstFollower){
            username?.let { context?.let { it1 -> mainViewModel.showFollower(it1, it) } }
            mainViewModel.saveStateFollower(true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showFollowingRecycler(listFollowing: List<FollowingUserResponse>?) {
        binding.rvFollowDetail.setHasFixedSize(true)
        binding.rvFollowDetail.layoutManager = LinearLayoutManager(activity)
        val followingUserAdapter = activity?.let { listFollowing?.let { it1 ->
            FollowingDetailAdapter(it,
                it1
            )
        } }
        binding.rvFollowDetail.adapter = followingUserAdapter
    }

    private fun showFollowerRecycler(listFollower: List<FollowerUserResponse>?) {
        binding.rvFollowDetail.setHasFixedSize(true)
        binding.rvFollowDetail.layoutManager = LinearLayoutManager(activity)
        val followerUserAdapter = activity?.let { listFollower?.let { it1 ->
            FollowerDetailAdapter(it,
                it1
            )
        } }
        binding.rvFollowDetail.adapter = followerUserAdapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.loadingFollow.visibility = View.VISIBLE
        } else {
            binding.loadingFollow.visibility = View.GONE
        }
    }

    companion object {
        const val ARG_SECTION_NUMBER = "page_number"
        const val ARG_NAME = "username"
    }
}