import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.project.appealic.R
import com.project.appealic.data.model.Artist
import com.project.appealic.ui.view.Fragment.ArtistProfileFragment

class ArtistDetailFragment(private var context: Context) : DialogFragment() {

    private var selectedArtist: Artist? = null // Biến để lưu trữ thông tin về nghệ sĩ được chọn
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(context)
        val window = dialog.window
        window?.setBackgroundDrawableResource(R.drawable.more_background)
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window?.setGravity(Gravity.BOTTOM or Gravity.START or Gravity.END)
        dialog.setContentView(R.layout.bottom_artist)

        // Lấy ID của nghệ sĩ từ bundle
        val artistId = arguments?.getString("ARTIST_ID")

        val llArtistDetail = dialog.findViewById<LinearLayout>(R.id.llArtistDetail)

        // Truy vấn Firebase để lấy thông tin chi tiết về nghệ sĩ
        val artistNameTextView = dialog.findViewById<TextView>(R.id.artistName)
        val artistImageView = dialog.findViewById<ImageView>(R.id.artistImage)
        val artistRef = FirebaseFirestore.getInstance().collection("artists").document(artistId.toString())
        artistRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    // Lấy thông tin chi tiết về nghệ sĩ từ Firestore
                    val artistName = document.getString("Name")
                    val artistImage = document.getString("ImageResource")

                    // Tạo một instance của Artist
                    selectedArtist = Artist(artistId, artistName, artistImage)

                    // Hiển thị thông tin chi tiết của nghệ sĩ trên giao diện của Dialog
                    artistNameTextView.text = artistName
                    artistImage?.let {
                        val gsReference = FirebaseStorage.getInstance().getReferenceFromUrl(it)
                        Glide.with(requireContext())
                            .load(gsReference)
                            .into(artistImageView)
                    }
                } else {
                    Log.d("ArtistDialogFragment", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("ArtistDialogFragment", "get failed with ", exception)
            }

//        llArtistDetail.setOnClickListener {
//            // Sử dụng thông tin về nghệ sĩ đã được tải trước đó
//            println(selectedArtist)
//            selectedArtist?.let { artist ->
//                // Tạo một instance của ArtistProfileFragment và truyền selectedArtist vào
//                val artistProfileFragment = ArtistProfileFragment().apply {
//                    arguments = Bundle().apply {
//                        putParcelable("selectedArtist", artist)
//                    }
//                }
//
//                // Chuyển sang ArtistProfileFragment
//                parentFragmentManager.beginTransaction()
//                    .replace(R.id.fragmenthome, artistProfileFragment)
//                    .addToBackStack(null)
//                    .commit()
//            }
//        }

        return dialog
    }
}
