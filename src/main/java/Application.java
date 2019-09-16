import java.time.Duration;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory;

import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import riot.I2C;

public class Application {

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, InterruptedException {
		ActorSystem system = ActorSystem.create("riot-bma280-demo");
		Materializer mat = ActorMaterializer.create(system);

		// Find BMA280 device
		Flow<BMA280.Command, BMA280.Results, NotUsed> bma280 = I2C.device(BMA280.class).onBus(1).at(BMA280.DEFAULT_ADDRESS)
				.asFlow(system);

		// Now, let's set up a timer: Send a GPIOState.TOGGLE object every 500 millis
		Source<BMA280.Command, ?> timerSource = Source.tick(Duration.ZERO, Duration.ofMillis(500), BMA280.Command.READ);

		// Regularly query, then print out the values measured by the BMA280
		timerSource.via(bma280).log("BMA280").to(Sink.ignore()).run(mat);

		// Wait forever
		Thread.currentThread().join();
	}

}
