package uk.ac.bournemouth.ap.floodit

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import com.google.android.material.snackbar.Snackbar
import org.example.student.flooditgame.StudentFlooditGame

class FlooitView: View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )
    var game: StudentFlooditGame = StudentFlooditGame()
    private val gridPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.WHITE
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
    fun boxColour(colorIndex: Int): Paint {
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
    fun coordinateConverter(x: Float,y: Float, canvasWidth: Float, canvasHeight: Float): StudentFlooditGame.box? {
       if (x<canvasWidth*(game.width/(game.width+1))+canvasWidth/(game.width*2) && y<canvasHeight*(game.height/(game.height+1))+canvasHeight/(game.height*2)){
           return game.boxes[(canvasWidth-canvasWidth/(game.width*2)/(x-canvasWidth/(game.width*2))).toInt(),(canvasHeight-canvasHeight/(game.height*2)/(x-canvasHeight/(game.height*2))).toInt()]
       }
        else return null
    }
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if(::gestureDetector.isInitialized){
            gestureDetector.onTouchEvent(event)
        }
        return true
    }

    override fun onDraw(canvas: Canvas) {
        var canvasWidth = width.toFloat()
        var canvasHeight = height.toFloat()

        var squareSpacingX = canvasWidth/(game.height+1)
        var squareSpacingY = canvasHeight/(game.width+1)

        var spaceFromLeft = squareSpacingX/2
        var spaceFromTop = squareSpacingY/2

        canvas.drawRect(0f, 0f, canvasWidth, canvasHeight, gridPaint)

        for (row in 0 until  game.width){
            val currentBox = game.boxes.iterator()
            val y = ((squareSpacingY) * (row)) + spaceFromTop
            val yEnd = ((squareSpacingY) * (row+1)) + spaceFromTop
            for (col in 0 until game.height){
                val x = ((squareSpacingX) * (col)) + spaceFromLeft
                val xEnd = ((squareSpacingX) * (col+1)) + spaceFromLeft
                canvas.drawRect(x,y,xEnd,yEnd,boxColour(game.boxes[row,col].ColorId))
            }
        }
    }
    init {
        post {
            val viewWidth = this.width
            val viewHeight = this.height
            gestureDetector = GestureDetectorCompat(context, object:
                GestureDetector.SimpleOnGestureListener(){

                override fun onDown(e: MotionEvent): Boolean = true
                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    val lineTouche = Snackbar
                        .make(this@FlooitView, "${e.x} ${e.y}", Snackbar.LENGTH_SHORT)
                        .show()
                    return true
                }
            })
        }
    }
}