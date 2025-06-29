package barcoll.server;

import barcoll.server.Maintenance;
import barcoll.server.Manager;
import barcoll.services.Service;
import tutien.services.func.Input;
import tutien.utils.Logger;
import com.girlkun.network.io.Message;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;

public class panel extends JPanel implements ActionListener {

    private JButton baotri, thaydoiexp, thaydoisk, chatserver, kickplayer, doitien;

    public panel() {
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        gbc.insets = new Insets(5, 5, 5, 5);

        baotri = createButton("BẢO TRÌ");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(baotri, gbc);

        thaydoiexp = createButton("ĐỔI EXP");
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(thaydoiexp, gbc);

        

        chatserver = createButton("CHAT ALL");
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(chatserver, gbc);

        kickplayer = createButton("ĐÁ PLAYER");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(kickplayer, gbc);

        
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.addActionListener(this);
        button.setFont(new Font("Arial", Font.ITALIC, 16));
        button.setForeground(Color.RED);
        button.setForeground(Color.blue);
        button.setFocusPainted(false);
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == baotri) {
            Maintenance.gI().start(30);
            Logger.error("Tiến Hành Bảo Trì !\n");
        } else if (e.getSource() == thaydoiexp) {
            String exp = JOptionPane.showInputDialog(this, "Bảng Exp Server\n"
                    + "Exp Server hiện tại: " + Manager.RATE_EXP_SERVER);
            if (exp != null) {
                Manager.RATE_EXP_SERVER = Byte.parseByte(exp);
                Logger.error("Exp hiện tại là: " + exp + "\n");
            }
        } else if (e.getSource() == thaydoisk) {
            String sk = JOptionPane.showInputDialog(this, "Bảng Sự Kiện\n"
                    + "Sự Kiện Server: " + Manager.SUKIEN
                    + "\n1 : Sự kiện Trung Thu"
                    + "\n2 : Sự kiện Hè"
                    + "\n3 : Sự kiện Tết"
                    + "\n4 : Sự kiện Valentine"
                    + "\n5 : Sự kiện Giỗ Tổ"
                    + "\n6 : Sự kiện Giáng Sinh"
                    + "\n7 : Sự kiện Halloween");
            if (sk != null) {
                Manager.SUKIEN = Byte.parseByte(sk);
                Logger.error("Sự Kiện: " + sk + "\n");
                if (Manager.SUKIEN == 1) {
                    Service.getInstance().sendThongBaoAllPlayer("|7|Sự kiện Trung thu đang được diễn ra"
                            + "\n|5|Thông tin chi tiết Sự kiện vui lòng xem tại NPC Trung thu tại Làng Aru");
                }
            }
        } else if (e.getSource() == chatserver) {
            String chat = JOptionPane.showInputDialog(this, "Thông Báo Server\n");
            if (chat != null) {
                Message msg = new Message(93);
                try {
                    msg.writer().writeUTF(chat);
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(Panel.class.getName()).log(Level.SEVERE, null, ex);
                }
                Service.getInstance().sendMessAllPlayer(msg);
                msg.cleanup();
                Logger.error("Thông báo: " + chat + "\n");
            }
        } else if (e.getSource() == kickplayer) {
            new Thread(() -> {
                Client.gI().close();
            }).start();
        
            
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("Barcoll");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                Panel panel = new Panel();
                frame.getContentPane().add(panel);
                frame.setSize(400, 200);
                frame.setVisible(true);
            }
        });
    }
}
