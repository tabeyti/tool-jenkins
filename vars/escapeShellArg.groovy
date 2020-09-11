def call(String input) {
    final def SpecialChars = [
        "\\": '\\\\',
        '"': '\\"',
        '$': '\\$'
    ]

    // Replace any unicode quotes with standard ascii.
    input = input.replace('“', '"').replace("”", '"').replace('‘', '"').replace("’", '"')

    // We replace single quotes with double as they cannot be escaped in a single quoted
    // arg within shell.
    // https://www.gnu.org/software/bash/manual/html_node/Single-Quotes.html
    input = input.replace("'", '"')

    SpecialChars.each { c, r ->
        input = input.replace(c, r)
    }

    return input
}
