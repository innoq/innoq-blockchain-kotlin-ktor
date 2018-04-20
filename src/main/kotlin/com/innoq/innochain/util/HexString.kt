package com.innoq.innochain.util

fun String.fromHexString(): ByteArray {
	val bytes: ByteArray = ByteArray(length / 2);
	for ((i, _) in bytes.withIndex()) {
		bytes.set(i, Integer.parseInt(substring(2 * i, 2 * i + 2), 16).toByte());
	}
	return bytes;
}

fun ByteArray.toHexString(): String {
	val hs = StringBuilder()
	for (b in this) {
		hs.append(String.format("%02X", b));
	}
	return hs.toString();
}