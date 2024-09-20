package org.example.student.flooditgame

import uk.ac.bournemouth.ap.floodit.lib.FlooditGame
import uk.ac.bournemouth.ap.lib.matrix.Matrix

class StudentFlooditGame(
    override val width: Int = 5,
    override val height: Int = 5,
    override val maxTurns: Int = 40,
    override val colourCount: Int = 6,
    val computerGame: Int = 0
) : FlooditGame {
    var boxes: Matrix<Box> = Matrix(width, height) { x: Int, y: Int -> Box(x,y)}
    inner class Box(val boxX: Int, val boxY: Int){
        var colorId: Int = (0..<colourCount).random()
            set (newValue) {
                require(newValue in 0..<colourCount){
                    "colour id should not exceed colour count"
                }
                field = newValue
            }
        val adjacentBoxesCoordinates: List<Pair<Int,Int>>
            get() {
            val list: List<Pair<Int,Int>> =
                listOf(
                    Pair(boxX-1,boxY),
                    Pair(boxX+1,boxY),
                    Pair(boxX,boxY+1),
                    Pair(boxX,boxY-1))
            val finalList = mutableListOf<Pair<Int,Int>>()
            finalList.addAll(list.filter {boxes.isValid(it.first,it.second)})
            return finalList
        }
    }
    override var round: Int = 0
    override val state: FlooditGame.State get() {
        return when {
            boxes.all {it.colorId == boxes.first().colorId} -> FlooditGame.State.WON
            round>=maxTurns -> FlooditGame.State.LOST
            else -> FlooditGame.State.RUNNING
        }
    }

    override fun get(x: Int, y: Int): Int {
        return if (boxes.isValid(x,y)){
            boxes[x,y].colorId
        } else
            -1
    }

    override fun playColour(clr: Int) {
        if (state != FlooditGame.State.RUNNING){
            throw IllegalStateException("the game is finished")
        }
        val adjacentBoxes: MutableList<Box> = mutableListOf(boxes[0, 0])
        var repeat = true
        while (repeat && state == FlooditGame.State.RUNNING) {
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
        round++
        notifyMove(round)
        if (state != FlooditGame.State.RUNNING){
            notifyWin(round)
        }
    }
    fun computerMoveSimple(){
        playColour((0..<colourCount).random())
    }
    fun computerMove(): Pair<Int, Int> {
        val adjacentBoxes: MutableList<Box> = mutableListOf(boxes[0, 0])
        var repeat = true
        while (repeat && state == FlooditGame.State.RUNNING) {
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
        val adjacentBoxesDifferentColours: MutableList<Box> = mutableListOf()
        val adjacentBoxesDifferentColoursID: MutableList<Int> = mutableListOf()
        for (box in adjacentBoxes) {
            for (adjacentCoordinates in box.adjacentBoxesCoordinates) {
                if (boxes[adjacentCoordinates.first, adjacentCoordinates.second].colorId != box.colorId) {
                    adjacentBoxesDifferentColoursID.add(boxes[adjacentCoordinates.first, adjacentCoordinates.second].colorId)
                    if (adjacentBoxes.indexOf(boxes[adjacentCoordinates.first, adjacentCoordinates.second]) == -1){
                        adjacentBoxesDifferentColours.add(boxes[adjacentCoordinates.first, adjacentCoordinates.second])
                    }
                }
            }
        }
        val mostEncounteredColourID: Int = (adjacentBoxesDifferentColoursID.groupingBy { it }.eachCount()).maxByOrNull { it.value }!!.key
        var boxPlayedCoordinate: Pair<Int, Int> = Pair(0,0)
        for (box in adjacentBoxesDifferentColours){
            if (box.colorId == mostEncounteredColourID){
                boxPlayedCoordinate = Pair(box.boxX, box.boxY)
                break
            }
        }
        playColour(mostEncounteredColourID)
        return boxPlayedCoordinate
    }
    private val onGameOverListeners = mutableListOf<FlooditGame.GameOverListener>()
    private val onGamePlayListeners = mutableListOf<FlooditGame.GamePlayListener>()

    override fun addGameOverListener(gameOverListener: FlooditGame.GameOverListener) {
        if (onGameOverListeners.indexOf(gameOverListener)==-1){
            onGameOverListeners.add(gameOverListener)
        }
    }

    override fun addGamePlayListener(listener: FlooditGame.GamePlayListener) {
        if (onGamePlayListeners.indexOf(listener)==-1){
            onGamePlayListeners.add(listener)
        }
    }

    override fun removeGameOverListener(gameOverListener: FlooditGame.GameOverListener) {
        onGameOverListeners.remove(gameOverListener)
    }

    override fun removeGamePlayListener(listener: FlooditGame.GamePlayListener) {
        onGamePlayListeners.remove(listener)
    }

    override fun notifyWin(round: Int) {
        for(listener in onGameOverListeners) {
            var isWon: Boolean = false
            if (state == FlooditGame.State.WON){
                isWon = true
            }
            listener.onGameOver(this, round, isWon)
        }
    }

    override fun notifyMove(round: Int) {
        for(listener in onGamePlayListeners) {
            listener.onGameChanged(this, round)
        }
    }
    init {
        require(width>0){
            "Width must be more than 0"
        }
        require(height>0){
            "Height must be more than 0"
        }
        require(maxTurns>0){
            "there must be at least 1 turn"
        }
        require(colourCount>1)
        //require(width*height>=colourCount){
        //   "colour amount should be more than maximum allowed colours"
        //}
    }
}