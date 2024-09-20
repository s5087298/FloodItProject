package uk.ac.bournemouth.ap.floodit

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.core.view.GestureDetectorCompat
import kotlinx.coroutines.*
import org.example.student.flooditgame.StudentFlooditGame
import uk.ac.bournemouth.ap.floodit.lib.FlooditGame

class FlooditView: View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )
    private lateinit var gridSizeSpinner: Spinner
    private lateinit var colourAmountSpinner: Spinner
    private lateinit var maxTurnSpinner: Spinner
    private lateinit var aiSpinner: Spinner
    private lateinit var roundText: TextView
    private lateinit var restartButton: Button
    private lateinit var computerPointer: Pair<Int,Int>

    var game: StudentFlooditGame = StudentFlooditGame()

    private val gridPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.WHITE
    }
    private val borderPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
        strokeWidth = 20f
    }
    private val outerCirclePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
        strokeWidth = 10f
    }
    private val color0: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.RED
    }
    private val color1: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.CYAN
    }
    private val color2: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.YELLOW
    }
    private val color3: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.GREEN
    }
    private val color4: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.MAGENTA
    }
    private val color5: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.BLUE
    }
    private val color6: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.GRAY
    }
    private val color7: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.BLACK
    }
// converts colour id to colour value
    private fun boxColour(colorIndex: Int): Paint {
        return when (colorIndex) {
            0 -> color0
            1 -> color1
            2 -> color2
            3 -> color3
            4 -> color4
            5 -> color5
            6 -> color6
            else -> color7
        }
    }

    private lateinit var gestureDetector: GestureDetectorCompat
