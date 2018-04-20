package com.innoq.innochain.dto

import com.innoq.innochain.model.Node

data class RegisterNodeResponse(val message: String, val node: Node)