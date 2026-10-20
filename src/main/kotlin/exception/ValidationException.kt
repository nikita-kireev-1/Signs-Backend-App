package exception

class ValidationException (
    val errors: Map<String, String>
) : RuntimeException()