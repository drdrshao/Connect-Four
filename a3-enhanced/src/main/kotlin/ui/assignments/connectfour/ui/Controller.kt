package ui.assignments.connectfour.ui

import javafx.animation.Interpolator
import javafx.animation.TranslateTransition
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.image.ImageView
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.Text
import javafx.util.Duration
import ui.assignments.connectfour.model.Grid
import ui.assignments.connectfour.model.Model
import ui.assignments.connectfour.model.Player
import java.lang.Double.max
import java.lang.Double.min
import java.lang.Exception
import kotlin.math.floor


class Controller(w: Double, h: Double) {

    val WINDOW_WIDTH = w
    val WINDOW_HEIGHT= h
    var rows = 0
    var cols = 0
    val labelFonts = Font.font(20.0)
    var curCol = -1
    var model = Model
    private val pieceSize = 70.0
    var pane = VBox()
    val redPieceStack = StackPane()
    val yellowPieceStack = StackPane()
    val pieceBox = HBox()
    var playerHBox = HBox()
    val boardVBox = VBox()
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
        label1.textFill = Color.GRAY
        label2.textFill = Color.GRAY
        val labelW = Label("Rows")
        val labelH = Label("Columns")
        val labelL = Label("Length")
        val inputW = TextField()
        val inputH = TextField()
        val inputL = TextField()
        val inputBox = HBox(10.0, labelW, inputW, labelH, inputH, labelL, inputL)
        val startGameLabel = Button("Click here to start game!")
        startGameLabel.isDisable = true

        inputH.textProperty().addListener{ _, _, new ->
            startGameLabel.isDisable = (new.isEmpty() || inputW.text.isEmpty() || inputL.text.isEmpty())
        }
        inputW.textProperty().addListener{ _, _, new ->
            startGameLabel.isDisable = (new.isEmpty() || inputH.text.isEmpty() || inputL.text.isEmpty())
        }
        inputL.textProperty().addListener{ _, _, new ->
            startGameLabel.isDisable = (new.isEmpty() || inputW.text.isEmpty() || inputH.text.isEmpty())
        }

        startGameLabel.onMouseClicked = EventHandler {
            try {
                rows = inputH.text.toInt()
                cols = inputW.text.toInt()
                if (rows < cols) {
                    val a = Alert(Alert.AlertType.WARNING)
                    a.contentText = "Rows need to be smaller than columns."
                    a.show()
                }
                else if (rows > 12 || rows < 1) {
                    val a = Alert(Alert.AlertType.WARNING)
                    a.contentText = "Columns needs to be between 1 and 12."
                    a.show()
                }
                else if (cols > 10 || cols < 1) {
                    val a = Alert(Alert.AlertType.WARNING)
                    a.contentText = "Rows needs to be between 1 and 10."
                    a.show()
                }
                else if (inputL.text.toInt() < 1) {
                    val a = Alert(Alert.AlertType.WARNING)
                    a.contentText = "Length must be greater or equal to 1."
                    a.show()
                } else{
                    model.length = inputL.text.toInt()
                    model.width = rows
                    model.height = cols
                    model.grid = Grid(model.width, model.height, model.length)
                    startGame()
                }
            } catch (_: Exception) {
                val a = Alert(Alert.AlertType.WARNING)
                a.contentText = "Input should be integers."
                a.show()
            }

        }
        startGameLabel.background = Background(BackgroundFill(Color.rgb(0, 180, 0, 0.7), CornerRadii(10.0), null))
        val region3 = Region()
        val region4 = Region()
        HBox.setHgrow(region3, Priority.ALWAYS)
        HBox.setHgrow(region4, Priority.ALWAYS)

        val infoBox = VBox(10.0, inputBox, startGameLabel)
        infoBox.alignment = Pos.CENTER

        val startBox = HBox(region3, infoBox, region4)
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
        pane.children.add(HBox(region1, boardVBox, region2))
    }

    private fun startGame() {
        pane.children.clear()
        for (i in 1..cols) {
            val boardHBox = HBox()
            for (j in 1..rows) {
                val boardPic = ImageView(Controller::class.java.getResource("/ui/assignments/connectfour/grid_1x1.png")?.toURI().toString())
                boardPic.fitWidth = pieceSize
                boardPic.fitHeight = pieceSize
                boardHBox.children.add(boardPic)
            }
            boardVBox.children.add(boardHBox)
        }
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
        pane.children.add(HBox(region1, boardVBox, region2))
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
                val leftBound = (WINDOW_WIDTH - rows * pieceSize) / 2.0
                val rightBound = leftBound + rows * pieceSize
                if (it.sceneX in leftBound..rightBound) {
                    curCol = floor((adaptX - leftBound) / pieceSize).toInt()
                    redPiecePic.translateX = leftBound + curCol * pieceSize - 10
                } else {
                    redPiecePic.translateX = min(max(0.0, it.sceneX - pieceSize / 2 - 10), WINDOW_WIDTH - 20 - pieceSize)
                }
            }

            redPiecePic.onMouseReleased = EventHandler {
                val leftBound = (WINDOW_WIDTH - rows * pieceSize) / 2.0
                val rightBound = leftBound + rows * pieceSize
                if (it.sceneX in leftBound..rightBound) {
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
                val leftBound = (WINDOW_WIDTH - rows * pieceSize) / 2.0
                val rightBound = leftBound + rows * pieceSize
                if (it.sceneX in leftBound..rightBound) {
                    curCol = floor((adaptX - leftBound) / pieceSize).toInt()
                    yellowPiece.translateX = leftBound + curCol * pieceSize  - (WINDOW_WIDTH - 20 - pieceSize) - 10
                } else {
                    yellowPiece.translateX = min(max(0.0, it.sceneX - pieceSize / 2 - 10), WINDOW_WIDTH - 20 - pieceSize) - (WINDOW_WIDTH - 20 - pieceSize)
                }
            }

            yellowPiece.onMouseReleased = EventHandler {
                val leftBound = (WINDOW_WIDTH - rows * pieceSize) / 2.0
                val rightBound = leftBound + rows * pieceSize
                if (it.sceneX in leftBound..rightBound) {
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
        if (model.onGameWin.value == Player.TWO) {
            endLabel.background = Background(BackgroundFill(Color.LIGHTYELLOW, CornerRadii(10.0), Insets(-10.0)))
            endLabel.text = "Game is over! Player #2 Wins!"
        }
        else if (model.onGameWin.value == Player.ONE) {
            endLabel.background = Background(BackgroundFill(Color.LIGHTSALMON, CornerRadii(10.0), Insets(-10.0)))
            endLabel.text = "Game is over! Player #1 Wins!"
        }
        else if (model.onGameDraw.value == true) {
            endLabel.background = Background(BackgroundFill(Color.GRAY, CornerRadii(10.0), Insets(-10.0)))
            endLabel.text = "Draw!"
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