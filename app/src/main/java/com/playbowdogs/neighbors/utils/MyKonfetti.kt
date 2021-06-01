package com.playbowdogs.neighbors.utils

import android.graphics.Color
import nl.dionsegijn.konfetti.KonfettiView
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size

object MyKonfetti {
    fun display(konfettiView: KonfettiView) {
        konfettiView.build()
            .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
            .setDirection(0.0, 359.0)
            .setSpeed(1f, 5f)
            .setFadeOutEnabled(true)
            .setTimeToLive(2000L)
            .addShapes(Shape.Square, Shape.Circle)
            .addSizes(Size(12), Size(3))
            .setPosition(-50f, konfettiView.width + 50f, -50f, -50f)
            .streamFor(300, 10000L)
    }

    fun cancel(konfettiView: KonfettiView) {
        konfettiView.reset()
    }
}