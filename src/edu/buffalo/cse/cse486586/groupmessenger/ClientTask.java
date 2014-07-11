/**
 * NAME   : ANKIT SARRAF
 * EMAIL  : sarrafan@buffalo.edu
 * ABOUT  : This class defines the Asynchronous Task for the
 *          purpose sending the message (string) to the
 *          destination AVD
 * PURPOSE: Sending messages across network is considered to be
 *          a heavy task and hence care must be taken while
 *          performing tasks in the Main or the UI Thread.
 *          Hence, this modularization has been done to enable
 *          the Main Thread to just issue the task (of sending
 *          message to other AVD) to another thread.
 *          In this manner, the Main Thread stays available for
 *          new tasks to come as soon as possible.
 * @author sarrafan
 */

package edu.buffalo.cse.cse486586.groupmessenger;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.AsyncTask;
import android.util.Log;

class ClientTask extends AsyncTask<String, Void, Void> {
	@Override
	protected Void doInBackground(String ... msgs) {
		PrintWriter printWriterOut;

		try {
			String msgToSend = msgs[0];
			String pppp = msgs[1] + "AAAAA";

			Log.e("ANKIT", "Value ::: " + pppp);
			Log.i("ANKIT", "In client");

			/**
			 * If the message has the Sequence number = -1, then only unicast it to the Sequencer
			 * Else if it has the sequence number >= 0 then it represents a message that is to be
			 * Multicast from sequencer 
			 */

			//sequenceNumber => Sequence Number of incoming messages
			int sequenceNumber = Integer.parseInt(msgToSend.substring(0, msgToSend.indexOf(":")));
			//Log.e("ANKIT", " Msg to send - " + msgToSend);

			if(sequenceNumber == -1) {
				/*
				 * This is the Message that is unicast by the AVDn to the Sequencer
				 * <-1:AVDn:Msg>
				 * -1 : It is raw message from an AVD
				 * AVDn : The source of the Message
				 * Msg : Actual Message
				 */

				Socket socket = new Socket
						(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}), 
								Integer.parseInt(Constants.REMOTE_PORT0));
				Log.e("ANKIT", "Message sent from AVD : " + msgToSend);

				printWriterOut = new PrintWriter(socket.getOutputStream(), true);
				printWriterOut.write(msgToSend);

				printWriterOut.close();
				socket.close();
			}
			else {
				/*
				 * This is the message the sequencer has serialized to multicast
				 * <S:AVDn:Msg>
				 * S : Sequence Number
				 * AVDn : The AVD which is the source
				 * Msg : Actual Message
				 */

				String [] remotePorts = {
						Constants.REMOTE_PORT0,
						Constants.REMOTE_PORT1,
						Constants.REMOTE_PORT2, 
						Constants.REMOTE_PORT3,
						Constants.REMOTE_PORT4
				};
				for(int i = 0 ; i < Constants.MAX ; i++) {
					Socket [] socket = new Socket[Constants.MAX];
					socket[i] = new Socket
							(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}), 
									Integer.parseInt(remotePorts[i]));

					printWriterOut = new PrintWriter(socket[i].getOutputStream(), true);
					printWriterOut.write(msgToSend);
					//Log.i("ANKIT", "Message Multicast from Sequencer : " + msgToSend);

					printWriterOut.close();						
					socket[i].close();
				}
			}
		} catch (UnknownHostException e) {
			Log.e("ANKIT", "ClientTask UnknownHostException");
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("ANKIT", "ClientTask socket IOException");
		}
		return null;
	}
}