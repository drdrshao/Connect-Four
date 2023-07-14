package ui.assignments.connectfour

import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage
import ui.assignments.connectfour.model.Grid
import ui.assignments.connectfour.model.Model
import ui.assignments.connectfour.ui.Controller

class ConnectFourApp : Application() {
    override fun start(stage: Stage) {
        val mod = Model

        val c = Controller(8, 7, mod)


        val scene = Scene(c.pane, 820.0, 600.0)
        stage.title = "CS349 - A3 Connect Four - g3shao"
        stage.scene = scene
        stage.isResizable = false
        stage.show()
    }
}