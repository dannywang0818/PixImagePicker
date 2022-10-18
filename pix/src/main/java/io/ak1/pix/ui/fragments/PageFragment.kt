package io.ak1.pix.ui.fragments

import android.graphics.Point
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import io.ak1.pix.databinding.FragmentPageBinding
import io.ak1.pix.engine.PicassoEngine
import io.ak1.pix.models.Img
import io.ak1.pix.ui.ItemsViewModel
import io.ak1.pix.ui.widget.CheckView
import io.ak1.pix.utility.PhotoMetadataUtils

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val PIX_IMG = "PIX_IMG"
private const val PAGER_POSITION = "PAGER_POSITION"

/**
 * A simple [Fragment] subclass.
 * Use the [PageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PageFragment : Fragment() {

    private var _binding: FragmentPageBinding? = null
    private val binding get() = _binding!!
    val items: ItemsViewModel by activityViewModels()


    // TODO: Rename and change types of parameters
    private var pixImg: Img? = null
    private var pagerPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            pixImg = it.getParcelable(PIX_IMG)
            pagerPosition = it.getInt(PAGER_POSITION)
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
//        zoomImageView.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.jetpack_logo))

//        zoomEngine.panTo(20.0F, 30F, true);

        val size: Point? = PhotoMetadataUtils.getBitmapSize(
            pixImg?.contentUrl,
            requireActivity()
        )
        if (size != null) {
            PicassoEngine.get()
                .loadImage(context, size.x, size.y, zoomImageView, pixImg?.contentUrl)
        }

        binding.checkViewOfPreview.setOnClickListener {
//            val id = this.layoutPosition
//            onSelectionListener!!.onCheckBoxClick(itemList[id], it, id)


            if (it is CheckView) {
                items.checkViewClicked(pixImg, pagerPosition) {
                    return@checkViewClicked true
                }
                if (it.checked()) {
                    it.setChecked(false)
                } else {
                    it.setChecked(true)
                }
            }
        }

        binding.checkViewOfPreview.setChecked(pixImg!!.selected)
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
        fun newInstance(pixImg: Img?, position: Int) =
            PageFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(PIX_IMG, pixImg)
                    putInt(PAGER_POSITION, position)
                }
            }
    }
}