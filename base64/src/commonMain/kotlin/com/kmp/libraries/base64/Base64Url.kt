package com.kmp.libraries.base64

/**
 * Encode a [String] to Base64 URL-safe encoded [String].
 *
 * See [RFC 4648 §5](https://datatracker.ietf.org/doc/html/rfc4648#section-5)
 */
val String.base64UrlEncoded: String
    get() = encodeInternal(Encoding.UrlSafe)

/**
 * Encode a [ByteArray] to Base64 URL-safe encoded [String].
 *
 * See [RFC 4648 §5](https://datatracker.ietf.org/doc/html/rfc4648#section-5)
 */
val ByteArray.base64UrlEncoded: String
    get() = asCharArray().concatToString().base64UrlEncoded

/**
 * Decode a Base64 URL-safe encoded [String] to [String].
 *
 * See [RFC 4648 §5](https://datatracker.ietf.org/doc/html/rfc4648#section-5)
 */
val String.base64UrlDecoded: String
    get() {
        val ret = decodeInternal(Encoding.UrlSafe).map { it.toChar() }
        val foo = ret.joinToString("")
        val bar = foo.dropLast(count { it == '=' })
        return bar.filterNot { it.code == 0 }
    }

/**
 * Decode a Base64 URL-safe encoded [String] to [ByteArray].
 *
 * See [RFC 4648 §5](https://datatracker.ietf.org/doc/html/rfc4648#section-5)
 */
val String.base64UrlDecodedBytes: ByteArray
    get() = decodeInternal(Encoding.UrlSafe)
        .map { it.toByte() }
        .toList()
        .dropLast(count { it == '=' })
        .toByteArray()

/**
 * Decode a Base64 URL-safe encoded [ByteArray] to [String].
 *
 * See [RFC 4648 §5](https://datatracker.ietf.org/doc/html/rfc4648#section-5)
 */
val ByteArray.base64UrlDecoded: String
    get() = asCharArray().concatToString().base64UrlDecoded