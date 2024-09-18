package uk.ac.bournemouth.ap.floodit

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
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
    private lateinit var spinner: Spinner
    private lateinit var spinner2: Spinner
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
        val gameWidth = game.width.toFloat()
        val gameHeight = game.height.toFloat()
        if (x<canvasWidth*(gameWidth/(gameWidth+1))+canvasWidth/((gameWidth+1)*2) &&
            y<canvasHeight*(gameHeight/(gameHeight+1))+canvasHeight/(gameHeight*2)){
           return game.boxes[
               ((y-canvasHeight/((gameHeight+1)*2))/((canvasHeight-canvasHeight/((gameHeight+1)))/gameHeight)).toInt(),
               ((x-canvasWidth/((gameWidth+1)*2))/((canvasWidth-canvasWidth/((gameWidth+1)))/gameWidth)).toInt()]
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
        val activity = context as MainActivity
        spinner = activity.findViewById<Spinner>(R.id.spinner)
        val spinnerItems = listOf("5x5","10x10","14x14","18x18")
        val spinnerAdapter = ArrayAdapter(activity,android.R.layout.simple_spinner_dropdown_item,spinnerItems)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter
        spinner.setSelection(1)
        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent!!.getItemAtPosition(position).toString()
                if (::spinner2.isInitialized) {
                    game = StudentFlooditGame(
                        selectedItem.split("x")[0].toInt(),
                        selectedItem.split("x")[1].toInt(),
                        colourCount =  spinner2.selectedItem.toString().toInt()
                    )
                } else {
                    game = StudentFlooditGame(
                        selectedItem.split("x")[0].toInt(),
                        selectedItem.split("x")[1].toInt())
                }
                invalidate()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        spinner2 = activity.findViewById<Spinner>(R.id.spinner2)
        val spinner2Items = listOf(3,4,5,6)
        val spinner2Adapter = ArrayAdapter(activity,android.R.layout.simple_spinner_dropdown_item,spinner2Items)
        spinner2Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner2.adapter = spinner2Adapter
        spinner2.setSelection(3)
        spinner2.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent!!.getItemAtPosition(position).toString()
                if (::spinner.isInitialized){
                    game = StudentFlooditGame(
                        spinner.selectedItem.toString().split("x")[0].toInt(),
                        spinner.selectedItem.toString().split("x")[1].toInt(),colourCount = selectedItem.toInt())
                } else
                    game = StudentFlooditGame(colourCount = selectedItem.toInt())
                invalidate()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }
        post {
            val viewWidth = this.width
            val viewHeight = this.height
            gestureDetector = GestureDetectorCompat(context, object:
                GestureDetector.SimpleOnGestureListener(){

                override fun onDown(e: MotionEvent): Boolean = true
                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    if (coordinateConverter(e.x, e.y, viewWidth.toFloat(),viewHeight.toFloat())!=null){
                        game.playColour(coordinateConverter(e.x, e.y, viewWidth.toFloat(),viewHeight.toFloat())!!.ColorId)
                    }
                    invalidate()
                    return true
                }
            })
        }
    }
}