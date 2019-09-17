package org.gielinor.util.extensions

import java.awt.Color

fun Color.toHex(): String = String.format("%06x", rgb and 0x00FFFFFF)
