package ram.hesokio.srawber.presentation.view.game_view.game

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ram.hesokio.srawber.R
import ram.hesokio.srawber.presentation.view.game_view.helper.ListCreator
import ram.hesokio.srawber.presentation.view.game_view.menu.FMenu

class FGame : Fragment(R.layout.frgm_game), AdapterCallback {

    private val itemAdapter = GameAdapter(this as AdapterCallback)
    private var bonus = 0
    private var list = ListCreator().createItemsList()

    private lateinit var recyclerView: RecyclerView
    private lateinit var buttonBack: ImageView
    private lateinit var gameText: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.frgm_game, container, false)

        recyclerView = view.findViewById(R.id.recycler)
        buttonBack = view.findViewById(R.id.btnGoBack)
        gameText = view.findViewById(R.id.gameText)

        return view
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler()
        itemAdapter.entitiesList = list

        onBackClick()
    }

    private fun initRecycler() {
        recyclerView.apply {
            layoutManager = GridLayoutManager(context, 4)
            adapter = itemAdapter
        }
    }

    private fun onBackClick() {
        buttonBack.setOnClickListener {
            parentFragmentManager.commit {
                setReorderingAllowed(true)
                replace(R.id.frgmContainer, FMenu())
                addToBackStack(null)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onStarClick(bonus: Int) {
        this.bonus += bonus
        gameText.text = "Now your bonus is ${this.bonus}"
    }
}