package com.ifc.jyg;

/**
 * Created by ZLY on 2016/3/28.
 */
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;
import java.io.File;

/**
 * swing基础实例
 * @author HZ20232
 *
 */
class NewFrame extends JFrame{
    private JLabel label1;
    private JButton button1;
    private JTextField text1;
    private JComboBox box;
    private JMenuBar menuBar;
    private JSlider slider;
    private JSpinner spinner;
    private JToolBar toolBar;
    public JFileChooser exeFileChooser;
    public JFileChooser inputFileChooser;
    public NewFrame(){
        super();


        //this.getContentPane().setLayout(new FlowLayout());//设置布局控制器

//        this.getContentPane().setLayout(new GridLayout(1,2));//设置布局控制器,需要给出设定的行列数目

//        this.getContentPane().setLayout(new BorderLayout());//设置布局控制器，以North,South,West,East，来控制控件布局

        this.getContentPane().setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));//设置布局控制器


        this.setTitle("IFC提取");//设置窗口标题exeFileChooser = new JFileChooser();
        JButton button = new JButton("选择EXE");
        button.setBounds(103,110,71,27);
        button.setText("选择EXE");
        button.setToolTipText("选择EXE");
        exeFileChooser = new JFileChooser(".\\");
        inputFileChooser = new JFileChooser(".\\");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exeFileChooser.showOpenDialog(null);
            }
        });
        JButton button2 = new JButton("选择IFC");
        button2.setBounds(103,110,71,27);
        button2.setText("选择IFC");
        button2.setToolTipText("选择IFC");

        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputFileChooser.showOpenDialog(null);
            }
        });
        this.add(button,0);
        this.add(button2,1);
        this.add(this.getButton(),2);//添加按钮
        this.add(this.getLabel(),3);//添加标签
        this.add(this.getBox(),4);//添加下拉列表框
        this.add(this.getTextField(),5);


        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int width = 500;
        int height = 500;
        this.setBounds((d.width - width) / 2, (d.height - height) / 2, width, height);

        this.setSize(1000,200);

    }

    public JFileChooser getExeFileChooser() {
        return exeFileChooser;
    }

    public JToolBar getToolBar(){
        if(toolBar==null){
            toolBar = new JToolBar();
            toolBar.setBounds(103,260,71,20);
            toolBar.setFloatable(true);
        }
        return toolBar;
    }
    public JSpinner getSpinner(){
        if(spinner==null){
            spinner = new JSpinner();
            spinner.setBounds(103,220, 80,20);
            spinner.setValue(100);
        }
        return spinner;
    }
    public JSlider getSlider(){
        if(slider==null){
            slider = new JSlider();
            slider.setBounds(103,200,100, 20);
            slider.setMaximum(100);
            slider.setMinimum(0);
            slider.setOrientation(0);
            slider.setValue(0);
        }
        return slider;
    }

    /**
     * 设置下拉列表框
     * @return
     */
    public JComboBox getBox(){
        if(box==null){
            box = new JComboBox();
            box.setBounds(103,140,71,27);
//            box.addItem("1");
//            box.addItem("2");
//            box.addActionListener(new comboxListener());//为下拉列表框添加监听器类

        }
        return box;
    }
    private class comboxListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            Object o = e.getSource();
            System.out.println(o.toString());
        }
    }
    /**
     * 设置标签
     * @return 设置好的标签
     */
    public JLabel getLabel(){
        if(label1==null){
            label1 = new JLabel();
            label1.setBounds(34,49,53,18);
            label1.setText("Name");
            label1.setToolTipText("JLabel");
        }
        return label1;
    }
    /**
     * 设置按钮
     * @return 设置好的按钮
     */
    public JButton getButton(){
        if(button1==null){
            button1 = new JButton();
            button1.setBounds(103,110,71,27);
            button1.setText("提取");
            button1.setToolTipText("提取");
            //button1.addActionListener(new HelloButton());//添加监听器类，其主要的响应都由监听器类的方法实现

        }
        return button1;
    }
    /**
     * 监听器类实现ActionListener接口，主要实现actionPerformed方法
     * @author HZ20232
     *
     */
    private class HelloButton implements ActionListener{
        public void actionPerformed(ActionEvent e){
            System.out.println("Hello world!");
        }
    }
    /**
     * 设定文本域
     * @return
     */
    public JTextField getTextField(){
        if(text1==null){
            text1 = new JTextField();
            text1.setBounds(96,49,160,20);
        }
        return text1;
    }
}
