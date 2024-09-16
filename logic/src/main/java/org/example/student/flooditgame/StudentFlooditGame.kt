package org.example.student.flooditgame

import uk.ac.bournemouth.ap.floodit.lib.FlooditGame
import uk.ac.bournemouth.ap.lib.matrix.Matrix
import javax.swing.Box

class StudentFlooditGame(
    override val width: Int,
    override val height: Int,
    override val maxTurns: Int,
    override val colourCount: Int
) : FlooditGame {
    val boxes: Matrix<box> = Matrix(width, height) { x: Int, y: Int -> box(x,y)}
    inner class box(val boxX: Int,val boxY: Int){
        var ColorId: Int = (0..colourCount-1).random()
        var adjacentBoxesCoordinates: List<Pair<Int,Int>> get() {
            when {
                boxX==width && boxY==height -> return listOf(Pair(boxX-1,boxY),Pair(boxX,boxY-1))
                boxX==width -> return listOf(Pair(boxX-1,boxY),Pair(boxX,boxY-1),Pair(boxX,boxY+1))
                boxY==height -> return listOf(Pair(boxX-1,boxY),Pair(boxX,boxY-1),Pair(boxX+1,boxY))
            }
        }
    }
    override var round: Int = 0
    override var state: FlooditGame.State = FlooditGame.State.RUNNING

    override fun get(x: Int, y: Int): Int {
        if (boxes.isValid(x,y)){
            return boxes[x,y].ColorId
        }
        else
            return -1
    }

    override fun playColour(clr: Int) {
        TODO("Not yet implemented")
    }

    override fun addGamePlayListener(listener: FlooditGame.GamePlayListener) {
        TODO("Not yet implemented")
    }

    override fun removeGamePlayListener(listener: FlooditGame.GamePlayListener) {
        TODO("Not yet implemented")
    }

    override fun addGameOverListener(gameOverListener: FlooditGame.GameOverListener) {
        TODO("Not yet implemented")
    }

    override fun removeGameOverListener(gameOverListener: FlooditGame.GameOverListener) {
        TODO("Not yet implemented")
    }

    override fun notifyMove(round: Int) {
        TODO("Not yet implemented")
    }

    override fun notifyWin(round: Int) {
        TODO("Not yet implemented")
    }

}