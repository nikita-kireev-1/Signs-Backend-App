package model.sign

import sql.SignStatusDb

enum class SignStatus {
    ACTIVE,
    HIDDEN,
    DELETED,
    EXPIRED
}
fun SignStatus.toSignStatusDb() : SignStatusDb{
    return when(this){
        SignStatus.ACTIVE -> SignStatusDb.ACTIVE
        SignStatus.HIDDEN -> SignStatusDb.HIDDEN
        SignStatus.EXPIRED -> SignStatusDb.EXPIRED
        SignStatus.DELETED -> SignStatusDb.DELETED
    }
}