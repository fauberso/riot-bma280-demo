import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pi4j.io.i2c.I2CDevice;

import riot.protocols.I2CProtocol;
import riot.protocols.ProtocolDescriptor;

/**
 * BMA280 protocol
 * 
 * @see http://www.mouser.com/ds/2/783/BST-BMA280-DS000-11_published-786496.pdf
 */
public class BMA280 implements I2CProtocol<BMA280.Command, BMA280.Results> {
	Logger log = LoggerFactory.getLogger(BMA280.class);

	/*
	 * Local addresses
	 */
	private static final int BGW_CHIPID = 0x00;
	private static final int ACCD_X_LSB = 0x02;
	private static final int ACCD_X_MSB = 0x03;
	private static final int ACCD_Y_LSB = 0x04;
	private static final int ACCD_Y_MSB = 0x05;
	private static final int ACCD_Z_LSB = 0x06;
	private static final int ACCD_Z_MSB = 0x07;
	private static final int ACCD_TEMP = 0x08;
	private static final int INT_STATUS_0 = 0x09;
	private static final int INT_STATUS_1 = 0x0A;
	private static final int INT_STATUS_2 = 0x0B;
	private static final int INT_STATUS_3 = 0x0C;
	private static final int FIFO_STATUS = 0x0E;
	private static final int PMU_RANGE = 0x0F;
	private static final int PMU_BW = 0x10;
	private static final int PMU_LPW = 0x11;
	private static final int PMU_LOW_NOISE = 0x12;
	private static final int ACCD_HBW = 0x13;
	private static final int BGW_SOFTRESET = 0x14;
	private static final int INT_EN_0 = 0x16;
	private static final int INT_EN_1 = 0x17;
	private static final int INT_EN_2 = 0x18;
	private static final int INT_MAP_0 = 0x19;
	private static final int INT_MAP_1 = 0x1A;
	private static final int INT_MAP_2 = 0x1B;
	private static final int INT_SRC = 0x1E;
	private static final int INT_OUT_CTRL = 0x20;
	private static final int INT_RST_LATCH = 0x21;
	private static final int INT_0 = 0x22;
	private static final int INT_1 = 0x23;
	private static final int INT_2 = 0x24;
	private static final int INT_3 = 0x25;
	private static final int INT_4 = 0x26;
	private static final int INT_5 = 0x27;
	private static final int INT_6 = 0x28;
	private static final int INT_7 = 0x29;
	private static final int INT_8 = 0x2A;
	private static final int INT_9 = 0x2B;
	private static final int INT_A = 0x2C;
	private static final int INT_B = 0x2D;
	private static final int INT_C = 0x2E;
	private static final int INT_D = 0x2F;
	private static final int FIFO_CONFIG_0 = 0x30;
	private static final int PMU_SELF_TEST = 0x32;
	private static final int TRIM_NVM_CTRL = 0x33;
	private static final int BGW_SPI3_WDT = 0x34;
	private static final int OFC_CTRL = 0x36;
	private static final int OFC_SETTING = 0x37;
	private static final int OFC_OFFSET_X = 0x38;
	private static final int OFC_OFFSET_Y = 0x39;
	private static final int OFC_OFFSET_Z = 0x3A;
	private static final int TRIM_GP0 = 0x3B;
	private static final int TRIM_GP1 = 0x3C;
	private static final int FIFO_CONFIG_1 = 0x3E;
	private static final int FIFO_DATA = 0x3F;

	public static final int DEFAULT_ADDRESS = 0x18; // when ADO is LOW

	/**
	 * Accelerometer Scale
	 */
	public enum AccelerometerScale {
		AFS_2G(0x02), AFS_4G(0x05), AFS_8G(0x08), AFS_16G(0x0C);

		private AccelerometerScale(int value) {
			this.value = (byte) value;
		}

		private final byte value;
	}

	private final byte accelerometerScale;

