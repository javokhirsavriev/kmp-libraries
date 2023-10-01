package com.kmp.libraries.base64

/**
 * Encode a [String] to Base64 standard encoded [String].
 *
 * See [RFC 4648 §4](https://datatracker.ietf.org/doc/html/rfc4648#section-4)
 */
val String.base64Encoded: String
    get() = encodeInternal(Encoding.Standard)

/**
 * Encode a [ByteArray] to Base64 standard encoded [String].
 *
 * See [RFC 4648 §4](https://datatracker.ietf.org/doc/html/rfc4648#section-4)
 */
val ByteArray.base64Encoded: String
    get() = asCharArray().concatToString().base64Encoded

/**
 * Decode a Base64 standard encoded [String] to [String].
 *
 * See [RFC 4648 §4](https://datatracker.ietf.org/doc/html/rfc4648#section-4)
 */
val String.base64Decoded: String
    get() = decodeInternal(Encoding.Standard)
        .map { it.toChar() }
        .joinToString("")
        .dropLast(count { it == '=' })

/**
 * Decode a Base64 standard encoded [String] to [ByteArray].
 *
 * See [RFC 4648 §4](https://datatracker.ietf.org/doc/html/rfc4648#section-4)
 */
val String.base64DecodedBytes: ByteArray
    get() = decodeInternal(Encoding.Standard)
        .map { it.toByte() }
        .toList()
        .dropLast(count { it == '=' })
        .toByteArray()

/**
 * Decode a Base64 standard encoded [ByteArray] to [String].
 *
 * See [RFC 4648 §4](https://datatracker.ietf.org/doc/html/rfc4648#section-4)
 */
val ByteArray.base64Decoded: String
    get() = asCharArray().concatToString().base64Decoded