package org.example.student.flooditgame

import uk.ac.bournemouth.ap.floodit.lib.FlooditGame
import uk.ac.bournemouth.ap.lib.matrix.Matrix
import javax.swing.Box

class StudentFlooditGame(
    override val width: Int = 5,
    override val height: Int = 5,
    override val maxTurns: Int = 70,
    override val colourCount: Int = 6
) : FlooditGame {
    val boxes: Matrix<box> = Matrix(width, height) { x: Int, y: Int -> box(x,y)}
    inner class box(val boxX: Int,val boxY: Int){
        var ColorId: Int = (0..colourCount-1).random()
        val adjacentBoxesCoordinates: List<Pair<Int,Int>>
            get() {
            val list: List<Pair<Int,Int>> = listOf(Pair(boxX-1,boxY),Pair(boxX+1,boxY),Pair(boxX,boxY+1),Pair(boxX-1,boxY-1))
            val finalList = mutableListOf<Pair<Int,Int>>()
            finalList.addAll(list.filter {boxes.isValid(it.first,it.second)})
            return finalList
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
        var adjacentBoxes: MutableList<box> = mutableListOf(boxes[0, 0])
        var repeat = true
        while (repeat) {
            for (box in adjacentBoxes) {
                for (adjacentCoordinates in box.adjacentBoxesCoordinates) {
                    if (boxes[adjacentCoordinates.first, adjacentCoordinates.second].ColorId == box.ColorId)
                        if (adjacentBoxes.indexOf(boxes[adjacentCoordinates.first, adjacentCoordinates.second]) == -1) {
                            adjacentBoxes.add(boxes[adjacentCoordinates.first, adjacentCoordinates.second])
                        }
                }
            }
        }
        for (box in adjacentBoxes){
            box.ColorId=clr
        }
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