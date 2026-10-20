package model.sign_reaction

import sql.SignReactionTypeDb

enum class SignReactionType {
    LIKE,
    DISLIKE
}
fun SignReactionType.toSignReactionTypeDb() : SignReactionTypeDb{
    return when(this){
        SignReactionType.LIKE -> SignReactionTypeDb.LIKE
        SignReactionType.DISLIKE -> SignReactionTypeDb.DISLIKE
    }
}