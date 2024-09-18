package org.example.student.flooditgame

import uk.ac.bournemouth.ap.floodit.lib.FlooditGame
import uk.ac.bournemouth.ap.lib.matrix.Matrix

class StudentFlooditGame(
    override val width: Int = 5,
    override val height: Int = 5,
    override val maxTurns: Int = 40,
    override val colourCount: Int = 6,
) : FlooditGame {
    val boxes: Matrix<Box> = Matrix(width, height) { x: Int, y: Int -> Box(x,y)}
    inner class Box(val boxX: Int, val boxY: Int){
        var colorId: Int = (0..<colourCount).random()
        val adjacentBoxesCoordinates: List<Pair<Int,Int>>
            get() {
            val list: List<Pair<Int,Int>> =
                listOf(
                    Pair(boxX-1,boxY),
                    Pair(boxX+1,boxY),
                    Pair(boxX,boxY+1),
                    Pair(boxX-1,boxY-1))
            val finalList = mutableListOf<Pair<Int,Int>>()
            finalList.addAll(list.filter {boxes.isValid(it.first,it.second)})
            return finalList
        }
    }
    override var round: Int = 0
    override var state: FlooditGame.State = FlooditGame.State.RUNNING

    override fun get(x: Int, y: Int): Int {
        return if (boxes.isValid(x,y)){
            boxes[x,y].colorId
        } else
            -1
    }

    override fun playColour(clr: Int) {
        val adjacentBoxes: MutableList<Box> = mutableListOf(boxes[0, 0])
        var repeat = true
        while (repeat) {
            val adjacentBoxesList = adjacentBoxes.toList()
            for (box in adjacentBoxesList) {
                for (adjacentCoordinates in box.adjacentBoxesCoordinates) {
                    if (boxes[adjacentCoordinates.first, adjacentCoordinates.second].colorId == box.colorId) {
                        if (adjacentBoxes.indexOf(boxes[adjacentCoordinates.first, adjacentCoordinates.second]) == -1) {
                            adjacentBoxes.add(boxes[adjacentCoordinates.first, adjacentCoordinates.second])
                        }
                    }
                }
            }
            if (adjacentBoxes == adjacentBoxesList) {
                repeat = false
            }
        }
        for (box in adjacentBoxes){
            box.colorId=clr
        }
    }
    private val onGameOverListeners = mutableListOf<FlooditGame.GameOverListener>()
    private val onGamePlayListeners = mutableListOf<FlooditGame.GamePlayListener>()

    override fun addGameOverListener(listener: FlooditGame.GameOverListener) {
        onGameOverListeners.add(listener)
    }

    override fun addGamePlayListener(listener: FlooditGame.GamePlayListener) {
        onGamePlayListeners.add(listener)
    }

    override fun removeGameOverListener(listener: FlooditGame.GameOverListener) {
        onGameOverListeners.remove(listener)
    }

    override fun removeGamePlayListener(listener: FlooditGame.GamePlayListener) {
        onGamePlayListeners.remove(listener)
    }

    override fun notifyWin(round: Int) {
        for(listener in onGameOverListeners) {
            listener.onGameOver(this, this.round, (round>maxTurns))
        }
    }

    override fun notifyMove(round: Int) {
        for(listener in onGamePlayListeners) {
            listener.onGameChanged(this, this.round)
        }
    }
}