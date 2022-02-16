package ram.hesokio.srawber.presentation.view.game_view.game

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ram.hesokio.srawber.R
import ram.hesokio.srawber.databinding.ItmStarBinding

fun interface AdapterCallback {
    fun onStarClick(bonus: Int)
}

class GameAdapter(val listener: AdapterCallback) :
    RecyclerView.Adapter<GameAdapter.EntityViewHolder>() {

    var entitiesList: MutableList<StarEntity> = mutableListOf()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntityViewHolder {
        val binding = ItmStarBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return EntityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GameAdapter.EntityViewHolder, position: Int) {
        entitiesList[position].let { holder.binding(it) }
    }

    override fun getItemCount(): Int = entitiesList.size

    inner class EntityViewHolder(private val binding: ItmStarBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("ResourceAsColor")
        fun binding(item: StarEntity) {
            binding.itmStar.setOnClickListener {
                if (item.bonus) {
                    binding.itmStar.setImageResource(R.drawable.star2)
                    binding.itmStar.background.setTint(R.color.teal_700)
                    listener.onStarClick(100)
                } else {
                    binding.itmStar.setImageResource(R.drawable.star3)
                    listener.onStarClick(0)
                }
            }
        }
    }
}