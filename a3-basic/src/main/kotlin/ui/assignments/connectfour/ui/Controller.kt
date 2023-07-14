package ui.assignments.connectfour.ui

import javafx.animation.Interpolator
import javafx.animation.TranslateTransition
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.image.ImageView
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.util.Duration
import ui.assignments.connectfour.model.Model
import ui.assignments.connectfour.model.Player
import java.lang.Double.max
import java.lang.Double.min
import kotlin.math.floor


class Controller(rows: Int, cols: Int, mod: Model) {

    val WINDOW_WIDTH = 820.0
    val WINDOW_HEIGHT= 620.0

    val labelFonts = Font.font(20.0)
    val rows = rows
    val cols = cols
    var curCol = -1
    var model = mod
    private val pieceSize = 70.0
    var pane = VBox()
    val redPieceStack = StackPane()
    val yellowPieceStack = StackPane()
    val pieceBox = HBox()
    var playerHBox = HBox()
    var label1 = Label("Player #1")
    var label2 = Label("Player #2")

    init {
        val r = Region()
        HBox.setHgrow(r, Priority.ALWAYS)
        pieceBox.children.addAll(redPieceStack, r, yellowPieceStack)
        pieceBox.padding = Insets(0.0, 10.0, 0.0, 10.0)
        initGame()
    }

    private fun initGame() {
        model.startGame()
        val boardPic = ImageView(Controller::class.java.getResource("/ui/assignments/connectfour/grid_8x7.png")?.toURI().toString())
        boardPic.fitWidth = pieceSize * rows.toDouble()
        boardPic.fitHeight = pieceSize * cols.toDouble()
        label1.textFill = Color.GRAY
        label2.textFill = Color.GRAY
        val startGameLabel = Label("Click here to start game!")
        startGameLabel.onMouseClicked = EventHandler {
            startGame()
        }
        startGameLabel.background = Background(BackgroundFill(Color.rgb(0, 180, 0, 0.7), CornerRadii(10.0), Insets(-10.0)))
        val region3 = Region()
        val region4 = Region()
        HBox.setHgrow(region3, Priority.ALWAYS)
        HBox.setHgrow(region4, Priority.ALWAYS)
        val startBox = HBox(region3, startGameLabel, region4)
        startGameLabel.prefHeight = pieceSize - 10.0
        startGameLabel.font = labelFonts
        startBox.padding = Insets(15.0)
        startBox.prefHeight = pieceSize
        label1.font = Font.font(30.0)
        label2.font = Font.font(30.0)
        val region = Region()
        val region1 = Region()
        val region2 = Region()
        HBox.setHgrow(region1, Priority.ALWAYS)
        HBox.setHgrow(region2, Priority.ALWAYS)
        val playerHBox = HBox(label1, region, label2)
        playerHBox.padding = Insets(0.0, 5.0, 0.0, 5.0)
        HBox.setHgrow(region, Priority.ALWAYS)
        pane.children.add(playerHBox)
        pane.children.add(startBox)
        pane.children.add(HBox(region1, boardPic, region2))
    }

    private fun startGame() {
        pane.children.clear()
        val boardPic = ImageView(Controller::class.java.getResource("/ui/assignments/connectfour/grid_8x7.png")?.toURI().toString())
        boardPic.fitWidth = pieceSize * rows.toDouble()
        boardPic.fitHeight = pieceSize * cols.toDouble()

        val region = Region()
        val region1 = Region()
        val region2 = Region()
        val region3 = Region()

        HBox.setHgrow(region1, Priority.ALWAYS)
        HBox.setHgrow(region2, Priority.ALWAYS)
        HBox.setHgrow(region3, Priority.ALWAYS)
        playerHBox = HBox(label1, region, label2)
        playerHBox.padding = Insets(0.0, 5.0, 0.0, 5.0)
        HBox.setHgrow(region, Priority.ALWAYS)
        pane.children.add(playerHBox)
        pane.children.add(pieceBox)
        pane.children.add(HBox(region1, boardPic, region2))
        createPiece(1)
    }

