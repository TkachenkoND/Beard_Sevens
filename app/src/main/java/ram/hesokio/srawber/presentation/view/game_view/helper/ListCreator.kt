package ram.hesokio.srawber.presentation.view.game_view.helper

import ram.hesokio.srawber.R
import ram.hesokio.srawber.presentation.view.game_view.game.StarEntity
import kotlin.random.Random

class ListCreator {

    fun createItemsList(): ArrayList<StarEntity> {
        var index = 16
        val list = ArrayList<StarEntity>()

        while (index > 0) {
            list.add(
                StarEntity(
                    R.drawable.star1,
                    getRandomBoolean()
                )
            )

            index--
        }

        return list
    }

    private fun getRandomBoolean(): Boolean {
        var boolean = false

        when (Random.nextInt(2) + 1) {
            1 -> boolean = true
            2 -> boolean = false
        }

        return boolean
    }
}