	/**
	 * Bandwidth
	 */
	public enum Bandwidth {
		BW_7_81Hz(0x08), // 15.62 Hz sample rate, etc
		BW_15_63Hz(0x09), //
		BW_31_25Hz(0x0A), //
		BW_62_5Hz(0x0B), //
		BW_125Hz(0x0C), // 250 Hz sample rate
		BW_250Hz(0x0D), //
		BW_500Hz(0x0E), //
		BW_1000Hz(0x0F); // 2 kHz sample rate == unfiltered data

		private Bandwidth(int value) {
			this.value = (byte) value;
		}

		private final byte value;
	}

	private final byte bandwidth;

	/**
	 * Power Mode
	 */
	public enum PowerMode {
		normal_Mode(0x00b), //
		deepSuspend_Mode(0x01), //
		lowPower_Mode(0x02), //
		suspend_Mode(0x04);

		private PowerMode(int value) {
			this.value = (byte) value;
		}

		private final byte value;
	}

	private final byte powerMode;

	/**
	 * Sleep Duration in Low-Power Mode
	 */
	public enum SleepDuration {
		sleep0_5ms(0x05), sleep1ms(0x06), sleep2ms(0x07), sleep4ms(0x08), sleep6ms(0x09), sleep10ms(0x0A), sleep25ms(
				0x0B), sleep50ms(0x0C), sleep100ms(0x0D), sleep500ms(0x0E), sleep1000ms(0x0F);

		private SleepDuration(int value) {
			this.value = (byte) value;
		}

		private final byte value;
	}

	private final byte sleepDuration;

	/**
	 * Commands served by the Actor
	 */
	public static enum Command {
		READ, CALIBRATE, SELFTEST
	}

	/**
	 * Results of a command execution
	 */
	public static class Results {
		int x, y, z, temp;
	}

	public BMA280() {
		this(AccelerometerScale.AFS_16G, Bandwidth.BW_125Hz, PowerMode.normal_Mode, SleepDuration.sleep1ms);
	}

	public BMA280(AccelerometerScale accelerometerScale, Bandwidth bandwidth, PowerMode powerMode,
			SleepDuration sleepDuration) {
		this.accelerometerScale = accelerometerScale.value;
		this.bandwidth = bandwidth.value;
		this.powerMode = powerMode.value;
		this.sleepDuration = sleepDuration.value;
	}

	@Override
	public ProtocolDescriptor<Command, Results> getDescriptor() {
		return new ProtocolDescriptor<Command, Results>(Command.class, Results.class);
	}

	@Override
	public void init(I2CDevice dev) throws IOException {
		dev.write(BGW_SOFTRESET, (byte) 0xB6);
		
		dev.write(PMU_RANGE, accelerometerScale);
		dev.write(PMU_BW, bandwidth);
		dev.write(PMU_LPW, (byte) (powerMode << 5 | sleepDuration << 1));

		dev.write(INT_EN_1, (byte) 0x10); // set data ready interrupt (bit 4)
		dev.write(INT_MAP_1, (byte) 0x01); // map data ready interrupt to INT1 (bit 0)
		dev.write(INT_EN_0, (byte) (0x20 | 0x10)); // set single tap interrupt (bit 5) and double tap interrupt (bit 4)
		dev.write(INT_MAP_2, (byte) (0x20 | 0x10)); // map single and double tap interrupts to INT2 (bits 4 and 5)
		dev.write(INT_9, (byte) 0x0A); // set tap threshold to 10 x 3.125% of full range
		dev.write(INT_OUT_CTRL, (byte) (0x04 | 0x01)); // interrupts push-pull, active HIGH (bits 0:3)
		
		log.info("BGA280 initialized. Chip ID {}.", dev.read(BGW_CHIPID));
	}

	@Override
	public Results exec(I2CDevice dev, Command message) throws IOException {
		System.out.println(message);
		return new Results();
	}

	@Override
	public void shutdown(I2CDevice dev) throws IOException {
		// TODO Auto-generated method stub

	}

}
