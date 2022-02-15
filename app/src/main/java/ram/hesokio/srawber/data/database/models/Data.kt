package ram.hesokio.srawber.data.database.models

import io.realm.RealmObject
import io.realm.annotations.Required

open class Data(
    @Required
    var fullLink: String = "null",
) : RealmObject()

