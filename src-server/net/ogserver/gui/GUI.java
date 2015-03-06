package net.ogserver.gui;

/*
* Copyright (c) 2015
* Christian Tucker.  All rights reserved.
*
* The use of OGServer is free of charge for personal and commercial use. *
*
* THIS SOFTWARE IS PROVIDED 'AS IS' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, 
* BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
* PARTICULAR PURPOSE, OR NON-INFRINGEMENT, ARE DISCLAIMED.  
* IN NO EVENT SHALL THE AUTHOR  BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, 
* SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
* PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
* INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
* CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
* ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
* THE POSSIBILITY OF SUCH DAMAGE.
*  
*   * Policy subject to change.
*/

import java.awt.Dimension;
import java.text.DecimalFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JLabel;

import net.ogserver.common.Session;


/**
 * This class is a test class for a future feature, you can execute this class to see
 * a basic text-representation of current networking status by setting {@link Config#launchGUI}
 * to true.
 * 
 * @author Christian
 *
 */
@SuppressWarnings("serial")
public class GUI extends JFrame implements Runnable {

	private final static ScheduledExecutorService serverTick = Executors.newScheduledThreadPool(1);

	private JLabel currentBytesIn;
	private JLabel averageBytesIn;
	private JLabel currentBytesOut;
	private JLabel averageBytesOut;
	private JLabel tickCount;
	
	public GUI() {
		this.setTitle("OGServer server statistics, ALPHA");
		this.setSize(new Dimension(400, 400));
		this.setLayout(null);
		this.setVisible(true);
		this.setResizable(false);
		this.setLocationRelativeTo(null);

		currentBytesIn  = new JLabel("Current input/second = 0000b/s");
		currentBytesIn.setBounds(0, 0, 250, 20);
		averageBytesIn  = new JLabel("Average input/second = 0000b/s");
		averageBytesIn.setBounds(0, 25, 250, 20);
		currentBytesOut = new JLabel("Current output/second = 0000b/s");
		currentBytesOut.setBounds(0, 50, 250, 20);
		averageBytesOut = new JLabel("Average output/second = 0000b/s");
		averageBytesOut.setBounds(0, 75, 250, 20);
		tickCount = new JLabel("Ticks: 0");
		tickCount.setBounds(0, 100, 250, 20);
		
		this.add(currentBytesIn);
		this.add(averageBytesIn);
		this.add(currentBytesOut);
		this.add(averageBytesOut);
		this.add(tickCount);
		
		serverTick.scheduleAtFixedRate(this, 0, 1000, TimeUnit.MILLISECONDS);
		
	}
	
	private int ticks;
	
	public void run() {
		try {
			ticks++;
			currentBytesIn.setText("TCP Current input/second = " + Session.bytesInCurrent + "b/s");
			averageBytesIn.setText("TCP Average input/second = " + new DecimalFormat("##.##").format((float)Session.bytesIn / ticks) + "b/s");
			currentBytesOut.setText("TCP Current output/second = " + Session.bytesOutCurrent + "b/s");
			averageBytesOut.setText("TCP Average output/second = " + new DecimalFormat("##.##").format((float)Session.bytesOut / ticks) + "b/s");
			tickCount.setText("Ticks: " + ticks);
			Session.bytesInCurrent = 0;
			Session.bytesOutCurrent = 0;
		} catch(Exception e) {
			e.printStackTrace();
			serverTick.shutdown();
		}
	}
}
