package io.ak1.pix.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import io.ak1.pix.R
import io.ak1.pix.databinding.FragmentPageBinding
import io.ak1.pix.models.Img

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val PIX_IMG = "PIX_IMG"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PageFragment : Fragment() {

    private var _binding: FragmentPageBinding? = null
    private val binding get() = _binding!!


    // TODO: Rename and change types of parameters
    private var pixImg: Img? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            pixImg = it.getParcelable(PIX_IMG)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPageBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val zoomImageView = binding.zoomImageView
        val zoomEngine = zoomImageView.engine
        zoomImageView.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.jetpack_logo))

        zoomEngine.panTo(20.0F, 30F, true);

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PageFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(pixImg: Img?, param2: String) =
            PageFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(PIX_IMG, pixImg)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}