    private fun createPiece(player: Int) {
        if (player == 1) {
            label1.textFill = Color.BLACK
            label2.textFill = Color.GRAY
            val redPiecePic = ImageView(Controller::class.java.getResource("/ui/assignments/connectfour/piece_red.png")?.toURI().toString())
            redPiecePic.fitWidth = pieceSize
            redPiecePic.fitHeight = pieceSize
            redPiecePic.onMouseDragged = EventHandler {
                val adaptX = it.sceneX
                val leftBound = (WINDOW_WIDTH - 8 * pieceSize) / 2.0
                val rightBound = leftBound + 8 * pieceSize
                if (it.sceneX in leftBound..rightBound) {
                    curCol = floor((adaptX - leftBound) / pieceSize).toInt()
                    redPiecePic.translateX = leftBound + curCol * pieceSize - 10
                } else {
                    redPiecePic.translateX = min(max(0.0, it.sceneX - pieceSize / 2 - 10), 800.0 - pieceSize)
                }
            }

            redPiecePic.onMouseReleased = EventHandler {
                if (it.sceneX in 120.0..680.0) {
                    model.dropPiece(curCol)
                    if (model.onPieceDropped.value == null) {
                        val targetX = 0.0
                        val anime = TranslateTransition(Duration.seconds(0.6), redPiecePic)
                        anime.interpolator = Interpolator.EASE_IN
                        anime.fromX = redPiecePic.translateX
                        anime.toX = targetX
                        anime.play()
                    } else {
                        val row = model.onPieceDropped.value!!.y
                        val targetY = redPiecePic.translateY +  (row + 1) * pieceSize
                        val anime = TranslateTransition(Duration.seconds(0.6), redPiecePic)
                        anime.interpolator = Interpolator.EASE_IN
                        anime.fromY = redPiecePic.translateY
                        anime.toY = targetY
                        anime.play()

                        if (model.onGameDraw.value == true || model.onGameWin.value != Player.NONE) {
                            endGame()
                        } else {
                            createPiece(2)
                        }
                    }
                } else {
                    val targetX = 0.0
                    val anime = TranslateTransition(Duration.seconds(0.6), redPiecePic)
                    anime.interpolator = Interpolator.EASE_IN
                    anime.fromX = redPiecePic.translateX
                    anime.toX = targetX
                    anime.play()
                }
            }
            redPieceStack.children.add(redPiecePic)
        }
        if (player == 2) {
            label2.textFill = Color.BLACK
            label1.textFill = Color.GRAY
            val yellowPiece = ImageView(Controller::class.java.getResource("/ui/assignments/connectfour/piece_yellow.png")?.toURI().toString())
            yellowPiece.fitWidth = pieceSize
            yellowPiece.fitHeight = pieceSize
            yellowPiece.onMouseDragged = EventHandler {
                val adaptX = it.sceneX
                val leftBound = (WINDOW_WIDTH - 8 * pieceSize) / 2.0
                val rightBound = leftBound + 8 * pieceSize
                if (it.sceneX in leftBound..rightBound) {
                    curCol = floor((adaptX - leftBound) / pieceSize).toInt()
                    yellowPiece.translateX = leftBound + curCol * pieceSize  - (800 - pieceSize) - 10
                } else {
                    yellowPiece.translateX = min(max(0.0, it.sceneX - pieceSize / 2 - 10), 800.0 - pieceSize) - (800 - pieceSize)
                }
            }

            yellowPiece.onMouseReleased = EventHandler {
                if (it.sceneX in 120.0..680.0) {
                    model.dropPiece(curCol)
                    if (model.onPieceDropped.value == null) {
                        val targetX = 0.0
                        val anime = TranslateTransition(Duration.seconds(0.6), yellowPiece)
                        anime.interpolator = Interpolator.EASE_IN
                        anime.fromX = yellowPiece.translateX
                        anime.toX = targetX
                        anime.play()
                    } else {
                        val row = model.onPieceDropped.value!!.y
                        val targetY = yellowPiece.translateY +  (row + 1) * pieceSize
                        val anime = TranslateTransition(Duration.seconds(0.6), yellowPiece)
                        anime.interpolator = Interpolator.EASE_IN
                        anime.fromY = yellowPiece.translateY
                        anime.toY = targetY
                        anime.play()
                        if (model.onGameDraw.value == true || model.onGameWin.value != Player.NONE) {
                            endGame()
                        } else {
                            createPiece(1)
                        }

                    }
                } else {
                    val targetX = 0.0
                    val anime = TranslateTransition(Duration.seconds(0.6), yellowPiece)
                    anime.interpolator = Interpolator.EASE_IN
                    anime.fromX = yellowPiece.translateX
                    anime.toX = targetX
                    anime.play()
                }
            }
            yellowPieceStack.children.add(yellowPiece)
        }
    }

    private fun endGame() {
        val endLabel = Label()
        endLabel.alignment = Pos.CENTER
        endLabel.font = labelFonts
        if (model.onGameDraw.value == true) {
            endLabel.background = Background(BackgroundFill(Color.GRAY, CornerRadii(10.0), Insets(-10.0)))
            endLabel.text = "Draw!"
        }
        else if (model.onGameWin.value == Player.ONE) {
            endLabel.background = Background(BackgroundFill(Color.LIGHTSALMON, CornerRadii(10.0), Insets(-10.0)))
            endLabel.text = "Game is over! Player #1 Wins!"
        }
        else if (model.onGameWin.value == Player.TWO) {
            endLabel.background = Background(BackgroundFill(Color.LIGHTYELLOW, CornerRadii(10.0), Insets(-10.0)))
            endLabel.text = "Game is over! Player #2 Wins!"
        }

        val region3 = Region()
        val region4 = Region()
        HBox.setHgrow(region3, Priority.ALWAYS)
        HBox.setHgrow(region4, Priority.ALWAYS)
        val startBox = HBox(region3, endLabel, region4)

        startBox.padding = Insets(10.0)
        endLabel.translateY = pieceSize - 20.0
        endLabel.prefHeight = pieceSize - 30.0
        endLabel.prefWidth = 300.0
        val r = Region()

        HBox.setHgrow(r, Priority.ALWAYS)
        playerHBox.children.add(1, endLabel)
        playerHBox.children.add(1, r)
    }
}