package com.innoq.innochain.util

internal fun String.fromHexString(): ByteArray {
	val bytes: ByteArray = ByteArray(this.length / 2);
	for ((i, _) in bytes.withIndex()) {
		bytes.set(i, Integer.parseInt(this.substring(2 * i, 2 * i + 2), 16).toByte());
	}
	return bytes;
}

internal fun ByteArray.toHexString(): String {
	val hs = StringBuilder()
	for (b in this) {
		hs.append(String.format("%02X", b));
	}
	return hs.toString();
}