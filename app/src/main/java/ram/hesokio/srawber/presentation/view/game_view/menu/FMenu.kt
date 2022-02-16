package ram.hesokio.srawber.presentation.view.game_view.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import ram.hesokio.srawber.R
import ram.hesokio.srawber.presentation.view.game_view.progress_bar.FProgress

class FMenu : Fragment(R.layout.frgm_menu) {

    private lateinit var buttonStart: Button
    private lateinit var buttonFinish: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.frgm_menu, container, false)
        buttonStart = view.findViewById(R.id.BStart)
        buttonFinish = view.findViewById(R.id.BFinish)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onStartClick()
        onFinishClick()
    }

    private fun onStartClick() {
        buttonStart.setOnClickListener {
            parentFragmentManager.commit {
                setReorderingAllowed(true)
                replace(R.id.frgmContainer, FProgress())
                addToBackStack(null)
            }
        }
    }

    private fun onFinishClick() {
        buttonFinish.setOnClickListener {
            activity?.finish()
        }
    }
}