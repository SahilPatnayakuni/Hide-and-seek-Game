package sp50_egd2.game.algoCmds;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;
import java.util.function.Supplier;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import common.API.ICommCmdToModelAdapter;
import common.API.ICommunicate;
import common.messagePacket.CommMsgAlgoCmd;
import common.messagePacket.MessageDataPacket;
import provided.mixedData.MixedDataKey;
import sp50_egd2.game.messages.AddChatRoomMsg;
import sp50_egd2.game.messages.ChatRoomMsg;

public class AddChatRoomAlgoCmd extends CommMsgAlgoCmd<AddChatRoomMsg> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7716157181355665998L;

	transient private ICommCmdToModelAdapter cmd2ModelAdpt;
	
	public AddChatRoomAlgoCmd(ICommCmdToModelAdapter cmd2ModelAdpt) {
		this.cmd2ModelAdpt = cmd2ModelAdpt;
	}
	
	@Override
	public Void apply(Class<?> index, MessageDataPacket<AddChatRoomMsg, ICommunicate> host, Void... params) {
		JPanel contentPane = new JPanel();
		contentPane.setPreferredSize(new Dimension(300, 500));
		final JScrollPane scrollMiddle = new JScrollPane();
		final JPanel pnlCenter = new JPanel();
		final JPanel pnlBottom = new JPanel();
		final JTextField txtMessage = new JTextField();
		txtMessage.setPreferredSize(new Dimension(75, 25));
		final JButton btnSendMessage = new JButton("Send Text");
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		scrollMiddle.setToolTipText("middle scroll panel");
		
		contentPane.add(scrollMiddle, BorderLayout.CENTER);
		pnlCenter.setToolTipText("middle panel for dispalying");
		
		scrollMiddle.setViewportView(pnlCenter);
		pnlCenter.setLayout(new BoxLayout(pnlCenter, BoxLayout.Y_AXIS));
		pnlBottom.setToolTipText("bottom panel");
		
		contentPane.add(pnlBottom, BorderLayout.SOUTH);
		
		txtMessage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChatRoomMsg chatMsg = new ChatRoomMsg(txtMessage.getText());
				cmd2ModelAdpt.sendMessageToLocalSet(ChatRoomMsg.class, chatMsg);
				txtMessage.setText("");
			}
		});
		pnlBottom.add(txtMessage);
		
		btnSendMessage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChatRoomMsg chatMsg = new ChatRoomMsg(txtMessage.getText());
				cmd2ModelAdpt.sendMessageToLocalSet(ChatRoomMsg.class, chatMsg);
				txtMessage.setText("");
			}
		});
		btnSendMessage.setToolTipText("send message");
		
		pnlBottom.add(btnSendMessage);
		
		cmd2ModelAdpt.putInDictionary(new MixedDataKey<JPanel>(UUID.fromString("ee175b5c-62b4-4214-a4b1-396f37a619d8"), "centerPanel", JPanel.class), pnlCenter);
		cmd2ModelAdpt.putInDictionary(new MixedDataKey<JPanel>(UUID.fromString("2556e51e-4574-4b7f-a18b-a125fe04fd2a"), "contentPane", JPanel.class), contentPane);
		
		Supplier<Component> chatSupplier = new Supplier<Component>() {

			@Override
			public Component get() {
				// TODO Auto-generated method stub
				return contentPane;
			}
			
		};
		
		cmd2ModelAdpt.handleStaticComponentFactory(chatSupplier);
		
		return null;
	}

	@Override
	public void setCmd2ModelAdpt(ICommCmdToModelAdapter cmd2ModelAdpt) {
		// TODO Auto-generated method stub
		this.cmd2ModelAdpt = cmd2ModelAdpt;
	}

}
