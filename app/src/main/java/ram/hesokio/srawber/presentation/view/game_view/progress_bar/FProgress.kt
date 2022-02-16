package ram.hesokio.srawber.presentation.view.game_view.progress_bar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ram.hesokio.srawber.R
import ram.hesokio.srawber.presentation.view.game_view.game.FGame

class FProgress : Fragment(R.layout.frgm_pg_bar) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.frgm_pg_bar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoroutineScope(Dispatchers.Main).launch {
            delay(2100)
            parentFragmentManager.commit {
                setReorderingAllowed(true)
                replace(R.id.frgmContainer, FGame())
                addToBackStack(null)
            }
        }
    }
}