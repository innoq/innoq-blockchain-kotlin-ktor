package com.innoq.innochain.util

fun fromHexString(value: String): ByteArray {
	val bytes: ByteArray = ByteArray(value.length / 2);
	for ((i, _) in bytes.withIndex()) {
		bytes.set(i, Integer.parseInt(value.substring(2 * i, 2 * i + 2), 16).toByte());
	}
	return bytes;
}

fun toHexString(value: ByteArray): String {
	val hs = StringBuilder()
	for (b in value) {
		hs.append(String.format("%02X", b));
	}
	return hs.toString();
}