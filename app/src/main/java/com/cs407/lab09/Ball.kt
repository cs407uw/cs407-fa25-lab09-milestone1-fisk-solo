package com.cs407.lab09

/**
 * Represents a ball that can move. (No Android UI imports!)
 *
 * Constructor parameters:
 * - backgroundWidth: the width of the background, of type Float
 * - backgroundHeight: the height of the background, of type Float
 * - ballSize: the width/height of the ball, of type Float
 */
class Ball(
    private val backgroundWidth: Float,
    private val backgroundHeight: Float,
    private val ballSize: Float
) {
    var posX = 0f
    var posY = 0f
    var velocityX = 0f
    var velocityY = 0f
    private var accX = 0f
    private var accY = 0f

    private var isFirstUpdate = true

    init {
        reset()
    }

    /**
     * Updates the ball's position and velocity based on the given acceleration and time step.
     * (See lab handout for physics equations)
     */
    fun updatePositionAndVelocity(xAcc: Float, yAcc: Float, dT: Float) {
        if(isFirstUpdate) {
            isFirstUpdate = false
            accX = xAcc
            accY = yAcc
            return
        }
        val dt = dT
        val dt2 = dt * dt

        //x-axis
        val a0x = accX
        val a1x = xAcc
        val v0x = velocityX

        //y-axis
        val a0y = accY
        val a1y = yAcc
        val v0y = velocityY

        //Equation 1 v1 = v0 + 1/2(a1 + a0)(t1-t2)
        val v1x = v0x + 0.5f * (a1x+a0x) * dt

        //Equation 2
        val dx = v0x * dt + (1f/6f) * dt2 * (3f * a0x + a1x)

        //calculate equations with y-axis
        val v1y = v0y + 0.5f * (a1y+a0y) * dt
        val dy = v0y * dt + (1f/6f) * dt2 * (3f * a0y + a1y)

        posX += dx
        posY += dy
        velocityX = v1x
        velocityY = v1y

        accX = xAcc
        accY = yAcc
    }

    /**
     * Ensures the ball does not move outside the boundaries.
     * When it collides, velocity and acceleration perpendicular to the
     * boundary should be set to 0.
     */
    fun checkBoundaries() {
        //left wall
        if(posX < 0f){
            posX = 0f
            velocityX = 0f
            accX = 0f
        }

        //top wall
        if(posY < 0f){
            posY = 0f
            velocityY = 0f
            accY = 0f
        }

        //right wall
        if(posX + ballSize > backgroundWidth){
            posX = backgroundWidth - ballSize
            velocityX = 0f
            accX = 0f
        }

        //bottom wall
        if(posY + ballSize > backgroundHeight){
            posY = backgroundHeight - ballSize
            velocityY = 0f
            accY = 0f
        }

    }

    /**
     * Resets the ball to the center of the screen with zero
     * velocity and acceleration.
     */
    fun reset() {
        // (Reset posX, posY, velocityX, velocityY, accX, accY, isFirstUpdate)
        posX = (backgroundWidth-ballSize)/2f
        posY = (backgroundHeight-ballSize)/2f

        velocityY = 0f
        velocityX = 0f
        accX = 0f
        accY = 0f
        isFirstUpdate = true
    }
}