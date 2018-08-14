package com.wzt.xmlparse;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ChooseFileSwing {
    private static final int XML2XLS = 0;
    private static final int XLS2XML = 1;

    public static void main(String[] args) {
        createAndShowGUI();
    }

    private static void createAndShowGUI() {
        // 确保一个漂亮的外观风格
        JFrame.setDefaultLookAndFeelDecorated(true);

        // 创建及设置窗口
        JFrame frame = new JFrame("请选择文件夹");
        frame.setSize(500, 250);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.add(panel);

        /* 布局部分我们这边不多做介绍
         * 这边设置布局为 null
         */
        panel.setLayout(null);

        // 创建 JLabel
        JLabel userLabel = new JLabel("res文件夹路径:");
        /* 这个方法定义了组件的位置。
         * setBounds(x, y, width, height)
         * x 和 y 指定左上角的新位置，由 width 和 height 指定新的大小。
         */
        userLabel.setBounds(10, 20, 100, 25);
        panel.add(userLabel);

        /*
         * 创建文本域用于用户输入
         */
        JTextField userText = new JTextField(100);
        userText.setBounds(120, 20, 300, 25);
        panel.add(userText);

        JButton resButton = new JButton("...");
        resButton.setBounds(420, 20, 20, 25);
        panel.add(resButton);
        resButton.addMouseListener(new ShowDialogListener(frame, userText, true));

        JLabel xlsDirLabel = new JLabel("xls生成文件夹路径:");
        xlsDirLabel.setBounds(10, 50, 80, 25);
        panel.add(xlsDirLabel);

        JTextField xlsDirText = new JTextField(20);
        xlsDirText.setBounds(120, 50, 300, 25);
        panel.add(xlsDirText);

        JButton xlsDirButton = new JButton("...");
        xlsDirButton.setBounds(420, 50, 20, 25);
        panel.add(xlsDirButton);
        xlsDirButton.addMouseListener(new ShowDialogListener(frame, xlsDirText, true));

        JLabel xlsLabel = new JLabel("xls文件路径:");
        xlsLabel.setBounds(10, 80, 80, 25);
        panel.add(xlsLabel);

        JTextField xlsText = new JTextField(20);
        xlsText.setBounds(120, 80, 300, 25);
        panel.add(xlsText);

        JButton xlsButton = new JButton("...");
        xlsButton.setBounds(420, 80, 20, 25);
        panel.add(xlsButton);
        xlsButton.addMouseListener(new ShowDialogListener(frame, xlsText, false));

        JLabel resDirLabel = new JLabel("res生成文件夹路径:");
        resDirLabel.setBounds(10, 110, 80, 25);
        panel.add(resDirLabel);

        JTextField resDirText = new JTextField(20);
        resDirText.setBounds(120, 110, 300, 25);
        panel.add(resDirText);

        JButton resDirButton = new JButton("...");
        resDirButton.setBounds(420, 110, 20, 25);
        panel.add(resDirButton);
        resDirButton.addMouseListener(new ShowDialogListener(frame, resDirText, true));

        JButton excel2xml = new JButton("xls转换xml");
        excel2xml.setBounds(150, 150, 100, 30);
        panel.add(excel2xml);

        JButton xml2excel = new JButton("xml转换xls");
        xml2excel.setBounds(300, 150, 100, 30);
        panel.add(xml2excel);

        JLabel resultLabel = new JLabel("");
        resultLabel.setBounds(250, 180, 100, 20);
        panel.add(resultLabel);

        excel2xml.addMouseListener(new ClickListener(xlsText, resDirText, resultLabel, XLS2XML));
        xml2excel.addMouseListener(new ClickListener(userText, xlsDirText, resultLabel, XML2XLS));


        frame.setVisible(true);
    }


    static class ShowDialogListener extends MouseAdapter {
        JFrame frame;
        JTextField userText;
        boolean isDirectory;

        public ShowDialogListener(JFrame frame, JTextField userText, boolean isDirectory) {
            this.frame = frame;
            this.userText = userText;
            this.isDirectory = isDirectory;
        }

        @Override
        public void mouseClicked(MouseEvent arg0) {
            super.mouseClicked(arg0);
            JFileChooser chooser = new JFileChooser(".");
            if (isDirectory)
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            else {
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "xls");
                chooser.setFileFilter(filter);
            }
            chooser.showOpenDialog(frame);
            if (chooser.getSelectedFile() != null) {
                String filePath = chooser.getSelectedFile().getAbsolutePath();
                userText.setText(filePath);
            }
        }
    }

    static class ClickListener extends MouseAdapter {
        JTextField fromTF;
        JTextField toDirTF;
        JLabel label;
        int type = -1;

        public ClickListener(JTextField fromTF, JTextField toDirTF, JLabel label, int type) {
            this.fromTF = fromTF;
            this.toDirTF = toDirTF;
            this.type = type;
            this.label = label;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            System.out.println("from = " + fromTF.getText() + "; toDirpath = " + toDirTF.getText());
            String fromPath = fromTF.getText();
            String toDirPath = toDirTF.getText();
            boolean result = false;
            if (type == XML2XLS)
                result = DOMTest.xml2xls(fromPath, toDirPath);
            else if (type == XLS2XML)
                result = DOMTest.xls2xml(fromPath, toDirPath);
            if (result)
                label.setText("转换成功");
            else
                label.setText("转换失败");
            super.mouseClicked(e);
        }
    }

}

