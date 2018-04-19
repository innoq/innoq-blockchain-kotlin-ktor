package com.innoq.innochain.model

import com.innoq.innochain.model.Block
import org.junit.Test

class BlockTest {
	
	@Test
	fun testNewBlockMining() : Unit {
		// GIVEN
		println();
		// WHEN
		val block = Block(index = 1, previousBlockHash = ByteArray(32))
		
		// THEN
		println(block)
	}
}