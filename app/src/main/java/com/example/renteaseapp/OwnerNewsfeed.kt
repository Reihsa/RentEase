package com.example.renteaseapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.toObject
import com.example.renteaseapp.PostAdapter
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.example.renteaseapp.Post

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OwnerNewsfeed.newInstance] factory method to
 * create an instance of this fragment.
 */
class OwnerNewsfeed : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var postAdapter: PostAdapter
    private lateinit var db: FirebaseFirestore
    private val posts = mutableListOf<Post>()
    private val handler = Handler(Looper.getMainLooper())
    private val updateInterval = 5 * 60 * 1000L // 5 minutes in milliseconds

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = try {
            // Inflate the layout for this fragment
            inflater.inflate(R.layout.fragment_owner_newsfeed, container, false)
        } catch (e: Exception) {
            Log.e("OwnerNewsfeed", "Error inflating layout", e)
            Toast.makeText(requireContext(), "Error inflating layout", Toast.LENGTH_SHORT).show()
            return null
        }

        try {
            db = Firebase.firestore
            recyclerView = view.findViewById(R.id.renterPosts)
            recyclerView.layoutManager = LinearLayoutManager(requireContext())

            postAdapter = PostAdapter(posts) { post, action ->
                when (action) {
                    PostAdapter.ActionType.LIKE -> handleLikeAction(post)
                    PostAdapter.ActionType.COMMENT -> handleCommentAction(post)
                    PostAdapter.ActionType.SHARE -> handleShareAction(post)
                }
            }
            recyclerView.adapter = postAdapter

            fetchPosts() // Load initial posts
            scheduleAutoUpdate() // Start periodic updates
        } catch (e: Exception) {
            Log.e("OwnerNewsfeed", "Error initializing RecyclerView or adapter", e)
            Toast.makeText(requireContext(), "Error setting up posts", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    private fun fetchPosts() {
        db.collection("posts")
            .whereEqualTo("type", "owner") // Filter posts by type "owner"
            .orderBy("date", Query.Direction.DESCENDING) // Order by date
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Log.w("OwnerNewsfeed", "No posts found matching the criteria")
                    Toast.makeText(requireContext(), "No posts found", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                posts.clear()
                for (document in documents) {
                    try {
                        Log.d("OwnerNewsfeed", "Document data: ${document.data}")

                        val post = document.toObject(Post::class.java)
                            ?: throw Exception("Null Post Object")

                        Log.d("OwnerNewsfeed", "Parsed Post: $post")
                        posts.add(post)
                    } catch (e: Exception) {
                        Log.e("OwnerNewsfeed", "Error processing post: ${document.id}", e)
                        Toast.makeText(requireContext(), "Error processing post: ${document.id}", Toast.LENGTH_SHORT).show()
                    }
                }

                postAdapter.notifyDataSetChanged()
                Log.d("OwnerNewsfeed", "Fetched ${posts.size} valid posts")
            }
            .addOnFailureListener { exception ->
                Log.e("OwnerNewsfeed", "Error fetching posts from Firestore", exception)
                Toast.makeText(requireContext(), "Failed to fetch posts", Toast.LENGTH_SHORT).show()
            }
    }




    private fun scheduleAutoUpdate() {
        try {
            handler.postDelayed(object : Runnable {
                override fun run() {
                    try {
                        fetchPosts() // Refresh posts
                        handler.postDelayed(this, updateInterval) // Schedule the next update
                    } catch (e: Exception) {
                        Log.e("OwnerNewsfeed", "Error in scheduled update", e)
                        Toast.makeText(requireContext(), "Error updating posts", Toast.LENGTH_SHORT).show()
                    }
                }
            }, updateInterval)
        } catch (e: Exception) {
            Log.e("OwnerNewsfeed", "Error scheduling auto-update", e)
            Toast.makeText(requireContext(), "Failed to schedule updates", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleLikeAction(post: Post) {
        try {
            Toast.makeText(requireContext(), "Liked post by ${post.username}", Toast.LENGTH_SHORT).show()
            Log.d("OwnerNewsfeed", "Liked post: $post")
        } catch (e: Exception) {
            Log.e("OwnerNewsfeed", "Error handling Like action", e)
            Toast.makeText(requireContext(), "Error liking post", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleCommentAction(post: Post) {
        try {
            Toast.makeText(requireContext(), "Commented on post by ${post.username}", Toast.LENGTH_SHORT).show()
            Log.d("OwnerNewsfeed", "Commented on post: $post")
        } catch (e: Exception) {
            Log.e("OwnerNewsfeed", "Error handling Comment action", e)
            Toast.makeText(requireContext(), "Error commenting on post", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleShareAction(post: Post) {
        try {
            Toast.makeText(requireContext(), "Shared post by ${post.username}", Toast.LENGTH_SHORT).show()
            Log.d("OwnerNewsfeed", "Shared post: $post")
        } catch (e: Exception) {
            Log.e("OwnerNewsfeed", "Error handling Share action", e)
            Toast.makeText(requireContext(), "Error sharing post", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        try {
            handler.removeCallbacksAndMessages(null) // Stop updates when the fragment is destroyed
        } catch (e: Exception) {
            Log.e("OwnerNewsfeed", "Error stopping handler", e)
            Toast.makeText(requireContext(), "Error stopping updates", Toast.LENGTH_SHORT).show()
        }
    }
}