// determines pressed box using coordinates
    fun coordinateConverter(x: Float,y: Float, canvasWidth: Float, canvasHeight: Float): StudentFlooditGame.Box? {
        val gameWidth = game.width.toFloat()
        val gameHeight = game.height.toFloat()
        return if (x<canvasWidth*(gameWidth/(gameWidth+1))+canvasWidth/((gameWidth+1)*2) &&
            y<canvasHeight*(gameHeight/(gameHeight+1))+canvasHeight/(gameHeight*2)){
            game.boxes[
                ((y-canvasHeight/((gameHeight+1)*2))/((canvasHeight-canvasHeight/((gameHeight+1)))/gameHeight)).toInt(),
                ((x-canvasWidth/((gameWidth+1)*2))/((canvasWidth-canvasWidth/((gameWidth+1)))/gameWidth)).toInt()]
        } else null
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if(::gestureDetector.isInitialized){
            gestureDetector.onTouchEvent(event)
        }
        return true
    }


    override fun onDraw(canvas: Canvas) {
        val canvasWidth = width.toFloat()
        val canvasHeight = height.toFloat()

        val squareSpacingX = canvasWidth/(game.height+1)
        val squareSpacingY = canvasHeight/(game.width+1)

        val spaceFromLeft = squareSpacingX/2
        val spaceFromTop = squareSpacingY/2

        canvas.drawRect(0f, 0f, canvasWidth, canvasHeight, gridPaint)
        canvas.drawRect(
            spaceFromLeft - 10f,
            spaceFromTop - 10f,
            squareSpacingX * (game.width+1) + 10f - spaceFromLeft,
            squareSpacingY * (game.height+1) - spaceFromTop + 10f, borderPaint)

        for (row in 0 until  game.width){
            val y = ((squareSpacingY) * (row)) + spaceFromTop
            val yEnd = ((squareSpacingY) * (row+1)) + spaceFromTop
            for (col in 0 until game.height){
                val x = ((squareSpacingX) * (col)) + spaceFromLeft
                val xEnd = ((squareSpacingX) * (col+1)) + spaceFromLeft
                canvas.drawRect(x,y,xEnd,yEnd,boxColour(game.boxes[row,col].colourId))
            }
        }
        if (::computerPointer.isInitialized && game.computerGame !=0 && game.round != 0){
            canvas.drawCircle(
                spaceFromLeft + computerPointer.first.toFloat() * squareSpacingX + squareSpacingX/2,
                spaceFromTop + computerPointer.second.toFloat() * squareSpacingY + squareSpacingY/2,
                10f, color7)
            canvas.drawCircle(
                spaceFromLeft + computerPointer.first.toFloat() * squareSpacingX + squareSpacingX/2,
                spaceFromTop + computerPointer.second.toFloat() * squareSpacingY + squareSpacingY/2,
                30f, outerCirclePaint)
        }

    }
    init {// declaring activities outside of this view
        val activity = context as MainActivity

        roundText = activity.findViewById(R.id.textView)

        gridSizeSpinner = activity.findViewById(R.id.gridSizeSpinner)
        val gridSizeSpinnerItems = listOf("5x5","10x10","14x14","18x18")
        val gridSizeSpinnerAdapter = ArrayAdapter(activity,android.R.layout.simple_spinner_dropdown_item,gridSizeSpinnerItems)
        gridSizeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        gridSizeSpinner.adapter = gridSizeSpinnerAdapter
        gridSizeSpinner.setSelection(1)

        colourAmountSpinner = activity.findViewById(R.id.colourAmountSpinner)
        val colourAmountSpinnerItems = listOf(3,4,5,6)
        val colourAmountSpinnerAdapter = ArrayAdapter(activity,android.R.layout.simple_spinner_dropdown_item,colourAmountSpinnerItems)
        colourAmountSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        colourAmountSpinner.adapter = colourAmountSpinnerAdapter
        colourAmountSpinner.setSelection(3)

        maxTurnSpinner = activity.findViewById(R.id.maxTurnSpinner)
        val maxTurnSpinnerItems = listOf(20,30,40,50,60,70)
        val maxTurnSpinnerAdapter = ArrayAdapter(activity,android.R.layout.simple_spinner_dropdown_item,maxTurnSpinnerItems)
        maxTurnSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        maxTurnSpinner.adapter = maxTurnSpinnerAdapter
        maxTurnSpinner.setSelection(2)

        aiSpinner = activity.findViewById(R.id.aiSpinner)
        val aiSpinnerItems = listOf("PLAYER", "EASY AI", "HARD AI")
        val aiSpinnerAdapter = ArrayAdapter(activity,android.R.layout.simple_spinner_dropdown_item,aiSpinnerItems)
        aiSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        aiSpinner.adapter = aiSpinnerAdapter

        var aiMoveCoroutine: Job? = null
        restartButton = activity.findViewById(R.id.restartButton)
        restartButton.setOnClickListener{
            game = if ( // just in case the values of other activities aren't initialized to avoid crashes
                (::gridSizeSpinner.isInitialized) &&
                (::colourAmountSpinner.isInitialized) &&
                (::maxTurnSpinner.isInitialized) &&
                (::aiSpinner.isInitialized))
                StudentFlooditGame(
                    width = gridSizeSpinner.selectedItem.toString().split("x")[0].toInt(),
                    height = gridSizeSpinner.selectedItem.toString().split("x")[1].toInt(),
                    colourCount = colourAmountSpinner.selectedItem.toString().toInt(),
                    maxTurns = maxTurnSpinner.selectedItem.toString().toInt(),
                    computerGame = aiSpinner.selectedItemPosition
                )
             else
                StudentFlooditGame()
            invalidate()
            aiMoveCoroutine?.cancel() // coroutine to allow AI's to make turns and for player to follow them
            aiMoveCoroutine = CoroutineScope(Dispatchers.Main).launch {
                while (game.state==FlooditGame.State.RUNNING && game.computerGame != 0){
                    delay(2000)
                    when (game.computerGame) {
                        1 -> computerPointer = game.computerMoveSimple()
                        2 -> computerPointer = game.computerMove()
                    }
                    roundText.setText("${game.round}/${game.maxTurns}")
                    if (game.state != FlooditGame.State.RUNNING){
                        roundText.setText("${game.round}/${game.maxTurns} YOU " + game.state.toString())
                    }
                    invalidate()
                }
            }
            roundText.setText("${game.round}/${game.maxTurns}")
        } // using button to initialize the values in other activities immediately
        restartButton.performClick()
        post { // using post to get access to the dimensions of the canvas
            val viewWidth = this.width
            val viewHeight = this.height
            roundText.setText("${game.round}/${game.maxTurns}")
            gestureDetector = GestureDetectorCompat(context, object:
                GestureDetector.SimpleOnGestureListener(){
                override fun onDown(e: MotionEvent): Boolean = true
                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    if (game.computerGame == 0){
                        if (coordinateConverter(
                                e.x, e.y, viewWidth.toFloat(),
                                viewHeight.toFloat())!=null &&
                            game.state==FlooditGame.State.RUNNING){
                            game.playColour(coordinateConverter(e.x, e.y, viewWidth.toFloat(),viewHeight.toFloat())!!.colourId)
                        }
                        roundText.setText("${game.round}/${game.maxTurns}")
                        if (game.state != FlooditGame.State.RUNNING){
                            roundText.setText("${game.round}/${game.maxTurns} YOU " + game.state.toString())
                        }
                        invalidate()
                    }
                    return true
                }
            })
            }
        }
}