import java.io.IOException;

import com.pi4j.io.i2c.I2CDevice;

import riot.protocols.I2CProtocol;
import riot.protocols.ProtocolDescriptor;

/**
 * BMA280 protocol
 * 
 * @see http://www.mouser.com/ds/2/783/BST-BMA280-DS000-11_published-786496.pdf
 */
public class BMA280 implements I2CProtocol<BMA280.Command, BMA280.Results> {
	public static final int BGW_CHIPID = 0x00;
	public static final int ACCD_X_LSB = 0x02;
	public static final int ACCD_X_MSB = 0x03;
	public static final int ACCD_Y_LSB = 0x04;
	public static final int ACCD_Y_MSB = 0x05;
	public static final int ACCD_Z_LSB = 0x06;
	public static final int ACCD_Z_MSB = 0x07;
	public static final int ACCD_TEMP = 0x08;
	public static final int INT_STATUS_0 = 0x09;
	public static final int INT_STATUS_1 = 0x0A;
	public static final int INT_STATUS_2 = 0x0B;
	public static final int INT_STATUS_3 = 0x0C;
	public static final int FIFO_STATUS = 0x0E;
	public static final int PMU_RANGE = 0x0F;
	public static final int PMU_BW = 0x10;
	public static final int PMU_LPW = 0x11;
	public static final int PMU_LOW_NOISE = 0x12;
	public static final int ACCD_HBW = 0x13;
	public static final int BGW_SOFTRESET = 0x14;
	public static final int INT_EN_0 = 0x16;
	public static final int INT_EN_1 = 0x17;
	public static final int INT_EN_2 = 0x18;
	public static final int INT_MAP_0 = 0x19;
	public static final int INT_MAP_1 = 0x1A;
	public static final int INT_MAP_2 = 0x1B;
	public static final int INT_SRC = 0x1E;
	public static final int INT_OUT_CTRL = 0x20;
	public static final int INT_RST_LATCH = 0x21;
	public static final int INT_0 = 0x22;
	public static final int INT_1 = 0x23;
	public static final int INT_2 = 0x24;
	public static final int INT_3 = 0x25;
	public static final int INT_4 = 0x26;
	public static final int INT_5 = 0x27;
	public static final int INT_6 = 0x28;
	public static final int INT_7 = 0x29;
	public static final int INT_8 = 0x2A;
	public static final int INT_9 = 0x2B;
	public static final int INT_A = 0x2C;
	public static final int INT_B = 0x2D;
	public static final int INT_C = 0x2E;
	public static final int INT_D = 0x2F;
	public static final int FIFO_CONFIG_0 = 0x30;
	public static final int PMU_SELF_TEST = 0x32;
	public static final int TRIM_NVM_CTRL = 0x33;
	public static final int BGW_SPI3_WDT = 0x34;
	public static final int OFC_CTRL = 0x36;
	public static final int OFC_SETTING = 0x37;
	public static final int OFC_OFFSET_X = 0x38;
	public static final int OFC_OFFSET_Y = 0x39;
	public static final int OFC_OFFSET_Z = 0x3A;
	public static final int TRIM_GP0 = 0x3B;
	public static final int TRIM_GP1 = 0x3C;
	public static final int FIFO_CONFIG_1 = 0x3E;
	public static final int FIFO_DATA = 0x3F;

	public static final int ADDRESS = 0x18; // if ADO is 0 (default)

	public static final int AFS_2G = 0x02;
	public static final int AFS_4G = 0x05;
	public static final int AFS_8G = 0x08;
	public static final int AFS_16G = 0x0C;

	public static final int BW_7_81Hz = 0x08; // 15.62 Hz sample rate, etc
	public static final int BW_15_63Hz = 0x09;
	public static final int BW_31_25Hz = 0x0A;
	public static final int BW_62_5Hz = 0x0B;
	public static final int BW_125Hz = 0x0C; // 250 Hz sample rate
	public static final int BW_250Hz = 0x0D;
	public static final int BW_500Hz = 0x0E;
	public static final int BW_1000Hz = 0x0F; // 2 kHz sample rate == unfiltered data

	public static final int normal_Mode = 0x00; // define power modes
	public static final int deepSuspend_Mode = 0x01;
	public static final int lowPower_Mode = 0x02;
	public static final int suspend_Mode = 0x04;

	public static final int sleep_0_5ms = 0x05; // define sleep duration in low power modes
	public static final int sleep_1ms = 0x06;
	public static final int sleep_2ms = 0x07;
	public static final int sleep_4ms = 0x08;
	public static final int sleep_6ms = 0x09;
	public static final int sleep_10ms = 0x0A;
	public static final int sleep_25ms = 0x0B;
	public static final int sleep_50ms = 0x0C;
	public static final int sleep_100ms = 0x0D;
	public static final int sleep_500ms = 0x0E;
	public static final int sleep_1000ms = 0x0F;

	public static enum Command {
		READ, CALIBRATE, SELFTEST
	}

	public static class Results {
		int x, y, z, temp;
	}

	@Override
	public ProtocolDescriptor<Command, Results> getDescriptor() {
		return new ProtocolDescriptor<Command, Results>(Command.class, Results.class);
	}

	@Override
	public void init(I2CDevice dev) throws IOException {
		
	}

	@Override
	public Results exec(I2CDevice dev, Command message) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void shutdown(I2CDevice dev) throws IOException {
		// TODO Auto-generated method stub
		
	}
	
	

}
