import fi.iki.elonen.NanoHTTPD
import java.util.concurrent.Executors

class MyHttpServer(private val port: Int) : NanoHTTPD(port) {

    override fun serve(session: IHTTPSession): Response {
        // Vérifiez la méthode et l'URI de la requête
        if (session.method == Method.GET && session.uri == "/detect") {
            // Le chat est détecté
            return newFixedLengthResponse("Chat detected!")
        }
        return newFixedLengthResponse("Not Found")
    }

    fun startServer() {
        val executor = Executors.newCachedThreadPool()
        executor.submit { start() }
    }
}
