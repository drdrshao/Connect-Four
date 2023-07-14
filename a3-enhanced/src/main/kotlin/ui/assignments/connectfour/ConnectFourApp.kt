package ui.assignments.connectfour

import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage
import ui.assignments.connectfour.model.Grid
import ui.assignments.connectfour.model.Model
import ui.assignments.connectfour.ui.Controller

class ConnectFourApp : Application() {
    override fun start(stage: Stage) {


        val WIDTH = 1020.0
        val HEIGHT = 820.0
        val c = Controller(WIDTH, HEIGHT)


        val scene = Scene(c.pane, WIDTH, HEIGHT)
        stage.title = "CS349 - A3 Connect Four - g3shao"
        stage.scene = scene
        stage.isResizable = false
        stage.show()
    }
}