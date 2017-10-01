package orthrus.web

import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.equalTo
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it


/**
 * Created by arthur on 17/6/2017.
 */
object ConfApiSpec : Spek({
    val app = TestApp()

    beforeGroup { app.start() }
    afterGroup { app.stop() }

    describe("api") {
        describe("conf") {
            it("should return env name") {
                app.get("/conf") {
                    assert.that(it.statusCode, equalTo(200))
                    assert.that(it.body.text, equalTo("LOCAL"))
                }
            }
        }
    }

})