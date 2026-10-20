package exception

class ResourceNotFoundException(
    val resource : String
) : RuntimeException()